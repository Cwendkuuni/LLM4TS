import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

 private Appendable outputStream;
 private CSVFormat format;
 private CSVPrinter printer;

 @Before
 public void setUp() throws Exception {
 outputStream = new ByteArrayOutputStream();
 format = new CSVFormat(',', QuotePolicy.MINIMAL);
 printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
 }

 @Test
 public void testPrintNullValue() throws IOException {
 printer.print(null);
 printer.flush();
 String result = outputStream.toString();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPrintStringValue() throws IOException {
 printer.print("hello world");
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello world", result);
 }

 @Test
 public void testPrintIntegerValue() throws IOException {
 printer.print(42);
 printer.flush();
 String result = outputStream.toString();
 assertEquals("42", result);
 }

 @Test
 public void testPrintQuotedStringValue() throws IOException {
 printer.print("hello, world");
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello,\" world\"", result);
 }

 @Test
 public void testPrintEscapedValue() throws IOException {
 printer.print("hello, world\nwith a newline");
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello, world\\nwith a newline", result);
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("# This is a comment");
 printer.println();
 printer.flush();
 String result = outputStream.toString();
 assertTrue(result.contains("# This is a comment"));
 }

 @Test
 public void testPrintRecord() throws IOException {
 printer.printRecord(new String[]{"hello", "world"});
 printer.println();
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello,world", result);
 }

 @Test
 public void testPrintRecords() throws Exception {
 printer.printRecords(Arrays.asList("hello1", "world1", "hello2", "world2"));
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello1,world1,hello2,world2\n", result);
 }

 @Test
 public void testPrintRecordsNullCollection() throws IOException {
 printer.printRecords(null);
 printer.flush();
 String result = outputStream.toString();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testPrintRecordsIterable() throws Exception {
 Iterable<String> iterable = Arrays.asList("hello1", "world1", "hello2", "world2");
 printer.printRecords(iterable);
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello1,world1,hello2,world2\n", result);
 }

 @Test
 public void testPrintRecordsArray() throws IOException {
 String[] array = new String[]{"hello1", "world1", "hello2", "world2"};
 printer.printRecords(array);
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello1,world1,hello2,world2\n", result);
 }

 @Test
 public void testPrintRecordsObject() throws IOException {
 Object[] array = new Object[]{1, "hello1", 3.14, "world1"};
 printer.printRecords(array);
 printer.flush();
 String result = outputStream.toString();
 assertEquals("1,hello1,3.14,world1\n", result);
 }

 @Test
 public void testPrintRecordsObjectArray() throws IOException {
 Object[] array = new Object[]{new Object[]{"hello1", "world1"}, new Object[]{"hello2", "world2"}};
 printer.printRecords(array);
 printer.flush();
 String result = outputStream.toString();
 assertEquals("hello1,world1,hello2,world2\n", result);
 }

 @Test
 public void testPrintRecordsResultSet() throws SQLException, IOException {
 String url = "jdbc:h2:mem:testdb";
 String driver = "org.h2.Driver";
 Class.forName(driver);
 try (Connection connection = DriverManager.getConnection(url);
 Statement statement = connection.createStatement();
 ResultSet resultSet = statement.executeQuery("SELECT name,value FROM properties")) {
 printer.printRecords(resultSet);
 printer.flush();String result = outputStream.toString();
 assertEquals("name,value\nhello,world\nproperty,value", result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintRecordsNullFormat() throws IOException {
 printer = new CSVPrinter(outputStream, null);
 printer.printRecord(new String[]{"hello", "world"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPrintRecordsNullOutput() throws IOException {
 printer = new CSVPrinter(null, format);
 printer.printRecord(new String[]{"hello", "world"});
 }

 @Test
 public void testClose() throws IOException {
 printer.close();
 assertNull(printer.getOut());
 }

 @Test
 public void testFlush() throws IOException {
 printer.flush();
 String result = outputStream.toString();
 assertTrue(result.isEmpty());
 printer.print("hello world");
 printer.flush();
 String result2 = outputStream.toString();
 assertEquals("hello world", result2);
 printer.close();
 }
}