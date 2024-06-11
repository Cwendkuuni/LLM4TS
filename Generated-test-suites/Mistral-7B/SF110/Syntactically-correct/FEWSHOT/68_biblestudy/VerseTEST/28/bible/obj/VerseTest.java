package bible.obj;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class VerseTest {

 private Connection connection;
 private Statement statement;
 private ResultSet resultSet;

 @Before
 public void setup() throws Exception {
 MockitoAnnotations.initMocks(this);
 connection = DriverManager.getConnection("jdbc:mock:jdbc:Derby", null);
 statement = connection.createStatement();
 }

 @After
 public void teardown() throws Exception {
 if (resultSet != null) {
 resultSet.close();
 }
 if (statement != null) {
 statement.close();
 }
 if (connection != null) {
 connection.close();
 }
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertEquals(1, verse.getId());
 assertNull(verse.getTranslationReference());
 assertNull(verse.getText());
 }

 @Test
 public void testGetId() {
 int translationId = 1;
 int bookId = 1;
 int chapterId = 1;
 int verseId = 1;
 int id = Verse.getId(translationId, bookId, chapterId, verseId);
 assertEquals(id, Verse.GetId(new Translation(translationId), new Book(bookId), chapterId, verseId));
 }

 @Test
 public void testGet() {
 Translation[] translations = new Translation[1];
 translations[0] = new Translation(1);
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(1, verses.size());
 assertEquals(1, verses.get(0).getId());
 }

 @Test
 public void testGetWithNullTranslations() {
 List<Verse> verses = Verse.Get(null, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithEmptyTranslations() {
 Translation[] translations = new Translation[0];
 List<Verse> verses = Verse.Get(translations, new Book(1, "Genesis"), new Reference(1, 1, 1), new Reference(1, 1, 1));
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetFromDatabase() throws SQLException {
 when(statement.executeQuery(ArgumentMatchers.anyString())).thenReturn(resultSet);
 resultSet.when(ResultSet.class).next().thenReturn(true);
 resultSet.when(ResultSet.class).getInt("Verse_ID").thenReturn(1);
 resultSet.when(ResultSet.class).getString("Text").thenReturn("The Lord is my shepherd");
 resultSet.when(ResultSet.class).getInt("Translation_ID").thenReturn(1);
 resultSet.when(ResultSet.class).getInt("Book_ID").thenReturn(1);
 resultSet.when(ResultSet.class).getInt("Chapter").thenReturn(1);
 resultSet.when(ResultSet.class).getInt("Verse_No").thenReturn(1);

 Verse verse = Verse.New(1);
 assertEquals(1, verse.getId());
 assertNull(verse.getTranslationReference());
 assertNull(verse.getText());

 verse = Verse.New(1, statement);
 assertEquals(1, verse.getId());
 assertEquals("The Lord is my shepherd", verse.getText());
 assertEquals(new Translation(1), verse.getTranslationReference().getTranslation());
 assertEquals(new Reference(1, 1, 1), verse.getReference());
 }

 @Test
 public void testGetIdFromDatabase() throws SQLException {
 when(statement.executeQuery(ArgumentMatchers.anyString())).thenReturn(resultSet);
 resultSet.when(ResultSet.class).next().thenReturn(true);
 resultSet.when(ResultSet.class).getInt("Verse_ID").thenReturn(1);

 int id = Verse.GetId(1, 1, 1, 1, statement);
 assertEquals(1, id);
 }

 @Test(expected = SQLException.class)
 public void testGetIdFromDatabaseWithException() throws SQLException {
 when(statement.executeQuery(ArgumentMatchers.anyString())).thenThrow(SQLException.class);
 Verse.GetId(1, 1, 1, 1, statement);
 }
}