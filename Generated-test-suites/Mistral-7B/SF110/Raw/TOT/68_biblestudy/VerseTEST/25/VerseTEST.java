import org.junit.After; import org.junit.Before; import org.junit.Test; import static org.junit.Assert.*; import bible.obj.Verse; import bible.obj.TranslationReference; import bible.obj.Reference; import bible.util.DbResult; import bible.util.Database; import java.sql.SQLException; import java.util.ArrayList; import java.util.HashMap; import java.util.IntArray;

public class VerseTest {

 private static final String TEST_TRANSLATION_ID = "1";
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_INVALID_VERSE_ID = -1;
 private static final int TEST_NON_EXISTENT_BOOK_ID = 200;
 private static final int TEST_NON_EXISTENT_CHAPTER_ID = 101;
 private static final Translation TEST_TRANSLATION = new Translation(1, "English");
 private static final Reference TEST_REFERENCE = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final Verse TEST_VERSE = new Verse(TEST_TRANSLATION_ID, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 private static final Verse TEST_NULL_VERSE = null;
 private static final ArrayList TEST_TRANSLATIONS = new ArrayList();
 private static final ArrayList TEST_VERSES_VALID = new ArrayList();
 private static final ArrayList TEST_VERSES_INVALID = new ArrayList();
 private static HashMap<Integer, HashMap<Integer, Integer>> TEST_TRANSLATION_BOOKS;
 private static DbResult TEST_DB_RESULT;

 @Before
 public void setUp() {
 TEST_TRANSLATION_BOOKS = new HashMap<Integer, HashMap<Integer, Integer>>();
 TEST_TRANSLATION_BOOKS.put(TEST_TRANSLATION_ID, new HashMap<Integer, Integer>());
 TEST_TRANSLATIONS.add(TEST_TRANSLATION);
 TEST_VERSES_VALID.add(TEST_VERSE);
 TEST_DB_RESULT = null;
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
 public void testNew_Valid() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 assertEquals(TEST_TRANSLATION_ID, verse.getTranslationReference().getTranslation().getId());
 assertEquals(TEST_BOOK_ID, verse.getReference().getBookId());
 assertEquals(TEST_CHAPTER_ID, verse.getReference().getChapterNo());
 assertEquals(TEST_VERSE_ID, verse.getId());
 assertEquals(TEST_VERSE.getText(), verse.getText());
 }

 @Test(expected = NullPointerException.class)
 public void testNew_NullId() {
 Verse.New(null);
 }

 @Test
 public void testGetId_Valid() {
 int id = Verse.GetId(TEST_TRANSLATION, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_VERSE_ID, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetId_InvalidTranslationId() {
 Verse.GetId(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 }

 @Test
 public void testGetId_InvalidBookId() {
 TEST_TRANSLATION_BOOKS.put(TEST_TRANSLATION_ID, new HashMap<Integer, Integer>());
 int result = Verse.GetId(TEST_TRANSLATION, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, result);
 }

 @Test
 public void testGetId_InvalidChapterId() {
 TEST_TRANSLATION_BOOKS.put(TEST_TRANSLATION_ID, new HashMap<Integer, Integer>());
 TEST_TRANSLATION_BOOKS.get(TEST_TRANSLATION_ID).put(TEST_BOOK_ID, 1);
 int result = Verse.GetId(TEST_TRANSLATION, TEST_BOOK_ID, TEST_NON_EXISTENT_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(-1, result);
 }

 @Test
 public void testGetId_InvalidVerseId() {
 TEST_TRANSLATION_BOOKS.put(TEST_TRANSLATION_ID, new HashMap<Integer, Integer>());
 TEST_TRANSLATION_BOOKS.get(TEST_TRANSLATION_ID).put(TEST_BOOK_ID, new HashMap<Integer, Integer>());
 TEST_TRANSLATION_BOOKS.get(TEST_TRANSLATION_ID).get(TEST_BOOK_ID).put(TEST_CHAPTER_ID, TEST_VERSE_ID);
 int result = Verse.GetId(TEST_TRANSLATION, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_INVALID_VERSE_ID);
 assertEquals(-1, result);
 }

 @Test
 public void testGet_Valid() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(1, verses.size());
 assertEquals(TEST_VERSE, verses.get(0));
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 ArrayList verses = Verse.Get(new Translation[0], TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_NullTranslationArray() {
 ArrayList verses = Verse.Get(null, TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBookId() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_NON_EXISTENT_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidChapterId() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_BOOK_ID, TEST_NON_EXISTENT_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidVerseId() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_INVALID_VERSE_ID);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_MultipleTranslations() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS.toArray(new Translation[0]), TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 assertEquals(TEST_TRANSLATIONS.size(), verses.size());
 for (int i = 0; i < TEST_TRANSLATIONS.size(); i++) {
 assertEquals(TEST_TRANSLATIONS.get(i), verses.get(i));
 }
 }

 @Test
 public void testToString() {
 assertEquals("bible.obj.Verse:[1 English:[1 1 1]]Text=Test Text]", TEST_VERSE.toString());
 }

 @Test
 public void testGetReference() {
 assertEquals(TEST_REFERENCE, TEST_VERSE.getReference());
 }

 @Test
 public void testGetTranslationReference() {
 assertEquals(TEST_TRANSLATION, TEST_VERSE.getTranslationReference().getTranslation());
 }

 @Test
 public void testGet_FromDatabase_Valid() throws SQLException {
 TEST_DB_RESULT = Database.Query("SELECT Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No FROM Verse WHERE Verse_ID = " + TEST_VERSE_ID);
 TEST_DB_RESULT.next();
 Verse verse = new Verse(TEST_DB_RESULT);
 assertEquals(TEST_VERSE_ID, verse.getId());
 assertEquals(TEST_VERSE.getText(), verse.getText());
 assertEquals(TEST_TRANSLATION, verse.getTranslationReference().getTranslation());
 }

 @Test(expected = SQLException.class)
 public void testGet_FromDatabase_InvalidId() throws SQLException {
 TEST_DB_RESULT = Database.Query("SELECT Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No FROM Verse WHERE Verse_ID = " + TEST_INVALID_VERSE_ID);
 TEST_DB_RESULT.next();
 fail();
 }

}