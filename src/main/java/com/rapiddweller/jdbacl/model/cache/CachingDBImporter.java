/*
 * (c) Copyright 2010-2012 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.rapiddweller.jdbacl.model.cache;

import com.rapiddweller.common.ConnectFailedException;
import com.rapiddweller.common.FileUtil;
import com.rapiddweller.common.ImportFailedException;
import com.rapiddweller.common.Period;
import com.rapiddweller.common.StringUtil;
import com.rapiddweller.common.SystemInfo;
import com.rapiddweller.jdbacl.model.DBMetaDataImporter;
import com.rapiddweller.jdbacl.model.Database;
import com.rapiddweller.jdbacl.model.jdbc.JDBCDBImporter;
import com.rapiddweller.jdbacl.model.xml.XMLModelExporter;
import com.rapiddweller.jdbacl.model.xml.XMLModelImporter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * {@link DBMetaDataImporter} that acts as a proxy to another DBMetaDataImporter,
 * adding the feature of caching its output. The data file is named '&lt;environment&gt;.meta.xml'
 * and expires after 12 hrs.<br/><br/>
 * Created: 10.01.2011 14:48:00
 *
 * @author Volker Bergmann
 * @since 0.6.5
 */
public class CachingDBImporter implements DBMetaDataImporter, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(CachingDBImporter.class);

  /**
   * The constant TIME_TO_LIVE_SYSPROP.
   */
  public static final String TIME_TO_LIVE_SYSPROP = "jdbacl.cache.timetolive";
  /**
   * The constant DEFAULT_TIME_TO_LIVE.
   */
  public static final long DEFAULT_TIME_TO_LIVE = Period.HOUR.getMillis() * 12;

  private static final String CACHE_FILE_SUFFIX = ".meta.xml";

  /**
   * The Real importer.
   */
  protected final JDBCDBImporter realImporter;
  /**
   * The Environment.
   */
  protected final String environment;

  /**
   * Instantiates a new Caching db importer.
   *
   * @param realImporter the real importer
   * @param environment  the environment
   */
  public CachingDBImporter(JDBCDBImporter realImporter, String environment) {
    this.realImporter = realImporter;
    this.environment = environment;
  }

  @Override
  public Database importDatabase() throws ConnectFailedException, ImportFailedException {
    File file = getCacheFile();
    long now = System.currentTimeMillis();
    long timeToLive = getTimeToLive();
    if (file.exists() && (timeToLive < 0 || now - file.lastModified() < timeToLive)) {
      return readCachedData(file);
    } else {
      return importFreshData(file);
    }
  }

  private static long getTimeToLive() {
    String sysProp = System.getProperty(TIME_TO_LIVE_SYSPROP);
    if (!StringUtil.isEmpty(sysProp)) {
      long scale = 1;
      if (sysProp.endsWith("d")) {
        scale = Period.DAY.getMillis();
        sysProp = sysProp.substring(0, sysProp.length() - 1);
      }
      return Long.parseLong(sysProp) * scale;
    } else {
      return DEFAULT_TIME_TO_LIVE;
    }
  }

  @Override
  public void close() throws IOException {
    if (realImporter != null) {
      ((Closeable) realImporter).close();
    }
  }

  /**
   * Gets cache file.
   *
   * @param environment the environment
   * @return the cache file
   */
  public static File getCacheFile(String environment) {
    String fileSeparator = File.separator;
    String cacheDirName = SystemInfo.getUserHome() + fileSeparator + "rapiddweller" + fileSeparator + "cache";
    String cacheFileName = environment + CACHE_FILE_SUFFIX;
    return FileUtil.getFileIgnoreCase(new File(cacheDirName, cacheFileName), false);
  }

  // non-public helpers ----------------------------------------------------------------------------------------------

  /**
   * Gets cache file.
   *
   * @return the cache file
   */
  protected File getCacheFile() {
    return getCacheFile(environment);
  }

  /**
   * Read cached data database.
   *
   * @param cacheFile the cache file
   * @return the database
   * @throws ConnectFailedException the connect failed exception
   * @throws ImportFailedException  the import failed exception
   */
  protected Database readCachedData(File cacheFile) throws ConnectFailedException, ImportFailedException {
    try {
      LOGGER.info("Importing database meta data from cache file " + cacheFile.getPath());
      Database database = new XMLModelImporter(cacheFile, realImporter).importDatabase();
      LOGGER.info("Database meta data import completed");
      return database;
    } catch (Exception e) {
      LOGGER.info("Error reading cache file, reparsing database", e);
      return importFreshData(cacheFile);
    }
  }

  /**
   * Import fresh data database.
   *
   * @param file the file
   * @return the database
   * @throws ConnectFailedException the connect failed exception
   * @throws ImportFailedException  the import failed exception
   */
  protected Database importFreshData(File file) throws ConnectFailedException, ImportFailedException {
    Database database = realImporter.importDatabase();
    return writeCacheFile(file, database);
  }

  /**
   * Update cache file.
   *
   * @param database the database
   */
  public static void updateCacheFile(Database database) {
    if (database == null) {
      throw new IllegalArgumentException("database is null");
    }
    String environment = database.getEnvironment();
    if (environment != null) {
      File cacheFile = getCacheFile(environment);
      writeCacheFile(cacheFile, database);
    }
  }

  /**
   * Write cache file database.
   *
   * @param file     the file
   * @param database the database
   * @return the database
   */
  public static Database writeCacheFile(File file, Database database) {
    LOGGER.info("Exporting Database meta data of " + database.getEnvironment() + " to cache file");
    try {
      FileUtil.ensureDirectoryExists(file.getParentFile());
      new XMLModelExporter(file).export(database);
      LOGGER.debug("Database meta data export completed");
    } catch (Exception e) {
      LOGGER.error("Error writing database meta data file " + ": " + e.getMessage(), e);
    }
    return database;
  }

}
