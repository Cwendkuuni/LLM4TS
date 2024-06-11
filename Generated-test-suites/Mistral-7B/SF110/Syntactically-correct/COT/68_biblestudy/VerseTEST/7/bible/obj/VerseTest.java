package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = "Lorem ipsum dolor sit amet";
        verse.setText(text);
        assertEquals(text, verse.getText());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        Translation translation = new Translation(1);
        Reference reference = new Reference(1, 1, 1);
        TranslationReference translationReference = new TranslationReference(translation, reference);
        verse.setTranslationReference(translationReference);
        assertEquals(translationReference, verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = new Reference(1, 1, 1);
        TranslationReference translationReference = new TranslationReference(new Translation(1), reference);
        verse.setTranslationReference(translationReference);
        assertEquals(reference, verse.getReference());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 [] Lorem ipsum dolor sit amet]";
        assertEquals(expected, verse.toString());
    }

    @Test
    public void testGetId() {
        Verse verse = new Verse(1);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        String text = "Lorem ipsum dolor sit amet";
        verse.setText(text);
        String expected = text;
        assertEquals(expected, verse.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTranslationReferenceNull() {
        Verse verse = new Verse(1);
        assertNull(verse.getTranslationReference());
        // this should throw an IllegalStateException
        verse.getReference();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetReferenceNull() {
        Verse verse = new Verse(1);
        TranslationReference translationReference = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
        verse.setTranslationReference(translationReference);
        assertNull(verse.getReference());
        // this should throw an IllegalStateException
        verse.getText();
    }

    @Test(expected = SQLException.class)
    public void testGetIdInvalidTranslationId() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.GetId(translationId, new Translation(1), new Book(1), new Chapter(1), chapterId, verseId);
        assertEquals(-1, result);
    }

    @Test(expected = SQLException.class)
    public void testGetIdInvalidBookId() {
        int translationId = 1;
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.GetId(translationId, new Translation(1), new Book(1), new Chapter(1), chapterId, verseId);
        assertEquals(-1, result);
    }

    @Test(expected = SQLException.class)
    public void testGetIdInvalidChapterId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = -1;
        int verseId = 1;
        int result = Verse.GetId(translationId, new Translation(1), new Book(1), new Chapter(1), chapterId, verseId);
        assertEquals(-1, result);
    }

    @Test(expected = SQLException.class)
    public void testGetIdInvalidVerseId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;
        int result = Verse.GetId(translationId, new Translation(1), new Book(1), new Chapter(1), chapterId, verseId);
        assertEquals(-1, result);
    }

    @Test
    public void testGet() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Book(1), new Chapter(1, 1), new Chapter(1, 1, 1));
        assertEquals(1, verses.size());
        Verse verse = verses.get(0);
        assertEquals(1, verse.getId());
        assertEquals(1, verse.getTranslationReference().getTranslation().getId());
        assertEquals(1, verse.getReference().getBookId());
        assertEquals(1, verse.getReference().getChapterNo());
        assertEquals(1, verse.getReference().getVerseNo());
    }

    @Test
    public void testGetWithEmptyTranslationArray() {
        List<Translation> translations = new ArrayList<>();
        List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, null, null);
        assertEquals(0, verses.size());
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidBeginReference() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, null, new Reference(-1, 1, 1));
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidEndReference() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, null, new Reference(1, -1, 1));
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidBeginBookId() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, new Book(-1), null);
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidBeginChapter() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, new Book(1), new Chapter(-1));
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidEndBookId() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, new Book(1), new Chapter(1, 1, -1));
    }

    @Test(expected = SQLException.class)
    public void testGetWithInvalidEndChapter() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        Verse.Get(translations.toArray(new Translation[0]), null, new Book(1), new Chapter(1, -1));
    }

    @Test
    public void testGetWithValidParameters() {
        List<Translation> translations = new ArrayList<>();
        translations.add(new Translation(1));
        List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Translation(1), new Book(1), new Chapter(1, 1, 1));
        assertEquals(1, verses.size());
        Verse verse = verses.get(0);
        assertEquals(1, verse.getId());
        assertEquals(1, verse.getTranslationReference().getTranslation().getId());
        assertEquals(1, verse.getReference().getBookId());
        assertEquals(1, verse.getReference().getChapterNo());
        assertEquals(1, verse.getReference().getVerseNo());
    }

    @Test
    public void testPrivateConstructor() {
        try {
            new Verse(-1);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            new Verse(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPrivateMethodComplete() throws SQLException {
        DbResult dbResult = new DbResult() {
            @Override
            public boolean next() {
                return true;
            }

            @Override
            public void close() {
                // no-op
            }
        };
        Verse verse = new Verse(dbResult);
        assertEquals(1, verse.getId());
        assertEquals("Text", verse.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse.getTranslationReference());
    }

    @Test
    public void testPrivateMethodReadId() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        DbResult dbResult = new DbResult() {
            @Override
            public boolean next() {
                return true;
            }

            @Override
            public void close() {
                // no-op
            }
        };
        dbResult.setInt("Verse_ID", 1);
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }

}