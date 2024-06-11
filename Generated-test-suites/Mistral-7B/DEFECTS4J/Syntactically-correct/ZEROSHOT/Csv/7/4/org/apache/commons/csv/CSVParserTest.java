package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /** The test data as a CSV string. */
    private static final String TEST_DATA = "name,age,country\nAlice,30,USA\nBob,42,Canada\n";

    /** The test data as a byte array. */
    private static final byte[] TEST_DATA_BYTE_ARRAY = TEST_DATA.getBytes();

    /** The test data as a CSV record array. */
    private static final CSVRecord[] TEST_DATA_RECORD_ARRAY = new CSVRecord[] {
            new CSVRecord(new String[] { "Alice", "30", "USA" }, Collections.emptyMap(), null, 1),
            new CSVRecord(new String[] { "Bob", "42", "Canada" }, Collections.emptyMap(), null, 2)
    };

    /**
     * Tests the creation of a parser from a file.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testParseFile() throws IOException {
        final File file = new File("test.csv");
        try (CSVParser parser = parse(file, DEFAULT)) {
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(TEST_DATA_RECORD_ARRAY), records);
        }
        assertFalse(file.exists());
    }

    /**
     * Tests the creation of a parser from a string.
     */
    @Test
    public void testParseString() {
        try (CSVParser parser = parse(TEST_DATA, DEFAULT)) {
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(TEST_DATA_RECORD_ARRAY), records);
        }
    }

    /**
     * Tests the creation of a parser from a byte array.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testParseByteArray() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(TEST_DATA_BYTE_ARRAY);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                CSVParser parser = parse(inputStream, DEFAULT)) {
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(TEST_DATA_RECORD_ARRAY), records);
        }

        outputStream.close();
    }

    /**
     * Tests the creation of a parser from an {@link InputStreamReader}.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testParseInputStreamReader() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA_BYTE_ARRAY);
        try (InputStreamReader reader = new InputStreamReader(inputStream);
                CSVParser parser = parse(reader, DEFAULT)) {
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(TEST_DATA_RECORD_ARRAY), records);
        }

        inputStream.close();
    }

    /**
     * Tests the getRecordNumber() method.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        try (CSVParser parser = parse(TEST_DATA, DEFAULT)) {
            final CSVRecord record = parser.nextRecord();
            assertTrue(record.getRecordNumber() == 1);
            final CSVRecord record2 = parser.nextRecord();
            assertTrue(record2.getRecordNumber() == 2);
            assertNull(parser.nextRecord());
        }
    }

    /**
     * Tests the getHeaderMap() method.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        try (CSVParser parser = parse(TEST_DATA, DEFAULT)) {
            final Map<String, Integer> headerMap = parser.getHeaderMap();
            assertSame(Collections.emptyMap(), headerMap);
        }
    }

    /**
     * Tests the iterator() method.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testIterator() throws IOException {
        try (CSVParser parser = parse(TEST_DATA, DEFAULT)) {
            final List<CSVRecord> records = new ArrayList<CSVRecord>();
            for (CSVRecord record : parser) {
                records.add(record);
            }
            assertEquals(Arrays.asList(TEST_DATA_RECORD_ARRAY), records);
        }
    }

    /**
     * Tests the close() method.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Test
    public void testClose() throws IOException {
        try (CSVParser parser = parse(TEST_DATA, DEFAULT)) {
            parser.close();
            assertFalse(parser.isClosed());
            parser.close();
            assertTrue(parser.isClosed());
        }
    }
}