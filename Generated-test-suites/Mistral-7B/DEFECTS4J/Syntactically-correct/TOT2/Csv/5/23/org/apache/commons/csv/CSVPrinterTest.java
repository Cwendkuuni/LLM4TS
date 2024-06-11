import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() throws IOException, SQLException {
        // Test constructor
        Appendable output = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(output, format);

        // Test print method with basic case
        printer.print("Hello, World!");
        printer.println();
        String expectedOutput = "Hello, World!\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test print method with null value
        printer.print(null);
        printer.println();
        expectedOutput = Constants.EMPTY;
        Assert.assertEquals(expectedOutput, output.toString());

        // Test print method with number value
        printer.print(123);
        printer.println();
        expectedOutput = "123\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test print method with List value
        List<String> values = Arrays.asList("Value1", "Value2", "Value3");
        printer.printRecords(values);
        expectedOutput = "Value1,Value2,Value3\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test print method with Object[] value
        Object[] objectValues = new Object[]{"Value1", "Value2", "Value3"};
        printer.printRecords(objectValues);
        expectedOutput = "Value1,Value2,Value3\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test print method with Iterable value
        Iterable<String> iterableValues = Collections.singletonList("Value1");
        printer.printRecords(iterableValues);
        expectedOutput = "Value1\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with basic case
        printer.printRecord(Arrays.asList("Value1", "Value2", "Value3"));
        expectedOutput = "Value1,Value2,Value3\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with null value
        printer.printRecord(null);
        expectedOutput = Constants.EMPTY;
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with Object[] value
        printer.printRecord(new Object[]{"Value1", "Value2", "Value3"});
        expectedOutput = "Value1,Value2,Value3\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printComment method
        printer.printComment("This is a comment.");
        expectedOutput = Constants.COMMENT + SP + "This is a comment.\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test println method
        printer.println();
        expectedOutput += Constants.LF;
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with empty Iterable
        printer.printRecords(Collections.emptyList());
        expectedOutput += ",\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with empty Object[]
        printer.printRecords(new Object[0]);
        expectedOutput += Constants.LF;
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecord method with empty Iterable and no newline
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        printer = new CSVPrinter(baos, format);
        printer.printRecords(Collections.emptyList());
        expectedOutput += ",";
        Assert.assertEquals(expectedOutput, baos.toString());

        // Test printRecord method with empty Object[] and no newline
        baos = new ByteArrayOutputStream();
        printer = new CSVPrinter(baos, format);
        printer.printRecords(new Object[0]);
        Assert.assertEquals(expectedOutput, baos.toString());

        // Test printRecords method with empty Iterable
        printer.printRecords
                (Collections.singletonList(Collections.emptyList()));
        expectedOutput += "\n,";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecords method with empty Object[]
        printer.printRecords(new Object[0][0]);
        expectedOutput += "\n";
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecords method with empty ResultSet
        printer = new CSVPrinter(output, format);
        printer.printRecords(Collections.emptyList());
        expectedOutput += Constants.LF;
        Assert.assertEquals(expectedOutput, output.toString());

        // Test printRecords method with ResultSet having no columns
        ResultSet rs = null;
        try {
            rs = TestUtil.getEmptyResultSet();
            printer.printRecords(rs);
            expectedOutput += Constants.LF;
            Assert.assertEquals(expectedOutput, output.toString());
        } finally {
            TestUtil.closeResultSet(rs);
        }

        // Test printRecords method with ResultSet having one column and one row
        String expectedValue = "Value1";
        rs = TestUtil.getResultSetWithOneColumnAndOneRow(expectedValue);
        try {
            printer.printRecords(rs);
            expectedOutput += expectedValue + Constants.LF;
            Assert.assertEquals(expectedOutput, output.toString());
        } finally {
            TestUtil.closeResultSet(rs);
        }

        // Test printRecords method with ResultSet having multiple columns and rows
        String[] expectedValues = new String[]{"Value1", "Value2", "Value3"};
        rs = TestUtil.getResultSetWithMultipleColumnsAndRows(expectedValues);
        try {
            printer.printRecords(rs);
            String expectedOutputTemp = "";
            for (String expectedValue : expectedValues) {
                expectedOutputTemp += expectedValue + "\n";
            }
            expectedOutput += expectedOutputTemp;
            Assert.assertEquals(expectedOutput, output.toString());
        } finally {
            TestUtil.closeResultSet(rs);
        }

        // Test flush method
        printer.flush();
        Assert.assertEquals(expectedOutput, output.toString());

        // Test close method
        printer.close();
        Assert.assertEquals(expectedOutput, output.toString());
    }
}