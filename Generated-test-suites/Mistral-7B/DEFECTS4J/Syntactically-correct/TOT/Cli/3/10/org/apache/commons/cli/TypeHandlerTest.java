import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "hello";
 Object obj = String.class;
 Object actual = TypeHandler.createValue(str, obj);
 assertSame(str, actual);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "123";
 Class clazz = Integer.class;
 Object actual = TypeHandler.createValue(str, clazz);
 assertEquals(123, actual);
 }

 @Test
 public void testCreateValue_StringClassInvalid() {
 String str = "abcd";
 Class clazz = Integer.class;
 Object actual = TypeHandler.createValue(str, clazz);
 assertNull(actual);
 }

 @Test
 public void testCreateObject_ValidClassName() {
 String className = "java.util.Date";
 Object actual = TypeHandler.createValue(className, Date.class);
 assertInstanceOf(Date.class, actual);
 }

 @Test
 public void testCreateObject_InvalidClassName() {
 String className = "InvalidClass";
 Object actual = TypeHandler.createValue(className, Date.class);
 assertNull(actual);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_ClassNotFoundException() {
 String className = "java.util.InvalidClass";
 Object actual = TypeHandler.createValue(className, Date.class);
 assertNull(actual);
 }

 @Test
 public void testCreateNumber_ValidNumber() {
 String str = "123";
 Object actual = TypeHandler.createValue(str, int.class);
 assertEquals(123, actual);
 }

 @Test
 public void testCreateNumber_InvalidNumber() {
 String str = "abcd";
 Object actual = TypeHandler.createValue(str, int.class);
 assertNull(actual);
 }

 @Test
 public void testCreateDate_ValidDateString() {
 String str = "2022-03-01";
 Object actual = TypeHandler.createValue(str, Date.class);
 assertInstanceOf(Date.class, actual);
 }

 @Test
 public void testCreateDate_InvalidDateString() {
 String str = "2022-33-01";
 Object actual = TypeHandler.createValue(str, Date.class);
 assertNull(actual);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidURLString() {
 String str = "http://example.com/invalid";
 Object actual = TypeHandler.createValue(str, URL.class);
 assertNull(actual);
 }

 @Test
 public void testCreateURL_ValidURLString() {
 String str = "http://example.com";
 Object actual = TypeHandler.createValue(str, URL.class);
 assertInstanceOf(URL.class, actual);
 }

 @Test
 public void testCreateFile_ValidFilePath() {
 String str = "/tmp/test.txt";
 File expected = new File(str);
 File actual = (File) TypeHandler.createValue(str, File.class);
 assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_InvalidFilePath() {
 String str = "/nonexistentfile.txt";
 File actual = (File) TypeHandler.createValue(str, File.class);
 assertNull(actual);
 }

 @Test
 public void testCreateFiles_ValidFilePathArray() {
 String[] paths = {"/tmp/test1.txt", "/tmp/test2.txt"};
 File[] expected = new File[2];
 expected[0] = new File(paths[0]);
 expected[1] = new File(paths[1]);
 File[] actual = (File[]) TypeHandler.createValue(String.join(" ", paths), File[].class);
 assertArrayEquals(expected, actual);
 }
}