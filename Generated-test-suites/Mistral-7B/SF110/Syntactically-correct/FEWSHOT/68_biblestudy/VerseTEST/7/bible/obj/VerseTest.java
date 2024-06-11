package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class VerseTest {

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Reference reference = new Reference(1, 1, 1);
 Verse verse = new Verse(1);

 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);

 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslationAndBook() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 Book book = new Book(1, "Genesis");
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 1);

 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithAllParameters() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1);
 translations[1] = new Translation(2);
 Book book = new Book(1, "Genesis");
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);

 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, new Book(1, "Genesis"), 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullBook() {
 int id = Verse.GetId(new Translation(1), null, 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullChapter() {
 int id = Verse.GetId(new Translation(1), new Book(1, "Genesis"), null, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullVerse() {
 int id = Verse.GetId(new Translation(1), new Book(1, "Genesis"), 1, null);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, new Book(1, "Genesis"), 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(new Translation(1), new Book(0, "Genesis"), 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(new Translation(1), new Book(1, "Genesis"), -1, 1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(new Translation(1), new Book(1, "Genesis"), 1, -1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithValidParameters() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;

 int id = Verse.GetId(translationId, new Book(bookId, "Genesis"), chapterId, verseId);
 assertEquals(expectedId, id);
 }

 @Test
 public void testNewWithDatabaseQuery() throws SQLException {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());

 DbResult result = null;
 String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1";
 try {
 result = Database.Query(query);
 result.next();
 Verse verseFromDatabase = new Verse(result);
 assertEquals(verse, verseFromDatabase);
 }
 catch (SQLException e) {
 e.printStackTrace();
 }
 finally {
 DbResult.Close(result);
 }
 }

 @Test
 public void testNewWithDatabaseQueryAndInvalidId() throws SQLException {
 String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = -1";
 try {
 DbResult result = Database.Query(query);
 result.next();
 fail("An Exception should be thrown when trying to create a new Verse with an invalid ID.");
 }
 catch (SQLException e) {
 // Expected
 }
 finally {
 DbResult.Close(result);
 }
 }
}