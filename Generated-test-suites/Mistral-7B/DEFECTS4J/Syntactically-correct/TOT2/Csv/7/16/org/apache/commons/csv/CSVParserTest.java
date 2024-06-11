import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseEmptyCSVFile() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertTrue(records.isEmpty());
        parser.close();
    }

    @Test
    public void testParseSimpleCSVFile() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("Name", firstRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("John", firstRecord.get("Name"));
        Assert.assertEquals(25, firstRecord.getInteger("Age"));
        Assert.assertEquals("New York", firstRecord.get("City"));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("Name", secondRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("Mike", secondRecord.get("Name"));
        Assert.assertEquals(30, secondRecord.getInteger("Age"));
        Assert.assertEquals("Chicago", secondRecord.get("City"));

        parser.close();
    }

    @Test
    public void testParseCSVFileWithMissingColumns() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("Name", firstRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("John", firstRecord.get("Name"));
        Assert.assertEquals(25, firstRecord.getInteger("Age"));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("Name", secondRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("Mike", secondRecord.get("Name"));
        Assert.assertEquals(30, secondRecord.getInteger("Age"));

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseCSVFileWithEmptyLine() throws Exception {
        String csvContent = "Name,Age\nJohn,25\n\nMike,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithInvalidFormat() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30\nName,City";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();
    }

    @Test
    public void testParseCSVFileWithComment() throws Exception {
        String csvContent = "Name,Age\nJohn,25\n---\nMike,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("Name", firstRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("John", firstRecord.get("Name"));
        Assert.assertEquals(25, firstRecord.getInteger("Age"));
        Assert.assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("Name", secondRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("Mike", secondRecord.get("Name"));
        Assert.assertEquals(30, secondRecord.getInteger("Age"));
        Assert.assertEquals("", secondRecord.getComment());

        parser.close();
    }

    @Test
    public void testParseCSVFileWithCustomFormat() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "N")
                .setHeader("Age", "A")
                .setDelimiter(';')
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("N", firstRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("John", firstRecord.get("Name"));
        Assert.assertEquals(25, firstRecord.getInteger("Age"));

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals("N", secondRecord.getHeaderMap().get("Name"));
        Assert.assertEquals("Mike", secondRecord.get("Name"));
        Assert.assertEquals(30, secondRecord.getInteger("Age"));

        parser.close();
    }

    @Test
    public void testParseCSVFileWithEmptyHeader() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader(Collections.emptyList())
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertNull(firstRecord.getHeaderMap());
        Assert.assertEquals("Name", firstRecord.getName(0));
        Assert.assertEquals("John", firstRecord.get(0));
        Assert.assertEquals(25, firstRecord.getInteger(1));

        CSVRecord secondRecord = records.get(1);
        Assert.assertNull(secondRecord.getHeaderMap());
        Assert.assertEquals("Name", secondRecord.getName(0));
        Assert.assertEquals("Mike", secondRecord.get(0));
        Assert.assertEquals(30, secondRecord.getInteger(1));

        parser.close();
    }

    @Test
    public void testParseCSVFileWithCustomHeader() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader(new String[]{"Label", "Value"})
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord firstRecord = records.get(0);
        Assert.assertArrayEquals(new String[]{"Label", "Value"}, firstRecord.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals("Label", firstRecord.getHeaderMap().get("Label"));
        Assert.assertEquals("Name", firstRecord.get("Label"));
        Assert.assertEquals("Value", firstRecord.get("Value"));
        Assert.assertEquals("John", firstRecord.get("Value"));
        Assert.assertEquals(25, firstRecord.getInteger("Value"));

        CSVRecord secondRecord = records.get(1);
        Assert.assertArrayEquals(new String[]{"Label", "Value"}, secondRecord.getHeaderMap().keySet().toArray(new String[0]));
        Assert.assertEquals("Label", secondRecord.getHeaderMap().get("Label"));
        Assert.assertEquals("Name", secondRecord.get("Label"));
        Assert.assertEquals("Value", secondRecord.get("Label"));
        Assert.assertEquals("Mike", secondRecord.get("Value"));
        Assert.assertEquals(30, secondRecord.getInteger("Value"));

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithInvalidCharset() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new StringBufferInputStream(new StringReader(csvContent)).getInputStream(), "foo"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithInvalidURL() throws Exception {
        String csvContent = "Name,Age\nJohn,25\nMike,30";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new URL("foo:/test.csv").openStream()), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();
    }
}