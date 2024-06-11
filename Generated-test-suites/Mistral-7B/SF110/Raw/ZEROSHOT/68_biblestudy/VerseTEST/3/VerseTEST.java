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
 private static final int TEST_INVALID_VERSE_ID = -1;

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertNotNull(verse);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = NullPointerException.class)
 public void testNewNullId() {
 Verse.New(null);
 }

 @Test
 public void testGetId() {
 assertEquals(TEST_VERSE_ID, Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdTranslationNull() {
 assertEquals(-1, Verse.GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdTranslationInvalid() {
 assertEquals(-1, Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdBookNull() {
 assertEquals(-1, Verse.GetId(TEST_TRANSLATION_ID, null, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdBookInvalid() {
 assertEquals(-1, Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdChapterNull() {
 assertEquals(-1, Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, null, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdChapterInvalid() {
 assertEquals(-1, Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdVerseNegative() {
 assertEquals(-1, Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_INVALID_VERSE_ID));
 }

 @Test
 public void testGetIdVerseZero() {
 assertEquals(0, Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, 0));
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 Verse verse = verses.get(0);
 assertEquals(TEST_VERSE_ID, verse.getId());

 verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID), new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(2, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullTranslations() {
 Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @Test
 public void testGetTranslationNull() {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { null }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetTranslationInvalid() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(-1) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetBookNull() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetBookInvalid() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetChapterNull() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, null, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetChapterInvalid() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVerseNegative() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVerseZero() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, 0);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetEmpty() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_ANOTHER_VERSE_ID);
 assertEquals(2, verses.size());
 Verse verse1 = verses.get(0);
 assertEquals(TEST_VERSE_ID, verse1.getId());

 Verse verse2 = verses.get(1);
 assertEquals(TEST_ANOTHER_VERSE_ID, verse2.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetWithException() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_ANOTHER_VERSE_ID);
 fail("Should have thrown SQLException");
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(TEST_VERSE_ID);
 String expected = "bible.obj.Verse:[1 " + new TranslationReference(new Translation(TEST_TRANSLATION_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)) + " " + verse.getText() + "]";
 assertEquals(expected, verse.toString());
 }
}