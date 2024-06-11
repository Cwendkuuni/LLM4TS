package bible.obj.test;

import org.junit.Assert;
import org.junit.Test;
import bible.obj.Translation;
import bible.obj.Verse;
import bible.obj.util.Database;
import bible.obj.util.DbResult;
import bible.obj.util.Reference;

public class VerseTest {

    @Test
    public void testGetId() {
        Verse verse = new Verse(1);
        int id = verse.getId();
        assertEquals(1, id);
    }

    // Test for New(int id) with existing id in cache
    @Test
    public void testNewExistingIdInCache() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }

    // Test for New(int id) with non-existing id
    @Test
    public void testNewNonExistingId() {
        Verse verse = new Verse(5);
        assertEquals(5, verse.getId());
        // Check if new Verse object is added to the cache
        Verse newVerse = Verse.New(5);
        assertEquals(5, newVerse.getId());
    }

    // Test for GetId(int translationId, int bookId, int chapterId, int verseId) with valid input
    @Test
    public void testGetIdValidInput() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    // Test for GetId(int translationId, int bookId, int chapterId, int verseId) with invalid input (translationId)
    @Test(expected = SQLException.class)
    public void testGetIdInvalidTranslationId() {
        Verse.GetId(-1, 1, 1, 1);
    }

    // Test for GetId(int translationId, int bookId, int chapterId, int verseId) with invalid input (bookId)
    @Test(expected = SQLException.class)
    public void testGetIdInvalidBookId() {
        Verse.GetId(1, -1, 1, 1);
    }

    // Test for GetId(int translationId, int bookId, int chapterId, int verseId) with invalid input (chapterId)
    @Test(expected = SQLException.class)
    public void testGetIdInvalidChapterId() {
        Verse.GetId(1, 1, -1, 1);
    }

    // Test for GetId(int translationId, int bookId, int chapterId, int verseId) with invalid input (verseId)
    @Test(expected = SQLException.class)
    public void testGetIdInvalidVerseId() {
        Verse.GetId(1, 1, 1, -1);
    }

    // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId) with valid input
    @Test
    public void testGetValidInput() {
        Translation[] translations = new Translation[1];
        Verse verse1 = new Verse(1);
        Verse verse2 = new Verse(2);
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));
    }

    // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId) with invalid input (translations)
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidTranslations() {
        Verse.Get(null, 1, 1, 1);
    }

    // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId) with invalid input (bookId)
    @Test(expected = SQLException.class)
    public void testGetInvalidBookId() {
        Translation[] translations = new Translation[1];
        Verse.Get(translations, -1, 1, 1);
    }

    // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId) with invalid input (chapterId)
    @Test(expected = SQLException.class)
    public void testGetInvalidChapterId() {
        Translation[] translations = new Translation[1];
        Verse.Get(translations, 1, -1, 1);
    }

    // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId) with invalid input (verseId)
    @Test(expected = SQLException.class)
    public void testGetInvalidVerseId() {
        Translation[] translations = new Translation[1];
        Verse.Get(translations, 1, 1, -1);
    }

    // Test for Get(Translation[] translations, Reference begin, Reference end) with valid input
    @Test
    public void testGetValidReferenceRange() {
        Translation[] translations = new Translation[1];
        Verse verse1 = new Verse(1);
        Verse verse2 = new Verse(2);
        translations[0] = new Translation(1, "English");
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));
    }

    // Test for Get(Translation[] translations, Reference begin, Reference end) with invalid input (translations)
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidTranslationsReferenceRange() {
        Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 2));
    }

    // Test for Get(Translation[] translations, Reference begin, Reference end) with invalid input (begin)
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidBeginReference() {
        Translation[] translations = new Translation[1];
        Verse.Get(translations, null, new Reference(1, 1, 2));
    }

    // Test for Get(Translation[] translations, Reference begin, Reference end) with invalid input (end)
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidEndReference() {
        Translation[] translations = new Translation[1];
        Verse.Get(translations, new Reference(1, 1, 1), null);
    }

    // Test for methods throwing SQLException
    @Test(expected = SQLException.class)
    public void testMethodsThrowingSQLException() {
        // Test for New(int id)
        Verse.New(new Integer(-1));

        // Test for GetId(int translationId, int bookId, int chapterId, int verseId)
        Verse.GetId(-1, 1, 1, 1);

        // Test for Get(Translation[] translations, int bookId, int chapterId, int verseId)
        Verse.Get(new Translation[0], 1, 1, 1);

        // Test for Get(Translation[] translations, Reference begin, Reference end)
        Verse.Get(new Translation[0], new Reference(-1, 1, 1), new Reference(1, 1, 1));
    }
}

//