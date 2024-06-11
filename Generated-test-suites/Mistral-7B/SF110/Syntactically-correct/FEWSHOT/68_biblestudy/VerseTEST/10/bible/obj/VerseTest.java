package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

 private static final String DB_URL = "jdbc:h2:mem:testdb";
 private static final String DB_USER = "sa";
 private static final String DB_PASS = "";

 @Before
 public void setup() throws SQLException, ClassNotFoundException {
 Class.forName("org.h2.Driver");
 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
 Statement statement = connection.createStatement()) {
 statement.execute("create table if not exists Verse (" +
 "Verse_ID int primary key," +
 "Text varchar(100)," +
 "Translation_ID int," +
 "Book_ID int," +
 "Chapter int," +
 "Verse_No int) ");
 }
 }

 @After
 public void teardown() throws SQLException {
 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
 Statement statement = connection.createStatement()) {
 statement.execute("drop table Verse");
 }
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.getId(translationId, bookId, chapterId, verseId);
 assertEquals(id, Verse.GetId(new Translation[] { new Translation(translationId) }, bookId, chapterId, verseId).get(0).getId());
 }

 @Test
 public void testGet() {
 Translation translation = new Translation(1);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, 1, 1, 1);
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGet_NoTranslations() {
 List<Verse> verses = Verse.Get(new Translation[0], 1, 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyTranslations() {
 List<Verse> verses = Verse.Get(new Translation[0], 1, 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_MultipleVerses() {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, 1, 1, 1);
 assertEquals(2, verses.size());
 }

 @Test
 public void testGet_MultipleVerses_SameTranslation() {
 Translation translation = new Translation(1);
 List<Verse> verses = Verse.Get(new Translation[] { translation, translation }, 1, 1, 1);
 assertEquals(1, verses.size());
 }

 @Test
 public void testGet_MultipleVerses_DifferentVerses() {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 List<Verse> verses1 = Verse.Get(new Translation[] { translation1 }, 1, 1, 1);
 List<Verse> verses2 = Verse.Get(new Translation[] { translation2 }, 1, 1, 1);
 assertEquals(1, verses1.size());
 assertEquals(1, verses2.size());
 assertNotSame(verses1.get(0), verses2.get(0));
 }

 @Test
 public void testGet_InvalidChapter() {
 List<Verse> verses = Verse.Get(new Translation[0], 1, -1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidVerse() {
 Translation translation = new Translation(1);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, 1, 1, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidTranslation() {
 List<Verse> verses = Verse.Get(new Translation[0], 1, 1, 1, -1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_InvalidTranslationFormat() {
 List<Verse> verses = Verse.Get((Object[]) null, 1, 1, 1, 1);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_EmptyResult() throws SQLException {
 List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGet_SingleResult() throws SQLException {
 Translation translation = new Translation(1);
 int id = Verse.getId(translation.getId(), 1, 1, 1);
 Verse verse = new Verse(id);
 List<Verse> verses = Verse.Get(new Translation[] { translation }, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(verse, verses.get(0));
 }

 @Test
 public void testGet_MultipleResults() throws SQLException {
 Translation translation1 = new Translation(1);
 Translation translation2 = new Translation(2);
 int id1 = Verse.getId(translation1.getId(), 1, 1, 1);
 int id2 = Verse.getId(translation2.getId(), 1, 1, 1);
 Verse verse1 = new Verse(id1);
 Verse verse2 = new Verse(id2);
 List<Verse> verses1 = Verse.Get(new Translation[] { translation1 }, new Reference(1, 1, 1), new Reference(1, 1, 1));
 List<Verse> verses2 = Verse.Get(new Translation[] { translation2 }, new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(2, verses1.size());
 assertEquals(2, verses2.size());
 List<Verse> expectedVerses = Arrays.asList(verse1, verse2);
 assertEquals(expectedVerses, verses1);
 assertEquals(expectedVerses, verses2);
 }
}