package com.rapiddweller.jdbacl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ForeignKeyPathTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() {
        ForeignKeyPath actualForeignKeyPath = new ForeignKeyPath(
                ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel()));
        assertNull(actualForeignKeyPath.getStartTable());
        assertEquals("", actualForeignKeyPath.getTablePath());
    }

    @Test
    public void testConstructor2() {
        Database createTestModelResult = AbstractModelTest.createTestModel();
        createTestModelResult.setPackagesImported(true);
        ForeignKeyPath actualForeignKeyPath = new ForeignKeyPath(ForeignKeyPath.parse("Spec", createTestModelResult));
        assertNull(actualForeignKeyPath.getStartTable());
        assertEquals("", actualForeignKeyPath.getTablePath());
    }

    @Test
    public void testConstructor3() {
        ForeignKeyPath actualForeignKeyPath = new ForeignKeyPath("Start Table");
        assertEquals("Start Table", actualForeignKeyPath.getStartTable());
        assertEquals("", actualForeignKeyPath.getTablePath());
    }

    @Test
    public void testConstructor4() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        ForeignKeyPath actualForeignKeyPath = new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertEquals(1, actualForeignKeyPath.getEndColumnNames().length);
        assertEquals(3, actualForeignKeyPath.getEdges().size());
        assertEquals("Name", actualForeignKeyPath.getStartTable());
    }

    @Test
    public void testConstructor5() {
        ForeignKeyPath actualForeignKeyPath = new ForeignKeyPath();
        assertNull(actualForeignKeyPath.getStartTable());
        assertEquals("", actualForeignKeyPath.getTablePath());
        assertTrue(actualForeignKeyPath.getEdges().isEmpty());
    }

    @Test
    public void testGetTargetTable() {
        assertNull(ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel()).getTargetTable());
    }

    @Test
    public void testGetTargetTable2() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        assertEquals("Name", (new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name")))
                .getTargetTable());
    }

    @Test
    public void testDerivePath() {
        ForeignKeyPath parseResult = ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel());
        DBTable owner = new DBTable("Name");
        ForeignKeyPath actualDerivePathResult = parseResult.derivePath(
                new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertEquals(1, actualDerivePathResult.getEndColumnNames().length);
        assertEquals(1, actualDerivePathResult.getEdges().size());
        assertEquals("Name", actualDerivePathResult.getStartTable());
    }

    @Test
    public void testDerivePath2() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        DBTable owner3 = new DBTable("Name");
        ForeignKeyPath actualDerivePathResult = foreignKeyPath.derivePath(
                new DBForeignKeyConstraint("Name", true, owner3, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertEquals(1, actualDerivePathResult.getEndColumnNames().length);
        assertEquals(4, actualDerivePathResult.getEdges().size());
        assertEquals("Name", actualDerivePathResult.getStartTable());
    }

    @Test
    public void testDerivePath3() {
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath("Start Table");
        DBTable owner = new DBTable("Name");
        thrown.expect(IllegalArgumentException.class);
        foreignKeyPath.derivePath(
                new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
    }

    @Test
    public void testDerivePath4() {
        ForeignKeyPath parseResult = ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel());
        DBTable owner = new DBTable("Name", TableType.TABLE, new DBSchema("Name"));
        ForeignKeyPath actualDerivePathResult = parseResult.derivePath(
                new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertEquals(1, actualDerivePathResult.getEndColumnNames().length);
        assertEquals(1, actualDerivePathResult.getEdges().size());
        assertEquals("Name", actualDerivePathResult.getStartTable());
    }

    @Test
    public void testDerivePath5() {
        ForeignKeyPath parseResult = ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel());
        DBTable owner = new DBTable("Name");
        ForeignKeyPath actualDerivePathResult = parseResult.derivePath(new DBForeignKeyConstraint("Name", true, owner,
                new String[]{"foo", "foo", "foo"}, new DBTable("Name"), new String[]{"foo", "foo", "foo"}));
        assertEquals(3, actualDerivePathResult.getEndColumnNames().length);
        assertEquals(1, actualDerivePathResult.getEdges().size());
        assertEquals("Name", actualDerivePathResult.getStartTable());
    }

    @Test
    public void testDerivePath6() {
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath("Start Table");
        DBTable owner = new DBTable("Name");
        thrown.expect(IllegalArgumentException.class);
        foreignKeyPath.derivePath(new DBForeignKeyConstraint("Name", true, owner, new String[]{"foo", "foo", "foo"},
                new DBTable("Name"), new String[]{"foo", "foo", "foo"}));
    }

    @Test
    public void testDerivePath7() {
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath("Name");
        DBTable owner = new DBTable("Name");
        ForeignKeyPath actualDerivePathResult = foreignKeyPath.derivePath(
                new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertEquals(1, actualDerivePathResult.getEndColumnNames().length);
        assertEquals(1, actualDerivePathResult.getEdges().size());
        assertEquals("Name", actualDerivePathResult.getStartTable());
    }

    @Test
    public void testGetIntermediates() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        assertEquals(2, (new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name")))
                .getIntermediates()
                .size());
    }

    @Test
    public void testHasIntermediate() {
        ForeignKeyPath parseResult = ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel());
        assertFalse(parseResult.hasIntermediate(new DBTable("Name")));
    }

    @Test
    public void testHasIntermediate2() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertTrue(foreignKeyPath.hasIntermediate(new DBTable("Name")));
    }

    @Test
    public void testHasIntermediate3() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("com.rapiddweller.jdbacl.model.DBColumn"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        ForeignKeyPath foreignKeyPath = new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name"));
        assertTrue(foreignKeyPath.hasIntermediate(new DBTable("Name")));
    }

    @Test
    public void testGetEndColumnNames() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        assertEquals(1, (new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name")))
                .getEndColumnNames().length);
    }

    @Test
    public void testParse() {
        ForeignKeyPath actualParseResult = ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel());
        assertNull(actualParseResult.getStartTable());
        assertEquals("", actualParseResult.getTablePath());
        assertTrue(actualParseResult.getEdges().isEmpty());
    }

    @Test
    public void testGetTablePath() {
        assertEquals("", ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel()).getTablePath());
    }

    @Test
    public void testGetTablePath2() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        assertEquals("Name, Name, Name, Name", (new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1,
                new DBForeignKeyConstraint("Name", true, owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name")))
                .getTablePath());
    }

    @Test
    public void testToString() {
        assertEquals("", ForeignKeyPath.parse("Spec", AbstractModelTest.createTestModel()).toString());
    }

    @Test
    public void testToString2() {
        DBTable owner = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint = new DBForeignKeyConstraint("Name", true, owner, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner1 = new DBTable("Name");
        DBForeignKeyConstraint dbForeignKeyConstraint1 = new DBForeignKeyConstraint("Name", true, owner1, "Fk Column Name",
                new DBTable("Name"), "Referee Column Name");
        DBTable owner2 = new DBTable("Name");
        assertEquals("Name(Fk Column Name) -> Name(Fk Column Name) -> Name(Fk Column Name) -> Name(Referee Column Name)",
                (new ForeignKeyPath(dbForeignKeyConstraint, dbForeignKeyConstraint1, new DBForeignKeyConstraint("Name", true,
                        owner2, "Fk Column Name", new DBTable("Name"), "Referee Column Name"))).toString());
    }
}

