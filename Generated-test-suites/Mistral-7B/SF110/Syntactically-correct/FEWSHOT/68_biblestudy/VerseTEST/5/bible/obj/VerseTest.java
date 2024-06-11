package bible.obj;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DbResult;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VerseTest {

    private Connection connection;
    private Statement statement;
    private DbResult dbResult;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        connection = DriverManager.getConnection("jdbc:mock:jdbc:mysql", "", "");
        statement = connection.createStatement();
        dbResult = mock(DbResult.class);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewNullId() {
        Verse.New(null);
    }

    @Test
    public void testGetIdTranslationNullBookNullChapterNullVerseNull() {
        assertEquals(-1, Verse.GetId(null, null, 1, 1));
    }

    @Test
    public void testGetIdTranslationNullBookNullChapterNullVerse() {
        assertEquals(-1, Verse.GetId(-1, null, null, null));
    }

    @Test
    public void testGetIdTranslationNullBookChapterNullVerse() {
        assertEquals(-1, Verse.GetId(-1, null, 1, null));
    }

    @Test
    public void testGetIdTranslationNullBookChapter() {
        assertEquals(-1, Verse.GetId(-1, new Reference(1, 1, 1), null));
    }

    @Test
    public void testGetIdTranslationNullBook() {
        assertEquals(-1, Verse.GetId(-1, null, null, null));
    }

    @Test
    public void testGetIdTranslationValid() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        when(dbResult.next()).thenReturn(true).thenReturn(false);
        when(dbResult.getInt("Verse_ID")).thenReturn(1);
        when(statement.executeQuery(anyString())).thenReturn(dbResult);

        int id = Verse.GetId(translationId, new Reference(bookId, chapterId, verseId), null);
        assertEquals(1, id);

        verify(statement, times(1)).executeQuery("select Verse_ID from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId);
    }

    @Test
    public void testGetIdTranslationValidNoResult() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        when(dbResult.next()).thenReturn(false);

        int id = Verse.GetId(translationId, new Reference(bookId, chapterId, verseId), null);
        assertEquals(-1, id);

        verify(statement, times(1)).executeQuery("select Verse_ID from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId);
    }

    @Test
    public void testGetIdTranslationInvalidId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        when(dbResult.next()).thenReturn(true).thenReturn(false);
        when(dbResult.getInt("Verse_ID")).thenReturn(-1);

        int id = Verse.GetId(translationId, new Reference(bookId, chapterId, verseId), null);
        assertEquals(-1, id);

        verify(statement, times(1)).executeQuery("select Verse_ID from Verse where Translation_ID = " + translationId + " and Book_ID = " + bookId + " and Chapter = " + chapterId + " and Verse_No = " + verseId);
    }

    @Test
    public void testGet() {
        int translationId = 1;
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        Verse[] verses = new Verse[2];
        when(dbResult.next()).thenReturn(true).thenReturn(false);
        when(dbResult.getInt("Verse_ID")).thenAnswer(i -> {
            int index = i.getIntValue() - 1;
            return verses[index];
        });
        when(statement.executeQuery(anyString())).thenReturn(dbResult);

        ArrayList<Verse> verses = Verse.Get(new Verse[1], begin, end);
        assertEquals(2, verses.size());
        assertEquals(verses[0], verses.get(0));
        assertEquals(verses[1], verses.get(1));

        verify(statement, times(1)).executeQuery("select Verse_ID from Verse where Translation_ID in (" + Util.ToString(Util.ToIntArray(new Verse[1])) + ") and Book_ID >= " + begin.getBookId() + " and Book_ID <= " + end.getBookId() + " and Chapter >= " + begin.getChapterNo() + " and Chapter <= " + end.getChapterNo() + " and Verse_No >= " + begin.getVerseNo() + " and Verse_No <= " + end.getVerseNo());
    }

    @Test
    public void testGetEmpty() {
        int translationId = 1;
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        when(dbResult.next()).thenReturn(false);

        ArrayList<Verse> verses = Verse.Get(new Verse[1], begin, end);
        assertEquals(0, verses.size());

        verify(statement, times(1)).executeQuery("select Verse_ID from Verse where Translation_ID in (" + Util.ToString(Util.ToIntArray(new Verse[1])) + ") and Book_ID >= " + begin.getBookId() + " and Book_ID <= " + end.getBookId() + " and Chapter >= " + begin.getChapterNo() + " and Chapter <= " + end.getChapterNo() + " and Verse_No >= " + begin.getVerseNo() + " and Verse_No <= " + end.getVerseNo());
    }
}