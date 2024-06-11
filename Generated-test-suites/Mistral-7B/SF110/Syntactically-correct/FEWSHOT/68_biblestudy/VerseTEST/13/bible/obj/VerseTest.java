package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.sql.DbResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test(expected = NullPointerException.class)
    public void testNewNullId() {
        Verse.New(null);
    }

    @Test
    public void testGetId() {
        Verse verse = new Verse(1);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getText());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNull(verse.getTranslationReference());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 null null]";
        assertEquals(expected, verse.toString());
    }

    @Test
    public void testGetIdFromTranslationBookChapterAndVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, Verse.GetId(translationId, bookId, chapterId, verseId));
    }

    @Test
    public void testGetFromTranslationArrayAndReference() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);

        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
        Verse verse = verses.get(0);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetFromTranslationArrayAndNullBeginEnd() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);

        List<Verse> verses = Verse.Get(translations, null, null);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetFromTranslationArrayAndEmptyBeginEnd() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);

        Reference begin = new Reference();
        Reference end = new Reference();

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetFromDatabase() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        Verse verse = new Verse(id);

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId));
        assertEquals(1, verses.size());
        Verse verseFromDatabase = verses.get(0);
        assertNotSame(verse, verseFromDatabase);
        assertEquals(id, verseFromDatabase.getId());
    }

    @Test
    public void testGetIdFromDatabase() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        Verse verse = new Verse(id);

        int idFromDatabase = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, idFromDatabase);
    }

    @Test
    public void testGetFromDatabaseEmptyResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetFromDatabaseNullResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        DbResult result = null;
        List<Verse> verses = Verse.Get(new Translation[0], new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId));
        assertTrue(verses.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testGetFromDatabaseInvalidQuery() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId + " and Invalid_Column = 'test'";
        List<Verse> verses = Verse.Get(new Translation[0], new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId));
    }

    @Test
    public void testNewWithDatabaseResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + verseId;
        DbResult result = Database.Query(query);

        Verse verse = Verse.New(verseId);
        assertNotNull(verse);
        assertEquals(verseId, verse.getId());

        Verse verseFromDatabase = new Verse(result);
        assertNotSame(verse, verseFromDatabase);
        assertEquals(verseId, verseFromDatabase.getId());
        assertEquals(verse.getText(), verseFromDatabase.getText());
        assertEquals(verse.getTranslationReference(), verseFromDatabase.getTranslationReference());
    }

    @Test
    public void testReadId() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(id, Verse.GetId(translationId, bookId, chapterId, verseId));
    }

    @Test
    public void testReadIdNullResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        DbResult result = null;
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, id);
    }

    @Test
    public void testReadIdEmptyResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        DbResult result = new DbResult() {
            @Override
            public boolean next() throws SQLException {
                return false;
            }
        };
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, id);
    }

    @Test
    public void testGetStaticFields() {
        assertEquals(Verse.COLUMNS, Verse.COLUMNS);
        assertNotNull(Verse.translations);
        assertNotNull(Verse.verseCache);
    }
}