import org.junit.Test;
import org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Util;
import java.sql.DbResult;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Integer.parseInt;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_OTHER_VERSE_ID = 2;
 private static final String TEST_TEXT = "Test Text";

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 Assert.assertNotNull(verse);
 Assert.assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(); // Assuming Translation is a class with an id
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(1, verses.size());
 Verse verse = (Verse) verses.get(0);
 Assert.assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGet_WrongTranslationId() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_DifferentBeginAndEnd() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_OTHER_VERSE_ID));
 Assert.assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGet_SqlException() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetId_NullTranslation() {
 int id = Verse.GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_NullBook() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_NullChapter() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, null, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_NullVerse() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, null);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidTranslationId() {
 int id = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidBookId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidChapterId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse();
 verse.id = TEST_VERSE_ID;
 verse.transRef = new TranslationReference(new Translation(), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 verse.text = TEST_TEXT;
 String result = verse.toString();
 String expected = "bible.obj.Verse:[" + TEST_VERSE_ID + " " + verse.transRef + " " + TEST_TEXT + "]";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testComplete() throws SQLException {
 DbResult result = new DbResult() {
 @Override public boolean next() {
 return true;
 }

 @Override public int getInt(String columnName) throws SQLException {
 switch (columnName) {
 case "Verse_ID": return TEST_VERSE_ID;
 case "Text": return 0; // Text is a String, but we assume it's not important for this test
 case "Translation_ID": return TEST_TRANSLATION_ID;
 case "Book_ID": return TEST_BOOK_ID;
 case "Chapter": return TEST_CHAPTER_ID;
 case "Verse_No": return TEST_VERSE_ID;
 default: throw new SQLException("Unknown column: " + columnName);
 }
 }
 };
 Verse verse = new Verse();
 verse.complete(result);
 Assert.assertEquals(TEST_VERSE_ID, verse.id);
 Assert.assertEquals(TEST_TEXT, verse.text);
 Assert.assertEquals(new TranslationReference(new Translation(), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)), verse.transRef);
 }

 @Test(expected = SQLException.class)
 public void testComplete_SqlException() throws SQLException {
 Verse verse = new Verse();
 verse.complete(null);
 }
}