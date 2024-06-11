import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {

 private static final int TEST_TRANSLATION_ID = 1;
 private static final int TEST_BOOK_ID = 1;
 private static final int TEST_CHAPTER_ID = 1;
 private static final int TEST_VERSE_ID = 1;
 private static final int TEST_ID = 1;
 private static final int TEST_ID_NOT_FOUND = -1;

 @BeforeClass
 static public void setup() {
 // Initialize database with test data
 Database.Initialize();

 // Add test translation to translations map
 Verse.translations.put(new Integer(TEST_TRANSLATION_ID), new HashMap<>());

 // Add test book to translation map
 Verse.translations.get(new Integer(TEST_TRANSLATION_ID)).put(new Integer(TEST_BOOK_ID), new HashMap<>());

 // Add test chapter to book map
 Verse.translations.get(new Integer(TEST_TRANSLATION_ID)).get(new Integer(TEST_BOOK_ID)).put(new Integer(TEST_CHAPTER_ID), new HashMap<>());

 // Add test verse to chapter map
 Verse.translations.get(new Integer(TEST_TRANSLATION_ID)).get(new Integer(TEST_BOOK_ID)).get(new Integer(TEST_CHAPTER_ID)).put(new Integer(TEST_VERSE_ID), new Integer(TEST_ID));
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(new TranslationReference(TEST_TRANSLATION_ID, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(TEST_ID, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(1, verses.size());
 assertEquals(TEST_ID, ((Verse) verses.get(0)).getId());
 }

 @Test
 public void testGetFromDb() {
 Verse verse = Verse.New(TEST_ID);
 assertEquals(TEST_ID, verse.getId());
 assertNotNull(verse.getText());
 assertNotNull(verse.getTranslationReference());
 assertNotNull(verse.getReference());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(new TranslationReference(0, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID)), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Should throw IllegalArgumentException with invalid translation id");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(new TranslationReference(TEST_TRANSLATION_ID, new Reference(0, TEST_CHAPTER_ID, TEST_VERSE_ID)), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Should throw IllegalArgumentException with invalid book id");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(new TranslationReference(TEST_TRANSLATION_ID, new Reference(TEST_BOOK_ID, 0, TEST_VERSE_ID)), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Should throw IllegalArgumentException with invalid chapter id");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(new TranslationReference(TEST_TRANSLATION_ID, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, 0)), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 fail("Should throw IllegalArgumentException with invalid verse id");
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 Translation[] translations = new Translation[0];
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID - 1, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID - 1, TEST_VERSE_ID));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginReferenceGreaterThanEndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(TEST_TRANSLATION_ID);
 ArrayList verses = Verse.Get(translations, new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID, TEST_VERSE_ID), new Reference(TEST_BOOK_ID, TEST_CHAPTER_ID - 1, TEST_VERSE_ID));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDbWithInvalidId() {
 try {
 Verse verse = Verse.New(TEST_ID_NOT_FOUND);
 fail("Should throw IllegalArgumentException with invalid id");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}