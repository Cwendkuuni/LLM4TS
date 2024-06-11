import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private ByteArrayOutputStream output;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(output), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintStrings() throws IOException {
        printer.print("Hello World");
        printer.println();
        printer.close();

        String expected = "Hello World\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        printer.println();
        printer.close();

        String expected = "";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.println();
        printer.close();

        String expected = CSVFormat.DEFAULT.getNullString();
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintNumbers() throws IOException {
        printer.print(12345);
        printer.println();
        printer.print(3.1415);
        printer.println();
        printer.close();

        String expected = "12345\n3.1415\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuotedStrings() throws IOException {
        printer.print("\"Hello World\"");
        printer.println();
        printer.close();

        String expected = "\"Hello World\"\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintEscapedStrings() throws IOException {
        printer.print("Hello World\"");
        printer.println();
        printer.close();

        String expected = "Hello World\"\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedStrings() throws IOException {
        printer.print("\"Hello World\"\"");
        printer.println();
        printer.close();

        String expected = "\"Hello World\"\"\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedNestedStrings() throws IOException {
        printer.print("\"Hello World\"\"Hello World\"");
        printer.println();
        printer.close();

        String expected = "\"Hello World\"\"Hello World\"\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedNestedStringsWithEscapeCharacter() throws IOException {
        printer.print("\"Hello World\"\"\\\"Hello World\\\"");
        printer.println();
        printer.close();

        String expected = "\"Hello World\"\"\\\"Hello World\\\"\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedStringsWithDelimiter() throws IOException {
        printer.setDelimiter(';');
        printer.print("\"Hello World\"");
        printer.print(";");
        printer.print("12345");
        printer.println();
        printer.close();

        String expected = "\"Hello World\"\";12345\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        Statement statement = DbUtils.createStatement(JdbcUtils.getConnection("jdbc:hsqldb:mem:test"));
        String createTable = "CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(50))";
        statement.executeUpdate(createTable);

        String insert = "INSERT INTO test VALUES (1, 'John Doe'), (2, 'Jane Doe')";
        statement.executeUpdate(insert);

        ResultSet resultSet = statement.executeQuery("SELECT * FROM test");

        printer.setDelimiter(',');
        printer.printRecords(resultSet);
        printer.close();

        String expected = "id,name\n1,John Doe\n2,Jane Doe\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        printer.close();

        String expected = "# This is a comment\n";
        assertEquals(expected, output.toString());
    }
}