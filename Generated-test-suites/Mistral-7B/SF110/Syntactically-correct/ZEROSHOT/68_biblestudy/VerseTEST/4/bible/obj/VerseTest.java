package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.*;
import bible.util.*;

public class VerseTest {

 private static Connection connection;
 static {
 try {
 Class.forName("com.mysql.cj.jdbc.Driver");
 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "password");
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @BeforeClass
 public static void setUpBeforeClass() throws Exception {
 // Initialize database here
 }

 @AfterClass
 public static void tearDownAfterClass() throws Exception {
 // Clean up database here
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() {
 Translation[] translations = { new Translation(1, "English"), new Translation(2, "French") };
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 assertEquals(2, verses.size());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 Verse verse = new Verse(1);
 int id = Verse.GetId(verse.getTranslationReference().getTranslation().getId(), verse.getBook().getId(), verse.getChapter().getChapterNo(), verse.getChapter().getVerseNo());
 assertEquals(verse.getId(), id);
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 Translation translation = new Translation(1, "English");
 Book book = new Book(1, "Genesis", 1);
 Reference reference = new Reference(1, 1, 1);
 Verse verse = Verse.New(Verse.GetId(translation.getId(), book.getId(), reference.getChapterNo(), reference.getVerseNo()));
 assertEquals(verse.getText(), "Expected text"); // Replace with actual text from database
 }

 @Test
 public void testGetFromDatabaseWithMultipleTranslations() throws SQLException {
 Translation[] translations = { new Translation(1, "English"), new Translation(2, "French") };
 ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 2));
 for (Object obj : verses) {
 Verse verse = (Verse) obj;
 assertEquals(expectedEnglishText, verse.getText()); // Replace with actual English text from database
 assertEquals(expectedFrenchText, verse.getTranslationReference().getTranslation().getText() + " " + verse.getTranslationReference().getReference().toString()); // Replace with actual French text and reference from database
 }
 }
}