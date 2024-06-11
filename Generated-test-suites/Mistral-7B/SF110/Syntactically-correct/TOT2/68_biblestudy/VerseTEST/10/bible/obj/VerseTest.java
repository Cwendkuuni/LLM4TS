import org.junit.Assert;
import org.junit.Test;
import bible.obj.*;
import bible.util.*;

public class VerseTest {

    @Test
    public void testNewWithValidId() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertNotNull(verse);
        assertEquals(id, verse.getId());
    }

    @Test
    public void testNewWithInvalidId() {
        int id = -1;
        Verse verse = Verse.New(id);
        assertNull(verse);
    }

    @Test
    public void testGetIdWithValidId() {
        Verse verse = new Verse(1);
        int id = verse.getId();
        assertEquals(1, id);
    }

    @Test
    public void testGetIdWithNullObject() {
        assertThrows(NullPointerException.class, () -> new Verse(null).getId());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 null null]";
        String actual = verse.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetVersesWithValidArguments() {
        Translation[] translations = new Translation[1];
        Verse expectedVerse = new Verse(1);
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
        assertEquals(expectedVerse, verses.get(0));
    }

    @Test
    public void testGetVersesWithNullArguments() {
        Translation[] translations = null;
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetVersesWithValidArguments() {
        Translation[] translations = new Translation[1];
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
    }

    @Test
    public void testGetVersesWithEmptyList() {
        Translation[] translations = new Translation[0];
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetIdWithValidArguments() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;
        int actualId = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetIdWithNullTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int actualId = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, actualId);
    }

    @Test
    public void testGetIdWithInvalidArguments() {
        int translationId = 1;
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;
        int actualId = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, actualId);
    }
}