package bible.obj.test;

import static org.junit.Assert.*;
import static bible.obj.Verse.*;
import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSum;
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
 private static final int TEST_ANOTHER_VERSE_ID = 2;

 @Before
 public void setUp() {
 Database.Initialize();
 }

 @After
 public void tearDown() {
 Database.Shutdown();
 }

 @Test
 public void testNew() {
 Verse verse1 = New(TEST_VERSE_ID);
 assertNotNull(verse1);
 assertEquals(TEST_VERSE_ID, verse1.getId());
 }

 @Test
 public void testGetId() {
 int id = GetId(new Translation(TEST_TRANSLATION_ID), new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 List<Identifible> verses = Get(translations, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_VERSE_ID, ((Verse) verses.get(0)).getId());
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 List<Identifible> verses = Get(new Translation[0], new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 List<Identifible> verses = Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetIdNullTranslation() {
 int id = GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdNegativeTranslationId() {
 int id = GetId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdInvalidBookId() {
 int id = GetId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdInvalidChapterId() {
 int id = GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdInvalidVerseId() {
 int id = GetId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testNewWithNullId() {
 Verse verse1 = Verse.New(null);
 assertNull(verse1);
 }

 @Test
 public void testNewWithNegativeId() {
 Verse verse1 = Verse.New(-1);
 assertNull(verse1);
 }

 @Test
 public void testToString() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 assertEquals("bible.obj.Verse:[" + TEST_VERSE_ID + " " + new TranslationReference(new Translation(TEST_TRANSLATION_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)) + " " + "]", verse1.toString());
 }

 @Test
 public void testGetText() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 assertEquals("", verse1.getText());
 // Assuming that a real Verse object has a Text property set in its constructor
 // Add test cases to cover different Text values
 }

 @Test
 public void testGetTranslationReference() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 TranslationReference reference = verse1.getTranslationReference();
 assertEquals(new Translation(TEST_TRANSLATION_ID), reference.getTranslation());
 assertEquals(new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), reference.getReference());
 }

 @Test
 public void testGetReference() {
 Verse verse1 = new Verse(TEST_VERSE_ID);
 Reference reference = verse1.getReference();
 assertEquals(TEST_BOOK_ID, reference.getBookId());
 assertEquals(TEST_CHAPTER_ID, reference.getChapterNo());
 assertEquals(TEST_VERSE_ID, reference.getVerseNo());
 }

 @Test
 public void testReadIdWithNullTranslationId() {
 int id = ReadId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithNegativeTranslationId() {
 int id = ReadId(-1, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidBookId() {
 int id = ReadId(TEST_TRANSLATION_ID, -1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidChapterId() {
 int id = ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, -1, TEST_VERSE_ID);
 assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidVerseId() {
 int id = ReadId(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetWithEmptyTranslationArrayAndEmptyResultSet() {
 Translation[] translations = new Translation[0];
 List<Identifible> verses = Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslationArrayAndNonEmptyResultSet() {
 Translation[] translations = new Translation[0];
 DbResult result = Database.Query("SELECT Verse_ID FROM Verse WHERE Verse_ID = " + TEST_VERSE_ID + " OR Verse_ID = " + TEST_ANOTHER_VERSE_ID);
 List<Identifible> verses = Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1));
 assertEquals(2, verses.size());
 }
}