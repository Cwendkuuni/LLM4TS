import org.junit.Test;
import org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.DbResult;
import bible.util.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.IntArray;

public class VerseTest {

    @Test
    public void testNewWithValidId() {
        Verse verse = Verse.New(1);
        Assert.assertNotNull(verse);
    }

    @Test(expected = NullPointerException.class)
    public void testNewWithNullId() {
        Verse.New(null);
    }

    @Test
    public void testNewWithCachedInstance() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = Verse.New(1);
        Assert.assertEquals(verse1, verse2);
    }

    @Test
    public void testNewWithNewInstance() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = new Verse(1);
        Assert.assertNotEquals(verse1, verse2);
    }

    @Test
    public void testGetId() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
        Assert.assertEquals(id, verseId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdWithInvalidTranslationId() {
        int translationId = -1, bookId = 1, chapterId = 1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdWithInvalidBookId() {
        int translationId = 1, bookId = -1, chapterId = 1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdWithInvalidChapterId() {
        int translationId = 1, bookId = 1, chapterId = -1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIdWithInvalidVerseId() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = -1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test
    public void testGetWithValidTranslationsAndRange() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "TestTranslation");
        List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 10));
        Assert.assertEquals(verses.size(), 10);
        for (Verse verse : verses) {
            Assert.assertEquals(verse.getId(), verse.getReference().getVerseNo());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetWithNullTranslations() {
        List<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidBookIdInRange() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "TestTranslation");
        List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 2, 1));
        Assert.assertEquals(verses.size(), 0);
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidQuery() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "TestTranslation");
        Verse.Get(translations, null, new Reference(1, 1, 1));
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "Verse:[1 TestTranslation:[1 1 1] TestText]";
        String actual = verse.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = SQLException.class)
    public void testGetIdWithInvalidTranslationAndBook() {
        int translationId = -1, bookId = 1, chapterId = 1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = SQLException.class)
    public void testGetIdWithInvalidTranslationAndChapter() {
        int translationId = 1, bookId = -1, chapterId = 1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = SQLException.class)
    public void testGetIdWithInvalidTranslationAndVerse() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = -1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidTranslations() {
        Translation[] translations = null;
        List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 10));
        Assert.assertEquals(verses.size(), 0);
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidBeginReference() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "TestTranslation");
        List<Verse> verses = Verse.Get(translations, null, new Reference(1, 1, 1));
        Assert.assertEquals(verses.size(), 0);
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidEndReference() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "TestTranslation");
        List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), null);
        Assert.assertEquals(verses.size(), 0);
    }

    @Test
    public void testGetIdWithValidDataFromDatabase() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        int id = Verse.GetId(translationId, new Reference(bookId, chapterId, verseId));
        Assert.assertEquals(id, verseId);
    }

    @Test(expected = SQLException.class)
    public void testGetIdWithInvalidQueryToDatabase() {
        int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
        Verse.GetId(translationId, new Reference(bookId, chapterId, verseId), "Invalid query");
    }
}