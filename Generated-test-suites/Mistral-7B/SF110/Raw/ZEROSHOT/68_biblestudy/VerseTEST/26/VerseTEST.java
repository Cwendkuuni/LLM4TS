package bible.obj;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final String TEST_TEXT = "Test text.";
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(new Translation(TEST_TRANSLATION_ID), TEST_REFERENCE);

 @Test
 public void testNew() {
     Verse verse = Verse.New(TEST_VERSE_ID);
     assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = NullPointerException.class)
 public void testNewNullId() {
     Verse.New(null);
 }

 @Test
 public void testGetId() {
     int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
     Verse verse1 = new Verse(TEST_VERSE_ID);
     Verse verse2 = new Verse(TEST_VERSE_ID + 1);
     List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(1, verses.size());
     assertEquals(verse1, verses.get(0));

     verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(1, verses.size());
     assertEquals(verse1, verses.get(0));
 }

 @Test
 public void testGetWithTranslation() {
     Verse verse1 = new Verse(TEST_VERSE_ID);
     Verse verse2 = new Verse(TEST_VERSE_ID + 1);
     List<Verse> verses = Verse.Get(new Translation[] { new Translation(TEST_TRANSLATION_ID) }, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_TRANSLATION_REFERENCE);
     assertEquals(1, verses.size());
     assertEquals(verse1, verses.get(0));
 }

 @Test
 public void testGetWithTranslationEmpty() {
     List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_TRANSLATION_REFERENCE);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() throws SQLException {
     DbResult result = Database.Query("SELECT Verse_ID FROM Verse WHERE Book_ID = " + TEST_BOOK_ID + " AND Chapter = " + TEST_CHAPTER_ID + " AND Verse_No >= " + TEST_VERSE_ID + " AND Verse_No <= " + (TEST_VERSE_ID + 2));
     List<Verse> verses = Verse.Get(new Translation[0], TEST_REFERENCE, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 2));
     assertEquals(3, verses.size());
     for (Verse verse : verses) {
         int id = verse.getId();
         if (id == TEST_VERSE_ID) {
             assertEquals(TEST_TEXT, verse.getText());
             assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
         }
         if (id == TEST_VERSE_ID + 1) {
             assertEquals("Test text 1.", verse.getText());
             assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
         }
         if (id == TEST_VERSE_ID + 2) {
             assertEquals("Test text 2.", verse.getText());
             assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
         }
     }
     Database.Close(result);
 }

 @Test
 public void testGetIdFromDb() throws SQLException {
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGetIdWithNullTranslationId() {
     int id = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullBookId() {
     int id = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullChapterId() {
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
     assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullVerseId() {
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
     assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNonexistentVerse() {
     int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, Integer.MAX_VALUE);
     assertEquals(-1, id);
 }

 @Test
 public void testPrivateConstructor() {
     Verse verse = new Verse(-1);
     fail("Should not be able to instantiate Verse with negative ID.");
 }

 @Test
 public void testPrivateConstructorWithResult() throws SQLException {
     DbResult result = Database.Query("SELECT Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No FROM Verse WHERE Verse_ID = 1");
     Verse verse = new Verse(result);
     assertEquals(1, verse.getId());
     assertEquals(TEST_TEXT, verse.getText());
     assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
     Database.Close(result);
 }

 @Test
 public void testToString() {
     Verse verse = new Verse(TEST_VERSE_ID);
     String expected = "bible.obj.Verse:[1 Test bible.obj.TranslationReference@52324532 Test text.]";
     assertEquals(expected, verse.toString());
 }

 @Test
 public void testGetText() {
     Verse verse = new Verse(TEST_VERSE_ID);
     assertEquals(TEST_TEXT, verse.getText());
 }

 @Test
 public void testGetTranslationReference() {
     Verse verse = new Verse(TEST_VERSE_ID);
     assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testGetReference() {
     Verse verse = new Verse(TEST_VERSE_ID);
     Reference expected = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals(expected, verse.getReference());
 }

 @Test
 public void testGetIdFromTranslationReference() {
     int id = Verse.GetId(TEST_TRANSLATION_REFERENCE);
     assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGetFromTranslationReference() {
     Verse verse = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID, TEST_TRANSLATION_REFERENCE).get(0);
     assertEquals(TEST_VERSE_ID, verse.getId());
 }
}