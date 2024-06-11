package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private static final String DB_URL = "jdbc:derby:test;create=true";
    private Connection connection;
    private Statement statement;

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS Verse (" + Verse.COLUMNS + ")");
    }

    @After
    public void tearDown() throws SQLException {
        statement.executeUpdate("DROP TABLE Verse");
        DbResult.Close(statement);
        DbResult.Close(connection);
    }

    @Test
    public void testNewVerse() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertEquals(id, verse.getId());
        assertNotNull(verse.getText());
        assertNull(verse.getTranslationReference());
    }

    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;
        int actualId = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetVersesByTranslationAndRange() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int beginVerse = 1;
        int endVerse = 2;

        List<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(new Verse(1));
        expectedVerses.add(new Verse(2));

        List<Verse> actualVerses = Verse.Get(new Translation[]{new Translation(translationId)}, new Reference(bookId, chapterId, beginVerse), new Reference(bookId, chapterId, endVerse));
        assertEquals(expectedVerses.size(), actualVerses.size());
        for (int i = 0; i < expectedVerses.size(); i++) {
            assertEquals(expectedVerses.get(i).getId(), actualVerses.get(i).getId());
        }
    }

    @Test
    public void testGetVersesByTranslationAndInvalidRange() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int beginVerse = 2;
        int endVerse = 1;

        List<Verse> actualVerses = Verse.Get(new Translation[]{new Translation(translationId)}, new Reference(bookId, chapterId, beginVerse), new Reference(bookId, chapterId, endVerse));
        assertTrue(actualVerses.isEmpty());
    }

    @Test
    public void testGetVersesByInvalidTranslation() {
        int bookId = 1;
        int chapterId = 1;
        int beginVerse = 1;
        int endVerse = 1;

        List<Verse> actualVerses = Verse.Get(new Translation[]{null}, new Reference(bookId, chapterId, beginVerse), new Reference(bookId, chapterId, endVerse));
        assertTrue(actualVerses.isEmpty());
    }

    @Test
    public void testGetVerseById() throws SQLException {
        int id = 1;
        String text = "Lorem ipsum";
        String translation = "New International Version";
        String book = "John";
        int chapter = 1;
        int verse = 1;

        statement.executeUpdate("INSERT INTO Verse(Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES(" + id + ", '" + text + "', " + Translation.getId(translation) + ", " + book.hashCode() + ", " + chapter + ", " + verse + ")");

        Verse verse = Verse.New(id);
        assertEquals(text, verse.getText());
        assertEquals(new TranslationReference(new Translation(translationId), new Reference(book.hashCode(), chapter, verse)), verse.getTranslationReference());

        DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = " + id);
        Verse dbVerse = new Verse(result);
        assertEquals(dbVerse.getText(), verse.getText());
        assertEquals(dbVerse.getTranslationReference(), verse.getTranslationReference());
    }

    @Test
    public void testGetVerseByIdNotFound() throws SQLException {
        int id = 1;
        Verse verse = Verse.New(id);
        assertNull(verse);

        DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = " + id);
        assertNull(result.next());
    }

    @Test(expected = SQLException.class)
    public void testGetVerseByInvalidId() throws SQLException {
        int id = -1;
        Verse verse = Verse.New(id);
        fail();

        DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = " + id);
        assertNull(result.next());
    }
}