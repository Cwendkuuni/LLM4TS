import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.StringReader;
import java.util.Arrays;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String csvString = "Name,Age,Comment\nJohn Doe,30,This is a comment\nJane Doe,25,Another comment\n";

 @Test
 public void testCSVRecordCreation() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvString));
 CSVRecord record = parser.nextRecord();

 assertEquals(3, record.size());
 assertEquals("Name", record.get("Name"));
 assertEquals("John Doe", record.get("Name"));
 assertEquals("Age", record.get("Age"));
 assertEquals("30", record.get("Age"));
 assertEquals("Comment", record.get("Comment"));
 assertEquals("This is a comment", record.get("Comment"));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoHeaderMapping() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvString));
 CSVRecord record = parser.nextRecord();
 record.get("NonExistentColumn");
 }

 @Test
 public void testGetEmptyString() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvString));
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isConsistent());
 assertEquals("", record.get(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndex() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvString));
 CSVRecord record = parser.nextRecord();
 record.get(record.size() + 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidName() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvString));
 CSVRecord record = parser.nextRecord();
 record.get("NonExistentColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMismatchedColumns() {
 String csvStringWithMismatchedColumns = "Name,Age,Comment\nJohn Doe,30,This is a comment\nName,Age,Name";
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(new StringReader(csvStringWithMismatchedColumns));
 CSVRecord record = parser.nextRecord();
 record.get("Comment");
 }
}