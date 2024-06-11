import org.junit.Test;
import org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import java.util.*;
import static java.util.Arrays.asList;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER = 1;
 private static final int TEST_VERSE = 1;
 private static final int TEST_ID = 1;
 private static final int TEST_OTHER_ID = 2;
 private static final Translation TEST_TRANSLATION = new Translation(TEST_TRANSLATION_ID, "Test Translation");
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER, TEST_VERSE);
 private static final Verse TEST_VERSE = new Verse(TEST_ID, "Test Verse", TEST_TRANSLATION, TEST_REFERENCE);

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 assertEquals(TEST_VERSE, verse);
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
 assertEquals(TEST_VERSE, verses.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullTranslations() {
 Verse.Get(null, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNegativeId() {
 Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, -1);
 }

 @Test
 public void testGetWithInvalidId() {
 int id = Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, TEST_OTHER_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetWithMultipleTranslations() {
 Translation[] translations = { TEST_TRANSLATION, new Translation(2, "Another Translation") };
 List<Verse> verses = Verse.Get(translations, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(2, verses.size());
 assertTrue(asList(verses).contains(TEST_VERSE));
 assertTrue(asList(verses).contains(new Verse(TEST_OTHER_ID, "Another Test Verse", new Translation(2), TEST_REFERENCE)));
 }

 @Test
 public void testGetWithInvalidReference() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, null, TEST_CHAPTER, TEST_VERSE);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, TEST_END);
 // Assuming TEST_BEGIN and TEST_END are valid references with some verses
 // Replace the assert statement below with appropriate checks
 assertEquals(2, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidBeginReference() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, new Reference(-1, 1, 1), TEST_END);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidEndReference() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, new Reference(-1, 1, 1));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetWithEmptyTranslationMap() {
 List<Verse> verses = Verse.Get(new Translation[0], TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullTranslationMap() {
 List<Verse> verses = Verse.Get(null, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 }

 @Test
 public void testGetIdWithMissingCache() {
 int id = Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGetIdWithCachedVerse() {
 Verse verse1 = new Verse(TEST_ID + 1, "Another Test Verse", TEST_TRANSLATION, TEST_REFERENCE);
 Verse verse2 = new Verse(TEST_ID, "Test Verse", TEST_TRANSLATION, TEST_REFERENCE);
 Verse.New(TEST_ID);
 int id = Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(TEST_ID, id);
 assertEquals(verse2, Verse.New(TEST_ID));
 }

 @Test
 public void testGetIdWithCachedVerseFromDatabase() throws SQLException {
 DbResult result = Database.Query("select Verse_ID from Verse where Verse_ID = " + TEST_ID);
 int id = 0;
 if (result.next()) {
 id = result.getInt("Verse_ID");
 }
 DbResult.Close(result);
 int cacheId = Verse.GetId(TEST_TRANSLATION, TEST_BOOK, TEST_CHAPTER, TEST_VERSE);
 assertEquals(TEST_ID, id);
 assertEquals(TEST_ID, cacheId);
 }

 @Test
 public void testGetWithDatabase() throws SQLException {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, TEST_END);
 // Assuming TEST_BEGIN and TEST_END are valid references with some verses
 // Replace the assert statement below with appropriate checks
 assertEquals(2, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetWithDatabaseError() throws SQLException {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, TEST_END);
 }

 private Translation[] TEST_TRANSLATIONS = { TEST_TRANSLATION };
 private Reference TEST_BEGIN = new Reference(TEST_BOOK_ID, TEST_CHAPTER, TEST_VERSE - 1);
 private Reference TEST_END = new Reference(TEST_BOOK_ID, TEST_CHAPTER, TEST_VERSE + 1);

}