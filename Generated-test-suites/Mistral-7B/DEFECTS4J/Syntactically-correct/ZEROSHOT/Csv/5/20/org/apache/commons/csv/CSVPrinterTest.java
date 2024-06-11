import org.apache.commons.csv.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws Exception {
        StringWriter writer = new StringWriter();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(writer, format);

        // Test print method
        printer.print("Hello, World!");
        printer.flush();
        assertEquals("\"Hello, World!\"", writer.toString().trim());

        // Test print method with null value
        printer.print(null);
        printer.flush();
        assertEquals("", writer.toString().trim());

        // Test print method with Object value
        printer.print(123);
        printer.flush();
        assertEquals("123", writer.toString().trim());

        // Test print method with CharSequence value
        printer.print("Test");
        printer.flush();
        assertEquals("Test", writer.toString().trim());

        // Test println method
        printer.println();
        printer.flush();
        assertEquals("", writer.toString().trim());

        // Test println method with String value
        printer.println("Hello, Line!");
        printer.flush();
        assertEquals("Hello, Line!\n", writer.toString().trim());

        // Test printRecord method with Iterable value
        List<String> values = Arrays.asList("Value 1", "Value 2", "Value 3");
        printer.printRecord(values);
        printer.flush();
        String expected = "Value 1,\"Value 2\",\"Value 3\"\n";
        assertEquals(expected, writer.toString().trim());

        // Test printRecord method with Object array value
        Object[] array = new Object[]{"Value 1", "Value 2", "Value 3"};
        printer.printRecord(array);
        printer.flush();
        assertEquals(expected, writer.toString().trim());

        // Test printRecord method with Iterable<Object[]> value
        List<Object[]> records = Arrays.asList(new Object[]{"Record 1", "Record 2", "Record 3"}, new Object[]{"Record 4", "Record 5", "Record 6"});
        printer.printRecords(records);
        printer.flush();
        String recordSeparator = System.lineSeparator();
        String expectedRecords = "Record 1,\"Record 2\",\"Record 3\"\nRecord 4,\"Record 5\",\"Record 6\"\n";
        assertEquals(expectedRecords, writer.toString().trim());

        // Test printComment method
        printer.printComment("This is a comment.");
        printer.flush();
        assertEquals("# This is a comment.", writer.toString().trim());

        // Test close method
        printer.close();
        String result = writer.toString().trim();
        assertFalse(writer.ready());
        assertEquals(result, printer.getOut().toString().trim());

        // Test flush method
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
        printer.print("Test");
        printer.flush();
        assertFalse(writer.ready());
        assertEquals("Test", printer.getOut().toString().trim());

        // Test print method with escaped characters
        printer.close();
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
        printer.print("Test\n\"\"\"");
        printer.flush();
        assertEquals("Test\n\"Test\\n\"\"\"\"", writer.toString().trim());

        // Test print method with quoted values
        printer.close();
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
        printer.printRecord(Arrays.asList("Test, \"Test, Test\"", "Test, \"Test, \"Test\""));
        printer.flush();
        expected = "Test,\"Test, \"Test\"\"\",Test,\"Test, \\\"Test\\\"\"\"\n";
        assertEquals(expected, writer.toString().trim());
    }
}