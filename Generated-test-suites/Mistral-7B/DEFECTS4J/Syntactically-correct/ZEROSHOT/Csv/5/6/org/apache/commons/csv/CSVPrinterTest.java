import org.apache.commons.csv.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

 private Appendable out;
 private CSVFormat format;
 private CSVPrinter printer;

 @Before
 public void setUp() {
 StringWriter stringWriter = new StringWriter();
 out = stringWriter;
 format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(';')); // Set the delimiter to ';' for testing
 printer = new CSVPrinter(out, format);
 }

 @After
 public void tearDown() throws IOException {
 printer.close();
 }

 @Test
 public void testPrint() throws IOException {
 printer.print("Test");
 printer.print(42);
 printer.print(true);
 printer.flush();
 String result = out.toString();
 String expected = "Test,42,true\n";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintArray() throws IOException {
 String[] arr = {"A", "B", "C"};
 printer.printRecord(arr);
 printer.flush();
 String result = out.toString();
 String expected = "A,B,C\n";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintIterable() throws IOException {
 Iterable<String> iter = Arrays.asList("D", "E", "F");
 printer.printRecords(iter);
 printer.flush();
 String result = out.toString();
 String expected = "D,E,F\nD,E,F\n";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintQuotedValues() throws IOException {
 printer.print("\"Test with quotes\"");
 printer.flush();
 String result = out.toString();
 String expected = "\"Test with quotes\"\n";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintEscapedValues() throws IOException {
 printer.setEscape('\\'); // Set the escape character to '\\' for testing
 printer.print("Test with escape characters: \n\r\t\"");
 printer.flush();
 String result = out.toString();
 String expected = "Test with escape characters: \\\n\\r\\t\\\"\n";
 assertEquals(expected, result);
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("This is a comment");
 printer.flush();
 String result = out.toString();
 String expected = "\n--This is a comment--\n";
 assertNotNull(result); // The comment should not affect the result if it's empty
 assertEquals(expected, result);
 }
}