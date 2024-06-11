import org.junit.Test;
import org.junit.Assert;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Reference;
import bible.util.Util;

public class VerseTest {

 private static final Verse NEW_VERSE_ID = 1;
 private static final int TRANSLATION_ID = 1;
 private static final int BOOK_ID = 1;
 private static final int CHAPTER = 1;
 private static final int VERSE_ID = 1;
 private static final int ANOTHER_VERSE_ID = 2;
 private static final int INVALID_ID = -1;
 private static final Translation TRANSLATION = new Translation(TRANSLATION_ID);
 private static final Reference REFERENCE = new Reference(BOOK_ID, CHAPTER, VERSE_ID);
 private static final TranslationReference TRANSLATION_REFERENCE = new TranslationReference(TRANSLATION, REFERENCE);
 private static final String INVALID_TEXT = "Invalid Text";
 private static final String VALID_TEXT = "John doe in the book of Genesis 1:1";
 private static final List<Translation> TRANSLATIONS = new ArrayList<>();
 private static final Reference BEGIN = new Reference(BOOK_ID, CHAPTER, VERSE_ID);
 private static final Reference END = new Reference(BOOK_ID, CHAPTER, VERSE_ID);
 private static final DbResult RESULT = null;

 @Test
 public void testNewStatic() throws SQLException {
 Verse verse = Verse.New(NEW_VERSE_ID);
 Assert.assertNotNull(verse);
 }

 @Test
 public void testNewIdNotInCache() throws SQLException {
 Verse verse = new Verse(NEW_VERSE_ID);
 Assert.assertEquals(NEW_VERSE_ID, verse.getId());
 }

 @Test
 public void testNewIdInCache() throws SQLException {
 Verse verse = Verse.New(NEW_VERSE_ID);
 Verse cachedVerse = Verse.New(NEW_VERSE_ID);
 Assert.assertSame(verse, cachedVerse);
 }

 @Test
 public void testGetId() {
 int id = Verse.GetId(TRANSLATION, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(NEW_VERSE_ID, id);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithNullTranslation() {
 int id = Verse.GetId(null, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(TRANSLATION_ID, -1, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapter() {
 int id = Verse.GetId(TRANSLATION_ID, BOOK_ID, -1, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
 int id = Verse.GetId(TRANSLATION_ID, BOOK_ID, CHAPTER, -1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithMultipleValidParameters() throws SQLException {
 List<Verse> verses = Verse.Get(TRANSLATIONS.toArray(new Translation[0]), BOOK_ID, CHAPTER, VERSE_ID);
 int id = verses.get(0).getId();
 Assert.assertEquals(NEW_VERSE_ID, id);
 }

 @Test
 public void testGetIdWithNullTranslationArray() {
 List<Verse> verses = Verse.Get(null, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginAndEnd() throws SQLException {
 List<Verse> verses = Verse.Get(TRANSLATIONS.toArray(new Translation[0]), BEGIN, END);
 Assert.assertEquals(1, verses.size());
 }

 @Test
 public void testGetWithInvalidBegin() throws SQLException {
 List<Verse> verses = Verse.Get(TRANSLATIONS.toArray(new Translation[0]), new Reference(-1, CHAPTER, VERSE_ID), END);
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEnd() throws SQLException {
 List<Verse> verses = Verse.Get(TRANSLATIONS.toArray(new Translation[0]), BEGIN, new Reference(-1, CHAPTER, VERSE_ID));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginAndEnd() throws SQLException {
 List<Verse> verses = Verse.Get(TRANSLATIONS.toArray(new Translation[0]), new Reference(-1, CHAPTER, VERSE_ID), new Reference(-1, CHAPTER, VERSE_ID));
 Assert.assertEquals(0, verses.size());
 }

 @Test
 public void testToString() {
 String toString = new Verse(NEW_VERSE_ID).toString();
 Assert.assertEquals(Verse.class.getName() + ":[" + NEW_VERSE_ID + " " + TRANSLATION_REFERENCE + " " + VALID_TEXT + "]", toString);
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithInvalidQueryResult() throws SQLException {
 Verse.GetId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.fail("SQLException expected but not thrown");
 }

 @Test
 public void testGetIdWithInvalidTranslationMap() {
 int id = Verse.GetId(-1, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidBookMap() {
 int id = Verse.GetId(TRANSLATION_ID, -1, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidChapterMap() {
 int id = Verse.GetId(TRANSLATION_ID, BOOK_ID, -1, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidVerseMap() {
 int id = Verse.GetId(TRANSLATION_ID, BOOK_ID, CHAPTER, -1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetIdWithInvalidCache() throws SQLException {
 Verse verse = new Verse(NEW_VERSE_ID);
 Verse.verseCache.remove(new Integer(NEW_VERSE_ID));
 int id = Verse.GetId(TRANSLATION, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(NEW_VERSE_ID, id);
 }

 @Test
 public void testNewWithInvalidId() throws SQLException {
 try {
 Verse verse = new Verse(INVALID_ID);
 Assert.fail("SQLException expected but not thrown");
 }
 catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithValidResult() throws SQLException {
 Verse verse = new Verse(RESULT);
 Assert.assertEquals(INVALID_ID, verse.getId());
 Assert.assertEquals(INVALID_TEXT, verse.getText());
 Assert.assertNull(verse.getTranslationReference());
 }

 @Test(expected = SQLException.class)
 public void testCompleteWithInvalidResult() throws SQLException {
 Verse verse = new Verse(RESULT);
 Assert.fail("SQLException expected but not thrown");
 }

 @Test
 public void testCompleteWithValidData() throws SQLException {
 Verse verse = new Verse(RESULT);
 Assert.assertEquals(NEW_VERSE_ID, verse.getId());
 Assert.assertEquals(VALID_TEXT, verse.getText());
 Assert.assertEquals(TRANSLATION_REFERENCE, verse.getTranslationReference());
 }

 @Test
 public void testReadIdWithValidData() throws SQLException {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(NEW_VERSE_ID, id);
 }

 @Test(expected = SQLException.class)
 public void testReadIdWithInvalidData() throws SQLException {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.fail("SQLException expected but not thrown");
 }

 @Test
 public void testReadIdWithInvalidTranslationMap() {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidBookMap() {
 int id = Verse.ReadId(TRANSLATION_ID, -1, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidChapterMap() {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, -1, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithInvalidVerseMap() {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, -1);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testReadIdWithNullTranslationMap() {
 int id = Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID);
 Assert.assertEquals(-1, id);
 }

 @Test
 public void testGetStaticVariables() {
 Assert.assertNotNull(Verse.translations);
 Assert.assertNotNull(Verse.verseCache);
 }
}