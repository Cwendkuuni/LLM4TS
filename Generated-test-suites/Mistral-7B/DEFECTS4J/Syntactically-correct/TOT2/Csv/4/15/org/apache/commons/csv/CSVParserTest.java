import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25,Paris";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.get(0));
        Assert.assertEquals("John Doe", record1.get(1));
        Assert.assertEquals(30, record1.get(2).getIntValue());

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.get(0));
        Assert.assertEquals("Jane Smith", record2.get(1));
        Assert.assertEquals(25, record2.get(2).getIntValue());

        parser.close();
    }

    @Test
    public void testParseCSVFileWithEmptyLine() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\n\nJane Smith,25,Paris";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.get(0));
        Assert.assertEquals("John Doe", record1.get(1));
        Assert.assertEquals(30, record1.get(2).getIntValue());

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.get(0));
        Assert.assertEquals("Jane Smith", record2.get(1));
        Assert.assertEquals(25, record2.get(2).getIntValue());

        parser.close();
    }

    @Test
    public void testParseCSVFileWithMissingColumn() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Smith,25";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.get(0));
        Assert.assertEquals("John Doe", record1.get(1));
        Assert.assertEquals(30, record1.size() > 2 ? record1.get(2).getIntValue() : null);

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.get(0));
        Assert.assertEquals("Jane Smith", record2.get(1));
        Assert.assertEquals(25, record2.size() > 2 ? record2.get(2).getIntValue() : null);

        parser.close();
    }

    @Test
    public void testParseCSVFileWithInvalidCSVFormat() throws IOException {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Smith,25 Apple";
        CSVFormat format = CSVFormat.DEFAULT;

        try {
            CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
            List<CSVRecord> records = parser.getRecords();
            parser.close();
            fail("Should have thrown an IOException.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseCSVFileWithMissingHeader() throws IOException {
        String csvContent = "Age,City\n30,New York\n25,Paris";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertEquals(2, record1.size());
        Assert.assertEquals(30, record1.get(0).getIntValue());
        Assert.assertEquals("City", record1.get(1).toString());

        CSVRecord record2 = records.get(1);
        Assert.assertEquals(2, record2.size());
        Assert.assertEquals(25, record2.get(0).getIntValue());
        Assert.assertEquals("City", record2.get(1).toString());

        parser.close();
    }
}