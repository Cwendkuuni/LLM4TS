import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

 private static final String TEST_FILE = "testData.csv";
 private static final String TEST_STRING = "a,b\nc,d";

 @Test
 public void testParseFile() throws IOException {
 CSVParser parser = CSVParser.parse(new File(TEST_FILE), CSVFormat.RFC4180);
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record;

 // Test empty file
 Assert.assertEquals(0, records.size());

 // Test valid file
 record = parser.nextRecord();
 Assert.assertNotNull(record);
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("a", record.get(0));
 Assert.assertEquals("b", record.get(1));

 // Test multi-line values
 record = parser.nextRecord();
 Assert.assertNotNull(record);
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("c", record.get(0));
 Assert.assertEquals("d", record.get(1));

 // Test empty line
 record = parser.nextRecord();
 Assert.assertNull(record);

 // Test null values
 record = parser.nextRecord();
 Assert.assertNull(record);

 // Test comment
 record = parser.nextRecord();
 Assert.assertNotNull(record);
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("", record.getComment());
 Assert.assertEquals("", record.get(0));
 Assert.assertEquals("", record.get(1));

 // Test invalid format
 parser = new CSVParser(new StringReader("a,b\nc,d\ne"), CSVFormat.RFC4180);
 record = parser.nextRecord();
 Assert.assertNull(record);

 // Test custom format
 CSVFormat customFormat = new CSVFormatBuilder() //
 .setHeader("Name", "Age") //
 .setTrimLeadingSpace(true) //
 .build();
 parser = new CSVParser(new StringReader(TEST_STRING), customFormat);
 record = parser.nextRecord();
 Assert.assertNotNull(record);
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("a", record.get("Name"));
 Assert.assertEquals("b", record.get("Age"));

 // Test parse record wise into memory
 parser = new CSVParser(new StringReader(TEST_STRING), CSVFormat.RFC4180);
 records = parser.getRecords();
 Assert.assertEquals(1, records.size());
 record = records.get(0);
 Assert.assertNotNull(record);
 Assert.assertEquals(2, record.size());
 Assert.assertEquals("a", record.get(0));
 Assert.assertEquals("b", record.get(1));
 }

 @Test(expected = NoSuchElementException.class)
 public void testParseFileEmptyIterator() throws IOException {
 CSVParser parser = CSVParser.parse(new File(TEST_FILE), CSVFormat.RFC4180);
 Iterator<CSVRecord> iterator = parser.iterator();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test(expected = IOException.class)
 public void testParseInvalidFile() throws IOException {
 CSVParser parser = CSVParser.parse(new File("non-existent-file.csv"), CSVFormat.RFC4180);
 List<CSVRecord> records = parser.getRecords();
 Assert.assertEquals(0, records.size());
 parser.close();
 }

}