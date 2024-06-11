import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), new CSVFormat(
                new CSVFormat.Builder()
                        .setDelimiter(';')
                        .setHeaderSeparator(CSVFormat.DEFAULT.getHeaderSeparator())
                        .setQuoteMode(QuoteMode.MINIMAL)
                        .build()));
    }

    @Test
    public void testPrintBasic() throws IOException {
        printer.print(1);
        printer.print(1.2);
        printer.print("Hello World!");
        printer.println();

        List<String> lines = Arrays.asList(
                "1,1.2,Hello World!",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintQuote() throws IOException {
        printer.printQuote('"');
        printer.print("\"Hello World\"");
        printer.println();

        List<String> lines = Arrays.asList(
                "\",\"Hello World\"",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintEscape() throws IOException {
        printer.printEscape('\\');
        printer.print("\\Hello World\\");
        printer.println();

        List<String> lines = Arrays.asList(
                "\\,\\Hello World\\",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecords(Arrays.asList(1, 1.2, "Hello World!"));

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "1,1.2,Hello World!",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintEmptyRecords() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecords(Collections.emptyList());

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException, SQLException {
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecords(new Object[] {1, 1.2, "Hello World!"});

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "1,1.2,Hello World!",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecords(Arrays.asList(1, 1.2, "Hello World!"));

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "1,1.2,Hello World!",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        printer.printComment("This is a comment");
        printer.println();
        printer.printRecords(resultSet("testData.csv"));

        List<String> lines = Arrays.asList(
                "; This is a comment",
                "1,1.2,Hello World!",
                "2,2.2,Goodbye World!",
                "",
                "");
        Assertions.assertEquals(String.join(System.lineSeparator(), lines), outputStream.toString());
    }

    private ResultSet resultSet(String fileName) throws SQLException {
        // Implement a mock ResultSet here that returns test data.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCSVPrinter() throws IOException {
        printer = new CSVPrinter(null, new CSVFormat(new CSVFormat.Builder().build()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCSVFormat() throws IOException {
        printer = new CSVPrinter(new StringWriter(outputStream), null);
    }
}