import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetMethod() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test getting a valid value by name
        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("LastName"));
        assertEquals("john.doe@example.com", record.get("Email"));

        // Test getting a valid value by index
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("john.doe@example.com", record.get(2));

        // Test getting a null value by name when no header mapping is provided
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);
        assertNull(recordWithoutMapping.get("Email"));

        // Test getting a null value by name when the record is inconsistent
        CSVRecord recordWithInvalidMapping = new CSVRecord(values, null, null, 1);
        recordWithInvalidMapping.mapping = new HashMap<>();
        recordWithInvalidMapping.mapping.put("Name", 2);
        recordWithInvalidMapping.mapping.put("Email", 3);
        assertNull(recordWithInvalidMapping.get("Email"));
    }

    @Test
    public void testGetCommentMethod() {
        String comment = "This is a comment for the record";
        CSVRecord record = new CSVRecord(null, null, comment, 1);
        assertEquals(comment, record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumberMethod() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentMethod() {
        String[] validValues = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord validRecord = new CSVRecord(validValues, null, null, 1);
        assertTrue(validRecord.isConsistent());

        String[] invalidValues = new String[]{"John", "Doe"};
        CSVRecord invalidRecord = new CSVRecord(invalidValues, null, null, 1);
        assertFalse(invalidRecord.isConsistent());
    }

    @Test
    public void testIsMappedMethod() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test whether a given column is mapped
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("LastName"));
        assertTrue(record.isMapped("Email"));

        // Test whether a given column is not mapped
        assertFalse(record.isMapped("NonExistentColumn"));
    }

    @Test
    public void testIsSetMethod() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("LastName", 1);
        mapping.put("Email", 2);

        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test whether a given column is set
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("LastName"));
        assertTrue(record.isSet("Email"));

        // Test whether a given column is not set
        assertFalse(record.isSet("NonExistentColumn"));
    }

    @Test
    public void testIteratorMethod() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test getting all values using the iterator
        List<String> list = record.toList();
        Iterator<String> iterator = record.iterator();

        assertEquals(list.size(), 3);
        assertEquals(iterator.next(), "John");
        assertEquals(iterator.next(), "Doe");
        assertEquals(iterator.next(), "john.doe@example.com");
    }

    @Test
    public void testPutInMethod() {
        Map<String, String> targetMap = new HashMap<>();
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "john.doe@example.com"}, null, null, 1);

        // Test putting all values into a map
        Map<String, String> map = record.putIn(targetMap);

        assertEquals(map.size(), 3);
        assertEquals(map.get("Name"), "John");
        assertEquals(map.get("LastName"), "Doe");
        assertEquals(map.get("Email"), "john.doe@example.com");
    }

    @Test
    public void testSizeMethod() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(record.size(), 3);
    }

    @Test
    public void testToListMethod() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> list = record.toList();

        assertEquals(list.size(), 3);
        assertEquals(list.get(0), "John");
        assertEquals(list.get(1), "Doe");
        assertEquals(list.get(2), "john.doe@example.com");
    }

    @Test
    public void testToStringMethod() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[John, Doe, john.doe@example.com]", record.toString());
    }
}