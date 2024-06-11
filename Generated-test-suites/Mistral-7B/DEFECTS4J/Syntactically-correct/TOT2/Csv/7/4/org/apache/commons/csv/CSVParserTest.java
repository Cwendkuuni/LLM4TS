import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

 @Test
 public void testParseFileWithValidFormat() throws IOException {
 File inputFile = new File("input/valid.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);

 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertEquals(3, record.size());
 Assert.assertEquals("Column1", record.get(0));
 Assert.assertEquals("Column2", record.get(1));
 Assert.assertEquals("Column3", record.get(2));

 // Checking header map
 Map<String, Integer> headerMap = parser.getHeaderMap();
 Assert.assertEquals(3, headerMap.size());
 Assert.assertEquals("Column1", headerMap.get("Column1"));
 Assert.assertEquals("Column2", headerMap.get("Column2"));
 Assert.assertEquals("Column3", headerMap.get("Column3"));
 }

 @Test
 public void testParseFileWithCustomFormat() throws IOException {
 File inputFile = new File("input/custom.csv");
 CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames("Header1", "Header2").build();
 CSVParser parser = CSVParser.parse(inputFile, customFormat);

 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("Header1Value1", record.get(0));
 Assert.assertEquals("Header2Value1", record.get(1));

 // Checking header map
 Map<String, Integer> headerMap = parser.getHeaderMap();
 Assert.assertEquals(2, headerMap.size());
 Assert.assertEquals("Header1", headerMap.get("Header1"));
 Assert.assertEquals("Header2", headerMap.get("Header2"));
 }

 @Test
 public void testParseInvalidFile() throws IOException {
 File inputFile = new File("input/invalid.csv");
 assertThrows(IOException.class, () -> CSVParser.parse(inputFile, CSVFormat.DEFAULT));
 }

 @Test
 public void testParseEmptyFile() throws IOException {
 File inputFile = new File("input/empty.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 Assert.assertTrue(records.isEmpty());
 }

 @Test
 public void testParseFileWithNullData() {
 File inputFile = new File("input/null.csv");
 assertThrows(FileNotFoundException.class, () -> CSVParser.parse(inputFile, CSVFormat.DEFAULT));
 }

 @Test
 public void testParseFileWithMissingFormat() throws IOException {
 File inputFile = new File("input/missingFormat.csv");
 assertThrows(IllegalArgumentException.class, () -> CSVParser.parse(inputFile, null));
 }

 @Test
 public void testParseFileWithTruncatedData() throws IOException {
 File inputFile = new File("input/truncated.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertTrue(record.size() > 0);
 Assert.assertFalse(record.size() == 3); // This is a truncated file
 }

 @Test
 public void testParseFileWithCustomHeaderNames() throws IOException {
 File inputFile = new File("input/customHeaderNames.csv");
 CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames("Header1", "Header2").build();
 CSVParser parser = CSVParser.parse(inputFile, customFormat);

 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertEquals("Header1Value1", record.get(0));
 Assert.assertEquals("Header2Value1", record.get(1));
 }

 @Test
 public void testParseFileWithEmptyLine() throws IOException {
 File inputFile = new File("input/emptyLine.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertTrue(record.size() > 0);
 Assert.assertEquals("", record.get(0));
 }

 @Test
 public void testParseFileWithComment() throws IOException {
 File inputFile = new File("input/comment.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertTrue(record.size() > 0);
 Assert.assertEquals("Column1", record.get(0));
 Assert.assertEquals("Column2", record.get(1));
 Assert.assertEquals("#This is a comment", record.getComment());
 }

 @Test
 public void testParseFileWithNullStringValues() throws IOException {
 File inputFile = new File("input/nullStringValues.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertEquals(2, record.size());
 Assert.assertNull(record.get(0));
 Assert.assertEquals("", record.get(1));
 }

 @Test
 public void testParseFileWithEmptyStringValues() throws IOException {
 File inputFile = new File("input/emptyStringValues.csv");
 CSVParser parser = CSVParser.parse(inputFile, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Checking record values
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("", record.get(0));
 Assert.assertEquals("", record.get(1));
 }

 @Test
 public void testParseFileWithInvalidFormat() throws IOException {
 File inputFile = new File("input/invalidFormat.csv");
 assertThrows(IOException.class, () -> CSVParser.parse(inputFile, CSVFormat.DEFAULT));
 }
}