import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

 private CSVPrinter printer;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 outputStream = new ByteArrayOutputStream();
 CSVFormat format = CSVFormat.DEFAULT.withQuoteAll();
 printer = new CSVPrinter(outputStream, format);
 }

 @After
 public void tearDown() throws IOException {
 printer.close();
 }

 @Test
 public void testPrintSingleValue() throws IOException {
 printer.print("Test");
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintNullValue() throws IOException {
 printer.print(null);
 String csvOutput = outputStream.toString();
 String expectedOutput = "";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleValuesWithoutQuotes() throws IOException {
 printer.printRecord(Arrays.asList("Test1", "Test2", "Test3"));
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test1,Test2,Test3";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleValuesWithQuotes() throws IOException {
 Quote quotePolicy = Quote.MINIMAL;
 CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(quotePolicy);
 printer = new CSVPrinter(outputStream, format);
 printer.printRecord(Arrays.asList("Test1", "Test2", "Test3"));
 String csvOutput = outputStream.toString();
 String expectedOutput = "\"Test1\",\"Test2\",\"Test3\"";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("This is a comment");
 String csvOutput = outputStream.toString();
 String expectedOutput = "\r\n# This is a comment\r\n";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintRecordWithQuotes() throws IOException {
 List<Object> record = Arrays.asList("Test\"String\"", "Test,String", "\r\nTest\r\nString", "\rTest\r\nString",
 "\"Test\"String\"");
 printer.printRecord(record);
 String csvOutput = outputStream.toString();
 String expectedOutput = "\"Test\"String\",Test,String,\r\nTest\r\nString,\rTest\r\nString,\"Test\"String\"";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintRecordWithEscapes() throws IOException {
 List<Object> record = Arrays.asList("Test,\"\"Test,String\"\"", "Test,\"Test,String with \r\nnewline and \t tab\"",
 "Test,\"Test,String with \"quotes\" and \b backspace\"");
 printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT.withEscapingMode(CSVFormat.ESCAPING_AUTO));
 printer.printRecord(record);
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test,\"\",\"Test,String with \\r\\nnewline and \\t tab\",Test,String with \"quotes\" and \\b backspace\"";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintRecordWithQuotesAndEscapes() throws IOException {
 List<Object> record = Arrays.asList("Test,\"Test,String with \\r\\nnewline and \\\\tab and \"quotes\" and \b backspace\"");
 printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT.withQuoteAll().withEscapingMode(CSVFormat.ESCAPING_AUTO));
 printer.printRecord(record);
 String csvOutput = outputStream.toString();
 String expectedOutput = "\"Test\",\"Test,String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace\"";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleRecords() throws IOException {
 List<Object> record1 = Arrays.asList("Test11", "Test21", "Test31");
 List<Object> record2 = Arrays.asList("Test12", "Test22", "Test32");
 printer.printRecords( Arrays.asList(record1, record2) );
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test11,Test21,Test31\r\nTest12,Test22,Test32\r\n";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleRecordsWithQuotes() throws IOException {
 Quote quotePolicy = Quote.ALL;
 CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(quotePolicy);
 printer = new CSVPrinter(outputStream, format);
 List<Object> record1 = Arrays.asList("Test11", "Test21", "Test31");
 List<Object> record2 = Arrays.asList("Test12", "Test22", "Test32");
 printer.printRecords( Arrays.asList(record1, record2) );
 String csvOutput = outputStream.toString();
 String expectedOutput = "\"Test11\",\"Test21\",\"Test31\"\r\n\"Test12\",\"Test22\",\"Test32\"\r\n";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleRecordsWithEscapes() throws IOException {
 List<Object> record1 = Arrays.asList("Test11, Test,String with \\r\nnewline and \\\\tab and \"quotes\" and \b backspace",
 "Test21, Test,String with \\r\nnewline and \\\\tab and \"quotes\" and \b backspace", "Test31, Test,String with \\r\nnewline and \\\\tab and \"quotes\" and \b backspace");
 printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT.withEscapingMode(CSVFormat.ESCAPING_AUTO));
 printer.printRecords( Arrays.asList(record1) );
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test11, Test,String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace,\r\nTest21, Test,String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace,\r\nTest31, Test,String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace\r\n";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleRecordsWithQuotesAndEscapes() throws IOException {
 List<Object> record1 = Arrays.asList("Test11, Test,\"String with \\r\nnewline and \\\\\\tab and \\\"quotes\\\" and \b backspace\"",
 "Test21, Test,\"String with \\r\nnewline and \\\\\\tab and \\\"quotes\\\" and \b backspace\"",
 "Test31, Test,\"String with \\r\nnewline and \\\\\\tab and \\\"quotes\\\" and \b backspace\"");
 printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT.withQuoteAll().withEscapingMode(CSVFormat.ESCAPING_AUTO));
 printer.printRecords( Arrays.asList(record1) );
 String csvOutput = outputStream.toString();
 String expectedOutput = "\"Test11\", \"Test\",\"String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace\"\r\n\"Test21\", \"Test\",\"String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace\"\r\n\"Test31\", \"Test\",\"String with \\r\\nnewline and \\\\\\tab and \\\"quotes\\\" and \\b backspace\"\r\n";
 assertEquals(expectedOutput, csvOutput);
 }

 @Test
 public void testPrintMultipleRecordsWithComment() throws IOException {
 List<Object> record1 = Arrays.asList("Test11", "Test21", "Test31");
 printer.printComment("This is a comment1");
 printer.printRecord(record1);
 printer.printComment("This is a comment2");
 List<Object> record2 = Arrays.asList("Test12", "Test22", "Test32");
 printer.printRecord(record2);
 String csvOutput = outputStream.toString();
 String expectedOutput = "Test11,Test21,Test31\r\n# This is a comment1\r\nTest12,Test22,Test32\r\n# This is a comment2\r\n";
 assertEquals(expectedOutput, csvOutput);
 }
}