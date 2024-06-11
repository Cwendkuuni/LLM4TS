import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
                .withCommentCharacter('#'));
        printer = new CSVPrinter(new StringWriter(new StringWriter(outputStream)), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        assertEquals("\"\"\n", outputStream.toString());
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        printer.println();
        assertEquals("\"\"\n", outputStream.toString());
    }

    @Test
    public void testPrintEmptyIterable() throws IOException {
        printer.printRecords(java.util.Collections.emptyList());
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        printer.printRecords((Object[]) new Object[0]);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Value");
        printer.println();
        assertEquals("Value\n", outputStream.toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.printRecord("Value1", "Value2", "Value3");
        printer.println();
        assertEquals("Value1,Value2,Value3\n", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment");
        printer.println();
        assertEquals("# This is a comment\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordFromIterable() throws IOException, SQLException {
        String sql = "SELECT name, age FROM users";
        try (Statement stmt = org.apache.commons.dbcp2.BasicDataSourceFactory.createDataSource()
                .getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            printer.printRecords(rs);
        }
        assertEquals("name,age\n", outputStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        new CSVPrinter(outputStream, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() throws IOException {
        new CSVPrinter(null, format);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValue() throws IOException {
        printer.print(null);
    }
}