package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @Before
    public void setup() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mock:jdbc:mysql", null, null);
        statement = connection.createStatement();
        resultSet = mock(ResultSet.class);
    }

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
        int expectedId = 1;

        when(resultSet.getInt("Verse_ID")).thenReturn(expectedId);
        when(resultSet.next()).thenReturn(true, false);

        Verse verse = new Verse(new DbResult() {
            {
                setResultSet(resultSet);
            }
        });

        assertEquals(expectedId, verse.getId());
    }

    @Test
    public void testGet() {
        Translation translation = mock(Translation.class);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        when(Translation.Get(anyInt())).thenReturn(translation);

        Verse verse1 = new Verse(1);
        Verse verse2 = new Verse(2);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, begin, end);

        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));
    }

    @Test
    public void testGetIdFromDatabase() throws SQLException {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;

        when(resultSet.getInt("Verse_ID")).thenReturn(expectedId);
        when(resultSet.next()).thenReturn(true, false);

        int id = Verse.getId(translation, new Book(bookId), chapterId, verseId);

        assertEquals(expectedId, id);
    }

    @Test
    public void testGetVersesFromDatabase() throws SQLException {
        Translation[] translations = new Translation[] { mock(Translation.class) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        when(Database.Query(anyString())).thenReturn(new DbResult() {
            {
                setResultSet(resultSet);
            }
        });

        List<Verse> verses = Verse.Get(translations, begin, end);

        verify(Database, times(1)).Query(eq("select Verse_ID from Verse where Translation_ID in (?) and Book_ID >= " + begin.getBookId() + " and Book_ID <= " + end.getBookId() + " and Chapter >= " + begin.getChapterNo() + " and Chapter <= " + end.getChapterNo() + " and Verse_No >= " + begin.getVerseNo() + " and Verse_No <= " + end.getVerseNo()));
    }

    @Test
    public void testGetIdWithMissingTranslation() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.getId(translationId, bookId, chapterId, verseId);

        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithMissingChapter() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.getId(translationId, bookId, chapterId, verseId);

        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithMissingVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.getId(translationId, bookId, chapterId, verseId);

        assertEquals(-1, id);
    }

    @Test
    public void testGetVersesFromDatabaseWithMissingTranslation() throws SQLException {
        Translation[] translations = new Translation[] { mock(Translation.class) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);

        when(Database.Query(anyString())).thenReturn(new DbResult() {
            {
                setResultSet(resultSet);
            }
        });

        List<Verse> verses = Verse.Get(translations, begin, end);

        verify(Database, never()).Query(eq("select Verse_ID from Verse where Translation_ID in (?) and Book_ID >= " + begin.getBookId() + " and Book_ID <= " + end.getBookId() + " and Chapter >= " + begin.getChapterNo() + " and Chapter <= " + end.getChapterNo() + " and Verse_No >= " + begin.getVerseNo() + " and Verse_No <= " + end.getVerseNo()));
    }
}