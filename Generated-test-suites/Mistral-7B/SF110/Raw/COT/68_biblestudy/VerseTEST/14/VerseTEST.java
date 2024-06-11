import org.junit.Test;
import org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
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
        Verse verse = new Verse(1);
        int id = verse.getId();
        assertEquals(1, id);
    }

    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = verse.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String toString = verse.toString();
        String expected = "bible.obj.Verse:[1 null null]";
        assertEquals(expected, toString);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullId() {
        Verse.New(0);
    }

    @Test
    public void testGetIdWithValidTranslationAndBookAndChapterAndVerse() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetIdWithNullTranslation() {
        int id = Verse.GetId(null, new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidTranslation() {
        int id = Verse.GetId(new TranslationReference(100, new Reference(1, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidBook() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(100, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidChapter() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 100, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidVerse() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 1, 1)), new Reference(1, 1, 1), 100, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetWithValidTranslationsAndBookAndChapterAndVerseRange() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, null);
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertEquals(1, verses.size());
        Verse verse = (Verse) verses.get(0);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetWithNullTranslations() {
        ArrayList verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithEmptyTranslations() {
        Translation[] translations = new Translation[0];
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithInvalidBook() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, null);
        ArrayList verses = Verse.Get(translations, new Reference(100, 1, 1), new Reference(1, 1, 2));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithInvalidChapter() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, null);
        ArrayList verses = Verse.Get(translations, new Reference(1, 100, 1), new Reference(1, 1, 2));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithInvalidVerse() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, null);
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 100, 2));
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithInvalidTranslationId() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(100, null);
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertTrue(verses.isEmpty());
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidQuery() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, null);
        ArrayList verses = Verse.Get(translations, null, new Reference(1, 1, 2));
    }

    @Test
    public void testGetIdWithValidTranslationAndBookAndChapterAndVerse() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetIdWithNullTranslationReference() {
        int id = Verse.GetId(null, new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidTranslationReferenceTranslation() {
        int id = Verse.GetId(new TranslationReference(100, new Reference(1, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidBook() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(100, 1, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidChapter() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 100, 1)), new Reference(1, 1, 1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithInvalidVerse() {
        int id = Verse.GetId(new TranslationReference(1, new Reference(1, 1, 1)), new Reference(1, 1, 100), 1, 1);
        assertEquals(-1, id);
    }
}