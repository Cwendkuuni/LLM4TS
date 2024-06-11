import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        assertEquals("John", record.get(CSVRecord.Column.NAME1));
        assertEquals("Doe", record.get(CSVRecord.Column.NAME2));
        assertEquals("30", record.get(CSVRecord.Column.AGE));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, new HashMap() {{
            put("Name1", 0);
            put("Name2", 1);
            put("Age", 2);
        }}, null, 1);
        assertEquals("John", record.get("Name1"));
        assertEquals("Doe", record.get("Name2"));
        assertEquals("30", record.get("Age"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30", "Comment=This is a comment"}, null, null, 1);
        assertEquals("This is a comment", record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        assertEquals(Long.valueOf(1), record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        assertTrue(record.isConsistent());
        CSVRecord record2 = new CSVRecord(new String[]{"Name1=John,Name2=Doe"}, null, null, 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, new HashMap() {{
            put("Name1", 0);
            put("Name2", 1);
            put("Age", 2);
        }}, null, 1);
        assertTrue(record.isMapped("Name1"));
        assertTrue(record.isMapped("Name2"));
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, new HashMap() {{
            put("Name1", 0);
            put("Name2", 1);
            put("Age", 2);
        }}, null, 1);
        assertTrue(record.isSet("Name1"));
        assertTrue(record.isSet("Name2"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        assertEquals("[Name1=John, Name2=Doe, Age=30]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        String[] values = record.values();
        assertEquals("Name1=John", values[0]);
        assertEquals("Name2=Doe", values[1]);
        assertEquals("Age=30", values[2]);
    }

    @Test
    public void testParseCSVWithComment() {
        String csv = "Name1,Name2,Age,Comment\n" +
                "Name1=John,Name2=Doe,Age=30,\"This is a comment\"\n";
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.parse(new StringReader(csv));
        CSVRecord record = parser.nextRecord();
        assertEquals("John", record.get("Name1"));
        assertEquals("Doe", record.get("Name2"));
        assertEquals("30", record.get("Age"));
        assertEquals("This is a comment", record.getComment());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        record.get("Unknown");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        record.get(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, new HashMap() {{
            put("Name1", 0);
            put("Name2", 1);
            put("Age", 2);
            put("Unknown", 3);
        }}, null, 1);
        record.get("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameMissingValue() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, new HashMap() {{
            put("Name1", 0);
            put("Name2", 1);
            put("Age", 2);
        }}, null, 1);
        record.get("Name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithNullMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name1=John,Name2=Doe,Age=30"}, null, null, 1);
        record.get("Name1");
    }
}