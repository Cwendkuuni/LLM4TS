package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.Token;

/**
 * Test class for CSVParser
 */
public class CSVParserTest {

    /**
     * Test parse method with simple CSV data
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParse() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        String[] values1 = record1.toArray(new String[record1.size()]);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record1.getHeader());
        assertEquals("John", values1[0]);
        assertEquals("25", values1[1]);
        assertEquals("New York", values1[2]);

        CSVRecord record2 = records.get(1);
        String[] values2 = record2.toArray(new String[record2.size()]);
        assertEquals("Mike", values2[0]);
        assertEquals("30", values2[1]);
        assertEquals("Chicago", values2[2]);

        parser.close();
    }

    /**
     * Test parse method with custom CSV format
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCustomFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago\n";

        CSVFormat format = new CSVFormat(',', QuotingMode.MINIMAL, null, null, null, false, false);

        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        String[] values1 = record1.toArray(new String[record1.size()]);
        assertEquals("Name", values1[0]);
        assertEquals("Age", values1[1]);
        assertEquals("City", values1[2]);

        CSVRecord record2 = records.get(1);
        String[] values2 = record2.toArray(new String[record2.size()]);
        assertEquals("John", values2[0]);
        assertEquals("25", values2[1]);
        assertEquals("New York", values2[2]);

        parser.close();
    }

    /**
     * Test parse method with empty CSV data
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseEmptyCSVData() throws IOException {
        String csvData = "";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    /**
     * Test parse method with invalid CSV data
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCSVData() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMike Age,Chicago\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();

        // The test should fail with an IOException
    }

    /**
     * Test iterator method
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testIterator() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        CSVRecordIterator iterator = records.iterator();

        CSVRecord record1 = iterator.next();
        assertEquals("Name", record1.get(0));
        assertEquals("Age", record1.get(1));
        assertEquals("City", record1.get(2));

        CSVRecord record2 = iterator.next();
        assertEquals("John", record2.get(0));
        assertEquals("25", record2.get(1));
        assertEquals("New York", record2.get(2));

        assertFalse(iterator.hasNext());

        parser.close();
    }

    /**
     * Test close method
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testClose() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMike,30,Chicago\n";

        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        parser.close();

        assertNull(parser.getRecords());
    }
}