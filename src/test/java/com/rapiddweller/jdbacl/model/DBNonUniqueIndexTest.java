package com.rapiddweller.jdbacl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DBNonUniqueIndexTest {
    @Test
    public void testConstructor() {
        DBNonUniqueIndex actualDbNonUniqueIndex = new DBNonUniqueIndex("Name", true, new DBTable("Name"), "foo", "foo",
                "foo");
        assertEquals("index", actualDbNonUniqueIndex.getObjectType());
        assertTrue(actualDbNonUniqueIndex.isNameDeterministic());
        assertEquals(3, actualDbNonUniqueIndex.getColumnNames().length);
        CompositeDBObject<?> expectedTable = actualDbNonUniqueIndex.owner;
        DBTable table = actualDbNonUniqueIndex.getTable();
        assertSame(expectedTable, table);
        assertEquals("Name", actualDbNonUniqueIndex.getName());
        assertEquals(0, table.getColumnNames().length);
    }

    @Test
    public void testAddColumnName() {
        DBNonUniqueIndex dbNonUniqueIndex = new DBNonUniqueIndex("Name", true, new DBTable("Name"), "foo", "foo", "foo");
        dbNonUniqueIndex.addColumnName("Column Name");
        assertEquals(4, dbNonUniqueIndex.getColumnNames().length);
    }

    @Test
    public void testIsIdentical() {
        DBNonUniqueIndex dbNonUniqueIndex = new DBNonUniqueIndex("Name", true, new DBTable("Name"), "foo", "foo", "foo");
        assertFalse(dbNonUniqueIndex.isIdentical(new DBCatalog()));
    }

    @Test
    public void testIsIdentical2() {
        assertFalse((new DBNonUniqueIndex("Name", true, new DBTable("Name"), "foo", "foo", "foo")).isIdentical(null));
    }
}

