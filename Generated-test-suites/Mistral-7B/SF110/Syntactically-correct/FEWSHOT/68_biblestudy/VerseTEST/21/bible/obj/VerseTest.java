package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.DbResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {

    private static final Translation T_EN = new Translation(1, "English", null);
    private static final Translation T_LA = new Translation(2, "Latin", null);
    private static final Reference R_JOHN_1_1 = new Reference(1, 1, 1);
    private static final Reference R_JOHN_1_10 = new Reference(1, 1, 10);
    private static final Reference R_GENESIS_1_1 = new Reference(1, 1, 1);
    private static final Reference R_GENESIS_1_10 = new Reference(1, 1, 10);
    private static final Reference R_MATTHHEW_1_1 = new Reference(27, 1, 1);
    private static final Reference R_MATTHHEW_1_10 = new Reference(27, 1, 10);

    @BeforeClass
    public static void setup() {
        Database.initialize();
    }

    @Test
    public void testNew() {
        assertEquals(Verse.class, Verse.New(1).getClass());
        assertNotNull(Verse.New(1).getId());
        assertEquals(1, Verse.New(1).getId());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(T_EN, new Book(1, "Genesis"), 1, 1);
        assertEquals(1, id);
        id = Verse.GetId(null, new Book(1, "Genesis"), 1, 1);
        assertNull(id);
        id = Verse.GetId(T_EN, null, 1, 1);
        assertNull(id);
        id = Verse.GetId(T_EN, new Book(1, "Genesis"), 1, 10);
        assertEquals(-1, id);
        id = Verse.GetId(T_EN, new Book(1, "Genesis"), 2, 1);
        assertEquals(-1, id);

        id = Verse.GetId(T_LA, new Book(1, "Genesis"), 1, 1);
        assertEquals(1, id);
        id = Verse.GetId(T_LA, new Book(1, "Genesis"), 1, 10);
        assertEquals(-1, id);
        id = Verse.GetId(T_LA, new Book(1, "Genesis"), 2, 1);
        assertEquals(-1, id);

        id = Verse.GetId(T_EN, new Book(1, "Matthew"), 1, 1);
        assertEquals(1, id);
        id = Verse.GetId(T_EN, new Book(1, "Matthew"), 1, 10);
        assertEquals(10, id);
        id = Verse.GetId(T_EN, new Book(1, "Matthew"), 2, 1);
        assertEquals(-1, id);

        id = Verse.GetId(T_LA, new Book(1, "Matthew"), 1, 1);
        assertEquals(1, id);
        id = Verse.GetId(T_LA, new Book(1, "Matthew"), 1, 10);
        assertEquals(10, id);
        id = Verse.GetId(T_LA, new Book(1, "Matthew"), 2, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGet() {
        List<Verse> versesEN = Verse.Get(new Translation[]{T_EN}, new Book(1, "Genesis"), 1, 10);
        assertEquals(10, versesEN.size());
        List<Verse> versesLA = Verse.Get(new Translation[]{T_LA}, new Book(1, "Genesis"), 1, 10);
        assertEquals(10, versesLA.size());

        IntSummaryStatistics statsEN = IntStream.range(0, versesEN.size())
                .mapToInt(i -> versesEN.get(i).getId())
                .summaryStatistics();
        assertTrue(statsEN.getMin() >= 1 && statsEN.getMax() <= 10);

        IntSummaryStatistics statsLA = IntStream.range(0, versesLA.size())
                .mapToInt(i -> versesLA.get(i).getId())
                .summaryStatistics();
        assertTrue(statsLA.getMin() >= 1 && statsLA.getMax() <= 10);
    }

    @Test
    public void testGetWithTranslationAndReference() {
        List<Verse> versesEN = Verse.Get(new Translation[]{T_EN}, null, R_JOHN_1, R_JOHN_10);
        assertEquals(10, versesEN.size());
        List<Verse> versesLA = Verse.Get(new Translation[]{T_LA}, null, R_GENESIS_1, R_GENESIS_10);
        assertEquals(10, versesLA.size());

        IntSummaryStatistics statsEN = IntStream.range(0, versesEN.size())
                .mapToInt(i -> versesEN.get(i).getId())
                .summaryStatistics();
        assertTrue(statsEN.getMin() >= 1 && statsEN.getMax() <= 10);

        IntSummaryStatistics statsLA = IntStream.range(0, versesLA.size())
                .mapToInt(i -> versesLA.get(i).getId())
                .summaryStatistics();
        assertTrue(statsLA.getMin() >= 1 && statsLA.getMax() <= 10);
    }

    @Test
    public void testGetWithNoTranslation() {
        List<Verse> verses = Verse.Get(null, null, null, null);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithInvalidReference() {
        List<Verse> verses = Verse.Get(T_EN, new Book(1, "Genesis"), null, null);
        assertEquals(0, verses.size());
    }

    @Test
    public void testNewWithInvalidId() {
        try {
            new Verse(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new Verse("foo");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testNewWithValidId() {
        Verse verse = Verse.New(1);
        assertEquals(Verse.class, verse.getClass());
        assertNotNull(Verse.New(1).getId());
        assertEquals(1, Verse.New(1).getId());
    }

    @Test
    public void testGetWithValidId() throws SQLException {
        Verse verse = Verse.New(1);
        assertEquals(Verse.class, verse.getClass());
        assertNotNull(Verse.New(1).getId());
        assertEquals(1, Verse.New(1).getId());

        assertEquals(verse.getText(), Verse.New(1).getText());
        assertEquals(verse.getTranslationReference(), Verse.New(1).getTranslationReference());

        assertTrue(Verse.verseCache.containsKey(new Integer(1)));
        assertEquals(verse, Verse.verseCache.get(new Integer(1)));
    }

    @Test
    public void testGetWithInvalidId() throws SQLException {
        try {
            Verse.New(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Verse.New("foo");
            fail();
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testGetWithValidIdFromDB() throws SQLException {
        Verse verse = Verse.New(1);
        assertEquals(verse.getText(), Verse.New(1).getText());
        assertEquals(verse.getTranslationReference(), Verse.New(1).getTranslationReference());

        String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = 1";
        DbResult result = Database.Query(query);
        assertTrue(result.next());
        Verse verseFromDB = new Verse(result);
        assertEquals(verse.getText(), verseFromDB.getText());
        assertEquals(verse.getTranslationReference(), verseFromDB.getTranslationReference());
    }

    @Test
    public void testGetWithInvalidIdFromDB() throws SQLException {
        String query = "select Verse_ID, Text, Translation_ID, Book_ID, Chapter, Verse_No from Verse where Verse_ID = -1";
        DbResult result = Database.Query(query);
        assertFalse(result.next());
    }
}