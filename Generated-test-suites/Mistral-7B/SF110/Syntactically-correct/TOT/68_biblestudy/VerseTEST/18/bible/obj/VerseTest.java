import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static DbResult result;
 static {
 Verse.translations = new HashMap<>();
 Verse.verseCache = new WeakHashMap<>();
 }

 @BeforeClass
 public static void initializeDatabase() throws SQLException {
 // Initialize the database with some data for testing
 // Replace this with your actual database initialization code
 Database.Initialize();
 }

 @AfterClass
 public static void closeDatabase() throws SQLException {
 // Close the database connection
 Database.Close();
 }

 @Test
 public void testNewVerse() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetVerseWithExistingId() {
 Verse verse = Verse.New(1);
 TranslationReference transRef = verse.getTranslationReference();
 Reference ref = verse.getReference();
 String text = verse.getText();

 assertEquals(Translate.ENGLISH.getId(), transRef.getTranslation().getId());
 assertEquals(new Reference(1, 1, 1), ref);
 assertEquals("The Lord is my shepherd; I shall not want.", text);
 }

 @Test(expected = NullPointerException.class)
 public void testNewVerseWithNullId() {
 Verse verse = Verse.New(null);
 fail("Expected NullPointerException but got: " + verse);
 }

 @Test
 public void testGetVerseWithNonExistentId() {
 Verse verse = Verse.New(1000);
 assertNull(verse);
 }

 @Test
 public void testGetVersesWithValidTranslationAndBook() {
 Translation[] translations = { new Translation(1, "English") };
 Verse[] verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.length);
 Verse verse = verses[0];
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetVersesWithEmptyTranslationArray() {
 Verse[] verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetVersesWithInvalidBookReference() {
 Translation[] translations = { new Translation(1, "English") };
 Reference invalidRef = new Reference(-1, -1, -1);
 Verse[] verses = Verse.Get(translations, invalidRef, new Reference(1, 1, 1));
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetVersesWithInvalidBeginAndEndReferences() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(0, 0, 0);
 Reference end = new Reference(1, 1, 2);
 Verse[] verses = Verse.Get(translations, begin, end);
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetVersesWithValidBeginAndEndReferencesAndTranslation() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 Verse[] verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.length);
 }

 @Test
 public void testGetIdWithValidInputs() {
 int id = Verse.GetId(new Translation(1, "English"), new Book(1, "Genesis"), 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetIdWithTranslationThatDoesNotExist() {
 int id = Verse.GetId(new Translation(-1, "English"), new Book(1, "Genesis"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithBookThatDoesNotExist() {
 int id = Verse.GetId(new Translation(1, "English"), new Book(-1, "Genesis"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithVerseThatDoesNotExist() {
 int id = Verse.GetId(new Translation(1, "English"), new Book(1, "Genesis"), -1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithValidInputsFromDatabase() throws SQLException {
 int id = Verse.GetId(1, 1, 1, 1);
 Verse verse = new Verse(id);
 assertEquals(id, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidTranslationId() throws SQLException {
 int id = Verse.GetId(-1, 1, 1, 1);
 fail("Expected SQLException but got: " + id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidBookId() throws SQLException {
 int id = Verse.GetId(1, -1, 1, 1);
 fail("Expected SQLException but got: " + id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidChapterId() throws SQLException {
 int id = Verse.GetId(1, 1, -1, 1);
 fail("Expected SQLException but got: " + id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidVerseId() throws SQLException {
 int id = Verse.GetId(1, 1, 1, -1);
 fail("Expected SQLException but got: " + id);
 }

 @Test
 public void testGet() throws SQLException {
 Translation[] translations = { new Translation(1, "English"), new Translation(2, "French") };
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 2);
 Verse[] verses = Verse.Get(translations, begin, end);
 assertEquals(2, verses.length);
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 Verse[] verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetWithInvalidBeginReference() {
 Translation[] translations = { new Translation(1, "English") };
 Reference invalidRef = new Reference(-1, -1, -1);
 Verse[] verses = Verse.Get(translations, invalidRef, new Reference(1, 1, 1));
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetWithInvalidEndReference() {
 Translation[] translations = { new Translation(1, "English") };
 Reference begin = new Reference(1, 1, 1);
 Reference invalidRef = new Reference(-1, -1, -1);
 Verse[] verses = Verse.Get(translations, begin, invalidRef);
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetWithEmptyResultSet() throws SQLException {
 result = Database.Query("select * from Verse where 1=0");
 Verse[] verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.length);
 }

 @Test
 public void testGetWithOneResultSet() throws SQLException {
 result = Database.Query("select * from Verse where Book_ID = 1 and Chapter = 1 and Verse_No = 1");
 Verse[] verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.length);
 }

 @Test
 public void testGetWithMultipleResultSet() throws SQLException {
 result = Database.Query("select * from Verse where Book_ID = 1 and Chapter = 1");
 Verse[] verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(2, verses.length);
 }
}