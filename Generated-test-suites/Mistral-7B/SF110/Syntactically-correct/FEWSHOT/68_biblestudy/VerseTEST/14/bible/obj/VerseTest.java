package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.junit.Test;

public class VerseTest {

 private static final String[] TEST_TRANSLATIONS = new String[]{"translation1", "translation2"};
 private static final int[] TEST_TRANSLATION_IDS = new int[]{1, 2};
 private static final int[] TEST_BOOK_IDS = new int[]{1, 2, 3};
 private static final int[] TEST_CHAPTER_IDS = new int[]{1, 2, 3};
 private static final int[] TEST_VERSE_IDS = new int[]{1, 2, 3, 4};
 private static final Reference TEST_BEGIN = new Reference(1, 1, 1);
 private static final Reference TEST_END = new Reference(3, 1, 2);

 @Test
 public void testNew() {
 Verse verse1 = Verse.New(TEST_VERSE_IDS[0]);
 Verse verse2 = Verse.New(TEST_VERSE_IDS[1]);
 assertEquals(TEST_VERSE_IDS[0], verse1.getId());
 assertEquals(TEST_VERSE_IDS[1], verse2.getId());
 }

 @Test
 public void testGetId() {
 assertEquals(Verse.GetId(null, null, 1, 1), -1);
 assertEquals(Verse.GetId(TEST_TRANSLATIONS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]), TEST_VERSE_IDS[0]);
 assertEquals(Verse.GetId(TEST_TRANSLATION_IDS[1], TEST_BOOK_IDS[1], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[1]), TEST_VERSE_IDS[1]);
 }

 @Test
 public void testGet() {
 ArrayList verses1 = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 ArrayList verses2 = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[1], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[1]);
 ArrayList verses3 = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, TEST_END);
 assertEquals(TEST_TRANSLATIONS.length, verses1.size());
 assertEquals(TEST_TRANSLATIONS.length, verses2.size());
 assertEquals(TEST_TRANSLATIONS.length * TEST_CHAPTER_IDS.length * TEST_VERSE_IDS.length, verses3.size());
 }

 @Test
 public void testGetIdFromDatabase() throws Exception {
 Verse verse1 = new Verse(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 int id1 = Verse.GetId(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 assertEquals(id1, verse1.getId());
 }

 @Test
 public void testGetFromDatabase() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]});
 Verse verse1 = new Verse(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 Verse verse2 = new Verse(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]);
 Database.setQueryResult(new MockDbResult[]{result1, result2});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], null);
 assertEquals(2, verses.size());
 assertEquals(text1, verses.get(0).getText());
 assertEquals(text2, verses.get(1).getText());
 }

 @Test
 public void testGetFromDatabaseWithTranslationFiltering() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[1], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]});
 Database.setQueryResult(new MockDbResult[]{result1, result2});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS);
 assertEquals(1, verses.size());
 assertEquals(text1, verses.get(0).getText());
 }

 @Test
 public void testGetFromDatabaseWithBookFiltering() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[1], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[1], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]});
 Database.setQueryResult(new MockDbResult[]{result1, result2});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[1], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithChapterFiltering() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[1]});
 Database.setQueryResult(new MockDbResult[]{result1, result2});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithVerseFiltering() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]});
 Database.setQueryResult(new MockDbResult[]{result1, result2});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[1]);
 assertEquals(1, verses.size());
 assertEquals(text2, verses.get(0).getText());
 }

 @Test
 public void testGetFromDatabaseWithReferenceRange() throws Exception {
 String text1 = "This is verse 1.";
 String text2 = "This is verse 2.";
 DbResult result1 = new MockDbResult(new Object[]{"1", text1, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[0]});
 DbResult result2 = new MockDbResult(new Object[]{"2", text2, TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[1]});
 DbResult result3 = new MockDbResult(new Object[]{"3", "This is verse 3.", TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[1], TEST_VERSE_IDS[2]});
 Database.setQueryResult(new MockDbResult[]{result1, result2, result3});
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, TEST_BEGIN, TEST_END);
 assertEquals(2, verses.size());
 assertEquals(text1, verses.get(0).getText());
 assertEquals(text2, verses.get(1).getText());
 }

 @Test
 public void testGetFromDatabaseWithNullReference() throws Exception {
 ArrayList verses = Verse.Get(TEST_TRANSLATIONS, null, null, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetIdFromDatabaseWithNullTranslationId() throws Exception {
 try {
 Verse.GetId(-1, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 fail("Expected SQLException");
 }
 catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testGetIdFromDatabaseWithNullBookId() throws Exception {
 try {
 Verse.GetId(TEST_TRANSLATION_IDS[0], -1, TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 fail("Expected SQLException");
 }
 catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testGetIdFromDatabaseWithNullChapterId() throws Exception {
 try {
 Verse.GetId(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], -1, TEST_VERSE_IDS[0]);
 fail("Expected SQLException");
 }
 catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testGetIdFromDatabaseWithNullVerseId() throws Exception {
 try {
 Verse.GetId(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], -1);
 fail("Expected SQLException");
 }
 catch (SQLException e) {
 // Expected
 }
 }

 @Test
 public void testGetFromDatabaseWithNullTranslationId() throws Exception {
 try {
 Verse verse = new Verse(null, TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetFromDatabaseWithNullBookId() throws Exception {
 try {
 Verse verse = new Verse(TEST_TRANSLATION_IDS[0], null, TEST_CHAPTER_IDS[0], TEST_VERSE_IDS[0]);
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetFromDatabaseWithNullChapterId() throws Exception {
 try {
 Verse verse = new Verse(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], null, TEST_VERSE_IDS[0]);
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetFromDatabaseWithNullVerseId() throws Exception {
 try {
 Verse verse = new Verse(TEST_TRANSLATION_IDS[0], TEST_BOOK_IDS[0], TEST_CHAPTER_IDS[0], null);
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }

 private static class MockDbResult implements DbResult {
 private Object[] data;

 MockDbResult(Object[] data) {
 this.data = data;
 }

 @Override
 public boolean next() {
 return this.data != null && this.data.length > 0;
 }

 @Override
 public int getInt(String columnName) throws SQLException {
 return Integer.parseInt(this.data[0][columnName.indexOf('_') >= 0 ? columnName.substring(columnName.indexOf('_') + 1) : columnName]);
 }

 @Override
 public String getString(String columnName) throws SQLException {
 return (String) this.data[0][columnName.indexOf('_') >= 0 ? this.data[0][columnName.substring(columnName.indexOf('_') + 1).length()] : this.data[0][columnName]];
 }
 }
}