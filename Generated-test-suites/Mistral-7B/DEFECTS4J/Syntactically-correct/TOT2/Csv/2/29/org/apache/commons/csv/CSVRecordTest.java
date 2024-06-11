import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 0);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals(null, record.get(2));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertEquals("John", record.get("Name"));
        assertNull(record.get("unknown"));
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertTrue(record.isConsistent());

        CSVRecord recordWithMismatch = new CSVRecord(new String[]{"Name:John, Age:30", "Address:123 Main St"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertFalse(recordWithMismatch.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertTrue(record.isSet("Name"));
        assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        Iterator<String> iterator = record.iterator();
        assertEquals("Name:John", iterator.next());
        assertEquals("Age:30", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        assertArrayEquals(new String[]{"Name:John", "Age:30"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30", "#This is a comment"}, Map.of("Name", 0, "Age", 1), null, 0);
        assertEquals("#This is a comment", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        assertEquals(2, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        assertEquals("[Name:John, Age:30]", record.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNullMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInconsistentRecord() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30", "Address:123 Main St"}, Map.of("Name", 0, "Age", 1), null, 0);
        record.get("Address");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        record.get(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexNegative() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        record.get(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithEmptyMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        record.get("Name");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexWithMoreColumns() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30", "Address:123 Main St"}, null, null, 0);
        record.get(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexWithLessColumns() {
        CSVRecord record = new CSVRecord(new String[]{"Name:John, Age:30"}, null, null, 0);
        record.get(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByIndexWithNoValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.get(0);
    }
}