package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.sql.DatabaseMetaData;
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

 private static final String DB_URL = "jdbc:derby:memory:VerseTestDB";
 private Connection connection;

 @Before
 public void setUp() throws SQLException {
 connection = DriverManager.getConnection(DB_URL);
 Statement statement = connection.createStatement();
 String sql = "CREATE TABLE Verse (" +
 "Verse_ID INT PRIMARY KEY," +
 "Translation_ID INT," +
 "Book_ID INT," +
 "Chapter INT," +
 "Verse_No INT," +
 "Text VARCHAR(255)";
 statement.execute(sql);
 }

 @After
 public void tearDown() throws SQLException {
 Statement statement = connection.createStatement();
 String sql = "DROP TABLE Verse";
 statement.execute(sql);
 connection.close();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 Translation translation = new Translation(1, "testTranslation");
 Book book = new Book(1, "testBook");
 Reference reference = new Reference(1, 1, 1);
 Verse verse = new Verse(1);
 int id = Verse.getId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "testTranslation");
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 List<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithNullReference() {
 List<Verse> verses = Verse.Get(null, null, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 Statement statement = connection.createStatement();
 String sql = "INSERT INTO Verse (Translation_ID, Book_ID, Chapter, Verse_No, Text) VALUES (1, 1, 1, 1, 'test')";
 statement.executeUpdate(sql);
 int id = Verse.getId(new Translation(1, "testTranslation"), new Book(1, "testBook"), 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetVersesFromDatabase() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "testTranslation");
 Statement statement = connection.createStatement();
 String sql = "INSERT INTO Verse (Translation_ID, Book_ID, Chapter, Verse_No, Text) VALUES " +
 "(" + translations[0].getId() + ", 1, 1, 1, 'test1')," +
 "(" + translations[0].getId() + ", 1, 1, 2, 'test2')";
 statement.executeUpdate(sql);
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 3));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetVersesWithEmptyResultSet() throws SQLException {
 List<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 3));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithNullResultSet() throws SQLException {
 Statement statement = connection.createStatement();
 Statement statement2 = connection.createStatement();
 ResultSet resultSet = null;
 List<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 3), statement2, resultSet);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithEmptyTranslationArrayAndNullReference() throws SQLException {
 List<Verse> verses = Verse.Get(null, null, null, connection);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithEmptyTranslationArray() throws SQLException {
 Translation[] translations = new Translation[0];
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 3), connection);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithNullTranslationArray() throws SQLException {
 List<Verse> verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 3), connection);
 assertEquals(0, verses.size());
 }
}