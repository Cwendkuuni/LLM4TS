package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String simpleCsv = "a,b\nc,d";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(simpleCsv.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", records.get(0).get(2));
        assertEquals("d", records.get(0).get(3));
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        final String csvWithHeader = "name,age\nAlice,25\nBob,33\nCharlie,42";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithHeader.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertSame("name", firstRecord.getHeaderMap().get("name"));
        assertSame("age", firstRecord.getHeaderMap().get("age"));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(Integer.class, firstRecord.get(1).getClass());
        assertEquals(25, firstRecord.get(1).intValue());
        assertEquals("Bob", records.get(1).get(0));
        assertEquals(33, records.get(1).get(1).intValue());
        assertEquals("Charlie", records.get(2).get(0));
        assertEquals(42, records.get(2).get(1).intValue());
    }

    /**
     * Test parsing a CSV file with an empty line.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithEmptyLine() throws IOException {
        final String csvWithEmptyLine = "a,b\nc,d\n";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithEmptyLine.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals("a", firstRecord.get(0));
        assertEquals("b", firstRecord.get(1));
        final CSVRecord secondRecord = records.get(1);
        assertNull(secondRecord);
    }

    /**
     * Test parsing a CSV file with an invalid record.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCSVWithInvalidRecord() throws IOException {
        final String csvWithInvalidRecord = "a,b\nc,d\nan";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        new CSVParser(new ByteArrayInputStream(csvWithInvalidRecord.getBytes(StandardCharsets.UTF_8)), format).getRecords();
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        final String csvWithComment = "a,b\n#This is a comment\nc,d";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithComment.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(1, records.size());
        final CSVRecord record = records.get(0);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertEquals("d", record.get(3));
        assertEquals("#This is a comment", record.getComment());
    }

    /**
     * Test parsing a CSV file with custom null string.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithCustomNullString() throws IOException {
        final String csvWithCustomNullString = "name,age\nAlice,,Bob";
        final String nullString = "NULL";
        final CSVFormat format = new CSVFormat(',', '\n', nullString, null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithCustomNullString.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertSame("name", firstRecord.getHeaderMap().get("name"));
        assertSame("age", firstRecord.getHeaderMap().get("age"));
        assertEquals("Alice", firstRecord.get(0));
        assertNull(firstRecord.get(1));
        assertEquals("Bob", records.get(1).get(0));
        assertNull(records.get(1).get(1));
    }

    /**
     * Test parsing a CSV file with escaped commas.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVEscapedCommas() throws IOException {
        final String csvWithEscapedCommas = "name,age\nAlice,b\"a,b\",Bob";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithEscapedCommas.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertSame("name", firstRecord.getHeaderMap().get("name"));
        assertSame("age", firstRecord.getHeaderMap().get("age"));
        assertEquals("Alice", firstRecord.get(0));
        assertEquals("b\"a,b\"", firstRecord.get(1));
        assertEquals("Bob", records.get(1).get(0));
        assertNull(records.get(1).get(1));
    }

    /**
     * Test parsing a CSV file with quoted commas.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVQuotedCommas() throws IOException {
        final String csvWithQuotedCommas = "name,age\n\"Alice,b\",b\"a,b\",Bob";
        final CSVFormat format = new CSVFormat(',', '\n', "'", null);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithQuotedCommas.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertSame("name", firstRecord.getHeaderMap().get("name"));
        assertSame("age", firstRecord.getHeaderMap().get("age"));
        assertEquals("Alice,b", firstRecord.get(0));
        assertEquals("b\"a,b\"", firstRecord.get(1));
        assertEquals("Bob", records.get(1).get(0));
        assertNull(records.get(1).get(1));
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException on parse error or input read-failure
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final String csvWithCustomFormat = "name:age\nAlice:25\nBob:33\nCharlie:42";
        final Map<String, Integer> headerMap = new HashMap<String, Integer>() {
            {
                put("name", 0);
                put("age", 1);
            }
        };
        final CSVFormat format = new CSVFormat(':', '\n', "'", null).setHeaderMap(headerMap);
        final CSVParser parser = new CSVParser(new ByteArrayInputStream(csvWithCustomFormat.getBytes(StandardCharsets.UTF_8)), format);
        final List<CSVRecord> records = parser.getRecords();
        parser.close();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals("Alice", firstRecord.get(0));
        assertEquals(Integer.class, firstRecord.get(1).getClass());
        assertEquals(25, firstRecord.get(1).intValue());
        assertEquals("Bob", records.get(1).get(0));
        assertEquals(33, records.get(1).get(1).intValue());
        assertEquals("Charlie", records.get(2).get(0));
        assertEquals(42, records.get(2).get(1).intValue());
    }
}