/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

package com.rapiddweller.jdbacl;

import com.rapiddweller.common.BeanUtil;
import com.rapiddweller.common.IOUtil;
import com.rapiddweller.common.exception.ExceptionFactory;
import com.rapiddweller.common.file.FileResourceNotFoundException;
import com.rapiddweller.common.version.VersionNumber;
import com.rapiddweller.jdbacl.dialect.UnknownDialect;

import java.util.Map;

/**
 * Manages {@link DatabaseDialect}s.<br/><br/>
 * Created: 18.02.2010 16:32:55
 * @author Volker Bergmann
 * @since 0.6.0
 */
public class DatabaseDialectManager {

  private static final String FILENAME = "com/rapiddweller/jdbacl/databene.db_dialect.properties";

  private static final Map<String, String> mappings;

  static {
    try {
      mappings = IOUtil.readProperties(FILENAME);
    } catch (FileResourceNotFoundException e) {
      throw ExceptionFactory.getInstance().deploymentFailed(
          "Database dialect configuration file not found: " + FILENAME, e);
    } catch (Exception e) {
      throw ExceptionFactory.getInstance().deploymentFailed(
          "Error processing Database dialect configuration file: " + FILENAME, e);
    }
  }

  private DatabaseDialectManager() {
    // private constructor to prevent instantiation of this utility class
  }

  /** Provides the dialect for a product in a certain version.
   *  @param productName the product name
   *  @param version     if no version is specified, the newest one is assumed */
  public static DatabaseDialect getDialectForProduct(String productName, VersionNumber version) {
    String normalizedProductName = productName.toLowerCase().replace(' ', '_');
    for (Map.Entry<String, String> entry : mappings.entrySet()) {
      String[] tokens = entry.getKey().split(" ");
      String p = tokens[0];
      String v = null;
      if (tokens.length == 2) {
        v = tokens[1];
      }
      if (normalizedProductName.contains(p) && (v == null || version == null || version.compareTo(VersionNumber.valueOf(v)) >= 0)) {
        return (DatabaseDialect) BeanUtil.newInstance(entry.getValue());
      }
    }
    return new UnknownDialect(productName);
  }

}
