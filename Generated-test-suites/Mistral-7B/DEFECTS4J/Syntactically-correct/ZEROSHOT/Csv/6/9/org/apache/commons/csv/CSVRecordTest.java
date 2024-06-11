package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVRecord}
 *
 * @author <a href="mailto:jesse.glick@gmail.com">Jesse Glick</a>
 */
public class CSVRecordTest {

    @org.junit.Test
    public void testBasic() {
        final String csv = "header1,header2,header3\n" + "value1_1,value1_2,value1_3\n"
                + "value2_1,value2_2,value2_3";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.nextRecord();

        assertEquals("value1_1", record1.get(0));
        assertEquals("value1_2", record1.get(1));
        assertEquals("value1_3", record1.get(2));
        assertEquals("value1_1", record1.get(Integer.valueOf("0")));
        assertEquals("value1_2", record1.get(Integer.valueOf("1")));
        assertEquals("value1_3", record1.get(Integer.valueOf("2")));
        assertEquals("value1_1", record1.get(Enum.valueOf("ZERO")));
        assertEquals("value1_2", record1.get(Enum.valueOf("ONE")));
        assertEquals("value1_3", record1.get(Enum.valueOf("TWO")));

        assertEquals("value2_1", record1.get(3)); // Invalid index
        assertNull(record1.get(4)); // Invalid index

        assertEquals("value1_1", record1.get("header1"));
        assertEquals("value1_2", record1.get("header2"));
        assertEquals("value1_3", record1.get("header3"));

        assertFalse(record1.isMapped("non-existent-header"));
        assertTrue(record1.isMapped("header1"));
        assertTrue(record1.isMapped("header2"));
        assertTrue(record1.isMapped("header3"));

        assertFalse(record1.isSet("non-existent-header"));
        assertTrue(record1.isSet("header1"));
        assertTrue(record1.isSet("header2"));
        assertTrue(record1.isSet("header3"));

        final Map<String, String> map = record1.toMap();
        assertEquals("value1_1", map.get("header1"));
        assertEquals("value1_2", map.get("header2"));
        assertEquals("value1_3", map.get("header3"));
        assertNull(map.get("non-existent-header"));

        assertEquals(Arrays.asList("value1_1", "value1_2", "value1_3"), record1.toList());

        assertEquals("value1_1,value1_2,value1_3", record1.toString());
        assertArrayEquals(new String[]{"value1_1", "value1_2", "value1_3"}, record1.values());

        record1.iterator();

        assertEquals(3, record1.size());

        final CSVRecord record2 = new CSVRecord(null, null, null, 0);
        assertNull(record2.get(0));
        assertNull(record2.get(1));
        assertNull(record2.get(2));
        assertFalse(record2.isConsistent());
        assertFalse(record2.isMapped("header1"));
        assertFalse(record2.isSet("header1"));
        assertNull(record2.getComment());
        assertNull(record2.getRecordNumber());
    }

    @org.junit.Test
    public void testComment() {
        final String csv = "header1,header2\n" + "value1_1,value1_2#comment\n" + "value2_1,value2_2";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        final CSVRecord record1 = parser.nextRecord();
        assertEquals("comment", record1.getComment());
        assertNull(parser.nextRecord().getComment());
    }

    @org.junit.Test
    public void testRecordNumber() {
        final String csv = "header1,header2\n" + "value1_1,value1_2\n" + "value2_1,value2_2\n"
                + "value3_1,value3_2";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        CSVRecord record3 = parser.nextRecord();
        parser.nextRecord(); // Skip a record

        assertEquals(1, record1.getRecordNumber());
        assertEquals(2, record2.getRecordNumber());
        assertEquals(3, record3.getRecordNumber());
        assertNull(parser.nextRecord().getRecordNumber());
    }

    @org.junit.Test
    public void testEmpty() {
        final String csv = "";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        final CSVRecord record = parser.nextRecord();
        assertNull(record);
    }

    @org.junit.Test
    public void testInvalidCsv() {
        final String csv = "header1,header2\n" + "value1_1,value1_2\n" + "value1_1,value1_2,value1_3,\n"
                + "value2_1,value2_2";

        try {
            final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
            parser.nextRecord();
            fail();
        } catch (final CSVException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testNullHeaderMapping() {
        final String csv = "header1,header2\n" + "value1_1,value1_2\n" + "value2_1,value2_2";

        final CSVParser parser = new CSVParser(new StringReader(csv), new CSVFormatBuilder()
                .setHeaderMapping(null).build());

        final CSVRecord record = parser.nextRecord();
        assertNull(record.get("header1"));
        assertNull(record.get("header2"));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isMapped("header2"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header2"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @org.junit.Test
    public void testMissingRecord() {
        final String csv = "header1,header2\n" + "value1_1,value1_2\n" + "value2_1,value2_2\n";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        final CSVRecord record = parser.nextRecord();
        parser.nextRecord(); // next record

        try {
            parser.nextRecord();
            fail();
        } catch (final NoSuchElementException e) {
            // expected
        }

        assertEquals(2, record.size());
    }

    @org.junit.Test
    public void testIterable() {
        final String csv = "header1,header2\n" + "value1_1,value1_2\n" + "value2_1,value2_2";

        final CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT);
        final CSVRecord record = parser.nextRecord();

        final List<String> list = record.iterator().asList();
        assertEquals("value1_1", list.get(0));
        assertEquals("value1_2", list.get(1));
        assertEquals(2, list.size());
    }
}