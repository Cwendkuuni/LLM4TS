package bible.obj.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bible.obj.DbResult;
import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.Database;
import bible.util.Util;

public class VerseTest {

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
		int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
		assertEquals(id, 1);
	}

	@Test
	public void testGet() {
		Translation[] translations = new Translation[1];
		Translation translation = new Translation(1, "Test Translation");
		translations[0] = translation;
		List<Verse> verses = Verse.Get(translations, new Book(1, "Test Book"), new Reference(1, 1, 1), new Reference(1, 1, 1));
		assertEquals(verses.size(), 1);
		Verse verse = verses.get(0);
		assertEquals(verse.getId(), 1);
		assertEquals(verse.getText(), "Test Verse Text");
	}

	@Test(expected = NullPointerException.class)
	public void testGetNullTranslations() {
		Verse.Get(null, new Book(1, "Test Book"), new Reference(1, 1, 1), new Reference(1, 1, 1));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetIdInvalidId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 100;
		int id = Verse.GetId(translationId, new Book(bookId), chapterId, verseId);
		fail("Expected IndexOutOfBoundsException");
	}

	@Test(expected = SQLException.class)
	public void testGetSqlException() {
		String query = "select Verse_ID from Verse where Verse_ID = -1";
		DbResult result = Database.Query(query);
		Verse.GetId(-1, new Translation(1, "Test Translation"), 1, 1);
	}

	@Test
	public void testGetWithNullBookReference() {
		Translation[] translations = new Translation[1];
		Translation translation = new Translation(1, "Test Translation");
		translations[0] = translation;
		List<Verse> verses = Verse.Get(translations, null, new Reference(1, 1, 1), new Reference(1, 1, 1));
		assertEquals(verses.size(), 0);
	}

	@Test
	public void testGetWithInvalidBookReference() {
		Translation[] translations = new Translation[1];
		Translation translation = new Translation(1, "Test Translation");
		translations[0] = translation;
		List<Verse> verses = Verse.Get(translations, new Reference(-1, 1, 1), new Reference(1, 1, 1), new Reference(1, 1, 1));
		assertEquals(verses.size(), 0);
	}

	@Test
	public void testNewWithId() {
		Verse verse = new Verse(1);
		assertNotNull(verse);
	}

	@Test(expected = SQLException.class)
	public void testNewWithInvalidId() {
		Verse verse = new Verse(-1);
	}

	@Test
	public void testComplete() throws SQLException {
		DbResult result = null;
		result = Database.Query("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1");
		Verse verse = new Verse(result);
		assertEquals(verse.getId(), 1);
		assertEquals(verse.getText(), "Test Verse Text");
		assertEquals(verse.getTranslationReference().getTranslation().getId(), 1);
		assertEquals(verse.getTranslationReference().getReference().getBookId(), 1);
		assertEquals(verse.getTranslationReference().getReference().getChapter(), 1);
		assertEquals(verse.getTranslationReference().getReference().getVerseNo(), 1);
	}

	@Test
	public void testGetIdWithNullTranslation() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 1;
		assertEquals(Verse.GetId(null, new Book(bookId), chapterId, verseId), -1);
	}

	@Test
	public void testGetIdWithNullBook() {
		int translationId = 1;
		int bookId = 0;
		int chapterId = 1;
		int verseId = 1;
		assertEquals(Verse.GetId(translationId, null, chapterId, verseId), -1);
	}

	@Test
	public void testGetIdWithNullChapter() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 0;
		int verseId = 1;
		assertEquals(Verse.GetId(translationId, new Book(bookId), null, verseId), -1);
	}

	@Test
	public void testGetIdWithNullVerseId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 0;
		assertEquals(Verse.GetId(translationId, new Book(bookId), chapterId, verseId), -1);
	}

	@Test
	public void testGetIdWithInvalidVerseId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 100;
		assertEquals(Verse.GetId(translationId, new Book(bookId), chapterId, verseId), -1);
	}

	@Test
	public void testReadIdWithNullTranslationId() {
		int bookId = 1;
		int chapterId = 1;
		int verseId = 1;
		assertEquals(Verse.ReadId(-1, bookId, chapterId, verseId), -1);
	}

	@Test
	public void testReadIdWithNullBookId() {
		int translationId = 1;
		int bookId = 0;
		int chapterId = 1;
		int verseId = 1;
		assertEquals(Verse.ReadId(translationId, bookId, chapterId, verseId), -1);
	}

	@Test
	public void testReadIdWithNullChapterId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 0;
		int verseId = 1;
		assertEquals(Verse.ReadId(translationId, bookId, chapterId, verseId), -1);
	}

	@Test
	public void testReadIdWithNullVerseId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 0;
		assertEquals(Verse.ReadId(translationId, bookId, chapterId, verseId), -1);
	}

	@Test
	public void testReadIdWithInvalidVerseId() {
		int translationId = 1;
		int bookId = 1;
		int chapterId = 1;
		int verseId = 100;
		assertEquals(Verse.ReadId(translationId, bookId, chapterId, verseId), -1);
	}
}