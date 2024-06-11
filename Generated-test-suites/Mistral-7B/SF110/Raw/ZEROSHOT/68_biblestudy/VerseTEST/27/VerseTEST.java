package bible.obj;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {

 private static Verse verse;
 private static ArrayList<Translation> translations;
 private static Reference begin, end;

 @BeforeClass
 static public void setup() throws SQLException, ClassNotFoundException {
 Database.connect();
 // Assuming you have initialized translations, begin and end variables elsewhere
 translations = Arrays.asList(new Translation[0]); // Initialize with an empty list for testing
 }

 @Test
 public void testNew() {
 int id = 1;
 verse = Verse.New(id);
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int expectedId = 1;
 int actualId = Verse.GetId(translationId, bookId, chapterId, verseId);
 assertEquals(expectedId, actualId);
 }

 @Test
 public void testGet() {
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId));
 assertEquals(1, verses.size());
 verse = verses.get(0);
 assertEquals(bookId, verse.getReference().getBookId());
 assertEquals(chapterId, verse.getReference().getChapterNo());
 assertEquals(verseId, verse.getReference().getVerseNo());
 }

 @Test
 public void testGetWithMultipleVerses() {
 int bookId = 1;
 int chapterId = 1;
 int beginVerseId = 1;
 int endVerseId = 3;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, chapterId, beginVerseId), new Reference(bookId, chapterId, endVerseId));
 assertEquals(3, verses.size());
 }

 @Test
 public void testGetWithNoVerses() {
 int bookId = 1;
 int chapterId = 1;
 int beginVerseId = 1;
 int endVerseId = 2;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, chapterId, beginVerseId), new Reference(bookId, chapterId, endVerseId));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithTranslationNotFound() {
 int invalidTranslationId = -1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, chapterId, verseId));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBookNotFound() {
 int translationId = 1;
 int invalidBookId = -1;
 int chapterId = 1;
 int verseId = 1;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(invalidBookId, chapterId, verseId));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithChapterNotFound() {
 int translationId = 1;
 int bookId = 1;
 int invalidChapterId = -1;
 int verseId = 1;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, invalidChapterId, verseId));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithVerseNotFound() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int invalidVerseId = -1;
 ArrayList<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(bookId, chapterId, invalidVerseId));
 assertEquals(0, verses.size());
 }

 @Test
 public void testToString() {
 int id = 1;
 String expected = "bible.obj.Verse:[1 null null]";
 verse = new Verse(id);
 String actual = verse.toString();
 assertEquals(expected, actual);
 }
}