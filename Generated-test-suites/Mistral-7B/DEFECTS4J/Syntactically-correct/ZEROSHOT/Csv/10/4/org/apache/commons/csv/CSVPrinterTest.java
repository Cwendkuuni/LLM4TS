package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR_LF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.DEFAULT_QUOTE_CHAR;
import static org.apache.commons.csv.CSVFormat.DEFAULT_QUOTE_POLICY;
import static org.apache.commons.csv.CSVFormat.DEFAULT_RECORD_SEPARATOR;
import static org.apache.commons.csv.CSVFormat.EXPLICIT;
import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.CSVPrinter.println;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the functionality of the CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream out;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(out, DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        if (printer != null) {
            printer.close();
        }
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        final String result = out.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintEmpty() throws IOException {
        printer.print("");
        printer.flush();
        final String result = out.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintSimpleString() throws IOException {
        printer.print("test");
        printer.flush();
        final String result = out.toString();
        assertEquals("test", result);
    }

    @Test
    public void testPrintEscapedString() throws IOException {
        printer.format = new CSVFormat(EXPLICIT, ' ', ',', '"', DEFAULT_QUOTE_CHAR, null, null, null,
                DEFAULT_RECORD_SEPARATOR, null, null, Quote.MINIMAL);
        printer.print("test,test,test\"test\"test");
        printer.flush();
        final String result = out.toString();
        assertEquals("test,\"test\"test,test\"test\"test", result);
    }

    @Test
    public void testPrintStringWithCR() throws IOException {
        printer.print("test\r");
        printer.flush();
        final String result = out.toString();
        assertEquals("test\r", result);
    }

    @Test
    public void testPrintStringWithLF() throws IOException {
        printer.print("test\n");
        printer.flush();
        final String result = out.toString();
        assertEquals("test\n", result);
    }

    @Test
    public void testPrintStringWithLFAndCR() throws IOException {
        printer.print("test\r\n");
        printer.flush();
        final String result = out.toString();
        assertEquals("test" + CR_LF, result);
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("test1,test2,test3");
        printer.flush();
        final String result = out.toString();
        assertEquals("test1,test2,test3", result);
    }

    @Test
    public void testPrintMultipleEscapedValues() throws IOException {
        printer.format = new CSVFormat(EXPLICIT, ' ', ',', '"', DEFAULT_QUOTE_CHAR, null, null, null,
                DEFAULT_RECORD_SEPARATOR, null, null, Quote.MINIMAL);
        printer.print("test1,\"test2\"test3,test4\"test5\"test6");
        printer.flush();
        final String result = out.toString();
        assertEquals("test1,\"test2\"test3,test4\"test5\"test6", result);
    }

    @Test
    public void testPrintMultipleValuesWithLF() throws IOException {
        printer.println("test1");
        printer.println("test2");
        printer.print("test3");
        printer.flush();
        final String result = out.toString();
        assertEquals(CR + "test1" + LF + CR + "test2" + LF + "test3", result);
    }

    @Test
    public void testPrintMultipleValuesWithCR() throws IOException {
        printer.println("test1");
        printer.println("test2");
        printer.print("\r");
        printer.print("test3");
        printer.flush();
        final String result = out.toString();
        assertEquals(CR + "test1" + CR + "test2" + CR + "test3", result);
    }

    @Test
    public void testPrintMultipleValuesWithLFAndCR() throws IOException {
        printer.println("test1");
        printer.println("test2");
        printer.print("\r\n");
        printer.print("test3");
        printer.flush();
        final String result = out.toString();
        assertEquals(CR + "test1" + CR_LF + "test2" + CR_LF + "test3", result);
    }

    @Test
    public void testPrintMultipleEscapedValuesWithLF() throws IOException {
        printer.format = new CSVFormat(RFC4180, ',', '"', '"', null, null, null, null, null, null, null,
                Quote.MINIMAL);
        printer.println("test1");
        printer.println("test2,\"test3\"test4,test5\"test6\"test7");
        printer.flush();
        final String result = out.toString();
        assertEquals(LF + "test1" + LF + "test2,\"test3\"test4,test5\"test6\"test7" + LF, result);
    }

    @Test
    public void testPrintMultipleEscapedValuesWithCR() throws IOException {
        printer.format = new CSVFormat(RFC4180, ',', '"', '"', null, null, null, null, null, null, null,
                Quote.MINIMAL);
        printer.println("test1");
        printer.print("test2,\"test3\"test4,test5\"test6\"test7" + CR);
        printer.flush();
        final String result = out.toString();
        assertEquals(LF + "test1" + CR + "test2,\"test3\"test4,test5\"test6\"test7" + CR, result);
    }

    @Test
    public void testPrintMultipleEscapedValuesWithLFAndCR() throws IOException {
        printer.format = new CSVFormat(RFC4180, ',', '"', '"', null, null, null, null, null, null, null,
                Quote.MINIMAL);
        printer.println("test1");
        printer.println("test2," + CR_LF + "\"test3\"test4,test5\"test6\"test7");
        printer.flush();
        final String result = out.toString();
        assertEquals(LF + "test1" + LF + "test2," + CR_LF + "\"test3\"test4,test5\"test6\"test7" + LF, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("test comment");
        printer.flush();
        final String result = out.toString();
        assertEquals(CR + COMMENT + " test comment" + LF, result);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer = new CSVPrinter(new StringWriter(), DEFAULT);
        final String[] values1 = { "test1", "test2", "test3" };
        printer.printRecords(values1);
        printer.flush();
        final CSVParser parser = CSVParser.parse(printer.toString(), DEFAULT);
        final CSVRecord record = parser.getRecord();
        assertEquals(3, record.size());
        assertEquals("test1", record.get(0));
        assertEquals("test2", record.get(1));
        assertEquals("test3", record.get(2));

        printer.reset();
        final String[] values2 = { "test1", null, "test3" };
        printer.printRecords(values2);
        printer.flush();
        final CSVParser parser2 = CSVParser.parse(printer.toString(), DEFAULT);
        final CSVRecord record2 = parser2.getRecord();
        assertEquals(3, record2.size());
        assertEquals("test1", record2.get(0));
        assertEquals(null, record2.get(1));
        assertEquals("test3", record2.get(2));

        printer.reset();
        final Iterable<String> values3 = Arrays.asList(new String[] { "test1", "test2", "test3" });
        printer.printRecords(values3);
        printer.flush();
        final CSVParser parser3 = CSVParser.parse(printer.toString(), DEFAULT);
        final CSVRecord record3 = parser3.getRecord();
        assertEquals(3, record3.size());
        assertEquals("test1", record3.get(0));
        assertEquals("test2", record3.get(1));
        assertEquals("test3", record3.get(2));

        printer.reset();
        final ResultSet resultSet = getResultSet();
        printer.printRecords(resultSet);
        printer.flush();
        final CSVParser parser4 = CSVParser.parse(printer.toString(), DEFAULT);
        int i = 0;
        while (parser4.hasNext()) {
            final CSVRecord record4 = parser4.next();
            assertEquals("test" + i, record4.get(0));
            i++;
        }
    }

    @Test
    public void testPrintRecordsWithQuoting() throws IOException, SQLException {
        printer = new CSVPrinter(new StringWriter(), new CSVFormat(EXPLICIT, ' ', ',', '"', '"', null, null,
                null, null, null, Quote.MINIMAL));
        final String[] values1 = { "test1,test2,test3", "test1,\"test2\"test3,test4\"test5\"test6" };
        printer.printRecords(values1);
        printer.flush();
        final CSVParser parser = CSVParser.parse(printer.toString(), new CSVFormat(EXPLICIT, ' ', ',', '"', '"',
                null, null, null, null, null, null, Quote.MINIMAL));
        final CSVRecord record = parser.getRecord();
        assertEquals(2, record.size());
        assertEquals("test1,test2,test3", record.get(0).toString());
        assertEquals("test1,\"test2\"test3,test4\"test5\"test6", record.get(1).toString());
    }

    @Test
    public void testPrintRecordsWithInvalidQuoting() throws IOException, SQLException {
        printer = new CSVPrinter(new StringWriter(), new CSVFormat(EXPLICIT, ' ', ',', '"', '"', null, null,
                null, null, null, Quote.MINIMAL));
        final String[] values1 = { "test1,test2,test3", "test1,\"test2\"test3,test4\"test5\"test6",
                "test1,\"test2\"test3,\"test4\"test5\"test6" };
        printer.printRecords(values1);
        printer.flush();
        try {
            final CSVParser parser = CSVParser.parse(printer.toString(), new CSVFormat(EXPLICIT, ' ', ',',
                    '"', '"', null, null, null, null, null, null, Quote.MINIMAL));
            parser.getRecord();
            fail();
        } catch (final CSVParseException e) {
            // expected
        }
    }

    private ResultSet getResultSet() throws SQLException {
        final String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
        final String driver = "org.h2.Driver";
        final String sql = "CREATE TABLE test (col1 varchar(10), col2 varchar(10), col3 varchar(10))";
        final String insert = "INSERT INTO test VALUES (?, ?, ?)";

        try {
            Class.forName(driver);
            final Connection conn = DriverManager.getConnection(url);
            final Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.executeUpdate(insert, new Object[] { "test1", "test2", "test3" });
            return stmt.executeQuery("SELECT * FROM test");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (final SQLException e) {
                // ignored
            }
        }
    }
}