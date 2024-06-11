package bible.obj;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VerseTest {

    private static final String DB_URL = "jdbc:mock:jdbc:mysql";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private Connection connection;
    private Database database;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        database = new Database(connection);
    }

    @After
    public void teardown() throws SQLException {
        Database.Close(connection);
    }

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
        assertNull(verse.getText());
        assertNull(verse.getTranslationReference());
    }

    @Test
    public void testGet() throws SQLException {
        when(database.Query(eq("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1")))
                .thenReturn(getMockResultWithText("Text1", "1", "1", "1", "1", "1"));

        Verse verse = Verse.New(1);
        assertEquals("Text1", verse.getText());
        assertEquals(new TranslationReference(new Translation(1), new Reference(1, 1, 1)), verse.getTranslationReference());

        verify(database).Query(eq("select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1"));
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(new Translation(1), new Book(1), 1, 1);
        assertEquals(1, id);

        verify(database, never()).Query(anyString());
    }

    @Test
    public void testGetIdWithNullTranslation() {
        int id = Verse.GetId((Translation) null, new Book(1), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetIdWithNullBook() {
        int id = Verse.GetId(new Translation(1), (Book) null, 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGet() throws SQLException {
        when(database.Query(eq("select Verse_ID from Verse where Translation_ID in (1, 2, 3) and Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 1 and Verse_No >= 1 and Verse_No <= 1")))
                .thenReturn(getMockResultWithIds(1, 2, 3));

        List<Verse> verses = Verse.Get(new Translation[]{new Translation(1), new Translation(2), new Translation(3)}, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(3, verses.size());

        for (Verse verse : verses) {
            assertTrue(verse instanceof Verse);
            assertEquals(1, verse.getId());
            assertNull(verse.getText());
            assertNull(verse.getTranslationReference());
        }

        verify(database).Query(eq("select Verse_ID from Verse where Translation_ID in (1, 2, 3) and Book_ID >= 1 and Book_ID <= 1 and Chapter >= 1 and Chapter <= 1 and Verse_No >= 1 and Verse_No <= 1"));
    }

    @Test
    public void testGetWithEmptyTranslationArray() {
        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(0, verses.size());

        verify(database, never()).Query(anyString());
    }

    @Test
    public void testGetWithBeginEndReferenceNull() {
        List<Verse> verses = Verse.Get(new Translation[0], null, null);
        assertEquals(0, verses.size());

        verify(database, never()).Query(anyString());
    }

    @Test
    public void testGetWithBeginReferenceNull() {
        List<Verse> verses = Verse.Get(new Translation[0], null, new Reference(1, 1, 1));
        assertEquals(0, verses.size());

        verify(database, never()).Query(anyString());
    }

    @Test
    public void testGetWithEndReferenceNull() {
        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), null);
        assertEquals(0, verses.size());

        verify(database, never()).Query(anyString());
    }

    @Test
    public void testGetWithBeginEndReferenceOutOfRange() {
        when(database.Query(eq("select Verse_ID from Verse where Translation_ID = 1 and Book_ID = 1 and Chapter = 0 and Verse_No = 0")))
                .thenReturn(getMockResultWithIds(1));

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 0, 0), new Reference(1, 1, 1));
        assertEquals(1, verses.size());

        assertEquals(-1, verses.get(0).getId());

        verify(database).Query(eq("select Verse_ID from Verse where Translation_ID = 1 and Book_ID = 1 and Chapter = 0 and Verse_No = 0"));
    }

    @Test
    public void testGetWithBeginEndReferenceInRange() {
        when(database.Query(eq("select Verse_ID from Verse where Translation_ID = 1 and Book_ID = 1 and Chapter = 1 and Verse_No = 1")))
                .thenReturn(getMockResultWithIds(1));

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(1, verses.size());

        assertEquals(1, verses.get(0).getId());

        verify(database).Query(eq("select Verse_ID from Verse where Translation_ID = 1 and Book_ID = 1 and Chapter = 1 and Verse_No = 1"));
    }

    private ResultSet getMockResultWithText(String text, String... ids) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("Text")).thenReturn(text);
        when(resultSet.getInt("Verse_ID")).thenReturn(Integer.parseInt(ids[0]));
        when(resultSet.getInt("Translation_ID")).thenReturn(Integer.parseInt(ids[1]));
        when(resultSet.getInt("Book_ID")).thenReturn(Integer.parseInt(ids[2]));
        when(resultSet.getInt("Chapter")).thenReturn(Integer.parseInt(ids[3]));
        when(resultSet.getInt("Verse_No")).thenReturn(Integer.parseInt(ids[4]));
        return resultSet;
    }

    private ResultSet getMockResultWithIds(int... ids) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("Verse_ID")).thenAnswer(i -> ids[i.getArgument(0)]);
        return resultSet;
    }
}