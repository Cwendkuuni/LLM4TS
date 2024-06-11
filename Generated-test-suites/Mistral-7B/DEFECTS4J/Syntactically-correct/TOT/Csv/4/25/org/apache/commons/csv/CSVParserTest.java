package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

/**
 * Test cases for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with default format.
     */
    @org.junit.Test
    public void testSimpleCSV() throws IOException {
        final String input = "a,b\nc,d\ne,f,g";

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());
        assertTrue(parser.getRecords().size() == 3);

        CSVRecord record1 = records.get(0);
        assertNotNull(record1);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertNotNull(record2);
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertNotNull(record3);
        assertArrayEquals(new String[]{"e", "f", "g"}, record3.getValues());
        assertNull(record3.getComment());

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @org.junit.Test
    public void testCustomCSVFormat() throws IOException {
        final String input = "col1,col2,col3\na,b,c\n1,2,3\n4,5,6";

        CSVFormat customFormat = CSVFormat.newBuilder().setHeaderNames(Arrays.asList("col1", "col2", "col3"))
                .build();

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), customFormat);
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());
        assertTrue(parser.getRecords().size() == 3);

        CSVRecord record1 = records.get(0);
        assertNotNull(record1);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        assertNotNull(record2);
        assertArrayEquals(new String[]{"1", "2"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        assertNotNull(record3);
        assertArrayEquals(new String[]{"4", "5"}, record3.getValues());

        parser.close();
    }

    /**
     * Test parsing a CSV file with a comment.
     */
    @org.junit.Test
    public void testCSVWithComment() throws IOException {
        final String input = "a,b\nc,d#This is a comment\ne,f,g";

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());
        assertTrue(parser.getRecords().size() == 3);

        CSVRecord record1 = records.get(0);
        assertNotNull(record1);
        assertArrayEquals(new String[]{"a", "b"}, record1.getValues());
        assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        assertNotNull(record2);
        assertArrayEquals(new String[]{"c", "d"}, record2.getValues());
        assertNotNull(record2.getComment());
        assertTrue(record2.getComment().startsWith("This is a comment"));

        parser.close();
    }

    /**
     * Test parsing an empty CSV file.
     */
    @org.junit.Test
    public void testEmptyCSV() throws IOException {
        final String input = "";

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertTrue(parser.getRecords().isEmpty());
        parser.close();
    }

    /**
     * Test parsing a CSV file with an incorrect format.
     */
    @org.junit.Test(expected = IOException.class)
    public void testIncorrectCSVFormat() throws IOException {
        final String input = "a,b\n1,2,3,4";

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), DEFAULT);
        parser.getRecords();
        parser.close();
    }

    /**
     * Test parsing a large CSV file.
     */
    @org.junit.Test
    public void testLargeCSVFile() throws IOException {
        final int recordCount = 10000;
        final String inputTemplate = "value%d,value%d,value%d,value%d\n";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < recordCount; i++) {
            out.write(String.format(inputTemplate, i, i, i, i).getBytes());
        }
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(out.toByteArray()));

        CSVParser parser = parse(reader, DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());
        assertTrue(parser.getRecords().size() == recordCount);

        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format and header.
     */
    @org.junit.Test
    public void testCustomCSVFormatWithHeader() throws IOException {
        final String input = "col1,col2,col3\na,b,c\n1,2,3";

        CSVFormat customFormat = CSVFormat.newBuilder()
                .setHeaderNames(Collections.singletonList("customColumn1"))
                .build();

        CSVParser parser = parse(new ByteArrayInputStream(input.getBytes()), customFormat);
        List<CSVRecord> records = parser.getRecords();

        assertFalse(parser.isClosed());
        assertTrue(parser.getRecords().size() == 2);

        CSVRecord record1 = records.get(0);
        assertNotNull(record1);
        assertNull(record1.getValues());
        assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        assertNotNull(record2);
        assertArrayEquals(new String[]{"1"}, record2.getValues());
        assertNull(record2.getComment());
        parser.close();
    }
}