package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.*;
import org.junit.Test;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_OTHER_VERSE_ID = 2;
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(new Translation(TEST_TRANSLATION_ID), TEST_REFERENCE);
 private static final Translation TEST_TRANSLATION = new Translation(TEST_TRANSLATION_ID);
 private static final Book TEST_BOOK = new Book(TEST_BOOK_ID);
 private static final List<Translation> TEST_TRANSLATIONS = new ArrayList<>();
 private static final Verse TEST_VERSE = new Verse(TEST_ID);
 private static final Verse TEST_OTHER_VERSE = new Verse(TEST_OTHER_ID);

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 assertEquals(TEST_ID, Verse.getId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = TEST_TRANSLATION;
 List<Verse> verses = Verse.Get(translations, TEST_BOOK, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithNullTranslations() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslations() {
 List<Translation> emptyTranslations = new ArrayList<>();
 List<Verse> verses = Verse.Get(emptyTranslations.toArray(new Translation[0]), TEST_BOOK, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidId() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_BOOK, TEST_CHAPTER_ID, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = new Translation[2];
 translations[0] = TEST_TRANSLATION;
 translations[1] = new Translation(2);
 List<Verse> verses = Verse.Get(translations, TEST_REFERENCE, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_CHAPTER_ID + 1));
 assertEquals(2, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 assertEquals(TEST_OTHER_ID, verses.get(1).getId());
 }

 @Test
 public void testGetWithBeginAndEndNull() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), null, null, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetIdWithValidParameters() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullBook() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullChapter() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK, null, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullVerse() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK, TEST_CHAPTER_ID, null);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK, TEST_CHAPTER_ID, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testToString() {
 assertEquals("bible.obj.Verse:[1 bible.util.TranslationReference[bible.obj.Translation[1], bible.obj.Reference[1 1 1]] foo]", TEST_VERSE.toString());
 }
}