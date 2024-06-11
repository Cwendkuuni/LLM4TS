package bible.obj;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {

 private static Translation translation;
 private static Book book;
 private static Reference reference;
 private static Verse verse;

 @BeforeClass
 static public void setUp() {
 // Set up your test data here, for example:
 translation = new Translation(1);
 book = new Book(1, "Genesis");
 reference = new Reference(1, 1, 1);
 }

 @Test
 public void testNew() {
 int id = 1;
 verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetId() {
 int translationId = translation.getId();
 int bookId = book.getId();
 int chapterId = reference.getChapterNo();
 int verseId = reference.getVerseNo();
 int expectedId = Verse.GetId(translation, book, chapterId, verseId);
 verse = new Verse(expectedId);
 assertEquals(expectedId, verse.getId());
 }

 @Test
 public void testGet() {
 Translation[] translations = { translation };
 ArrayList verses = Verse.Get(translations, book.getReference(), reference.getReference());
 assertEquals(1, verses.size());
 verse = (Verse) verses.get(0);
 assertEquals(expectedId, verse.getId());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 int translationId = translation.getId();
 int bookId = book.getId();
 int chapterId = reference.getChapterNo();
 int verseId = reference.getVerseNo();
 int expectedId = Verse.GetId(translationId, bookId, chapterId, verseId);
 verse = new Verse(expectedId);
 assertEquals(expectedId, verse.getId());
 }

 @Test(expected = SQLException.class)
 public void testGetIdFromDatabaseFailure() throws SQLException {
 int translationId = -1;
 int bookId = book.getId();
 int chapterId = reference.getChapterNo();
 int verseId = reference.getVerseNo();
 int expectedId = Verse.GetId(translationId, bookId, chapterId, verseId);
 fail("Exception expected but none thrown.");
 }

 @Test
 public void testToString() {
 String expected = "bible.obj.Verse:[1 [bible.obj.TranslationReference:Translation:[1 Reference:[1 1 1]] Book:[Genesis, 1] Chapter:[1] Verse:[1]] Text:test]";
 assertEquals(expected, verse.toString());
 }
}