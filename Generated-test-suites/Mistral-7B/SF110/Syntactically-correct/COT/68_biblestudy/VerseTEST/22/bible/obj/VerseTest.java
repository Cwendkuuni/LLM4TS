package bible.obj.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerseTest {

 private static Connection connection;

 @BeforeClass
 public static void setup() throws SQLException {
 connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "sa");
 Database.Setup(connection);
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 assertEquals(id, Verse.GetId(new Translation(translationId), new Book(bookId), chapterId, verseId));
 }

 @Test
 public void testGet() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);

 ArrayList verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGet_NullTranslations() {
 ArrayList verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyTranslations() {
 Translation[] translations = {};
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginReference() {
 Reference begin = new Reference(-1, 1, 1);
 ArrayList verses = Verse.Get(new Translation[0], begin, new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidEndReference() {
 Reference end = new Reference(1, -1, 1);
 ArrayList verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), end);
 assertEquals(0, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidBeginAndEndReference() {
 Reference begin = new Reference(-1, -1, -1);
 Reference end = new Reference(-1, 1, 1);
 ArrayList verses = Verse.Get(new Translation[0], begin, end);
 }

 @Test
 public void testGet_NoVersesFound() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(2, 1, 1);
 Reference end = new Reference(2, 1, 2);

 ArrayList verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetId_NullTranslation() {
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(null, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidBookId() {
 int translationId = 1;
 int bookId = -1;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidChapterId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = -1;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidVerseId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = -1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_NotFound() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 2;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationNotFound() {
 int translationId = 2;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationBookNotFound() {
 int translationId = 1;
 int bookId = 2;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationChapterNotFound() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 2;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationVerseNotFound() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 3;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 assertEquals(-1, id);
 }

 @Test(expected = SQLException.class)
 public void testGetId_DatabaseError() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;

 int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
 fail("Should have thrown SQLException");
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "bible.obj.Verse:[1 " + new TranslationReference(new Translation(1), new Reference(1, 1, 1)) + " " + "]";
 assertEquals(expected, verse.toString());
 }
}