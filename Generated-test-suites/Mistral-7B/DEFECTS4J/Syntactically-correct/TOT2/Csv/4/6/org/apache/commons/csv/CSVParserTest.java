import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleFile() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Boston\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertNull(record.getComment());
        }
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseInvalidFile() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Boston\nName,City";
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(parser);
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvContent = "Name;Age;City\nJohn;30;New York\nMary;25;Boston\n";
        CSVFormat customFormat = new CSVFormat(';', null);
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertNull(record.getComment());
        }
    }

    @Test
    public void testParseFileWithComment() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York#This is a comment\nMary,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord secondRecord = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("Mary", secondRecord.get(0));
        Assert.assertEquals(25, secondRecord.get(1).intValue());
        Assert.assertEquals("Boston", secondRecord.get(2));
        Assert.assertEquals("#This is a comment", secondRecord.getComment());
    }

    @Test
    public void testParseFileWithSkippedHeader() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.nextRecord(); // Skip header
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals(30, record.get(1).intValue());
        Assert.assertEquals("New York", record.get(2));
        Assert.assertNull(record.getComment());
    }

    @Test
    public void testParseFileWithEmptyLine() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\n\nMary,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals(30, firstRecord.get(1).intValue());
        Assert.assertEquals("New York", firstRecord.get(2));
        Assert.assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("Mary", secondRecord.get(0));
        Assert.assertEquals(25, secondRecord.get(1).intValue());
        Assert.assertEquals("Boston", secondRecord.get(2));
        Assert.assertNull(secondRecord.getComment());
    }

    @Test
    public void testParseFileWithNullValues() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,null,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals(30, firstRecord.get(1).intValue());
        Assert.assertEquals("New York", firstRecord.get(2));
        Assert.assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("Mary", secondRecord.get(0));
        Assert.assertNull(secondRecord.get(1));
        Assert.assertEquals("Boston", secondRecord.get(2));
        Assert.assertNull(secondRecord.getComment());
    }

    @Test
    public void testParseFileWithCommaInValue() throws IOException {
        String csvContent = "Name,Age,City\nJohn,\"Name, Age\",New York\nMary,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, firstRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals("Name, Age", firstRecord.get(1));
        Assert.assertEquals("New York", firstRecord.get(2));
        Assert.assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, secondRecord.headerMap.keySet().toArray(new String[0]));
        Assert.assertEquals("Mary", secondRecord.get(0));
        Assert.assertEquals(25, secondRecord.get(1).intValue());
        Assert.assertEquals("Boston", secondRecord.get(2));
        Assert.assertNull(secondRecord.getComment());
    }

    @Test
    public void testParseFileWithCustomDelimiter() throws IOException {
        String csvContent = "Name|Age|City\nJohn|30|New York\nMary|25|Boston";
        CSVFormat customFormat = new CSVFormat('|');
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertNull(record.getComment());
        }
    }

    @Test
    public void testParseFileWithMissingValues() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,\nMary,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.headerMap.keySet().toArray(new String[0]));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1) == null ? null : record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertNull(record.getComment());
        }
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidFormat() throws IOException {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Boston\nName:City";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        parser.getRecords();
    }
}