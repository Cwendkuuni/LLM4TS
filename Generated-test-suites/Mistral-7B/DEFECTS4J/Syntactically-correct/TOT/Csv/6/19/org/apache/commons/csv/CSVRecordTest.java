import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        final String csv = ""; // Empty CSV file
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertArrayEquals(new String[0], record.values());
        Assert.assertNull(record.getComment());
        Assert.assertNull(record.getMapping());
        Assert.assertEquals(-1, record.getRecordNumber());
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithValidValues() {
        final String csv = "name,age,city\nJohn Doe,32,New York,comment1\nJane Doe,27,Chicago,comment2";
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals("name", record.mapping.get("name"));
        Assert.assertEquals("age", record.mapping.get("age"));
        Assert.assertEquals("city", record.mapping.get("city"));
        Assert.assertEquals("John Doe", record.get("name"));
        Assert.assertEquals("comment1", record.getComment());
        Assert.assertArrayEquals(new String[]{"John Doe", "32", "New York"}, record.values());
        Assert.assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidValues() {
        final String csv = "name,age,city\nJohn Doe,32,New York,comment1\nJane Doe,27,Chicago,comment2\nInvalid,3";
        final CSVFormat format = new CSVFormat();
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals(-1, record.getRecordNumber());
        record.get("Invalid"); // Trigger the exception
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithInvalidMapping() {
        final String csv = "name,age\nJohn Doe,32\nJane Doe,27";
        final CSVFormat format = new CSVFormat();
        format.setHeaderMappingStrategy(CSVFormat.DEFAULT.HEADER_mappingStrategy().withFirstHeader());
        final CSVParser parser = format.newParser(new StringReader(csv));
        final CSVRecord record = parser.nextRecord();
        record.putIn(new HashMap<>());
        record.get("city"); // Trigger the exception
    }
}