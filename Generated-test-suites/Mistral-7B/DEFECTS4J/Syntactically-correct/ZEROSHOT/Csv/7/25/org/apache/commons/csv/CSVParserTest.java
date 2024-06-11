package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    @org.junit.Test
    public void testParseSimpleCsv() throws IOException {
        String csvContent = "a,b,c";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertEquals(3, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        parser.close();
    }

    @org.junit.Test
    public void testParseEmptyCsv() throws IOException {
        String csvContent = "";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithNullValues() throws IOException {
        String csvContent = "a,b,c,d";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withNullString(""));
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertEquals(4, record.size());
        assertEquals("a", record.get(0));
        assertNull(record.get(1));
        assertEquals("c", record.get(2));
        assertEquals("d", record.get(3));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithHeader() throws IOException {
        String csvContent = "header1,header2,header3\n1,2,3\n4,5,6";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(3, record1.size());
        assertEquals("header1", record1.get(0));
        assertEquals("1", record1.get(1));
        assertEquals("3", record1.get(2));
        assertEquals(3, record2.size());
        assertEquals("header1", record2.get(0));
        assertEquals("4", record2.get(1));
        assertEquals("6", record2.get(2));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithComment() throws IOException {
        String csvContent = "a,b\n# this is a comment\n1,2";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("1", record.get(1));
        assertNull(record.getComments());
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithCustomFormat() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25";
        CSVFormat csvFormat = new CSVFormatBuilder() //
                .setHeaderNames(Arrays.asList("name", "age")) //
                .build();
        CSVParser parser = parse(new StringReader(csvContent), csvFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(2, record1.size());
        assertEquals("name", record1.getHeaderName(0));
        assertEquals("Alice", record1.get(0));
        assertEquals(30, record1.get(1).intValue());
        assertEquals(2, record2.size());
        assertEquals("name", record2.getHeaderName(0));
        assertEquals("Bob", record2.get(0));
        assertEquals(25, record2.get(1).intValue());
        parser.close();
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseCsvWithInvalidFormat() throws IOException {
        String csvContent = "a,b\n1,2,\n3";
        parse(new StringReader(csvContent), CSVFormat.DEFAULT);
    }

    @org.junit.Test
    public void testParseCsvWithEmptyLine() throws IOException {
        String csvContent = "a,b\n1,2\n\n3,4";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(3, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("1", record1.get(1));
        assertEquals("3", record1.get(2));
        assertEquals(3, record2.size());
        assertEquals("a", record2.get(0));
        assertEquals("", record2.get(1));
        assertEquals("4", record2.get(2));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithTrailingComma() throws IOException {
        String csvContent = "a,b\n1,2,\n3,,";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(3, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("1", record1.get(1));
        assertEquals("3", record1.get(2));
        assertEquals(3, record2.size());
        assertEquals("a", record2.get(0));
        assertEquals(null, record2.get(1));
        assertEquals("", record2.get(2));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithEmptyLinesBetweenRecords() throws IOException {
        String csvContent = "a,b\n1,2\n\n3,4\n";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(3, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("1", record1.get(1));
        assertEquals("3", record1.get(2));
        assertEquals(3, record2.size());
        assertEquals("a", record2.get(0));
        assertEquals("4", record2.get(1));
        assertEquals("", record2.get(2));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithCustomDelimiter() throws IOException {
        String csvContent = "col1|col2\nvalue1|value2\nvalue3|value4";
        CSVFormat csvFormat = new CSVFormatBuilder() //
                .setDelimiter('|') //
                .build();
        CSVParser parser = parse(new StringReader(csvContent), csvFormat);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(2, record1.size());
        assertEquals("col1", record1.getHeaderName(0));
        assertEquals("value1", record1.get(0));
        assertEquals("col2", record1.getHeaderName(1));
        assertEquals("value2", record1.get(1));
        assertEquals(2, record2.size());
        assertEquals("col1", record2.getHeaderName(0));
        assertEquals("value3", record2.get(0));
        assertEquals("col2", record2.getHeaderName(1));
        assertEquals("value4", record2.get(1));
        parser.close();
    }

    @org.junit.Test
    public void testParseCsvWithSkipHeaderRecord() throws IOException {
        String csvContent = "header1,header2\n1,2\n3,4";
        CSVFormat csvFormat = new CSVFormatBuilder() //
                .setHeaderNames(Arrays.asList("header1", "header2")) //
                .build();
        CSVParser parser = parse(new StringReader(csvContent), csvFormat.withSkipHeaderRecord());
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals(2, record1.size());
        assertEquals("1", record1.get(0));
        assertEquals("4", record1.get(1));
        assertEquals(2, record2.size());
        assertEquals("3", record2.get(0));
        assertEquals("2", record2.get(1));
        parser.close();
    }

    @org.junit.Test
    public void testIteration() throws IOException {
        String csvContent = "header1,header2,header3\n1,2,3\n4,5,6";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        int i = 0;
        for (CSVRecord record : parser) {
            assertEquals(3, record.size());
            assertEquals(String.valueOf(i + 1), record.get(0).toString());
            assertEquals(String.valueOf(i + 2), record.get(1).toString());
            assertEquals(String.valueOf(i + 3), record.get(2).toString());
            i++;
        }
        parser.close();
    }

    @org.junit.Test
    public void testIterationWithEmptyRecords() throws IOException {
        String csvContent = "header1,header2\n1,2\n\n3,4\n";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        int i = 0;
        for (CSVRecord record : parser) {
            assertEquals(3, record.size());
            if (i == 1) {
                assertFalse(record.get(1) instanceof String);
                assertEquals(Collections.emptyList(), record.getValuesList());
            }
            assertEquals(String.valueOf(i + 1), record.getHeaderName(0));
            assertEquals(String.valueOf(i + 1), record.get(0).toString());
            assertEquals(String.valueOf(i + 3), record.get(2).toString());
            i++;
        }
        parser.close();
    }

    @org.junit.Test
    public void testIterationWithCustomDelimiter() throws IOException {
        String csvContent = "col1|col2\nvalue1|value2\nvalue3|value4";
        CSVFormat csvFormat = new CSVFormatBuilder() //
                .setDelimiter('|') //
                .build();
        CSVParser parser = parse(new StringReader(csvContent), csvFormat);
        int i = 0;
        for (CSVRecord record : parser) {
            assertEquals(2, record.size());
            assertEquals("col1", record.getHeaderName(0));
            assertEquals("col2", record.getHeaderName(1));
            assertEquals(String.valueOf(i + 1), record.get(0).toString());
            assertEquals(String.valueOf(i + 2), record.get(1).toString());
            i++;
        }
        parser.close();
    }

    @org.junit.Test
    public void testIterationWithCustomFormat() throws IOException {
        String csvContent = "name,age\nAlice,30\nBob,25";
        CSVFormat csvFormat = new CSVFormatBuilder() //
                .setHeaderNames(Arrays.asList("name", "age")) //
                .build();
        CSVParser parser = parse(new StringReader(csvContent), csvFormat);
        int i = 0;
        for (CSVRecord record : parser) {
            assertEquals(2, record.size());
            assertEquals("name", record.getHeaderName(0));
            assertEquals("age", record.getHeaderName(1));
            assertEquals(String.valueOf(i + 1), record.get(0).toString());
            assertEquals(Integer.valueOf(i + 30), record.get(1));
            i++;
        }
        parser.close();
    }

    @org.junit.Test
    public void testIterationWithEmptyCsv() throws IOException {
        String csvContent = "";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        for (CSVRecord record : parser) {
            // should never be reached
        }
        parser.close();
    }

    @org.junit.Test
    public void testIterationWithClosedParser() throws IOException {
        String csvContent = "header1,header2\n1,2\n3,4";
        CSVParser parser = parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        for (CSVRecord record : records) {
            // should never be reached
        }
    }
}