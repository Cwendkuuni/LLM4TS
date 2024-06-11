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
    public void testParseSimpleCSVFile() throws IOException {
        // Given
        String csvContent = "Name,Age\nJohn,30\nMary,25\n";
        CSVFormat format = CSVFormat.DEFAULT;

        // When
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Name", record.getHeaderMap().get("Name").toString());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals("Age", record.getHeaderMap().get("Age").toString());
            Assert.assertEquals(30, Integer.parseInt(record.get(1)));
        }

        // Cleanup
        parser.close();
    }

    @Test
    public void testParseEmptyCSVFile() throws IOException {
        // Given
        String csvContent = "";
        CSVFormat format = CSVFormat.DEFAULT;

        // When
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());

        // Cleanup
        parser.close();
    }

    @Test
    public void testParseCSVFileWithMissingHeader() throws IOException {
        // Given
        String csvContent = "Age,Value\n30,John\n25,Mary\n";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        // When
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        // Then
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Age", "Value"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("Age", record.getHeaderMap().get("Age").toString());
            Assert.assertEquals(30, Integer.parseInt(record.get(0)));
            Assert.assertEquals("Value", record.getHeaderMap().get("Value").toString());
            Assert.assertEquals("John", record.get(1));
        }

        // Cleanup
        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyRecord() throws IOException {
        // Given
        String csvContent = "Name\n";
        CSVFormat format = CSVFormat.DEFAULT;

        // When
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        // Then
        parser.nextRecord();

        // Cleanup
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSVFile() throws IOException {
        // Given
        String csvContent = "Name,Age\nJohn,30\nMary,25\nName,Value";
        CSVFormat format = CSVFormat.DEFAULT;

        // When
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);

        // Then
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // This record should throw an IOException

        // Cleanup
        parser.close();
    }
}