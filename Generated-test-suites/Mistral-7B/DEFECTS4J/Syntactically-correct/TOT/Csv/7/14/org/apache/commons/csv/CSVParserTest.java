package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test for {@link CSVParser#parse(java.io.File, CSVFormat)} with an empty CSV file.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        final File file = new File("src/test/resources/empty.csv");
        final CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertSame(Collections.emptyList(), records);
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.File, CSVFormat)} with a simple single-line CSV file.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseSimpleSingleLineCSV() throws IOException {
        final File file = new File("src/test/resources/simple.csv");
        final CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record = records.get(0);
        assertSame(Arrays.asList("flag", "Sweden", "Stockholm").toArray(), record.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.File, CSVFormat)} with a simple multi-line CSV file.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseSimpleMultiLineCSV() throws IOException {
        final File file = new File("src/test/resources/simple_multi_line.csv");
        final CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record = records.get(0);
        assertSame(Arrays.asList("flag", "Sweden", "Stockholm").toArray(), record.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.File, CSVFormat)} with a CSV file containing headers.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseCSVWithHeaders() throws IOException {
        final File file = new File("src/test/resources/headers.csv");
        final CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180.withHeader());
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record = records.get(0);
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("age", record.getHeaderMap().get("age"));
        assertSame(Arrays.asList("John Doe", "30").toArray(), record.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.StringReader, CSVFormat)} with a CSV string.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseCSVString() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream);
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record1 = records.get(0);
        assertSame(Arrays.asList("flag", "Sweden", "Stockholm").toArray(), record1.getValues());
        final CSVRecord record2 = records.get(1);
        assertSame(Arrays.asList("flag", "Norway", "Oslo").toArray(), record2.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.InputStreamReader, CSVFormat)} with a CSV stream.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseCSVStream() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(csvString.getBytes());
        final InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record1 = records.get(0);
        assertSame(Arrays.asList("flag", "Sweden", "Stockholm").toArray(), record1.getValues());
        final CSVRecord record2 = records.get(1);
        assertSame(Arrays.asList("flag", "Norway", "Oslo").toArray(), record2.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.net.URL, CSVFormat, java.nio.charset.Charset)} with a CSV URL.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseCSVURL() throws IOException {
        final String urlString = "http://example.com/csv.csv";
        final URL url = new URL(urlString);
        final CSVParser parser = CSVParser.parse(url, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record = records.get(0);
        assertNull(record); // The test assumes the URL does not contain any data
        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(java.io.Reader)} with a CSV reader.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testParseCSVReader() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayReader reader = new ByteArrayReader(csvString.getBytes());
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final CSVRecord record1 = records.get(0);
        assertSame(Arrays.asList("flag", "Sweden", "Stockholm").toArray(), record1.getValues());
        final CSVRecord record2 = records.get(1);
        assertSame(Arrays.asList("flag", "Norway", "Oslo").toArray(), record2.getValues());
        parser.close();
    }

    /**
     * Test for {@link CSVParser#getRecordNumber()} with a parser that has read all records.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testGetRecordNumber() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayReader reader = new ByteArrayReader(csvString.getBytes());
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        final long recordNumber = parser.getRecordNumber();
        assertEquals(2, recordNumber);
        parser.close();
    }

    /**
     * Test for {@link CSVParser#getRecordNumber()} with a parser that has not read all records.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testGetRecordNumberUnread() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayReader reader = new ByteArrayReader(csvString.getBytes());
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        parser.nextRecord();
        final long recordNumber = parser.getRecordNumber();
        assertEquals(1, recordNumber);
        parser.close();
    }

    /**
     * Test for {@link CSVParser#close()}
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayReader reader = new ByteArrayReader(csvString.getBytes());
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        parser.close();
        try {
            parser.nextRecord();
            fail();
        } catch (final NoSuchElementException expected) {
            // expected
        }
    }

    /**
     * Test for {@link CSVParser#close()} with a closed parser.
     *
     * @throws IOException on I/O error
     */
    @org.junit.Test
    public void testCloseTwice() throws IOException {
        final String csvString = "flag,Sweden,Stockholm\nflag,Norway,Oslo";
        final ByteArrayReader reader = new ByteArrayReader(csvString.getBytes());
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.RFC4180);
        parser.close();
        parser.close();
        try {
            parser.nextRecord();
            fail();
        } catch (final NoSuchElementException expected) {
            // expected
        }
    }
}