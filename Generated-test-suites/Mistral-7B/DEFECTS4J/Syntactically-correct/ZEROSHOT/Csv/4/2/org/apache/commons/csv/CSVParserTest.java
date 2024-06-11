package org.apache.commons.csv;

import static org.apache.commons.csv.CSVRecord.*;
import static org.apache.commons.csv.CSVParser.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.junit.Test;

/**
 * Test class for CSVParser class.
 */
public class CSVParserTest {

    /**
     * Test parse method with valid CSV data from file.
     *
     * @throws IOException
     */
    @Test
    public void testParseFromFile() throws IOException {
        File csvFile = new File("testData.csv");
        CSVParser parser = parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals(1, record1.size());
        assertEquals("A1", record1.get(0));

        assertEquals(2, record2.size());
        assertEquals("B1", record2.get(0));
        assertEquals("C1", record2.get(1));

        assertEquals(3, record3.size());
        assertEquals("D1", record3.get(0));
        assertEquals("E1", record3.get(1));
        assertEquals("F1", record3.get(2));
    }

    /**
     * Test parse method with valid CSV data from String.
     */
    @Test
    public void testParseFromString() {
        String csvData = "A1\nB1,C1\nD1,E1,F1";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()),
                Charset.forName("UTF-8"));
        CSVParser parser = parse(inputStreamReader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals(1, record1.size());
        assertEquals("A1", record1.get(0));

        assertEquals(2, record2.size());
        assertEquals("B1", record2.get(0));
        assertEquals("C1", record2.get(1));

        assertEquals(3, record3.size());
        assertEquals("D1", record3.get(0));
        assertEquals("E1", record3.get(1));
        assertEquals("F1", record3.get(2));
    }

    /**
     * Test parse method with valid CSV data from URL.
     *
     * @throws IOException
     */
    @Test
    public void testParseFromURL() throws IOException {
        String csvUrl = "https://example.com/testData.csv";
        CSVParser parser = parse(new URL(csvUrl), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals(1, record1.size());
        assertEquals("A1", record1.get(0));

        assertEquals(2, record2.size());
        assertEquals("B1", record2.get(0));
        assertEquals("C1", record2.get(1));

        assertEquals(3, record3.size());
        assertEquals("D1", record3.get(0));
        assertEquals("E1", record3.get(1));
        assertEquals("F1", record3.get(2));
    }

    /**
     * Test parse method with invalid CSV data from file.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testParseFromFileInvalid() throws IOException {
        File csvFile = new File("testDataInvalid.csv");
        CSVParser parser = parse(csvFile, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
    }

    /**
     * Test parse method with empty CSV data from file.
     *
     * @throws IOException
     */
    @Test
    public void testParseFromFileEmpty() throws IOException {
        File csvFile = new File("testDataEmpty.csv");
        CSVParser parser = parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(0, records.size());
    }

    /**
     * Test parse method with custom CSVFormat.
     *
     * @throws IOException
     */
    @Test
    public void testParseWithCustomFormat() throws IOException {
        String csvData = "A1\nB1,C1\nD1,E1,F1";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()),
                Charset.forName("UTF-8"));
        CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames("A", "B", "C").build();
        CSVParser parser = parse(inputStreamReader, customFormat);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals(1, record1.size());
        assertEquals("A1", record1.get("A"));

        assertEquals(2, record2.size());
        assertEquals("B1", record2.get("B"));
        assertEquals("C1", record2.get("C"));

        assertEquals(3, record3.size());
        assertEquals("D1", record3.get("D"));
        assertEquals("E1", record3.get("E"));
        assertEquals("F1", record3.get("F"));
    }

    /**
     * Test parse method with custom CSVFormat and header names.
     *
     * @throws IOException
     */
    @Test
    public void testParseWithCustomFormatAndHeaderNames() throws IOException {
        String csvData = "A1\nB1,C1\nD1,E1,F1";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(csvData.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()),
                Charset.forName("UTF-8"));
        CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames("Name", "Value").build();
        CSVParser parser = parse(inputStreamReader, customFormat);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        CSVRecord record3 = records.get(2);

        assertEquals(2, record1.size());
        assertEquals("A1", record1.get("Name"));
        assertEquals("A1", record1.get("Value"));

        assertEquals(2, record2.size());
        assertEquals("B1", record2.get("Name"));
        assertEquals("C1", record2.get("Value"));

        assertEquals(3, record3.size());
        assertEquals("D1", record3.get("Name"));
        assertEquals("E1", record3.get("Value"));
        assertEquals("F1", record3.get("Value"));
    }
}