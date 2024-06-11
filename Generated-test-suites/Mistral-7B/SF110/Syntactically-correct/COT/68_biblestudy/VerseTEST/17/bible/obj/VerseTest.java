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
import bible.util.DbResult;
import bible.util.Database;
import bible.util.Util;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ANOTHER_VERSE_ID = 2;
 private static final int TEST_BOOK_ID_OUT_OF_RANGE = 101;
 private static final int TEST_CHAPTER_ID_OUT_OF_RANGE = 11;
 private static final int TEST_VERSE_ID_OUT_OF_RANGE = 11;
 private static final int TEST_INVALID_TRANSLATION_ID = -1;

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 List<Verse> verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_VERSE_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithInvalidTranslationId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(-1);
 List<Verse> verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithOutOfRangeBookId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID_OUT_OF_RANGE), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithOutOfRangeChapterId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithOutOfRangeVerseId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookIdAndChapterId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID_OUT_OF_RANGE), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookIdAndVerseId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidChapterAndVerseId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidTranslationIdAndBookIdAndChapterId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID_OUT_OF_RANGE), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidTranslationIdAndBookIdAndVerseId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidTranslationIdAndChapterIdAndVerseId() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = null;
 try {
 verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithMultipleTranslations() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 translations[1] = new Translation(TEST_TRANSLATION_ID + 1);
 List<Verse> verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = new Translation[1];
 List<Verse> verses = Verse.Get(translations, null, new Reference(TEST_BOOK_ID, 1, 1), new Reference(TEST_BOOK_ID, 1, 1));
 assertEquals(0, verses.size());

 verses = Verse.Get(translations, new Translation[0], new Reference(TEST_BOOK_ID, 1, 1), new Reference(TEST_BOOK_ID, 1, 11));
 assertEquals(11, verses.size());
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(new Translation(-1), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithOutOfRangeBookId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID_OUT_OF_RANGE), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithOutOfRangeChapterId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithOutOfRangeVerseId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookIdAndChapterId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID_OUT_OF_RANGE), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookIdAndVerseId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterAndVerseId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndBookIdAndChapterId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(-1), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndBookIdAndVerseId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndChapterIdAndVerseId() {
 int id = -1;
 try {
 id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID_OUT_OF_RANGE, TEST_VERSE_ID);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testNewWithInvalidId() {
 Verse verse = null;
 try {
 verse = Verse.New(TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(null, verse);
 }

 @Test
 public void testNewWithValidId() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGetText() {
 Verse verse = new Verse(TEST_VERSE_ID);
 String text = "test text";
 verse.setText(text);
 assertEquals(text, verse.getText());
 }

 @Test
 public void testGetTranslationReference() {
 Verse verse = new Verse(TEST_VERSE_ID);
 TranslationReference ref = new TranslationReference(new Translation(TEST_TRANSLATION_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 verse.setTranslationReference(ref);
 assertEquals(ref, verse.getTranslationReference());
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(TEST_VERSE_ID);
 String expected = "Verse:[1 bible.obj.test.TranslationReference@1b9d358[Translation:[1 bible.obj.test.Translation@18b4a99[Id:1], bible.obj.Reference@7852e9b9[Id:1, Book:1, Chapter:1, Verse:1]] Text:null TranslationReference:null]";
 assertEquals(expected, verse.toString());
 }

 @Test
 public void testGetIdFromDatabaseWithValidData() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGetIdFromDatabaseWithInvalidData() throws SQLException {
 int id = -1;
 try {
 id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(-1, id);
 }

 @Test
 public void testGetVersesFromDatabaseWithValidData() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_VERSE_ID + 1);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetVersesFromDatabaseWithInvalidData() throws SQLException {
 List<Verse> verses = null;
 try {
 verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID_OUT_OF_RANGE, TEST_VERSE_ID_OUT_OF_RANGE + 1);
 } catch (Exception e) {
 // Expected
 }
 assertEquals(0, verses.size());
 }
}