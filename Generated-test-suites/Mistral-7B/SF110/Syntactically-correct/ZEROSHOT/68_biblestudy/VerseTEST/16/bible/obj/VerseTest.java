package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.DbResult;

public class VerseTest {

 private static final String DB_URL = "jdbc:derby:test;create=true";

 @Before
 public void setUp() throws Exception {
 Database.connect(DB_URL);
 }

 @After
 public void tearDown() throws Exception {
 Database.disconnect();
 }

 @Test
 public void testNew() {
 Verse verse = Verse.New(1);
 assertNotNull(verse);
 assertEquals(1, verse.getId());
 }

 @Test
 public void testGetId() {
 Verse verse = new Verse(1);
 int id = Verse.GetId(null, null, 1, 1);
 assertEquals(1, id);

 Translation translation = new Translation(1);
 id = Verse.GetId(translation, null, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGet() throws SQLException {
 List<Translation> translations = new ArrayList<>();
 translations.add(new Translation(1));
 translations.add(new Translation(2));

 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, null, null);
 assertEquals(2, verses.size());

 for (Verse verse : verses) {
 assertEquals(1, verse.getId());
 }
 }

 @Test
 public void testGetIdWithTranslationAndBook() throws SQLException {
 Translation translation = new Translation(1);
 Book book = new Book(1, "Genesis", 1);
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(1, id);
 }

 @Test
 public void testGetIdWithTranslationAndInvalidBook() throws SQLException {
 Translation translation = new Translation(1);
 Book book = new Book(2, "Genesis", 1);
 int id = Verse.GetId(translation, book, 1, 1);
 assertEquals(-1, id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullTranslation() throws SQLException {
 int id = Verse.GetId(null, null, 1, 1);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullBook() throws SQLException {
 Translation translation = new Translation(1);
 int id = Verse.GetId(translation, null, 1, 1);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testGet() throws SQLException {
 List<Translation> translations = new ArrayList<>();
 translations.add(new Translation(1));
 translations.add(new Translation(2));

 List<Reference> references = new ArrayList<>();
 references.add(new Reference(1, "Genesis", 1, 1));
 references.add(new Reference(1, "Genesis", 1, 2));

 List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, references.get(0), references.get(1));
 assertEquals(2, verses.size());

 for (Verse verse : verses) {
 assertEquals(1, verse.getId());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullTranslations() throws SQLException {
 List<Verse> verses = Verse.Get(null, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullBegin() throws SQLException {
 List<Verse> verses = Verse.Get(null, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullEnd() throws SQLException {
 List<Verse> verses = Verse.Get(null, null, null, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 null null[1 Genesis 1 1]]", verse.toString());
 }
}