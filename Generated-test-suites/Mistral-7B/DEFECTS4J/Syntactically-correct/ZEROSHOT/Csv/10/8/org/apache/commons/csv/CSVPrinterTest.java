import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream output;

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
        try {
            printer = new CSVPrinter(new StringWriter(output), DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPrint() throws IOException, SQLException {
        // test printing simple values
        printer.print("hello");
        printer.print(123);
        printer.print(true);
        printer.print(null);
        printer.println();

        Assert.assertEquals("hello,123,true,null\n", output.toString());

        // test printing complex values
        printer.print(new String[]{"hello", "world", "123", "456"});
        printer.println();

        Assert.assertEquals("hello,world,123,456\n", output.toString());

        // test printing a comment
        printer.printComment("This is a comment");

        Assert.assertEquals("Comment: This is a comment\n", output.toString());

        // test printing a record from a result set
        String sql = "SELECT name, age FROM users WHERE id = 1";
        ResultSet resultSet = getMockResultSet(sql, new Object[]{"John Doe", 30});
        printer.printRecords(resultSet);

        Assert.assertEquals("name,age\nJohn Doe,30\n", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        try {
            new CSVPrinter(null, DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOutput() {
        try {
            new CSVPrinter(new Object(), DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatWithQuotingAndEscaping() {
        CSVFormat format = new CSVFormat('|', '|', ',', '\'', '\'');
        try {
            new CSVPrinter(new StringWriter(output), format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testQuotingWithNoQuotePolicy() {
        CSVFormat format = new CSVFormat('|', '|', ',', '\'', null);
        try {
            new CSVPrinter(new StringWriter(output), format);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet getMockResultSet(String sql, Object[] data) throws SQLException {
        return new MockResultSet(sql, data);
    }

}

class MockResultSet implements ResultSet {

    private final String sql;
    private final Object[] data;
    private int index = 0;

    public MockResultSet(String sql, Object[] data) {
        this.sql = sql;
        this.data = data;
    }

    @Override
    public boolean next() throws SQLException {
        return index < data.length;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return (String) data[index++];
    }

    @Override
    public int getMetaData() throws SQLException {
        throw new UnsupportedOperationException();
    }

}