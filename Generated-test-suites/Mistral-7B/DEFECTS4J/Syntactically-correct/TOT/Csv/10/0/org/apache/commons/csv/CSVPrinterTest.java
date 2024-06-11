package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    @org.junit.Test
    public void testCSVPrinter() throws Exception {
        // Test printing a simple record
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printRecord(Arrays.asList("Alice", 23, "Bob", 45.0f));
                printer.flush();

                String expected = "\"Alice\",23,\"Bob\",45.0\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printing a record with empty fields
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printRecord(Collections.emptyList());
                printer.flush();

                String expected = "\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printing a comment
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printComment("This is a comment.");
                printer.println();
                printer.flush();

                String expected = format.getCommentStart() + " This is a comment.\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printing an empty line
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.println();
                printer.flush();

                String expected = "\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printing an empty line after a record
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printRecord(Arrays.asList("Alice", 23, "Bob", 45.0f));
                printer.println();
                printer.flush();

                String expected = "\"Alice\",23,\"Bob\",45.0\n\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printing a record with null values
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printRecord(Arrays.asList("Alice", null, "Bob", null, new Object()));
                printer.flush();

                String expected = "\"Alice\",, \"Bob\", , \n";
                assertEquals(expected, out.toString());
            }
        }

        // Test quote policy: MINIMAL
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVFormat minimalFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), minimalFormat)) {
                printer.printRecord(Arrays.asList("Alice", "Bob", "Alice and Bob"));
                printer.flush();

                String expected = "\"Alice\",\"Bob\",\"Alice and Bob\"\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test quote policy: NONE
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVFormat noneFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), noneFormat)) {
                printer.printRecord(Arrays.asList("Alice", "Bob", "Alice and Bob"));
                printer.flush();

                String expected = "Alice,Bob,Alice and Bob\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test quote policy: NON_NUMERIC
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVFormat nonNumericFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NON_NUMERIC));
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), nonNumericFormat)) {
                printer.printRecord(Arrays.asList("Alice", 23, "Bob", 45.0f));
                printer.flush();

                String expected = "\"Alice\",23,\"Bob\",45.0\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test quote policy: ALL
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVFormat allFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL));
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), allFormat)) {
                printer.printRecord(Arrays.asList("Alice", "Bob", "Alice and Bob"));
                printer.flush();

                String expected = "\"Alice\",\"Bob\",\"Alice and Bob\"\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test escaping
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader(out.toString())), format)) {
                printer.printRecord(Arrays.asList("Alice\nBob", "Alice,\"Bob\"", "Alice,\"Alice\""));
                printer.flush();

                String expected = "\"Alice\\nBob\",\"Alice,\"Bob\",\"Alice,\"Alice\"\n";
                assertEquals(expected, out.toString());
            }
        }

        // Test printComment with invalid comment character
        assertThrows(IllegalArgumentException.class, () -> new CSVFormat(CSVFormat.DEFAULT.withComment('X')));

        // Test printComment with empty comment
        try (CSVPrinter printer = new CSVPrinter(new StringWriter(new StringReader("")), format)) {
            printer.printComment("");
            printer.flush();

            String expected = "\n";
            assertEquals(expected, printer.getOut().toString());
        }

        // Test printComment with comment character at the beginning
        try (StringWriter writer = new StringWriter()) {
            try (CSVPrinter printer = new CSVPrinter(writer, format)) {
                printer.printComment("");
                printer.println();
                printer.print("test");
                printer.flush();

                String expected = format.getCommentStart() + "\n" + "test\n";
                assertEquals(expected, writer.toString());
            }
        }
    }

    /**
     * Helper class to read the contents of a String as an InputStream.
     */
    private static InputStream newStringReader(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }
}