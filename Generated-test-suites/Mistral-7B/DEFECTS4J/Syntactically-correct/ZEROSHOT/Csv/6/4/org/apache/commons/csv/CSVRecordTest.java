import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordBasic() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csv = "Name,Age,City\nJohn,25,New York\nMary,30,Los Angeles";
 try (CSVParser parser = new CSVParserBuilder().withFormat(format).buildParser(new StringReader(csv));) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("Name,Age,City", record.getMapping().toString());
 assertEquals("John", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertEquals("John", record.get(0));
 assertEquals("Age", record.getMapping().get("Age").toString());
 assertEquals(3, record.size());

 // Test get method with Enum
 EnumTest enumTest = EnumTest.AGE;
 assertEquals("Age", record.get(enumTest));

 }
 }

 @Test
 public void testCSVRecordNoHeader() {
 CSVFormat format = CSVFormat.DEFAULT.withHeaderExcluded();
 String csv = "Name,Age,City\nJohn,25\nMary,30";
 try (CSVParser parser = new CSVParserBuilder().withFormat(format).buildParser(new StringReader(csv));) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertTrue(record.isConsistent());
 assertFalse(record.isMapped("Name"));
 assertNull(record.getComment());
 assertNull(record.getRecordNumber());
 assertEquals(2, record.size());
 }
 }

 @Test
 public void testCSVRecordInconsistent() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csv = "Name,Age,City\nJohn,25,New York\nMary,30";
 try (CSVParser parser = new CSVParserBuilder().withFormat(format).buildParser(new StringReader(csv));) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 CSVRecord inconsistentRecord = new CSVRecord(record.values(), record.mapping, record.comment, record.recordNumber) {
 @Override
 public boolean isConsistent() {
 return false;
 }
 };

 assertEquals("Name,Age,City", record.getMapping().toString());
 assertFalse(record.isConsistent());
 assertFalse(inconsistentRecord.isConsistent());
 assertEquals("New York", record.get("City"));
 assertEquals("New York", inconsistentRecord.get("City"));
 }
 }

 @Test
 public void testCSVRecordComment() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csv = "Name,Age,City\n# John is my friend\nJohn,25,New York\nMary,30,Los Angeles";
 try (CSVParser parser = new CSVParserBuilder().withFormat(format).buildParser(new StringReader(csv));) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("Name,Age,City", record.getMapping().toString());
 assertEquals("John is my friend", record.getComment());
 assertEquals("John", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("New York", record.get("City"));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordNullMapping() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csv = "Name,Age,City\nJohn,25,New York\nMary,30,Los Angeles";
 CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 0);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordMissingMapping() {testCSVRecordBasic();
 CSVRecord record = records.get(0);
 record.get("MissingColumn");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordIndexOutOfBounds() {
 CSVFormat format = CSVFormat.DEFAULT;
 String csv = "Name,Age,City\nJohn,25,New York\nMary,30,Los Angeles";
 try (CSVParser parser = new CSVParserBuilder().withFormat(format).buildParser(new StringReader(csv));) {
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 assertEquals("Name,Age,City", record.getMapping().toString());
 record.get(5);
 }
 }
}