import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

 @Test
 public void testParseFile() throws IOException {
 CSVParser parser = CSVParser.parse(new File("testData.csv"), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertNotNull(record);
 assertEquals(5, record.size());
 assertEquals("Column1", record.get("Column1"));
 assertEquals("Column2", record.get(1));
 assertEquals("Column3", record.get(2));
 assertEquals("Column4", record.get(3));
 assertEquals("Column5", record.get(4));
 }

 @Test(expected = IOException.class)
 public void testParseFileWithInvalidFormat() throws IOException {
 CSVParser parser = CSVParser.parse(new File("testData.csv"), new CSVFormat().withHeader("InvalidHeader"));
 assertFalse(parser.hasNext());
 }

 @Test
 public void testParseString() throws IOException {
 String csvData = "Column1,Column2,Column3,Column4,Column5\nData1,Data2,Data3,Data4,Data5";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertNotNull(record);
 assertEquals(5, record.size());
 assertEquals("Data1", record.get("Column1"));
 assertEquals("Data2", record.get(1));
 assertEquals("Data3", record.get(2));
 assertEquals("Data4", record.get(3));
 assertEquals("Data5", record.get(4));
 }

 @Test(expected = IOException.class)
 public void testParseStringWithInvalidFormat() throws IOException {
 String csvData = "Column1,Column2,Column3,Column4,Column5\nData1,Data2,Data3,Data4,Data5";
 CSVParser parser = new CSVParser(new StringReader(csvData), new CSVFormat().withHeader("InvalidHeader"));
 assertFalse(parser.hasNext());
 }

 @Test
 public void testParseURL() throws IOException {
 URL url = new URL("http://example.com/testData.csv");
 CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 assertNotNull(record);
 assertEquals(5, record.size());
 assertEquals("Column1", record.get("Column1"));
 assertEquals("Column2", record.get(1));
 assertEquals("Column3", record.get(2));
 assertEquals("Column4", record.get(3));
 assertEquals("Column5", record.get(4));
 }

 @Test(expected = IOException.class)
 public void testParseURLWithInvalidFormat() throws IOException {
 URL url = new URL("http://example.com/testData.csv");
 CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), new CSVFormat().withHeader("InvalidHeader"));
 assertFalse(parser.hasNext());
 }

 @Test
 public void testParseEmptyFile() throws IOException {
 File emptyCSVFile = new File("emptyCSVFile.csv");
 CSVParser parser = CSVParser.parse(emptyCSVFile, CSVFormat.DEFAULT);
 assertFalse(parser.hasNext());
 }

 @Test
 public void testParseEmptyString() throws IOException {
 String emptyCSVData = "";
 CSVParser parser = new CSVParser(new StringReader(emptyCSVData), CSVFormat.DEFAULT);
 assertFalse(parser.hasNext());
 }

}