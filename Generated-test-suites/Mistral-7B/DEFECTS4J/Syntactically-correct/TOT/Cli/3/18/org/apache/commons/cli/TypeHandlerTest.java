import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

 @Test
 public void testCreateValue_StringObject() {
 String str = "abcd";
 Object obj = String.class;
 Object result = TypeHandler.createValue(str, obj);
 assertEquals("abcd", result);
 }

 @Test
 public void testCreateValue_StringClass() {
 String str = "abcd";
 Class clazz = String.class;
 Object result = TypeHandler.createValue(str, clazz);
 assertEquals("abcd", result);
 }

 @Test
 public void testCreateValue_StringPatternOptionClass() {
 String str = "123";
 Class clazz = PatternOptionBuilder.NUMBER_VALUE;
 Number result = (Number) TypeHandler.createValue(str, clazz);
 assertEquals(123, result.intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateValue_NullString() {
 String str = null;
 Class clazz = PatternOptionBuilder.NUMBER_VALUE;
 Object result = TypeHandler.createValue(str, clazz);
 fail("Expected NullPointerException");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateObject_NonExistingClass() {
 String className = "NonExistingClass";
 Object result = TypeHandler.createObject(className);
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testCreateObject_ExistingClass() {
 String className = "java.lang.String";
 Object result = TypeHandler.createObject(className);
 assertNotNull(result);
 }

 @Test
 public void testCreateNumber_ValidNumber() {
 String str = "123.45";
 Number result = (Number) TypeHandler.createNumber(str);
 assertEquals(123.45, result.doubleValue(), 0.001);
 }

 @Test(expected = NumberFormatException.class)
 public void testCreateNumber_InvalidNumber() {
 String str = "abcd";
 Number result = (Number) TypeHandler.createNumber(str);
 fail("Expected NumberFormatException");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testCreateClass_NonExistingClass() {
 String className = "NonExistingClass";
 Class result = (Class) TypeHandler.createClass(className);
 fail("Expected ClassNotFoundException");
 }

 @Test
 public void testCreateClass_ExistingClass() {
 String className = "java.lang.String";
 Class result = (Class) TypeHandler.createClass(className);
 assertEquals(String.class, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateDate_InvalidDateFormat() {
 String dateString = "23/12/2022";
 Date result = TypeHandler.createDate(dateString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreateDate_ValidDateFormat() {
 String dateString = "23/12/2022";
 Date result = TypeHandler.createDate(dateString);
 assertNotNull(result);
 }

 @Test(expected = MalformedURLException.class)
 public void testCreateURL_InvalidURL() {
 String urlString = "http://example.com/a/b/c/../d";
 URL result = TypeHandler.createURL(urlString);
 fail("Expected MalformedURLException");
 }

 @Test
 public void testCreateURL_ValidURL() {
 String urlString = "http://example.com/";
 URL result = TypeHandler.createURL(urlString);
 assertNotNull(result);
 }

 @Test
 public void testCreateFile_ValidFile() {
 String filePath = "src/test/resources/test.txt";
 File result = TypeHandler.createFile(filePath);
 assertNotNull(result);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFile_InvalidFile() {
 String filePath = "src/test/resources/non-existent.txt";
 File result = TypeHandler.createFile(filePath);
 fail("Expected FileNotFoundException");
 }

 @Test
 public void testCreateFiles_ValidFiles() {
 String[] filePaths = new String[] {"src/test/resources/file1.txt", "src/test/resources/file2.txt"};
 File[] result = TypeHandler.createFiles(String.join(File.pathSeparator, filePaths));
 assertNotNull(result);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCreateFiles_InvalidFilePath() {
 String[] filePaths = new String[] {"src/test/resources/non-existent.txt"};
 File[] result = TypeHandler.createFiles(String.join(File.pathSeparator, filePaths));
 fail("Expected FileNotFoundException");
 }
}