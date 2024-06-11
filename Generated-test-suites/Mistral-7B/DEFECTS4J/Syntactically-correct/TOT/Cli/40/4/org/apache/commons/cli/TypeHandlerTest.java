import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringClass() throws ParseException
    {
        String str = "hello";
        Class<String> clazz = String.class;

        String expected = "hello";
        String result = TypeHandler.createValue(str, clazz).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringStringClass() throws ParseException
    {
        String str = "hello";
        Class<String> clazz = String.class;

        String expected = "hello";
        String result = TypeHandler.createValue(str, TypeHandler.PatternOptionBuilder.STRING_VALUE).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringObjectClass() throws ParseException
    {
        String str = "hello";
        Class<Object> clazz = Object.class;

        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValue_StringClassOfString() throws ParseException
    {
        String str = "hello";
        Class<String> clazz = String.class;

        String expected = "hello";
        String result = TypeHandler.createValue(str, clazz).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringClassOfObject() throws ParseException
    {
        String str = "HelloWorld";
        Class<Object> clazz = Object.class;

        String expected = "HelloWorld";
        String result = TypeHandler.createValue(str, TypeHandler.PatternOptionBuilder.OBJECT_VALUE).getClass().getSimpleName();

        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidStringObject() throws ParseException
    {
        String str = "123";
        Class<Object> clazz = Integer.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringClassOfLong() throws ParseException
    {
        String str = "123";
        Class<Long> clazz = Long.class;

        Long expected = Long.parseLong("123");
        Long result = TypeHandler.createValue(str, clazz);

        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidStringDate() throws ParseException
    {
        String str = "2022-01-31";
        Class<Date> clazz = Date.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringClassOfDate() throws ParseException
    {
        String str = "2022-02-01";
        Class<Date> clazz = Date.class;

        Date expected = new Date(2022, 1, 1);
        Date result = TypeHandler.createValue(str, clazz);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringClassOfClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<Class<?>> clazz = Class.class;
        Class<?> expectedClass = Class.forName(classname);

        Class<?> resultClass = TypeHandler.createValue(classname, clazz).asSubclass(Date.class);

        assertEquals(expectedClass, resultClass);
    }

    @Test
    public void testCreateValue_StringClassOfFile() throws ParseException, IOException
    {
        String str = "test.txt";
        Class<File> clazz = File.class;

        File expected = new File("test.txt");
        File result = TypeHandler.createValue(str, clazz);

        assertEquals(expected, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistentFile() throws ParseException, FileNotFoundException
    {
        String str = "test.txt";
        Class<FileInputStream> clazz = FileInputStream.class;

        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFile_ExistentFile() throws ParseException, FileNotFoundException, IOException
    {
        String str = "test.txt";
        File testFile = new File(str);

        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream expected = new FileInputStream(testFile);
        FileInputStream result = TypeHandler.openFile(str);

        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidStringURL() throws ParseException
    {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_StringClassOfURL() throws ParseException
    {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;

        URL expected = new URL("http://example.com");
        URL result = TypeHandler.createValue(str, clazz);

        assertEquals(expected, result);
    }
}