package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;

 @BeforeClass
 static public void setUp() throws SQLException {
 // Assuming you have a setup method to initialize the database with some data
 // for testing purposes
 // ...

 translation = new Translation(1);
 book = new Book(1, "Test Book");
 reference = new Reference(1, 1, 1);
 }

 // Testing constructor New
 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertEquals(1, verse.getId());
 }

 // Testing getId method
 @Test
 public void testGetId() {
 assertEquals(1, Verse.GetId(translation, book, 1, 1));
 }

 // Testing Get method with null translations
 @Test(expected = IllegalArgumentException.class)
 public void testGetNullTranslations() {
 Verse.Get(null, book.getId(), 1, 1);
 }

 // Testing Get method with empty translations array
 @Test
 public void testGetEmptyTranslations() {
 ArrayList<Verse> verses = Verse.Get(new Translation[0], book.getId(), 1, 1);
 assertEquals(0, verses.size());
 }

 // Testing Get method with valid parameters
 @Test
 public void testGet() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] { translation }, book.getId(), 1, 1);
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 // Testing Get method with begin and end references
 @Test
 public void testGetWithBeginAndEnd() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] { translation }, reference, new Reference(1, 1, 2));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 // Testing GetId method with null translation
 @Test(expected = IllegalArgumentException.class)
 public void testGetIdNullTranslation() {
 Verse.GetId(null, book.getId(), 1, 1);
 }

 // Testing GetId method with invalid translationId
 @Test(expected = IllegalArgumentException.class)
 public void testGetIdInvalidTranslationId() {
 Verse.GetId(-1, book.getId(), 1, 1);
 }

 // Testing GetId method with valid parameters
 @Test
 public void testGetId() {
 int id = Verse.GetId(translation.getId(), book.getId(), 1, 1);
 assertEquals(1, id);
 }

 // Testing private constructor Verse(int id)
 @Test(expected = IllegalAccessException.class)
 public void testPrivateConstructor() {
 new Verse(1);
 }

 // Testing method complete(DbResult result)
 @Test
 public void testComplete() throws SQLException {
 DbResult result = Database.Query("select 1 from Verse where Verse_ID = 1");
 Verse verse = new Verse();
 verse.complete(result);
 assertEquals(1, verse.getId());
 }

 // Testing method ReadId
 @Test
 public void testReadId() throws SQLException {
 int id = Verse.ReadId(translation.getId(), book.getId(), 1, 1);
 assertEquals(1, id);
 }

 // Testing static initializer
 @Test
 public void testStaticInitializer() {
 assertEquals(0, Verse.translations.size());
 }

}