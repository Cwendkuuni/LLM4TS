import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CSVParserTest {

    @Test
    public void testParseEmptyCSV() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertNull(record);
    }

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("name", record.getHeaderMap().get("name"));
        Assert.assertEquals("Alice", record.get("name"));
        Assert.assertEquals(30, record.getInteger("age"));

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("age", record.getHeaderMap().get("name"));
        Assert.assertEquals("Bob", record.get("name"));
        Assert.assertEquals(25, record.getInteger("age"));

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyRecord() throws IOException {
        String csvData = "name\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord(); // This should throw an exception
    }

    @Test(expected = IOException.class)
    public void testParseMalformedCSV() throws IOException {
        String csvData = "name,age\nAlice,30aple";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord(); // The parse should fail here
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Age")
                .setNullValue("N/A")
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);
        CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("Name", record.getHeaderMap().get("name"));
        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals(30, record.getInteger("Age"));

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("Age", record.getHeaderMap().get("name"));
        Assert.assertEquals("Bob", record.get("Name"));
        Assert.assertEquals(25, record.getInteger("Age"));

        parser.close();
    }

    @Test
    public void testParseCSVWithComments() throws IOException {
        String csvData = "name,age\nAlice,30# This is a comment\nBob,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("name", record.getHeaderMap().get("name"));
        Assert.assertEquals("Alice", record.get("name"));
        Assert.assertEquals(30, record.getInteger("age"));
        Assert.assertNull(record.getComment());

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("age", record.getHeaderMap().get("name"));
        Assert.assertEquals("Bob", record.get("name"));
        Assert.assertEquals(25, record.getInteger("age"));
        Assert.assertEquals("# This is a comment", record.getComment());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCSVWithInvalidFormat() throws IOException {
        String csvData = "name,age\nAlice,30aple";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord(); // The parse should fail here
    }

    @Test
    public void testParseCSVWithCustomFormatAndEmptyRecord() throws IOException {
        String csvData = "name,age\nAlice,30\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Age")
                .setNullValue("N/A")
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);
        CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("Name", record.getHeaderMap().get("name"));
        Assert.assertEquals("Alice", record.get("Name"));
        Assert.assertEquals(30, record.getInteger("Age"));

        record = parser.nextRecord();
        Assert.assertNull(record);

        parser.close();
    }

    @Test
    public void testParseCSVWithEmptyFile() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertNull(record);
        parser.close();
    }
}