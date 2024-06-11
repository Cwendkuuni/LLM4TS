import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

 private ByteArrayOutputStream outputStream;
 private CSVPrinter printer;

 @Before
 public void setUp() throws IOException {
 outputStream = new ByteArrayOutputStream();
 printer = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT);
 }

 @After
 public void tearDown() throws IOException {
 printer.close();
 }

 @Test
 public void testPrintNull() throws IOException {
 printer.print(null);
 printer.println();
 String csv = outputStream.toString();
 assertEquals("", csv);
 }

 @Test
 public void testPrintBoolean() throws IOException {
 printer.print(true);
 printer.println();
 csvPrintEquals("true");

 printer.print(false);
 printer.println();
 csvPrintEquals("false");
 }

 @Test
 public void testPrintByte() throws IOException {
 printer.print((byte) 1);
 printer.println();
 csvPrintEquals("1");

 printer.print((byte) -128);
 printer.println();
 csvPrintEquals("-128");
 }

 @Test
 public void testPrintShort() throws IOException {
 printer.print((short) 12345);
 printer.println();
 csvPrintEquals("12345");
 }

 @Test
 public void testPrintInt() throws IOException {
 printer.print(123456);
 printer.println();
 csvPrintEquals("123456");
 }

 @Test
 public void testPrintLong() throws IOException {
 printer.print(12345678901234L);
 printer.println();
 csvPrintEquals("12345678901234");
 }

 @Test
 public void testPrintFloat() throws IOException {
 printer.print(123.45f);
 printer.println();
 csvPrintEquals("123.45");
 }

 @Test
 public void testPrintDouble() throws IOException {
 printer.print(123.456);
 printer.println();
 csvPrintEquals("123.456");
 }

 @Test
 public void testPrintString() throws IOException {
 printer.print("Hello, World!");
 printer.println();
 csvPrintEquals("\"Hello, World!\"");
 }

 @Test
 public void testPrintArrayOfString() throws IOException {
 String[] strings = {"Hello", "World!"};
 printer.print(strings);
 printer.println();
 csvPrintEquals(Arrays.toString(strings).replaceAll("[\\[\\]]", ""));
 }

 @Test
 public void testPrintArrayOfInt() throws IOException {
 int[] numbers = {1, 2, 3};
 printer.print(numbers);
 printer.println();
 csvPrintEquals(Arrays.toString(numbers).replaceAll("[\\[\\]]", ""));
 }

 @Test
 public void testPrintRecord() throws IOException {
 CSVRecord record = new CSVRecord(CSVFormat.DEFAULT, new Object[]{"Hello", 123, 123.45});
 printer.printRecord(record);
 printer.println();
 csvPrintEquals(record.toString());
 }

 @Test
 public void testPrintRecords() throws IOException {
 Object[][] records = new Object[][]{
 {"Hello1", 1, 1.0},
 {"Hello2", 2, 2.0},
 {"Hello3", 3, 3.0}
 };
 printer.printRecords(Arrays.asList(records));
 printer.println();
 csvPrintEquals(Arrays.deepToString(records).replaceAll("[\\[\\]]", ""));
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("This is a comment");
 printer.println();
 String csv = outputStream.toString();
 assertEquals("#This is a comment\n", csv);
 }

 @Test
 public void testPrintRecordWithComment() throws IOException {
 printer.printComment("This is a comment");
 printer.printRecord(new CSVRecord(CSVFormat.DEFAULT, new Object[]{"Hello", "World!"}));
 printer.println();
 String csv = outputStream.toString();
 assertEquals("#This is a comment\nHello,\"World!\"\n", csv);
 }

 @Test
 public void testQuoting() throws IOException {
 printer.setQuotePolicy(Quote.MINIMAL);
 printer.print("Hello,\"World!\",Goodbye");
 printer.println();
 String csv = outputStream.toString();
 assertEquals("Hello,\"World!\",Goodbye", csv);

 printer.setQuotePolicy(Quote.ALL);
 printer.print("Hello,\"World!\",Goodbye");
 printer.println();
 csv = outputStream.toString();
 assertEquals("\"Hello\",\"World!\",\"Goodbye\"", csv);
 }

 @Test
 public void testEscaping() throws IOException {
 printer.setEscapeCharacter('!');
 printer.print("Hello, World!");
 printer.println();
 csvPrintEquals("Hello, World!\\!");
 printer.setEscapeCharacter('\0');
 }

 @Test
 public void testPrintCrLf() throws IOException {
 printer.println();
 printer.println();
 csvPrintEquals("\r\n\r\n");
 }

 private void csvPrintEquals(String expected) throws IOException {
 String csv = outputStream.toString();
 assertEquals(expected, csv);
 }
}