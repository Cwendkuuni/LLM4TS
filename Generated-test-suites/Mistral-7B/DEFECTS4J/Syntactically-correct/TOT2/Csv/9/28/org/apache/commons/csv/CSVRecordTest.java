import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

 @Test
 public void testCSVRecord_BasicUsage() {
 String csv = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = format.newParser(new StringReader(csv));

 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(2, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertEquals("Age", record.get(1).getClass().getSimpleName());
 Assertions.assertEquals("30", record.get(1));
 Assertions.assertEquals("New York", record.get(2));
 }

 @Test
 public void testCSVRecord_EmptyRecord() {
 String csv = "";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = format.newParser(new StringReader(csv));

 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 Assertions.assertNotNull(e);
 }

 Assertions.assertEquals(0, record.size());
 }

 @Test
 public void testCSVRecord_MissingColumn() {
 String csv = "Name,Age\nAlice,30\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = format.newParser(new StringReader(csv));

 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(2, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertNull(record.get(1));
 }

 @Test
 public void testCSVRecord_InvalidColumn() {
 String csv = "Name,Age,City\nAlice,30,\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = format.newParser(new StringReader(csv));

 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertEquals("30", record.get(1));
 Assertions.assertNull(record.get(2));
 }

 @Test
 public void testCSVRecord_NullMapping() {
 String csv = "Name,Age,City\nAlice,30,New York\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = new CSVRecord(new String[0], null, null, 0);

 Assertions.assertThrows(IllegalStateException.class, () -> record.get("Name"));
 }

 @Test
 public void testCSVRecord_InconsistentSize() {
 String csv = "Name,Age,City\nAlice,30,New York\nBob,25\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = new CSVRecord(new String[]{"Alice", "New York"}, null, null, 0);

 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get("Age"));
 }

 @Test
 public void testCSVRecord_EmptyValues() {
 String csv = "Name,Age,City\nAlice,\\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertNull(record.get(1));
 Assertions.assertEquals("Chicago", record.get(2));
 }

 @Test
 public void testCSVRecord_MultipleNullValues() {
 String csv = "Name,Age,City\nAlice,30,\nBob,null,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertNull(record.get(1));
 Assertions.assertEquals("Chicago", record.get(2));
 }

 @Test
 public void testCSVRecord_NullComment() {
 String csv = "Name,Age,City\nAlice,30,";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = new CSVRecord(new String[3], new HashMap<String, Integer>(), null, 0);

 Assertions.assertEquals(3, record.size());
 Assertions.assertNull(record.getComment());
 }

 @Test
 public void testCSVRecord_NullRecordNumber() {
 String csv = "Name,Age,City\nAlice,30,\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = new CSVRecord(new String[3], new HashMap<String, Integer>(), null, -1);

 Assertions.assertEquals(3, record.size());
 Assertions.assertNull(record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_EmptyStringValues() {
 String csv = "Name,Age,City\nAlice,\\nBob, ,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertEquals("", record.get(1));
 Assertions.assertEquals("Chicago", record.get(2));
 }

 @Test
 public void testCSVRecord_EmptyStringHeaderMapping() {
 String csv = "Name,Age,City\nAlice,30,\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT.withHeaderMapping(Collections.emptyMap());
 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get("Name"));
 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get("Age"));
 Assertions.assertThrows(IllegalArgumentException.class, () -> record.get("City"));
 }

 @Test
 public void testCSVRecord_SingleStringHeaderMapping() {
 String csv = "Name,Age,City\nAlice,30,\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT.withHeaderMapping(Map.of("Name", 0, "Age", 1, "City", 2));
 CSVRecord record = null;
 try {
 record = parser.nextRecord();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Assertions.assertEquals(3, record.size());
 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertEquals("30", record.get(1));
 Assertions.assertEquals("Chicago", record.get(2));
 }

 @Test
 public void testCSVRecord_IntegerHeaderMapping() {
 String csv = "Name,Age,City\nAlice,30,\nBob,25,Chicago\n";
 CSVFormat format = CSVFormat.DEFAULT.withHeaderMapping(Map.of("Name", 0, "Age", 1, "City", 2));
 CSVRecord record = new CSVRecord(new String[3], new HashMap<String, Integer>(), null, 0);

 record.mapping = Map.of("Name", 0, "Age", 1, "City", 2);
 record.values = new String[]{"Alice", "30", "Chicago"};

 Assertions.assertEquals("Alice", record.get(0));
 Assertions.assertEquals("30", record.get(1));
 Assertions.assertEquals("Chicago", record.get(2));
 }

}