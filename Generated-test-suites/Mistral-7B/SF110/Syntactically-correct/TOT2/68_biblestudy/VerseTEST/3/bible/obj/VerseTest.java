package bible.obj.test;

import bible.obj.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {

    @Test
    public void testNewVerse() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertNotNull(verse);
        assertEquals(id, verse.getId());
    }

    @Test
    public void testGetId() {
        Verse verse = new Verse(1);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetVerses() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
        assertTrue(verses.get(0) instanceof Verse);
        assertTrue(verses.get(1) instanceof Verse);
    }

    // Add tests for other methods here

    @Test(expected = IllegalArgumentException.class)
    public void testNewVerseWithNegativeId() {
        int id = -1;
        Verse.New(id);
        fail("Expected IllegalArgumentException but didn't get it");
    }

    @Test(expected = NullPointerException.class)
    public void testNewVerseWithNullId() {
        Integer id = null;
        Verse.New(id);
        fail("Expected NullPointerException but didn't get it");
    }

    @Test
    public void testGetIdWithNegativeId() {
        Verse verse = new Verse(-1);
        assertNull(verse);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIdWithNullObject() {
        Verse verse = null;
        int id = verse.getId();
        fail("Expected NullPointerException but didn't get it");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetVersesWithBeginAfterLastVerse() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 3);
        Reference end = new Reference(1, 1, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(0, verses.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetVersesWithNullTranslations() {
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList verses = Verse.Get(null, begin, end);
        assertEquals(0, verses.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVersesWithIncorrectNumberOfTranslations() {
        Translation[] translations = {new Translation(1)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
        assertTrue(verses.get(0) instanceof Verse);
        Translation[] incorrectTranslations = {new Translation(1), new Translation(2), new Translation(3)};
        ArrayList incorrectVerses = Verse.Get(incorrectTranslations, begin, end);
        assertEquals(2, incorrectVerses.size());
    }

}