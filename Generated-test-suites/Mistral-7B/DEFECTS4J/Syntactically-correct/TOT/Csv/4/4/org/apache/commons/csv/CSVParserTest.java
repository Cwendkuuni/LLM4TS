package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test that {@link CSVParser#parse(java.io.File, CSVFormat)} correctly reads a simple CSV file with a single record.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseSimpleFile() throws IOException {
        final String filePath = "src/test/resources/simple.csv";
        final CSVParser parser = parse(new File(filePath), DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("header1", record.get(0));
        assertEquals("header2", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.File, CSVFormat)} correctly reads a CSV file with multiple records.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseMultipleRecordsFile() throws IOException {
        final String filePath = "src/test/resources/multiple-records.csv";
        final CSVParser parser = parse(new File(filePath), DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(3, firstRecord.size());
        assertEquals("header1", firstRecord.get(0));
        assertEquals("header2", firstRecord.get(1));
        assertEquals("header3", firstRecord.get(2));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("value1", secondRecord.get(0));
        assertEquals("value2", secondRecord.get(1));

        final CSVRecord thirdRecord = records.get(2);
        assertEquals(3, thirdRecord.size());
        assertEquals("value3", thirdRecord.get(0));
        assertEquals("value4", thirdRecord.get(1));
        assertEquals("value5", thirdRecord.get(2));

        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.File, CSVFormat)} correctly handles missing header in a CSV file.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseMissingHeaderFile() throws IOException {
        final String filePath = "src/test/resources/missing-header.csv";
        final CSVParser parser = parse(new File(filePath), DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.File, CSVFormat)} correctly handles empty CSV file.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseEmptyFile() throws IOException {
        final String filePath = "src/test/resources/empty.csv";
        final CSVParser parser = parse(new File(filePath), DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.File, CSVFormat)} correctly handles CSV file with malformed record.
     *
     * @throws IOException
     *             on read error
     */
    @Test(expected = IOException.class)
    public void testParseMalformedRecordFile() throws IOException {
        final String filePath = "src/test/resources/malformed-record.csv";
        parse(new File(filePath), DEFAULT);
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly reads a CSV input stream with a single record.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseSimpleReader() throws IOException {
        final String csvData = "header1,header2\na,b";
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("header1", record.getHeaderMap().get("header1"));
        assertEquals("header2", record.getHeaderMap().get("header2"));
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly reads a CSV input stream with multiple records.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseMultipleRecordsReader() throws IOException {
        final String csvData = "header1,header2,header3\na,b,c\nd,e,f\ne,g,h";
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(3, firstRecord.size());
        assertEquals("header1", firstRecord.getHeaderMap().get("header1"));
        assertEquals("header2", firstRecord.getHeaderMap().get("header2"));
        assertEquals("header3", firstRecord.getHeaderMap().get("header3"));
        assertEquals("a", firstRecord.get(0));
        assertEquals("b", firstRecord.get(1));
        assertEquals("c", firstRecord.get(2));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(3, secondRecord.size());
        assertEquals("header1", secondRecord.getHeaderMap().get("header1"));
        assertEquals("header2", secondRecord.getHeaderMap().get("header2"));
        assertEquals("header3", secondRecord.getHeaderMap().get("header3"));
        assertEquals("d", secondRecord.get(0));
        assertEquals("e", secondRecord.get(1));
        assertEquals("f", secondRecord.get(2));

        final CSVRecord thirdRecord = records.get(2);
        assertEquals(3, thirdRecord.size());
        assertEquals("header1", thirdRecord.getHeaderMap().get("header1"));
        assertEquals("header2", thirdRecord.getHeaderMap().get("header2"));
        assertEquals("header3", thirdRecord.getHeaderMap().get("header3"));
        assertEquals("e", thirdRecord.get(0));
        assertEquals("g", thirdRecord.get(1));
        assertEquals("h", thirdRecord.get(2));

        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly handles missing header in a CSV input stream.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseMissingHeaderReader() throws IOException {
        final String csvData = "a,b";
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly handles empty CSV input stream.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseEmptyReader() throws IOException {
        final String csvData = "";
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly handles CSV input stream with malformed record.
     *
     * @throws IOException
     *             on read error
     */
    @Test(expected = IOException.class)
    public void testParseMalformedRecordReader() throws IOException {
        final String csvData = "header1,header2\na,b,\n";
        final Reader reader = new StringReader(csvData);
        new CSVParser(reader, DEFAULT);
    }

    /**
     * Test that {@link CSVParser#parse(java.net.URL, Charset, CSVFormat)} correctly reads a CSV resource.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseResource() throws IOException {
        final String urlPath = "org/apache/commons/csv/test/resources/simple.csv";
        final CSVParser parser = parse(new URL(urlPath), Charset.forName("UTF-8"), DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("header1", record.get(0));
        assertEquals("header2", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly parses CSV records with custom header.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseCustomHeader() throws IOException {
        final String csvData = "column1,column2\na,column3\nb,column4";
        final CSVFormat format = new CSVFormat(DEFAULT, ',', '\n', "\r\n");
        format.setHeader(new String[]{"column1_expected", "column2_expected"});
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(2, firstRecord.size());
        assertEquals("column1_expected", firstRecord.getHeaderMap().get("column1_expected"));
        assertEquals("column3", firstRecord.get(0));
        assertEquals("column2_expected", firstRecord.getHeaderMap().get("column2_expected"));
        assertEquals(null, firstRecord.get(1));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("column1_expected", secondRecord.getHeaderMap().get("column1_expected"));
        assertEquals("column4", secondRecord.get(0));
        assertEquals("column2_expected", secondRecord.getHeaderMap().get("column2_expected"));
        assertEquals("b", secondRecord.get(1));

        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly handles missing header in a CSV input stream with custom header.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseMissingCustomHeaderReader() throws IOException {
        final String csvData = "a,b,c";
        final CSVFormat format = new CSVFormat(DEFAULT, ',', '\n', "\r\n");
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(3, record.size());
        assertEquals(null, record.getHeaderMap().get("column1_expected"));
        assertEquals("a", record.get(0));
        assertEquals(null, record.getHeaderMap().get("column2_expected"));
        assertEquals("c", record.get(1));
        parser.close();
    }

    /**
     * Test that {@link CSVParser#parse(java.io.Reader)} correctly handles empty CSV input stream with custom header.
     *
     * @throws IOException
     *             on read error
     */
    @Test
    public void testParseEmptyCustomHeaderReader() throws IOException {
        final String csvData = "";
        final CSVFormat format = new CSVFormat(DEFAULT, ',', '\n', "\r\n");
        final Reader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }
}