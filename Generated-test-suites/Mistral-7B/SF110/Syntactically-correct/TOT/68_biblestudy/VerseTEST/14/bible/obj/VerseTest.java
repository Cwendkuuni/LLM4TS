package bible.obj;

import static org.junit.Assert.*;
import static bible.util.Util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

 private static final String DB_URL = "jdbc:derby:test;create=true";
 private static Connection connection;

 @Before
 public void setUp() throws SQLException {
 connection = DriverManager.getConnection(DB_URL);
 Statement statement = connection.createStatement();
 statement.execute("CREATE TABLE IF NOT EXISTS Verse (" +
 "Verse_ID INTEGER PRIMARY KEY," +
 "Text VARCHAR(100)," +
 "Translation_ID INTEGER," +
 "Book_ID INTEGER," +
 "Chapter INTEGER," +
 "Verse_No INTEGER) ");
 }

 @After
 public void tearDown() throws SQLException {
 connection.close();
 Statement statement = connection.createStatement();
 statement.execute("DROP TABLE IF EXISTS Verse");
 }

 @Test
 public void testNewWithExistingId() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testNewWithNonExistingId() {
 int id = 100;
 Verse verse = Verse.New(id);
 assertNull(verse);
 }

 @Test
 public void testGetWithValidArguments() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 31);
 int chapterId = 1, verseId = 1;
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book.getId(), chapterId, verseId);
 Verse expected = new Verse(1);
 assertEquals(expected, verses.get(0));
 }

 @Test
 public void testGetWithNullTranslations() {
 List<Verse> verses = Verse.Get(null, 1, 1, 1);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithEmptyTranslations() {
 Translation[] translations = {};
 List<Verse> verses = Verse.Get(translations, 1, 1, 1);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidBookId() {
 Translation translation = new Translation(1, "English");
 List<Verse> verses = Verse.Get(new Translation[] { translation }, 0, 1, 1);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidChapterId() {
 Translation translation = new Translation(1, "English");
 List<Verse> verses = Verse.Get(new Translation[] { translation }, 1, 0, 1);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidVerseId() {
 Translation translation = new Translation(1, "English");
 List<Verse> verses = Verse.Get(new Translation[] { translation }, 1, 1, 0);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithMultipleTranslations() throws SQLException {
 // Set up data
 Statement statement = connection.createStatement();
 statement.executeUpdate("INSERT INTO Verse(Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES('Verse 1', 1, 1, 1, 1)");;
 statement.executeUpdate("INSERT INTO Verse(Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES('Verse 2', 2, 1, 1, 1)");;
 // Test
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 31);
 int chapterId = 1, verseId = 1;
 List<Verse> verses = Verse.Get(new Translation[] { translation, new Translation(2, "France") }, book.getId(), chapterId, verseId);
 // Check result
 assertEquals(2, verses.size());
 Verse verse1 = verses.get(0);
 assertEquals(1, verse1.getId());
 assertEquals("Verse 1", verse1.getText());
 Verse verse2 = verses.get(1);
 assertEquals(2, verse2.getId());
 assertEquals("Verse 2", verse2.getText());
 }

 @Test
 public void testGetWithBeginAndEndReference() {
 // Set up data
 Statement statement = connection.createStatement();
 statement.executeUpdate("INSERT INTO Verse(Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES('Verse 1', 1, 1, 1, 1)");
 statement.executeUpdate("INSERT INTO Verse(Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES('Verse 2', 1, 1, 1, 2)");
 statement.executeUpdate("INSERT INTO Verse(Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES('Verse 3', 1, 1, 1, 3)");
 // Test
 Translation[] translations = {new Translation(1, "English")};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 3);
 List<Verse> verses = Verse.Get(translations, begin, end);
 // Check result
 assertEquals(3, verses.size());
 Verse verse1 = verses.get(0);
 assertEquals(1, verse1.getId());
 assertEquals("Verse 1", verse1.getText());
 Verse verse2 = verses.get(1);
 assertEquals(2, verse2.getId());
 assertEquals("Verse 2", verse2.getText());
 Verse verse3 = verses.get(2);
 assertEquals(3, verse3.getId());
 assertEquals("Verse 3", verse3.getText());
 }

 @Test
 public void testGetIdWithValidArguments() {
 int translationId = 1;
 int bookId = 1, chapterId = 1, verseId = 1;
 int id = Verse.GetId(new Translation(translationId, "English"), new Book(bookId, "Genesis", 31), chapterId, verseId);
 assertEquals(1, id);
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int bookId = 1, chapterId = 1, verseId = 1;
 int id = Verse.GetId(null, new Book(bookId, "Genesis", 31), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullBook() {
 int translationId = 1, chapterId = 1, verseId = 1;
 int id = Verse.GetId(new Translation(translationId, "English"), null, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int translationId = 1, chapterId = 1, verseId = 1;
 int id = Verse.GetId(new Translation(translationId, "English"), new Book(0, "Genesis", 31), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int translationId = 1, bookId = 1, verseId = 1;
 int id = Verse.GetId(new Translation(translationId, "English"), new Book(bookId, "Genesis", 31), 0, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int translationId = 1, bookId = 1, chapterId = 1;
 int id = Verse.GetId(new Translation(translationId, "English"), new Book(bookId, "Genesis", 31), chapterId, 0);
 assertEquals(-1, id);
 }
}