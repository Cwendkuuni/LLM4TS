import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static CSVFormat format;
 private static CSVParser parser;

 @BeforeClass
 public static void setup() {
 format = new CSVFormat(CSVFormat.DEFAULT, ',', '\'\'', '\'\'');
 parser = format.newParser();
 }

 @Test
 public void testCSVRecord_Empty() {
 String csv = "";
 List<CSVRecord> records = parseCSV(csv);
 assertEquals(0, records.size());
 }

 @Test
 public void testCSVRecord_SingleRecord() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 assertNotNull(records.get(0));
 CSVRecord record = records.get(0);
 assertEquals("Name", record.get("Name"));
 assertEquals("John Doe", record.get("Name"));
 assertEquals("Age", record.get("Age"));
 assertEquals("30", record.get("Age"));
 assertEquals("City", record.get("City"));
 assertEquals("New York", record.get("City"));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testCSVRecord_MultipleRecords() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York\nJane Doe,\"25\",Chicago\n";
 List<CSVRecord> records = parseCSV(csv);
 assertEquals(2, records.size());
 assertNotNull(records.get(0));
 assertNotNull(records.get(1));
 }

 @Test
 public void testCSVRecord_NullString() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York\n,25,Chicago\n";
 List<CSVRecord> records = parseCSV(csv);
 assertEquals(2, records.size());
 assertNotNull(records.get(0));
 assertNotNull(records.get(1));
 assertNull(records.get(0).get("City"));
 assertEquals(null, records.get(1).get("Age"));
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_MissingMapping() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 record.get("Unknown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_IndexOutOfBounds() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 record.get(3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_MismatchedSize() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York\nJohn Doe,\"30\",New York,New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 assertEquals(3, record.size());
 }

 @Test
 public void testCSVRecord_EmptyMapping() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecord_NonEmptyMapping() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecord_MapValues() {
 String csv = "Name,Age,City\nJohn Doe,\"30\",New York";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 Map<String, String> map = record.toMap();
 assertEquals(3, map.size());
 assertEquals("Name", map.keySet().toArray()[0].toString());
 assertEquals("30", map.get("Age"));
 }

 @Test
 public void testCSVRecord_EmptyMap() {
 String csv = "";
 List<CSVRecord> records = parseCSV(csv);
 CSVRecord record = records.get(0);
 assertEquals(0, record.size());
 }

 private List<CSVRecord> parseCSV(String csv) {
 return parser.parse(new StringReader(csv)).getRecords();
 }

}