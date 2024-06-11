import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import bible.obj.Verse;
import bible.obj.Translation;
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
 private static final int TEST_INVALID_TRANSLATION_ID = -1;
 private static final int TEST_INVALID_BOOK_ID = -1;
 private static final int TEST_INVALID_CHAPTER_ID = -1;
 private static final int TEST_INVALID_VERSE_ID = -1;

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_ID);
 Assertions.assertNotNull(verse);
 }

 @Test
 public void testGetId() {
 int expectedId = TEST_VERSE_ID;
 Translation translation = new Translation(TEST_TRANSLATION_ID);
 Reference reference = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Verse verse = new Verse(expectedId);
 int actualId = Verse.getId(translation, reference.getBook(), reference.getChapter(), reference.getVerse());
 Assertions.assertEquals(expectedId, actualId);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 ArrayList expectedVerses = new ArrayList();
 expectedVerses.add(new Verse(TEST_VERSE_ID));
 Assertions.assertEquals(expectedVerses, verses);
 }

 @Test
 public void testGetWithInvalidTranslationId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_INVALID_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assertions.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBookId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_INVALID_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assertions.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidChapterId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_INVALID_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assertions.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerseId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_INVALID_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assertions.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEndReferencesOutOfRange() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID - 1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 Reference end = new Reference(TEST_BOOK_ID + 1, TEST_CHAPTER_ID, TEST_VERSE_ID);
 ArrayList verses = Verse.Get(translations, begin, end);
 Assertions.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithValidInput() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 Reference begin = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID - 1);
 Reference end = new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID + 1);
 ArrayList verses = Verse.Get(translations, begin, end);
 ArrayList expectedVerses = new ArrayList();
 expectedVerses.add(new Verse(TEST_VERSE_ID - 1));
 expectedVerses.add(new Verse(TEST_VERSE_ID));
 expectedVerses.add(new Verse(TEST_VERSE_ID + 1));
 Assertions.assertEquals(expectedVerses, verses);
 }

 @Test
 public void testGetFromDatabase() {
 // Arrange
 Database.Initialize();
 int translationId = TEST_TRANSLATION_ID;
 int bookId = TEST_BOOK_ID;
 int chapterId = TEST_CHAPTER_ID;
 int verseId = TEST_VERSE_ID;

 // Act
 int id = Verse.GetId(Translation.New(translationId), new Book(bookId), chapterId, verseId);
 Verse verse = Verse.New(id);

 // Assert
 Assertions.assertEquals(TEST_BOOK_ID, verse.getReference().getBookId());
 Assertions.assertEquals(TEST_CHAPTER_ID, verse.getReference().getChapterNo());
 Assertions.assertEquals(TEST_VERSE_ID, verse.getReference().getVerseNo());
 Assertions.assertEquals(TEST_TRANSLATION_ID, verse.getTranslationReference().getTranslation().getId());
 Assertions.assertEquals(TEST_BOOK_ID, verse.getTranslationReference().getReference().getBookId());
 Assertions.assertEquals(TEST_CHAPTER_ID, verse.getTranslationReference().getReference().getChapterNo());
 Assertions.assertEquals(TEST_VERSE_ID, verse.getTranslationReference().getReference().getVerseNo());
 Assertions.assertEquals(TEST_TEXT, verse.getText());

 // Cleanup
 Database.Terminate();
 }

 @Test
 public void testGetFromDatabaseWithInvalidInputs() {
 // Arrange
 Database.Initialize();
 int invalidTranslationId = TEST_INVALID_TRANSLATION_ID;
 int bookId = TEST_BOOK_ID;
 int chapterId = TEST_CHAPTER_ID;
 int verseId = TEST_VERSE_ID;

 // Act & Assert
 AssertThrows(IllegalArgumentException.class, () -> Verse.GetId(Translation.New(invalidTranslationId), new Book(bookId), chapterId, verseId));
 Database.Terminate();
 }
}