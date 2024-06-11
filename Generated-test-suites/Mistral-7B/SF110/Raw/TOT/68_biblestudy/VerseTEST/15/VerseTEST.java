import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 private static Verse verse;
 private static Translation translation;
 private static Reference reference;
 private static int id;

 @BeforeClass
 public static void setUp() {
 // Initialize the database and create some test data
 Database.init();
 id = Verse.GetId(new Translation(1), new Book(1, "Test Book"), 1, 1).intValue();
 verse = Verse.New(id);
 translation = new Translation(1);
 reference = new Reference(1, 1, 1);
 }

 @AfterClass
 public static void tearDown() {
 // Clean up the database
 Database.shutdown();
 }

 @Test
 public void testNewVerseWithValidId() {
 Verse testVerse = Verse.New(id);
 assertEquals("Verse:[1 TestReference:[Reference:[1 1 1]] TestTranslation:[Translation:[1]] TestText:]", testVerse.toString());
 }

 @Test
 public void testGetId() {
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetText() {
 assertEquals("", verse.getText());
 }

 @Test
 public void testGetReference() {
 Reference reference = verse.getReference();
 assertEquals("Reference:[1 1 1]", reference.toString());
 }

 @Test
 public void testGetTranslationReference() {
 TranslationReference transRef = verse.getTranslationReference();
 Translation translation = transRef.getTranslation();
 assertEquals("Translation:[Translation:[1]]", translation.toString());
 }

 @Test
 public void testGetVersesWithValidTranslationAndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(verse, verses.get(0));
 }

 @Test
 public void testGetVersesWithNullTranslationAndReference() {
 ArrayList verses = Verse.Get(null, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithEmptyTranslationArray() {
 Translation[] translations = new Translation[0];
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithInvalidBookId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(-1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithInvalidChapterId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, -1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithInvalidVerseId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, -1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithInvalidTranslationId() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(-1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVerseWithInvalidId() {
 int invalidId = -1;
 Verse testVerse = Verse.New(invalidId);
 assertNull(testVerse);
 }

 @Test
 public void testGetVersesWithValidBeginAndEnd() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetVersesWithInvalidBeginReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(-1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithInvalidEndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(-1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetIdWithValidTranslationReference() {
 int id = Verse.GetId(translation, new Book(1, "Test Book"), 1, 1);
 assertEquals(Verse.New(id).getId(), id);
 }

 @Test
 public void testGetIdWithInvalidTranslationReference() {
 int id = Verse.GetId(null, new Book(1, "Test Book"), 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookReference() {
 int id = Verse.GetId(translation, null, 1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
 int id = Verse.GetId(translation, new Book(1, "Test Book"), -1, 1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(translation, new Book(1, "Test Book"), 1, -1);
 assertEquals(-1, id);
 }

 @Test
 public void testGetVersesWithEmptyTranslationArrayAndValidBeginAndEnd() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithValidTranslationArrayAndInvalidBeginReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(-1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithValidTranslationArrayAndInvalidEndReference() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(-1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithValidTranslationArrayAndValidBeginAndEnd() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }
}