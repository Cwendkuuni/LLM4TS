import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

 // Test method for parse(File, CSVFormat)
 @Test
 public void testParseFile() throws IOException {
 File file = new File("test.csv");
 CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the correct number of records are returned
 assertEquals(5, records.size());

 // Check if the values in the first record are correct
 CSVRecord firstRecord = records.get(0);
 Assert.assertEquals(5, firstRecord.size());
 Assert.assertEquals("Header1", firstRecord.get(0));
 Assert.assertEquals("Header2", firstRecord.get(1));
 Assert.assertEquals("Header3", firstRecord.get(2));
 Assert.assertEquals("Header4", firstRecord.get(3));
 Assert.assertEquals("Header5", firstRecord.get(4));

 // Check if the values in the last record are correct
 CSVRecord lastRecord = records.get(4);
 Assert.assertEquals(5, lastRecord.size());
 Assert.assertEquals("Value1_5", lastRecord.get(0));
 Assert.assertEquals("Value2_5", lastRecord.get(1));
 Assert.assertEquals("Value3_5", lastRecord.get(2));
 Assert.assertEquals("Value4_5", lastRecord.get(3));
 Assert.assertEquals("Value5_5", lastRecord.get(4));
 }

 // Test method for parse(String, CSVFormat)
 @Test
 public void testParseString() throws IOException {
 String csvData = "Header1,Header2,Header3,Header4,Header5\n" +
 "Value1_1,Value2_1,Value3_1,Value4_1,Value5_1\n" +
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5";
 CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the correct number of records are returned
 assertEquals(5, records.size());

 // Check if the values in the first record are correct
 CSVRecord firstRecord = records.get(0);
 Assert.assertEquals(5, firstRecord.size());
 Assert.assertEquals("Header1", firstRecord.get(0));
 Assert.assertEquals("Header2", firstRecord.get(1));
 Assert.assertEquals("Header3", firstRecord.get(2));
 Assert.assertEquals("Header4", firstRecord.get(3));
 Assert.assertEquals("Header5", firstRecord.get(4));

 // Check if the values in the last record are correct
 CSVRecord lastRecord = records.get(4);
 Assert.assertEquals(5, lastRecord.size());
 Assert.assertEquals("Value1_5", lastRecord.get(0));
 Assert.assertEquals("Value2_5", lastRecord.get(1));
 Assert.assertEquals("Value3_5", lastRecord.get(2));
 Assert.assertEquals("Value4_5", lastRecord.get(3));
 Assert.assertEquals("Value5_5", lastRecord.get(4));
 }

 // Test method for parse(URL, Charset, CSVFormat)
 @Test
 public void testParseURL() throws IOException {
 String urlString = "https://example.com/test.csv";
 URL url = new URL(urlString);
 Charset charset = Charset.forName("UTF-8");
 CSVParser parser = CSVParser.parse(url, charset, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the correct number of records are returned
 assertEquals(5, records.size());

 // Check if the values in the first record are correct
 CSVRecord firstRecord = records.get(0);
 Assert.assertEquals(5, firstRecord.size());
 Assert.assertEquals("Header1", firstRecord.get(0));
 Assert.assertEquals("Header2", firstRecord.get(1));
 Assert.assertEquals("Header3", firstRecord.get(2));
 Assert.assertEquals("Header4", firstRecord.get(3));
 Assert.assertEquals("Header5", firstRecord.get(4));

 // Check if the values in the last record are correct
 CSVRecord lastRecord = records.get(4);
 Assert.assertEquals(5, lastRecord.size());
 Assert.assertEquals("Value1_5", lastRecord.get(0));
 Assert.assertEquals("Value2_5", lastRecord.get(1));
 Assert.assertEquals("Value3_5", lastRecord.get(2));
 Assert.assertEquals("Value4_5", lastRecord.get(3));
 Assert.assertEquals("Value5_5", lastRecord.get(4));
 }

 // Test method for getRecords()
 @Test
 public void testGetRecords() throws IOException {
 String csvData = "Header1,Header2,Header3,Header4,Header5\n" +
 "Value1_1,Value2_1,Value3_1,Value4_1,Value5_1\n" +
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the correct number of records are returned
 assertEquals(5, records.size());

 // Check if the values in the first record are correct
 CSVRecord firstRecord = records.get(0);
 Assert.assertEquals(5, firstRecord.size());
 Assert.assertEquals("Header1", firstRecord.get(0));
 Assert.assertEquals("Header2", firstRecord.get(1));
 Assert.assertEquals("Header3", firstRecord.get(2));
 Assert.assertEquals("Header4", firstRecord.get(3));
 Assert.assertEquals("Header5", firstRecord.get(4));

 // Check if the values in the last record are correct
 CSVRecord lastRecord = records.get(4);
 Assert.assertEquals(5, lastRecord.size());
 Assert.assertEquals("Value1_5", lastRecord.get(0));
 Assert.assertEquals("Value2_5", lastRecord.get(1));
 Assert.assertEquals("Value3_5", lastRecord.get(2));
 Assert.assertEquals("Value4_5", lastRecord.get(3));
 Assert.assertEquals("Value5_5", lastRecord.get(4));
 }

 // Test method for parse(Reader) using CSVFormat.parse(Reader)
 @Test
 public void testParseReaderWithCSVFormatParse() throws IOException {
 String csvData = "Header1,Header2,Header3,Header4,Header5\n" +
 "Value1_1,Value2_1,Value3_1,Value4_1,Value5_1\n" +
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5";
 Reader reader = new StringReader(csvData);
 CSVParser parser = CSVFormat.DEFAULT.parse(reader);
 List<CSVRecord> records = parser.getRecords();

 // Check if the correct number of records are returned
 assertEquals(5, records.size());

 // Check if the values in the first record are correct
 CSVRecord firstRecord = records.get(0);
 Assert.assertEquals(5, firstRecord.size());
 Assert.assertEquals("Header1", firstRecord.get(0));
 Assert.assertEquals("Header2", firstRecord.get(1));
 Assert.assertEquals("Header3", firstRecord.get(2));
 Assert.assertEquals("Header4", firstRecord.get(3));
 Assert.assertEquals("Header5", firstRecord.get(4));

 // Check if the values in the last record are correct
 CSVRecord lastRecord = records.get(4);
 Assert.assertEquals(5, lastRecord.size());
 Assert.assertEquals("Value1_5", lastRecord.get(0));
 Assert.assertEquals("Value2_5", lastRecord.get(1));
 Assert.assertEquals("Value3_5", lastRecord.get(2));
 Assert.assertEquals("Value4_5", lastRecord.get(3));
 Assert.assertEquals("Value5_5", lastRecord.get(4));
 }

 // Test for empty CSV file
 @Test
 public void testEmptyCSVFile() throws IOException {
 File emptyCSVFile = new File("empty.csv");
 CSVParser parser = CSVParser.parse(emptyCSVFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the list is empty
 assertTrue(records.isEmpty());
 }

 // Test for null input
 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() throws IOException {
 String nullCSVData = null;
 Reader reader = new StringReader(nullCSVData);
 CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
 }

 // Test for invalid format
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() throws IOException {
 String csvData = "Header1,Header2,Header3,Header4,Header5\n" +
 "Value1_1,Value2_1,Value3_1,Value4_1,Value5_1\n" +
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5\n" +
 "Value1_6,Value2_6,Value3_6,Value4_6,Value5_6,Value6_6"; // extra value
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // This should not reach here as an IllegalArgumentException should be thrown
 Assert.assertEquals(6, records.size());
 }

 // Test for missing header
 @Test
 public void testMissingHeader() throws IOException {
 String csvData = "Value1_1,Value2_1,Value3_1,Value4_1,Value5_1\n" +
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check if the list is not empty
 assertFalse(records.isEmpty());

 // Check if the header map is empty
 Map<String, Integer> headerMap = parser.getHeaderMap();
 assertTrue(headerMap == null || headerMap.isEmpty());
 }

 // Test for invalid character in CSV data
 @Test(expected = IOException.class)
 public void testInvalidCharacterInCSVData() throws IOException {
 String csvData = "Header1,Header2,Header3,Header4,Header5\n" +
 "Value1_1,\"Value2_1,Value3_1,Value4_1,Value5_1\n" + // invalid quote in value2_1
 "Value1_2,Value2_2,Value3_2,Value4_2,Value5_2\n" +
 "Value1_3,Value2_3,Value3_3,Value4_3,Value5_3\n" +
 "Value1_4,Value2_4,Value3_4,Value4_4,Value5_4\n" +
 "Value1_5,Value2_5,Value3_5,Value4_5,Value5_5";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // This should not reach here as an IOException should be thrown
 Assert.assertEquals(5, records.size());
 }
}