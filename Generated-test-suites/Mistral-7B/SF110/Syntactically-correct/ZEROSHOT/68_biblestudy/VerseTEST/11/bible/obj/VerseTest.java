package bible.obj.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static Verse verse1;
 private static Verse verse2;
 private static Verse verse3;
 private static List<Translation> translations;
 private static Translation translation1;
 private static Translation translation2;
 private static Reference reference1;
 private static Reference reference2;
 private static int id1;
 private static int id2;
 private static int id3;

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 Translation.initDB();
 // Create Translations
 translation1 = new Translation("Name1", "Language1");
 translation2 = new Translation("Name2", "Language2");
 translations = new ArrayList<Translation>();
 translations.add(translation1);
 translations.add(translation2);
 // Create References
 reference1 = new Reference(1, 1, 1);
 reference2 = new Reference(2, 1, 2);
 // Create Verses
 id1 = Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), 1, 1);
 id2 = Verse.GetId(translation2, new Bible.obj.Book(1, "Book1"), 1, 1);
 id3 = Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), 1, 2);
 verse1 = new Verse(id1);
 verse2 = new Verse(id2);
 verse3 = new Verse(id3);
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 Database.closeConnection();
 }

 @Test
 public void testNew() {
 Verse verse4 = Verse.New(id1);
 assertEquals(verse1.getId(), verse4.getId());
 }

 @Test
 public void testGetId() {
 assertEquals(id1, verse1.getId());
 assertEquals(id2, verse2.getId());
 assertEquals(id3, verse3.getId());
 }

 @Test
 public void testGetText() {
 fail("Method not yet implemented"); // TODO
 }

 @Test
 public void testGetTranslationReference() {
 TranslationReference transRef1 = verse1.getTranslationReference();
 TranslationReference transRef2 = verse2.getTranslationReference();
 TranslationReference transRef3 = verse3.getTranslationReference();
 Reference ref1 = transRef1.getReference();
 Reference ref2 = transRef2.getReference();
 Reference ref3 = transRef3.getReference();
 assertEquals(translation1.getId(), transRef1.getTranslation().getId());
 assertEquals(translation1.getName(), transRef1.getName());
 assertEquals(translation1.getLanguage(), transRef1.getLanguage());
 assertEquals(ref1, transRef1.getReference());
 assertEquals(translation2.getId(), transRef2.getTranslation().getId());
 assertEquals(translation2.getName(), transRef2.getName());
 assertEquals(translation2.getLanguage(), transRef2.getLanguage());
 assertEquals(ref2, transRef2.getReference());
 assertEquals(translation1.getId(), transRef3.getTranslation().getId());
 assertEquals(ref1, transRef3.getReference());
 }

 @Test
 public void testToString() {
 String expected = "bible.obj.Verse:[1 VerseReference[1 BookReference[1 Book{1 Book1 Name:Book1}, 1, 1], 1, 1] Name1 Language1 Text:...";
 assertEquals(expected, verse1.toString());
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Bible.obj.Book(1, "Book1"), 1, 1);
 assertEquals(2, verses.size());
 assertTrue(verses.contains(verse1));
 assertTrue(verses.contains(verse2));
 }

 @Test
 public void testGetStatic() {
 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Bible.obj.Book(1, "Book1"), 1, 1);
 List<Verse> verses2 = Verse.Get(translations.toArray(new Translation[0]), new Bible.obj.Book(1, "Book1"), 1, 1);
 assertEquals(verses, verses2);
 }

 @Test
 public void testGet() throws SQLException {
 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(3, verses.size());
 assertTrue(verses.contains(verse1));
 assertTrue(verses.contains(verse2));
 assertTrue(verses.contains(verse3));
 }

 @Test (expected = SQLException.class)
 public void testGetThrowsException() throws Exception {
 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(1, 1, 3), new Reference(1, 1, 2));
 fail("An exception should be thrown");
 }

 @Test
 public void testGetIdStatic() {
 assertEquals(id1, Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), 1, 1));
 assertEquals(id2, Verse.GetId(translation2, new Bible.obj.Book(1, "Book1"), 1, 1));
 assertEquals(id3, Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), 1, 2));
 }

 @Test
 public void testGetIdStaticWithNullTranslation() {
 assertEquals(-1, Verse.GetId(null, new Bible.obj.Book(1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdStaticWithNullBook() {
 assertEquals(-1, Verse.GetId(translation1, null, 1, 1));
 }

 @Test
 public void testGetIdStaticWithNullChapter() {
 assertEquals(-1, Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), null, 1));
 }

 @Test
 public void testGetIdStaticWithNullVerse() {
 assertEquals(-1, Verse.GetId(translation1, new Bible.obj.Book(1, "Book1"), 1, null));
 }

}