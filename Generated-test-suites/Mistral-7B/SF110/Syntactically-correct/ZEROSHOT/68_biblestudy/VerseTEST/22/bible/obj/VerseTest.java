package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.Util;

public class VerseTest {

 private Connection connection;

 @Before
 public void setUp() throws Exception {
 connection = DriverManager.getConnection("jdbc:derby:test;create=true", "root", "password");
 Statement statement = connection.createStatement();
 String sql = "CREATE TABLE Verse ("
 + "Verse_ID INT PRIMARY KEY,"
 + "Text VARCHAR(255),"
 + "Translation_ID INT,"
 + "Book_ID INT,"
 + "Chapter INT,"
 + "Verse_No INT)";
 statement.executeUpdate(sql);
 }

 @After
 public void tearDown() throws Exception {
 connection.close();
 }

 @Test
 public void testNew() throws Exception {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetId() throws Exception {
 int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;
 int result = Verse.getId(null, null, bookId, chapterId, verseId);
 assertEquals(-1, result);

 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Test Book", 1);
 Reference reference = new Reference(bookId, chapterId, verseId);
 int id1 = Verse.getId(translation, book, chapterId, verseId);
 assertEquals(id1, Verse.getId(translation.getId(), bookId, chapterId, verseId));
 }

 @Test
 public void testGet() throws Exception {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");

 Verse expected = new Verse(1);
 expected.setText("Expected Text");
 expected.getTranslationReference().setReference(reference);
 expected.getTranslationReference().getReference().setBook(book);
 expected.getTranslationReference().getReference().setChapter(chapter);
 expected.getTranslationReference().getReference().setVerse(verse);

 List<Verse> verses = Verse.Get(translations, book, chapter, verse);
 assertEquals(1, verses.size());
 assertEquals(expected, verses.get(0));
 }

 @Test
 public void testGetIdWithTranslations() throws Exception {
 int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;

 Translation translation = new Translation(translationId, "English");
 Book book = new Book(bookId, "Test Book", 1);
 Reference reference = new Reference(bookId, chapterId, verseId);

 int id = Verse.getId(translation, book, chapterId, verseId);
 assertEquals(id, Verse.GetId(translation, bookId, chapterId, verseId));
 }

 @Test
 public void testGetWithTranslations() throws Exception {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");

 Book book = new Book(1, "Test Book", 1);
 Reference begin = new Reference(bookId, chapterId, verseId);
 Reference end = new Reference(bookId, chapterId, verseId);

 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());

 String sql = "INSERT INTO Verse (Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES (1, 'Test Text', 1, 1, 1, 1)";
 statement.executeUpdate(sql);

 verses = Verse.Get(translations, begin, end);
 assertEquals(1, verses.size());
 Verse verse = verses.get(0);
 assertEquals(1, verse.getId());
 assertEquals("Test Text", verse.getText());
 assertEquals(translation, verse.getTranslationReference().getTranslation());
 assertEquals(reference, verse.getReference());
 }

 @Test
 public void testGetWithNoTranslations() throws Exception {
 List<Verse> verses = Verse.Get(null, bookId, chapterId, verseId);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginEndOutOfRange() throws Exception {
 List<Verse> verses = Verse.Get(translations, new Reference(bookId, 1, 1), new Reference(bookId, 2, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginEndInRange() throws Exception {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");

 Book book = new Book(1, "Test Book", 1);
 Reference begin = new Reference(bookId, chapterId, verseId);
 Reference end = new Reference(bookId, chapterId, verseId + 1);

 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(1, verses.size());
 }

 private Book book = new Book(1, "Test Book", 1);
 private Reference chapter = new Reference(bookId, chapterId, verseId);
 private Reference verse = new Reference(bookId, chapterId, verseId);

 @Test
 public void testGetIdStatic() throws Exception {
 int result = Verse.GetId(null, null, bookId, chapterId, verseId);
 assertEquals(-1, result);

 Translation translation = new Translation(translationId, "English");
 int id = Verse.GetId(translation, book, chapterId, verseId);
 assertEquals(id, Verse.GetId(translation.getId(), bookId, chapterId, verseId));
 }

 @Test
 public void testGetStaticWithTranslations() throws Exception {
 int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;

 Translation translation = new Translation(translationId, "English");
 Book book = new Book(bookId, "Test Book", 1);
 Reference reference = new Reference(bookId, chapterId, verseId);

 int id = Verse.GetId(translation, book, chapterId, verseId);
 assertEquals(id, Verse.GetId(translation.getId(), bookId, chapterId, verseId));
 }

 @Test
 public void testGetStaticWithNoTranslations() throws Exception {
 int result = Verse.GetId(null, null, bookId, chapterId, verseId);
 assertEquals(-1, result);
 }

 @Test
 public void testGetStaticWithBeginEndOutOfRange() throws Exception {
 List<Verse> verses = Verse.Get(translations, new Reference(bookId, 1, 1), new Reference(bookId, 2, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetStaticWithBeginEndInRange() throws Exception {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");

 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());

 String sql = "INSERT INTO Verse (Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No) VALUES (1, 'Test Text', 1, 1, 1, 1)";
 statement.executeUpdate(sql);

 verses = Verse.Get(translations, begin, end);
 assertEquals(1, verses.size());
 Verse verse = verses.get(0);
 assertEquals(1, verse.getId());
 assertEquals("Test Text", verse.getText());
 assertEquals(translation, verse.getTranslationReference().getTranslation());
 assertEquals(reference, verse.getReference());
 }

 @Test
 public void testToString() throws Exception {
 int translationId = 1, bookId = 1, chapterId = 1, verseId = 1;

 Translation translation = new Translation(translationId, "English");
 Book book = new Book(bookId, "Test Book", 1);
 Reference reference = new Reference(bookId, chapterId, verseId);
 Verse verse = new Verse(translationId, bookId, chapterId, verseId, "Test Text");

 String expected = "Verse:[1 TestText:English:Test Book:1:1:1]";
 assertEquals(expected, verse.toString());
 }
}