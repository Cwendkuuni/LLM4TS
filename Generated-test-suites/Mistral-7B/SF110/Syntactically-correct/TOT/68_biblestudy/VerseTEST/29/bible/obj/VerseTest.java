import org.junit.jupiters.json.Json;
import org.junit.jupiters.json.JsonProvider;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import bible.obj.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseTest {

 private static final String DB_URL = "jdbc:derby:test;create=true";
 private static Connection connection;

 @org.junit.BeforeClass
 public static void setup() throws SQLException {
 connection = DriverManager.getConnection(DB_URL);
 Statement statement = connection.createStatement();
 statement.execute("CREATE TABLE Verse (Verse_ID INT PRIMARY KEY, Translation_ID INT, Book_ID INT, Chapter INT, Verse_No INT, Text VARCHAR(100))");
 }

 @org.junit.AfterClass
 public static void teardown() throws SQLException {
 connection.close();
 }

 @Test
 public void testNew_ValidId() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNew_NullId() {
 Verse verse = Verse.New(null);
 assertNull(verse);
 }

 @Test
 public void testGetId_ValidTranslationBookChapterVerse() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetId_InvalidTranslationId() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(null, book, 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetId_NullBook() {
 Translation translation = new Translation(1, "English");
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(translation, null, 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetId_NullChapter() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = null;
 int id = Verse.GetId(translation, book, 1, 1);
 assertNull(id);
 }

 @Test
 public void testGetId_NullVerse() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(translation, book, 1, 0);
 assertNull(id);
 }

 @Test
 public void testGet_ValidTranslationBookChapterVerse() {
 Translation[] translations = { new Translation(1, "English") };
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis", 1), 1, 1);
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 Translation[] translations = {};
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis", 1), 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBook() {
 Translation[] translations = { new Translation(1, "English") };
 List<Verse> verses = Verse.Get(translations, null, 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidChapter() {
 Translation[] translations = { new Translation(1, "English") };
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis", 1), 0, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidVerse() {
 Translation[] translations = { new Translation(1, "English") };
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis", 1), 1, 0);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_ValidTranslationBeginEnd() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGet_EmptyTranslationBeginEnd() {
 Translation[] translations = {};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginBook() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(0, 1, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginChapter() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 0, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginVerse() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 0);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidEndBook() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(0, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidEndChapter() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 0, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidEndVerse() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 0);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "bible.obj.Verse:[1 null nullnull]";
 assertEquals(expected, verse.toString());
 }

 @Test
 public void testGetReference() {
 Verse verse = new Verse(1);
 Reference reference = verse.getReference();
 String expected = "TranslationReference{Translation{TranslationID=1, Name=English}, Reference{BookID=1, Chapter=1, VerseNo=1}}";
 assertEquals(expected, JsonProvider.printf(Json.PRETTY_PRINT, reference).getJsonText());
 }

 @Test
 public void testVersesEqual() {
 Verse verse1 = new Verse(1);
 Verse verse2 = new Verse(1);
 assertTrue(verse1.equals(verse2));
 }

 @Test
 public void testVersesNotEqual() {
 Verse verse1 = new Verse(1);
 Verse verse2 = new Verse(2);
 assertFalse(verse1.equals(verse2));
 }

 @Test
 public void testVersesCache() {
 Verse verse1 = new Verse(1);
 Verse verse2 = Verse.New(1);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testTranslations() {
 Translation[] translations = { new Translation(1, "English") };
 Map<Integer, Map<Integer, Integer>> translationsMap = new HashMap<>();
 translationsMap.put(1, new HashMap<Integer, Integer>() {{
 put(1, new Integer(1));
 put(2, null);
 }} );
 assertEquals(translationsMap, Verse.translations);
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 Statement statement = connection.createStatement();
 int dbId = statement.executeUpdate("INSERT INTO Verse VALUES (" + translationId + ", " + bookId + ", " + chapterId + ", " + verseId + ", 'Test Text')") > 0 ? statement.executeQuery("SELECT Verse_ID FROM Verse WHERE Translation_ID = " + translationId + " AND Book_ID = " + bookId + " AND Chapter = " + chapterId + " AND Verse_No = " + verseId).getInt("Verse_ID") : -1;
 assertEquals(id, dbId);
 }

 @Test(expected = SQLException.class)
 public void testGetIdFromDatabase_Error() throws SQLException {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 0;
 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 }
}