import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "hello";
 Object obj = String.class;
 Object result = TypeHandler.createValue(str, obj);
 assertEquals("hello", result);
 }

 @Test
 public void testCreateValue_StringString() {
 String str = "hello";
 Object obj = PatternOptionBuilder.STRING_VALUE;
 Object result = TypeHandler.createValue(str, obj);
 assertEquals("hello", result);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "java.lang.String";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals("hello", result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateValue_NullClass() {
 String str = "java.lang.String";
 Class clazz = null;
 Object result = TypeHandler.createValue(str, clazz);
 fail("Expected NullPointerException but got: " + result);
 }

 @Test
 public void testCreateValue_StringObjectNotFound() {
 String str = "com.nonexistent.Class";
 Object obj = Object.class;
 Object result = TypeHandler.createValue(str, obj);
 assertNull(result);
 }

 @Test
 public void testCreateObject() {
 String className = "java.lang.String";
 Object result = TypeHandler.createObject(className);
 assertInstanceOf(String.class, result);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_ClassNotFound() {
 String className = "com.nonexistent.Class";
 Object result = TypeHandler.createObject(className);
 fail("Expected ClassNotFoundException but got: " + result);
 }

 @Test
 public void testCreateNumber_Long() {
 String str = "123";
 Number result = TypeHandler.createNumber(str);
 assertEquals(123L, result.longValue());
 }

 @Test
 public void testCreateNumber_Double() {
 String str = "123.45";
 Number result = TypeHandler.createNumber(str);
 assertEquals(123.45, result.doubleValue(), 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumber_InvalidLong() {
 String str = "abc";
 Number result = TypeHandler.createNumber(str);
 fail("Expected NumberFormatException but got: " + result);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumber_InvalidDouble() {
 String str = "123.abc";
 Number result = TypeHandler.createNumber(str);
 fail("Expected NumberFormatException but got: " + result);
 }

 @Test
 public void testCreateDate_ValidDate() {
 String str = "2022-03-01";
 Date result = TypeHandler.createDate(str);
 assertEquals("Wed Mar 01 00:00:00 GMT 2022", result.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDate_InvalidFormat() {
 String str = "2022-03-01T12:34:56Z"; // Invalid format for Date parsing
 Date result = TypeHandler.createDate(str);
 fail("Expected IllegalArgumentException but got: " + result);
 }

 @Test
 public void testCreateURL_ValidURL() {
 String str = "http://www.example.com";
 URL result = TypeHandler.createURL(str);
 assertEquals("http://www.example.com", result.toString());
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidURL() {
 String str = "http://www.example.com:port/path/"; // Missing protocol
 URL result = TypeHandler.createURL(str);
 fail("Expected MalformedURLException but got: " + result);
 }

 @Test
 public void testCreateFile_ValidFile() {
 String filePath = "/path/to/file.txt";
 File result = TypeHandler.createFile(filePath);
 assertTrue(result.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_InvalidFile() {
 String filePath = "/path/to/nonexistentfile.txt";
 File result = TypeHandler.createFile(filePath);
 fail("Expected FileNotFoundException but got: " + result);
 }

 @Test
 public void testCreateFiles_ValidFiles() {
 String filePath = "/path/to/file1.txt,/path/to/file2.txt";
 File[] result = TypeHandler.createFiles(filePath);
 assertEquals(2, result.length);
 for (File file : result) {
 assertTrue(file.exists());
 }
 }
}