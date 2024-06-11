package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(DB_URL);
        Statement statement = connection.createStatement();
        statement.execute("create table Verse (Verse_ID int primary key, Text varchar(100), Translation_ID int, Book_ID int, Chapter int, Verse_No int)");
        statement.execute("create table Translation (Translation_ID int primary key, Book_ID int, Name varchar(50))");
    }

    @After
    public void tearDown() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute("drop table Verse");
        statement.execute("drop table Translation");
        connection.close();
    }

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = Verse.New(1);
        assertEquals(verse1, verse2);
    }

    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int id = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(id, Verse.GetId(new Translation(translationId, "Test Translation"), new Book(bookId, "Test Book"), chapterId, verseId));
    }

    @Test
    public void testGet() {
        Translation translation1 = new Translation(1, "Test Translation 1");
        Translation translation2 = new Translation(2, "Test Translation 2");

        Book book = new Book(1, "Test Book");
        book.addVerse(new Verse(1, translation1, "Test Verse 1"));
        book.addVerse(new Verse(2, translation2, "Test Verse 2"));

        List<Verse> verses = Verse.Get(new Translation[] { translation1, translation2 }, book.getReference(), book.getReference());
        assertEquals(2, verses.size());
        assertEquals(new Verse(1, translation1, "Test Verse 1"), verses.get(0));
        assertEquals(new Verse(2, translation2, "Test Verse 2"), verses.get(1));
    }

    @Test
    public void testGetWithEmptyTranslationArray() {
        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithInvalidReference() {
        Translation translation = new Translation(1, "Test Translation");
        fail("Expected IllegalArgumentException when get verses with invalid reference");
        List<Verse> verses = Verse.Get(new Translation[] { translation }, null, null);
    }

    @Test
    public void testGetIdFromDatabase() throws SQLException {
        String text = "Test Verse";
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        Statement statement = connection.createStatement();
        statement.execute("insert into Verse (Text, Translation_ID, Book_ID, Chapter, Verse_No) values ('" + text + "', " + translationId + ", " + bookId + ", " + chapterId + ", " + verseId + ")");
        int id = Verse.getId(translationId, bookId, chapterId, verseId);
        assertEquals(id, Verse.GetId(translationId, bookId, chapterId, verseId));
    }

    @Test
    public void testGetFromDatabase() throws SQLException {
        String text1 = "Test Verse 1";
        String text2 = "Test Verse 2";
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId1 = 1;
        int verseId2 = 2;

        Statement statement = connection.createStatement();
        statement.execute("insert into Verse (Text, Translation_ID, Book_ID, Chapter, Verse_No) values ('" + text1 + "', " + translationId + ", " + bookId + ", " + chapterId + ", " + verseId1 + ")");
        statement.execute("insert into Verse (Text, Translation_ID, Book_ID, Chapter, Verse_No) values ('" + text2 + "', " + translationId + ", " + bookId + ", " + chapterId + ", " + verseId2 + ")");

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(bookId, chapterId, 1), new Reference(bookId, chapterId, 2));
        assertEquals(2, verses.size());
        assertEquals(new Verse(verseId1, new Translation(translationId, "Test Translation"), new Reference(bookId, chapterId, verseId1), text1), verses.get(0));
        assertEquals(new Verse(verseId2, new Translation(translationId, "Test Translation"), new Reference(bookId, chapterId, verseId2), text2), verses.get(1));
    }

    @Test
    public void testGetFromDatabaseWithEmptyResultSet() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from Verse where Translation_ID = 1 and Book_ID = 1 and Chapter = 1 and Verse_No >= 1 and Verse_No <= 2");

        List<Verse> verses = Verse.Get(new Translation[0], new Reference(1, 1, 1), new Reference(1, 1, 2));
        assertEquals(0, verses.size());
    }
}