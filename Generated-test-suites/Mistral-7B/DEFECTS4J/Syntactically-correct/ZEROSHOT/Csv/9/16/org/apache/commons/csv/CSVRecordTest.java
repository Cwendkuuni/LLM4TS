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
    public void testEmptyCSVRecord() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 0L);
        Assert.assertArrayEquals(new String[0], csvRecord.values());
        Assert.assertNull(csvRecord.getComment());
        Assert.assertNull(csvRecord.getRecordNumber());
        Assert.assertFalse(csvRecord.isConsistent());
        Assert.assertFalse(csvRecord.isMapped("header"));
        Assert.assertFalse(csvRecord.isSet("header"));
        Assert.assertFalse(csvRecord.iterator().hasNext());
        Assert.assertEquals(0, csvRecord.size());
        Assert.assertEquals("", csvRecord.toString());
    }

    @Test
    public void testCSVRecordWithValues() {
        String csvData = "header1,header2,header3\nvalue1_1,value2_1,value3_1\nvalue1_2,value2_2,value3_2";
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVParser csvParser = csvFormat.newParser(new StringReader(csvData));
        CSVRecord csvRecord = csvParser.nextRecord();

        Assert.assertEquals("value1_1", csvRecord.get("header1"));
        Assert.assertEquals("value2_1", csvRecord.get("header2"));
        Assert.assertEquals("value3_1", csvRecord.get("header3"));
        Assert.assertEquals("value1_2", csvRecord.get("header1", 1));
        Assert.assertEquals("value2_2", csvRecord.get("header2", 1));
        Assert.assertEquals("value3_2", csvRecord.get("header3", 1));
        Assert.assertEquals("value1_1", csvRecord.get(0));
        Assert.assertEquals("value2_1", csvRecord.get(1));
        Assert.assertEquals("value3_1", csvRecord.get(2));
        Assert.assertEquals("value1_2", csvRecord.get(3));
        Assert.assertEquals("value2_2", csvRecord.get(4));
        Assert.assertEquals("value3_2", csvRecord.get(5));
        Assert.assertEquals("value1_1,value2_1,value3_1", Arrays.toString(csvRecord.values()));
        Assert.assertEquals("", csvRecord.getComment());
        Assert.assertEquals(1, csvRecord.getRecordNumber());
        Assert.assertTrue(csvRecord.isConsistent());
        Assert.assertTrue(csvRecord.isMapped("header1"));
        Assert.assertTrue(csvRecord.isMapped("header2"));
        Assert.assertTrue(csvRecord.isMapped("header3"));
        Assert.assertTrue(csvRecord.isSet("header1"));
        Assert.assertTrue(csvRecord.isSet("header2"));
        Assert.assertTrue(csvRecord.isSet("header3"));
        Assert.assertEquals(6, csvRecord.size());
        Assert.assertEquals("CSVRecord[header1=value1_1, header2=value2_1, header3=value3_1, header1=value1_2, header2=value2_2, header3=value3_2]", csvRecord.toString());

        Map<String, String> map = csvRecord.toMap();
        Assert.assertEquals("value1_1", map.get("header1"));
        Assert.assertEquals("value2_1", map.get("header2"));
        Assert.assertEquals("value3_1", map.get("header3"));
        Assert.assertEquals("value1_2", map.get("header1"));
        Assert.assertEquals("value2_2", map.get("header2"));
        Assert.assertEquals("value3_2", map.get("header3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String csvData = "header1,header2,header3\nvalue1_1,value2_1,value3_1";
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeaderMappingStrategy(CSVFormat.NO_HEADERS);
        CSVParser csvParser = csvFormat.newParser(new StringReader(csvData));
        CSVRecord csvRecord = csvParser.nextRecord();
        csvRecord.get("header1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingNotFound() {
        String csvData = "header1,header2,header3\nvalue1_1,value2_1,value3_1";
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVParser csvParser = csvFormat.newParser(new StringReader(csvData));
        CSVRecord csvRecord = csvParser.nextRecord();
        csvRecord.get("header4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecord() {
        String csvData = "header1,header2\nvalue1_1,value2_1,value3_1";
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVParser csvParser = csvFormat.newParser(new StringReader(csvData));
        CSVRecord csvRecord = csvParser.nextRecord();
        csvRecord.get("header3");
    }
}