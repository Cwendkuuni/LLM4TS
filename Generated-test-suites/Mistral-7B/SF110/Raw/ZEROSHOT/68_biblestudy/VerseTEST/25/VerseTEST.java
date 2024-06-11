package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.DbResult;
import bible.util.Util;

public class VerseTest {

	private static Database database;
	private static List<Translation> translations;
	private static Reference begin, end;

	@BeforeClass
	public static void setUp() throws SQLException, ClassNotFoundException {
		database = new Database();
		database.connect();
		translations = new ArrayList<>();
		translations.add(new Translation(1, "English", "EN"));
		translations.add(new Translation(2, "Spanish", "ES"));
		begin = new Reference(1, 1, 1);
		end = new Reference(1, 1, 11);
	}

	@Test
	public void testNew() {
		Verse verse1 = Verse.New(1);
		assertNotNull(verse1);
		assertEquals(1, verse1.getId());
		assertEquals("", verse1.getText());
		assertNull(verse1.getTranslationReference());

		Verse verse2 = Verse.New(1);
		assertSame(verse1, verse2);
	}

	@Test
	public void testGetId() {
		int id = Verse.GetId(translations.get(0), new Book(1, "Test Book"), 1, 1);
		assertEquals(1, id);

		id = Verse.GetId(translations.get(1), new Book(1, "Test Book"), 1, 1);
		assertEquals(-1, id);
	}

	@Test
	public void testGet() {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Book(1, "Test Book", "Test Book"), begin, end);
		assertEquals(11, verses.size());

		for (Verse verse : verses) {
			assertNotNull(verse);
			assertEquals(1, verse.getId());
			assertEquals(translations.get(0), verse.getTranslationReference().getTranslation());
			assertEquals(begin, verse.getReference());
			assertEquals("", verse.getText());
		}
	}

	@Test
	public void testGetIdStatic() {
		int id = Verse.GetId(1, new Book(1, "Test Book", "Test Book"), 1, 1);
		assertEquals(1, id);
	}

	@Test
	public void testGet() throws SQLException {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, begin, end);
		assertEquals(11, verses.size());

		for (Verse verse : verses) {
			assertEquals(1, verse.getId());
			assertEquals(translations.get(0), verse.getTranslationReference().getTranslation());
			assertEquals(begin, verse.getReference());
			assertEquals("", verse.getText());
		}
	}

	@Test(expected = SQLException.class)
	public void testGetWithInvalidQuery() throws SQLException {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, begin, null);
	}

	@Test
	public void testNewWithDatabase() throws SQLException {
		Verse verse = new Verse(1);
		assertEquals(1, verse.getId());
		assertEquals("", verse.getText());
		assertNull(verse.getTranslationReference());

		DbResult result = database.Query("select * from Verse where Verse_ID = 1");
		assertTrue(result.next());
		Verse verseFromDb = new Verse(result);
		assertEquals(1, verseFromDb.getId());
		assertEquals("", verseFromDb.getText());
		assertNull(verseFromDb.getTranslationReference());

		assertEquals(verse, verseFromDb);
	}

	@Test
	public void testComplete() throws SQLException {
		DbResult result = database.Query("select * from Verse where Verse_ID = 1");
		assertTrue(result.next());
		Verse verseFromDb = new Verse(result);

		Verse verse = new Verse(1);
		assertEquals(1, verse.getId());
		assertEquals("", verse.getText());
		assertNull(verse.getTranslationReference());

		verse.complete(result);

		assertEquals(1, verse.getId());
		assertEquals(Util.toString(translations.get(0)), verse.getTranslationReference().getTranslation().getName());
		assertEquals(begin, verse.getReference());
		assertEquals("", verse.getText());

		assertEquals(verseFromDb, verse);
	}
}