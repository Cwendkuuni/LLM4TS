package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.Test;

import bible.obj.*;
import bible.util.*;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ANOTHER_VERSE_ID = 2;
 private static final int TEST_INVALID_ID = -1;

 @Test
 public void testNew() {
 Verse verse1 = Verse.New(TEST_VERSE_ID);
 Verse verse2 = Verse.New(TEST_VERSE_ID);
 assertEquals(verse1, verse2);
 }

 @Test(expected = NullPointerException.class)
 public void testNewNullId() {
 Verse.New(null);
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(Translation.New(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = Translation.New(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_CHAPTER_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 Verse verse = (Verse) verses.get(0);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGetEmpty() {
 Translation[] translations = new Translation[0];
 ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetRange() {
 Translation[] translations = new Translation[1];
 translations[0] = Translation.New(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO, TEST_VERSE_NO, TEST_VERSE_NO);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO, TEST_VERSE_NO + 1, TEST_VERSE_NO + 1);
 ArrayList verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetRangeEmpty() {
 Translation[] translations = new Translation[0];
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO, TEST_VERSE_NO, TEST_VERSE_NO);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO, TEST_VERSE_NO + 1, TEST_VERSE_NO + 1);
 ArrayList verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetIdInvalid() {
 int id = Verse.GetId(Translation.New(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_INVALID_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testNewWithResult() throws SQLException {
 DbResult result = new DbResult();
 result.setInt("Verse_ID", TEST_VERSE_ID);
 result.setString("Text", "Test Text");
 result.setInt("Translation_ID", TEST_TRANSLATION_ID);
 result.setInt("Book_ID", TEST_BOOK_ID);
 result.setInt("Chapter", TEST_CHAPTER_ID);
 result.setInt("Verse_No", TEST_VERSE_ID);
 Verse verse = new Verse(result);
 assertEquals(TEST_VERSE_ID, verse.getId());
 assertEquals("Test Text", verse.getText());
 }

 @Test
 public void testCacheHit() {
 Verse verse1 = Verse.New(TEST_VERSE_ID);
 Verse verse2 = Verse.New(TEST_VERSE_ID);
 assertSame(verse1, verse2);
 }

 @Test
 public void testCacheMiss() throws SQLException {
 Verse verse1 = Verse.New(TEST_VERSE_ID);
 // Simulate cache miss
 Verse. verseCache.remove(new Integer(TEST_VERSE_ID));
 Verse verse2 = Verse.New(TEST_VERSE_ID);
 assertNotSame(verse1, verse2);
 }

 @Test
 public void testGetIdFromDb() throws SQLException {
 int id = Verse.GetId(Translation.New(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 int dbId = ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(id, dbId);
 }

 @Test
 public void testGetIdFromDbInvalid() throws SQLException {
 int id = Verse.GetId(Translation.New(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_INVALID_ID);
 int dbId = ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_INVALID_ID);
 assertEquals(-1, id);
 assertEquals(-1, dbId);
 }
}