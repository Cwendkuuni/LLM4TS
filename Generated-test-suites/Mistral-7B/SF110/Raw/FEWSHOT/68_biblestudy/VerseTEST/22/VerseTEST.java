package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private static final String DB_URL = "jdbc:derby:memory:testing;create=true";
    private static Database database;
    private static Connection connection;

    @Before
    public void setUp() throws SQLException {
        database = new Database(DB_URL);
        connection = database.getConnection();
        Verse.initDB(connection);
    }

    @After
    public void tearDown() throws SQLException {
        Verse.clearDB(connection);
        database.shutdown();
    }

    @Test
    public void testNewVerse() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
        assertEquals(new TranslationReference(null, new Reference(0, 0, 0)), verse.getTranslationReference());
        assertNull(verse.getText());

        String text = "And God said, Let there be light: and there was light.";
        verse = new Verse(1, text);
        assertEquals(1, verse.getId());
        assertEquals(text, verse.getText());
        assertEquals(new TranslationReference(null, new Reference(0, 0, 1)), verse.getTranslationReference());
    }

    @Test
    public void testGetVerseById() {
        Verse verse1 = new Verse(1, "In the beginning God created the heaven and the earth.");
        Verse verse2 = new Verse(2, "And the earth was without form, and void; and darkness was upon the face of the deep.");

        ArrayList verses = Verse.Get(new Translation[0], new Reference(0, 1, 1), new Reference(0, 1, 1));
        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));

        verse1 = Verse.New(1);
        verse2 = Verse.New(2);

        assertEquals(verse1, Verse.New(1));
        assertEquals(verse2, Verse.New(2));

        assertEquals(verse1, Verse.New(Verse.GetId(new Translation(1, "Test Translation"), new Book(1, "Test Book", new Reference(1, 1, 1)), 1, 1)));
        assertEquals(verse2, Verse.New(Verse.GetId(new Translation(1, "Test Translation"), new Book(1, "Test Book", new Reference(1, 1, 1)), 1, 2)));
    }

    @Test
    public void testGetVersesByRange() {
        Verse verse1 = new Verse(1, "In the beginning God created the heaven and the earth.");
        Verse verse2 = new Verse(2, "And the earth was without form, and void; and darkness was upon the face of the deep.");
        Verse verse3 = new Verse(3, "And God said, Let there be light: and there was light.");

        ArrayList verses = Verse.Get(new Translation[0], new Reference(0, 1, 1), new Reference(0, 1, 3));
        assertEquals(3, verses.size());
        assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { verse1.getId(), verse2.getId(), verse3.getId() });
    }

    @Test
    public void testGetVersesByTranslationAndBook() {
        Verse verse1 = new Verse(1, "In the beginning God created the heaven and the earth.");
        Verse verse2 = new Verse(2, "And the earth was without form, and void; and darkness was upon the face of the deep.");
        Verse verse3 = new Verse(3, "And God said, Let there be light: and there was light.");

        ArrayList verses = Verse.Get(new Translation[0], new Book(1, "Test Book", new Reference(1, 1, 1)), new Reference(0, 1, 3));
        assertEquals(3, verses.size());
        assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { verse1.getId(), verse2.getId(), verse3.getId() });
    }

    @Test
    public void testGetId() {
        Verse verse = new Verse(1, "In the beginning God created the heaven and the earth.");
        assertEquals(1, Verse.GetId(new Translation(1, "Test Translation"), new Book(1, "Test Book", new Reference(1, 1, 1)), 1, 1));
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetIdInvalidTranslation() {
        assertThrows(IllegalArgumentException.class, () -> Verse.GetId(-1, new Book(1, "Test Book", new Reference(1, 1, 1)), 1, 1));
    }

    @Test
    public void testGetIdInvalidBook() {
        assertThrows(IllegalArgumentException.class, () -> Verse.GetId(new Translation(1, "Test Translation"), null, 1, 1));
    }

    @Test
    public void testGetIdInvalidVerse() {
        assertThrows(IllegalArgumentException.class, () -> Verse.GetId(new Translation(1, "Test Translation"), new Book(1, "Test Book", new Reference(1, 1, 1)), -1, 1));
    }

    @Test
    public void testGetIdInvalidChapter() {
        assertThrows(IllegalArgumentException.class, () -> Verse.GetId(new Translation(1, "Test Translation"), new Book(1, "Test Book", new Reference(1, -1, 1)), 1, 1));
    }
}