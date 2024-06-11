package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

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

 private static Verse verse;
 private static Translation translation;
 private static Book book;

 @BeforeClass
 public static void setup() throws SQLException, ClassNotFoundException {
 Database.Initialize();
 translation = new Translation(1);
 book = new Book(1, "Test Book", 1);
 }

 @Test
 public void testNew() {
 int id = 1;
 verse = Verse.New(id);
 assertEquals("Verse:[1 null null]", verse.toString());
 }

 @Test
 public void testGetText() {
 verse = new Verse(1);
 assertEquals("", verse.getText());
 }

 @Test
 public void testGetTranslationReference() {
 verse = new Verse(1);
 TranslationReference transRef = verse.getTranslationReference();
 Reference reference = verse.getReference();
 assertEquals(translation.toString() + ":" + reference.toString(), verse.toString());
 }

 @Test
 public void testGetId() {
 verse = new Verse(1);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetValue() {
 verse = new Verse(1);
 assertEquals("", verse.getValue());
 assertEquals("", verse.getText());
 }

 @Test
 public void testGetReference() {
 verse = new Verse(1);
 Reference reference = verse.getReference();
 assertEquals(reference.getBookId(), 1);
 assertEquals(reference.getChapterNo(), 0);
 assertEquals(reference.getVerseNo(), 1);
 }

 @Test
 public void testToString() {
 verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 " + translation.toString() + " " + reference.toString() + "]", verse.toString());
 }

 @Test
 public void testGetIdStatic() {
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetStatic() {
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals("Verse:[1 " + translation.toString() + " " + new Reference(1, 1, 1) + "]", verses.get(0).toString());
 }

 @Test
 public void testGetStaticWithTranslation() {
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetStaticWithNullTranslation() {
 List<Verse> verses = Verse.Get(null, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithEmptyArray() {
 List<Verse> verses = Verse.Get(new Translation[0], book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithNullBook() {
 List<Verse> verses = Verse.Get(translation, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithEmptyBook() {
 List<Verse> verses = Verse.Get(translation, new Book[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithNullReference() {
 List<Verse> verses = Verse.Get(translation, book, null, new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithEmptyReference() {
 List<Verse> verses = Verse.Get(translation, book, new Reference[0], new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithNullBegin() {
 List<Verse> verses = Verse.Get(translation, book, null, null);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetStaticWithEmptyEnd() {
 List<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), null);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetIdStaticWithNullTranslation() {
 int id = Verse.GetId(null, book, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithNullBook() {
 int id = Verse.GetId(translation, null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithNullChapterId() {
 int id = Verse.GetId(translation, book, 0, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithNullVerseId() {
 int id = Verse.GetId(translation, book, 1, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithInvalidTranslationId() {
 int id = Verse.GetId(-1, book, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithInvalidBookId() {
 int id = Verse.GetId(translation, new Book(0, "", 0), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithInvalidChapterId() {
 int id = Verse.GetId(translation, book, 0, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithInvalidVerseId() {
 int id = Verse.GetId(translation, book, 1, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdStaticWithValidParameters() {
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetWithNullTranslationArray() {
 List<Verse> verses = Verse.Get(null, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 List<Verse> verses = Verse.Get(new Translation[0], book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithNullBook() {
 List<Verse> verses = Verse.Get(translation, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithEmptyBook() {
 List<Verse> verses = Verse.Get(translation, new Book[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithNullBegin() {
 List<Verse> verses = Verse.Get(translation, book, null, new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithEmptyEnd() {
 List<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), null);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithNullTranslation() {
 List<Verse> verses = Verse.Get(null, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithEmptyTranslationReference() {
 List<Verse> verses = Verse.Get(translation, book, new Reference[0], new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidBegin() {
 List<Verse> verses = Verse.Get(translation, book, new Reference(-1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidEnd() {
 List<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(-1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithInvalidBeginAndEnd() {
 List<Verse> verses = Verse.Get(translation, book, new Reference(-1, 1, 1), new Reference(-1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetWithValidParameters() throws SQLException {
 List<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }
}