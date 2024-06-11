import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,Canada";
        CSVFormat format = new CSVFormat().withHeader("name", "age", "country");
        Iterable<CSVRecord> records = format.parse(csv);

        CSVRecord record = records.iterator().next();
        Assert.assertEquals("John Doe", record.get("name"));
        Assert.assertEquals("30", record.get("age"));
        Assert.assertEquals("USA", record.get("country"));
        Assert.assertEquals("John Doe", record.get(0));
        Assert.assertEquals("30", record.get(1));
        Assert.assertEquals("USA", record.get(2));
        Assert.assertEquals("USA", record.get(CSVRecord.Column.COUNTRY));
        Assert.assertEquals("John Doe", record.get(CSVRecord.Column.NAME));
        Assert.assertEquals("30", record.get(CSVRecord.Column.AGE));
        Assert.assertEquals(2, record.size());
        Assert.assertTrue(record.isConsistent());
        Assert.assertFalse(record.isSet("unknown"));

        Map<String, String> map = record.toMap();
        Assert.assertEquals("John Doe", map.get("name"));
        Assert.assertEquals("30", map.get("age"));
        Assert.assertEquals("USA", map.get("country"));

        List<String> values = Arrays.asList(record.values());
        Assert.assertEquals(Arrays.asList("John Doe", "30", "USA"), values);

        record = new CSVRecord(null, null, null, 0L);
        Assert.assertNull(record.get("name"));
        Assert.assertFalse(record.isConsistent());
    }

    public enum Column {
        NAME,
        AGE,
        COUNTRY
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,Canada";
        CSVFormat format = new CSVFormat();
        Iterable<CSVRecord> records = format.parse(csv);
        CSVRecord record = records.iterator().next();
        record.get("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingNotFound() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,Canada";
        CSVFormat format = new CSVFormat().withHeader("name", "age", "country");
        Iterable<CSVRecord> records = format.parse(csv);
        CSVRecord record = records.iterator().next();
        record.get("unknownColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecordInconsistent() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,Canada\n3,2";
        CSVFormat format = new CSVFormat().withHeader("name", "age", "country");
        Iterable<CSVRecord> records = format.parse(csv);
        CSVRecord record = records.iterator().next();
        record.get(CSVRecord.Column.COUNTRY);
    }

    @Test
    public void testRecordWithNullString() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,<NULL>\n";
        CSVFormat format = new CSVFormat().withNullString("<NULL>");
        Iterable<CSVRecord> records = format.parse(csv);
        CSVRecord record = records.iterator().next();
        Assert.assertEquals("<NULL>", record.get("country"));
    }
}