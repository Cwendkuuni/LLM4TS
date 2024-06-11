package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

 private static final String TEST_TRANSLATION_ID = "1";
 private static final String TEST_BOOK_ID = "1";
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_OTHER_VERSE_ID = 2;
 private static final String TEST_OTHER_BOOK_ID = "2";
 private static final int TEST_OTHER_CHAPTER_ID = 2;
 private static final int TEST_OTHER_VERSE_NO = 2;
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final Translation TEST_TRANSLATION = new Translation("Test Translation", null);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(TEST_TRANSLATION, TEST_REFERENCE);
 private static final List<Translation> TEST_TRANSLATIONS = new ArrayList<>();

 @BeforeClass
 static public void setup() throws SQLException, ClassNotFoundException {
 TEST_TRANSLATIONS.add(TEST_TRANSLATION);
 Database.Setup();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertEquals("bible.obj.Verse:[1 TestTranslation Reference[1 1 1]]", verse.toString());
 }

 @Test
 public void testGetId() {
 int id = Verse.getId(TEST_TRANSLATION, new Book(TEST_BOOK_ID, null), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), new Book(TEST_BOOK_ID, null), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals("bible.obj.Verse:[1 TestTranslation Reference[1 1 1]]", verses.get(0).toString());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), null, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_NO), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_NO));
 assertEquals(2, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetWithInvalidQuery() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), null, null, null);
 fail("Should have thrown SQLException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullTranslations() {
 List<Verse> verses = Verse.Get(null, new Book(TEST_BOOK_ID, null), TEST_CHAPTER_ID, TEST_VERSE_ID);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullBook() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), null, TEST_REFERENCE, TEST_REFERENCE);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullReference() {
 List<Verse> verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), new Book(TEST_BOOK_ID, null), null, null);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testGetIdStatic() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGetIdWithNullTranslationId() {
 int id = Verse.GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId("-1", TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullBookId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, null, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, "test", TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullChapterIdAndVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, null, null);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullChapterId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, null, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithNullVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, null);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndBookId() {
 int id = Verse.GetId("-1", TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndChapterId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertNull(id);
 }

 @Test
 public void testGetIdWithInvalidTranslationIdAndVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertNull(id);
 }

 @Test
 public void testNewWithNullId() {
 try {
 Verse verse = new Verse(null);
 fail("Should have thrown NullPointerException");
 }
 catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 TestTranslation Reference[1 1 1]]", verse.toString());
 }

 @Test(expected = SQLException.class)
 public void testConstructorWithInvalidQuery() throws SQLException {
 Verse verse = new Verse(Database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + Integer.MAX_VALUE));
 fail("Should have thrown SQLException");
 }

 @Test(expected = SQLException.class)
 public void testConstructorWithNullResult() throws SQLException {
 try {
 new Verse(null);
 fail("Should have thrown SQLException");
 }
 catch (SQLException e) {
 // pass
 }
 }

 @Test
 public void testGetTranslationReference() {
 Verse verse = new Verse(TEST_VERSE_ID);
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testGetReference() {
 Verse verse = new Verse(TEST_VERSE_ID);
 assertEquals(TEST_REFERENCE, verse.getReference());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetReferenceWithNullId() {
 Verse verse = new Verse(0);
 try {
 verse.getReference();
 fail("Should have thrown IllegalStateException");
 }
 catch (IllegalStateException e) {
 // pass
 }
 }

 @Test
 public void testGetText() {
 Verse verse = new Verse(TEST_VERSE_ID);
 assertEquals("", verse.getText());
 }

 @Test
 public void testGetTextWithValidText() {
 Verse verse = new Verse(TEST_VERSE_ID + " Test Text");
 assertEquals("Test Text", verse.getText());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetTextWithNullId() {
 Verse verse = new Verse(0);
 try {
 verse.getText();
 fail("Should have thrown IllegalStateException");
 }
 catch (IllegalStateException e) {
 // pass
 }
 }

 @Test
 public void testGetTranslationReferenceWithNullId() {
 Verse verse = new Verse(0);
 assertNull(verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReferenceWithValidId() {
 Verse verse = new Verse(TEST_VERSE_ID);
 assertNotNull(verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReferenceWithNullTranslation() {
 Verse verse = new Verse(TEST_VERSE_ID);
 Translation translation = null;
 verse.setTranslationReference(new TranslationReference(translation, TEST_REFERENCE));
 assertNull(verse.getTranslationReference());
 }

 @Test
 public void testGetTranslationReferenceWithValidTranslation() {
 Verse verse = new Verse(TEST_VERSE_ID);
 verse.setTranslationReference(TEST_TRANSLATION_REFERENCE);
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testSetTranslationReference() {
 Verse verse = new Verse(TEST_VERSE_ID);
 TranslationReference translationReference = new TranslationReference(TEST_TRANSLATION, TEST_REFERENCE);
 verse.setTranslationReference(translationReference);
 assertEquals(translationReference, verse.getTranslationReference());
 }

 @Test
 public void testSetTranslationReferenceWithNullTranslationReference() {
 Verse verse = new Verse(TEST_VERSE_ID);
 verse.setTranslationReference(null);
 assertNull(verse.getTranslationReference());
 }
}