import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testBasic() {
 String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertArrayEquals(new String[]{"Name", "Age", "City"}, record.mapping.keySet().toArray(new String[0]));
 assertArrayEquals(new String[]{"Name", "Age", "City"}, record.values());
 assertEquals("John Doe", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertEquals("New York", record.get(2));
 assertEquals("Name", record.mapping.get("Name").toString());
 assertEquals(0, record.mapping.get("Name").intValue());
 assertEquals("Age", record.mapping.get("Age").toString());
 assertEquals(1, record.mapping.get("Age").intValue());
 assertEquals("City", record.mapping.get("City").toString());
 assertEquals(2, record.mapping.get("City").intValue());
 assertEquals(null, record.get("Unknown"));
 assertTrue(record.isConsistent());
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertFalse(record.isSet("Unknown"));
 assertEquals("3 elements: [Name, Age, City]", record.toString());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNoMapping() {
 String csv = "Name,Age\nJohn Doe,30";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 }
 }

 @Test
 public void testComment() {
 String csv = "Name,Age,City#This is a comment\nJohn Doe,30,New York";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertEquals(null, record.getComment());
 record = records.get(1);
 assertEquals("This is a comment", record.getComment());
 }
 }

 @Test
 public void testRecordNumber() {
 String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record1 = records.get(0);
 CSVRecord record2 = records.get(1);
 assertEquals(0L, record1.getRecordNumber());
 assertEquals(1L, record2.getRecordNumber());
 }
 }

 @Test
 public void testGetByEnum() {
 String csv = "Name,Age,City\nJohn Doe,30,New York";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertEquals("John Doe", record.get(CSVRecord.FIELD.NAME.getName()));
 assertEquals("30", record.get(CSVRecord.FIELD.AGE.getName()));
 assertEquals("New York", record.get(CSVRecord.FIELD.CITY.getName()));
 }
 }

 @Test
 public void testIterable() {
 String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 List<String> values = record.iterator().asList();
 assertArrayEquals(new String[]{"Name", "Age", "City"}, values.toArray(new String[0]));
 }
 }

 @Test
 public void testPutIn() {
 String csv = "Name,Age,City\nJohn Doe,30,New York";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 Map<String, String> map = new HashMap<>();
 map = record.putIn(map);
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("Name"));
 }
 }

 @Test
 public void testToMap() {
 String csv = "Name,Age,City\nJohn Doe,30,New York";
 try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 Map<String, String> map = record.toMap();
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("Name"));
 }
 }
}