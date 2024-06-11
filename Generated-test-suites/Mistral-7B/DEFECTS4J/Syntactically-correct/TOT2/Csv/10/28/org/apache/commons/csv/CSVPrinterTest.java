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
import java.sql.Statement;
import java.util.Arrays;

public class CSVPrinterTest {

 private CSVPrinter printer;
 private ByteArrayOutputStream out;
 private StringWriter stringWriter;

 @Before
 public void setup() throws SQLException, IOException {
 out = new ByteArrayOutputStream();
 stringWriter = new StringWriter(out);
 printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
 }

 @Test
 public void testPrintString() throws IOException {
 printer.print("Hello, World!");
 printer.println();
 Assert.assertEquals("Hello, World!\n", stringWriter.toString());
 }

 @Test
 public void testPrintNull() throws IOException {
 printer.print(null);
 printer.println();
 Assert.assertEquals("\"\"\n", stringWriter.toString());
 }

 @Test
 public void testPrintEscapedString() throws IOException {
 printer.print("This is a test: with escaped characters, \"double quotes\", LF, and CR.");
 printer.println();
 Assert.assertEquals("This is a test: with escaped characters, \"double quotes\", \"\",\\n,\\r.\n", stringWriter.toString());
 }

 @Test
 public void testPrintObject() throws IOException {
 printer.print(new Object[]{"Hello", "World"});
 printer.println();
 Assert.assertEquals("Hello, World\n", stringWriter.toString());
 }

 @Test
 public void testPrintIterable() throws IOException {
 printer.print(Arrays.asList("One", "Two", "Three"));
 printer.println();
 Assert.assertEquals("One, Two, Three\n", stringWriter.toString());
 }

 @Test
 public void testPrintObjectArray() throws IOException {
 printer.print(new Object[]{"One", "Two", "Three"});
 printer.println();
 Assert.assertEquals("One, Two, Three\n", stringWriter.toString());
 }

 @Test
 public void testPrintRecord() throws IOException {
 printer.printRecord(Arrays.asList("One", "Two", "Three"));
 printer.println();
 Assert.assertEquals("One, Two, Three\n", stringWriter.toString());
 }

 @Test
 public void testPrintRecords() throws IOException, SQLException {
 Statement statement = TestHelper.getConnection().createStatement();
 ResultSet resultSet = statement.executeQuery("SELECT * FROM test_data");
 printer.printRecords(resultSet);
 printer.close();
 Assert.assertEquals(TestHelper.TEST_DATA, stringWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() throws IOException {
 try {
 printer = new CSVPrinter(null, CSVFormat.DEFAULT);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("format cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOut() throws IOException {
 try {
 printer = new CSVPrinter("", CSVFormat.DEFAULT);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("out cannot be null or empty", e.getMessage());
 }
 }

 @Test(expected = IOException.class)
 public void testClose() throws IOException {
 printer.close();
 printer.print("Hello, World!");
 printer.println();
 Assert.assertEquals("", stringWriter.toString());
 }

 @Test(expected = IOException.class)
 public void testFlush() throws IOException {
 printer.flush();
 printer.print("Hello, World!");
 printer.println();
 Assert.assertEquals("", stringWriter.toString());
 }

 @Test(expected = SQLException.class)
 public void testPrintRecordsException() throws SQLException, IOException {
 try {
 printer.printRecords(null);
 } catch (SQLException e) {
 Assert.assertEquals("ResultSet cannot be null", e.getMessage());
 }
 }

 @Test(expected = SQLException.class)
 public void testPrintRecordsEmptyResultSet() throws SQLException, IOException {
 Statement statement = TestHelper.getConnection().createStatement();
 ResultSet resultSet = statement.executeQuery("SELECT * FROM empty_table");
 printer.printRecords(resultSet);
 printer.close();
 Assert.assertEquals("", stringWriter.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuotePolicy() {
 try {
 CSVFormat format = new CSVFormat(',', Quote.ILLEGAL, null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("QuotePolicy 'ILLEGAL' is not supported. Valid values are: ALL, NONE, NON_NUMERIC, MINIMAL", e.getMessage());
 }
 }

 @Test
 public void testCustomQuotePolicy() throws IOException {
 CSVFormat format = new CSVFormat(',', Quote.MINIMAL, null);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 StringWriter stringWriter = new StringWriter(out);
 CSVPrinter printer = new CSVPrinter(stringWriter, format);
 printer.print("One\ttwo");
 printer.println();
 Assert.assertEquals("One	two\n", stringWriter.toString());
 printer.close();
 }
}