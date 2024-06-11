package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

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

 private static DbResult result;
 static {
 Verse.translations = new HashMap<>();
 Verse.verseCache = new WeakHashMap<>();
 }

 @BeforeClass
 public static void setUpBeforeClass() throws SQLException {
 Database.Connect();
 }

 @AfterClass
 public static void tearDownAfterClass() throws SQLException {
 Database.Disconnect();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(Translation.NEW, new Book(1, "Genesis"), 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = Translation.NEW;
 ArrayList verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 1);
 assertEquals(1, verses.size());
 Verse verse = (Verse) verses.get(0);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetWithTranslations() {
 Translation[] translations = new Translation[2];
 translations[0] = Translation.NEW;
 translations[1] = Translation.NEW;
 ArrayList verses = Verse.Get(translations, new Book(1, "Genesis"), 1, 1);
 assertEquals(2, verses.size());
 Verse verse1 = (Verse) verses.get(0);
 assertEquals(1, verse1.getId());
 Verse verse2 = (Verse) verses.get(1);
 assertEquals(1, verse2.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetWithInvalidQuery() throws SQLException {
 Database.Query("select 1");
 fail("Expected SQLException but got none.");
 }

 @Test
 public void testGetWithEmptyResultSet() throws SQLException {
 result = Database.Query("select Verse_ID from Verse where 1=2");
 assertEquals(0, result.size());
 }

 @Test
 public void testGetWithSingleResultSet() throws SQLException {
 result = Database.Query("select * from Verse where Verse_ID = 1");
 assertNotNull(result.next());
 Verse verse = new Verse(result);
 assertEquals(1, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetWithNullResultSet() throws SQLException {
 result = null;
 new Verse(result);
 fail("Expected SQLException but got none.");
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, new Book(1, "Genesis"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, new Book(1, "Genesis"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithBookIdGreaterThanZero() {
 int id = Verse.GetId(Translation.NEW.getId(), new Book(3, "Genesis"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(Translation.NEW.getId(), new Book(1, "Genesis"), -1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(Translation.NEW.getId(), new Book(1, "Genesis"), 1, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithValidParameters() {
 int id = Verse.GetId(Translation.NEW.getId(), new Book(1, "Genesis"), 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testNewWithExistingId() {
 Verse verse1 = Verse.New(1);
 Verse verse2 = Verse.New(1);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testNewWithDifferentIds() {
 Verse verse1 = Verse.New(1);
 Verse verse2 = Verse.New(2);
 assertNotEquals(verse1, verse2);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "Verse:[1 bible.obj.TranslationReference[Translation:[1 bible.obj.Book:[1 Genesis], 1], Reference:[1, 1, 1]] " +
         "bible.obj.String@5b9d61f2]";
 assertEquals(expected, verse.toString());
 }

 @Test
 public void testGetReferenceWithNullVerse() {
 assertNull(Verse.New(1).getReference());
 }

 @Test
 public void testGetReferenceWithValidVerse() {
 Verse verse = Verse.New(1);
 Reference expected = new Reference(1, 1, 1);
 Reference result = verse.getReference();
 assertEquals(expected, result);
 }

}