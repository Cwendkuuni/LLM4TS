import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

 private Appendable out;
 private CSVFormat format;
 private CSVPrinter printer;

 @Before
 public void setUp() {
 StringWriter writer = new StringWriter();
 format = CSVFormat.EXPLICIT_ORDER_PREFERRED;
 printer = new CSVPrinter(writer, format);
 }

 @Test
 public void testPrint() throws IOException {
 printer.print("Test");
 printer.flush();
 assertEquals("Test\n", writer.toString());
 }

 @Test
 public void testPrintNull() throws IOException {
 printer.print(null);
 printer.flush();
 assertEquals("\"\"\n", writer.toString());
 }

 @Test
 public void testPrintEmptyArray() throws IOException {
 printer.printRecord();
 printer.println();
 printer.flush();
 assertEquals("", writer.toString());
 }

 @Test
 public void testPrintArray() throws IOException {
 String[] arr = {"Test1", "Test2", "Test3"};
 printer.printRecords(arr);
 printer.flush();
 assertEquals("Test1,Test2,Test3\nTest1,Test2,Test3\n", writer.toString());
 }

 @Test
 public void testPrintIterable() throws IOException {
 Iterable<String> iter = Arrays.asList("Test1", "Test2", "Test3");
 printer.printRecords(iter);
 printer.flush();
 assertEquals("Test1,Test2,Test3\nTest1,Test2,Test3\n", writer.toString());
 }

 @Test
 public void testPrintObject() throws IOException {
 Person p = new Person("TestName", 25);
 printer.printRecord(p);
 printer.println();
 printer.flush();
 assertEquals("TestName,25\n", writer.toString());
 }

 @Test
 public void testPrintObjectArray() throws IOException {
 Person[] arr = {new Person("TestName1", 25), new Person("TestName2", 30)};
 printer.printRecords(arr);
 printer.flush();
 assertEquals("TestName1,25\nTestName2,30\n", writer.toString());
 }

 @Test
 public void testPrintObjectIterable() throws IOException {
 Iterable<Person> iter = Arrays.asList(new Person("TestName1", 25), new Person("TestName2", 30));
 printer.printRecords(iter);
 printer.flush();
 assertEquals("TestName1,25\nTestName2,30\n", writer.toString());
 }

 @Test
 public void testPrintComment() throws IOException {
 printer.printComment("This is a comment");
 printer.println();
 printer.flush();
 assertEquals("\"\"\n# This is a comment\n", writer.toString());
 }

 @Test
 public void testPrintRecord() throws IOException {
 printer.printRecord(Arrays.asList("Test1", "Test2", "Test3"));
 printer.println();
 printer.flush();
 assertEquals("Test1,Test2,Test3\n", writer.toString());
 }

 @Test
 public void testPrintRecords() throws IOException, SQLException {
 Connection con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/testdb", "sa", "");
 Statement stmt = con.createStatement();
 ResultSet rs = stmt.executeQuery("SELECT * FROM testTable");
 printer.printRecords(rs);
 printer.flush();
 assertEquals("Test1,Test2,Test3\nTest4,Test5,Test6\n", writer.toString());
 }
}

class Person {
 private String name;
 private Integer age;

 public Person(String name, Integer age) {
 this.name = name;
 this.age = age;
 }

 public String toString() {
 return name + "," + age;
 }
}