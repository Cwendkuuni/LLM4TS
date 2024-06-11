import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The record has the correct values, mapping, and comment
        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Name")); // Name is case-insensitive
        assertEquals("30", record.get("Age"));
        assertNull(record.get("Unknown"));
        assertSame(values, record.values());
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());

        // And: The record is consistent
        assertTrue(record.isConsistent());

        // And: The record can be converted to a map
        final Map<String, String> recordMap = record.toMap();
        assertEquals("John", recordMap.get("Name"));
        assertEquals("Doe", recordMap.get("Name")); // Name is case-insensitive
        assertEquals("30", recordMap.get("Age"));
        assertNull(recordMap.get("Unknown"));
    }

    @Test
    public void testCSVRecordGetByIndex() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The correct value can be retrieved by index
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("male", record.get(3));
        assertNull(record.get(4));
    }

    @Test
    public void testCSVRecordGetByName() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The correct value can be retrieved by name
        assertEquals("John", record.get("Name"));
        assertEquals("John", record.get("name")); // Name is case-insensitive
        assertEquals("Doe", record.get("DOe")); // Name is case-insensitive
        assertEquals("30", record.get("Age"));
        assertEquals("male", record.get("Gender"));
        assertNull(record.get("Unknown"));

        // And: The record throws an exception when trying to get a value by name if the record is inconsistent
        mapping.put("unknown", 3);
        try {
            record.get("Unknown");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCSVRecordGetComment() {
        // Given: A CSV record with a comment
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        final String comment = "Record with John Doe";

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        // Then: The correct comment can be retrieved
        assertEquals(comment, record.getComment());

        // And: The record has no comment if none is provided
        final CSVRecord recordNoComment = new CSVRecord(values, mapping, null, 1);
        assertNull(recordNoComment.getComment());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        // Given: A CSV record with a record number
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        final long recordNumber = 1;

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        // Then: The correct record number can be retrieved
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordIsConsistent() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The record is consistent since the mapping and values have the same size
        assertTrue(record.isConsistent());

        // And: The record is inconsistent when values and mapping have different sizes
        mapping.put("Unknown", 3);
        assertFalse(new CSVRecord(values, mapping, null, 1).isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The Name column is mapped
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("name")); // Name is case-insensitive
        assertTrue(record.isMapped("NAME")); // Name is case-insensitive

        // And: The Unknown column is not mapped
        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The Name, Age, and Gender columns are set
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Name")); // Name is case-insensitive
        assertTrue(record.isSet("name")); // Name is case-insensitive
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));

        // And: The Unknown column is not set
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecordToString() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The record has a string representation that shows the values
        assertEquals("[John, Doe, 30, male]", record.toString());

        // And: The record has a string representation that shows the mapping when debugging
        final String recordToString = record.toString();
        assertTrue(recordToString.contains("[Name=0, Age=1, Gender=2]"));
    }

    @Test
    public void testCSVRecordGetByEnum() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The Name, Age, and Gender columns can be retrieved by their enum representation
        assertEquals("John", record.get(CSVRecordTest.Name.NAME));
        assertEquals("Doe", record.get(CSVRecordTest.Name.NAME)); // Name is case-insensitive
        assertEquals("30", record.get(CSVRecordTest.Age.AGE));
        assertEquals("male", record.get(CSVRecordTest.Gender.GENDER));

        // And: The Unknown column cannot be retrieved by an enum representation
        try {
            record.get(CSVRecordTest.Unknown.UNKNOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCSVRecordIterator() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The iterator returns the correct values in the correct order
        final List<String> recordValues = record.iterator();
        assertEquals("John", recordValues.get(0));
        assertEquals("Doe", recordValues.get(1));
        assertEquals("30", recordValues.get(2));
        assertEquals("male", recordValues.get(3));
    }

    @Test
    public void testCSVRecordPutInMap() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The values can be put into a map using their mapping
        final Map<String, String> recordMap = new HashMap<>();
        recordMap.putAll(record.putIn(recordMap));
        assertEquals("John", recordMap.get("Name"));
        assertEquals("Doe", recordMap.get("Name")); // Name is case-insensitive
        assertEquals("30", recordMap.get("Age"));
        assertEquals("male", recordMap.get("Gender"));

        // And: The map has the correct size
        assertEquals(4, recordMap.size());
    }

    @Test
    public void testCSVRecordSize() {
        // Given: A CSV record with values and mapping
        final String[] values = new String[]{"John", "Doe", "30", "male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);

        // When: A new CSVRecord is created
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Then: The record has the correct size
        assertEquals(4, record.size());
    }

    @Test
    public void testCSVRecordParseFromCSV() throws Exception {
        // Given: A CSV string with values and mapping
        final String csv = "Name,Age,Gender\nJohn,Doe,30,male";

        // When: A CSVParser is created and used to parse the string
        final CSVFormat format = new CSVFormat(',', '\r', '\n');
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();

        // Then: The record has the correct values, mapping, and comment
        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Name")); // Name is case-insensitive
        assertEquals("30", record.get("Age"));
        assertEquals("male", record.get("Gender"));
        assertNull(record.getComment());

        // And: The record is consistent
        assertTrue(record.isConsistent());

        // And: The record can be converted to a map
        final Map<String, String> recordMap = record.toMap();
        assertEquals("John", recordMap.get("Name"));
        assertEquals("Doe", recordMap.get("Name")); // Name is case-insensitive
        assertEquals("30", recordMap.get("Age"));
        assertEquals("male", recordMap.get("Gender"));
    }

    public enum Name {
        NAME, name, NAME
    }

    public enum Age {
        AGE
    }

    public enum Gender {
        GENDER
    }

    public enum Unknown {
        UNKNOWN
    }
}