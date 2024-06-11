package bible.obj.test;

import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_NEW_ID = 2;

 @Before
 public void setUp() throws Exception {
 Database.Initialize();
 }

 @After
 public void tearDown() throws Exception {
 Database.Shutdown();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertNotNull(verse);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[] { TEST_TRANSLATION }, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslation() {
 Translation translation = new Translation(TEST_TRANSLATION_ID);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = { TEST_TRANSLATION };
 List<Verse> verses = Verse.Get(translations, new Reference(TEST_BOOK_ID - 1, TEST_CHAPTER_ID, TEST_VERSE_ID - 1), new Reference(TEST_BOOK_ID + 1, TEST_CHAPTER_ID + 1, TEST_VERSE_ID + 1));
 assertEquals(3, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidBegin() {
 Translation[] translations = { TEST_TRANSLATION };
 List<Verse> verses = Verse.Get(translations, new Reference(TEST_BOOK_ID - 1, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID + 1, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Expected an IllegalArgumentException but no exception was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidEnd() {
 Translation[] translations = { TEST_TRANSLATION };
 List<Verse> verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID + 1, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Expected an IllegalArgumentException but no exception was thrown");
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 DbResult result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = " + TEST_NEW_ID);
 assertTrue(result.next());
 int idFromDatabase = result.getInt("Verse_ID");
 assertEquals(TEST_NEW_ID, idFromDatabase);
 }

 private Translation TEST_TRANSLATION = new Translation(TEST_TRANSLATION_ID);

}