package bible.obj.test;

import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Identifible;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerseTest {

 private static final String DB_URL = "jdbc:derby:memory:testdb";
 private static final String DB_USER = "app";
 private static final String DB_PASSWORD = "app";

 @BeforeClass
 public static void setupClass() throws SQLException {
 Database.connect(DB_URL, DB_USER, DB_PASSWORD);
 Database.createTables();
 }

 @AfterClass
 public static void teardownClass() throws SQLException {
 Database.disconnect();
 }

 @Before
 public void setup() {
 // Initialize Verse translations and cache
 Verse.translations = new HashMap<>();
 Verse.verseCache = new WeakHashMap<>();
 }

 @After
 public void teardown() {
 // Clear Verse translations and cache
 Verse.translations.clear();
 Verse.verseCache.clear();
 }

 @Test
 public void testNew() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetId() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 Verse verse = new Verse(1);
 int id = Verse.GetId(translation, book, chapter, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Chapter(1, new Book(1, "Genesis"), 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslation() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, null);
 assertEquals(1, verses.size());
 TranslationReference transRef = verses.get(0).getTranslationReference();
 assertEquals(translation.getId(), transRef.getTranslation().getId());
 assertEquals(chapter.getReference(), transRef.getReference());
 }

 @Test
 public void testGetWithNullTranslation() {
 List<Verse> verses = Verse.Get(null, new Book(1, "Genesis"), new Chapter(1, new Book(1, "Genesis"), 1), null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 List<Verse> verses = Verse.Get(new Translation[0], new Book(1, "Genesis"), new Chapter(1, new Book(1, "Genesis"), 1), null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidTranslationId() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookId() {
 Translation translation = new Translation(1);
 Book book = new Book(0, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidChapterId() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(0, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerseId() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, new Reference(1, 0, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidReference() {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, null, null, begin, end);
 assertEquals(2, verses.size());
 Verse firstVerse = verses.get(0);
 assertEquals(1, firstVerse.getId());
 Verse secondVerse = verses.get(1);
 assertEquals(2, secondVerse.getId());
 }

 @Test
 public void testGetWithBeginButNoEnd() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(1, 1, 1);
 List<Verse> verses = Verse.Get(translations, null, null, begin);
 assertEquals(1, verses.size());
 }

 @Test
 public void testGetWithEndButNoBegin() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference end = new Reference(1, 1, 2);
 List<Verse> verses = Verse.Get(translations, null, null, end);
 assertEquals(1, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginReference() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(0, 1, 1);
 List<Verse> verses = Verse.Get(translations, null, null, begin);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndReference() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference end = new Reference(1, 0, 1);
 List<Verse> verses = Verse.Get(translations, null, null, null, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginAndEndReferences() {
 Translation[] translations = {new Translation(1), new Translation(2)};
 Reference begin = new Reference(0, 1, 1);
 Reference end = new Reference(1, 0, 1);
 List<Verse> verses = Verse.Get(translations, null, null, begin, end);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndFromDifferentTranslations() {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 Book book = new Book(1, "Genesis");
 Chapter chapter = new Chapter(1, book, 1);
 List<Verse> verses = Verse.Get(new Translation[0], book, chapter, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 Verse firstVerse = verses.get(0);
 assertEquals(translation1.getId(), firstVerse.getTranslationReference().getTranslation().getId());
 Verse secondVerse = verses.get(1);
 assertEquals(translation2.getId(), secondVerse.getTranslationReference().getTranslation().getId());
 }
}