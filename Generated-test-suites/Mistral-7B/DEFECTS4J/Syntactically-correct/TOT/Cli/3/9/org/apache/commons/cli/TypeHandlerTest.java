import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "abc";
 Object obj = new String("def");
 Object result = TypeHandler.createValue(str, obj.getClass());
 assertEquals("abc", result);
 }

 @Test
 public void testCreateValue_StringClassString() {
 String str = "123";
 Class clazz = Integer.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals((Integer) 123, result);
 }

 @Test
 public void testCreateValue_StringClass_InvalidNumberFormat() {
 String str = "abc";
 Class clazz = Integer.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateValue_StringClass_InvalidClass() {
 String str = "java.util.Date";
 Class clazz = Integer.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertNull(result);
 }

 @Test
 public void testCreateValue_StringClass_File() {
 String str = "/tmp/testFile.txt";
 Class clazz = File.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(new File(str), result);
 }

 @Test
 public void testCreateValue_StringClass_URL() {
 String str = "http://example.com";
 Class clazz = URL.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(new URL(str), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateValue_StringClass_InvalidOptionType() {
 String str = "com.example.MyClass";
 Class clazz = PatternOptionBuilder.STRING_VALUE.getClass();
 Object result = TypeHandler.createValue(str, clazz);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreateObject() {
 String className = "java.util.Date";
 Object result = TypeHandler.createObject(className);
 assertTrue(result instanceof Date);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_InvalidClassName() {
 String className = "com.example.InvalidClass";
 Object result = TypeHandler.createObject(className);
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testCreateNumber() {
 String str = "123";
 Number result = TypeHandler.createNumber(str);
 assertEquals((Integer) 123, result);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumber_InvalidNumberFormat() {
 String str = "abc";
 Number result = TypeHandler.createNumber(str);
 fail("Expected NumberFormatException");
 }

 @Test
 public void testCreateDate() {
 String str = "2022-01-01";
 Date result = TypeHandler.createDate(str);
 assertEquals(new Date(0), result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDate_InvalidDateFormat() {
 String str = "2022-01-33";
 Date result = TypeHandler.createDate(str);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreateURL() {
 String str = "http://example.com";
 URL result = TypeHandler.createURL(str);
 assertEquals(new URL(str), result);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidURLFormat() {
 String str = "http://example.com/invalid";
 URL result = TypeHandler.createURL(str);
 fail("Expected MalformedURLException");
 }

 @Test
 public void testCreateFile() {
 String path = "/tmp/testFile.txt";
 File result = TypeHandler.createFile(path);
 assertTrue(new File(path).exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_InvalidPath() {
 String path = "/tmp/testFileThatDoesNotExist.txt";
 File result = TypeHandler.createFile(path);
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testCreateFiles() {
 String path = "/tmp/testFiles/";
 String[] files = {"testFile1.txt", "testFile2.txt"};
 File[] result = TypeHandler.createFiles(path + File.separator + "*.txt");
 assertEquals(files.length, result.length);
 for (int i = 0; i < files.length; i++) {
 assertEquals(new File(path + File.separator + files[i]), result[i]);
 }
 }
}