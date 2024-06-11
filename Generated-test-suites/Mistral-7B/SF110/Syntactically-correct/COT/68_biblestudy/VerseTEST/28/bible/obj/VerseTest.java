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

 private static final String TEST_DATABASE_URL = "jdbc:your_database_url_here";
 private static final int TEST_DATABASE_PORT = 0; // use default port if 0
 private static final String TEST_DATABASE_USER = "your_database_username";
 private static final String TEST_DATABASE_PASSWORD = "your_database_password";

 @BeforeClass
 public static void setup() {
 Database.connect(TEST_DATABASE_URL, TEST_DATABASE_PORT, TEST_DATABASE_USER, TEST_DATABASE_PASSWORD);
 }

 @AfterClass
 public static void teardown() {
 Database.disconnect();
 }

 // Test for New method
 @Test
 public void testNew() {
 final int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 // Test for getId method
 @Test
 public void testGetId() {
 final Translation translation = new Translation();
 final Book book = new Book();
 final int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(id, Verse.New(id).getId());
 }

 // Test for Get method with no translations
 @Test
 public void testGetEmpty() {
 final Translation[] translations = new Translation[0];
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 final ArrayList verses = Verse.Get(translations, new Reference(bookId, chapterId, verseId));
 assertEquals(0, verses.size());
 }

 // Test for Get method with valid translations
 @Test
 public void testGetValid() throws SQLException {
 final Translation translation = new Translation();
 final Book book = new Book();
 final Reference begin = new Reference(book.getId(), 1, 1);
 final Reference end = new Reference(book.getId(), 1, 1);
 final ArrayList verses = Verse.Get(new Translation[] { translation }, begin, end);
 assertEquals(1, verses.size());
 }

 // Test for Get method with overlapping references
 @Test(expected = IllegalArgumentException.class)
 public void testGetOverlapping() throws SQLException {
 final Translation translation1 = new Translation();
 final Translation translation2 = new Translation();
 final Book book = new Book();
 final Reference begin = new Reference(book.getId(), 1, 1);
 final Reference end1 = new Reference(book.getId(), 1, 2);
 final Reference end2 = new Reference(book.getId(), 1, 2);
 Verse.Get(new Translation[] { translation1, translation2 }, begin, end1);
 Verse.Get(new Translation[] { translation1, translation2 }, begin, end2); // should throw exception
 }

 // Test for Get method with null translations
 @Test(expected = NullPointerException.class)
 public void testGetNullTranslations() {
 final Reference begin = new Reference(1, 1, 1);
 final Reference end = new Reference(1, 1, 1);
 Verse.Get(null, begin, end); // should throw exception
 }

 // Test for GetId method with invalid translation id
 @Test
 public void testGetIdInvalidTranslationId() {
 final int id = -1;
 final Book book = new Book();
 final int chapterId = 1;
 final int verseId = 1;
 final int result = Verse.GetId(-1, book.getId(), chapterId, verseId);
 assertEquals(-1, result);
 }

 // Test for GetId method with null book
 @Test(expected = NullPointerException.class)
 public void testGetIdNullBook() {
 final int translationId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 final int result = Verse.GetId(translationId, null, chapterId, verseId);
 assertEquals(-1, result);
 }

 // Test for GetId method with null chapter
 @Test(expected = NullPointerException.class)
 public void testGetIdNullChapter() {
 final int translationId = 1;
 final int bookId = 1;
 final int verseId = 1;
 final int result = Verse.GetId(translationId, new HashMap(), verseId);
 assertEquals(-1, result);
 }

 // Test for GetId method with null verseId
 @Test(expected = NullPointerException.class)
 public void testGetIdNullVerseId() {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int result = Verse.GetId(translationId, new HashMap(), null);
 assertEquals(-1, result);
 }

 // Test for GetId method with missing verse in database
 @Test
 public void testGetIdMissingVerse() {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 2;
 final int result = Verse.GetId(translationId, new HashMap(), chapterId, verseId);
 assertEquals(-1, result);
 }

 // Test for New method with null database connection
 @Test(expected = SQLException.class)
 public void testNewNullDatabaseConnection() throws SQLException {
 Database.disconnect(); // disconnect to simulate null connection
 final int id = 1;
 try {
 Verse.New(id);
 fail("Should have thrown SQLException");
 }
 catch (SQLException e) {
 // expected
 }
 finally {
 Database.connect(TEST_DATABASE_URL, TEST_DATABASE_PORT, TEST_DATABASE_USER, TEST_DATABASE_PASSWORD);
 }
 }

}