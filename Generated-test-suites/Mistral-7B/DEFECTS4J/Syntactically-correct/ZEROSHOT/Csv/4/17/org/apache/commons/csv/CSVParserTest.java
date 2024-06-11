package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.Token.Type.TOKEN;
import static org.apache.commons.csv.CSVRecord.VALID;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.IterableCSVParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    private static final String RFC4180_FORMAT = "format1,format2,format3\n"
            + "A,B,C\n"
            + "D,E,F";

    private CSVFormat format;
    private CSVParser parser;
    private Iterator<CSVRecord> recordIterator;
    private List<CSVRecord> records;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = CSVParser.parse(new StringReader(RFC4180_FORMAT), format);
        records = parser.getRecords();
        recordIterator = parser.iterator();
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(3, headerMap.size());
        assertEquals("format1", headerMap.get(0));
        assertEquals("format2", headerMap.get(1));
        assertEquals("format3", headerMap.get(2));
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.close();
    }

    @Test
    public void testIteratorHasNext() throws IOException {
        assertTrue(recordIterator.hasNext());
        parser.nextRecord();
        assertTrue(recordIterator.hasNext());
        parser.close();
        assertFalse(recordIterator.hasNext());
    }

    @Test
    public void testIteratorNext() throws IOException {
        CSVRecord record = recordIterator.next();
        assertSame(records.get(0), record);
        record = recordIterator.next();
        assertSame(records.get(1), record);
        parser.close();
        assertNull(recordIterator.next());
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertEquals(records.get(0).getRecordNumber(), 0);
        assertEquals(records.get(1).getRecordNumber(), 1);
        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoMoreRecords() throws IOException {
        parser.close();
        recordIterator.next();
    }

    @Test
    public void testParseFile() throws IOException {
        File csvData = new File("CSVParserTest.java");
        CSVParser parser = CSVParser.parse(csvData, format);
        CSVRecordIterator recordIterator = parser.iterator();
        CSVRecord record = recordIterator.next();
        assertSame( records.get(0), record );
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse(RFC4180_FORMAT, format);
        CSVRecordIterator recordIterator = parser.iterator();
        CSVRecord record = recordIterator.next();
        assertSame(records.get(0), record );
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = getClass().getResource("/CSVParserTest.java");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), format);
        CSVRecordIterator recordIterator = parser.iterator();
        CSVRecord record = recordIterator.next();
        assertSame(records.get(0), record );
        parser.close();
    }

    @Test
    public void testParseInputStreamReader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(RFC4180_FORMAT.getBytes());
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream), format);
        CSVRecord record = parser.nextRecord();
        assertSame(records.get(0), record );
        parser.close();
    }

    @Test
    public void testParseReader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(RFC4180_FORMAT.getBytes());
        CSVParser parser = new CSVParser(new BufferedReader(new InputStreamReader(inputStream)), format);
        CSVRecord record = parser.nextRecord();
        assertSame(records.get(0), record );
        parser.close();
    }

    @Test
    public void testParseIterableCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        Iterable<String> input = Arrays.asList(RFC4180_FORMAT.split(System.lineSeparator()));
        CSVParser parser = CSVFormat.DEFAULT.parse(input);
        CSVRecord record = parser.nextRecord();
        assertSame(records.get(0), record );
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullFormat() throws IOException {
        CSVParser.parse(new File("CSVParserTest.java"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullFile() throws IOException {
        CSVParser.parse(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullFormatAndFile() throws IOException {
        CSVParser.parse(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullCharset() throws IOException {
        CSVParser.parse(new URL("file:CSVParserTest.java"), null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullURL() throws IOException {
        CSVParser.parse(null, Charset.forName("UTF-8"), format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullURLAndCharset() throws IOException {
        CSVParser.parse(null, null, format);
    }
}