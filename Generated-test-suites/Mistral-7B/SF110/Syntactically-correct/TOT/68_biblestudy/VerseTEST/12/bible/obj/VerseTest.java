import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;

 @BeforeClass
 static public void setup() {
 // Initialize test data
 translation = new Translation(1);
 book = new Book(1, "Test Book");
 reference = new Reference(book.getId(), 1, 1);
 }

 @Test
 public void testNew_ValidId() {
 int id = 1;
 Verse verse = Verse.New(id);
 assertEquals("Verse with id " + id, new Integer(id), verse.getId());
 }

 @Test(expected = NullPointerException.class)
 public void testNew_NullId() {
 Verse verse = Verse.New(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGet_ValidIds() {
 int[] ids = {1, 2, 3};
 ArrayList verses = Verse.Get(new Translation[]{translation}, book.getId(), 1, 1);
 for (int index = 0; index < ids.length; ++index) {
 assertEquals("Verse with id " + ids[index], new Integer(ids[index]), verses.get(index).getId());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_InvalidIds() {
 Translation[] translations = null;
 ArrayList verses = Verse.Get(translations, book.getId(), 1, 1);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGet_ValidReference() {
 ArrayList verses = Verse.Get(new Translation[]{translation}, book, reference);
 assertEquals("Number of verses returned", verses.size(), 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_NullReference() {
 ArrayList verses = Verse.Get(new Translation[]{translation}, null, reference);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 ArrayList verses = Verse.Get(new Translation[0], book, reference);
 assertEquals("Number of verses returned", verses.size(), 0);
 }

 @Test
 public void testGetId_ValidIds() {
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals("Id returned", new Integer(1), new Integer(id));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetId_NullTranslation() {
 int id = Verse.GetId(null, book, 1, 1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetId_InvalidBookId() {
 int id = Verse.GetId(translation, null, 1, 1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetId_InvalidChapterId() {
 int id = Verse.GetId(translation, book, 0, 1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetId_InvalidVerseId() {
 int id = Verse.GetId(translation, book, 1, 0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 assertEquals("Verse:[1 bible.obj.TranslationReference@3d3a4b Verse:Test Test Book:1 1 1]", verse.toString());
 }

 @Test(expected = SQLException.class)
 public void testGet_InvalidQuery() {
 ArrayList verses = Verse.Get(new Translation[]{translation}, null, null, null);
 fail("Expected SQLException");
 }

 @Test(expected = SQLException.class)
 public void testGetId_InvalidQuery() {
 int id = Verse.GetId(-1, book.getId(), 1, 1);
 fail("Expected SQLException");
 }
}