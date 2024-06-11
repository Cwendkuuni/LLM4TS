package bible.obj.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.DatabaseMetaData;
import java.sql.DbConnection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;

import bible.obj.Identifible;
import bible.obj.Reference;
import bible.obj.Translation;
import bible.obj.TranslationReference;
import bible.obj.Verse;
import bible.util.DbResult;
import bible.util.Database;
import bible.util.Util;

public class VerseTest {

    private static final String DB_URL = "jdbc:mock:jdbc:testdb";
    private static final String DB_USER = "testuser";
    private static final String DB_PASSWORD = "testpassword";

    private Database database;
    private DbConnection connection;

    @Before
    public void setUp() throws Exception {
        database = new Database();
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        database.setConnection(connection);
    }

    @After
    public void tearDown() throws Exception {
        database.close();
        connection.close();
    }

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        Translation translation = mock(Translation.class);
        Reference reference = new Reference(1, 1, 1);
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(new Book(1, "Genesis"));
        TranslationReference transRef = new TranslationReference(translation, reference);

        int id = Verse.GetId(translation, null, 1, 1);
        assertEquals(1, id);

        id = Verse.GetId(null, new Book(1, "Genesis"), 1, 1);
        assertEquals(1, id);

        id = Verse.GetId(translation, new Book(1, "Genesis"), 1, 1);
        assertEquals(1, id);

        id = Verse.GetId(translation, new Book(1, "Genesis"), 1, 2);
        assertNull(id);
    }

    @Test
    public void testGet() {
        Translation translation1 = mock(Translation.class);
        Translation translation2 = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        Reference reference1 = new Reference(1, 1, 1);
        Reference reference2 = new Reference(1, 1, 2);
        TranslationReference transRef1 = new TranslationReference(translation1, reference1);
        TranslationReference transRef2 = new TranslationReference(translation2, reference2);

        when(translation1.getId()).thenReturn(1);
        when(translation1.getBook()).thenReturn(book);
        when(translation2.getId()).thenReturn(2);
        when(translation2.getBook()).thenReturn(book);

        Verse verse1 = new Verse(1);
        Verse verse2 = new Verse(2);

        List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, null, null, null);
        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));

        verses = Verse.Get(new Translation[] { translation1, translation2 }, book, null, null);
        assertEquals(2, verses.size());
        assertEquals(verse1, verses.get(0));
        assertEquals(verse2, verses.get(1));

        verses = Verse.Get(new Translation[] { translation1, translation2 }, null, reference1, reference1);
        assertEquals(1, verses.size());
        assertEquals(verse1, verses.get(0));

        verses = Verse.Get(new Translation[] { translation1, translation2 }, null, reference1, reference2);
        assertEquals(1, verses.size());
        assertSame(verse1, verses.get(0));
    }

    @Test
    public void testGetWithEmptyTranslationArray() {
        List<Verse> verses = Verse.Get(new Translation[0], null, null, null);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithNullReferenceBegin() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, null, null);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithNullReferenceEnd() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, null, new Reference(1, 1, 1));
        assertEquals(0, verses.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidReferenceBegin() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, new Reference(-1, 1, 1), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidReferenceEnd() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, null, new Reference(1, -1, 1));
    }

    @Test(expected = SQLException.class)
    public void testGetWithSqlException() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        Reference reference = new Reference(1, 1, 1);
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        Statement statement = mock(Statement.class);
        when(statement.executeQuery(anyString())).thenThrow(new SQLException());

        when(connection.createStatement()).thenReturn(statement);

        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, reference, reference);
    }

    @Test
    public void testNewWithIdFromDatabase() {
        DbResult result = mock(DbResult.class);
        when(result.next()).thenReturn(true).thenReturn(false);
        when(result.getInt("Verse_ID")).thenReturn(1);
        when(result.getString("Text")).thenReturn("The first verse.");
        when(result.getInt("Translation_ID")).thenReturn(1);
        when(result.getInt("Book_ID")).thenReturn(1);
        when(result.getInt("Chapter")).thenReturn(1);
        when(result.getInt("Verse_No")).thenReturn(1);

        when(database.query(anyString())).thenReturn(result);

        Verse verse = Verse.New(1);
        assertEquals("The first verse.", verse.getText());
        assertEquals(1, verse.getId());
    }

    @Test(expected = SQLException.class)
    public void testNewWithSqlException() {
        when(database.query(anyString())).thenThrow(new SQLException());

        Verse verse = Verse.New(1);
    }

    @Test
    public void testComplete() {
        DbResult result = mock(DbResult.class);
        when(result.next()).thenReturn(true).thenReturn(false);
        when(result.getInt("Verse_ID")).thenReturn(1);
        when(result.getString("Text")).thenReturn("The first verse.");
        when(result.getInt("Translation_ID")).thenReturn(1);
        when(result.getInt("Book_ID")).thenReturn(1);
        when(result.getInt("Chapter")).thenReturn(1);
        when(result.getInt("Verse_No")).thenReturn(1);

        when(database.query(anyString())).thenReturn(result);

        Verse verse = new Verse(null);
        verse.complete(result);

        assertEquals(1, verse.getId());
        assertEquals("The first verse.", verse.getText());
        assertEquals(new TranslationReference(new Translation(1, "Test Translation"), new Reference(1, 1, 1)), verse.getTranslationReference());
    }

    @Test(expected = SQLException.class)
    public void testCompleteWithSqlException() {
        when(database.query(anyString())).thenThrow(new SQLException());

        Verse verse = new Verse(null);
        verse.complete(null);
    }

    @Test
    public void testReadId() {
        Translation translation = mock(Translation.class);
        Book book = new Book(1, "Genesis");
        Reference reference = new Reference(1, 1, 1);
        when(translation.getId()).thenReturn(1);
        when(translation.getBook()).thenReturn(book);

        when(database.query(anyString())).thenReturn(createResultWithId(2));

        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(2, id);
    }

    @Test
    public void testReadIdWithNullDatabaseResult() {
        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testReadIdWithNoResult() {
        when(database.query(anyString())).thenReturn(createEmptyResult());

        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testStaticInitialization() {
        assertNotNull(Verse.translations);
        assertNotNull(Verse.verseCache);
    }

    private DbResult createEmptyResult() {
        DbResult result = mock(DbResult.class);
        when(result.next()).thenReturn(false);
        return result;
    }

    private DbResult createResultWithId(int id) {
        DbResult result = mock(DbResult.class);
        when(result.next()).thenReturn(true).thenReturn(false);
        when(result.getInt("Verse_ID")).thenReturn(id);
        return result;
    }
}