/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

package com.rapiddweller.jdbacl.dialect;

import com.rapiddweller.jdbacl.DBUtil;
import com.rapiddweller.jdbacl.DatabaseTestUtil;
import com.rapiddweller.jdbacl.model.DBSequence;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the PostgreSQLDialect.<br/><br/>
 * Created: 10.11.2009 18:18:51
 *
 * @author Volker Bergmann
 * @since 0.6.0
 */
public class PostgreSQLDialectTest extends DatabaseDialectTest<PostgreSQLDialect> {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    PostgreSQLDialect actualPostgreSQLDialect = new PostgreSQLDialect();
    assertEquals("postgres", actualPostgreSQLDialect.getSystem());
    assertTrue(actualPostgreSQLDialect.quoteTableNames);
    assertTrue(actualPostgreSQLDialect.isSequenceBoundarySupported());
  }

  /**
   * Test sequence no cycle.
   */
  @Test
  public void testSequenceNoCycle() {
    assertEquals("NO CYCLE", (new PostgreSQLDialect()).sequenceNoCycle());
  }

  /**
   * Instantiates a new Postgre sql dialect test.
   */
  public PostgreSQLDialectTest() {
    super(new PostgreSQLDialect());
  }

  /**
   * Test format date.
   */
  @Test
  public void testFormatDate() {
    assertEquals("date '1971-02-03'", dialect.formatValue(DATE_19710203));
  }

  /**
   * Test format datetime.
   */
  @Test
  public void testFormatDatetime() {
    assertEquals("timestamp '1971-02-03 13:14:15'", dialect.formatValue(DATETIME_19710203131415));
  }

  /**
   * Test format time.
   */
  @Test
  public void testFormatTime() {
    assertEquals("time '13:14:15'", dialect.formatValue(TIME_131415));
  }

  /**
   * Test format timestamp.
   */
  @Test
  public void testFormatTimestamp() {
    assertEquals("timestamp '1971-02-03 13:14:15.123456789'",
        dialect.formatValue(TIMESTAMP_19710203131415123456789));
  }

  /**
   * Test is deterministic pk name.
   */
  @Test
  public void testIsDeterministicPKName() {
    assertTrue(dialect.isDeterministicPKName("USER_PK"));
  }

  /**
   * Test is deterministic uk name.
   */
  @Test
  public void testIsDeterministicUKName() {
    assertTrue(dialect.isDeterministicUKName("USER_NAME_UK"));
  }

  /**
   * Test is deterministic fk name.
   */
  @Test
  public void testIsDeterministicFKName() {
    assertTrue(dialect.isDeterministicFKName("USER_ROLE_FK"));
  }

  /**
   * Test is deterministic index name.
   */
  @Test
  public void testIsDeterministicIndexName() {
    assertTrue(dialect.isDeterministicIndexName("USER_NAME_IDX"));
  }

  /**
   * Test supports regex.
   */
  @Test
  public void testSupportsRegex() {
    assertTrue((new PostgreSQLDialect()).supportsRegex());
  }

  /**
   * Test regex query.
   */
  @Test
  public void testRegexQuery() {
    assertEquals("NOT Expression ~ 'Regex'", (new PostgreSQLDialect()).regexQuery("Expression", true, "Regex"));
    assertEquals("Expression ~ 'Regex'", (new PostgreSQLDialect()).regexQuery("Expression", false, "Regex"));
  }

  /**
   * Test regex.
   */
  @Test
  public void testRegex() {
    assertTrue(dialect.supportsRegex());
    assertEquals("code ~ '[A-Z]{5}'", dialect.regexQuery("code", false, "[A-Z]{5}"));
    assertEquals("NOT code ~ '[A-Z]{5}'", dialect.regexQuery("code", true, "[A-Z]{5}"));
  }

  /**
   * Test render create sequence.
   */
  @Test
  public void testRenderCreateSequence() {
    assertEquals("CREATE SEQUENCE my_seq", dialect.renderCreateSequence(new DBSequence("my_seq", null)));
    assertEquals("CREATE SEQUENCE my_seq START WITH 10 INCREMENT BY 2 MAXVALUE 999 MINVALUE 5 CYCLE CACHE 3",
        dialect.renderCreateSequence(createConfiguredSequence()));
  }

  /**
   * Test render create sequence 2.
   */
  @Test
  public void testRenderCreateSequence2() {
    DBSequence sequence = new DBSequence("Name", null);
    assertEquals("CREATE SEQUENCE Name", (new PostgreSQLDialect()).renderCreateSequence(sequence));
  }

  /**
   * Test render create sequence 3.
   */
  @Test
  public void testRenderCreateSequence3() {
    DBSequence dbSequence = new DBSequence("Name", null);
    dbSequence.setCache(0L);
    assertEquals("CREATE SEQUENCE Name CACHE 0", (new PostgreSQLDialect()).renderCreateSequence(dbSequence));
  }

  /**
   * Test is default catalog.
   */
  @Test
  public void testIsDefaultCatalog() {
    assertFalse((new PostgreSQLDialect()).isDefaultCatalog("Catalog", "User"));
    assertTrue((new PostgreSQLDialect()).isDefaultCatalog("", "User"));
    assertTrue((new PostgreSQLDialect()).isDefaultCatalog("Catalog", "Catalog"));
  }

  /**
   * Test is default schema.
   */
  @Test
  public void testIsDefaultSchema() {
    assertFalse((new PostgreSQLDialect()).isDefaultSchema("Schema", "User"));
    assertTrue((new PostgreSQLDialect()).isDefaultSchema("public", "User"));
  }

  /**
   * Test set next sequence value.
   *
   * @throws Exception the exception
   */
  @Test // requires a PostgreSQL installation configured as environment named 'postgres'
  public void testSetNextSequenceValue() throws Exception {
    if (DatabaseTestUtil.getConnectData("postgres") == null) {
      logger.warn("Skipping test " + getClass() + ".testSetNextSequenceValue() since there is no 'postgres' " +
          "environment defined or online");
      return;
    }
    Connection connection = DBUtil.connect("postgres", false);
    String sequenceName = getClass().getSimpleName();
    try {
      DBUtil.executeUpdate("create sequence " + sequenceName, connection);
      dialect.setNextSequenceValue(sequenceName, 123, connection);
      String seqValQuery = dialect.renderFetchSequenceValue(sequenceName);
      assertEquals(123L, DBUtil.queryScalar(seqValQuery, connection));
    } finally {
      DBUtil.executeUpdate("drop sequence " + sequenceName, connection);
    }
  }

  /**
   * Test render fetch sequence value.
   */
  @Test
  public void testRenderFetchSequenceValue() {
    assertEquals("select nextval('SEQ')", dialect.renderFetchSequenceValue("SEQ"));
    assertEquals("select nextval('Sequence Name')",
        (new PostgreSQLDialect()).renderFetchSequenceValue("Sequence Name"));
  }

  /**
   * Test format timestamp 2.
   */
  @Test
  public void testFormatTimestamp2() {
    Timestamp timestamp = new Timestamp(10L);
    assertNotNull((new PostgreSQLDialect()).formatTimestamp(timestamp));
  }

  /**
   * Test drop sequence.
   */
  @Test
  public void testDropSequence() {
    assertEquals("drop sequence SEQ", dialect.renderDropSequence("SEQ"));
  }

  /**
   * Test render case.
   */
  @Test
  public void testRenderCase() {
    assertEquals("CASE WHEN condition1 THEN result1 WHEN condition2 THEN result2 ELSE result4 END AS col",
        dialect.renderCase("col", "result4", "condition1", "result1", "condition2", "result2"));
  }

}
