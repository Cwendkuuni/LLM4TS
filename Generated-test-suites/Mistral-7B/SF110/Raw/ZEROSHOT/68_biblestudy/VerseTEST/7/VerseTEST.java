package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
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

	private static Database db;
	private static Verse verse1;
	private static Verse verse2;
	private static List<Translation> translations;
	private static Reference ref1;
	private static Reference ref2;
	private static TranslationReference translationRef1;
	private static TranslationReference translationRef2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		db = new Database();
		db.connect();
		// Create translations, references, and translation references
		Translation t1 = new Translation(1, "Test Translation 1");
		Translation t2 = new Translation(2, "Test Translation 2");
		translations = Arrays.asList(t1, t2);
		ref1 = new Reference(1, 1, 1);
		ref2 = new Reference(1, 1, 2);
		translationRef1 = new TranslationReference(t1, ref1);
		translationRef2 = new TranslationReference(t2, ref2);

		// Insert translation references and verses into the database
		db.startTransaction();
		db.executeUpdate("INSERT INTO TranslationReference VALUES (?, ?)", new Object[] { translationRef1.getId(), verse1.getId() });
		db.executeUpdate("INSERT INTO TranslationReference VALUES (?, ?)", new Object[] { translationRef2.getId(), verse2.getId() });
		db.executeUpdate("INSERT INTO Verse VALUES (?, ?, ?, ?, ?, ?)", new Object[] { verse1.getId(), verse1.getText(), translationRef1.getId(), 1, 1, 1 });
		db.executeUpdate("INSERT INTO Verse VALUES (?, ?, ?, ?, ?, ?)", new Object[] { verse2.getId(), verse2.getText(), translationRef2.getId(), 1, 1, 2 });
		db.commit();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		db.disconnect();
	}

	@Test
	public void testNew() {
		verse1 = Verse.New(verse1.getId());
		assertEquals(verse1.getId(), 1);
		assertEquals(verse1.getText(), "Test Text 1");
		assertEquals(verse1.getTranslationReference(), translationRef1);
		assertEquals(verse1.toString(), "bible.obj.Verse:[1 Test Translation 1 Test Text 1]");
	}

	@Test
	public void testGetId() {
		assertEquals(Verse.GetId(translations.get(0), new Reference(1, 1, 1), 1, 1), 1);
		assertEquals(Verse.GetId(translations.get(1), new Reference(1, 1, 1), 1, 2), 2);
	}

	@Test
	public void testGet() {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(1, 1, 1), new Reference(1, 1, 2));
		assertEquals(verses.size(), 2);
		assertEquals(verses.get(0).getId(), 1);
		assertEquals(verses.get(0).getText(), "Test Text 1");
		assertEquals(verses.get(0).getTranslationReference(), translationRef1);
		assertEquals(verses.get(1).getId(), 2);
		assertEquals(verses.get(1).getText(), "Test Text 2");
		assertEquals(verses.get(1).getTranslationReference(), translationRef2);
	}

	@Test
	public void testGetIdFromDatabase() {
		assertEquals(Verse.GetId(1, 1, 1, 1), 1);
		assertEquals(Verse.GetId(1, 1, 1, 2), 2);
	}

	@Test
	public void testGetFromDatabase() throws SQLException {
		DbResult result = null;
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Reference(1, 1, 1), new Reference(1, 1, 2));
		assertEquals(verses.size(), 2);
		// Test getting verse 1 from the database
		result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = ?", new Object[] { verse1.getId() });
		assertTrue(result.next());
		Verse dbVerse1 = new Verse(result);
		assertEquals(dbVerse1.getText(), "Test Text 1");
		assertEquals(dbVerse1.getTranslationReference(), translationRef1);
		DbResult.Close(result);
		// Test getting verse 2 from the database
		result = Database.Query("SELECT * FROM Verse WHERE Verse_ID = ?", new Object[] { verse2.getId() });
		assertTrue(result.next());
		Verse dbVerse2 = new Verse(result);
		assertEquals(dbVerse2.getText(), "Test Text 2");
		assertEquals(dbVerse2.getTranslationReference(), translationRef2);
		DbResult.Close(result);
	}

	@Test
	public void testToString() {
		assertEquals(verse1.toString(), "bible.obj.Verse:[1 Test Translation 1 Test Text 1]");
		assertEquals(verse2.toString(), "bible.obj.Verse:[2 Test Translation 2 Test Text 2]");
	}
}