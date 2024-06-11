import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

 @Test
 public void testParseFile() throws IOException {
 File csvFile = new File("testCSVFile.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Test if expected number of records are present in the file
 assertEquals(5, records.size());

 // Test if the values in the first record are as expected
 CSVRecord firstRecord = records.get(0);
 String[] expectedFirstRecordValues = new String[]{"Name", "Age", "City"};
 assertArrayEquals(expectedFirstRecordValues, firstRecord.toArray());
 }

 @Test(expected = IOException.class)
 public void testInvalidFileFormat() throws IOException {
 File csvFile = new File("testInvalidFileFormat.csv");
 CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
 parser.getRecords(); // Should throw an exception as the file format is invalid
 }

 @Test
 public void testParseString() throws IOException {
 String csvString = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago";
 CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Test if expected number of records are present in the string
 assertEquals(2, records.size());

 // Test if the values in the first record are as expected
 CSVRecord firstRecord = records.get(0);
 String[] expectedFirstRecordValues = new String[]{"Name", "Age", "City"};
 assertArrayEquals(expectedFirstRecordValues, firstRecord.toArray());

 // Test if the values in the second record are as expected
 CSVRecord secondRecord = records.get(1);
 String[] expectedSecondRecordValues = new String[]{"John", "25", "New York"};
 assertArrayEquals(expectedSecondRecordValues, secondRecord.toArray());
 }

 @Test(expected = NoSuchElementException.class)
 public void testEndOfStream() throws IOException {
 CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 parser.getRecord(); // Should throw an exception as the end of the stream has been reached
 }

 @Test
 public void testParseCustomFormat() throws IOException {
 String csvString = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago";
 CSVFormat customFormat = new CSVFormat().withHeader("Name", "Age", "City");
 CSVParser parser = new CSVParser(new StringReader(csvString), customFormat);
 List<CSVRecord> records = parser.getRecords();

 // Test if expected number of records are present in the string
 assertEquals(2, records.size());

 // Test if the values in the first record are as expected
 CSVRecord firstRecord = records.get(0);
 String[] expectedFirstRecordValues = new String[]{"Name", "Age", "City"};
 assertArrayEquals(expectedFirstRecordValues, firstRecord.toArray());

 // Test if the values in the second record are as expected
 CSVRecord secondRecord = records.get(1);
 String[] expectedSecondRecordValues = new String[]{"John", "25", "New York"};
 assertArrayEquals(expectedSecondRecordValues, secondRecord.toArray());
 }

}