import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordCreation() throws IOException {
 String csvText = "name, age, comment\nJohn Doe, 30, \"This is a comment for John Doe\"\nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record1 = parser.nextRecord();
 CSVRecord record2 = parser.nextRecord();

 assertEquals("name", record1.get("name"));
 assertEquals("John Doe", record1.get("name"));
 assertEquals("30", record1.get("age"));
 assertEquals("This is a comment for John Doe", record1.getComment());
 assertEquals(1, record1.getRecordNumber());

 assertEquals("name", record2.get("name"));
 assertEquals("Jane Doe", record2.get("name"));
 assertEquals("25", record2.get("age"));
 assertNull(record2.getComment());
 assertEquals(2, record2.getRecordNumber());

 assertFalse(record1.isMapped("unknown"));
 assertFalse(record2.isMapped("unknown"));

 assertTrue(record1.isSet("name"));
 assertTrue(record1.isSet("age"));
 assertTrue(record1.isConsistent());
 assertTrue(record2.isSet("name"));
 assertTrue(record2.isSet("age"));
 assertTrue(record2.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithoutMapping() throws IOException {
 String csvText = "name, age\nJohn Doe, 30\nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record = parser.nextRecord();
 record.get("comment"); // should throw an IllegalStateException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordInvalidIndex() throws IOException {
 String csvText = "name, age\nJohn Doe, 30\nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record = parser.nextRecord();
 record.get(10); // should throw an IllegalArgumentException
 }

 @Test
 public void testCSVRecordNullValue() throws IOException {
 String csvText = "name, age, comment\nJohn Doe, null, \nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withNullString(""));
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record1 = parser.nextRecord();
 assertTrue(String.isEmpty(record1.get("age")));

 CSVRecord record2 = parser.nextRecord();
 assertEquals("", record2.get("age"));
 }

 @Test
 public void testCSVRecordMapToMap() throws IOException {
 String csvText = "name, age, comment\nJohn Doe, 30, This is a comment for John Doe\nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record = parser.nextRecord();
 Map<String, String> map = record.toMap();
 assertEquals("name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("name"));
 assertEquals("30", map.get("age"));
 assertEquals("This is a comment for John Doe", map.get("comment"));
 }

 @Test
 public void testCSVRecordToString() throws IOException {
 String csvText = "name, age, comment\nJohn Doe, 30, This is a comment for John Doe\nJane Doe, 25";
 CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
 CSVParser parser = format.parse(new StringReader(csvText));
 CSVRecord record = parser.nextRecord();
 assertEquals("[name=John Doe, age=30, comment=This is a comment for John Doe]", record.toString());
 }
}