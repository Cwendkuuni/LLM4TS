import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JUnit 4 test cases for the CSVPrinter class.
 */
public class CSVPrinterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final StringWriter out = new StringWriter();
        final CSVFormat format = new CSVFormatBuilder().setHeader("Header1", "Header2")
                .build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            Assert.assertNotNull(printer);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test empty CSV record.
     */
    @Test
    public void testEmptyRecord() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test single value CSV record.
     */
    @Test
    public void testSingleValueRecord() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("Value");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test multiple value CSV record.
     */
    @Test
    public void testMultipleValueRecord() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("Value1");
            printer.print("Value2");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test single value CSV record with quoting.
     */
    @Test
    public void testSingleValueRecordWithQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setQuote('"').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("\"Value\"");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("\"Value\"\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test multiple value CSV record with quoting.
     */
    @Test
    public void testMultipleValueRecordWithQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setQuote('"').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("\"Value1\"");
            printer.print("\"Value2\"");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("\"Value1\",\"Value2\"\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test single value CSV record with escaping.
     */
    @Test
    public void testSingleValueRecordWithEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setEscape('\\').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("Value");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test multiple value CSV record with escaping.
     */
    @Test
    public void testMultipleValueRecordWithEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setEscape('\\').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("Value1");
            printer.print("Value2");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test single value CSV record with escaping and quoting.
     */
    @Test
    public void testSingleValueRecordWithEscapingAndQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setQuote('"').setEscape('\\').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("\"Value\\\"");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("\"Value\\\"\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test multiple value CSV record with escaping and quoting.
     */
    @Test
    public void testMultipleValueRecordWithEscapingAndQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setQuote('"').setEscape('\\').build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.print("\"Value1\\\"");
            printer.print("\"Value2\\\"");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("\"Value1\\\",\"Value2\\\"\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test comment.
     */
    @Test
    public void testComment() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setComment("//").build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.printComment("This is a comment");
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("// This is a comment\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print record.
     */
    @Test
    public void testPrintRecord() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.printRecord(new Object[]{"Value1", "Value2"});
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print records.
     */
    @Test
    public void testPrintRecords() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.printRecords(Arrays.asList(new Object[]{"Value1", "Value2"}, new Object[]{"Value3", "Value4"}));
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\nValue3,Value4\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print record with null values.
     */
    @Test
    public void testPrintRecordWithNullValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.printRecord(new Object[]{"Value1", null, "Value3"});
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1, , Value3\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print records with null values.
     */
    @Test
    public void testPrintRecordsWithNullValues() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.printRecords(Arrays.asList(new Object[]{null, "Value2"}, new Object[]{"Value1", null}));
            printer.close();
            final String result = out.toString();
            Assert.assertEquals(" , Value2\nValue1, \n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print records with iterable values.
     */
    @Test
    public void testPrintRecordsWithIterableValues() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            final Object[] array = new Object[]{new Object[]{"Value1", "Value2"}, new Object[]{"Value3", "Value4"}};
            printer.printRecords(Arrays.asList(array));
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\nValue3,Value4\n", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test print records with result set values.
     */
    @Test
    public void testPrintRecordsWithResultSetValues() throws SQLException, IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            final ResultSet resultSet = Mockito.mock(ResultSet.class);
            Mockito.when(resultSet.next()).thenReturn(true);
            Mockito.when(resultSet.getString(1)).thenReturn("Value1");
            Mockito.when(resultSet.getString(2)).thenReturn("Value2");
            Mockito.when(resultSet.getString(3)).thenReturn("Value3");
            printer.printRecords(resultSet);
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("Value1,Value2\nValue1,Value2\nValue1,Value3\n", result);
        } catch (SQLException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test flush.
     */
    @Test
    public void testFlush() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.println();
            printer.flush();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test close.
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        try {
            final CSVPrinter printer = new CSVPrinter(out, format);
            printer.println();
            printer.close();
            final String result = out.toString();
            Assert.assertEquals("", result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}