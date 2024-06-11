package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bible.obj.Database;
import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;

public class VerseTest {

 private static final int TEST_TRANS_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertNotNull(verse);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGet() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_VERSE_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithNullTranslation() throws SQLException {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslationArray() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithDifferentTranslationIds() throws SQLException {
 Translation translation1 = new Translation(TEST_TRANS_ID + 1);
 Translation translation2 = new Translation(TEST_TRANS_ID + 2);
 List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(2, verses.size());
 assertNotSame(verses.get(0), verses.get(1));
 }

 @Test
 public void testGetWithDifferentChapterId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithDifferentVerseId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 Verse verse = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID).get(0);
 assertEquals(TEST_VERSE_ID, verse.getId());
 assertEquals(TEST_BOOK_ID, verse.getReference().getBookId());
 assertEquals(TEST_CHAPTER_ID, verse.getReference().getChapterNo());
 assertEquals(TEST_VERSE_ID, verse.getReference().getVerseNo());
 assertEquals(translation, verse.getTranslationReference().getTranslation());
 assertEquals(TEST_TRANS_ID, translation.getId());
 assertEquals(TEST_BOOK_ID, translation.getBook().getId());
 assertEquals(TEST_CHAPTER_ID, translation.getBook().getChapter(0).getId());
 }

 @Test
 public void testGetFromDatabaseWithDifferentTranslationId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID + 1);
 Verse verse = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID).get(0);
 assertNull(verse);
 }

 @Test
 public void testGetFromDatabaseWithDifferentBookId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 Verse verse = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID + 1, TEST_CHAPTER_ID, TEST_VERSE_ID).get(0);
 assertNull(verse);
 }

 @Test
 public void testGetFromDatabaseWithDifferentChapterId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 Verse verse = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_VERSE_ID).get(0);
 assertNull(verse);
 }

 @Test
 public void testGetFromDatabaseWithDifferentVerseId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 Verse verse = Verse.Get(new Translation[] { translation }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1).get(0);
 assertNull(verse);
 }

 @Test
 public void testGetId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 Book book = new Book(TEST_BOOK_ID);
 Chapter chapter = book.getChapter(TEST_CHAPTER_ID - 1);
 assertEquals(Verse.GetId(translation, book, TEST_CHAPTER_ID, TEST_VERSE_ID), TEST_VERSE_ID);
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullBook() {
 int id = Verse.GetId(new Translation(TEST_TRANS_ID), null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithInvalidChapterId() {
 Translation translation = new Translation(TEST_TRANS_ID);
 Book book = new Book(TEST_BOOK_ID);
 Chapter chapter = new Chapter(TEST_CHAPTER_ID * -1);
 Verse.GetId(translation, book, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 Translation translation = new Translation(TEST_TRANS_ID);
 Book book = new Book(TEST_BOOK_ID);
 Chapter chapter = book.getChapter(TEST_CHAPTER_ID - 1);
 assertEquals(-1, Verse.GetId(translation, book, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID * -1));
 }

 @Test
 public void testGetWithBeginAndEnd() throws SQLException {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANS_ID);
 translations[1] = new Translation(TEST_TRANS_ID + 1);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.size());
 assertEquals(TEST_VERSE_ID, verses.get(0).getId());
 assertEquals(TEST_VERSE_ID + 1, verses.get(1).getId());
 assertNotSame(verses.get(0), verses.get(1));
 }

 @Test
 public void testGetWithBeginAndEndNull() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], null, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndEmptyTranslationArray() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndDifferentTranslationIds() throws SQLException {
 Translation translation1 = new Translation(TEST_TRANS_ID);
 Translation translation2 = new Translation(TEST_TRANS_ID + 1);
 List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(2, verses.size());
 assertEquals(TEST_VERSE_ID, verses.get(0).getId());
 assertEquals(TEST_VERSE_ID + 1, verses.get(1).getId());
 assertNotSame(verses.get(0), verses.get(1));
 }

 @Test
 public void testGetWithBeginAndEndDifferentBookId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, new Reference(TEST_BOOK_ID + 1, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndDifferentChapterId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndDifferentVerseId() throws SQLException {
 Translation translation = new Translation(TEST_TRANS_ID);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 int id = Verse.GetId(new Translation(TEST_TRANS_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGetIdFromDatabaseWithNullTranslation() {
 int id = Verse.GetId(null, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithNullBook() {
 int id = Verse.GetId(new Translation(TEST_TRANS_ID), null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdFromDatabaseWithInvalidChapterId() {
 int id = Verse.GetId(new Translation(TEST_TRANS_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID * -1, TEST_VERSE_ID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdFromDatabaseWithInvalidVerseId() {
 int id = Verse.GetId(new Translation(TEST_TRANS_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID * -1);
 }

}