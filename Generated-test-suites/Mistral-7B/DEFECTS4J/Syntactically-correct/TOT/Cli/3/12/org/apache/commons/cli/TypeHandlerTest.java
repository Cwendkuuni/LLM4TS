import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testStringValue() {
 String str = "test";
 Object obj = TypeHandler.createValue(str, String.class);
 assertEquals(str, obj);
 }

 @Test
 public void testObjectValueInvalidClass() {
 String str = "java.lang.String";
 Object obj = TypeHandler.createValue(str, int.class);
 assertNull(obj);
 }

 @Test
 public void testObjectValueValidClass() {
 String clazzName = "java.lang.String";
 Object obj = TypeHandler.createValue(clazzName, String.class);
 assertEquals(clazzName, obj.getClass().getName());
 }

 @Test
 public void testNumberValueLong() {
 String str = "123";
 Object obj = TypeHandler.createValue(str, long.class);
 assertEquals(123L, obj);
 }

 @Test
 public void testNumberValueDouble() {
 String str = "3.14";
 Object obj = TypeHandler.createValue(str, double.class);
 assertEquals(3.14, obj, 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testNumberValueInvalidNumber() {
 String str = "abc";
 Object obj = TypeHandler.createValue(str, int.class);
 fail("Expected NumberFormatException but got: " + obj);
 }

 @Test
 public void testDateValueValidDateFormat() {
 String str = "2022-01-01";
 Object obj = TypeHandler.createValue(str, Date.class);
 assertNotNull(obj);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDateValueInvalidFormat() {
 String str = "Jan-01-2022";
 Object obj = TypeHandler.createValue(str, Date.class);
 fail("Expected IllegalArgumentException but got: " + obj);
 }

 @Test
 public void testClassNameValidClass() {
 String clazzName = "java.lang.String";
 Object obj = TypeHandler.createValue(clazzName, Class.class);
 assertEquals(String.class, obj);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testClassNameInvalidClass() {
 String clazzName = "java.lang.NonExistentClass";
 Object obj = TypeHandler.createValue(clazzName, Class.class);
 fail("Expected ClassNotFoundException but got: " + obj);
 }

 @Test
 public void testFileValueValidPath() {
 String path = "/path/to/file.txt";
 Object obj = TypeHandler.createValue(path, File.class);
 assertEquals(new File(path), obj);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileValueInvalidPath() {
 String path = "/path/to/non-existent-file.txt";
 Object obj = TypeHandler.createValue(path, File.class);
 fail("Expected FileNotFoundException but got: " + obj);
 }

 @Test
 public void testURLValueValidURL() {
 String urlString = "http://example.com";
 Object obj = TypeHandler.createValue(urlString, URL.class);
 assertNotNull(obj);
 }

 @Test(expected = MalformedURLException.class)
 public void testURLValueInvalidURL() {
 String urlString = "http:example.com";
 Object obj = TypeHandler.createValue(urlString, URL.class);
 fail("Expected MalformedURLException but got: " + obj);
 }

 @Test
 public void testObjectFromClass() {
 String className = "java.lang.String";
 Object obj = TypeHandler.createValue(className, Object.class);
 assertNotNull(obj);
 }

 @Test
 public void testObjectFromUnknownClass() {
 String className = "com.mypackage.UnknownClass";
 Object obj = TypeHandler.createValue(className, Object.class);
 assertNull(obj);
 }
}