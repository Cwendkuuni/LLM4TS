package org.apache.commons.csv;

import org.junit.Assert;
import org.junit.Test;

/**
 * CSVRecordTest.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Assert.assertEquals(values, record.values());
        Assert.assertEquals(mapping, record.mapping);
        Assert.assertNull(record.getComment());
        Assert.assertEquals(0L, record.getRecordNumber());
        Assert.assertTrue(record.isConsistent());
    }

    /**
     * Test get method with enum.
     */
    @Test
    public void testGetEnum() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        String name = record.get(Enum.NAME);
        Assert.assertEquals("John", name);

        String email = record.get(Enum.EMAIL);
        Assert.assertEquals("john.doe@example.com", email);

        String age = record.get(Enum.AGE);
        Assert.assertEquals("45", age);
    }

    /**
     * Test get method with String.
     */
    @Test
    public void testGetString() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        String name = record.get("Name");
        Assert.assertEquals("John", name);

        String email = record.get("Email");
        Assert.assertEquals("john.doe@example.com", email);

        String age = record.get("Age");
        Assert.assertEquals("45", age);

        String nonExistingColumn = record.get("NonExistentColumn");
        Assert.assertNull(nonExistingColumn);
    }

    /**
     * Test get method with null mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetStringWithNullMapping() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        record.get("Name");
    }

    /**
     * Test get method with inconsistent record.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringWithInconsistentRecord() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2); // Incorrect index
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        record.get("Age");
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Assert.assertTrue(record.isConsistent());

        // Test with smaller mapping
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1); // Shorter
        CSVRecord recordWithShorterMapping = new CSVRecord(values, mapping, null, 0L);

        Assert.assertFalse(recordWithShorterMapping.isConsistent());

        // Test with larger mapping
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2); // Larger
        mapping.put("NonExistentColumn", 4); // Non existent column
        CSVRecord recordWithLargerMapping = new CSVRecord(values, mapping, null, 0L);

        Assert.assertFalse(recordWithLargerMapping.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Email"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isMapped("NonExistentColumn"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Assert.assertTrue(record.isSet("Name"));
        Assert.assertTrue(record.isSet("Email"));
        Assert.assertTrue(record.isSet("Age"));
        Assert.assertFalse(record.isSet("NonExistentColumn"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Iterator<String> iterator = record.iterator();
        List<String> valuesList = Arrays.asList(values);

        Assert.assertEquals(valuesList.size(), iterator.size());

        int i = 0;
        while (iterator.hasNext()) {
            String actual = iterator.next();
            Assert.assertEquals(valuesList.get(i++), actual);
        }
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Email", "john.doe@example.com");
        expectedMap.put("Age", "45");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> actualMap = record.putIn(new HashMap<>());

        Assert.assertEquals(expectedMap, actualMap);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Assert.assertEquals(values.length, record.size());
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        List<String> expectedList = Arrays.asList(values);
        List<String> actualList = record.toList();

        Assert.assertEquals(expectedList, actualList);
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Email", "john.doe@example.com");
        expectedMap.put("Age", "45");

        Map<String, String> actualMap = record.toMap();

        Assert.assertEquals(expectedMap, actualMap);
    }
}