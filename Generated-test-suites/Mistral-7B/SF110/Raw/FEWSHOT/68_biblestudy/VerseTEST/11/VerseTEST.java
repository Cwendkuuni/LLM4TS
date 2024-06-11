package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
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
        assertEquals("", verse.getText());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNull(verse.getTranslationReference());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertEquals("bible.obj.Verse:[1 null ]", verse.toString());
    }

    @Test
    public void testGetIdTranslationBookChapterVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, Verse.GetId(translationId, bookId, chapterId, verseId));
    }

    @Test
    public void testGetEmpty() {
        Translation[] translations = new Translation[0];
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetSingle() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        Verse expectedVerse = new Verse(1);
        assertEquals(expectedVerse, verses.get(0));
    }

    @Test
    public void testGetMultiple() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1, "English");
        translations[1] = new Translation(2, "Spanish");
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(2, verses.size());
        for (int i = 0; i < verses.size(); i++) {
            Verse verse = (Verse) verses.get(i);
            assertEquals(i + 1, verse.getId());
        }
    }

    @Test
    public void testGetWithBeginEnd() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 5));
        assertEquals(5, verses.size());
        for (int i = 0; i < verses.size(); i++) {
            Verse verse = (Verse) verses.get(i);
            int expectedId = i + 1;
            assertEquals(expectedId, verse.getId());
        }
    }

    @Test
    public void testGetWithEmptyBeginEnd() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, null, null);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithInvalidBeginEnd() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 0));
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetId() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;
        Verse verse = new Verse(expectedId);
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdInvalidTranslationId() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        Verse.GetId(translationId, bookId, chapterId, verseId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdInvalidBookId() {
        int translationId = 1;
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;
        Verse.GetId(translationId, bookId, chapterId, verseId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdInvalidChapterId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = -1;
        int verseId = 1;
        Verse.GetId(translationId, bookId, chapterId, verseId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdInvalidVerseId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;
        Verse.GetId(translationId, bookId, chapterId, verseId);
    }

    @Test
    public void testGetArrayList() {
        int translationId = 1;
        Translation translation = new Translation(translationId, "English");
        int bookId = 1;
        Book book = new Book(bookId, "Book");
        int chapterId = 1;
        Chapter chapter = new Chapter(chapterId, 1, "Chapter", book);
        int verseId1 = 1;
        int verseId2 = 2;
        ArrayList verses1 = Verse.Get(new Translation[] { translation }, book, chapter);
        assertEquals(2, verses1.size());
        ArrayList verses2 = Verse.Get(new Translation[] { translation }, chapter, new Reference(chapterId, verseId2));
        assertEquals(1, verses2.size());
        assertEquals(verseId1, ((Verse) verses1.get(0)).getId());
        assertEquals(verseId2, ((Verse) verses2.get(0)).getId());
    }

    @Test
    public void testGetFromDatabase() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        Verse expectedVerse = new Verse(1);
        Verse verse = Verse.New(verseId);
        assertEquals(expectedVerse, verse);
        DbResult result = Database.Query("select * from Verse where Verse_ID = " + verseId);
        verse = new Verse(result);
        assertEquals(expectedVerse, verse);
    }

    @Test(expected = SQLException.class)
    public void testGetFromDatabaseFailure() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;
        DbResult result = Database.Query("select * from Verse where Verse_ID = " + verseId);
    }

}