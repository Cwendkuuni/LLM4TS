package bible.obj;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseTest {

 private static Connection connection;

 @BeforeClass
 static public void setUp() throws SQLException {
 try {
 Class.forName("com.mysql.cj.jdbc.Driver");
 connection = DriverManager.getConnection(
 "jdbc:mysql://localhost:3306/test", "username", "password");
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testNew() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 int id = Verse.getId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(expectedId, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 Verse verse = verses.get(0);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetWithTranslation() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, null, null, null);
 assertEquals(0, verses.size());

 translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses2 = Verse.Get(translations, null, null, null);
 assertEquals(0, verses2.size());

 translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses3 = Verse.Get(translations, new Book(1, "Genesis"), null, null);
 assertEquals(0, verses3.size());

 translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses4 = Verse.Get(translations, null, new Reference(1, 1, 1), null);
 assertEquals(0, verses4.size());

 translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses5 = Verse.Get(translations, null, null, new Reference(1, 1, 1));
 assertEquals(0, verses5.size());

 translations = new Translation[2];
 translations[0] = new Translation(1);
 translations[1] = new Translation(2);
 List<Verse> verses6 = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(2, verses6.size());
 }

 @Test
 public void testGetWithInvalidReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 2), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithMissingTranslation() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(2);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithMissingBook() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithMissingTranslationAndBook() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(2);
 List<Verse> verses = Verse.Get(translations, null, null, new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testStaticInitialization() {
 HashMap translations = Verse.translations;
 assertTrue(translations.isEmpty());
 WeakHashMap verseCache = Verse.verseCache;
 assertTrue(verseCache.isEmpty());
 }

 @Test
 public void testGetIdWithMissingTranslation() {
 int translationId = 2;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.getId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithMissingChapter() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 0;
 int verseId = 1;
 int id = Verse.getId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithMissingVerse() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 0;
 int id = Verse.getId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithValidData() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 int id = Verse.getId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(expectedId, id);
 }

 @Test
 public void testNewWithInvalidId() {
 int id = -1;
 Verse verse = Verse.New(id);
 assertNull(verse);
 }

 @Test
 public void testNewWithValidId() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testNewWithCachedVerse() {
 int id = 1;
 Verse verse1 = Verse.New(id);
 Verse verse2 = Verse.New(id);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testNewWithNewVerse() {
 int id = 1;
 Verse verse1 = Verse.New(id);
 Verse verse2 = new Verse(id);
 assertNotSame(verse1, verse2);
 }

 @Test
 public void testToString() {
 int id = 1;
 String text = "This is a test verse.";
 TranslationReference transRef = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
 Verse verse = new Verse(id, text, transRef);
 String expectedString = "bible.obj.Verse:[1 bible.obj.TranslationReference[bible.obj.Translation[1] bible.obj.Reference[Genesis 1 1]] This is a test verse.]";
 assertEquals(expectedString, verse.toString());
 }

 @Test
 public void testGetTranslationReference() {
 int id = 1;
 String text = "This is a test verse.";
 Translation translation = new Translation(1);
 Reference reference = new Reference(1, 1, 1);
 Verse verse = new Verse(id, text, new TranslationReference(translation, reference));
 TranslationReference transRef = verse.getTranslationReference();
 assertEquals(translation, transRef.getTranslation());
 assertEquals(reference, transRef.getReference());
 }

 @Test
 public void testGetReference() {
 int id = 1;
 String text = "This is a test verse.";
 Translation translation = new Translation(1);
 Reference reference = new Reference(1, 1, 1);
 Verse verse = new Verse(id, text, new TranslationReference(translation, reference));
 Reference expectedReference = reference;
 Reference reference2 = verse.getReference();
 assertEquals(expectedReference, reference2);
 }

 @Test
 public void testGetReferenceWithNullTranslationReference() {
 Verse verse = new Verse(1);
 Reference reference = verse.getReference();
 assertNull(reference);
 }

 @Test
 public void testGetText() {
 int id = 1;
 String text = "This is a test verse.";
 TranslationReference transRef = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
 Verse verse = new Verse(id, text, transRef);
 String expectedText = "This is a test verse.";
 String text2 = verse.getText();
 assertEquals(expectedText, text2);
 }

 @Test
 public void testGetIdWithNullTranslationReference() {
 Verse verse = new Verse(1);
 int id = verse.getId();
 assertEquals(1, id);
 }

 @Test
 public void testGetIdWithValidData() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 Verse verse = new Verse(translationId, bookId, chapterId, verseId);
 int id = verse.getId();
 assertEquals(expectedId, id);
 }

 @Test
 public void testComplete() throws SQLException {
 String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1";
 Statement statement = connection.createStatement();
 ResultSet resultSet = statement.executeQuery(query);
 resultSet.next();
 int id = resultSet.getInt("Verse_ID");
 String text = resultSet.getString("Text");
 int translationId = resultSet.getInt("Translation_ID");
 int bookId = resultSet.getInt("Book_ID");
 int chapter = resultSet.getInt("Chapter");
 int verseNo = resultSet.getInt("Verse_No");
 Verse verse = new Verse(id);
 verse.complete(resultSet);
 int verseId = verse.getId();
 assertEquals(id, verseId);
 assertEquals(text, verse.getText());
 assertEquals(new TranslationReference(new Translation(translationId), new Reference(bookId, chapter, verseNo)), verse.getTranslationReference());
 }

 @Test
 public void testCompleteWithNullResultSet() {
 Verse verse = new Verse(1);
 try {
 verse.complete(null);
 fail("Expected SQLException not thrown");
 } catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithInvalidResultSet() {
 Verse verse = new Verse(1);
 try {
 verse.complete(new DbResult() {
 @Override
 public boolean next() {
 return false;
 }

 @Override
 public int getInt(String columnLabel) throws SQLException {
 return -1;
 }

 @Override
 public String getString(String columnLabel) throws SQLException {
 return null;
 }

 @Override
 public Statement getStatement() throws SQLException {
 return null;
 }

 @Override
 public String getString(int columnIndex) throws SQLException {
 return null;
 }

 @Override
 public ResultSetMetaData getMetaData() throws SQLException {
 return null;
 }

 @Override
 public void close() throws SQLException {
 // No-op
 }
 });
 fail("Expected SQLException not thrown");
 } catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testReadIdWithMissingTranslation() {
 int translationId = 2;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithMissingBook() {
 int translationId = 1;
 int bookId = 0;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithMissingChapter() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 0;
 int verseId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithMissingVerse() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 0;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithValidData() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(expectedId, id);
 }

 @Test
 public void testReadIdWithNullTranslationId() {
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.ReadId(0, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidBookId() {
 int translationId = 1;
 int bookId = 0;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidChapterId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 0;
 int verseId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidVerseId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 0;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithValidData() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
 assertEquals(expectedId, id);
 }

 @Test
 public void testStaticMethods() {
 String columns = Verse.COLUMNS;
 String expectedColumns = "Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No";
 assertEquals(expectedColumns, columns);
 }

 @Test
 public void testStaticMethodsWithMissingTranslation() {
 HashMap translations = Verse.translations;
 assertTrue(translations.isEmpty());
 }

 @Test
 public void testStaticMethodsWithMissingVerseCache() {
 WeakHashMap verseCache = Verse.verseCache;
 assertTrue(verseCache.isEmpty());
 }
}