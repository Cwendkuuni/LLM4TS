package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.Test;

import bible.obj.*;
import bible.util.DbResult;
import bible.util.Database;
import bible.util.Reference;
import bible.util.Translation;
import bible.util.TranslationReference;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;

 @Test
 public void testNew() {
     Verse verse = Verse.New(TEST_VERSE_ID);
     assertNotNull(verse);
 }

 @Test
 public void testGetId() {
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(1, verses.size());
     Verse verse = (Verse) verses.get(0);
     assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test
 public void testGetWithNullTranslations() {
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslations() {
     Translation[] translations = new Translation[0];
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidTranslationId() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(-1);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookId() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidChapterId() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, -1, TEST_VERSE_ID);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerseId() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidParameters() {
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID);
     assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetWithException() throws SQLException {
     Database.setThrowException(true);
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @Test
 public void testGetWithNullDatabase() {
     Database database = null;
     Translation[] translations = new Translation[1];
     translations[0] = new Translation(TEST_TRANSLATION_ID);
     ArrayList verses = Verse.Get(translations, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testToString() {
     Verse verse = new Verse(TEST_VERSE_ID);
     String result = verse.toString();
     String expected = "bible.obj.Verse:[" + TEST_VERSE_ID + " null null]";
     assertEquals(expected, result);
 }

 @Test
 public void testGetText() {
     Verse verse = new Verse(TEST_VERSE_ID);
     String text = verse.getText();
     assertNull(text);
 }

 @Test
 public void testGetTranslationReference() {
     Translation translation = new Translation(TEST_TRANSLATION_ID);
     Reference reference = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     Verse verse = new Verse(TEST_VERSE_ID);
     TranslationReference transRef = verse.getTranslationReference();
     Reference ref = transRef.getReference();
     assertEquals(reference.getBookId(), ref.getBookId());
     assertEquals(reference.getChapterNo(), ref.getChapterNo());
     assertEquals(reference.getVerseNo(), ref.getVerseNo());
 }

 @Test
 public void testGetValue() {
     Verse verse = new Verse(TEST_VERSE_ID);
     String value = verse.getValue();
     assertEquals(verse.getText(), value);
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
     Database database = new Database();
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithException() throws SQLException {
     Database database = new Database();
     Database.setThrowException(true);
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
 }

 @Test
 public void testGetWithDatabaseResults() throws SQLException {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + TEST_VERSE_ID);
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(1, verses.size());
     Verse verse = (Verse) verses.get(0);
     assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetWithEmptyDatabaseResults() throws SQLException {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + TEST_VERSE_ID);
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
 }

 @Test
 public void testGetWithInvalidTranslationId() {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Translation_ID = -1");
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookId() {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Book_ID = -1");
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidChapterId() {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Chapter = -1");
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerseId() {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_No = -1");
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidParameters() {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Book_ID = 1 and Chapter = 1 and Verse_No = 1 and Verse_ID = 1");
     ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID, database);
     assertEquals(0, verses.size());
 }

 @Test
 public void testNewFromDatabase() throws SQLException {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + TEST_VERSE_ID);
     Verse verse = Verse.New(result.getInt("Verse_ID"));
     assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testNewFromDatabaseWithException() throws SQLException {
     Database database = new Database();
     Database.setThrowException(true);
     Verse verse = Verse.New(TEST_VERSE_ID);
 }

 @Test
 public void testNewFromDatabaseWithNullResult() {
     Database database = new Database();
     Verse verse = Verse.New(TEST_VERSE_ID);
     assertNull(verse);
 }

 @Test
 public void testComplete() throws SQLException {
     Database database = new Database();
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + TEST_VERSE_ID);
     Verse verse = new Verse(result);
     assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testCompleteWithException() throws SQLException {
     Database database = new Database();
     Database.setThrowException(true);
     DbResult result = database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + TEST_VERSE_ID);
     new Verse(result);
 }

 @Test
 public void testReadId() throws SQLException {
     int id = Verse.ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test(expected = SQLException.class)
 public void testReadIdWithException() throws SQLException {
     Database database = new Database();
     Database.setThrowException(true);
     int id = Verse.ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @Test
 public void testStaticInitialization() {
     assertNotNull(Verse.translations);
     assertNotNull(Verse.verseCache);
 }
}