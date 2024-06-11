import org.junit.Assert;
import org.junit.Test;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 Assert.assertNotNull(verse);
 Assert.assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId_Valid() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(translation, book, 1, 1);
 Assert.assertEquals(1, id);
 }

 @Test
 public void testGetId_TranslationNotFound() {
 int translationId = -1;
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(new Translation[0], book, 1, 1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_BookNotFound() {
 Translation translation = new Translation(1, "English");
 Reference reference = new Reference(1, 1, 1);
 int id = Verse.GetId(translation, null, 1, 1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_ChapterNotFound() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 int id = Verse.GetId(translation, book, 0, 1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetId_VerseNotFound() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 int id = Verse.GetId(translation, book, 1, 0);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGet_EmptyArray() {
 Translation[] translations = new Translation[0];
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 ArrayList verses = Verse.Get(translations, new Reference(bookId, chapterId, verseId));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_Valid() throws SQLException {
 Translation translation = new Translation(1, "English");
 Translation translation2 = new Translation(2, "Spanish");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 ArrayList verses = Verse.Get(new Translation[] {translation, translation2}, book, reference, reference);
 Assert.assertEquals(2, verses.size());
 }

 @Test
 public void testGet_TranslationNotFound() throws SQLException {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 1);
 ArrayList verses = Verse.Get(new Translation[0], book, begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_BookNotFound() throws SQLException {
 Translation[] translations = new Translation[] {new Translation(1, "English"), new Translation(2, "Spanish")};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 1);
 ArrayList verses = Verse.Get(translations, null, begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginReference() throws SQLException {
 Translation[] translations = new Translation[] {new Translation(1, "English"), new Translation(2, "Spanish")};
 Reference begin = new Reference(0, 1, 1);
 Reference end = new Reference(1, 1, 1);
 ArrayList verses = Verse.Get(translations, new Book(1, "Genesis", 1), begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidEndReference() throws SQLException {
 Translation[] translations = new Translation[] {new Translation(1, "English"), new Translation(2, "Spanish")};
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(-1, 1, 1);
 ArrayList verses = Verse.Get(translations, new Book(1, "Genesis", 1), begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidBeginAndEndReferences() throws SQLException {
 Translation[] translations = new Translation[] {new Translation(1, "English"), new Translation(2, "Spanish")};
 Reference begin = new Reference(0, 1, 1);
 Reference end = new Reference(-1, 1, 1);
 ArrayList verses = Verse.Get(translations, new Book(1, "Genesis", 1), begin, end);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGet_ValidQueryResult() throws SQLException {
 Translation[] translations = new Translation[] {new Translation(1, "English"), new Translation(2, "Spanish")};
 Book book = new Book(1, "Genesis", 1);
 Reference begin = new Reference(1, 1, 1);
 Reference end = new Reference(1, 1, 3);
 DbResult result = Database.Query("select Verse_ID from Verse where Book_ID = 1 and Chapter = 1 and Verse_No >= 1 and Verse_No <= 3");
 ArrayList verses = new ArrayList();
 while (result.next()) {
 verses.add(Verse.New(result.getInt("Verse_ID")));
 }
 ArrayList testVerses = Verse.Get(translations, book, begin, end);
 Assert.assertEquals(verses.size(), testVerses.size());
 for (int i = 0; i < verses.size(); i++) {
 Assert.assertEquals(verses.get(i), testVerses.get(i));
 }
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 String expected = "bible.obj.Verse:[1 [bible.obj.TranslationReference:[bible.obj.Translation:[1 English], bible.obj.Reference:[1 1 1]] bible.obj.String:Test Verse]]";
 Assert.assertEquals(expected, verse.toString());
 }
}