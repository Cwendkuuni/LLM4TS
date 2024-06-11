package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        this.parser = CSVParser.parse(new StringReader("name,age\nAlice,25\nbob,30\ncatherine,35"), format);
    }

    @After
    public void tearDown() throws IOException {
        if (this.parser != null) {
            this.parser.close();
        }
    }

    /**
     * Test parsing a simple CSV file with a header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseWithHeader() throws IOException {
        assertNotNull(this.parser);

        List<CSVRecord> records = this.parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals("age", record.getHeaderMap().get("age"));
        assertEquals(Integer.class, record.get("age").getClass());
        assertEquals(25, record.get("age").intValue());

        record = records.get(1);
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("bob", record.get("name"));
        assertEquals("age", record.getHeaderMap().get("age"));
        assertEquals(Integer.class, record.get("age").getClass());
        assertEquals(30, record.get("age").intValue());

        String comment = this.parser.getHeaderMap().get("name").toString();
        assertNotNull(comment);
        assertEquals("", comment); // default comment is empty

        this.parser.close();
    }

    /**
     * Test parsing a simple CSV file without a header.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseWithoutHeader() throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new StringReader("Alice,25\nbob,30\ncatherine,35"), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("", record.getHeaderMap().keys().next().toString());
        assertEquals("Alice", record.get(0));
        assertEquals(Integer.class, record.get(1).getClass());
        assertEquals(25, record.get(1).intValue());

        this.parser.close();
        parser.close();
    }

    /**
     * Test parsing a CSV file with an invalid CRLF sequence.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test(expected = IOException.class)
    public void testParseWithInvalidCRLF() throws IOException {
        final String input = "name,age\nAlice,25\r\n\rbob,30\ncatherine,35\n";
        CSVParser parser = new CSVParser(new StringReader(input), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException on parse error or input read-failure
     */
    @Test
    public void testParseWithComment() throws IOException {
        final String input = "name,age\nAlice,25\n---\n";
        CSVParser parser = new CSVParser(new StringReader(input), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("name", record.getHeaderMap().get("name"));
        assertEquals("Alice", record.get("name"));
        assertEquals("age", record.getHeaderMap().get("age"));
        assertEquals(Integer.class, record.get("age").getClass());
        assertEquals(25, record.get("age").intValue());
        String comment = record.getComment();
        assertNotNull(comment);
        assertEquals("---\n", comment);

        this.parser.close();
        parser.close();
    }
}