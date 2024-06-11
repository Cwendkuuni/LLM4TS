package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String resourceName = "simple.csv";
        final CSVParser parser = CSVParser.parse(new FileReader(new File(getClass().getResource(resourceName).getFile())),
                CSVFormat.DEFAULT);

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals("Value " + index++, record.get(0));
            assertEquals("Value " + index++, record.get(1));
        }
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        final String resourceName = "header.csv";
        final CSVParser parser = CSVParser.parse(new FileReader(new File(getClass().getResource(resourceName).getFile())),
                CSVFormat.DEFAULT.withFirstRecordAsHeader());

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals("Name" + index, record.get(0));
            assertEquals("Age" + index, record.get(1));
            assertEquals(Integer.valueOf(index * 2), record.get(2));
        }
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test parsing a CSV file with comments.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseCSVWithComments() throws IOException {
        final String resourceName = "comments.csv";
        final CSVParser parser = CSVParser.parse(new FileReader(new File(getClass().getResource(resourceName).getFile())),
                CSVFormat.DEFAULT);

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals("Value " + index++, record.get(0));
            assertEquals("Value " + index++, record.get(1));
            assertEquals("---", record.getComment());
        }
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        final String resourceName = "empty.csv";
        final CSVParser parser = CSVParser.parse(new FileReader(new File(getClass().getResource(resourceName).getFile())),
                CSVFormat.DEFAULT);
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test parsing an invalid CSV file.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseInvalidCSV() throws IOException {
        final String resourceName = "invalid.csv";
        assertThrows(IOException.class, () -> CSVParser.parse(new FileReader(new File(getClass().getResource(resourceName)
                .getFile())), CSVFormat.DEFAULT));
    }

    /**
     * Test parsing a CSV file with a missing header.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseCSVWithoutHeader() throws IOException {
        final String resourceName = "header.csv";
        final String[] header = new String[]{"Name", "Age", "Value"};
        final CSVParser parser = CSVParserBuilder.create(new FileReader(new File(getClass().getResource(resourceName)
                .getFile()))).withCSVFormat(CSVFormat.DEFAULT.withHeader(header)).build();

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals("Value " + index++, record.get(index));
        }
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException on error
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final String resourceName = "custom.csv";
        final String[] header = new String[]{"Name", "Age", "Value"};
        final CSVParser parser = CSVParserBuilder.create(new FileReader(new File(getClass().getResource(resourceName)
                .getFile()))).withCSVFormat(new CSVFormat(StandardCharsets.UTF_8, header, null, '\t'))
                .build();

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals("Value " + index++, record.get(index));
        }
        assertNull(parser.nextRecord());
        parser.close();
    }
}