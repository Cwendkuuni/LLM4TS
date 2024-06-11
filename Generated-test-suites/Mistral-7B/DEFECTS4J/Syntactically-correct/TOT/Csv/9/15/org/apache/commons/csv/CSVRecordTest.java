import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGetValueByEnum() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get(Enum.NAME));
 assertEquals("New York", record.get(Enum.CITY));
 }

 @Test
 public void testCSVRecordGetValueByIndex() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testCSVRecordGetValueByName() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("John", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordGetValueByNameWithNoHeader() {
 CSVFormat format = new CSVFormat();
 String csv = "30,New York\n25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetValueByNameWithInvalidName() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 record.get("InvalidName");
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City,Comment\nJohn,30,New York,This is a comment\nMary,25,Chicago,Another comment";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("This is a comment", record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals(0, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 }

 @Test
 public void testCSVRecordIterator() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csv), format);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 List<String> values = new ArrayList<>(Arrays.asList(record.iterator()));

 assertEquals(Arrays.asList("John", "30", "New York"), values);
 }
}