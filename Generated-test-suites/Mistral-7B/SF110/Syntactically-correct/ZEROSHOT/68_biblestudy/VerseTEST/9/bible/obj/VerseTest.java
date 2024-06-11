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
import bible.util.DbResult;
import bible.util.Database;

public class VerseTest {

 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_ID = 1;
 private static final int TEST_OTHER_ID = 2;
 private static final int TEST_VERSE_ID = 1;
 private static final String TEST_TEXT = "Test Text";
 private static final String TEST_TRANSLATION_NAME = "Test Translation";
 private static final String TEST_BOOK_NAME = "Test Book";
 private static final String TEST_REFERENCE_BOOK_NAME = "Test Book";
 private static final String TEST_REFERENCE_CHAPTER_NAME = "Test Chapter";
 private static final String TEST_REFERENCE_VERSE_NAME = "Test Verse";
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), TEST_REFERENCE);
 private static final Verse TEST_VERSE = new Verse(TEST_ID, TEST_TEXT, TEST_TRANSLATION_REFERENCE);

 @Test
 public void testNew() throws SQLException {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetId() throws SQLException {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGet() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 List<Verse> verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslation() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME);
 List<Verse> verses = Verse.Get(translations, TEST_REFERENCE, TEST_REFERENCE);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID=" + TEST_ID);
 Verse verse = new Verse(result);
 assertEquals(TEST_ID, verse.getId());
 assertEquals(TEST_TEXT, verse.getText());
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testGetVersesFromDatabase() throws SQLException {
 DbResult result = Database.Query("SELECT * FROM Verse WHERE Translation_ID=" + TEST_TRANSLATION_ID + " AND Book_ID=" + TEST_BOOK_ID + " AND Chapter=" + TEST_CHAPTER_ID + " AND Verse_No >= " + TEST_VERSE_ID);
 List<Verse> verses = new ArrayList<>();
 while (result.next()) {
 verses.add(new Verse(result));
 }
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetIdFromDatabaseWithNullTranslationId() throws SQLException {
 int id = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithNullBookId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithNullChapterId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithNullVerseId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithInvalidTranslationId() throws SQLException {
 int id = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithInvalidBookId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithInvalidChapterId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseWithInvalidVerseId() throws SQLException {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetVersesFromDatabaseWithNullTranslationArray() throws SQLException {
 Translation[] translations = null;
 List<Verse> verses = Verse.Get(translations, new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesFromDatabaseWithInvalidBookId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_OTHER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesFromDatabaseWithInvalidChapterId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_OTHER_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesFromDatabaseWithInvalidVerseId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID + 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesFromDatabaseWithInvalidTranslationId() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testToString() {
 assertEquals("bible.obj.Verse:[" + TEST_ID + " " + TEST_TRANSLATION_REFERENCE + " " + TEST_TEXT + "]", TEST_VERSE.toString());
 }

 @Test
 public void testConstructors() {
 Verse verse = new Verse(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 verse.complete(null);
 assertEquals(TEST_TEXT, verse.getText());
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testConstructorsWithDbResult() throws SQLException {
 Verse verse = new Verse(Database.Query("SELECT * FROM Verse WHERE Verse_ID=" + TEST_ID).getResult());
 assertEquals(TEST_ID, verse.getId());
 assertEquals(TEST_TEXT, verse.getText());
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test(expected = SQLException.class)
 public void testConstructorsWithNullDbResult() throws SQLException {
 new Verse(null);
 }
}