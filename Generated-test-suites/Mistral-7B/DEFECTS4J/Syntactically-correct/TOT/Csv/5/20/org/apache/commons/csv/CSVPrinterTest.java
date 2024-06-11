package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.csv.CSVRecord.builder;
import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter writer;

    @Before
    public void setUp() {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        writer = new CSVPrinter(new StringWriter(outStream), new CSVFormat(',', '\'', null));
    }

    @Test
    public void testPrintString() throws IOException {
        writer.print("Hello, World!");
        String actual = writer.getOut().toString();
        assertEquals("Hello, World!\n", actual);
    }

    @Test
    public void testPrintObject() throws IOException {
        writer.print("Foo", "Bar");
        String actual = writer.getOut().toString();
        assertEquals("Foo,Bar\n", actual);
    }

    @Test
    public void testPrintNull() throws IOException {
        writer.print(null);
        String actual = writer.getOut().toString();
        assertEquals("\"\",\n", actual);
    }

    @Test
    public void testPrintNullAndString() throws IOException {
        writer.print(null, "Foo");
        String actual = writer.getOut().toString();
        assertEquals("\"\",Foo\n", actual);
    }

    @Test
    public void testPrintList() throws IOException {
        List<String> list = Arrays.asList("One", "Two", "Three");
        writer.printRecords(list);
        String actual = writer.getOut().toString();
        String expected = "One,Two,Three\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintEmptyList() throws IOException {
        writer.printRecords(Collections.emptyList());
        String actual = writer.getOut().toString();
        assertEquals("", actual);
    }

    @Test
    public void testPrintArray() throws IOException {
        String[] arr = new String[]{"One", "Two", "Three"};
        writer.printRecords(arr);
        String actual = writer.getOut().toString();
        String expected = "One,Two,Three\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        String[] arr = new String[0];
        writer.printRecords(arr);
        String actual = writer.getOut().toString();
        assertEquals("", actual);
    }

    @Test
    public void testPrintRecord() throws IOException {
        CSVRecord record = builder().add("One").add("Two").add("Three").build();
        writer.printRecord(record);
        String actual = writer.getOut().toString();
        String expected = "One,Two,Three\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintComment() throws IOException {
        writer.printComment("# This is a comment.");
        String actual = writer.getOut().toString();
        assertEquals("# This is a comment.\n", actual);
    }

    @Test
    public void testPrintRecordWithComment() throws IOException, SQLException {
        String query = "SELECT name, comment FROM users WHERE id = 1";
        ResultSet resultSet = TestUtils.getMockResultSet(query, "Alice", "# This is a comment.");
        writer.printRecords(resultSet);
        String actual = writer.getOut().toString();
        String expected = "Alice,# This is a comment.\n";
        assertEquals(expected, actual);
    }
}