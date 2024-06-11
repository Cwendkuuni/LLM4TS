import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testGetValueByEnum() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get(Enum.NAME));
 assertEquals("New York", record.get(Enum.CITY));
 }

 @Test
 public void testGetValueByIndex() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testGetValueByName() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("New York", record.get("City"));
 }

 @Test
 public void testGetComment() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City#This is a comment\nJohn,25,New York";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("This is a comment", record.getComment());
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age\nJohn,25\nMike,30";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testIsSet() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByInvalidName() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("Name", record.get("Unknown"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetByInvalidIndex() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("Name", record.get(-1));
 }

 @Test
 public void testIterator() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 Iterator<String> it = record.iterator();

 List<String> values = new ArrayList<>();
 while (it.hasNext()) {
 values.add(it.next());
 }

 assertEquals(3, values.size());
 assertEquals("Name", values.get(0));
 assertEquals("25", values.get(1));
 assertEquals("New York", values.get(2));
 }

 @Test
 public void testPutIn() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 Map<String, String> map = new HashMap<>();
 record.putIn(map);

 assertEquals(3, map.size());
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("John", map.get("Name"));
 assertEquals("25", map.get("Age"));
 assertEquals("New York", map.get("City"));
 }

 @Test
 public void testSize() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals(3, record.size());
 }

 @Test
 public void testToList() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals(Arrays.asList("Name", "25", "New York"), record.toList());
 }

 @Test
 public void testToMap() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles";
 CSVParser parser = format.parse(csv.split("\n")).getParser();
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 Map<String, String> map = record.toMap();
 assertEquals(3, map.size());
 assertEquals("Name", map.keySet().iterator().next());
 assertEquals("Name", map.get("Name"));
 assertEquals("25", map.get("Age"));
 assertEquals("New York", map.get("City"));
 }
}