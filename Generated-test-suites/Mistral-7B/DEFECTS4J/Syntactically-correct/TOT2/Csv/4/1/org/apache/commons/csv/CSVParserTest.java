import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

 @Test
 public void testParseFile() throws IOException {
 File csvFile = new File("src/test/resources/sample.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);

 List<CSVRecord> records = parser.getRecords();
 assertEquals(4, records.size());

 for (CSVRecord record : records) {
 String[] values = record.toArray(new String[record.size()]);
 assertEquals(3, values.length);

 assertEquals("Name1", values[0]);
 assertEquals("Value1", values[1]);
 assertEquals("Value2", values[2]);
 }
 }

 @Test(expected = IOException.class)
 public void testParseInvalidFile() throws IOException {
 File csvFile = new File("src/test/resources/invalid.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 parser.getRecords(); // Throws IOException
 }

 @Test
 public void testParseString() throws IOException {
 String csvString = "Name1,Value1,Value2\nName2,Value3,Value4";
 CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 assertEquals(2, records.size());

 for (CSVRecord record : records) {
 String[] values = record.toArray(new String[record.size()]);
 assertEquals(3, values.length);

 assertEquals("Name1", values[0]);
 assertEquals("Value1", values[1]);
 assertEquals("Value2", values[2]);

 assertEquals("Name2", values[3]);
 assertEquals("Value3", values[4]);
 assertEquals("Value4", values[5]);
 }
 }

 @Test(expected = IOException.class)
 public void testParseInvalidString() throws IOException {
 String csvString = "Name1,Value1,Value2\nName2,Value3,Value4\nInvalidData";
 CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
 parser.getRecords(); // Throws IOException
 }

 @Test
 public void testParseEmptyFile() throws IOException {
 File csvFile = new File("src/test/resources/empty.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 assertEquals(0, records.size());
 }

 @Test
 public void testParseNoHeaderFile() throws IOException {
 File csvFile = new File("src/test/resources/nohdr.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 assertEquals(3, records.size());

 for (CSVRecord record : records) {
 String[] values = record.toArray(new String[record.size()]);
 assertEquals(3, values.length);

 assertEquals("Value1", values[0]);
 assertEquals("Value2", values[1]);
 assertEquals("Value3", values[2]);
 }
 }

 @Test
 public void testParseWithHeader() throws IOException {
 File csvFile = new File("src/test/resources/header.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT.withHeader());
 List<CSVRecord> records = parser.getRecords();
 assertEquals(3, records.size());

 for (CSVRecord record : records) {
 String[] values = record.toArray(new String[record.size()]);
 assertEquals(3, values.length);

 assertEquals("Name1", values[0]);
 assertEquals("Value1", values[1]);
 assertEquals("Value2", values[2]);
 }
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetRecordAfterEndOfFile() throws IOException {
 File csvFile = new File("src/test/resources/sample.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord(); // Throws NoSuchElementException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidFormat() throws IOException {
 String csvString = "Name1,Value1,Value2\nName2,Value3,Value4";
 CSVParser parser = new CSVParser(new StringReader(csvString), null); // Throws IllegalArgumentException
 parser.getRecords(); // Does not reach here
 }
}