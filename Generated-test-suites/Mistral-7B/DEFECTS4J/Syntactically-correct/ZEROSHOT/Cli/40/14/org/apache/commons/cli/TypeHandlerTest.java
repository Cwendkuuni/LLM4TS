import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String() throws ParseException {
        String str = "test_string";
        String expected = str;
        Object actual = TypeHandler.createValue(str, String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test_string";
        Object obj = new Object();
        Object actual = TypeHandler.createValue(str, Object.class);
        assertNotSame(obj, actual);
        assertEquals(str.getClass(), actual.getClass());
        String actualStr = (String) actual;
        assertEquals(str, actualStr);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_InvalidClass() throws ParseException {
        String str = "test_string";
        TypeHandler.createValue(str, Integer.class);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_STRING_VALUE() throws ParseException {
        String str = "test_string";
        Object actual = TypeHandler.createValue(str, STRING_VALUE);
        assertEquals(str.getClass(), actual.getClass());
        String actualStr = (String) actual;
        assertEquals(str, actualStr);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_OBJECT_VALUE() throws ParseException {
        String str = "test_object";
        Class<?> clazz = String.class; // This test will fail as the TypeHandler does not support creating an object from a String and a Class directly.
        Object actual = TypeHandler.createValue(str, clazz);
        fail("TypeHandler.createValue(String, Class<T>) should throw a ParseException.");
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_NUMBER_VALUE() throws ParseException {
        String str = "123";
        Number expected = 123;
        Number actual = TypeHandler.createValue(str, Number.class);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_DATE_VALUE() throws ParseException {
        String str = "2023-03-26";
        Date expected = new Date(System.currentTimeMillis());
        Date actual = TypeHandler.createValue(str, Date.class);
        assertNotSame(expected, actual);
        assertEquals(str.getClass(), actual.getClass());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date actualDate = dateFormat.parse(str);
        assertEquals(actualDate, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_PatternOptionBuilder_DATE_VALUE_Unsupported() throws ParseException, UnsupportedOperationException {
        String str = "2023-03-26";
        TypeHandler.createValue(str, Date.class);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_CLASS_VALUE() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String className = "java.util.Date";
        Class<?> expectedClass = Class.forName(className);
        Object actual = TypeHandler.createValue(className, Object.class);
        assertEquals(expectedClass, actual.getClass());
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_FILE_VALUE() throws ParseException, FileNotFoundException {
        String filePath = "test.txt";
        File expected = new File(filePath);
        File actual = TypeHandler.createFile(filePath);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_PatternOptionBuilder_EXISTING_FILE_VALUE() throws ParseException, FileNotFoundException {
        String filePath = "nonexistent_file.txt";
        TypeHandler.openFile(filePath);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_URL_VALUE() throws ParseException, MalformedURLException {
        String urlString = "https://www.example.com";
        URL expected = new URL(urlString);
        URL actual = TypeHandler.createURL(urlString);
        assertEquals(expected.toString(), actual.toString());
    }
}