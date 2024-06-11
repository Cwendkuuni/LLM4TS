package bible.obj.test;

import static org.junit.Assert.*;

import java.util.*;
import static java.util.Arrays.asList;

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

 private static Verse verse1;
 private static Verse verse2;
 private static Translation translation;
 private static Reference reference;

 @BeforeClass
 public static void setUp() throws Exception {
 // Assuming the database has some data for testing
 translation = new Translation(1, "English");
 reference = new Reference(1, 1, 1, 1, 1, 1);
 verse1 = Verse.New(1);
 verse2 = Verse.New(2);
 }

 @Test
 public void testNew() {
 Verse verse3 = Verse.New(3);
 assertNull(verse3);

 Verse verse4 = new Verse(3);
 assertNotNull(verse4);
 assertEquals(3, verse4.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(translation, null, 1, 1);
 assertEquals(1, id);

 id = Verse.GetId(null, null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[] { translation }, null, null);
 assertEquals(2, verses.size());
 assertTrue(verses.contains(verse1));
 assertTrue(verses.contains(verse2));

 verses = Verse.Get(new Translation[] { translation }, reference, reference);
 assertEquals(2, verses.size());
 assertTrue(verses.contains(verse1));
 assertTrue(verses.contains(verse2));
 }

 @Test
 public void testGetIdFromDatabase() {
 int id = Verse.GetId(translation.getId(), 1, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 DbResult result = Database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1");
 Verse verse3 = new Verse(result);
 assertEquals(1, verse3.getId());
 assertEquals(verse1.getText(), verse3.getText());
 assertEquals(translation, verse3.getTranslationReference().getTranslation());
 assertEquals(reference, verse3.getReference());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidId() {
 Verse.GetId(-1, 1, 1, 1);
 }

 @Test
 public void testVersesFromDatabase() throws SQLException {
 List<Verse> verses = Verse.Get(null, null, null, null);
 assertEquals(0, verses.size());

 DbResult result = Database.Query("select Verse_ID from Verse where Book_ID = 1 and Chapter = 1 and Verse_No >= 1 and Verse_No <= 1");
 verses = new ArrayList(asList(Verse.New(result.getInt("Verse_ID"))));
 DbResult.Close(result);
 assertEquals(1, verses.size());
 assertEquals(verse1.getText(), verses.get(0).getText());
 }

}