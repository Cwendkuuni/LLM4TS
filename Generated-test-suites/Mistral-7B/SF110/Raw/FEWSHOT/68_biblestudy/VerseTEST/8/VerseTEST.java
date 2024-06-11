package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.util.*;
import java.sql.*;
import bible.util.*;

public class VerseTest {

 private static final Verse NEW_VERSE_ID = new Verse(1);
 private static final Reference NEW_REFERENCE = new Reference(1, 1, 1);
 private static final TranslationReference NEW_TRANSLATION_REFERENCE = new TranslationReference(new Translation(1), NEW_REFERENCE);

 @Test
 public void testNew() {
 Verse verse = Verse.New(NEW_VERSE_ID.getId());
 assertEquals(NEW_VERSE_ID, verse);
 }

 @Test
 public void testGetId() {
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(null, null, 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), null, 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation[0], null, 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(null, new Book(1), 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), new Book(1), 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation[0], new Book(1), 1, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), null, 1, 2));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), new Book(1), 1, 2));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation[0], new Book(1), 1, 2));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), null, 2, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation(1), new Book(1), 2, 1));
 assertEquals(NEW_VERSE_ID.getId(), Verse.GetId(new Translation[0], new Book(1), 2, 1));
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[0], null, null);
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1) }, null, null);
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1) }, new Book(1), null);
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1) }, null, new Reference(1, 1, 1));
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1) }, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1), new Translation(2) }, null, null);
 assertEquals(2, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1), new Translation(2) }, new Book(1), null);
 assertEquals(2, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1), new Translation(2) }, null, new Reference(1, 1, 1));
 assertEquals(2, verses.size());

 verses = Verse.Get(new Translation[] { new Translation(1), new Translation(2) }, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetIdFromDatabase() {
 int id = Verse.GetId(new Translation(1), new Book(1), 1, 1);
 assertNotNull(id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdFromDatabaseEmptyResult() {
 Verse.GetId(new Translation(1), new Book(1), 1, 1);
 }

 @Test
 public void testGetIdFromDatabaseInvalidTranslation() {
 int id = Verse.GetId(-1, new Book(1), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseInvalidBook() {
 int id = Verse.GetId(new Translation(1), null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseInvalidChapterOrVerse() {
 int id = Verse.GetId(new Translation(1), new Book(1), 0, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdFromDatabaseMultipleResults() {
 int id1 = Verse.GetId(new Translation(1), new Book(1), 1, 1);
 int id2 = Verse.GetId(new Translation(1), new Book(1), 1, 1);
 assertEquals(id1, id2);
 }

 @Test
 public void testToString() {
 assertEquals("bible.obj.Verse:[1 bible.obj.TranslationReference[bible.obj.Translation[1] bible.util.Reference[1 1 1]] foo]",
 NEW_VERSE_ID.toString());
 }

 @Test
 public void testGetText() {
 assertEquals("foo", NEW_VERSE_ID.getText());
 }

 @Test
 public void testGetTranslationReference() {
 assertEquals(NEW_TRANSLATION_REFERENCE, NEW_VERSE_ID.getTranslationReference());
 }

 @Test
 public void testGetReference() {
 assertEquals(NEW_REFERENCE, NEW_VERSE_ID.getReference());
 }

 @Test
 public void testEquals() {
 Verse verse1 = new Verse(1);
 Verse verse2 = new Verse(1);
 assertEquals(verse1, verse2);

 Verse verse3 = new Verse(2);
 assertNotEquals(verse1, verse3);
 }

 @Test
 public void testHashCode() {
 int hashCode1 = NEW_VERSE_ID.hashCode();
 int hashCode2 = new Verse(1).hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testNewFromDatabase() throws SQLException {
 DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = 1");
 Verse verse = Verse.NewFromDatabase(result);
 assertEquals(NEW_VERSE_ID, verse);
 }

 @Test(expected = SQLException.class)
 public void testNewFromDatabaseEmptyResult() throws SQLException {
 Verse.NewFromDatabase(null);
 }

 @Test
 public void testComplete() {
 DbResult result = Database.Query("SELECT Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No FROM Verse WHERE Verse_ID = 1");
 Verse verse = new Verse(null);
 verse.complete(result);
 assertEquals(1, verse.getId());
 assertEquals("foo", verse.getText());
 assertEquals(NEW_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }
}