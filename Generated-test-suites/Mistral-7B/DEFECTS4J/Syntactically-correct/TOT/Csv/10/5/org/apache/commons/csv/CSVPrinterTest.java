package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.quote;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * This class contains the test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private static final String TEST_HEADER = "TestHeader1,TestHeader2";
    private static final String TEST_RECORD = "TestValue1,TestValue2,TestValue3";

    @org.junit.Test
    public void testPrintNullValues() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecord(Arrays.asList("Value1", null, "Value3"));
            printer.flush();

            final CSVParser parser = CSVParser.parse(new StringReader(baos.toString()));
            final CSVRecord record = parser.nextRecord();
            assertEquals("Value1,null,Value3", record.toString());
        }
    }

    @org.junit.Test
    public void testPrintEmptyValues() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecord(Arrays.asList("Value1", "", "Value3"));
            printer.flush();

            final CSVParser parser = CSVParser.parse(new StringReader(baos.toString()));
            final CSVRecord record = parser.nextRecord();
            assertEquals("Value1,\"\",Value3", record.toString());
        }
    }

    @org.junit.Test
    public void testPrintEmptyLine() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.println();
            printer.flush();

            final String csv = baos.toString();
            final List<String> lines = Arrays.asList(csv.split(System.lineSeparator()));
            assertEquals(2, lines.size());
            assertEquals(String.join(DEFAULT.getRecordSeparator(), Arrays.asList(TEST_HEADER)), lines.get(0));
            assertEquals(String.join(LF, Arrays.asList("")), lines.get(1));
        }
    }

    @org.junit.Test
    public void testPrintWithQuoting() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecord(Arrays.asList("Test\"Value1\"1,Test\"Value2\"2,Test\"Value3\"3"));
            printer.flush();

            final CSVParser parser = CSVParser.parse(new StringReader(baos.toString()));
            final CSVRecord record = parser.nextRecord();
            assertEquals("Test\"Value1\"1,Test\"Value2\"2,Test\"Value3\"3", record.toString());
        }
    }

    @org.junit.Test
    public void testPrintWithEscaping() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.NONE).withEscapingMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecord(Arrays.asList("TestVal1\"Value2", "TestVal3\"Value4"));
            printer.flush();

            final CSVParser parser = CSVParser.parse(new StringReader(baos.toString()));
            final CSVRecord record = parser.nextRecord();
            assertEquals("TestVal1\"\"Value2\",TestVal3\"\"Value4", record.toString());
        }
    }

    @org.junit.Test
    public void testPrintWithComment() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL).withCommentingEnabled(true);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printComment("-- Test Comment");
            printer.println();
            printer.println();
            printer.println();
            printer.printRecord(Arrays.asList("TestHeader1", "TestHeader2"));
            printer.println();
            printer.printRecord(Arrays.asList("TestValue1", "TestValue2"));
            printer.println();
            printer.flush();

            final CSVParser parser = CSVParser.parse(new StringReader(baos.toString()));
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());

            final CSVRecord firstRecord = records.get(0);
            assertEquals(TEST_HEADER, firstRecord.toString());

            final CSVRecord secondRecord = records.get(1);
            assertEquals(TEST_RECORD, secondRecord.toString());

            final List<String> comments = parser.getComments();
            assertEquals(3, comments.size());
            assertEquals("-- Test Comment", comments.get(0));
            assertEquals(LF, comments.get(1));
            assertEquals(LF, comments.get(2));
        }
    }

    @org.junit.Test
    public void testPrintWithRecordSeparator() throws IOException {
        final CSVFormat format = DEFAULT.withRecordSeparator("\u0001");
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecord(Arrays.asList("TestValue1", "TestValue2", "TestValue3"));
            printer.println();
            printer.printRecord(Arrays.asList("TestValue4", "TestValue5", "TestValue6"));
            printer.println();
            printer.flush();

            final String csv = baos.toString();
            final byte[] bytes = csv.getBytes();
            final int indexOfRecordSeparator = Arrays.binarySearch(bytes, '\u0001');

            assertEquals(-3, indexOfRecordSeparator);
            assertEquals(TEST_RECORD + LF + TEST_RECORD, csv.substring(0, indexOfRecordSeparator));
            assertEquals(TEST_RECORD, csv.substring(indexOfRecordSeparator));
        }
    }

    @org.junit.Test
    public void testThrowsIllegalArgumentExceptionWithNullFormat() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(null, DEFAULT));
    }

    @org.junit.Test
    public void testThrowsIllegalArgumentExceptionWithNullOut() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(null, DEFAULT));
    }

    @org.junit.Test
    public void testThrowsIllegalArgumentExceptionWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(new StringWriter(), new CSVFormat()));
    }

    @org.junit.Test
    public void testThrowsIOExceptionOnClose() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, DEFAULT)) {
            printer.println();
            printer.close();
        }
        assertThrows(IOException.class, () -> baos.close());
    }

    @org.junit.Test
    public void testThrowsIOExceptionOnFlush() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, DEFAULT)) {
            printer.flush();
            printer.close();
        }
        assertThrows(IOException.class, () -> baos.flush());
    }

    @org.junit.Test
    public void testThrowsSQLException() throws IOException, SQLException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String testQuery = "SELECT * FROM test";
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, DEFAULT);
                final ResultSet resultSet = new MockResultSet(testQuery)) {

            printer.printRecords(resultSet);
            printer.flush();
        }
    }

    @org.junit.Test
    public void testPrintEmptyIterable() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecords(Collections.emptyList());
            printer.flush();

            final String csv = baos.toString();
            assertEquals(TEST_HEADER + LF, csv);
        }
    }

    @org.junit.Test
    public void testPrintEmptyArray() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            printer.printRecords(new Object[0]);
            printer.flush();

            final String csv = baos.toString();
            assertEquals(TEST_HEADER + LF, csv);
        }
    }

    @org.junit.Test
    public void testPrintSingleObjectArray() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            final Object[] array = new Object[] { Arrays.asList("TestValue1", "TestValue2") };
            printer.printRecords(array);
            printer.flush();

            final String csv = baos.toString();
            assertEquals(TEST_RECORD + LF, csv);
        }
    }

    @org.junit.Test
    public void testPrintIterableOfArrays() throws IOException {
        final CSVFormat format = DEFAULT.withHeader().withQuoteMode(quote.ALL);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final Writer out = new OutputStreamWriter(baos);
                final CSVPrinter printer = new CSVPrinter(out, format)) {
            final Object[][] arrays = new Object[][] { Arrays.asList("TestValue11", "TestValue12"),
                    Arrays.asList("TestValue21", "TestValue22") };
            printer.printRecords(Arrays.asList(arrays));
            printer.flush();

            final String csv = baos.toString();
            assertEquals(TEST_RECORD + CR + TEST_RECORD, csv);
        }
    }
}