import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_INVALID_TRANSLATION_ID = -1;
 private static final int TEST_INVALID_BOOK_ID = -1;
 private static final int TEST_INVALID_CHAPTER_ID = -1;
 private static final int TEST_INVALID_VERSE_ID = -1;
 private static final int TEST_MIN_BOOK_ID = 1;
 private static final int TEST_MAX_BOOK_ID = 100;
 private static final int TEST_MIN_CHAPTER_ID = 1;
 private static final int TEST_MAX_CHAPTER_ID = 100;
 private static final int TEST_MIN_VERSE_ID = 1;
 private static final int TEST_MAX_VERSE_ID = 10000;

 private DbResult result;
 private static Verse verse;
 private static ArrayList<Verse> verses;
 private static Translation translation;
 private static Reference reference;

 @Before
 public void setUp() {
 translation = new Translation(TEST_TRANSLATION_ID);
 reference = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @After
 public void tearDown() {
 try {
 Database.Close();
 }
 catch (SQLException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNew_ValidId() {
 verse = Verse.New(TEST_VERSE_ID);
 assertNotNull(verse);
 assertEquals(TEST_VERSE_ID, verse.getId());
 }

 @Test(expected = NullPointerException.class)
 public void testNew_NullId() {
 verse = Verse.New(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGet_ValidId() {
 verse = Verse.New(TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, Verse.GetId(translation, new Book(TEST_BOOK_ID), TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGet_TranslationNull() {
 verses = Verse.Get(null, new Book(TEST_BOOK_ID, TEST_BOOK_ID, "Test Book"), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_InvalidId() {
 verse = Verse.New(TEST_INVALID_VERSE_ID);
 assertNull(verse);
 }

 @Test
 public void testGet_MinId() {
 verse = Verse.New(TEST_MIN_VERSE_ID);
 assertNotNull(verse);
 }

 @Test
 public void testGet_MaxId() {
 verse = Verse.New(TEST_MAX_VERSE_ID);
 assertNotNull(verse);
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 verses = Verse.Get(new Translation[0], new Book(TEST_BOOK_ID, TEST_BOOK_ID, "Test Book"), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() == 0);
 }

 @Test
 public void testGet_MinBookId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_MIN_BOOK_ID, TEST_CHAPTER_ID, "Test Chapter"), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_MaxBookId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_MAX_BOOK_ID, TEST_CHAPTER_ID, "Test Chapter"), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_MinChapterId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_MIN_CHAPTER_ID, "Test Chapter"), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_MaxChapterId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_MAX_CHAPTER_ID, "Test Chapter"), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_MinVerseId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_MIN_VERSE_ID), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_MaxVerseId() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_MAX_VERSE_ID), reference, reference);
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_BeginEndInvalid() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_BOOK_ID, "Test Book"), new Reference(TEST_BOOK_ID, TEST_MIN_CHAPTER_ID, TEST_MIN_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_MAX_CHAPTER_ID, TEST_MAX_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_BeginEndEqual() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_BeginEndOverlap() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_MIN_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_MAX_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGet_BeginEndNoOverlap() {
 verses = Verse.Get(translation.getTranslations(), new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_CHAPTER_ID), new Reference(TEST_BOOK_ID, TEST_MIN_CHAPTER_ID, TEST_MIN_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_MAX_CHAPTER_ID, TEST_MAX_VERSE_ID));
 assertNotNull(verses);
 assertTrue(verses.size() > 0);
 }

 @Test
 public void testGetId_ValidId() {
 assertEquals(TEST_VERSE_ID, Verse.GetId(translation, new Book(TEST_BOOK_ID, TEST_BOOK_ID, "Test Book"), TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetId_TranslationNull() {
 assertEquals(-1, Verse.GetId(null, new Book(TEST_BOOK_ID, TEST_BOOK_ID, "Test Book"), TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetId_InvalidBookId() {
 assertEquals(-1, Verse.GetId(translation, new Book(TEST_INVALID_BOOK_ID, TEST_CHAPTER_ID, "Test Chapter"), TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetId_InvalidChapterId() {
 assertEquals(-1, Verse.GetId(translation, new Book(TEST_BOOK_ID, TEST_INVALID_CHAPTER_ID, "Test Chapter"), TEST_CHAPTER_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGetId_InvalidVerseId() {
 assertEquals(-1, Verse.GetId(translation, new Book(TEST_BOOK_ID, TEST_CHAPTER_ID, "Test Chapter"), TEST_INVALID_VERSE_ID, TEST_VERSE_ID));
 }

 @Test
 public void testGet_Query() {
 result = Database.Query("select Verse_ID from Verse where Translation_ID = " + TEST_TRANSLATION_ID + " and Book_ID = " + TEST_BOOK_ID + " and Chapter = " + TEST_CHAPTER_ID + " and Verse_No = " + TEST_VERSE_ID);
 verses = new ArrayList<>();
 while (result.next()) {
 verses.add(Verse.New(result.getInt("Verse_ID")));
 }
 assertEquals(1, verses.size());
 assertEquals(TEST_VERSE_ID, verses.get(0).getId());
 }

 @Test
 public void testGet_EmptyResult() {
 result = Database.Query("select Verse_ID from Verse where Translation_ID = " + TEST_TRANSLATION_ID + " and Book_ID = " + TEST_BOOK_ID + " and Chapter = " + TEST_CHAPTER_ID + " and Verse_No = " + TEST_MIN_VERSE_ID);
 verses = new ArrayList<>();
 assertTrue(result.next());
 assertFalse(result.next());
 assertEquals(0, verses.size());
 }
}