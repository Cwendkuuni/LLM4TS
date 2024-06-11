import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_StringNullObject() {
        String str = "test";
        Object obj = null;
        Object result = TypeHandler.createValue(str, obj);
        fail("Expected NullPointerException, but received: " + result);
    }

    @Test
    public void testCreateValue_StringStringClass() {
        String str = "java.lang.String";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(ClassUtils.forName(str), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringInvalidClass() {
        String str = "java.lang.Integer";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        fail("Expected IllegalArgumentException, but received: " + result);
    }

    @Test
    public void testCreateValue_StringPatternOptionClass() {
        String str = "abcdef";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringObjectValueClass() {
        String str = "java.lang.Object";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_StringNumberClass() {
        String str = "123";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof Number);
        Number numberResult = (Number) result;
        assertEquals(123, numberResult.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateValue_StringInvalidNumberClass() {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected NumberFormatException, but received: " + result);
    }

    @Test
    public void testCreateValue_StringDateClass() {
        String str = "2023-03-25";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof Date);
        Date dateResult = (Date) result;
        assertEquals("Sun Mar 26 00:00:00 GMT+2 2023", dateResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringInvalidDateClass() {
        String str = "2023-03-25";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected IllegalArgumentException, but received: " + result);
    }

    @Test
    public void testCreateValue_StringClassValueClass() {
        String str = "java.lang.String.class";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(ClassUtils.forName(str), result);
    }

    @Test
    public void testCreateValue_StringFileValueClass() {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof File);
        File fileResult = (File) result;
        assertEquals("test.txt", fileResult.getAbsolutePath());
    }

    @Test
    public void testCreateValue_StringExistingFileValueClass() {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof File);
        File fileResult = (File) result;
        assertEquals("test.txt", fileResult.getAbsolutePath());
        assertTrue(fileResult.exists());
    }

    @Test
    public void testCreateValue_StringFilesValueClass() {
        String str = "test,test2.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof File[]);
        File[] filesResult = (File[]) result;
        assertEquals(2, filesResult.length);
        for (File file : filesResult) {
            assertTrue(file.exists());
        }
    }

    @Test
    public void testCreateValue_StringUrlValueClass() {
        String str = "http://example.com";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof URL);
        URL urlResult = (URL) result;
        assertEquals("http://example.com", urlResult.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValue_StringInvalidUrlValueClass() {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected MalformedURLException, but received: " + result);
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("123");
        assertTrue(result instanceof Integer);
        assertEquals(123, result.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        Number result = TypeHandler.createNumber("test");
        fail("Expected NumberFormatException, but received: " + result);
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2023-03-25");
        assertEquals("Sun Mar 26 00:00:00 GMT+2 2023", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidDateFormat() {
        Date result = TypeHandler.createDate("test");
        fail("Expected IllegalArgumentException, but received: " + result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NotFound() {
        File result = TypeHandler.createFile("non_existent_file.txt");
        fail("Expected FileNotFoundException, but received: " + result);
    }

    @Test
    public void testCreateFiles() {
        String[] files = {"test1.txt", "test2.txt"};
        File[] result = TypeHandler.createFiles(String.join(",", files));
        assertEquals(2, result.length);
        for (File file : result) {
            assertTrue(file.exists());
        }
    }

    @Test(expected = IOException.class)
    public void testCreateFiles_IOException() {
        String[] files = {"test1.txt", "test2.txt"};
        try {
            File[] result = TypeHandler.createFiles(String.join(",", files));
            fail("Expected IOException, but received: " + result);
        } catch (IOException ioe) {
            // Expected
        }
    }

    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("http://example.com");
        assertEquals("http://example.com", result.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        URL result = TypeHandler.createURL("test");
        fail("Expected MalformedURLException, but received: " + result);
    }
}