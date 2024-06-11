import org.junit.Assert;import org.junit.Test;import bible.obj.Verse;import bible.obj.TranslationReference;import bible.obj.Reference;import bible.util.Database;import bible.util.DbResult;import java.sql.SQLException;import java.util.ArrayList;import java.util.HashMap;import static java.util.Arrays.asList;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ID = 1;
 private static final String TEST_TEXT = "Test text";
 private static final String TEST_BOOK_NAME = "Test book";
 private static final String TEST_CHAPTER_NAME = "Test chapter";
 private static final String TEST_REFERENCE_TEXT = "Test book 1:1";

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 Assert.assertNotNull(verse);
 Assert.assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetText() {
 Verse verse = new Verse(TEST_ID);
 Assert.assertEquals(TEST_TEXT, verse.getText());
 }

 @Test
 public void testGetTranslationReference() {
 Verse verse = new Verse(TEST_ID);
 TranslationReference translationReference = verse.getTranslationReference();
 Assert.assertNotNull(translationReference);
 Assert.assertEquals(TEST_REFERENCE_TEXT, translationReference.toString());
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(TEST_ID);
 Assert.assertEquals("Verse:[" + TEST_ID + " " + TEST_REFERENCE_TEXT + " " + TEST_TEXT + "]", verse.toString());
 }

 @Test
 public void testGetId() {
 Verse verse = new Verse(TEST_ID);
 Assert.assertEquals(TEST_ID, verse.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetIdNullReference() {
 Verse verse = null;
 Assert.assertNull(verse.getId());
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(1, verses.size());
 Verse testVerse = (Verse) verses.get(0);
 Assert.assertEquals(TEST_ID, testVerse.getId());
 }

 @Test
 public void testGetEdgeCases() {
 Translation[] translations = new Translation[0];
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(0, verses.size());

 translations = new Translation[100];
 for (int i = 0; i < translations.length; i++) {
 translations[i] = new Translation(i + 1);
 }
 verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(1, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNegativeChapterId() {
 Reference reference = new Reference(-1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(new Translation[0], reference);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullReference() {
 ArrayList verses = Verse.Get(new Translation[0], null);
 }

 @Test
 public void testGetWithEmptyTranslationList() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetThrowsSQLException() {
 Database.setMockResult(DbResult.emptyResult());
 ArrayList verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdStatic() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(TEST_ID, result);
 }

 @Test
 public void testGetIdNullTranslation() {
 int result = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdNullBook() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdNullChapter() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdNullVerse() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidTranslationId() {
 int result = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidBookId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidChapterId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidVerseId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdDifferentTranslationId() {
 int result = Verse.GetId(2, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidBookId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, 0, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidChapterId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, 0, TEST_VERSE_ID);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetIdInvalidVerseId() {
 int result = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, 0);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testGetList() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assert.assertEquals(1, verses.size());
 }

 @Test
 public void testGetListEmptyTranslationList() {
 Translation[] translations = new Translation[0];
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetListNullBeginReference() {
 Translation[] translations = new Translation[1];
 ArrayList verses = Verse.Get(translations, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetListNullEndReference() {
 Translation[] translations = new Translation[1];
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), null);
 }

 @Test(expected = SQLException.class)
 public void testGetListThrowsSQLException() {
 Database.setMockResult(DbResult.emptyResult());
 Translation[] translations = new Translation[1];
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 }

}