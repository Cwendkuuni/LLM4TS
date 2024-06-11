package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
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

 private static final String DB_URL = "jdbc:derby:memory:testdb";
 private static Connection connection;

 @Before
 public void setUp() throws SQLException {
 connection = DriverManager.getConnection(DB_URL);
 Statement statement = connection.createStatement();
 String createTableQuery = "CREATE TABLE Verse (" +
 "Verse_ID INTEGER PRIMARY KEY," +
 "Text VARCHAR(255)," +
 "Translation_ID INTEGER," +
 "Book_ID INTEGER," +
 "Chapter INTEGER," +
 "Verse_No INTEGER)";
 statement.executeUpdate(createTableQuery);
 }

 @After
 public void tearDown() throws SQLException {
 connection.close();
 }

 @Test
 public void testNew() {
 Verse verse1 = Verse.New(1);
 assertNotNull(verse1);
 Verse verse2 = Verse.New(1);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.getId(translationId, bookId, chapterId, verseId);
 assertEquals(id, Verse.GetId(new Translation(translationId), new Book(bookId), chapterId, verseId));
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 1);
 assertEquals(1, verses.size());
 assertEquals(verses.get(0).getId(), 1);
 }

 @Test
 public void testGetWithInvalidChapter() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), 0, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerse() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 0);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithMultipleVerses() throws SQLException {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1);
 translations[1] = new Translation(2);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 1);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetWithMultipleVersesAndTranslation() throws SQLException {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1);
 translations[1] = new Translation(2);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 1);
 Verse verse1 = verses.get(0);
 Verse verse2 = verses.get(1);
 assertEquals(verse1.getId(), 1);
 assertEquals(verse2.getId(), 2);
 assertEquals(verse1.getTranslationReference().getTranslation().getId(), 1);
 assertEquals(verse2.getTranslationReference().getTranslation().getId(), 2);
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 int id = Verse.getId(translation.getId(), book.getId(), 1, 1);
 Verse verse = new Verse(id);
 assertEquals(verse.getId(), id);
 assertEquals(verse.getText(), "This is a test verse.");
 }

 @Test
 public void testGetFromDatabaseWithMultipleVerses() throws SQLException {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 Book book = new Book(1, "Genesis");
 int id1 = Verse.getId(translation1.getId(), book.getId(), 1, 1);
 int id2 = Verse.getId(translation2.getId(), book.getId(), 1, 2);
 List<Verse> verses = Verse.Get(new Translation[] {translation1, translation2}, book, 1, 1);
 Verse verse1 = verses.get(0);
 Verse verse2 = verses.get(1);
 assertEquals(verse1.getId(), id1);
 assertEquals(verse2.getId(), id2);
 assertEquals(verse1.getText(), "This is the first test verse.");
 assertEquals(verse2.getText(), "This is the second test verse.");
 }
}