import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

 private CSVParser parser;
 private CSVFormat format;

 @Before
 public void setUp() throws Exception {
 format = CSVFormat.DEFAULT;
 parser = CSVParser.parse(new StringReader("name,age\nTom,25\nJerry,30\nMike,35"), format);
 }

 @Test
 public void testParseRecord() throws Exception {
 CSVRecord record = parser.nextRecord();
 assertNotNull(record);
 assertNotNull(record.getValues());
 assertEquals("name", record.getHeaderMap().get("name"));
 assertEquals("Tom", record.getValues()[0]);
 assertEquals("age", record.getHeaderMap().get("age"));
 assertEquals(Integer.valueOf(25), record.getValues()[1]);
 }

 @Test(expected = NoSuchElementException.class)
 public void testNoMoreRecords() throws Exception {
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord();
 parser.nextRecord(); // should throw NoSuchElementException
 }

 @Test(expected = IOException.class)
 public void testInvalidFormat() throws Exception {
 StringReader reader = new StringReader("name,age\nTom,25\nJerry,30\nMike,35\nINVALID_TOKEN");
 try {
 CSVParser parser = new CSVParser(reader, format);
 parser.nextRecord();
 fail("Should throw IOException on invalid format");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFormat() throws Exception {
 try {
 CSVParser parser = new CSVParser(new StringReader("name,age\nTom,25\nJerry,30\nMike,35"), null);
 fail("Should throw IllegalArgumentException on null format");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testNullReader() throws Exception {
 try {
 CSVParser parser = new CSVParser(null, format);
 fail("Should throw IOException on null reader");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testEmptyFile() throws Exception {
 parser = CSVParser.parse(new StringReader(""), format);
 assertTrue(parser.isEmpty());
 parser.close();
 }

 @Test(expected = IOException.class)
 public void testMissingDelimiter() throws Exception {
 StringReader reader = new StringReader("name,ageTom25Jerry30Mike35");
 try {
 CSVParser parser = new CSVParser(reader, format);
 parser.nextRecord();
 fail("Should throw IOException on missing delimiter");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testMissingValue() throws Exception {
 StringReader reader = new StringReader("name,age\nTom,25\nJerry,30\nMike");
 try {
 CSVParser parser = new CSVParser(reader, format);
 parser.nextRecord();
 fail("Should throw IOException on missing value");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testInvalidValue() throws Exception {
 StringReader reader = new StringReader("name,age\nTom,25\nJerry,30\nMike,InvalidValue");
 try {
 CSVParser parser = new CSVParser(reader, format);
 parser.nextRecord();
 fail("Should throw IOException on invalid value");
 } catch (IOException e) {
 // expected
 }
 }
}