package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.VerseTest;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Reference;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;

        int actualId = Verse.getId(translationId, new Translation(translationId), new Book(bookId), new Chapter(chapterId), new Verse(chapterId, verseId));
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }

    @Test(expected = SQLException.class)
    public void testGet_InvalidQuery() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);

        Verse.Get(translations, null, null);
    }

    @Test
    public void testGet_EmptyResult() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGet_SingleResult() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }

    @Test
    public void testGet_MultipleResults() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1);
        translations[1] = new Translation(2);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);

        List<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 null nullYour class name here:[1 null Your class name here:[Your class name here:[1  null]]]";
        String actual = verse.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetIdFromDatabase() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;

        DbResult result = Database.Query("select Verse_ID from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId);
        result.next();
        int actualId = result.getInt("Verse_ID");
        assertEquals(expectedId, actualId);
    }

    @Test(expected = SQLException.class)
    public void testGetIdFromDatabase_EmptyResult() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        DbResult result = Database.Query("select Verse_ID from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId);
        result.next();

        if (result.next()) {
            throw new RuntimeException("Unexpected result.");
        }
    }

    @Test
    public void testComplete() throws SQLException {
        Verse verse = new Verse(1);

        DbResult result = Database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1");
        result.next();

        verse.complete(result);

        assertEquals(1, verse.getId());
        assertEquals("Your text here", verse.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse.getTranslationReference());
    }
}