import org.junit.Assert;
import org.junit.Test;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_VERSE_ID_1 = 1;
 private static final int TEST_VERSE_ID_2 = 2;
 private static final String TEST_TRANSLATION_NAME = "English";
 private static final String TEST_BOOK_NAME = "Genesis";
 private static final String TEST_CHAPTER_NAME = "Chapter 1";
 private static final String TEST_VERSE_TEXT_1 = "In the beginning God created the heavens and the earth.";
 private static final String TEST_VERSE_TEXT_2 = "Now the earth was formless and empty, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.";

 @Test
 public void testNew_ValidId() {
 Verse verse1 = Verse.New(TEST_VERSE_ID_1);
 Assert.assertNotNull(verse1);
 }

 @Test(expected = NullPointerException.class)
 public void testNew_InvalidId() {
 Verse verse1 = Verse.New(-1);
 }

 @Test
 public void testGetId_ValidArguments() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID_1);
 Assert.assertEquals(TEST_VERSE_ID_1, id);
 }

 @Test
 public void testGetId_TranslationNotFound() {
 int id = Verse.GetId(new Translation(101, "Invalid"), new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID_1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_BookNotFound() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Book(101, "Invalid"), TEST_CHAPTER_ID, TEST_VERSE_ID_1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_ChapterNotFound() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Book(TEST_BOOK_ID, TEST_BOOK_NAME), 101, TEST_VERSE_ID_1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_VerseNotFound() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, 101);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGet_ValidArguments() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 Assert.assertEquals(2, verses.size());
 Assert.assertNotNull(verses.get(0));
 Assert.assertNotNull(verses.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidArguments() {
 Translation[] translations = null;
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 }

 @Test
 public void testGet_TranslationNotFound() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(101, "Invalid");
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_BookNotFound() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(101, "Invalid"), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_ChapterRangeOutsideBook() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, 101), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidVerseRange() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID - 1, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 Assert.assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGet_DatabaseError() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 Database.setThrowExceptionOnQuery(true);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_2));
 }

 @Test
 public void testToString() {
 Verse verse1 = new Verse(TEST_VERSE_ID_1);
 String expected = "Verse:[1 English:[Genesis 1:1 In the beginning God created the heavens and the earth.]";
 String result = verse1.toString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testGet_MultipleVersesWithSameId() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 translations[1] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1));
 Assert.assertEquals(2, verses.size());
 }

 @Test
 public void testGet_VerseWithSameText() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 translations[1] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1));
 Verse verse1 = (Verse) verses.get(0);
 Verse verse2 = (Verse) verses.get(1);
 Assert.assertEquals(TEST_VERSE_TEXT_1, verse1.getText());
 Assert.assertEquals(TEST_VERSE_TEXT_1, verse2.getText());
 }

 @Test
 public void testGet_VerseWithDifferentTexts() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 translations[1] = new Translation(TEST_TRANSLATION_ID, "AnotherTranslationName");
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1));
 Verse verse1 = (Verse) verses.get(0);
 Verse verse2 = (Verse) verses.get(1);
 Assert.assertNotEquals(TEST_VERSE_TEXT_1, verse2.getText());
 }

 @Test
 public void testGet_VerseWithSameTranslationButDifferentReferences() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 translations[1] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME, TEST_CHAPTER_ID + 1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_VERSE_ID_1), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_1));
 Verse verse1 = (Verse) verses.get(0);
 Verse verse2 = (Verse) verses.get(1);
 Assert.assertNotEquals(verse1.getId(), verse2.getId());
 }
}