package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.Token.Type.TOKEN;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.parser.Lexer;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test CSVParser.parse(File, CSVFormat)
     *
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        File file = new File("src/test/resources/csv/simple.csv");
        CSVParser parser = parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = null;
        while ((record = iterator.next()) != null) {
            System.out.println(record.getValues());
        }
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "30", "female"}, record1.getValues());
        assertEquals("Alice", record1.getHeaderName(0));
        assertEquals("30", record1.getHeaderName(1));
        assertEquals("female", record1.getHeaderName(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "25", "male"}, record2.getValues());
        assertEquals("Bob", record2.getHeaderName(0));
        assertEquals("25", record2.getHeaderName(1));
        assertEquals("male", record2.getHeaderName(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Charlie", "45", "male"}, record3.getValues());
        assertEquals("Charlie", record3.getHeaderName(0));
        assertEquals("45", record3.getHeaderName(1));
        assertEquals("male", record3.getHeaderName(2));
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test CSVParser.parse(String, CSVFormat)
     *
     * @throws IOException
     */
    @Test
    public void testParseString() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        CSVParser parser = parse(new StringReader(data), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = null;
        while ((record = iterator.next()) != null) {
            System.out.println(record.getValues());
        }
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "30", "female"}, record1.getValues());
        assertEquals("Alice", record1.getHeaderName(0));
        assertEquals("30", record1.getHeaderName(1));
        assertEquals("female", record1.getHeaderName(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "25", "male"}, record2.getValues());
        assertEquals("Bob", record2.getHeaderName(0));
        assertEquals("25", record2.getHeaderName(1));
        assertEquals("male", record2.getHeaderName(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Charlie", "45", "male"}, record3.getValues());
        assertEquals("Charlie", record3.getHeaderName(0));
        assertEquals("45", record3.getHeaderName(1));
        assertEquals("male", record3.getHeaderName(2));
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test CSVParser.parse(URL, Charset, CSVFormat)
     *
     * @throws IOException
     */
    @Test
    public void testParseURL() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String url = "classpath:csv/simple.csv";
        CSVParser parser = parse(new URL(url), Charset.forName("UTF-8"), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = null;
        while ((record = iterator.next()) != null) {
            System.out.println(record.getValues());
        }
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "30", "female"}, record1.getValues());
        assertEquals("Alice", record1.getHeaderName(0));
        assertEquals("30", record1.getHeaderName(1));
        assertEquals("female", record1.getHeaderName(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "25", "male"}, record2.getValues());
        assertEquals("Bob", record2.getHeaderName(0));
        assertEquals("25", record2.getHeaderName(1));
        assertEquals("male", record2.getHeaderName(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Charlie", "45", "male"}, record3.getValues());
        assertEquals("Charlie", record3.getHeaderName(0));
        assertEquals("45", record3.getHeaderName(1));
        assertEquals("male", record3.getHeaderName(2));
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test CSVParser.parse(Reader, CSVFormat)
     *
     * @throws IOException
     */
    @Test
    public void testParseReader() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        StringReader reader = new StringReader(data);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = null;
        while ((record = iterator.next()) != null) {
            System.out.println(record.getValues());
        }
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "30", "female"}, record1.getValues());
        assertEquals("Alice", record1.getHeaderName(0));
        assertEquals("30", record1.getHeaderName(1));
        assertEquals("female", record1.getHeaderName(2));
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "25", "male"}, record2.getValues());
        assertEquals("Bob", record2.getHeaderName(0));
        assertEquals("25", record2.getHeaderName(1));
        assertEquals("male", record2.getHeaderName(2));
        assertEquals(2, record2.getRecordNumber());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Charlie", "45", "male"}, record3.getValues());
        assertEquals("Charlie", record3.getHeaderName(0));
        assertEquals("45", record3.getHeaderName(1));
        assertEquals("male", record3.getHeaderName(2));
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test CSVParser.getRecords()
     *
     * @throws IOException
     */
    @Test
    public void testGetRecords() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        StringReader reader = new StringReader(data);
        CSVParser parser = new CSVParser(reader, format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Alice", "30", "female"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Bob", "25", "male"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Charlie", "45", "male"}, record3.getValues());

        List<CSVRecord> emptyList = Collections.emptyList();
        CSVParser emptyParser = parse(new StringReader(""), format);
        List<CSVRecord> emptyRecords = emptyParser.getRecords();
        emptyParser.close();

        assertSame(emptyList, emptyRecords);
    }

    /**
     * Test CSVParser.iterator()
     *
     * @throws IOException
     */
    @Test
    public void testIterator() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        StringReader reader = new StringReader(data);
        CSVParser parser = new CSVParser(reader, format);

        CSVRecordIterator iterator = parser.iterator();
        CSVRecord record = null;

        assertFalse(iterator.hasNext());

        parser.nextRecord();

        record = iterator.next();
        assertSame(parser.getRecord(0), record);
        assertEquals("Alice", record.getValues()[0]);

        record = iterator.next();
        assertSame(parser.getRecord(1), record);
        assertEquals("Bob", record.getValues()[0]);

        record = iterator.next();
        assertSame(parser.getRecord(2), record);
        assertEquals("Charlie", record.getValues()[0]);

        record = iterator.next();
        assertNull(record);
    }

    /**
     * Test CSVParser.getRecordNumber()
     *
     * @throws IOException
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        StringReader reader = new StringReader(data);
        CSVParser parser = parse(reader, format);

        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        CSVRecord record3 = parser.nextRecord();

        assertEquals(1, record1.getRecordNumber());
        assertEquals(2, record2.getRecordNumber());
        assertEquals(3, record3.getRecordNumber());
    }

    /**
     * Test CSVParser.close()
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        CSVFormat format = CSVFormat.RFC4180;
        String data = "Alice,30,female\nBob,25,male\nCharlie,45,male";
        StringReader reader = new StringReader(data);
        CSVParser parser = new CSVParser(reader, format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        try {
            parser.getRecords();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.iterator().next();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.nextRecord();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.getRecord(1);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.getHeaderMap();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.getCurrentLineNumber();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.getRecordNumber();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            parser.isClosed();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

}