package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
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

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ANOTHER_VERSE_ID = 2;
 private static final int TEST_BOOK_ID_2 = 2;
 private static final int TEST_CHAPTER_ID_2 = 1;
 private static final int TEST_VERSE_ID_2 = 1;

 private List<Translation> testTranslations;
 private Translation testTranslation;
 private Verse testVerse;
 private Verse testAnotherVerse;

 @Before
 public void setUp() throws Exception {
 testTranslations = new ArrayList<>();
 testTranslation = new Translation(TEST_TRANSLATION_ID);
 testTranslations.add(testTranslation);
 }

 @After
 public void tearDown() throws SQLException, ClassNotFoundException {
 Database.Close();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertEquals("Verse:[1 TranslationReference[Translation[1] Reference[1 1 1]] Text=null]", verse.toString());
 }

 @Test
 public void testGetId() throws SQLException {
 int id = Verse.GetId(testTranslation, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals("Expected: " + TEST_VERSE_ID + ", Actual: " + id, TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() throws SQLException {
 List<Verse> verses = Verse.Get(testTranslations.toArray(new Translation[0]), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals("Size: Expected: " + verses.size() + ", Actual: " + verses.size(), verses.size(), 2);
 testVerse = verses.get(0);
 testAnotherVerse = verses.get(1);
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetWithNullBeginReference() {
 List<Verse> verses = Verse.Get(testTranslations.toArray(new Translation[0]), null, null, null);
 assertEquals("Size: Expected: " + verses.size() + ", Actual: " + verses.size(), verses.size(), 0);
 }

 @Test
 public void testGetWithNullEndReference() {
 List<Verse> verses = Verse.Get(testTranslations.toArray(new Translation[0]), new Book(TEST_BOOK_ID), null, null);
 assertEquals("Size: Expected: " + verses.size() + ", Actual: " + verses.size(), verses.size(), 0);
 }

 @Test
 public void testGetIdWithNullBook() {
 int id = Verse.GetId(testTranslation, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(testTranslation, new Book(-1, TEST_CHAPTER_ID, TEST_CHAPTER_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(testTranslation, new Book(TEST_BOOK_ID, -1, TEST_CHAPTER_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(testTranslation, new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, -1), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetIdWithInvalidVerseIdAndTranslationIdNotFound() {
 int id = Verse.GetId(new Translation(-1), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull("Expected null", id);
 }

 @Test
 public void testGetWithBeginAfterEnd() {
 List<Verse> verses = Verse.Get(testTranslations.toArray(new Translation[0]), new Book(TEST_BOOK_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID + 1, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals("Size: Expected: " + verses.size() + ", Actual: " + verses.size(), verses.size(), 0);
 }

 @Test
 public void testGetWithBeginEqualsEnd() {
 List<Verse> verses = Verse.Get(testTranslations.toArray(new Translation[0]), new Book(TEST_BOOK_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals("Size: Expected: " + verses.size() + ", Actual: " + verses.size(), verses.size(), 1);
 }

 @Test
 public void testComplete() throws SQLException {
 DbResult result = Database.Query("select * from Verse where Verse_ID = " + TEST_VERSE_ID);
 assertTrue("Query should have returned a result", result.next());
 assertFalse("Expected new instance, got cached one", Verse.verseCache.containsKey(new Integer(TEST_VERSE_ID)));
 testVerse = new Verse(TEST_VERSE_ID);
 testVerse.complete(result);
 assertTrue("Expected cached instance, got new one", Verse.verseCache.containsKey(new Integer(TEST_VERSE_ID)));
 assertEquals("Verse:[1 TranslationReference[Translation[1] Reference[1 1 1]] Text=null]", testVerse.toString());
 }

 @Test
 public void testCompleteWithDbResultContainingAnotherVerse() throws SQLException {
 DbResult result = Database.Query("select * from Verse where Verse_ID in (" + TEST_VERSE_ID + ", " + TEST_ANOTHER_VERSE_ID + ")");
 assertTrue("Query should have returned a result", result.next());
 result.next();
 assertFalse("Expected new instance for testVerse, got cached one", Verse.verseCache.containsKey(new Integer(TEST_VERSE_ID)));
 testVerse = new Verse(TEST_VERSE_ID);
 testVerse.complete(result);
 assertTrue("Expected cached instance for testVerse, got new one", Verse.verseCache.containsKey(new Integer(TEST_VERSE_ID)));
 assertEquals("Verse:[1 TranslationReference[Translation[1] Reference[1 1 1]] Text=null]", testVerse.toString());
 assertFalse("Expected new instance for testAnotherVerse, got cached one", Verse.verseCache.containsKey(new Integer(TEST_ANOTHER_VERSE_ID)));
 }

 @Test
 public void testToString() {
 assertEquals("Verse:[1 TranslationReference[Translation[1] Reference[1 1 1]] Text=null]", testVerse.toString());
 }

 @Test
 public void testGetReference() {
 Reference reference = testVerse.getReference();
 assertEquals("Expected: Reference[1 1 1], Actual: " + reference, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), reference);
 }

 @Test
 public void testGetTranslationReference() {
 TranslationReference translationReference = testVerse.getTranslationReference();
 TranslationReference expectedTranslationReference = new TranslationReference(testTranslation, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals("Expected: " + expectedTranslationReference + ", Actual: " + translationReference, expectedTranslationReference, translationReference);
 }

 @Test
 public void testGetText() {
 assertEquals("Expected: null, Actual: " + testVerse.getText(), null, testVerse.getText());
 }

 @Test
 public void testGetValue() {
 assertEquals("Expected: Text=null, Actual: " + testVerse.getValue(), "Text=null", testVerse.getValue());
 }

 @Test
 public void testEquals() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 Verse verse2 = new Verse(TEST_VERSE_ID);
 assertTrue("Expected verse1 == verse2", verse1.equals(verse2));
 verse2.text = "Test Text";
 assertFalse("Expected verse1 != verse2", verse1.equals(verse2));
 }

 @Test
 public void testHashCode() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 Verse verse2 = new Verse(TEST_VERSE_ID);
 int hashCode1 = verse1.hashCode();
 int hashCode2 = verse2.hashCode();
 assertEquals("Expected hashCode1 == hashCode2", hashCode1, hashCode2);
 verse2.text = "Test Text";
 assertNotEquals("Expected hashCode1 != hashCode2", hashCode1, hashCode2);
 }
}