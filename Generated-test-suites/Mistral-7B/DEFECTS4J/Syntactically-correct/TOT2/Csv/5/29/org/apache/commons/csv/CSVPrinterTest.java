package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

/**
 * Test cases for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;
    private StringWriter stringWriter;

    @BeforeEach
    public void initialize() throws IOException {
        outputStream = new ByteArrayOutputStream();
        stringWriter = new StringWriter(outputStream);
        printer = new CSVPrinter(new OutputStreamWriter(outputStream, "UTF-8"), CSVFormat.DEFAULT);
    }

    /**
     * Test for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)} constructor.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testCSVPrinterConstructor() throws IOException {
        CSVPrinter printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        Assertions.assertNotNull(printer);
    }

    /**
     * Test for {@link CSVPrinter#close()} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testClose() throws IOException {
        printer.close();
        Assertions.assertThrows(IOException.class, printer::close);
    }

    /**
     * Test for {@link CSVPrinter#flush()} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testFlush() throws IOException {
        printer.flush();
        Assertions.assertThrows(IOException.class, printer::flush);
    }

    /**
     * Test for {@link CSVPrinter#print(Object)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrint() throws IOException {
        printer.print("Test");
        Assertions.assertEquals("Test" + LF, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#print(CharSequence, int, int)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrint_CharSequence() throws IOException {
        printer.print("Test" + LF);
        Assertions.assertEquals("Test" + LF, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#print(Object, CharSequence, int, int)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrint_Object_CharSequence() throws IOException {
        printer.print("Test", "Test", 0, 4);
        Assertions.assertEquals("Test" + LF, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#printRecord(Iterable)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrintRecord_Iterable() throws IOException {
        List<String> record = Arrays.asList("Test1", "Test2", "Test3");
        printer.printRecord(record);
        String expected = "Test1,Test2,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#printRecord(Object...)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrintRecord_Object() throws IOException {
        printer.printRecord("Test1", "Test2", "Test3");
        String expected = "Test1,Test2,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#printComment(String)} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("Test comment");
        String expected = "# Test comment" + LF + LF;
        Assertions.assertEquals(expected, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#println()} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();
        String expected = LF;
        Assertions.assertEquals(expected, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#printRecords(Iterable)} method.
     * @throws IOException for any I/O error.
     * @throws SQLException for any SQL error.
     */
    @Test
    public void testPrintRecords_Iterable() throws IOException, SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString(Mockito.anyInt())).thenAnswer(invocation -> invocation.getArgument(0).toString());

        printer.printRecords(resultSet);

        String expected = "Test1,Test2,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());
    }

    /**
     * Test for {@link CSVPrinter#getOut()} method.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testGetOut() throws IOException {
        Assertions.assertInstanceOf(OutputStreamWriter.class, printer.getOut());
    }

    /**
     * Test for edge cases and exceptions.
     * @throws IOException for any I/O error.
     */
    @Test
    public void testEdgeCasesAndExceptions() throws IOException {
        // Test for null input
        printer = new CSVPrinter(null, CSVFormat.DEFAULT);
        Assertions.assertThrows(NullPointerException.class, printer::print);

        // Test for empty string as input
        printer.print("");
        Assertions.assertEquals(SP, outputStream.toString());

        // Test for empty record
        printer.printRecord(Arrays.asList());
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty string
        List<String> record = Arrays.asList("Test", "", "Test3");
        printer.printRecord(record);
        Assertions.assertEquals("Test,,,Test3" + LF, outputStream.toString());

        // Test for record with special characters
        record = Arrays.asList("Test1", "Test\"2", "Test3");
        printer.printRecord(record);
        String expected = "Test1,\"Test\"2,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        // Test for record with newline and comment characters
        record = Arrays.asList("Test1", "Test2\r\n# Test comment", "Test3");
        printer.printRecord(record);
        expected = "Test1,Test2" + CR + LF + "# Test comment" + LF + "Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        // Test for record with quoting and escaping
        record = Arrays.asList(new String[]{"Test1", "Test\"2, Test\"3", "Test4"});
        printer.printRecord(record);
        expected = "Test1,\"Test\"2,\"Test\"3,Test4" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        // Test for null record
        printer.printRecord(null);
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for empty array as input
        printer.printRecords((Object[]) null);
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for empty array with null element
        Object[] array = new Object[1];
        array[0] = null;
        printer.printRecords(array);
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with null element
        record = Arrays.asList("Test1", null, "Test3");
        printer.printRecord(record);
        expected = "Test1,,,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        // Test for multiple records with null elements
        List<List<Object>> records = Arrays.asList(
                Arrays.asList("Test1", null, "Test3"),
                Arrays.asList("Test4", "Test5", null),
                Arrays.asList("Test6", "Test7", "Test8")
        );
        printer.printRecords(records);
        expected = "Test1,,,Test3" + LF +
                "Test4,Test5,," + LF +
                "Test6,Test7,Test8" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        // Test for record with empty array
        Object[] recordArray = new Object[1];
        printer.printRecord(Arrays.asList(recordArray));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and null element
        Object[] recordArray2 = new Object[2];
        recordArray2[0] = null;
        printer.printRecord(Arrays.asList(recordArray2));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string
        Object[] recordArray3 = new Object[1];
        printer.printRecord(Arrays.asList(recordArray3, ""));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string and null element
        Object[] recordArray4 = new Object[2];
        printer.printRecord(Arrays.asList(recordArray4, "", null));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string and null element and null record
        Object[] recordArray5 = new Object[1];
        printer.printRecord(Arrays.asList(recordArray5, Arrays.asList()));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string and null element and null record and empty array
        Object[] recordArray6 = new Object[1];
        Object[] recordArray7 = new Object[1];
        printer.printRecord(Arrays.asList(recordArray6, Arrays.asList(recordArray7)));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string and null element and null record and empty array and null record
        Object[] recordArray8 = new Object[1];
        Object[] recordArray9 = new Object[1];
        printer.printRecord(Arrays.asList(recordArray6, Arrays.asList(recordArray7), Arrays.asList(recordArray8, recordArray9)));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with empty array and empty string and null element and null record and empty array and null record and empty array
        Object[] recordArray10 = new Object[1];
        Object[] recordArray11 = new Object[1];
        Object[] recordArray12 = new Object[1];
        printer.printRecord(Arrays.asList(recordArray6, Arrays.asList(recordArray7), Arrays.asList(recordArray8, recordArray9), Arrays.asList(recordArray10, recordArray11, recordArray12)));
        Assertions.assertEquals(LF, outputStream.toString());

        // Test for record with null CSVFormat
        printer = new CSVPrinter(outputStream, null);
        Assertions.assertThrows(NullPointerException.class, printer::print);

        // Test for record with invalid CSVFormat
        printer = new CSVPrinter(outputStream, new CSVFormat());
        Assertions.assertThrows(IllegalArgumentException.class, printer::print);

        // Test for record with invalid appendable
        printer = new CSVPrinter(null, CSVFormat.DEFAULT);
        Assertions.assertThrows(NullPointerException.class, printer::print);
    }

    /**
     * Test for {@link CSVPrinter#printRecords(ResultSet)} method.
     * @throws IOException for any I/O error.
     * @throws SQLException for any SQL error.
     */
    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString(Mockito.anyInt())).thenAnswer(invocation -> invocation.getArgument(0).toString());

        printer.printRecords(resultSet);

        String expected = "Test1,Test2,Test3" + LF;
        Assertions.assertEquals(expected, outputStream.toString());

        Mockito.when(resultSet.next()).thenReturn(false);
    }
}