import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import static bible.util.Util.ToString;

public class VerseTest {

 private int id;
 private String text;
 private TranslationReference transRef;
 private Verse verse;
 private Translation translation;
 private Book book;
 private Reference ref;

 @Before
 public void setUp() {
 id = 1;
 text = "And God said, Let there be light: and there was light.";
 transRef = new TranslationReference(new Translation(1, "KJV"), new Reference(1, 1, 3));
 translation = new Translation(1, "KJV");
 book = new Book(1, "Genesis");
 ref = new Reference(1, 1, 3);
 }

 @Test
 public void testNew() {
 verse = Verse.New(id);
 assertEquals(Integer.valueOf(id), verse.getId());
 assertEquals(text, verse.getText());
 assertEquals(transRef, verse.getTranslationReference());
 assertEquals(ref, verse.getReference());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 3;
 id = Verse.GetId(translation, book, chapterId, verseId);
 assertEquals(Integer.valueOf(id), Verse.New(id).getId());
 }

 @Test
 public void testGet() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book.getReference(), new Reference(bookId, 1, 5));
 assertEquals(1, verses.size());
 assertEquals(verse, verses.get(0));
 }

 @Test
 public void testGetWithInvalidChapter() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book.getReference(), new Reference(bookId, 1, 0));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidVerse() {
 Translation[] translations = {translation};
 ArrayList verses = Verse.Get(translations, book.getReference(), new Reference(bookId, 1, 5));
 Verses.remove(verses.get(0));
 try {
 int id = Verse.GetId(translation, book, chapterId, verseId);
 fail("Expected SQLException");
 } catch (SQLException e) {
 //expected
 }
 }

 @Test
 public void testDatabaseConnection() {
 ArrayList verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 10));
 assertEquals(0, verses.size());
 }

 @Test
 public void testDatabaseQuery() {
 ArrayList verses = Verse.Get(new Translation[] {translation}, new Reference(1, 1, 1), new Reference(1, 1, 5));
 assertEquals(5, verses.size());
 }

 @After
 public void tearDown() {
 Database.Close();
 }
}