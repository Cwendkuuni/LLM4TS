import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static Translation translation1, translation2;
 private static Book book;

 @BeforeClass
 static public void setup() {
 // Set up static variables and objects used in the class
 Verse.translations = new HashMap<>();
 Verse.verseCache = new WeakHashMap<>();
 Database.connect();

 // Create test data
 translation1 = new Translation(1, "Test Translation 1");
 translation2 = new Translation(2, "Test Translation 2");
 book = new Book(1, "Test Book");
 }

 @Test
 public void testNew_ValidId() {
 Verse verse = Verse.New(1);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testNew_InvalidId() {
 Verse verse = Verse.New(-1);
 assertNull(verse);
 }

 @Test
 public void testGet_ValidParams() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] {translation1, translation2}, book, 1, 1);
 assertEquals(2, verses.size());
 for (Verse verse : verses) {
 assertEquals(1, verse.getId());
 }
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 ArrayList<Verse> verses = Verse.Get(new Translation[0], book, 1, 1);
 assertEmpty(verses);
 }

 @Test
 public void testGet_InvalidBookId() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] {translation1, translation2}, null, 1, 1);
 assertEmpty(verses);
 }

 @Test
 public void testGet_InvalidChapterOrVerse() {
 ArrayList<Verse> verses = Verse.Get(new Translation[] {translation1, translation2}, book, 0, 0);
 assertEmpty(verses);
 }

 @Test
 public void testGet_NullReferenceBegin() {
 ArrayList<Verse> verses = Verse.Get(new Translation[0], null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEmpty(verses);
 }

 @Test
 public void testGet_InvalidReferenceEnd() {
 ArrayList<Verse> verses = Verse.Get(new Translation[0], book, new Reference(1, 1, 1), null);
 assertEmpty(verses);
 }

 @Test
 public void testGetId_ValidParams() {
 int id = Verse.GetId(translation1, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetId_TranslationDoesNotExist() {
 int id = Verse.GetId(-1, book, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_BookDoesNotExist() {
 int id = Verse.GetId(translation1.getId(), null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_ChapterOrVerseOutOfBounds() {
 int id = Verse.GetId(translation1.getId(), book.getId(), 0, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationAndBookBothNull() {
 int id = Verse.GetId(-1, null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_TranslationIdOutOfBounds() {
 int id = Verse.GetId(-1, book.getId(), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_ChapterOrVerseOutOfBoundsInTranslation() {
 int id = Verse.GetId(translation1.getId(), book.getId(), 0, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGet_NullTranslations() {
 ArrayList<Verse> verses = Verse.Get(null, book, 1, 1);
 assertEmpty(verses);
 }

 @Test
 public void testGet_EmptyDatabase() {
 ArrayList<Verse> verses = Verse.Get(new Translation[0], book, 1, 1);
 assertEmpty(verses);
 }

 @Test
 public void testGet_DatabaseError() {
 Database.setThrowExceptionOnQuery(true);
 ArrayList<Verse> verses = Verse.Get(new Translation[0], book, 1, 1);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testPrivateConstructor() {
 Verse verse = new Verse(-1);
 assertNull(verse);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 Test Translation 1 Test Book 1 1]", verse.toString());
 }
}