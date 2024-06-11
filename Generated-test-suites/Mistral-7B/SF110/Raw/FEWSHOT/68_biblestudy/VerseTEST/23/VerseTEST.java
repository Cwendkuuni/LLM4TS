package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.sql.DbResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStats;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class VerseTest {

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 }

 @Test
 public void testGet() {
 Verse verse1 = new Verse(1);
 Verse verse2 = Verse.New(1);
 assertEquals(verse1, verse2);
 }

 @Test
 public void testGetId() {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 assertEquals(id, Verse.GetId(Translation.NEW, Book.NEW, chapterId, verseId));
 }

 @Test
 public void testGetByIds() {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 final Translation translation = Translation.NEW;
 final Book book = Book.NEW;
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(bookId, chapterId, verseId));
 assertEquals(1, verses.size());
 assertEquals(verseId, verses.get(0).getId());
 }

 @Test
 public void testGetByRange() {
 final int translationId = 1;
 final int bookId = 1;
 final int beginChapter = 1;
 final int beginVerse = 1;
 final int endChapter = 2;
 final int endVerse = 2;
 final Translation translation = Translation.NEW;
 final Book book = Book.NEW;
 List<Verse> verses = Verse.Get(new Translation[] { translation }, book, new Reference(bookId, beginChapter, beginVerse), new Reference(bookId, endChapter, endVerse));
 IntSummaryStats stats = new IntSummaryStats();
 for (Verse verse : verses) {
 stats.addValue(verse.getId());
 }
 assertEquals(3, stats.getN());
 assertTrue(stats.getMin() <= beginChapter * 100 + beginVerse);
 assertTrue(stats.getMax() >= beginChapter * 100 + endVerse);
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 DbResult result = Database.Query("select Verse_ID from Verse where Verse_ID = " + id);
 assertNotNull(result.next());
 assertEquals(id, result.getInt("Verse_ID"));
 }

 @Test
 public void testComplete() throws SQLException {
 final int translationId = 1;
 final int bookId = 1;
 final int chapterId = 1;
 final int verseId = 1;
 DbResult result = Database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = " + verseId);
 Verse verse = new Verse(result);
 int id = verse.getId();
 assertEquals(id, verseId);
 assertEquals(verse.getText(), result.getString("Text"));
 TranslationReference transRef = verse.getTranslationReference();
 assertEquals(translationId, transRef.getTranslation().getId());
 assertEquals(new Reference(bookId, chapterId, verseId), transRef.getReference());
 }
}