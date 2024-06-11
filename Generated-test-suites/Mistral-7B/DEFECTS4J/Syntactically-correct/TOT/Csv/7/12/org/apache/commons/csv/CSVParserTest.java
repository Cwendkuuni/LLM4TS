package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Testing {@link CSVParser} class
 */
public class CSVParserTest {

    @Test
    public void testEmptyCSV() throws IOException {
        List<CSVRecord> records = parse(new StringReader(""), new CSVFormat());
        assertEquals(Collections.emptyList(), records);
    }

    @Test
    public void testSimpleCSV() throws IOException {
        List<CSVRecord> records = parse(new StringReader("a,b\nc,d"), new CSVFormat());
        assertArrayEquals(new String[][] { new String[] { "a", "b" }, new String[] { "c", "d" } },
                records.toArray(new CSVRecord[records.size()][]));
    }

    @Test
    public void testHeaderCSV() throws IOException {
        List<CSVRecord> records = parse(new StringReader("Name,Age\nJohn,30\nMike,25"), new CSVFormat(new String[] { "Name", "Age" }));
        CSVRecord record = records.get(0);
        assertEquals("John", record.get(0));
        assertEquals(30, record.get(1).intValue());

        record = records.get(1);
        assertEquals("Mike", record.get(0));
        assertEquals(25, record.get(1).intValue());
    }

    @Test
    public void testInvalidCSV() throws IOException {
        List<CSVRecord> records = parse(new StringReader("Invalid"), new CSVFormat());
        assertNull(records);
    }

    @Test
    public void testMultiLineValue() throws IOException {
        List<CSVRecord> records = parse(new StringReader("Name,Value\nJohn,\"This is a multi-line value\"\nMike,42"), new CSVFormat());

        CSVRecord record = records.get(0);
        assertEquals("John", record.get(0));
        assertEquals("This is a multi-line value", record.get(1).toString());

        record = records.get(1);
        assertEquals("Mike", record.get(0));
        assertEquals(42, record.get(1).intValue());
    }

    @Test(expected = IOException.class)
    public void testInvalidFormat() throws IOException {
        parse(new StringReader("Invalid"), new CSVFormat("InvalidFormat"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> parse(null, new CSVFormat()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> parse(new StringReader("a,b"), null));
    }

    @Test(expected = IOException.class)
    public void testCloseParser() throws IOException {
        List<CSVRecord> records = parse(new StringReader("a,b\nc,d"), new CSVFormat());
        records.iterator().next();
        parse(new StringReader("a,b\nc,d"), new CSVFormat()).close();
        assertThrows(NoSuchElementException.class, () -> records.iterator().next());
    }

}