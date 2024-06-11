import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecord_Basic() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nAlice,30,New York\nBob,25,Los Angeles";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 assertEquals("Alice", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertEquals("Alice", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 }

 @Test
 public void testCSVRecord_Empty() {
 CSVFormat format = new CSVFormat();
 String csv = "";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 assertNull(record);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse empty CSV");
 }
 }

 @Test
 public void testCSVRecord_NullValues() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nAlice,null,New York\nBob,30,null";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 assertEquals("Alice", record.get("Name"));
 assertNull(record.get("Age"));
 assertEquals("New York", record.get("City"));
 assertNull(record.get(1));
 assertEquals("New York", record.get(2));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecord_MissingColumn() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age\nAlice,30";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 record.get("City"); // should throw an exception
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testCSVRecord_NoMapping() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age\nAlice,30";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 record.get("Name2"); // should throw an exception
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 }

 @Test
 public void testCSVRecord_Inconsistent() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nAlice,30\nBob,30,New York,Los Angeles";

 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 assertFalse(record.isConsistent());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 }

 @Test
 public void testCSVRecord_Iterator() {
 CSVFormat format = new CSVFormat();
 String csv = "Name,Age,City\nAlice,30,New York\nBob,25,Los Angeles";
 List<String> values;
 try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
 CSVRecord record = parser.getRecord();
 values = record.iterator().asList();
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to parse CSV");
 }
 assertEquals(Arrays.asList("Alice", "30", "New York"), values);
 }
}