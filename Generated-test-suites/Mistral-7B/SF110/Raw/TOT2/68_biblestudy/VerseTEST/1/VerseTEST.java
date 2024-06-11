import static org.junit.Assert.*;
import bible.obj.*;
import static bible.util.Util.ToString;

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
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference ref = verse.getReference();
        assertNotNull(ref);
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 null null]";
        String result = verse.toString();
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNewWithNullId() {
        Verse verse = Verse.New(null);
        assertNull(verse);
    }

    @Test
    public void testGetIdWithNegativeId() {
        Verse verse = new Verse(1);
        try {
            int id = verse.getId(-1);
            fail("Expected NegativeArraySizeException or IllegalArgumentException");
        } catch (NegativeArraySizeException | IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTextWithEmptyText() {
        Verse verse = new Verse(1);
        verse.setText("");
        String text = verse.getText();
        assertEquals("", text);

        try {
            String emptyText = verse.getText(10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetTextWithNullText() {
        Verse verse = new Verse(1);
        verse.setText(null);
        String text = verse.getText();
        assertEquals(null, text);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTranslationReferenceWithNullTransRef() {
        Verse verse = new Verse(1);
        verse.setTranslationReference(null);
        TranslationReference transRef = verse.getTranslationReference();
        assertNull(transRef);
    }

    @Test(expected = NullPointerException.class)
    public void testGetReferenceWithNullRef() {
        Verse verse = new Verse(1);
        verse.setReference(null);
        Reference ref = verse.getReference();
        assertNull(ref);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidTranslations() {
        Translation[] translations = new Translation[0];
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(0, verses.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullBegin() {
        Translation[] translations = new Translation[1];
        Reference begin = null;
        ArrayList verses = Verse.Get(translations, begin, new Reference(1, 1, 1));
        assertEquals(0, verses.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullEnd() {
        Translation[] translations = new Translation[1];
        Reference end = null;
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), end);
        assertEquals(0, verses.size());
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidSqlQuery() {
        Translation[] translations = new Translation[1];
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        try {
            Verse.Get(translations, new Reference(1, 1, 1), new Reference("Invalid", 1, 1));
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected exception
        }
    }

    @Test(expected = SQLException.class)
    public void testGetIdWithInvalidSqlQuery() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        try {
            id = Verse.GetId(translationId, bookId, chapterId, verseId + 1);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected exception
        }
    }
}