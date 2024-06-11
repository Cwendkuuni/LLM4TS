package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    private static final String COMMA_DELIMITED = ",";

    private static final String CR_LF_DELIMITED = "\r\n";

    private static final String EMPTY_LINE = "\r";

    private static final String EOL_DELIMITED = "\n";

    private static final String ESCAPED_COMMA = "\",";

    private static final String ESCAPED_QUOTE = "\\\"";

    private static final String QUOTED_COMMA = ",\"";

    private static final String QUOTED_ESCAPED_COMMA = ",\"\",";

    private static final String QUOTED_STRING = "\"Hello World\"";

    private static final String TAB_DELIMITED = "\t";

    private CSVParser parser;

    private final CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder() //
                .setDelimiter(COMMA_DELIMITED) //
                .setHeaderNames(Arrays.asList("Name", "Age", "City")) //
                .setIgnoreLeadingWhiteSpace(true) //
                .setIgnoreTrailingWhiteSpace(true) //
                .setTrimLeadingWhiteSpace(true) //
                .setTrimTrailingWhiteSpace(true) //
                .build();
    }

    @Test
    public void testParseCommaDelimited() throws IOException {
        final String testData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record0 = records.get(0);
        assertEquals("Name", record0.getColumnName(0));
        assertEquals("John Doe", record0.get(0));
        assertEquals("Age", record0.getColumnName(1));
        assertEquals(Integer.valueOf(35), record0.get(1));
        assertEquals("City", record0.getColumnName(2));
        assertEquals("New York", record0.get(2));

        CSVRecord record1 = records.get(1);
        assertEquals("Name", record1.getColumnName(0));
        assertEquals("Jane Doe", record1.get(0));
        assertEquals("Age", record1.getColumnName(1));
        assertEquals(Integer.valueOf(28), record1.get(1));
        assertEquals("City", record1.getColumnName(2));
        assertEquals("Chicago", record1.get(2));

        parser.close();
    }

    @Test
    public void testParseCrLfDelimited() throws IOException {
        final String testData = "Name\rAge\rCity\r\nJohn Doe\r35\rNew York\r\nJane Doe\r28\rChicago\r";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record0 = records.get(0);
        assertEquals("Name", record0.getColumnName(0));
        assertEquals("John Doe", record0.get(0));
        assertEquals("Age", record0.getColumnName(1));
        assertEquals(Integer.valueOf(35), record0.get(1));
        assertEquals("City", record0.getColumnName(2));
        assertEquals("New York", record0.get(2));

        CSVRecord record1 = records.get(1);
        assertEquals("Name", record1.getColumnName(0));
        assertEquals("Jane Doe", record1.get(0));
        assertEquals("Age", record1.getColumnName(1));
        assertEquals(Integer.valueOf(28), record1.get(1));
        assertEquals("City", record1.getColumnName(2));
        assertEquals("Chicago", record1.get(2));

        parser.close();
    }

    @Test
    public void testParseEmptyLine() throws IOException {
        final String testData = EMPTY_LINE;
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());

        CSVRecord record = records.get(0);
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(2));

        parser.close();
    }

    @Test
    public void testParseEolDelimited() throws IOException {
        final String testData = "Name\nAge\nCity\nJohn Doe\n35\nNew York\nJane Doe\n28\nChicago\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record0 = records.get(0);
        assertEquals("Name", record0.getColumnName(0));
        assertEquals("John Doe", record0.get(0));
        assertEquals("Age", record0.getColumnName(1));
        assertEquals(Integer.valueOf(35), record0.get(1));
        assertEquals("City", record0.getColumnName(2));
        assertEquals("New York", record0.get(2));

        CSVRecord record1 = records.get(1);
        assertEquals("Name", record1.getColumnName(0));
        assertEquals("Jane Doe", record1.get(0));
        assertEquals("Age", record1.getColumnName(1));
        assertEquals(Integer.valueOf(28), record1.get(1));
        assertEquals("City", record1.getColumnName(2));
        assertEquals("Chicago", record1.get(2));

        parser.close();
    }

    @Test
    public void testParseEscapedComma() throws IOException {
        final String testData = "Name,\"Age,City\",City";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals("Age,City", record.get(0));
        assertEquals("City", record.getColumnName(1));
        assertEquals(Collections.singletonList("City"), record.getColumn(1));

        parser.close();
    }

    @Test
    public void testParseEscapedQuote() throws IOException {
        final String testData = "Name,\"\"Hello World\"\"";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals("Hello World", record.get(0));

        parser.close();
    }

    @Test
    public void testParseQuotedComma() throws IOException {
        final String testData = "Name,\"Name,Age,City\",City";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals(Arrays.asList("Name,Age,City"), record.get(0));
        assertEquals("City", record.getColumnName(1));
        assertEquals(Collections.singletonList("City"), record.getColumn(1));

        parser.close();
    }

    @Test
    public void testParseQuotedEscapedComma() throws IOException {
        final String testData = "Name,\",\"Age,City,\"\",City\"";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals(Arrays.asList("", "Age,City", "", "City"), record.get(0));
        assertEquals("City", record.getColumnName(3));
        assertEquals(Collections.singletonList("City"), record.getColumn(3));

        parser.close();
    }

    @Test
    public void testParseQuotedString() throws IOException {
        final String testData = "Name,\"Hello World\"";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        assertEquals("Name", record.getColumnName(0));
        assertEquals("Hello World", record.get(0));

        parser.close();
    }

    @Test
    public void testParseTabDelimited() throws IOException {
        final String testData = "Name\tAge\tCity\nJohn Doe\t35\tNew York\nJane Doe\t28\tChicago";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, new CSVFormatBuilder().setDelimiter(TAB_DELIMITED).build());

        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record0 = records.get(0);
        assertEquals("Name", record0.getColumnName(0));
        assertEquals("John Doe", record0.get(0));
        assertEquals("Age", record0.getColumnName(1));
        assertEquals(Integer.valueOf(35), record0.get(1));
        assertEquals("City", record0.getColumnName(2));
        assertEquals("New York", record0.get(2));

        CSVRecord record1 = records.get(1);
        assertEquals("Name", record1.getColumnName(0));
        assertEquals("Jane Doe", record1.get(0));
        assertEquals("Age", record1.getColumnName(1));
        assertEquals(Integer.valueOf(28), record1.get(1));
        assertEquals("City", record1.getColumnName(2));
        assertEquals("Chicago", record1.get(2));

        parser.close();
    }

    @Test
    public void testToString() throws IOException {
        final String testData = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        parser = CSVParser.parse(inputStream, format);

        List<CSVRecord> records = parser.getRecords();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        parser.writeRecords(outputStream);

        String output = outputStream.toString();

        String expected = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,28,Chicago";

        assertEquals(expected, output);

        parser.close();
    }
}