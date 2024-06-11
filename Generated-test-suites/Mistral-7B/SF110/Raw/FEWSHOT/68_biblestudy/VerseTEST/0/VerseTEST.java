package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private static Connection connection;
    private static Statement statement;
    private static Map<Integer, Map<Integer, Map<Integer, Verse>>> cache;

    @Before
    public void setUp() throws SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        statement = connection.createStatement();
        cache = new HashMap<>();
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Verse (" +
                "Verse_ID INTEGER PRIMARY KEY," +
                "Text TEXT," +
                "Translation_ID INTEGER," +
                "Book_ID INTEGER," +
                "Chapter INTEGER," +
                "Verse_No INTEGER)";
        statement.executeUpdate(createTableQuery);
        insertTestData();
    }

    @After
    public void tearDown() throws SQLException {
        statement.executeUpdate("DELETE FROM Verse");
        statement.close();
        connection.close();
    }

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        assertNotNull(verse1);
        assertEquals(1, verse1.getId());
        assertEquals("Test Text 1", verse1.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse1.getTranslationReference());

        Verse verse2 = Verse.New(2);
        assertNotNull(verse2);
        assertEquals(2, verse2.getId());
        assertEquals("Test Text 2", verse2.getText());
        assertEquals(new TranslationReference(new Translation(2), new Reference(2, 1, 1)), verse2.getTranslationReference());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(new Translation(1), new Book(1), 1, 1);
        assertEquals(1, id);

        id = Verse.GetId(new Translation(2), new Book(1), 1, 1);
        assertEquals(2, id);

        id = Verse.GetId(new Translation(1), new Book(2), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGet() {
        List<Verse> verses1 = Verse.Get(new Translation[] { new Translation(1) }, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(2, verses1.size());
        assertTrue(verses1.contains(new Verse(1)));
        assertTrue(verses1.contains(new Verse(2)));

        List<Verse> verses2 = Verse.Get(new Translation[] { new Translation(2) }, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(0, verses2.size());

        List<Verse> verses3 = Verse.Get(new Translation[] { new Translation(1), new Translation(2) }, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(2, verses3.size());
    }

    @Test
    public void testGetIdFromCache() {
        Verse verse1 = new Verse(1);
        assertNotNull(verse1);
        assertEquals(1, verse1.getId());
        assertEquals("Test Text 1", verse1.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse1.getTranslationReference());

        Verse verse2 = Verse.New(1);
        assertNotNull(verse2);
        assertEquals(1, verse2.getId());
        assertEquals("Test Text 1", verse2.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse2.getTranslationReference());

        assertEquals(verse1, verse2); // They should reference the same object in the cache
    }

    @Test
    public void testGetFromDatabase() throws SQLException {
        Verse verse1 = new Verse(3);
        assertNotNull(verse1);
        assertEquals(3, verse1.getId());
        assertEquals("Test Text 3", verse1.getText());
        assertEquals(new TranslationReference(new Translation(3), new Reference(3, 1, 1)), verse1.getTranslationReference());

        Verse verse2 = Verse.New(3);
        assertNotNull(verse2);
        assertEquals(3, verse2.getId());
        assertEquals("Test Text 3", verse2.getText());
        assertEquals(new TranslationReference(new Translation(3), new Reference(3, 1, 1)), verse2.getTranslationReference());

        assertEquals(verse1, verse2); // They should reference the same object loaded from the database
    }

    @Test
    public void testGetIdFromDatabase() throws SQLException {
        int id = Verse.GetId(new Translation(3), new Book(3), 1, 1);
        assertEquals(3, id);

        id = Verse.GetId(new Translation(4), new Book(3), 1, 1);
        assertEquals(-1, id);
    }

    private void insertTestData() throws SQLException {
        String insertQuery1 = "INSERT INTO Verse (Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES (1, 'Test Text 1', 1, 1, 1, 1)";
        String insertQuery2 = "INSERT INTO Verse (Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES (2, 'Test Text 2', 1, 1, 1, 1)";
        String insertQuery3 = "INSERT INTO Verse (Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES (3, 'Test Text 3', 3, 3, 1, 1)";
        statement.executeUpdate(insertQuery1);
        statement.executeUpdate(insertQuery2);
        statement.executeUpdate(insertQuery3);
    }
}