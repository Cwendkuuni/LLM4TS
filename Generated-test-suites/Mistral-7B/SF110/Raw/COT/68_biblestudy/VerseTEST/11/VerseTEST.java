import org.junit.Test;
import org.junit.Assert.*;
import bible.obj.Verse;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Util;
import java.sql.DbResult;
import java.sql.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.IntSummaryStatistics;

public class VerseTest {

 private static final int[] TEST_TRANSLATION_IDS = {1, 2, 3};
 private static final int[] TEST_BOOK_IDS = {1, 2, 3};
 private static final int[] TEST_CHAPTER_IDS = {1, 2, 3};
 private static final int[] TEST_VERSE_IDS = {1, 2, 3, 4, 5};
 private static final Reference TEST_REFERENCE = new Reference(1, 1, 1);
 private static final TranslationReference TEST_TRANSLATION_REFERENCE = new TranslationReference(new Translation(1), TEST_REFERENCE);
 private static final Verse TEST_VERSE_1 = new Verse(1);
 private static final Verse TEST_VERSE_2 = new Verse(2);
 private static final Verse TEST_VERSE_3 = new Verse(3);
 private static final Verse TEST_VERSE_4 = new Verse(4);
 private static final Verse TEST_VERSE_5 = new Verse(5);
 private static final ArrayList TEST_VERSES = new ArrayList() {{
 add(TEST_VERSE_1);
 add(TEST_VERSE_2);
 add(TEST_VERSE_3);
 add(TEST_VERSE_4);
 add(TEST_VERSE_5);
 }};

 @Test
 public void testNew() {
 Verse verse = Verse.New(TEST_VERSE_IDS[0]);
 assertEquals(TEST_VERSE_IDS[0], verse.getId());
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(null, null, TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);

 id = Verse.GetId(TEST_TRANSLATION_REFERENCE, null, TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);

 id = Verse.GetId(TEST_TRANSLATION_REFERENCE, new Book(TEST_BOOK_IDS[0]), TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);
 }

 @Test
 public void testGet() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSES.size(), verses.size());

 for (int index = 0; index < TEST_VERSES.size(); ++index) {
 assertEquals(TEST_VERSES.get(index).getId(), verses.get(index).getId());
 }
 }

 @Test
 public void testGet_NullParams() {
 ArrayList verses = Verse.Get(null, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 ArrayList verses = Verse.Get(new Translation[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyVerses() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, null, TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidParameters() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[new int[] {5, 6}]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidQuery() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[new int[] {-1, 0}]);
 assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGet_Exception() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[new int[] {5, 6, 7}]);
 fail("An SQLException should have been thrown.");
 }

 @Test
 public void testToString() {
 String expected = "bible.obj.Verse:[1 [bible.obj.TranslationReference:[Translation:[1], Reference:[1 1 1]]] John 1:1]";
 assertEquals(expected, TEST_VERSE_1.toString());
 }

 @Test
 public void testGetId_NullTranslation() {
 int id = Verse.GetId(-1, null, TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_NullBook() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, null, TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);
 }

 @Test
 public void testGetId_NullChapter() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, new Book(TEST_BOOK_IDS[0]), null, TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);
 }

 @Test
 public void testGetId_NullVerse() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, new Book(TEST_BOOK_IDS[0]), TEST_CHAPTER_IDS[0], null);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidTranslationId() {
 int id = Verse.GetId(-1, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[new int[] {-1, 0}]);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidBookId() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[new int[] {-1, 0}], TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[0]);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidChapterId() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[new int[] {-1, 0}], TEST_VERSE_IDs[0]);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_InvalidVerseId() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDs[new int[] {-1, -1}]);
 assertEquals(-1, id);
 }

 @Test
 public void testGetId_NullCache() {
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);
 }

 @Test
 public void testGetId_CachedVerse() {
 Verse verse = TEST_VERSE_1;
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(TEST_VERSE_IDs[0], id);
 assertEquals(verse, Verse.New(id));
 }

 @Test
 public void testGetId_NewVerse() {
 Verse newVerse = new Verse(0);
 int id = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[TEST_VERSES.size()]);
 assertEquals(newVerse.getId(), id);
 assertEquals(newVerse, Verse.New(id));
 }

 @Test
 public void testGetId_SameVerses() {
 Verse verse1 = TEST_VERSE_1;
 Verse verse2 = TEST_VERSE_1;
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(id1, id2);
 assertEquals(verse1, Verse.New(id1));
 assertEquals(verse2, Verse.New(id2));
 }

 @Test
 public void testGetId_DifferentVerses() {
 Verse verse1 = TEST_VERSE_1;
 Verse verse2 = TEST_VERSE_2;
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDs[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[1]);
 assertNotEquals(id1, id2);
 assertNotEquals(verse1, Verse.New(id2));
 assertNotEquals(verse2, Verse.New(id1));
 }

 @Test
 public void testGetId_MultipleCalls() {
 Verse verse = TEST_VERSE_1;
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(id1, id2);
 assertEquals(verse, Verse.New(id1));
 }

 @Test
 public void testGetId_SameTranslationAndBook() {
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[1]);
 assertEquals(id1, id2);
 }

 @Test
 public void testGetId_SameTranslationButDifferentBook() {
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[1], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertNotEquals(id1, id2);
 }

 @Test
 public void testGetId_DifferentTranslationsButSameBookAndChapter() {
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE1, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE2, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertNotEquals(id1, id2);
 }

 @Test
 public void testGetId_MultipleVersesInSameTranslationAndBook() {
 int id1 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 int id2 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[1]);
 int id3 = Verse.GetId(TEST_TRANSLATION_REFERENCE, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[2]);
 IntSummaryStatistics idStatistics = new IntSummaryStatistics();
 idStatistics.add(id1);
 idStatistics.add(id2);
 idStatistics.add(id3);
 assertEquals(3, idStatistics.getCount());
 assertEquals(Math.min(id1, Math.min(id2, id3)), idStatistics.getMin());
 assertEquals(Math.max(id1, Math.max(id2, id3)), idStatistics.getMax());
 }

 @Test
 public void testGetId_ManyVersesInDifferentTranslationsAndBooks() {
 int[] ids = new int[TEST_VERSE_IDS.length];
 for (int index = 0; index < TEST_VERSE_IDS.length; ++index) {
 ids[index] = Verse.GetId(TEST_TRANSLATION_REFERENCES[index % 3], TEST_BOOK_IDS[index % 3], TEST_CHAPTER_IDS[index / 3], TEST_VERSE_IDS[index]);
 }
 IntSummaryStatistics idStatistics = new IntSummaryStatistics();
 for (int id : ids) {
 idStatistics.add(id);
 }
 assertEquals(TEST_VERSE_IDS.length, idStatistics.getCount());
 }

 @Test
 public void testGet() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[new int[] {0, 1, 2, 3, 4}]);
 assertEquals(TEST_VERSES.size(), verses.size());

 for (int index = 0; index < TEST_VERSES.size(); ++index) {
 assertEquals(TEST_VERSES.get(index).getId(), verses.get(index).getId());
 }
 }

 @Test
 public void testGet_NullParams() {
 ArrayList verses = Verse.Get(null, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyTranslationArray() {
 ArrayList verses = Verse.Get(new Translation[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyVerses() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, null, TEST_CHAPTER_IDs[0], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidParameters() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[new int[] {-1, 0}], TEST_VERSE_IDs[0]);
 assertEquals(0, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGet_Exception() {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDs[new int[] {5, 6, 7}], TEST_VERSE_IDS[0]);
 fail("An SQLException should have been thrown.");
 }

 }