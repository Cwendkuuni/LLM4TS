import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithNoCommentAndDefaultMapping() {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles\n";

        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.parse(new StringReader(csvContent));

        CSVRecord record = null;
        while ((record = parser.nextRecord()) != null) {
            // Test get methods with valid indices and names
            final String name = record.get(0);
            final int age = Integer.parseInt(record.get(1));
            final String city = record.get(2);

            assertEquals("John", name);
            assertEquals(30, age);
            assertEquals("New York", city);

            // Test get methods with invalid indices
            final String invalidName = record.get(3);
            assertNull(invalidName);

            // Test get method with valid enum
            final Enum header = Enum.valueOf("Name");
            final String nameFromEnum = record.get(header);
            assertEquals("John", nameFromEnum);

            // Test get method with invalid enum
            final Enum invalidHeader = Enum.valueOf("Age");
            final String invalidNameFromEnum = record.get(invalidHeader);
            assertNull(invalidNameFromEnum);

            // Test isConsistent method
            assertTrue(record.isConsistent());

            // Test isMapped method
            assertTrue(record.isMapped("Name"));
            assertFalse(record.isMapped("InvalidHeader"));

            // Test isSet method
            assertTrue(record.isSet("Name"));
            assertFalse(record.isSet("InvalidColumn"));

            // Test size method
            assertEquals(3, record.size());

            // Test toList method
            List<String> list = record.toList();
            assertEquals(3, list.size());
            assertEquals("John", list.get(0));
            assertEquals("30", list.get(1));
            assertEquals("New York", list.get(2));

            // Test toMap method
            Map<String, String> map = record.toMap();
            assertEquals(3, map.size());
            assertEquals("Name", map.keySet().toArray()[0].toString());
            assertEquals("John", map.get("Name"));
            assertEquals("Age", map.keySet().toArray()[1].toString());
            assertEquals("30", map.get("Age"));
            assertEquals("City", map.keySet().toArray()[2].toString());
            assertEquals("New York", map.get("City"));
            break;
        }

        parser.close();
    }

    @Test
    public void testCSVRecordWithCommentAndCustomMapping() {
        final String csvContent = "Name,Age,City\n#This is a comment\nJohn,30,New York\nMike,25,Los Angeles\n";

        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeaderComment("This is a comment"));
        final CSVParser parser = format.parse(new StringReader(csvContent));

        CSVRecord record = null;
        while ((record = parser.nextRecord()) != null) {
            // Test get methods with valid indices and names
            final String name = record.get(0);
            final int age = Integer.parseInt(record.get(1));
            final String city = record.get(2);

            assertEquals("John", name);
            assertEquals(30, age);
            assertEquals("New York", city);

            // Test get method with valid enum
            final Enum header = Enum.valueOf("Name");
            final String nameFromEnum = record.get(header);
            assertEquals("John", nameFromEnum);

            // Test get method with invalid enum
            final Enum invalidHeader = Enum.valueOf("Age");
            final String invalidNameFromEnum = record.get(invalidHeader);
            assertNull(invalidNameFromEnum);

            // Test isConsistent method
            assertTrue(record.isConsistent());

            // Test isMapped method
            assertTrue(record.isMapped("Name"));
            assertFalse(record.isMapped("InvalidHeader"));

            // Test isSet method
            assertTrue(record.isSet("Name"));
            assertFalse(record.isSet("InvalidColumn"));

            // Test size method
            assertEquals(3, record.size());

            // Test toList method
            List<String> list = record.toList();
            assertEquals(3, list.size());
            assertEquals("John", list.get(0));
            assertEquals("30", list.get(1));
            assertEquals("New York", list.get(2));

            // Test toMap method
            Map<String, String> map = record.toMap();
            assertEquals(3, map.size());
            assertEquals("Name", map.keySet().toArray()[0].toString());
            assertEquals("John", map.get("Name"));
            assertEquals("Age", map.keySet().toArray()[1].toString());
            assertEquals("30", map.get("Age"));
            assertEquals("City", map.keySet().toArray()[2].toString());
            assertEquals("New York", map.get("City"));

            // Test getComment method
            assertEquals("#This is a comment", record.getComment());

            // Test getRecordNumber method
            assertEquals(1, record.getRecordNumber());

            // Test isEmpty method
            assertFalse(record.isEmpty());

            // Test equals method
            final CSVRecord record2 = new CSVRecord(record.values(), record.mapping, record.comment, record.recordNumber);
            assertTrue(record.equals(record2));

            // Test hashCode method
            final int recordHashCode = record.hashCode();
            final int record2HashCode = record2.hashCode();
            assertEquals(recordHashCode, record2HashCode);

            // Test toString method
            assertEquals("[John, 30, New York]", record.toString());
            break;
        }

        parser.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Los Angeles";

        final CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);

        CSVRecord record = null;
        while ((record = parser.nextRecord()) != null) {
            // Test get methods with valid indices and names
            record.get(0);
            record.get(1);
            record.get(2);

            // Test get method with valid enum
            final Enum header = Enum.valueOf("Name");
            record.get(header);

            // Test get method with invalid enum
            final Enum invalidHeader = Enum.valueOf("Invalid");
            record.get(invalidHeader);

            // Test isConsistent method
            record.isConsistent();

            // Test isMapped method
            record.isMapped("Name");
            record.isMapped("Invalid");

            // Test isSet method
            record.isSet("Name");
            record.isSet("Invalid");

            // Test size method
            record.size();

            // Test toList method
            record.toList();

            // Test toMap method
            record.toMap();

            // Test getComment method
            record.getComment();

            // Test getRecordNumber method
            record.getRecordNumber();

            // Test isEmpty method
            record.isEmpty();

            // Test equals method
            record.equals(null);
            record.equals(new Object());

            // Test hashCode method
            record.hashCode();

            // Test toString method
            record.toString();

            // Test get method with invalid index
            record.get(3);

            // Test get method with invalid name
            record.get("Invalid");

            // Test get method with null values
            final String[] nullValues = null;
            final CSVRecord nullRecord = new CSVRecord(nullValues, record.mapping, record.comment, record.recordNumber);
            nullRecord.get(0);

            // Test isConsistent method with inconsistent record
            final String[] invalidValues = { "John", "30", "New York", "Mike" };
            final CSVRecord invalidRecord = new CSVRecord(invalidValues, record.mapping, record.comment, record.recordNumber);
            invalidRecord.isConsistent();

            // Test isMapped method with unrecognized header
            record.isMapped("Unrecognized");

            // Test isSet method with unrecognized column
            record.isSet("Unrecognized");

            // Test size method with empty record
            final CSVRecord emptyRecord = new CSVRecord(new String[0], record.mapping, record.comment, record.recordNumber);
            emptyRecord.size();

            // Test toList method with empty record
            final List<String> emptyList = emptyRecord.toList();
            emptyList.size();

            // Test toMap method with empty record
            final Map<String, String> emptyMap = emptyRecord.toMap();
            emptyMap.size();

            // Test getComment method with empty record
            emptyRecord.getComment();

            // Test getRecordNumber method with empty record
            emptyRecord.getRecordNumber();

            // Test isEmpty method with empty record
            emptyRecord.isEmpty();

            // Test equals method with empty record
            emptyRecord.equals(new CSVRecord(new String[0], record.mapping, record.comment, record.recordNumber));

            // Test hashCode method with empty record
            emptyRecord.hashCode();

            // Test toString method with empty record
            emptyRecord.toString();

            // Test get method with null comment
            final CSVRecord nullCommentRecord = new CSVRecord(record.values(), record.mapping, null, record.recordNumber);
            nullCommentRecord.getComment();

            // Test get method with null values and comment
            final String[] nullValuesWithComment = new String[] { null, "Age", "City" };
            final CSVRecord nullValuesRecord = new CSVRecord(nullValuesWithComment, record.mapping, record.comment, record.recordNumber);
            nullValuesRecord.get(0);

            // Test get method with null values and no comment
            final String[] nullValuesNoComment = new String[] { null, "Age", "City" };
            final CSVRecord nullValuesNoCommentRecord = new CSVRecord(nullValuesNoComment, null, null, record.recordNumber);
            nullValuesNoCommentRecord.get(0);

            // Test equals method with empty record and null comment
            emptyRecord.equals(new CSVRecord(new String[0], record.mapping, null, record.recordNumber));

            // Test equals method with record with null comment
            emptyRecord.equals(new CSVRecord(new String[0], record.mapping, null, record.recordNumber));

            // Test equals method with record with empty comment
            emptyRecord.equals(new CSVRecord(new String[0], record.mapping, "", record.recordNumber));

            // Test equals method with record with different comment
            emptyRecord.equals(new CSVRecord(new String[0], record.mapping, "Another comment", record.recordNumber));

            parser.close();
        }
    }
}