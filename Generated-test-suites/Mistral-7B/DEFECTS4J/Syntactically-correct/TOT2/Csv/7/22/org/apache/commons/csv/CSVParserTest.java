import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvContent = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("name", firstRecord.getNamedValue("name"));
        Assert.assertEquals("John Doe", firstRecord.getNamedValue("name"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", firstRecord.getNamedValue("age"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(1));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("name", secondRecord.getNamedValue("name"));
        Assert.assertEquals("Jane Doe", secondRecord.getNamedValue("name"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", secondRecord.getNamedValue("age"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(1));
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseCSVWithHeader() throws IOException {
        String csvContent = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("name", firstRecord.getNamedValue("name"));
        Assert.assertEquals("John Doe", firstRecord.getNamedValue("name"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", firstRecord.getNamedValue("age"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(1));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("name", secondRecord.getNamedValue("name"));
        Assert.assertEquals("Jane Doe", secondRecord.getNamedValue("name"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", secondRecord.getNamedValue("age"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(1));

        Assert.assertEquals("name", parser.getHeaderMap().get("name"));
        Assert.assertEquals(0, parser.getHeaderMap().get("name").intValue());
        Assert.assertEquals("age", parser.getHeaderMap().get("age"));
        Assert.assertEquals(1, parser.getHeaderMap().get("age").intValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyRecord() throws IOException {
        String csvContent = "name\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.nextRecord();
        parser.nextRecord(); // should fail here
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvContent = "name,age\nJohn Doe,30a";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.nextRecord();
        parser.nextRecord(); // should fail here
    }

    @Test
    public void testParseCSVWithComment() throws IOException {
        String csvContent = "name,age\nJohn Doe,30# This is a comment\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("name", firstRecord.getNamedValue("name"));
        Assert.assertEquals("John Doe", firstRecord.getNamedValue("name"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", firstRecord.getNamedValue("age"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(1));
        Assert.assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("name", secondRecord.getNamedValue("name"));
        Assert.assertEquals("Jane Doe", secondRecord.getNamedValue("name"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", secondRecord.getNamedValue("age"));
        Assert.assertEquals("This is a comment", secondRecord.getComment());
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvContent = "name,age\nJohn Doe,30\nJane Doe,25";
        CSVFormat customFormat = new CSVFormat(',', null, new String[]{"name", "age"}, null);
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("name", firstRecord.getNamedValue("name"));
        Assert.assertEquals("John Doe", firstRecord.getNamedValue("name"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", firstRecord.getNamedValue("age"));
        Assert.assertEquals(30, firstRecord.getStructuralValueAt(1));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("name", secondRecord.getNamedValue("name"));
        Assert.assertEquals("Jane Doe", secondRecord.getNamedValue("name"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(0));
        Assert.assertEquals("age", secondRecord.getNamedValue("age"));
        Assert.assertEquals(25, secondRecord.getStructuralValueAt(1));
    }
}