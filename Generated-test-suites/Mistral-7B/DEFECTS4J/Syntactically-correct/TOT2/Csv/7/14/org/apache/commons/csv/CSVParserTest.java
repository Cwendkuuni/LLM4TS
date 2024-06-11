import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

 private CSVParser parser;
 private CSVFormat format;

 @Before
 public void setUp() throws IOException {
 format = new CSVFormatBuilder() // Build a CSVFormat with default settings
 .setHeader("Name,Age,City")
 .build();
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,25,New York\nBob,30,Chicago"), format);
 }

 @Test
 public void testParseBasic() throws IOException {
 CSVRecord record;
 List<CSVRecord> records = new ArrayList<>();

 // Test parsing basic record
 record = parser.nextRecord();
 Assert.assertEquals("Alice", record.get("Name"));
 Assert.assertEquals(25, record.getInteger("Age"));
 Assert.assertEquals("New York", record.get("City"));
 records.add(record);

 // Test parsing multiple records
 record = parser.nextRecord();
 Assert.assertEquals("Bob", record.get("Name"));
 Assert.assertEquals(30, record.getInteger("Age"));
 Assert.assertEquals("Chicago", record.get("City"));
 records.add(record);

 // Test end of file
 record = parser.nextRecord();
 Assert.assertNull(record);

 // Test header map
 Map<String, Integer> headerMap = parser.getHeaderMap();
 Assert.assertEquals(3, headerMap.size());
 Assert.assertEquals("Name", headerMap.get(0).toString());
 Assert.assertEquals("Age", headerMap.get(1).toString());
 Assert.assertEquals("City", headerMap.get(2).toString());

 // Test record number
 assertEquals(2, parser.getRecordNumber());

 // Test iterator
 Iterator<CSVRecord> iterator = parser.iterator();
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(records.get(0), iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(records.get(1), iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testParseInvalidFormat() throws IOException {
 parser = new CSVParser(new StringReader("Name\nAge,City"), format);
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord(); // Throws NoSuchElementException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIllegalFormat() throws IOException {
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,25,New York\nBob,30,Chicago,X"), format);
 parser.nextRecord();
 parser.nextRecord();
 }

 @Test(expected = IOException.class)
 public void testParseIOException() throws IOException {
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,25,New York\nBob,30,Chicago"), format);
 parser.getReader().close(); // Close reader to simulate I/O error
 parser.nextRecord(); // Throws IOException
 }

 @Test(expected = NoSuchElementException.class)
 public void testParseEmptyFile() throws IOException {
 parser = new CSVParser(new StringReader(""), format);
 parser.nextRecord(); // Throws NoSuchElementException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullFormat() throws IOException {
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,25,New York\nBob,30,Chicago"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullReader() throws IOException {
 parser = null;
 try {
 new CSVParser(null, format);
 } catch (IllegalArgumentException e) {
 // Test exception message
 assertEquals("reader may not be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullFormatAndReader() throws IOException {
 parser = null;
 try {
 new CSVParser(null, null);
 } catch (IllegalArgumentException e) {
 // Test exception message
 assertEquals("reader or format may not be null", e.getMessage());
 }
 }

 @Test
 public void testParseComment() throws IOException {
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,25,New York#This is a comment\nBob,30,Chicago"), format);
 CSVRecord record = parser.nextRecord();
 Assert.assertEquals("Alice", record.get("Name"));
 Assert.assertEquals(25, record.getInteger("Age"));
 Assert.assertEquals("New York", record.get("City"));
 Assert.assertEquals("This is a comment", record.getComment());
 record = parser.nextRecord();
 Assert.assertEquals("Bob", record.get("Name"));
 Assert.assertEquals(30, record.getInteger("Age"));
 Assert.assertEquals("Chicago", record.get("City"));
 Assert.assertNull(record.getComment());
 }
}