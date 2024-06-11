package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test for {@link CSVParser} class.
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25\n";
        this.parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        if (this.parser != null) {
            this.parser.close();
            this.parser = null;
        }
    }

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException on failure
     */
    @Test
    public void testParseCSV() throws IOException {
        CSVRecord record;
        int index = 0;
        while ((record = this.parser.nextRecord()) != null) {
            Assert.assertEquals("Expected record number: " + index++, index, record.getRecordNumber());
            Assert.assertNotNull("Record is null", record);
            Assert.assertEquals("Name", record.getHeaderMap().get("Name"), record.getHeaderMap().get(0));
            Assert.assertEquals("John Doe", record.get("Name"));
            Assert.assertEquals("30", record.get("Age"));
        }
        Assert.assertFalse("No more records to parse", this.parser.hasNext());
    }

    /**
     * Test parsing a CSV file with comments.
     *
     * @throws IOException on failure
     */
    @Test
    public void testParseCSVWithComments() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\n---\nJane Doe,25\n";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"), record.getHeaderMap().get(0));
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));
        Assert.assertNull("Comment was not read", record.getComment());

        record = parser.nextRecord();
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"), record.getHeaderMap().get(0));
        Assert.assertEquals("Jane Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("---\n", record.getComment());

        Assert.assertFalse("No more records to parse", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with an empty line.
     *
     * @throws IOException on failure
     */
    @Test
    public void testParseCSVWithEmptyLine() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\n\nJane Doe,25\n";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"), record.getHeaderMap().get(0));
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertEquals("30", record.get("Age"));

        record = parser.nextRecord();
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"), record.getHeaderMap().get(0));
        Assert.assertEquals("Jane Doe", record.get("Name"));
        Assert.assertEquals("25", record.get("Age"));

        Assert.assertFalse("No more records to parse", parser.hasNext());
    }

    /**
     * Test parsing a CSV file with an invalid format.
     *
     * @throws IOException on failure
     */
    @Test(expected = IOException.class)
    public void testParseCSVWithInvalidFormat() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30,\nJane Doe,25";
        new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
    }
}