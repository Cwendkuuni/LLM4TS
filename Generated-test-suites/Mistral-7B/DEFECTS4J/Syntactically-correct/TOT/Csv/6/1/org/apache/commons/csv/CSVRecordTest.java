package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for {@link CSVRecord}
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}
     */
    @Test
    public void testCSVRecord() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals("Alice", record.get("name"));
        assertEquals("30", record.get("age"));
        assertEquals("F", record.get("gender"));
        assertEquals("age=30", record.get(Enum.valueOf(Age.AGE)));
        assertEquals("gender=F", record.get(Enum.valueOf(Gender.GENDER)));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("unknown"));
        assertFalse(record.isSet("unknown"));
        assertEquals(Arrays.asList("Alice", "Smith", "age=30", "gender=F"), record.toList());
        assertEquals(Arrays.asList("name", "age", "gender"), record.keySet());
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)}
     */
    @Test
    public void testGetByEnum() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("30", record.get(Age.AGE));
        assertEquals("F", record.get(Gender.GENDER));
    }

    /**
     * Test method for {@link CSVRecord#get(int)}
     */
    @Test
    public void testGetByIndex() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("gender=F", record.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}
     */
    @Test
    public void testGetByName() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Alice", record.get("name"));
        assertEquals("30", record.get("age"));
        assertEquals("F", record.get("gender"));
        assertNull(record.get("unknown"));

        try {
            record.get("name1");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get("age1");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            record.get("gender1");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CSVRecord#getComment()}
     */
    @Test
    public void testGetComment() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);

        assertEquals("comment", record.getComment());
        assertNull(record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}
     */
    @Test
    public void testIsConsistent() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record1 = new CSVRecord(values, mapping, null, 1);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 1);
        CSVRecord record3 = new CSVRecord(new String[0], mapping, null, 1);

        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());
        assertFalse(record3.isConsistent());

        mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 2);
        mapping.put("gender", 1);

        CSVRecord record4 = new CSVRecord(values, mapping, null, 1);
        assertFalse(record4.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}
     */
    @Test
    public void testIsMapped() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("gender"));
        assertFalse(record.isMapped("unknown"));

        mapping = new HashMap<>();
        mapping.put("name", 0);

        CSVRecord record2 = new CSVRecord(values, mapping, null, 1);

        assertFalse(record2.isMapped("age"));
        assertFalse(record2.isMapped("gender"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}
     */
    @Test
    public void testIsSet() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("gender"));
        assertFalse(record.isSet("unknown"));

        String[] values2 = {"Alice", "Smith"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("name", 0);
        mapping2.put("age", 1);

        CSVRecord record2 = new CSVRecord(values2, mapping2, null, 1);

        assertFalse(record2.isSet("age"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}
     */
    @Test
    public void testIterator() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Iterator<String> it = record.iterator();
        List<String> expectedList = Arrays.asList("Alice", "Smith", "age=30", "gender=F");

        for (int i = 0; i < 4; i++) {
            assertEquals(expectedList.get(i), it.next());
        }
        assertFalse(it.hasNext());
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)}
     */
    @Test
    public void testPutIn() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        map = record.putIn(map);

        assertEquals(4, map.size());
        assertEquals("Alice", map.get("name"));
        assertEquals("Smith", map.get("name1"));
        assertEquals("age=30", map.get("age"));
        assertEquals("gender=F", map.get("gender"));
    }

    /**
     * Test method for {@link CSVRecord#size()}
     */
    @Test
    public void testSize() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(4, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toList()}
     */
    @Test
    public void testToList() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList("Alice", "Smith", "age=30", "gender=F");
        assertEquals(expectedList, record.toList());
    }

    /**
     * Test method for {@link CSVRecord#toMap()}
     */
    @Test
    public void testToMap() {
        String[] values = {"Alice", "Smith", "age=30", "gender=F"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name", "Alice");
        expectedMap.put("age", "30");
        expectedMap.put("gender", "F");

        Map<String, String> map = record.toMap();

        assertEquals(expectedMap, map);
    }

    interface Age {
        String AGE = "age";
    }

    interface Gender {
        String GENDER = "gender";
    }
}