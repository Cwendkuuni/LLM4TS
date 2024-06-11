import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGetByEnum() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 Enum<String> header1 = Enum.valueOf("Header1", String.class);
 String value1 = record.get(header1);
 assertEquals("Value1A", value1);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordGetByIndex() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 String value1 = record.get(0);
 assertEquals("Value1A", value1);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordGetByName() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 String header1 = "Header1";
 String value1 = record.get(header1);
 assertEquals("Value1A", value1);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetByNameNotFound() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 String header5 = "Header5";
 record.get(header5);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordGetByIndexOutOfBounds() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 record.get(3);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4,Comment\n" +
 "Value1A,Value1B,Value1C,Value1D,Some comment\n" +
 "Value2A,Value2B,Value2C,Value2D,Another comment";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 String comment = record.getComment();
 assertEquals("Some comment", comment);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4,RecordNumber\n" +
 "Value1A,Value1B,Value1C,Value1D,1\n" +
 "Value2A,Value2B,Value2C,Value2D,2";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 long recordNumber = record.getRecordNumber();
 assertEquals(1, recordNumber);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isConsistent());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsNotConsistent() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3\n" +
 "Value1A,Value1B,Value1C\n" +
 "Value2A,Value2B,Value2C";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertFalse(record.isConsistent());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isMapped("Header1"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsNotMapped() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertFalse(record.isMapped("Header5"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isSet("Header1"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIsNotSet() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 assertFalse(record.isSet("Header5"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordIterator() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 Iterator<String> iterator = record.iterator();
 assertEquals(record.size(), iterator.size());
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCSVRecordPutInMap() {
 CSVFormat format = new CSVFormat(',').withNullString("NA");
 String csv = "Header1,Header2,Header3,Header4\n" +
 "Value1A,Value1B,Value1C,Value1D\n" +
 "Value2A,Value2B,Value2C,Value2D";

 Map<String, String> map = new HashMap<>();
 try (CSVParser parser = format.parse(new StringReader(csv))) {
 CSVRecord record = parser.nextRecord();
 map.putAll(record.putIn(map));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }

 assertEquals(record.size(), map.size());
 for (int i = 0; i < record.size(); i++) {
 assertEquals(record.get(i), map.get(String.valueOf(i)));
 }
 }
}