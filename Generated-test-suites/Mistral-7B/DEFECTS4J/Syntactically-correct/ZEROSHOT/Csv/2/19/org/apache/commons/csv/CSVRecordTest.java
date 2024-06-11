import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        // Test empty record
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        Assert.assertArrayEquals(new String[0], record.values());
        Assert.assertNull(record.getComment());
        Assert.assertEquals(0, record.getRecordNumber());
        Assert.assertFalse(record.isConsistent());
        Assert.assertFalse(record.isMapped("columnName"));
        Assert.assertFalse(record.isSet("columnName"));
        Assert.assertEquals(0, record.size());

        // Test record with values and mapping
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        Assert.assertArrayEquals(values, record1.values());
        Assert.assertNull(record1.getComment());
        Assert.assertEquals(1, record1.getRecordNumber());
        Assert.assertTrue(record1.isConsistent());
        Assert.assertTrue(record1.isMapped("Name"));
        Assert.assertTrue(record1.isMapped("Age"));
        Assert.assertTrue(record1.isMapped("City"));
        Assert.assertTrue(record1.isSet("Name"));
        Assert.assertTrue(record1.isSet("Age"));
        Assert.assertTrue(record1.isSet("City"));
        Assert.assertEquals(3, record1.size());

        // Test record with comment
        String comment = "This is a comment";
        CSVRecord record2 = new CSVRecord(values, mapping, comment, 2);
        Assert.assertArrayEquals(values, record2.values());
        Assert.assertEquals(comment, record2.getComment());
        Assert.assertEquals(2, record2.getRecordNumber());

        // Test record with values, mapping and comment
        String[] values2 = new String[]{"Name1", "Age1", "City1", "Column1"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("Name1", 0);
        mapping2.put("Age1", 1);
        mapping2.put("City1", 2);
        mapping2.put("Column1", 3);
        CSVRecord record3 = new CSVRecord(values2, mapping2, comment, 3);
        Assert.assertArrayEquals(values2, record3.values());
        Assert.assertEquals(comment, record3.getComment());
        Assert.assertEquals(3, record3.getRecordNumber());
        Assert.assertTrue(record3.isConsistent());
        Assert.assertTrue(record3.isMapped("Name1"));
        Assert.assertTrue(record3.isMapped("Age1"));
        Assert.assertTrue(record3.isMapped("City1"));
        Assert.assertTrue(record3.isMapped("Column1"));
        Assert.assertTrue(record3.isSet("Name1"));
        Assert.assertTrue(record3.isSet("Age1"));
        Assert.assertTrue(record3.isSet("City1"));
        Assert.assertTrue(record3.isSet("Column1"));
        Assert.assertEquals(4, record3.size());
    }

    @Test
    public void testCSVRecordGetters() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("City", record.get(2));
        Assert.assertNull(record.get(3));

        Assert.assertEquals("Name", record.get("Name"));
        Assert.assertEquals("Age", record.get("Age"));
        Assert.assertEquals("City", record.get("City"));
        Assert.assertNull(record.get("Column"));

        Assert.assertTrue(record.isConsistent());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertTrue(record.isMapped("City"));
        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertTrue(record.isSet("City"));
        Assert.assertEquals(3, record.size());

        Assert.assertArrayEquals(values, record.values());
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());

        Assert.assertEquals("Name", record.get(0));
        Assert.assertEquals("Name", record.get("Name"));
        Assert.assertEquals(0, record.getMap().get("Name").intValue());

        Assert.assertEquals("Age", record.get(1));
        Assert.assertEquals("Age", record.get("Age"));
        Assert.assertEquals(1, record.getMap().get("Age").intValue());

        Assert.assertEquals("City", record.get(2));
        Assert.assertEquals("City", record.get("City"));
        Assert.assertEquals(2, record.getMap().get("City").intValue());

        Assert.assertFalse(record.isMapped("Column"));
        Assert.assertFalse(record.isSet("Column"));
        Assert.assertNull(record.get("Column"));
    }

    @Test
    public void testCSVRecordIterable() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedValues = Arrays.asList("Name", "Age", "City");

        Iterator<String> iterator = record.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Name", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("Age", iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals("City", iterator.next());
        Assert.assertFalse(iterator.hasNext());

        Assert.assertEquals(expectedValues, Arrays.asList(record.values()));
    }
}