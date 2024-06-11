package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 3));
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with comments.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        String csvData = "name,age\nJohn Doe,30\n-- Comment for this record\nJane Doe,28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecord record = parser.nextRecord();
        assertEquals("name[0]", record.get(0), "name");
        assertEquals("age[0]", record.get(1), "30");
        assertNull("comment should be null", record.getComment());

        record = parser.nextRecord();
        assertEquals("name[1]", record.get(0), "Jane Doe");
        assertEquals("age[1]", record.get(1), "28");
        assertEquals("-- Comment for this record", record.getComment());

        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with empty values.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithEmptyValues() throws IOException {
        String csvData = "name,age\nJohn Doe,\nJane Doe,28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            if (index == 0) {
                assertNull("age[0] should be null", record.get(1));
            } else {
                assertEquals("age[" + index + "]", record.get(1), "28");
            }
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with malformed input.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseMalformedCSV() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28,\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // This will throw an IOException
    }

    /**
     * Test parsing a CSV file with malformed input (missing quotes).
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseMalformedCSVMissingQuotes() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28,Smith";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord(); // This will throw an IOException
    }

    /**
     * Test parsing a CSV file with a large number of records.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithLargeNumberOfRecords() throws IOException {
        String csvData = "name,age\n";
        for (int i = 0; i < 100000; i++) {
            csvData += String.format("%s,%d%n", "Record " + i, i + 1);
        }
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        int index = 0;
        CSVRecord record;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "Record " + index);
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 1));
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test parsing a CSV file from a byte array.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVFromByteArray() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28";
        byte[] byteArray = csvData.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        CSVParser parser = CSVParser.parse(bais, CSVFormat.DEFAULT);

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 3));
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with header.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withHeader());

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 3));
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("name"));
        assertTrue(headerMap.containsKey("age"));
    }

    /**
     * Test parsing a CSV file with custom format.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "name;age\nJohn Doe;30\nJane Doe;28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withDelimiter(';'));

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 3));
            index++;
        }
        assertFalse("parser should not have any more records", parser.hasNext());
    }

    /**
     * Test closing the parser.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCloseParser() throws IOException {
        String csvData = "name,age\nJohn Doe,30\nJane Doe,28";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecord record;
        int index = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals("name[" + index + "]", record.get(0), "name");
            assertEquals("age[" + index + "]", record.get(1), Integer.toString(index + 3));
            index++;
        }

        parser.close();

        try {
            parser.nextRecord();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        assertFalse("parser should not have any records", parser.hasNext());
        parser.close();
    }
}