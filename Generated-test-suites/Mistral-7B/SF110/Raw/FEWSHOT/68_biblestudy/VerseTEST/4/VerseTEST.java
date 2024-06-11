package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.util.*;
import java.sql.*;
import bible.obj.Verse;
import bible.util.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class VerseTest {

 private static final String TEST_TRANSLATION_ID = "1";
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ID = 1;
 private static final String TEST_TEXT = "This is a test verse.";
 private static final String TEST_BOOK_NAME = "Test Book";
 private static final String TEST_CHAPTER_NAME = "Test Chapter";
 private static final String TEST_REFERENCE_STRING = TEST_BOOK_NAME + " " + TEST_CHAPTER_ID + ":" + TEST_VERSE_ID;
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(mock(Translation.class), TEST_REFERENCE);

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 Database.init();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 assertNotNull(verse.getText());
 assertNull(verse.getTranslationReference());
 }

 @Test
 public void testGet() {
 List<Translation> translations = Arrays.asList(mock(Translation.class));
 ArrayList verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, verses.get(0).getId());
 }

 @Test
 public void testGetId() {
 assertEquals(TEST_ID, Verse.getId(mock(Translation.class), new BibleReference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), TEST_VERSE_ID));
 }

 @Test
 public void testGet() throws SQLException {
 DbResult dbResult = mock(DbResult.class);
 when(dbResult.next()).thenReturn(true);
 when(dbResult.getInt("Verse_ID")).thenReturn(TEST_ID);
 when(dbResult.getString("Text")).thenReturn(TEST_TEXT);
 when(dbResult.getInt("Translation_ID")).thenReturn(Integer.parseInt(TEST_TRANSLATION_ID));
 when(dbResult.getInt("Book_ID")).thenReturn(TEST_BOOK_ID);
 when(dbResult.getInt("Chapter")).thenReturn(TEST_CHAPTER_ID);
 when(dbResult.getInt("Verse_No")).thenReturn(TEST_VERSE_ID);

 Verse verse = Verse.New(TEST_ID);
 assertNull(verse);

 verse = new Verse(dbResult);
 assertEquals(TEST_ID, verse.getId());
 assertEquals(TEST_TEXT, verse.getText());
 assertEquals(TEST_TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test(expected = SQLException.class)
 public void testGet_DatabaseError() throws SQLException {
 DbResult dbResult = mock(DbResult.class);
 when(dbResult.next()).thenThrow(new SQLException());

 Verse verse = new Verse(dbResult);
 fail("Should have thrown SQLException");
 }
}