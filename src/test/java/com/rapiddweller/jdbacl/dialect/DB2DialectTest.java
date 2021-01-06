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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the {@link DB2Dialect}.<br/><br/>
 * Created: 10.11.2009 17:33:48
 *
 * @author Volker Bergmann
 * @since 0.6.0
 */
public class DB2DialectTest extends DatabaseDialectTest<DB2Dialect> {

    @Test
    public void testConstructor() {
        DB2Dialect actualDb2Dialect = new DB2Dialect();
        assertEquals("db2", actualDb2Dialect.getSystem());
        assertFalse(actualDb2Dialect.quoteTableNames);
        assertTrue(actualDb2Dialect.isSequenceSupported());
    }

    @Test
    public void testRenderFetchSequenceValue() {
        assertEquals("select nextval for Sequence Name from sysibm.sysdummy1",
                (new DB2Dialect()).renderFetchSequenceValue("Sequence Name"));
        assertEquals("select nextval for sysdummy1 from sysibm",
                (new DB2Dialect()).renderFetchSequenceValue("sysibm.sysdummy1"));
    }

    public DB2DialectTest() {
        super(new DB2Dialect());
    }

    @Test
    public void testnextSequenceValue() {
        assertEquals("select nextval for SEQ from sysibm.sysdummy1", dialect.renderFetchSequenceValue("SEQ"));
    }

    @Test
    public void testDropSequence() {
        assertEquals("drop sequence SEQ", dialect.renderDropSequence("SEQ"));
    }

    @Test
    public void testFormatDate() {
        assertEquals("'1971-02-03'", dialect.formatValue(DATE_19710203));
    }

    @Test
    public void testFormatDatetime() {
        assertEquals("'1971-02-03 13:14:15'", dialect.formatValue(DATETIME_19710203131415));
    }

    @Test
    public void testFormatTime() {
        assertEquals("'13:14:15'", dialect.formatValue(TIME_131415));
    }

    @Test
    public void testFormatTimestamp() {
        assertEquals("'1971-02-03 13:14:15.123456789'",
                dialect.formatValue(TIMESTAMP_19710203131415123456789));
    }

    @Test
    public void testIsDeterministicPKName() {
        assertFalse(dialect.isDeterministicPKName("SQL110710154222500"));
        assertTrue(dialect.isDeterministicPKName("USER_PK"));
        assertTrue((new DB2Dialect()).isDeterministicPKName("Pk Name"));
        assertFalse((new DB2Dialect()).isDeterministicPKName("SQL999999999999999"));
    }

    @Test
    public void testIsDeterministicUKName() {
        assertFalse(dialect.isDeterministicUKName("SQL110710154222560"));
        assertTrue(dialect.isDeterministicUKName("USER_NAME_UK"));
        assertTrue((new DB2Dialect()).isDeterministicUKName("Uk Name"));
        assertFalse((new DB2Dialect()).isDeterministicUKName("SQL999999999999999"));
    }

    @Test
    public void testIsDeterministicFKName() {
        assertFalse(dialect.isDeterministicFKName("SQL110710154222560"));
        assertTrue(dialect.isDeterministicFKName("USER_ROLE_FK"));
        assertTrue((new DB2Dialect()).isDeterministicFKName("Fk Name"));
        assertFalse((new DB2Dialect()).isDeterministicFKName("SQL999999999999999"));
    }

    @Test
    public void testIsDeterministicIndexName() {
        assertFalse(dialect.isDeterministicIndexName("SQL110710154222480"));
        assertTrue(dialect.isDeterministicIndexName("USER_NAME_IDX"));
        assertTrue((new DB2Dialect()).isDeterministicIndexName("Index Name"));
        assertFalse((new DB2Dialect()).isDeterministicIndexName("SQL999999999999999"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegex() {
        assertFalse(dialect.supportsRegex());
        dialect.regexQuery("code", false, "[A-Z]{4}");
    }

    @Test
    public void testRenderCase() {
        assertEquals("CASE WHEN condition1 THEN result1 WHEN condition2 THEN result2 ELSE result4 END AS col",
                dialect.renderCase("col", "result4", "condition1", "result1", "condition2", "result2"));
    }

}
