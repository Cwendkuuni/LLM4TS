package bible.obj;

import static org.junit.Assert.*;

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
 private static final int TEST_ID = 1;
 private static final String TEST_TEXT = "Test Text";
 private static final String TEST_BOOK_NAME = "Test Book";
 private static final int TEST_CHAPTER_NO = 1;
 private static final int TEST_VERSE_NO = 1;
 private static final int TEST_REFERENCE_ID = 1;

 private static Verse verse;
 private static Translation translation;
 private static Reference reference;
 private static ArrayList verses;

 @Before
 public void setUp() throws Exception {
 translation = new Translation(TEST_TRANSLATION_ID, TEST_BOOK_NAME);
 reference = new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO);
 verse = new Verse(TEST_ID);
 verse.setText(TEST_TEXT);
 verse.setTranslationReference(new TranslationReference(translation, reference));
 verses = new ArrayList();
 verses.add(verse);
 }

 @After
 public void tearDown() throws Exception {
 Database.Close();
 verse = null;
 translation = null;
 reference = null;
 verses = null;
 }

 @Test
 public void testNew() {
 Verse newVerse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, newVerse.getId());
 assertEquals(TEST_TEXT, newVerse.getText());
 assertEquals(translation, newVerse.getTranslationReference().getTranslation());
 assertEquals(reference, newVerse.getTranslationReference().getReference());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(translation, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGet() throws SQLException {
 List<Verse> versesFromDatabase = Verse.Get(new Translation[] { translation }, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO), new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(1, versesFromDatabase.size());
 assertEquals(TEST_ID, versesFromDatabase.get(0).getId());
 assertEquals(TEST_TEXT, versesFromDatabase.get(0).getText());
 assertEquals(translation, versesFromDatabase.get(0).getTranslationReference().getTranslation());
 assertEquals(reference, versesFromDatabase.get(0).getTranslationReference().getReference());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 int idFromDatabase = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_ID, idFromDatabase);
 }

 @Test(expected = SQLException.class)
 public void testGetIdInvalidTranslationId() throws SQLException {
 int idFromDatabase = Verse.GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 fail("Should throw SQLException but no exception was thrown.");
 }

 @Test(expected = SQLException.class)
 public void testGetIdInvalidBookId() throws SQLException {
 int idFromDatabase = Verse.GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 fail("Should throw SQLException but no exception was thrown.");
 }

 @Test(expected = SQLException.class)
 public void testGetIdInvalidChapterId() throws SQLException {
 int idFromDatabase = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 fail("Should throw SQLException but no exception was thrown.");
 }

 @Test(expected = SQLException.class)
 public void testGetIdInvalidVerseId() throws SQLException {
 int idFromDatabase = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 fail("Should throw SQLException but no exception was thrown.");
 }

 @Test
 public void testGet() throws SQLException {
 verses = Verse.Get(new Translation[] { translation }, null, null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), null, null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO), null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO), new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 verses = Verse.Get(new Translation[] { translation }, null, null, null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), null, null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO), null);
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, new Bible.obj.Book(TEST_BOOK_ID, TEST_BOOK_NAME), null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_CHAPTER_NO), new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(0, verses.size());
 verses = Verse.Get(new Translation[] { translation }, null, null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_NO, TEST_VERSE_NO));
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 assertEquals(TEST_TEXT, verses.get(0).getText());
 assertEquals(translation, verses.get(0).getTranslationReference().getTranslation());
 assertEquals(reference, verses.get(0).getTranslationReference().getReference());
 }
}