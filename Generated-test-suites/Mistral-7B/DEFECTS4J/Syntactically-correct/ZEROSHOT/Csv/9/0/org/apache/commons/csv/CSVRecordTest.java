import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_DATA = "name,age,city\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";

 @Test
 public void testCSVRecordCreation() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = csvParser.getRecord();

 assertEquals(1, csvRecord.size());
 assertEquals("name", csvRecord.mapping.get("name"));
 assertEquals("age", csvRecord.mapping.get("age"));
 assertEquals("city", csvRecord.mapping.get("city"));
 assertEquals("John Doe", csvRecord.get("name"));
 assertEquals("30", csvRecord.get("age"));
 assertEquals("New York", csvRecord.get("city"));
 assertEquals("John Doe", csvRecord.get(0));
 assertEquals("30", csvRecord.get(1));
 assertEquals("New York", csvRecord.get(2));
 assertEquals(null, csvRecord.getComment());
 assertEquals(0, csvRecord.getRecordNumber());
 assertTrue(csvRecord.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecordWithoutHeaderMapping() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
 csvParser.getRecordCollection().add(csvRecord);
 csvParser.getRecord(); // consume the first record
 csvRecord.get("name"); // should throw an exception
 }

 @Test
 public void testCSVRecordGetNullValue() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVFormat csvFormatBuilder = new CSVFormat(' ');
 csvFormatBuilder.setNullString("null");
 CSVParser csvParser = csvFormatBuilder.parse(CSV_DATA);
 CSVRecord csvRecord = csvParser.getRecord();
 assertEquals(csvRecord.get(2), csvRecord.get("city"));
 assertEquals("New York", csvRecord.get("city"));
 assertEquals("null", csvRecord.get("city"), "The value for city should be nullString but is not.");
 }

 @Test
 public void testCSVRecordWithInconsistentData() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = new CSVRecord(new String[]{"name", "age", "city", "extra"}, null, null, 0);
 csvParser.getRecordCollection().add(csvRecord);
 csvParser.getRecord(); // consume the first record
 csvRecord.get("name"); // should throw an exception
 }

 @Test
 public void testCSVRecordIterable() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = csvParser.getRecord();
 Iterator<String> iterator = csvRecord.iterator();
 List<String> values = Arrays.asList("name", "age", "city");
 int i = 0;
 while (iterator.hasNext()) {
 String value = iterator.next();
 assertEquals(values.get(i++), value);
 }
 }

 @Test
 public void testCSVRecordList() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = csvParser.getRecord();
 List<String> values = csvRecord.toList();
 List<String> expectedValues = Arrays.asList("name", "age", "city");
 assertEquals(expectedValues, values);
 }

 @Test
 public void testCSVRecordMap() throws Exception {
 CSVFormat csvFormat = new CSVFormat();
 CSVParser csvParser = csvFormat.parse(CSV_DATA);
 CSVRecord csvRecord = csvParser.getRecord();
 Map<String, String> values = csvRecord.toMap();
 Map<String, String> expectedValues = new HashMap<>();
 expectedValues.put("name", "John Doe");
 expectedValues.put("age", "30");
 expectedValues.put("city", "New York");
 assertEquals(expectedValues, values);
 }

}