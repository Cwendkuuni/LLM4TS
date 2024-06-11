import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.DbResult;
import bible.util.Database;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;

 @BeforeClass
 public static void setup() {
 // Assuming Database class is set up with proper data
 translation = new Translation(1);
 book = new Book(1, "Genesis");
 reference = new Reference(1, 1, 1);
 }

 @Test
 public void testNewWithExistingId() {
 Verse verse1 = Verse.New(1);
 Verse verse2 = Verse.New(1);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testNewWithNonExistingId() {
 Verse verse = Verse.New(100);
 assertNull(verse);
 }

 @Test
 public void testGetIdWithValidData() {
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetIdWithInvalidTranslation() {
 int id = Verse.GetId(null, book, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBook() {
 int id = Verse.GetId(translation, null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterOrVerse() {
 int id = Verse.GetId(translation, book, 0, 0);
 assertEquals(-1, id);
 }

 @Test
 public void testGetWithValidData() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(verse1, verses.get(0));
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 ArrayList verses = Verse.Get(new Translation[0], book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginOrEndReference() {
 ArrayList verses = Verse.Get(translations, null, new Reference(0, 0, 0), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginReferenceBeforeTranslation() {
 ArrayList verses = Verse.Get(translations, null, new Reference(-1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEndReferenceBeforeBeginReference() {
 ArrayList verses = Verse.Get(translations, null, new Reference(1, 1, 1), new Reference(-1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithValidDataFromDatabase() throws SQLException {
 DbResult result = Database.Query("select * from Verse where Verse_ID = 1");
 ArrayList verses = Verse.Get(translations, book, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(new Verse(result.getInt(1)), verses.get(0));
 }

 @Test
 public void testGetIdFromDatabaseWithNonExistingData() throws SQLException {
 int id = Verse.GetId(translation.getId(), book.getId(), 1, 1);
 assertEquals(-1, id);
 }
}