package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVParserBuilder;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.CSVFormatExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CSVParserTest {

    private File file;
    private CSVFormat format;

    public CSVParserTest(File file, CSVFormat format) {
        this.file = file;
        this.format = format;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new File("src/test/resources/testCSVFile.csv"), CSVFormat.EXCEL },
                { new File("src/test/resources/testCSVFile2.csv"), CSVFormat.RFC4180 },
                { new File("src/test/resources/testCSVFile3.csv"), CSVFormat.DEFAULT },
                { new File("src/test/resources/testCSVFile4.csv"), new CSVFormatBuilder().setHeaderNames("Name", "Age")
                        .setDelimiter(';').build() }
        });
    }

    @Test
    public void testParse() throws IOException {
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record;
        int index = 1;

        // Test that the correct number of records are being read
        assertEquals(10, records.size());

        // Test that the records can be iterated through
        for (CSVRecord csvRecord : parser) {
            record = csvRecord;
            String name = record.get(0);
            int age = (int) record.get(1);

            // Test that the headers match the names in the CSV file
            assertEquals("Test Person " + index++, name);
            assertEquals(index, age);
        }

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("src/test/resources/testInvalidFile.csv"), format);
        parser.nextRecord();
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        CSVParser parser = CSVParser.parse(file, null);
        parser.nextRecord();
        parser.close();
    }

    @Test
    public void testParseCommented() throws IOException {
        StringReader stringReader = new StringReader("Name,Age\r\nTest Person 1,20\r\n--\r\nName,Age\r\nTest Person 2,25");
        CSVParser parser = new CSVParser(stringReader, format);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record;

        // Test that the correct number of records are being read
        assertEquals(2, records.size());

        // Test that the records can be iterated through
        for (CSVRecord csvRecord : parser) {
            record = csvRecord;
            String name = record.get(0);
            int age = (int) record.get(1);

            if (name.equals("Test Person 1")) {
                assertNull(record.getComment());
            } else {
                assertSame("Test Person 2 record comment", "Comment for Test Person 2", record.getComment());
            }

            assertEquals("Test Person " + index++, name);
            assertEquals(index, age);
        }

        parser.close();
    }

    @Test
    public void testParseNullValues() throws IOException {
        StringReader stringReader = new StringReader("Name,Age\r\nTest Person 1,20\r\nTest Person 2,null");
        CSVParser parser = new CSVParser(stringReader, format);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record;

        // Test that the correct number of records are being read
        assertEquals(2, records.size());

        // Test that the records can be iterated through
        for (CSVRecord csvRecord : parser) {
            record = csvRecord;
            String name = record.get(0);
            Object age = record.get(1);

            if (name.equals("Test Person 1")) {
                assertEquals(20, age);
            } else {
                assertNull(age);
            }
        }

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        StringReader stringReader = new StringReader("Name,Age\r\nTest Person 1,20\r\nTest Person 2,25");
        CSVParser parser = new CSVParser(stringReader, new CSVFormatBuilder().setHeaderNames("Name", "Age").build());
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record;

        // Test that the correct number of records are being read
        assertEquals(2, records.size());

        // Test that the records can be iterated through
        for (CSVRecord csvRecord : parser) {
            record = csvRecord;
            String name = record.get(0);
            int age = (int) record.get(1);

            assertEquals("Test Person " + index++, name);
            assertEquals(index, age);
        }

        parser.close();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("src/test/resources/testEmptyFile.csv"), format);
        assertNull(parser.getRecords());
        parser.close();
    }
}