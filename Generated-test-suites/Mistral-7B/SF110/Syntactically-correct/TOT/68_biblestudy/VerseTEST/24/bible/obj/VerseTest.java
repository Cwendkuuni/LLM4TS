import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.*;
import static bible.util.Util.toString;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;

 @BeforeClass
 public static void setup() {
 // Assuming setup for Database.Query(), Translation.Get(), and Reference.getReference() has been done elsewhere

 translation = new Translation(1);
 book = new Book(1, "Test Book");
 reference = new Reference(1, 1, 1);
 }

 @Test
 public void testGetVerseId() {
 int verseId = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, verseId);
 }

 @Test
 public void testGetEmptyVerses() {
 Translation[] emptyTranslationArray = {};
 ArrayList verses = Verse.Get(emptyTranslationArray, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVerses() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book, 1, 1);
 assertEquals(1, verses.size());
 Verse verse = (Verse) verses.get(0);
 assertEquals(translation.getId(), verse.getTranslationReference().getTranslation().getId());
 assertEquals(book, verse.getTranslationReference().getReference().getBook());
 assertEquals(1, verse.getTranslationReference().getReference().getChapterNo());
 assertEquals(1, verse.getTranslationReference().getReference().getVerseNo());
 assertEquals(verse.getText(), "Test Text"); // Assuming Verse has a setText method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVersesWithNullTranslationArray() {
 Verse.Get(null, book, 1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVersesWithNullBeginOrEnd() {
 Verse.Get(translations, null, 1, 1);
 Verse.Get(translations, book, null, 1);
 }

 @Test
 public void testGetVersesWithNoMatches() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book, 2, 2);
 assertTrue(verses.isEmpty());
 }

 @Test
 public void testGetVersesWithMultipleMatches() {
 Translation[] translations = {translation, new Translation(2)};
 ArrayList verses = Verse.Get(translations, book, 1, 1);
 assertEquals(2, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidTranslationId() {
 Verse.GetId(-1, book, 1, 1);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidBookId() {
 Verse.GetId(translation.getId(), -1, 1, 1);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidChapterOrVerse() {
 Verse.GetId(translation.getId(), book.getId(), -1, 1);
 Verse.GetId(translation.getId(), book.getId(), 1, -1);
 }

 @Test
 public void testNewVerse() {
 Verse verse = Verse.New(1);
 assertEquals(1, verse.getId());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewNullId() {
 Verse.New(null);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "bible.obj.Verse:[" + 1 + " " + toString(verse.getTranslationReference().getTranslation()) + " " + toString(verse.getTranslationReference().getReference()) + " " + verse.getText() + "]";
 assertEquals(expected, verse.toString());
 }
}