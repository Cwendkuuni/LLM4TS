package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class VerseTest {

 private static final String DB_URL = "jdbc:mock:myds:1"; // Replace with your actual database URL

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.GetId(translationId, bookId, chapterId, verseId);
 assertEquals(id, verseId);
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");
 List<Verse> verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertNotNull(verses.get(0));
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
 List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithBeginEndReference() throws SQLException {
 DatabaseMetaData dbMetaData = Database.getConnection().getMetaData();
 String query = dbMetaData.getTableTypes()[0].getTableName();
 ResultSet rs = Database.Query(query);
 int bookId = rs.getInt("TABLE_NAME");
 rs.next();
 query = rs.getString("TABLE_COMMENTS");
 int chapterId = Integer.parseInt(query.split(" ")[1].split("=")[1].substring(1));
 int verseId = 1;
 int translationId = 1;
 List<Verse> verses = Verse.Get(new Translation[1], new Reference(bookId, chapterId, verseId), new Reference(bookId, chapterId, verseId + 1));
 assertEquals(2, verses.size());
 }

 @Test(expected = SQLException.class)
 public void testGetWithInvalidBeginEndReference() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[1], new Reference(1, 1, 1), new Reference(1, 1, 2));
 fail("Should have thrown SQLException");
 }

 @Test
 public void testGetIdStatic() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(1, "Genesis", 1, 1, 1), 1);
 assertEquals(id, 1);
 }

 @Test
 public void testGetIdWithInvalidTranslation() {
 int id = Verse.GetId(-1, new BibleReference(1, "Genesis", 1, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidBookReference() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(-1, "Genesis", 1, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidChapterVerse() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(1, "Genesis", -1, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidVerse() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(1, "Genesis", 1, -1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidTranslationId() {
 int id = Verse.GetId(-1, new BibleReference(1, "Genesis", 1, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidBookId() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(0, "Genesis", 1, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidChapter() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(1, "Genesis", 0, 1, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testGetIdWithInvalidVerse() {
 int id = Verse.GetId(new Translation(1, "English"), new BibleReference(1, "Genesis", 1, 0, 1), 1);
 assertEquals(id, -1);
 }

 @Test
 public void testNewWithDatabase() throws SQLException {
 Translation translation = new Translation(1, "English");
 BibleReference reference = new BibleReference(1, "Genesis", 1, 1, 1);
 Verse verse = new Verse(translation, reference, "This is a test verse.");
 int id = verse.getId();
 assertEquals(id, verse.getId());
 }

 @Test
 public void testGetWithDatabase() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");
 List<Verse> verses = Verse.Get(translations, new BibleReference(1, "Genesis", 1, 1, 1), new BibleReference(1, "Genesis", 1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetWithEmptyDatabaseResult() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");
 List<Verse> verses = Verse.Get(translations, new BibleReference(1, "Genesis", 1, 1, 1), new BibleReference(1, "Genesis", 1, 1, 2));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithInvalidDatabaseResult() throws SQLException {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1, "English");
 List<Verse> verses = Verse.Get(translations, new BibleReference(1, "Genesis", 1, 1, 1), new BibleReference(1, "Genesis", 1, 2, 1));
 fail("Should have thrown SQLException");
 }
}