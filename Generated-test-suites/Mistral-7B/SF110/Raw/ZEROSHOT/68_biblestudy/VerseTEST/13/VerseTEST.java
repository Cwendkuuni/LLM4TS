package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
 public void setUp() throws SQLException {
 connection = DriverManager.getConnection("jdbc:derby:test;create=true", "test", "test");
 Database.Init(connection);
 }

 @After
 public void tearDown() throws SQLException {
 connection.close();
 Database.Shutdown();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 Translation translation = new Translation(1, "test", null);
 Book book = new Book(1, "test", null);
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "test", null);
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 Verse verse = verses.get(0);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGet_noTranslations() {
 List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_outOfRange() {
 List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 2, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_multipleTranslations() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1, "test1", null);
 translations[1] = new Translation(2, "test2", null);
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGet_withTranslation() {
 Translation translation = new Translation(1, "test", null);
 Book book = new Book(1, "test", null);
 int id = Verse.GetId(translation, book, 1, 1);
 Verse verse = new Verse(id);
 TranslationReference transRef = verse.getTranslationReference();
 Translation expectedTranslation = translations[0];
 Translation actualTranslation = transRef.getTranslation();
 assertEquals(expectedTranslation.getId(), actualTranslation.getId());
 }

 @Test
 public void testGet_multipleTranslations() {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1, "test1", null);
 translations[1] = new Translation(2, "test2", null);
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 for (Verse verse : verses) {
 TranslationReference transRef = verse.getTranslationReference();
 Translation expectedTranslation = translations[Math.abs(verse.getId() % translations.length)];
 Translation actualTranslation = transRef.getTranslation();
 assertEquals(expectedTranslation.getId(), actualTranslation.getId());
 }
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "bible.obj.Verse:[1 test1:[1 test 1] test]";
 String actual = verse.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetId_existing() throws SQLException {
 int id = Verse.GetId(1, 1, 1, 1);
 assertTrue(id > 0);
 }

 @Test
 public void testGetId_nonexistent() throws SQLException {
 int id = Verse.GetId(1, 1, 1, 2);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_multipleTranslations() throws SQLException {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id1 = Verse.GetId(translationId, bookId, chapterId, verseId);
 int id2 = Verse.GetId(translationId + 1, bookId, chapterId, verseId);
 assertNotEquals(id1, id2);
 }

 @Test
 public void testGet() throws SQLException {
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 1);
 List<Verse> verses = Verse.Get(new Translation[0], begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_noVerses() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "test", null);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_outOfRange() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "test", null);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 2, 1);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_multipleVerses() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "test", null);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 3);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(3, verses.size());
 }

 @Test
 public void testGet_multipleTranslations() throws SQLException {
 Translation[] translations = new Translation[2];
 translations[0] = new Translation(1, "test1", null);
 translations[1] = new Translation(2, "test2", null);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 3);
 List<Verse> verses = Verse.Get(translations, begin, end);
 assertEquals(3, verses.size());
 for (Verse verse : verses) {
 TranslationReference transRef = verse.getTranslationReference();
 Translation expectedTranslation = translations[Math.abs(verse.getId() % translations.length)];
 Translation actualTranslation = transRef.getTranslation();
 assertEquals(expectedTranslation.getId(), actualTranslation.getId());
 }
 }
}