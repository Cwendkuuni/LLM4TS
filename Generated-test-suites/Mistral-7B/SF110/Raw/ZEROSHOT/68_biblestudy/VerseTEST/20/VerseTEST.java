package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

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

	private static DbResult result;
	private static Database db;
	private static Verse verse;
	private static List<Translation> translations;
	private static Translation translation1;
	private static Translation translation2;
	private static int id1;
	private static int id2;
	private static Reference reference1;
	private static Reference reference2;
	private static int bookId = 1;
	private static int chapterId = 1;
	private static int verseId = 1;
	private static int verseId2 = 2;

	@BeforeClass
	public static void setup() throws SQLException, ClassNotFoundException {
		db = new Database();
		db.connect();

		translations = new ArrayList<Translation>();
		translation1 = new Translation(1, "Test Translation 1");
		translation2 = new Translation(2, "Test Translation 2");
		translations.add(translation1);
		translations.add(translation2);

		reference1 = new Reference(bookId, chapterId, verseId);
		reference2 = new Reference(bookId, chapterId, verseId2);
	}

	@AfterClass
	public static void teardown() throws SQLException {
		db.disconnect();
	}

	@Test
	public void testNew() {
		Verse verse1 = Verse.New(id1);
		assertNotNull(verse1);
	}

	@Test
	public void testGetId() {
		id1 = Verse.GetId(translation1, new Book(bookId), chapterId, verseId);
		assertEquals(id1, Verse.GetId(translation1.getId(), bookId, chapterId, verseId));
	}

	@Test
	public void testGet() {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), new Book(bookId), chapterId, verseId);
		assertEquals(2, verses.size());
		assertEquals(verses.get(0).getId(), id1);
		assertEquals(verses.get(1).getId(), Verse.GetId(translation2, new Book(bookId), chapterId, verseId));
	}

	@Test
	public void testGet_withValidReference() {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, reference1, reference1);
		assertEquals(2, verses.size());
		assertEquals(verses.get(0).getId(), id1);
		assertEquals(verses.get(1).getId(), id2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGet_withInvalidReference() {
		List<Verse> verses = Verse.Get(translations.toArray(new Translation[0]), null, reference1, reference2);
		fail();
	}

	@Test
	public void testGetId_withValidValues() {
		id1 = Verse.GetId(translation1.getId(), bookId, chapterId, verseId);
		assertEquals(id1, Verse.GetId(translation1.getId(), bookId, chapterId, verseId));
	}

	@Test
	public void testGetId_withInvalidTranslationId() {
		try {
			Verse.GetId(-1, bookId, chapterId, verseId);
			fail();
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testGetId_withInvalidBookId() {
		try {
			Verse.GetId(translation1.getId(), -1, chapterId, verseId);
			fail();
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testGetId_withInvalidChapterId() {
		try {
			Verse.GetId(translation1.getId(), bookId, -1, verseId);
			fail();
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testGetId_withInvalidVerseId() {
		try {
			Verse.GetId(translation1.getId(), bookId, chapterId, -1);
			fail();
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testNewWithId() {
		verse = Verse.New(id1);
		assertEquals(id1, verse.getId());
	}

	@Test
	public void testNewWithDbResult() throws SQLException {
		result = db.Query("SELECT * FROM Verse WHERE Verse_ID = " + id1);
		assertNotNull(result);
		result.next();
		verse = new Verse(result);
		assertEquals(id1, verse.getId());
		assertEquals(verse.getText(), "Test Text 1"); // Assuming this is the text for id1
		assertEquals(verse.getTranslationReference().getTranslation().getId(), translation1.getId());
		assertEquals(verse.getTranslationReference().getReference().getBookId(), bookId);
		assertEquals(verse.getTranslationReference().getReference().getChapter(), chapterId);
		assertEquals(verse.getTranslationReference().getReference().getVerseNo(), verseId);
	}

	@Test
	public void testReadId() throws SQLException {
		id1 = Verse.ReadId(translation1.getId(), bookId, chapterId, verseId);
		assertEquals(id1, Verse.GetId(translation1.getId(), bookId, chapterId, verseId));
	}

	@Test
	public void testReadId_withInvalidTranslationId() {
		try {
			Verse.ReadId(-1, bookId, chapterId, verseId);
			fail();
		} catch (SQLException e) {
			// Expected
		}
	}

	@Test
	public void testReadId_withInvalidBookId() {
		try {
			Verse.ReadId(translation1.getId(), -1, chapterId, verseId);
			fail();
		} catch (SQLException e) {
			// Expected
		}
	}

	@Test
	public void testReadId_withInvalidChapterId() {
		try {
			Verse.ReadId(translation1.getId(), bookId, -1, verseId);
			fail();
		} catch (SQLException e) {
			// Expected
		}
	}

	@Test
	public void testReadId_withInvalidVerseId() {
		try {
			Verse.ReadId(translation1.getId(), bookId, chapterId, -1);
			fail();
		} catch (SQLException e) {
			// Expected
		}
	}
}