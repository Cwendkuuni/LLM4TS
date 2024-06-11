package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_ID = 1;
 private static final int TEST_OTHER_ID = 2;
 private static final String TEST_TEXT = "Test Text";
 private static final String TEST_TRANSLATION_NAME = "Test Translation";
 private static final String TEST_BOOK_NAME = "Test Book";
 private static final String TEST_REFERENCE_BOOK_NAME = "Test Reference Book";
 private static final String TEST_REFERENCE_CHAPTER_NAME = "Test Reference Chapter";
 private static final int TEST_REFERENCE_CHAPTER_NO = 1;
 private static final int TEST_REFERENCE_VERSE_NO = 1;
 private static final int TEST_REFERENCE_ID = 1;

 @BeforeClass
 public static void setUpBeforeClass() throws SQLException, ClassNotFoundException {
 Database.initialize();
 }

 @AfterClass
 public static void tearDownAfterClass() throws SQLException {
 Database.clearAll();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertNotNull(verse);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(new Translation[]{new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME)}, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetWithTranslationAndReference() {
 List<Verse> verses = Verse.Get(new Translation[]{new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME)}, new Reference(TEST_REFERENCE_BOOK_NAME, TEST_REFERENCE_CHAPTER_NAME, TEST_REFERENCE_CHAPTER_NO, TEST_REFERENCE_VERSE_NO, TEST_REFERENCE_ID), new Reference(TEST_BOOK_NAME, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(2, verses.size());

 Verse verse1 = verses.get(0);
 assertEquals(TEST_ID, verse1.getId());
 assertEquals(TEST_TRANSLATION_ID, verse1.getTranslationReference().getTranslation().getId());
 assertEquals(TEST_BOOK_ID, verse1.getReference().getBookId());
 assertEquals(TEST_CHAPTER_ID, verse1.getReference().getChapterNo());
 assertEquals(TEST_VERSE_ID, verse1.getReference().getVerseNo());

 Verse verse2 = verses.get(1);
 assertEquals(TEST_OTHER_ID, verse2.getId());
 assertEquals(TEST_TRANSLATION_ID, verse2.getTranslationReference().getTranslation().getId());
 assertEquals(TEST_BOOK_ID, verse2.getReference().getBookId());
 assertEquals(TEST_CHAPTER_ID, verse2.getReference().getChapterNo());
 assertEquals(TEST_VERSE_ID + 1, verse2.getReference().getVerseNo());
 }

 @Test
 public void testGetWithNullTranslationArray() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_ID);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(TEST_ID, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullTranslation() {
 Verse.GetId(null, new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetIdWithNullBookReference() {
 Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), null);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(TEST_ID);
 assertEquals("bible.obj.Verse:[" + TEST_ID + " " + new TranslationReference(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)) + " " + TEST_TEXT + "]", verse.toString());
 }

 @Test
 public void testGet() throws SQLException {
 Verse verse = Verse.Get(new Translation[]{new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME)}, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_ID)[0];
 assertEquals(TEST_TEXT, verse.getText());
 }

 @Test
 public void testGetWithEmptyTranslationMap() {
 HashMap translationMap = new HashMap();
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), translationMap);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetWithNullTranslationMapKey() {
 int id = Verse.GetId(null, new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), null);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetWithEmptyTranslationMapValue() {
 HashMap translationMap = new HashMap();
 translationMap.put(new Integer(TEST_TRANSLATION_ID), null);
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), translationMap);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetWithNullTranslationMap() {
 int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID, TEST_TRANSLATION_NAME), new Bible.obj.Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), null);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testNewWithDatabaseQuery() throws SQLException {
 Verse verse = Verse.New(TEST_ID);
 assertTrue(verse.getId() > 0);

 DbResult dbResult = Database.Query("SELECT * FROM Verse WHERE Verse_ID = " + TEST_ID);
 dbResult.next();

 int dbId = dbResult.getInt("Verse_ID");
 assertEquals(TEST_ID, dbId);
 }

 @Test
 public void testNewWithNullDatabaseQueryResult() {
 Verse verse = Verse.New(TEST_ID);
 assertTrue(verse.getId() <= 0);
 }

 @Test(expected = SQLException.class)
 public void testNewWithEmptyDatabaseQueryResult() throws SQLException {
 Verse.New(TEST_ID);
 fail("Should throw SQLException");
 }
}