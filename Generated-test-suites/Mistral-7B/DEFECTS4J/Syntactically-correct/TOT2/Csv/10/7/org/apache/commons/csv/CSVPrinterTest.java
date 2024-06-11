package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.apache.commons.csv.Quote.MINIMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream output;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, CSVFormat.NO_QUOTE_CHARACTER, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END);
        printer = new CSVPrinter(new OutputStreamWriter(output), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintBasic() throws IOException {
        printer.print("value1");
        printer.println();
        printer.print("value2");
        printer.print("value3");
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "value1" + LF + "value2,value3" + LF;
        assertEquals(expected, result);
    }

    @Test
    public void testQuoting() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, MINIMAL, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END);
        printer = new CSVPrinter(new OutputStreamWriter(output), format);

        printer.print("value, with comma");
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "\"value, with comma\"";
        assertEquals(expected, result);
    }

    @Test
    public void testEscaping() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, CSVFormat.DEFAULT, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END);
        printer = new CSVPrinter(new OutputStreamWriter(output), format);

        printer.print("value with \n line break");
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "value with \"\\n\" line break";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, CSVFormat.DEFAULT, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END);
        format.setComment("-- Comment --");
        printer = new CSVPrinter(new OutputStreamWriter(output), format);

        printer.printRecord(new String[]{"value1", "-- Comment --"});
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "value1,-- Comment --\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"value1", "value2"});
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "value1,value2\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(new String[][]{{"value1", "value2"}, {"value3", "value4"}});
        printer.flush();

        String result = output.toString();
        String expected = "value1,value2\nvalue3,value4\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        Iterable<String[]> records = new Iterable<String[]>() {
            @Override
            public Iterator<String[]> iterator() {
                return new Iterator<String[]>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }

                    @Override
                    public String[] next() {
                        return new String[]{"value" + (index + 1), "value" + (index + 2)};
                    }
                };
            }
        };

        printer.printRecords(records);
        printer.flush();

        String result = output.toString();
        String expected = "value1,value2\nvalue2,value3\n";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "";
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        printer = new CSVPrinter(new OutputStreamWriter(output), null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullOut() throws IOException {
        printer = new CSVPrinter(null, new CSVFormat(CSVFormat.DEFAULT, CSVFormat.DEFAULT, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END));
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        printer.print("value");
        assertThrows(IOException.class, () -> printer.println());
    }

    @Test
    public void testCommentingEnabled() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, CSVFormat.DEFAULT, CSVFormat.DEFAULT_RECORD_SEPARATOR,
                CSVFormat.DEFAULT_LINE_END);
        format.setComment("-- Comment --");
        printer = new CSVPrinter(new OutputStreamWriter(output), format);

        printer.printComment("Test Comment");
        printer.println();
        printer.printRecord(new String[]{"value1", "value2"});
        printer.println();
        printer.flush();

        String result = output.toString();
        String expected = "-- Comment --\nvalue1,value2\n";
        assertEquals(expected, result);
    }

}