import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseBasic() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertEquals("John", record1.get("Name"));
        Assert.assertEquals(30, record1.getInteger("Age"));
        Assert.assertEquals("New York", record1.get("City"));

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertEquals("Mary", record2.get("Name"));
        Assert.assertEquals(25, record2.getInteger("Age"));
        Assert.assertEquals("Chicago", record2.get("City"));
    }

    @Test
    public void testParseWithHeader() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertEquals("John", record1.get("Name"));
        Assert.assertEquals(30, record1.getInteger("Age"));
        Assert.assertEquals("City", record1.getHeaderMap().get("City"));
        Assert.assertEquals("New York", record1.get("City"));

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertEquals("Mary", record2.get("Name"));
        Assert.assertEquals(25, record2.getInteger("Age"));
        Assert.assertEquals("City", record2.getHeaderMap().get("City"));
        Assert.assertEquals("Chicago", record2.get("City"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmpty() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago\nName,Age";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMary,25,Chicago";
        CSVFormat customFormat = new CSVFormat(CSVFormat.DEFAULT.withHeader().withNullString("NA"));
        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertEquals("John", record1.get("Name"));
        Assert.assertEquals(30, record1.getInteger("Age"));
        Assert.assertEquals("City", record1.getHeaderMap().get("City"));
        Assert.assertEquals("New York", record1.get("City"));
        Assert.assertNull(record1.get("Age2"));

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertEquals("Mary", record2.get("Name"));
        Assert.assertEquals(25, record2.getInteger("Age"));
        Assert.assertEquals("City", record2.getHeaderMap().get("City"));
        Assert.assertNull(record2.get("City2"));
        Assert.assertEquals("NA", record2.get("Age2"));
    }

    @Test
    public void testParseEmptyRecord() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\n,\nMary,25,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        // Test for empty record
        Assert.assertEquals(0, record1.size());
        Assert.assertEquals("Name", record1.getHeaderMap().get("Name"));
        Assert.assertEquals(0, record1.getInteger("Age"));
        Assert.assertNull(record1.get("City"));

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.getHeaderMap().get("Name"));
        Assert.assertEquals("John", record2.get("Name"));
        Assert.assertEquals(30, record2.getInteger("Age"));
        Assert.assertEquals("City", record2.getHeaderMap().get("City"));
        Assert.assertEquals("New York", record2.get("City"));

        CSVRecord record3 = records.get(2);
        Assert.assertEquals("Name", record3.getHeaderMap().get("Name"));
        Assert.assertEquals("Mary", record3.get("Name"));
        Assert.assertEquals(25, record3.getInteger("Age"));
        Assert.assertEquals("City", record3.getHeaderMap().get("City"));
        Assert.assertEquals("Chicago", record3.get("City"));
    }
}