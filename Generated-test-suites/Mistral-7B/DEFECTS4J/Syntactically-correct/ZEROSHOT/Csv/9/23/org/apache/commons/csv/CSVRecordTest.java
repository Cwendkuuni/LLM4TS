import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

 private CSVRecord record;
 private CSVParser parser;

 @Before
 public void setup() throws IOException {
 String csv = "name,age,comment\nJohn Doe,30,test\nJane Doe,25,test2\n";
 InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csv.getBytes()));
 parser = new CSVParser(isr, CSVFormat.DEFAULT.withFirstRecordAsHeader());
 record = parser.nextRecord();
 }

 @Test
 public void testGet() {
 Assert.assertEquals("John Doe", record.get(0));
 Assert.assertEquals("30", record.get(1));
 Assert.assertEquals("test", record.get(2));
 }

 @Test
 public void testGetEnum() {
 Assert.assertEquals("John Doe", record.get(CSVRecordTest.Name.NAME.toString()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidName() {
 record.get("invalidName");
 }

 @Test
 public void testGetInvalidIndex() {
 try {
 record.get(-1);
 } catch (ArrayIndexOutOfBoundsException e) {
 Assert.assertEquals(-1, e.getMessage().codePointAt(0) - 48);
 }
 }

 @Test
 public void testGetComment() {
 Assert.assertEquals("test", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 Assert.assertTrue(record.isMapped("name"));
 Assert.assertTrue(record.isMapped("age"));
 Assert.assertTrue(record.isMapped("comment"));
 }

 @Test
 public void testIsSet() {
 Assert.assertTrue(record.isSet("name"));
 Assert.assertTrue(record.isSet("age"));
 Assert.assertTrue(record.isSet("comment"));
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, record.size());
 }

 @Test
 public void testToMap() {
 Map<String, String> map = record.toMap();
 Assert.assertEquals("John Doe", map.get("name"));
 Assert.assertEquals("30", map.get("age"));
 Assert.assertEquals("test", map.get("comment"));
 }

 @Test
 public void testToString() {
 List<String> expected = Arrays.asList("John Doe", "30", "test");
 List<String> actual = Arrays.asList(record.toString().split(","));
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testValues() {
 String[] expected = new String[]{"John Doe", "30", "test"};
 String[] actual = record.values();
 Assert.assertArrayEquals(expected, actual);
 }
}

enum Name {
 NAME
}