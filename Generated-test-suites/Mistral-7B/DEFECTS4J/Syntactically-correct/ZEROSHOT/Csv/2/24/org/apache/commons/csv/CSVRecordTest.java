import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertNotNull(record);
        Assert.assertEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.getComment());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
        Assert.assertEquals(2, record.size());
    }

    @Test
    public void testCSVRecordGet() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("John", record.get(0));
        Assert.assertEquals("Doe", record.get(1));
        Assert.assertEquals("john.doe@example.com", record.get(2));
        Assert.assertNull(record.get("Unknown"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInconsistent() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "Some extra value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Extra");
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Email"));
        Assert.assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Email"));
        Assert.assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> list = Arrays.asList(record.iterator());

        Assert.assertEquals(Arrays.asList("John", "Doe", "john.doe@example.com"), list);
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("[John, Doe, john.doe@example.com]", record.toString());
    }

    @Test
    public void testCSVRecordComment() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, "A comment for this record", 1);

        Assert.assertEquals("A comment for this record", record.getComment());
    }

    @Test
    public void testCSVRecordRecordNumber() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordEmpty() {
        CSVRecordBuilder builder = new CSVRecordBuilder();

        CSVRecord record = builder.build();

        Assert.assertNotNull(record);
        Assert.assertEquals(Collections.emptyList(), record.iterator());
        Assert.assertEquals(Collections.emptyMap(), record.mapping);
        Assert.assertNull(record.getComment());
        Assert.assertEquals(0, record.getRecordNumber());
        Assert.assertFalse(record.isConsistent());
        Assert.assertEquals(0, record.size());
        Assert.assertEquals("[]", record.toString());
    }
}