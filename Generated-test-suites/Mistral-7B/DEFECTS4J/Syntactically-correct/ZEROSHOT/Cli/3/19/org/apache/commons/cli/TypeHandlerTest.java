import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "hello";
 Object obj = String.class;
 Object result = TypeHandler.createValue(str, obj);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "hello";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test
 public void testCreateValue_StringPatternOptionBuilder_STRING_VALUE() {
 String str = "hello";
 Class clazz = PatternOptionBuilder.STRING_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(str, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateValue_StringClass_NullClass() {
 String str = "hello";
 Class clazz = null;
 Object result = TypeHandler.createValue(str, clazz);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testCreateValue_StringClass_ObjectValue() {
 String str = "java.lang.Integer";
 Class clazz = PatternOptionBuilder.OBJECT_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(Integer.class, result.getClass());
 }

 @Test
 public void testCreateValue_StringClass_NumberValue() {
 String str = "3.14";
 Class clazz = PatternOptionBuilder.NUMBER_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(Double.class, result.getClass());
 }

 @Test
 public void testCreateValue_StringClass_DateValue() {
 String str = "2022-01-01";
 Class clazz = PatternOptionBuilder.DATE_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(Date.class, result.getClass());
 }

 @Test
 public void testCreateValue_StringClass_ClassValue() {
 String str = "java.lang.String";
 Class clazz = PatternOptionBuilder.CLASS_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(String.class, result.getClass());
 }

 @Test
 public void testCreateValue_StringClass_FileValue() {
 String str = "path/to/file.txt";
 Class clazz = PatternOptionBuilder.FILE_VALUE;
 File expected = new File(str);
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateValue_StringClass_ExistingFileValue() {
 String str = "path/to/file.txt";
 Class clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
 File expected = new File(str);
 boolean exists = expected.exists();
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(expected, result);
 assertTrue(exists);
 }

 @Test
 public void testCreateValue_StringClass_URLValue() {
 String str = "http://example.com";
 Class clazz = PatternOptionBuilder.URL_VALUE;
 URL expected = new URL(str);
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateValue_StringClass_FilesValue() {
 String str = "path/to/file1.txt,path/to/file2.txt";
 Class clazz = PatternOptionBuilder.FILES_VALUE;
 File[] expected = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
 Object result = TypeHandler.createValue(str, clazz);
 assertArrayEquals(expected, (File[]) result);
 }

 @Test
 public void testCreateObject() {
 String className = "java.lang.String";
 String expected = "hello";
 String str = "org.apache.commons.cli.TypeHandlerTest$TestClass";
 Class<?> clazz = ClassUtils.forName(str);
 Object result = TypeHandler.createObject(className);
 assertEquals(expected, result.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateObject_NullClassName() {
 String className = null;
 Object result = TypeHandler.createObject(className);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testCreateNumber() {
 String str = "3";
 Number expected = 3;
 Number result = (Number) TypeHandler.createValue(str, Number.class);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateNumber_InvalidNumber() {
 String str = "invalid";
 Number expected = null;
 Number result = (Number) TypeHandler.createValue(str, Number.class);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateDate() {
 String str = "2022-01-01";
 Date expected = new Date(0);
 Date result = (Date) TypeHandler.createValue(str, Date.class);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDate_InvalidDateFormat() {
 String str = "invalid_date";
 Date result = (Date) TypeHandler.createValue(str, Date.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testCreateURL() {
 String str = "http://example.com";
 URL expected = new URL(str);
 URL result = (URL) TypeHandler.createValue(str, URL.class);
 assertEquals(expected, result);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidURLFormat() {
 String str = "invalid_url";
 URL result = (URL) TypeHandler.createValue(str, URL.class);
 fail("Should throw MalformedURLException");
 }

 @Test
 public void testCreateFile() {
 String str = "path/to/file.txt";
 File expected = new File(str);
 File result = (File) TypeHandler.createValue(str, File.class);
 assertEquals(expected, result);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_NonExistentFile() {
 String str = "path/to/nonexistent_file.txt";
 File result = (File) TypeHandler.createValue(str, File.class);
 fail("Should throw FileNotFoundException");
 }

 @Test
 public void testCreateFiles() {
 String str = "path/to/file1.txt,path/to/file2.txt";
 File[] expected = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
 File[] result = (File[]) TypeHandler.createValue(str, File[].class);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCreateFiles_InvalidNumberOfFiles() {
 String str = "path/to/file1.txt,path/to/file2.txt,path/to/file3.txt,path/to/file4.txt,path/to/file5.txt,path/to/file6.txt,path/to/file7.txt";
 String[] tokens = str.split(",");
 File[] expected = new File[tokens.length - 1];
 System.arraycopy(tokens, 0, expected, 0, expected.length);
 File[] result = (File[]) TypeHandler.createValue(str, File[].class);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }
}