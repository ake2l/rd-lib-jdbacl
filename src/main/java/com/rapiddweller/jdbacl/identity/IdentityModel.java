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

package com.rapiddweller.jdbacl.identity;

import com.rapiddweller.common.ArrayFormat;
import com.rapiddweller.common.Assert;
import com.rapiddweller.common.ErrorHandler;
import com.rapiddweller.common.Level;
import com.rapiddweller.common.Named;
import com.rapiddweller.common.bean.HashCodeBuilder;
import com.rapiddweller.common.iterator.TabularIterator;
import com.rapiddweller.jdbacl.ArrayResultSetIterator;
import com.rapiddweller.jdbacl.model.DBRow;
import com.rapiddweller.jdbacl.model.Database;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract parent for classes which provide information about tables
 * and features for processing the tables.<br/><br/>
 * Created: 01.09.2010 08:53:02
 *
 * @author Volker Bergmann
 * @since 0.6.4
 */
public abstract class IdentityModel implements Named {

  /**
   * The Logger.
   */
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * The Error handler.
   */
  final ErrorHandler errorHandler = new ErrorHandler("DBMerger", Level.warn);

  /**
   * The Table name.
   */
  final String tableName;
  private final Set<String> unimportantColumns;

  /**
   * Instantiates a new Identity model.
   *
   * @param tableName the table name
   */
  public IdentityModel(String tableName) {
    Assert.notNull(tableName, "tableName");
    this.tableName = tableName;
    this.unimportantColumns = new HashSet<>();
  }

  // properties ------------------------------------------------------------------------------------------------------

  /**
   * Gets table name.
   *
   * @return the table name
   */
  public String getTableName() {
    return tableName;
  }

  @Override
  public String getName() {
    return tableName;
  }

  /**
   * Add irrelevant column.
   *
   * @param unimportantColumn the unimportant column
   */
  public void addIrrelevantColumn(String unimportantColumn) {
    this.unimportantColumns.add(unimportantColumn);
  }

  // functional interface --------------------------------------------------------------------------------------------

  /**
   * Create nk pk iterator tabular iterator.
   *
   * @param connection the connection
   * @param dbId       the db id
   * @param mapper     the mapper
   * @param database   the database
   * @return the tabular iterator
   */
  public abstract TabularIterator createNkPkIterator(
      Connection connection, String dbId, KeyMapper mapper, Database database);

  /**
   * Extract nk string.
   *
   * @param nkPkTuple the nk pk tuple
   * @return the string
   */
  public String extractNK(Object[] nkPkTuple) {
    return String.valueOf(nkPkTuple[0]);
  }

  /**
   * Extract pk object.
   *
   * @param nkPkTuple the nk pk tuple
   * @return the object
   */
  public Object extractPK(Object[] nkPkTuple) {
    if (nkPkTuple.length == 2) {
      return nkPkTuple[1];
    } else if (nkPkTuple.length > 2) {
      Object[] pk = new Object[nkPkTuple.length - 1];
      System.arraycopy(nkPkTuple, 1, pk, 0, nkPkTuple.length - 1);
      return pk;
    } else {
      throw new UnsupportedOperationException("Table " + tableName + " does not have a primary key");
    }
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public abstract String getDescription();

  // non-public helpers ----------------------------------------------------------------------------------------------

  /**
   * Query tabular iterator.
   *
   * @param query      the query
   * @param connection the connection
   * @return the tabular iterator
   */
  protected TabularIterator query(String query, Connection connection) {
    Assert.notEmpty(query, "query");
    return new ArrayResultSetIterator(connection, query);
  }

  /**
   * Handle nk not found.
   *
   * @param naturalKey the natural key
   * @param tableName  the table name
   * @param sourceDbId the source db id
   * @param targetDbId the target db id
   */
  protected void handleNKNotFound(String naturalKey, String tableName, String sourceDbId, String targetDbId) {
    String message = "Missing entry: " + sourceDbId + '.' + tableName + "[" + naturalKey + "]" +
        " does not appear in " + targetDbId;
    errorHandler.handleError(message);
  }

  /**
   * Handle non equivalence.
   *
   * @param message the message
   * @param pk      the pk
   * @param entity  the entity
   */
  protected void handleNonEquivalence(String message, Object pk, DBRow entity) {
    errorHandler.handleError(message);
  }

  /**
   * Handle missing owner.
   *
   * @param ownedTableName the owned table name
   * @param ownedEntity    the owned entity
   * @param ownerTableName the owner table name
   * @param ownerId        the owner id
   * @param sourceDbId     the source db id
   */
  protected void handleMissingOwner(String ownedTableName, DBRow ownedEntity, String ownerTableName, Object ownerId,
                                    String sourceDbId) {
    String message = "Owner of " + sourceDbId + '.' + ownedTableName +
        '[' + ArrayFormat.format(ownedEntity.getPKValues()) + "] was dropped. " +
        "Missing: " + sourceDbId + '.' + ownerTableName + '[' + ownerId + "]. " +
        "Possibly it was rejected or it was missing in the NK query";
    errorHandler.handleError(message);
  }

  // java.lang.Object overrides --------------------------------------------------------------------------------------

  @Override
  public int hashCode() {
    return HashCodeBuilder.hashCode(tableName);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    IdentityModel that = (IdentityModel) obj;
    return this.tableName.equals(that.tableName);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + tableName + ")";
  }

}
