package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static final String DB_URL = "jdbc:derby:test;create=true";
 private static Connection connection;

 @BeforeClass
 public static void initDatabase() throws SQLException {
 connection = Database.getConnection(DB_URL);
 Database.createTables();
 }

 @AfterClass
 public static void cleanupDatabase() throws SQLException {
 Database.dropTables();
 Database.closeConnection(connection);
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
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
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslation() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(translation, verses.get(0).getTranslationReference().getTranslation());
 }

 @Test
 public void testGetWithMultipleTranslations() {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 Book book = new Book(1, "Genesis");
 List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, book, new Reference(1, 1, 1));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Reference> references = new ArrayList<>();
 references.add(new Reference(1, 1, 1));
 references.add(new Reference(1, 1, 2));
 List<Verse> verses = Verse.Get(translations, null, references.get(0), references.get(1));
 assertEquals(2, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidBeginReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Reference> references = new ArrayList<>();
 references.add(new Reference(0, 1, 1));
 Verse.Get(translations, null, references.get(0), references.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidEndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Reference> references = new ArrayList<>();
 references.add(new Reference(1, 1, 1));
 references.add(new Reference(1, 0, 1));
 Verse.Get(translations, null, references.get(0), references.get(1));
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "Verse:[1 bible.obj.TestVerse@1b6d3586 [Translation:[1 com.github.test.TestTranslation@4c6d6222 Reference:[Genesis 1 1]] TestText]]";
 assertEquals(expected, verse.toString());
 }
}