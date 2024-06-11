import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("john.doe@example.com", record.get(3));
        assertEquals("john.doe@example.com", record.get("Email"));
        assertNull(record.get("InvalidName"));
        assertEquals(1, record.size());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isSet("Email"));
        assertFalse(record.isMapped("InvalidName"));
        assertFalse(record.isSet("InvalidName"));
    }

    @Test
    public void testCSVRecordGetComment() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com", "This is a comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        mapping.put("Comment", 4);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertNull(record.getComment());
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isConsistent());

        String[] values2 = new String[]{"John", "Doe", "30"};
        mapping.put("Email", 3);

        CSVRecord record2 = new CSVRecord(values2, mapping, null, 1L);

        assertFalse(record2.isConsistent());
    }

    @Test
    public void testCSVRecordParseCSV() throws Exception {
        String csv = "Name,Age,Email\nJohn,Doe,john.doe@example.com\n";

        CSVFormat format = new CSVFormat();

        List<CSVRecord> records = format.parse(new StringReader(csv));

        CSVRecord record = records.get(0);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("john.doe@example.com", record.get(2));
        assertNull(record.get("InvalidName"));
        assertEquals(1, record.size());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        // Test map values
        Map<String, String> map = record.toMap();
        assertEquals("Doe", map.get("Name"));
        assertEquals("john.doe@example.com", map.get("Email"));
        assertNull(map.get("InvalidName"));
        // Test toString
        assertEquals("[John, Doe, 30, john.doe@example.com]", record.toString());
    }

    @Test
    public void testCSVRecordErrorScenario() throws Exception {
        String csv = "Name,Age,Email\nJohn,Doe,30,john.doe@example.com\n";
        CSVFormat format = new CSVFormat();

        List<CSVRecord> records = format.parse(new StringReader(csv));

        CSVRecord record = records.get(0);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("john.doe@example.com", record.get(3));
        assertNull(record.get("InvalidName"));
        assertEquals(4, record.size());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Email"));
        assertFalse(record.isMapped("InvalidName"));
        assertFalse(record.isSet("InvalidName"));
    }
}