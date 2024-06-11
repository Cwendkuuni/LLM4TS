import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test constructor
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        Appendable out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, format);

        // Test print methods
        printer.print("Test Value");
        printer.print(null);
        printer.print(123L);
        printer.print(3.14);
        printer.print(true);

        printer.println(); // New record

        printer.print("Test Value 2");
        printer.print(null);
        printer.print(1234L);
        printer.print(3.14159);
        printer.print(false);

        printer.println(); // New record

        printer.printComment("This is a comment.");

        // Test printRecord method
        String[] record1 = {"Test Value 1", "Test Value 2", "Test Value 3"};
        printer.printRecord(record1);

        // Test printRecords method with Iterable
        Iterable<String> record2 = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 4;
                    }

                    @Override
                    public String next() {
                        return "Test Value " + (index + 1);
                    }
                };
            }
        };
        printer.printRecords(record2);

        // Test printRecords method with Object[]
        String[] record3 = {"Test Value 1", "Test Value 2", "Test Value 3"};
        printer.printRecords(record3);

        // Test printRecords method with Iterable<Object[]>
        Iterable<Object[]> records = new Iterable<Object[]>() {
            @Override
            public Iterator<Object[]> iterator() {
                return new Iterator<Object[]>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }

                    @Override
                    public Object[] next() {
                        Object[] record = new Object[3];
                        for (int i = 0; i < 3; i++) {
                            record[i] = "Test Value " + (index * 3 + i + 1);
                        }
                        index++;
                        return record;
                    }
                };
            }
        };
        printer.printRecords(records);

        // Test printRecords method with ResultSet
        ResultSet resultSet = createTestResultSet();
        printer.printRecords(resultSet);

        // Test getOut method
        Assert.assertEquals(printer.getOut().toString(), getExpectedOutput());

        // Close resources
        printer.close();
        resultSet.close();
    }

    private ResultSet createTestResultSet() throws SQLException {
        String query = "SELECT 'Test Value 1', 'Test Value 2', 'Test Value 3' FROM test_table";
        return getTestDatabaseConnection().createStatement().executeQuery(query);
    }

    private String getExpectedOutput() {
        return "Test Value,Test Value 1\n" +
                "Test Value,Test Value 2,Test Value 3\n" +
                "# This is a comment.\n" +
                "Test Value 1,Test Value 2,Test Value 3\n" +
                "Test Value 4,Test Value 5,Test Value 6\n" +
                "Test Value 7,Test Value 8,Test Value 9\n";
    }
}