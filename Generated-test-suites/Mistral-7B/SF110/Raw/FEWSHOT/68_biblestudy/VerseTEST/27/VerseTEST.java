package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.DbResult;
import bible.util.Util;

public class VerseTest {

 static final class DbResultStub implements DbResult {
  String[] columns = {"Verse_ID", "Text", "Translation_ID", "Book_ID", "Chapter", "Verse_No"};
  int[] intValues = {1, 2, 3, 4, 5, 6};
  String[] stringsValues = {"Verse1", "Text1", "Translation1", "Book1", "Chapter1", "Verse1"};

  @Override
  public boolean next() {
    return this.index < this.intValues.length;
  }

  @Override
  public int getInt(final String columnName) {
    int index = Integer.parseInt(columnName.substring(columnName.lastIndexOf("_") + 1));
    return this.intValues[index];
  }

  @Override
  public String getString(final String columnName) {
    int index = Integer.parseInt(columnName.substring(columnName.lastIndexOf("_") + 1));
    return this.stringsValues[index];
  }

  @Override
  public void close() {
  }

  private int index = 0;
 }

 @Test
 public void testNew() {
  Verse verse = Verse.New(1);
  assertEquals("bible.obj.Verse:[1 null null]", verse.toString());
 }

 @Test
 public void testGetId() {
  int id = Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, 1);
  assertEquals(1, id);
 }

 @Test
 public void testGet() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, "Book1", 1), new Reference(1, "Book1", 1));
  assertEquals(1, verses.size());
  assertEquals("bible.obj.Verse:[1 null null]", verses.get(0).toString());
 }

 @Test
 public void testGetWithTranslations() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, null, null);
  assertEquals(1, verses.size());
  assertEquals("bible.obj.Verse:[1 null null]", verses.get(0).toString());
 }

 @Test
 public void testGetIdWithNullTranslation() {
  assertNull(Verse.GetId(null, new Book(1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdWithNullBook() {
  assertNull(Verse.GetId(new Translation(1), null, 1, 1));
 }

 @Test
 public void testGetIdWithNullChapter() {
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), null, 1));
 }

 @Test
 public void testGetIdWithNullVerse() {
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, null));
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
  assertNull(Verse.GetId(-1, new Book(1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdWithInvalidBookId() {
  assertNull(Verse.GetId(new Translation(1), new Book(-1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdWithInvalidChapterId() {
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), -1, 1));
 }

 @Test
 public void testGetIdWithInvalidVerseId() {
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, -1));
 }

 @Test
 public void testGetIdWithExistingId() {
  int id = Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, 1);
  Verse verse = new Verse(id);
  assertEquals("bible.obj.Verse:[" + id + " null null]", verse.toString());
 }

 @Test
 public void testGetWithInvalidBeginReference() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, null, null);
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndReference() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(-1, "Book1", 1), new Reference(-1, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginBookId() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, -1, 1), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginChapter() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 0, 1), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidBeginVerse() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 0), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndBookId() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(-1, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndChapter() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(0, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidEndVerse() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 0));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithValidArguments() {
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, "Book1", 1), new Reference(1, "Book1", 2));
  assertEquals(2 - 1, verses.size());
  for (int i = 0; i < verses.size(); i++) {
    assertEquals("bible.obj.Verse:[" + (i + 1) + " null null]", verses.get(i).toString());
  }
 }

 @Test
 public void testGetIdFromDatabase() {
  DbResult result = new DbResultStub();
  int id = Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, 1);
  assertEquals(1, id);
 }

 @Test
 public void testGetIdFromDatabase_InvalidTranslationId() {
  DbResult result = new DbResultStub();
  assertNull(Verse.GetId(new Translation(-1), new Book(1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdFromDatabase_InvalidBookId() {
  DbResult result = new DbResultStub();
  assertNull(Verse.GetId(new Translation(1), new Book(-1, "Book1"), 1, 1));
 }

 @Test
 public void testGetIdFromDatabase_InvalidChapter() {
  DbResult result = new DbResultStub();
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), -1, 1));
 }

 @Test
 public void testGetIdFromDatabase_InvalidVerse() {
  DbResult result = new DbResultStub();
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, -1));
 }

 @Test
 public void testGetIdFromDatabase_NoResult() {
  DbResult result = new DbResultStub();
  assertNull(Verse.GetId(new Translation(1), new Book(1, "Book1"), 1, 1));
 }

 @Test
 public void testGetFromDatabase() {
  DbResult result = new DbResultStub();
  ArrayList verses = Verse.Get(new Translation[0], null, null);
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithValidArguments() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, "Book1", 1), new Reference(1, "Book1", 2));
  assertEquals(2 - 1, verses.size());
  for (int i = 0; i < verses.size(); i++) {
    assertEquals("bible.obj.Verse:[" + (i + 1) + " null null]", verses.get(i).toString());
  }
 }

 @Test
 public void testGetFromDatabaseWithInvalidBeginReference() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, null, null);
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidEndReference() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(-1, "Book1", 1), new Reference(-1, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidBeginBookId() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, -1, 1), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidBeginChapter() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 0, 1), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidBeginVerse() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 0), new Reference(1, 1, 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidEndBookId() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(-1, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidEndChapter() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(0, "Book1", 1));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithInvalidEndVerse() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 0));
  assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabaseWithValidArguments() {
  DbResult result = new DbResultStub();
  Translation[] translations = new Translation[1];
  translations[0] = new Translation(1);
  ArrayList verses = Verse.Get(translations, new Reference(1, "Book1", 1), new Reference(1, "Book1", 2));
  assertEquals(2 - 1, verses.size());
  for (int i = 0; i < verses.size(); i++) {
    assertEquals("bible.obj.Verse:[" + (i + 1) + " null null]", verses.get(i).toString());
  }
 }
}