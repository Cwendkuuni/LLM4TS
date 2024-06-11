
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.*;

public class VerseTest {

    @Test
    public void testNewMethod() {
        int id = 1;
        Verse verse = Verse.New(id);
        assertEquals(id, verse.getId());
    }

    @Test
    public void testGetIdMethod() {
        Verse verse = new Verse(1);
        int expectedId = 1;
        int actualId = verse.getId();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetTextMethod() {
        Verse verse = new Verse(1);
        String expectedText = "Sample text";
        String actualText = verse.getText();
        assertEquals(expectedText, actualText);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdMethodWithInvalidTranslationId() {
        Translation translation = null;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;

        int actualId = Verse.GetId(translation, new Book(bookId), chapterId, verseId);
        assertNotEquals(expectedId, actualId);
    }

    @Test
    public void testGetIdMethodWithNonExistentTranslation() {
        int translationId = 100;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;

        int actualId = Verse.GetId(new Translation(translationId), new Book(bookId), chapterId, verseId);
        assertEquals(expectedId, actualId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdMethodWithInvalidData() {
        Translation translation = new Translation(1);
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;

        int actualId = Verse.GetId(translation, new Book(bookId), chapterId, verseId);
        assertNotEquals(expectedId, actualId);
    }
}