package bible.obj;

import static org.junit.Assert.*;
import static bible.util.Util.ToString;

import java.sql.DbResult;
import java.sql.SQLException;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;
 private static Verse verse;

 @BeforeClass
 public static void setup() throws SQLException {
 // Assuming a setup method to initialize the database and create some data for testing
 // You may need to replace it with the actual database setup method
 Database.Initialize();

 translation = Translation.New(1);
 book = new Book(1, "Genesis");
 reference = new Reference(1, 1, 1);
 }

 @Test
 public void testNew() {
 Verse v1 = Verse.New(1);
 assertEquals(1, v1.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] { translation }, book, reference);
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetReference() {
 Reference ref = verse.getReference();
 assertEquals(reference, ref);
 }

 @Test
 public void testGetTranslationReference() {
 TranslationReference transRef = verse.getTranslationReference();
 assertEquals(translation, transRef.getTranslation());
 assertEquals(reference, transRef.getReference());
 }

 @Test
 public void testToString() {
 verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 [bible.obj.TranslationReference:[1 bible.obj.Reference:[1 1 Genesis 1:1]]]] foo]", verse.toString());
 }

 @Test
 public void testGet() throws SQLException {
 ArrayList<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetIdWithInvalidTranslation() {
 int id = Verse.GetId(-1, book.getId(), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBook() {
 int id = Verse.GetId(translation.getId(), -1, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapter() {
 int id = Verse.GetId(translation.getId(), book.getId(), -1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerse() {
 int id = Verse.GetId(translation.getId(), book.getId(), 1, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, book.getId(), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullBook() {
 int id = Verse.GetId(translation.getId(), null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullChapter() {
 int id = Verse.GetId(translation.getId(), book.getId(), null, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullVerse() {
 int id = Verse.GetId(translation.getId(), book.getId(), 1, null);
 assertEquals(-1, id);
 }

 @Test
 public void testGetVersesWithNullTranslationArray() {
 ArrayList<Verse> verses = Verse.Get(null, book.getId(), 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithNullBeginReference() {
 ArrayList<Verse> verses = Verse.Get(translation, null, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithNullEndReference() {
 ArrayList<Verse> verses = Verse.Get(translation, book, null, new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidBeginReference() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(-1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidEndReference() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(-1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidBeginBook() {
 ArrayList<Verse> verses = Verse.Get(translation, new Book(-1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidEndBook() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, -1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidBeginChapter() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, -1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidEndChapter() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, -1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidBeginVerse() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, -1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidEndVerse() {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(1, 1, -1));
 assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetVersesWithDatabaseError() throws SQLException {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(0, verses.size());
 Database.Close(); // Closing the connection should throw an SQLException
 ArrayList<Verse> verses2 = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 }

 @Test
 public void testGetVerses() throws SQLException {
 ArrayList<Verse> verses = Verse.Get(translation, book, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }
}