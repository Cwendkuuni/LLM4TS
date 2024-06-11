package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.apache.commons.csv.test.CSVPrinterTestBase;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest extends CSVPrinterTestBase {

    private CSVPrinter printer;
    private ByteArrayOutputStream out;
    private StringWriter writer;
    private CSVFormat format;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        writer = new StringWriter(out);
        format = CSVFormat.DEFAULT.withQuoteAll(true);
        printer = new CSVPrinter(writer, format);
    }

    @Test
    public void testPrintObject() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals(CSVPrinter.Constants.EMPTY, out.toString());

        printer.print(Boolean.TRUE);
        printer.flush();
        assertEquals(CSVPrinter.Constants.TRUE, out.toString());

        printer.print(Integer.valueOf(42));
        printer.flush();
        assertEquals("42", out.toString());

        printer.print("test");
        printer.flush();
        assertEquals("test", out.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        String value = "\"test\"";
        printer.print(value);
        printer.flush();
        assertEquals("\"test\"", out.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        String value = "test,\"test\"";
        printer.print(value);
        printer.flush();
        assertEquals("test,\"test\"", out.toString());

        value = "test\ntest";
        printer.print(value);
        printer.flush();
        assertEquals("test\\ntest", out.toString());

        value = "test\\";
        printer.print(value);
        printer.flush();
        assertEquals("test\\", out.toString());

        value = "test,\"test\\\"";
        printer.print(value);
        printer.flush();
        assertEquals("test,\"test\\\"", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# test comment");
        printer.flush();
        assertEquals("# test comment" + CR, out.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        assertEquals(CSVPrinter.LF, out.toString());
    }

    @Test
    public void testPrintDelimitedRecord() throws IOException {
        printer.printRecord(new String[]{"test1", "test2", "test3"});
        printer.flush();
        CSVRecord record = CsvParser.parse(new StringReader(out.toString()), format);
        assertEquals(3, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        printer.printRecords(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("test1", "test2", "test3").iterator();
            }
        });
        printer.flush();
        CSVRecord record = CsvParser.parse(new StringReader(out.toString()), format);
        assertEquals(3, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));
    }

    @Test
    public void testPrintRecordWithObjectArray() throws IOException {
        printer.printRecords(new Object[][]{new String[]{"test1", "test2", "test3"}});
        printer.flush();
        CSVRecord record = CsvParser.parse(new StringReader(out.toString()), format);
        assertEquals(3, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        StringWriter writer2 = new StringWriter(out2);
        CSVPrinter printer2 = new CSVPrinter(writer2, format);
        ResultSet rs = prepareTestResultSet();
        printer2.printRecords(rs);
        printer2.flush();
        String expected = "test1,test2,test3" + CR + "test4,test5,test6" + CR;
        assertEquals(expected, out2.toString());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        StringWriter writer3 = new StringWriter(out3);
        CSVPrinter printer3 = new CSVPrinter(writer3, format);
        printer3.close();
        assertNull(printer3.getOut());
        assertFalse(printer3.isOpen());
        assertEquals(
                "", // empty string because no data was written
                out3.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrintAfterClose() throws IOException {
        ByteArrayOutputStream out4 = new ByteArrayOutputStream();
        StringWriter writer4 = new StringWriter(out4);
        CSVPrinter printer4 = new CSVPrinter(writer4, format);
        printer4.close();
        printer4.print("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCSVFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            CSVPrinter printer5 = new CSVPrinter(out, null);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            CSVPrinter printer6 = new CSVPrinter(null, format);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCSVFormat() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {
            CSVPrinter printer7 = new CSVPrinter(out, null);
        });
    }
}