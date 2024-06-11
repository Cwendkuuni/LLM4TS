package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.DbResult;
import bible.util.Database;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }

    @Test
    public void testGetId() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Translation(translationId), new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertEquals(id, verseId);
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis", "KJV"), new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertEquals(verses.size(), 2);
    }

    @Test
    public void testGetException() {
        Translation[] translations = new Translation[0];
        List<Verse> verses = null;
        try {
            verses = Verse.Get(translations, new Book(1, "Genesis", "KJV"), new Reference(1, 1, 1), new Reference(1, 1, 3));
        } catch (Exception e) {
            assertEquals("Error in query", e.getMessage());
        }
        assertNull(verses);
    }

    @Test
    public void testGetIdFromDatabase() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Translation(translationId), new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertEquals(id, verseId);
    }

    @Test
    public void testGetIdFromDatabaseNull() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, null, new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertNull(id);
    }

    @Test
    public void testGetIdFromDatabaseInvalidTranslation() {
        int translationId = -1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Translation(translationId), new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertNull(id);
    }

    @Test
    public void testGetIdFromDatabaseInvalidBook() {
        int translationId = 1, bookId = -1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Translation(translationId), null, new Reference(bookId, chapterId, verseId));
        assertNull(id);
    }

    @Test
    public void testGetIdFromDatabaseInvalidChapter() {
        int translationId = 1, bookId = 1, chapterId = -1, verseId = 1;
        int id = Verse.GetId(translationId, new Translation(translationId), new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertNull(id);
    }

    @Test
    public void testGetIdFromDatabaseInvalidVerse() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = -1;
        int id = Verse.GetId(translationId, new Translation(translationId), new Book(bookId), new Reference(bookId, chapterId, verseId));
        assertNull(id);
    }

    @Test
    public void testNewWithDatabase() throws SQLException {
        int id = 1;
        Verse verse = Verse.New(id);
        assertEquals(id, verse.getId());
    }

    @Test
    public void testGetFromDatabase() throws SQLException {
        int id = 1;
        Verse verse = Verse.New(id);
        String text = "Test verse";
        Database database = new Database();
        DbResult result = database.Query("UPDATE Verse SET Text = '" + text + "' WHERE Verse_ID = " + id);
        List<Verse> verses = Verse.Get(new Translation[0], new Book(1, "Genesis", "KJV"), new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(verses.size(), 1);
        assertEquals(verses.get(0).getText(), text);
        database.Close();
    }

    @Test(expected = SQLException.class)
    public void testGetFromDatabaseException() throws SQLException {
        List<Verse> verses = Verse.Get(new Translation[0], new Book(1, "Genesis", "KJV"), new Reference(1, 1, 1), new Reference(1, 1, 1));
        fail("Expected SQLException");
    }

    @Test
    public void testToString() {
        int id = 1;
        String text = "Test verse";
        TranslationReference transRef = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
        Verse verse = new Verse(id, text, transRef);
        String expected = "Verse:[1 Test verse TranslationReference[Translation:[1 KJV Reference[Book:[Genesis 1 1]] Verse:1]]]";
        assertEquals(expected, verse.toString());
    }

    @Test
    public void testGetTranslationReference() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        Verse verse = new Verse(translationId, "", new TranslationReference(new Translation(translationId), new Reference(bookId, chapterId, verseId)));
        TranslationReference expected = new TranslationReference(new Translation(translationId), new Reference(bookId, chapterId, verseId));
        assertEquals(expected, verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        int bookId = 1, chapter = 1, verse = 1;
        Reference reference = new Reference(bookId, chapter, verse);
        Verse verse = new Verse(1, "", new TranslationReference(new Translation(1), reference));
        Reference expected = reference;
        assertEquals(expected, verse.getReference());
    }

    @Test
    public void testConstructorWithResult() throws SQLException {
        DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = 1");
        Verse verse = new Verse(result);
        int id = result.getInt("Verse_ID");
        assertEquals(id, verse.getId());
    }

    @Test
    public void testConstructorWithEmptyResult() throws SQLException {
        DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = -1");
        Verse verse = null;
        try {
            verse = new Verse(result);
        } catch (SQLException e) {
        }
        assertNull(verse);
    }
}