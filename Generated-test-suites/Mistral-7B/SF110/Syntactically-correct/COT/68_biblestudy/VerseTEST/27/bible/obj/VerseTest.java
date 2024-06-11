import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_OTHER_TRANSLATION_ID = 2;
 private static final int TEST_OTHER_BOOK_ID = 2;
 private static final int TEST_OTHER_CHAPTER_ID = 2;
 private static final int TEST_OTHER_VERSE_ID = 2;
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(new Translation(TEST_TRANSLATION_ID), TEST_REFERENCE);

 @Test
 public void testNew() {
     Verse verse1 = Verse.New(TEST_ID);
     assertEquals("Unexpected Verse object.", verse1, Verse.New(TEST_ID));
 }

 @Test(expected = NullPointerException.class)
 public void testNewNullId() {
     Verse.New(null);
 }

 @Test
 public void testGetId() {
     int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", TEST_ID, id);
 }

 @Test
 public void testGet() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID), new Translation(TEST_OTHER_TRANSLATION_ID)};
     ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected number of verses.", 1, verses.size());
     Verse verse1 = (Verse) verses.get(0);
     assertEquals("Unexpected Verse object.", TEST_ID, verse1.getId());
 }

 @Test
 public void testGetWithEmptyTranslations() {
     ArrayList verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected number of verses.", 0, verses.size());
 }

 @Test
 public void testGetWithEmptyBook() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID)};
     ArrayList verses = Verse.Get(translations, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected number of verses.", 0, verses.size());
 }

 @Test
 public void testGetInvalidId() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID), new Translation(TEST_OTHER_TRANSLATION_ID)};
     ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, Integer.MIN_VALUE);
     assertEquals("Unexpected number of verses.", 0, verses.size());
 }

 @Test
 public void testGetEmptyDatabaseResult() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID), new Translation(TEST_OTHER_TRANSLATION_ID)};
     DbResult dbResult = new DbResult();
     ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID, dbResult);
     assertEquals("Unexpected number of verses.", 0, verses.size());
 }

 @Test
 public void testGetWithDuplicateId() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID), new Translation(TEST_TRANSLATION_ID)};
     ArrayList verses = Verse.Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected number of verses.", 1, verses.size());
 }

 @Test
 public void testGetEmptyTranslationMap() {
     int id = Verse.GetId(null, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test
 public void testGetEmptyBookMap() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), null, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test
 public void testGetEmptyChapterMap() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), null, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test
 public void testGetIdNullTranslation() {
     int id = Verse.GetId(null, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test
 public void testGetIdNullBook() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), null, TEST_CHAPTER_ID, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test
 public void testGetIdNullChapter() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), null, TEST_VERSE_ID);
     assertEquals("Unexpected ID.", -1, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdInvalidChapter() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), -1, TEST_VERSE_ID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdInvalidVerse() {
     int id = Verse.GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, -1);
 }

 @Test
 public void testGetDatabaseQuery() {
     String query = Verse.GetDatabaseQuery(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     String expected = "select Verse_ID from Verse where Translation_ID in (1) and Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 1 and Verse_No >= 1 and Verse_No <= 1";
     assertEquals("Unexpected database query.", expected, query);
 }

 @Test
 public void testGetDatabaseQueryWithTranslations() {
     String query = Verse.GetDatabaseQuery(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
     String expected = "select Verse_ID from Verse where Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 1 and Verse_No >= 1 and Verse_No <= 1";
     assertEquals("Unexpected database query.", expected, query);
 }

 @Test
 public void testGetDatabaseQueryWithEmptyDatabaseResult() {
     Translation[] translations = {new Translation(TEST_TRANSLATION_ID)};
     String query = Verse.GetDatabaseQuery(translations, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
     String expected = "select Verse_ID from Verse where Book_ID is null and Chapter is null and Verse_No is null";
     assertEquals("Unexpected database query.", expected, query);
 }

 @Test
 public void testConstructorWithId() throws SQLException {
     Verse verse1 = new Verse(TEST_ID);
     assertEquals("Unexpected Verse object ID.", TEST_ID, verse1.getId());
 }

 @Test(expected = SQLException.class)
 public void testConstructorWithInvalidId() throws SQLException {
     new Verse(-1);
 }

 @Test
 public void testConstructorWithDatabaseResult() throws SQLException {
     DbResult dbResult = new DbResult();
     dbResult.next();
     Verse verse1 = new Verse(dbResult);
     assertEquals("Unexpected Verse object ID.", 1, verse1.getId());
 }

 @Test(expected = SQLException.class)
 public void testConstructorWithInvalidDatabaseResult() throws SQLException {
     DbResult dbResult = new DbResult();
     new Verse(dbResult);
 }

 @Test
 public void testToString() {
     Verse verse1 = new Verse(TEST_ID);
     String expected = "bible.obj.Verse:[1 bible.obj.TranslationReference:[1 bible.obj.Translation:1 bible.obj.Reference:[1 1 1]] bible.obj.String:test]";
     String actual = verse1.toString();
     assertEquals("Unexpected Verse object string representation.", expected, actual);
 }
}