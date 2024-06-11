package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;

/**
 * Tests for {@link CSVParser} class
 */
public class CSVParserTest {

    /**
     * Test parsing records with a basic format.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseBasic() throws IOException {
        final String csvData = "Name,Age\nJohn Doe,25\nJane Doe,30";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        CSVRecordIterator iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        assertEquals(2, records.size());
        assertEquals("Name", records.get(0).getHeaderMap().get("Name"));
        assertEquals("Age", records.get(0).getHeaderMap().get("Age"));
        assertEquals("John Doe", records.get(0).get("Name"));
        assertEquals(25, records.get(0).get("Age").orElse(null));
        assertEquals("Jane Doe", records.get(1).get("Name"));
        assertEquals(30, records.get(1).get("Age").orElse(null));
    }

    /**
     * Test parsing records with a custom format.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseCustom() throws IOException {
        final String csvData = "Name,Age\nJohn Doe,25\nJane Doe,30";

        CSVFormat customFormat = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age1").withNullString(""));

        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);

        CSVRecordIterator iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        assertEquals(2, records.size());
        assertEquals("Name", records.get(0).getHeaderMap().get("Name"));
        assertEquals("Age1", records.get(0).getHeaderMap().get("Age1"));
        assertEquals("John Doe", records.get(0).get("Name"));
        assertNull(records.get(0).get("Age"));
        assertEquals(25, records.get(0).get("Age1").orElse(null));
        assertEquals("Jane Doe", records.get(1).get("Name"));
        assertNull(records.get(1).get("Age1"));
    }

    /**
     * Test parsing records with a custom format and header.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseCustomWithHeader() throws IOException {
        final String csvData = "Name,Age\nJohn Doe,25\nJane Doe,30";

        CSVFormat customFormat = new CSVFormat(CSVFormat.DEFAULT.withHeader("Name", "Age1").withNullString(""));

        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);

        CSVRecord record = parser.nextRecord();
        assertEquals(2, record.size());
        assertEquals("Name", record.getHeaderMap().get("Name"));
        assertEquals("Age1", record.getHeaderMap().get("Age1"));
        assertEquals("John Doe", record.get("Name"));
        assertNull(record.get("Age"));
        assertEquals(25, record.get("Age1").orElse(null));

        record = parser.nextRecord();
        assertEquals(2, record.size());
        assertEquals("Name", record.getHeaderMap().get("Name"));
        assertEquals("Age1", record.getHeaderMap().get("Age1"));
        assertEquals("Jane Doe", record.get("Name"));
        assertNull(record.get("Age1"));
    }

    /**
     * Test parsing records with an empty file.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseEmpty() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing records with an invalid format.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        new CSVParser(new StringReader(""), new CSVFormat("InvalidFormat"));
    }

    /**
     * Test parsing records with a malformed file.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseMalformed() throws IOException {
        new CSVParser(new StringReader("Name,Age\nJohn,Do,25"), CSVFormat.DEFAULT);
    }

    /**
     * Test parsing records with a null format.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testParseNullFormat() throws IOException {
        new CSVParser(new StringReader("Name,Age\nJohn Doe,25"), null);
    }

    /**
     * Test parsing records with a null reader.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testParseNullReader() throws IOException {
        new CSVParser(null, CSVFormat.DEFAULT);
    }

    /**
     * Test parsing records with a file that does not exist.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test(expected = FileNotFoundException.class)
    public void testParseNonExistentFile() throws IOException {
        new CSVParser(new FileReader("/nonexistentfile.csv"), CSVFormat.DEFAULT);
    }

    /**
     * Test parsing records with a file that has no records.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseNoRecordsFile() throws IOException {
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/csvparser/norecords.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing records with a file that has only comments.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseOnlyCommentsFile() throws IOException {
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/csvparser/onlycomments.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing records with a file that has only headers.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseOnlyHeadersFile() throws IOException {
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/csvparser/onlyheaders.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        List<String> headerNames = Arrays.asList("Name", "Age");
        assertEquals(Collections.unmodifiableList(headerNames), parser.getHeaderMap().values());
        assertTrue(records.isEmpty());
    }

    /**
     * Test parsing records with a file that has records and comments.
     *
     * @throws IOException
     *             On I/O error
     */
    @org.junit.Test
    public void testParseRecordsAndCommentsFile() throws IOException {
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/csvparser/recordsandcomments.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertEquals("Name1", record1.getHeaderMap().get("Name1"));
        assertEquals("Record1", record1.getHeaderMap().get("Comment1"));
        assertEquals(5, record1.size());
        assertEquals("Value1", record1.get(0));
        assertEquals("Value2", record1.get(1));
        assertEquals("Value3", record1.get(2));
        assertEquals("Value4", record1.get(3));
        assertEquals("Value5", record1.get(4));
        assertNull(record1.get("Name2"));
        assertNull(record1.get("Comment2"));
        assertFalse(record1.isLastRecord());
        assertEquals("Name2", record2.getHeaderMap().get("Name2"));
        assertEquals("Comment2", record2.getHeaderMap().get("Comment2"));
        assertEquals(1, record2.size());
        assertEquals("Value6", record2.get(0));
        assertNull(record2.get("Name1"));
        assertNull(record2.get("Comment1"));
        assertTrue(record2.isLastRecord());
    }
}