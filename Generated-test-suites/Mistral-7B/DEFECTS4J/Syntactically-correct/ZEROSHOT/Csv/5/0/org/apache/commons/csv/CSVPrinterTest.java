import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() throws IOException {
        format = new CSVFormatBuilder()
                .setDelimiter(';') // set delimiter as an example
                .setHeader("header1", "header2")
                .build();
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }

    @Test
    public void testPrintBasic() throws IOException, SQLException {
        printer.print("test");
        printer.println();
        printer.print("123");
        printer.print("true");
        printer.println();
        printer.printComment("comment1");
        printer.flush();

        String expected = "test;" + CR + LF + "123;" + CR + LF + "true;" + CR + LF + "# comment1" + CR + LF;
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintQuotedValue() throws IOException, SQLException {
        printer.print("test,\"test with \"quotes\"");
        printer.println();
        printer.flush();

        String expected = "test,\"test with \"quotes\"\";" + CR + LF;
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        Object[] record = {"test1", 123, true};
        printer.printRecord(record);
        printer.flush();

        String expected = "test1;123;true;" + CR + LF;
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        Object[][] records = {{"test1", 123, true}, {"test2", 456, false}};
        printer.printRecords(records);
        printer.flush();

        String expected = "test1;123;true;" + CR + LF + "test2;456;false;" + CR + LF;
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        ResultSet resultSet = getTestResultSet();
        printer.printRecords(resultSet);
        printer.flush();

        String expected = "header1;header2;" + CR + LF + "John Doe;Doe" + CR + LF + "Jane Doe;Doe" + CR + LF;
        String actual = outputStream.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        new CSVPrinter(new OutputStreamWriter(outputStream), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() {
        try {
            new CSVPrinter(null, format);
        } catch (IOException e) {
            throw new AssertionError("Should have thrown IllegalArgumentException.", e);
        }
    }

    private ResultSet getTestResultSet() throws SQLException {
        // Create a test ResultSet
        // ...
        return resultSet;
    }
}