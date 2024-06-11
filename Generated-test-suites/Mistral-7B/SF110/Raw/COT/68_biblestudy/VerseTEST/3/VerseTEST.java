package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

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

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ID = 1;
 private static final String TEST_TEXT = "Test text";
 private static final String TEST_BOOK_NAME = "Test book";
 private static final String TEST_CHAPTER_NAME = "Test chapter";
 private static final String TEST_REFERENCE_TEXT = "Test reference text";

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);

 id = Verse.GetId(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 };
 List<Verse> verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGet_NullTranslations() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyTranslations() {
 Translation[] translations = new Translation[0];
 List<Verse> verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidId() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBookId() {
 List<Verse> verses = Verse.Get(null, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidChapterId() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidVerseId() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_DifferentTranslations() {
 Translation[] translations1 = new Translation[] { new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, null };
 Translation[] translations2 = new Translation[] { null, new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID + 1;
 }
 } };
 List<Verse> verses1 = Verse.Get(translations1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 List<Verse> verses2 = Verse.Get(translations2, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses1.size());
 assertEquals(1, verses2.size());
 assertNotEquals(verses1.get(0), verses2.get(0));
 }

 @Test(expected = SQLException.class)
 public void testGet_DatabaseError() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Database.setThrowDatabaseError(true);
 verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 fail("Expected SQLException");
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(TEST_ID);
 String expected = "Verse:[" + TEST_ID + " " + new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)) + " " + TEST_TEXT + "]";
 assertEquals(expected, verse.toString());
 }

 @Test
 public void testGetReference_NullTransRef() {
 Reference reference = null;
 TranslationReference transRef = new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, reference);
 assertEquals(TEST_REFERENCE_TEXT, Verse.New(TEST_ID).getReference().toString());
 assertEquals(TEST_REFERENCE_TEXT, Verse.New(TEST_ID).getTranslationReference().getReference().toString());
 assertEquals(TEST_REFERENCE_TEXT, Verse.New(TEST_ID).getReference().getBookName(), TEST_BOOK_NAME);
 assertEquals(TEST_CHAPTER_ID, Verse.New(TEST_ID).getReference().getChapterNo());
 assertEquals(TEST_VERSE_ID, Verse.New(TEST_ID).getReference().getVerseNo());
 assertEquals(TEST_BOOK_ID, Verse.New(TEST_ID).getReference().getBookId());
 assertEquals(TEST_TRANSLATION_ID, Verse.New(TEST_ID).getTranslationReference().getId());
 assertEquals(TEST_TRANSLATION_ID, Verse.New(TEST_ID).getTranslationReference().getTranslation().getId());
 assertEquals(TEST_TRANSLATION_ID, Verse.New(TEST_ID).getTranslationReference().getReference().getId());
 assertEquals(TEST_TRANSLATION_ID, Verse.New(TEST_ID).getTranslationReference().getTranslation().getId());
 assertNull(Verse.New(TEST_ID).getTranslationReference().getTranslation().getName());
 assertNull(Verse.New(TEST_ID).getTranslationReference().getTranslation().getVersion());
 assertEquals(TEST_TEXT, Verse.New(TEST_ID).getText());
 }

 @Test
 public void testGetTranslationReference_NullTransRefId() {
 Verse verse = new Verse(TEST_ID);
 TranslationReference transRef = null;
 assertEquals(null, verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReference_NullTransRefTranslation() {
 Verse verse = new Verse(TEST_ID);
 TranslationReference transRef = new TranslationReference(null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(new TranslationReference(null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)), verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReference_NullVersesCache() {
 Verse verse = new Verse(TEST_ID);
 TranslationReference transRef = new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertNull(Verse.verseCache.get(new Integer(TEST_ID)));
 assertEquals(transRef, verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReference_ValidVersesCache() throws SQLException {
 Verse verse = new Verse(TEST_ID);
 TranslationReference transRef = new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Database.setMockDatabaseResult(new DbResult() {
 @Override
 public boolean next() {
 return true;
 }

 @Override
 public int getInt(String columnName) throws SQLException {
 switch (columnName) {
 case "Verse_ID":
 return TEST_ID;
 case "Translation_ID":
 return TEST_TRANSLATION_ID;
 case "Book_ID":
 return TEST_BOOK_ID;
 case "Chapter":
 return TEST_CHAPTER_ID;
 case "Verse_No":
 return TEST_VERSE_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 }
 });
 assertEquals(Verse.New(TEST_ID), verse.getTranslationReference().getTranslation().getVerses().get(0));
 assertEquals(transRef, verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReference_VersesCacheWithDifferentTranslation() throws SQLException {
 Verse verse1 = new Verse(TEST_ID + 1);
 Verse verse2 = new Verse(TEST_ID);
 TranslationReference transRef1 = new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID + 1;
 }
 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 TranslationReference transRef2 = new TranslationReference(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Database.setMockDatabaseResult(new DbResult() {
 @Override
 public boolean next() {
 switch (Database.getCurrentIndex()) {
 case 0:
 return true;
 case 1:
 return true;
 default:
 return false;
 }
 }

 @Override
 public int getInt(String columnName) throws SQLException {
 switch (columnName) {
 case "Verse_ID":
 switch (Database.getCurrentIndex()) {
 case 0:
 return TEST_ID + 1;
 case 1:
 return TEST_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 case "Translation_ID":
 switch (Database.getCurrentIndex()) {
 case 0:
 return TEST_TRANSLATION_ID + 1;
 case 1:
 return TEST_TRANSLATION_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 case "Book_ID":
 return TEST_BOOK_ID;
 case "Chapter":
 return TEST_CHAPTER_ID;
 case "Verse_No":
 return TEST_VERSE_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 }
 });
 assertEquals(verse2, verse1.getTranslationReference().getTranslation().getVerses().get(0));
 assertEquals(transRef1, verse1.getTranslationReference());
 assertEquals(verse1, verse2.getTranslationReference().getTranslation().getVerses().get(0));
 assertEquals(transRef2, verse2.getTranslationReference());
 }

 @Test
 public void testGetTranslationReference_VersesCacheWithSameTranslationButDifferentVerses() throws SQLException {
 Verse verse1 = new Verse(TEST_ID + 1);
 Verse verse2 = new Verse(TEST_ID);
 Translation translation = new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 };
 TranslationReference transRef1 = new TranslationReference(translation, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 TranslationReference transRef2 = new TranslationReference(translation, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 Database.setMockDatabaseResult(new DbResult() {
 @Override
 public boolean next() {
 switch (Database.getCurrentIndex()) {
 case 0:
 return true;
 case 1:
 return true;
 default:
 return false;
 }
 }

 @Override
 public int getInt(String columnName) throws SQLException {
 switch (columnName) {
 case "Verse_ID":
 switch (Database.getCurrentIndex()) {
 case 0:
 return TEST_ID + 1;
 case 1:
 return TEST_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 case "Translation_ID":
 return TEST_TRANSLATION_ID;
 case "Book_ID":
 return TEST_BOOK_ID;
 case "Chapter":
 return TEST_CHAPTER_ID;
 case "Verse_No":
 switch (Database.getCurrentIndex()) {
 case 0:
 return TEST_VERSE_ID + 1;
 case 1:
 return TEST_VERSE_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 }
 });
 assertEquals(verse2, verse1.getTranslationReference().getTranslation().getVerses().get(0));
 assertEquals(transRef1, verse1.getTranslationReference());
 assertEquals(transRef2, verse2.getTranslationReference());
 assertEquals(verse1, verse2.getTranslationReference().getTranslation().getVerses().get(0));
 }

 @Test
 public void testGet() throws SQLException {
 Verse verse = null;
 Database.setMockDatabaseResult(new DbResult() {
 @Override
 public boolean next() {
 if (verse == null) {
 verse = new Verse(TEST_ID);
 return true;
 }
 return false;
 }

 @Override
 public int getInt(String columnName) throws SQLException {
 switch (columnName) {
 case "Verse_ID":
 return TEST_ID;
 case "Text":
 return TEST_TEXT.getBytes("UTF-8")[0];
 case "Translation_ID":
 return TEST_TRANSLATION_ID;
 case "Book_ID":
 return TEST_BOOK_ID;
 case "Chapter":
 return TEST_CHAPTER_ID;
 case "Verse_No":
 return TEST_VERSE_ID;
 default:
 throw new SQLException("Unsupported column: " + columnName);
 }
 }
 });
 assertEquals(TEST_ID, Verse.GetId(new Translation() {
 @Override
 public int getId() {
 return TEST_TRANSLATION_ID;
 }
 }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID).intValue());
 assertEquals(TEST_TEXT, Verse.New(TEST_ID).getText());
 }

 @Test(expected = SQLException.class)
 public void testGet_DatabaseError() throws SQLException {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Database.setThrowDatabaseError(true);
 verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 fail("Expected SQLException");
 }
}