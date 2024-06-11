import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseEmptyCSVFile() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
        parser.close();
    }

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("a", record.get(0));
        Assert.assertEquals("b", record.get(1));
        parser.close();
    }

    @Test
    public void testParseCSVFileWithHeaders() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\nMike,30"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        Assert.assertEquals(2, record1.size());
        Assert.assertEquals("Name", record1.getHeader().get(0));
        Assert.assertEquals("Age", record1.getHeader().get(1));
        Assert.assertEquals("John", record1.get(0));
        Assert.assertEquals("25", record1.get(1));
        Assert.assertEquals(2, record2.size());
        Assert.assertEquals("Name", record2.getHeader().get(0));
        Assert.assertEquals("Age", record2.getHeader().get(1));
        Assert.assertEquals("Mike", record2.get(0));
        Assert.assertEquals("30", record2.get(1));
        parser.close();
    }

    @Test
    public void testParseCSVFileWithEmptyRecord() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\n,30"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        Assert.assertEquals(2, record1.size());
        Assert.assertEquals("Name", record1.getHeader().get(0));
        Assert.assertEquals("Age", record1.getHeader().get(1));
        Assert.assertEquals("John", record1.get(0));
        Assert.assertEquals("25", record1.get(1));
        Assert.assertEquals(1, record2.size());
        Assert.assertNull(record2.getHeader());
        Assert.assertNull(record2.get(0));
        parser.close();
    }

    @Test
    public void testParseCSVFileWithTrailingEmptyLines() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25\nMike,30\n\n"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        Assert.assertEquals(2, record1.size());
        Assert.assertEquals("Name", record1.getHeader().get(0));
        Assert.assertEquals("Age", record1.getHeader().get(1));
        Assert.assertEquals("John", record1.get(0));
        Assert.assertEquals("25", record1.get(1));
        Assert.assertEquals(2, record2.size());
        Assert.assertEquals("Name", record2.getHeader().get(0));
        Assert.assertEquals("Age", record2.getHeader().get(1));
        Assert.assertEquals("Mike", record2.get(0));
        Assert.assertEquals("30", record2.get(1));
        parser.close();
    }

    @Test
    public void testParseCSVFileWithQuotedValues() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,\"First,Last\"\nAge,18"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assert.assertEquals(2, record.size());
        Assert.assertEquals(2, record.getHeader().size());
        Assert.assertEquals("Name", record.getHeader().get(0));
        Assert.assertEquals("Age", record.getHeader().get(1));
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("First,Last", record.get(1));
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithInvalidFormat() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25aple"), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        Assert.fail("Expected IOException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithNullFormat() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("Name,Age\nJohn,25"), null);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithNullReader() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader(""), null);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithNullCharset() throws IOException {
        CSVParser parser = CSVParser.parse(new URL("file:test.csv"), null, CSVFormat.DEFAULT);
        Assert.fail("Expected IOException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVFileWithInvalidURL() throws IOException {
        CSVParser parser = CSVParser.parse(new URL("file:test.csv:123"), CSVFormat.DEFAULT);
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = IOException.class)
    public void testParseCSVFileWithFileNotFound() throws IOException {
        CSVParser parser = CSVParser.parse(new File("non-existent-file.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
        parser.close();
    }

}