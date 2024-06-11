package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Unit tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parse(File, CSVFormat) method.
     *
     * @throws IOException on file read error
     */
    @org.junit.Test
    public void testParseFile() throws IOException {
        String filePath = "test-files/example.csv";
        CSVParser parser = CSVParser.parse(new File(filePath), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
        for (CSVRecord record : records) {
            assertTrue(record.size() > 0);
            assertEquals(TYPE.STRING, record.get(0).getType());
            assertEquals(TYPE.INTEGER, record.get(1).getType());
            assertEquals(TYPE.DOUBLE, record.get(2).getType());
            assertEquals(TYPE.BOOLEAN, record.get(3).getType());
        }
    }

    /**
     * Test parse(String, CSVFormat) method.
     */
    @org.junit.Test
    public void testParseString() {
        String csvData = "name,age,price,enabled\nAlice,30,15.5,true\nBob,22,23.0,false";
        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            assertTrue(record.size() == 4);
            for (int i = 0; i < record.size(); i++) {
                assertEquals(TYPE.STRING, record.getType(i));
                if (i == 1) {
                    assertEquals(30, record.getInteger(i));
                } else if (i == 2) {
                    assertEquals(15.5, record.getDouble(i), 0.0);
                } else if (i == 3) {
                    assertEquals(true, record.getBoolean(i));
                }
            }
        }
    }

    /**
     * Test parse(URL, Charset, CSVFormat) method.
     *
     * @throws IOException on file read error
     */
    @org.junit.Test
    public void testParseURL() throws IOException {
        String urlString = "http://example.com/test.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), DEFAULT, DEFAULT.getCharset());
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        // Test record contents
        for (CSVRecord record : records) {
            assertTrue(record.size() > 0);
            assertEquals(TYPE.STRING, record.get(0).getType());
            assertEquals(TYPE.INTEGER, record.get(1).getType());
        }
        // Test record headers
        assertFalse(records.get(0).isHeader());
        assertFalse(records.get(1).isHeader());
        assertTrue(records.get(2).isHeader());
        // Test record numbers
        assertEquals(1, records.get(0).getRecordNumber());
        assertEquals(2, records.get(1).getRecordNumber());
        assertEquals(3, records.get(2).getRecordNumber());

        parser.close();
    }

    /**
     * Test getRecords() method.
     */
    @org.junit.Test
    public void testGetRecords() {
        String csvData = "name,age,price,enabled\nAlice,30,15.5,true\nBob,22,23.0,false";
        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        parser.close();
    }

    /**
     * Test getRecords() method with empty CSV data.
     */
    @org.junit.Test
    public void testGetRecordsEmpty() {
        String csvData = "";
        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    /**
     * Test getRecords() method with invalid CSV data.
     */
    @org.junit.Test
    public void testGetRecordsInvalid() {
        String csvData = "name,age,price,enabled\nAlice,30,15.5,true\nBob,22,23.0,false";
        CSVParser parser = new CSVParser(new StringReader("name,age,price,enabled\nAlice,30,15.5,\nBob,22,23.0,false"), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        assertEquals(3, records.get(0).size());
        // Test record contents
        assertEquals("Alice", records.get(0).get(0));
        assertEquals(30, records.get(0).getInteger(1));
        assertNull(records.get(0).getDouble(2));
        // Test record headers
        assertFalse(records.get(0).isHeader());
        parser.close();
    }
}