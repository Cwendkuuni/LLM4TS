import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "test";
 Object obj = String.class;
 Object result = TypeHandler.createValue(str, obj);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "test";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValue_UnknownType() {
 String str = "test";
 Object obj = Integer.class;
 Object result = TypeHandler.createValue(str, obj);
 assertNull(result);
 }

 @Test
 public void testCreateObject() {
 String className = "java.util.Date";
 Object result = TypeHandler.createObject(className);
 assertNotNull(result);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_UnknownClass() {
 String className = "UnknownClass";
 Object result = TypeHandler.createObject(className);
 assertNull(result);
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number result = (Number) TypeHandler.createValue(str, Number.class);
 assertEquals(123, result.intValue());

 String str2 = "123.45";
 Number result2 = (Number) TypeHandler.createValue(str2, Number.class);
 assertEquals(123.45, result2.doubleValue(), 0.001);
 }

 @Test
 public void testCreateDate() {
 String dateString = "2023-03-31";
 Date result = (Date) TypeHandler.createValue(dateString, Date.class);
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDate_InvalidFormat() {
 String dateString = "2023-35-31";
 TypeHandler.createValue(dateString, Date.class);
 }

 @Test
 public void testCreateURL() {
 String urlString = "http://example.com";
 URL result = (URL) TypeHandler.createValue(urlString, URL.class);
 assertNotNull(result);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidFormat() {
 String urlString = "http:example.com";
 TypeHandler.createValue(urlString, URL.class);
 }

 @Test
 public void testCreateFile() {
 String filePath = "/test/TypeHandlerTest.java";
 File result = (File) TypeHandler.createValue(filePath, File.class);
 assertNotNull(result);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_NonExistentFile() {
 String filePath = "/test/nonExistentFile.txt";
 TypeHandler.createValue(filePath, File.class);
 }

}