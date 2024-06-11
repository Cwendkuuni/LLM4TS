import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateStringValue() throws ParseException
    {
        String str = "testString";
        String expected = "testString";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateObjectValueNull() throws ParseException
    {
        String classname = null;
        Class<?> clazz = Object.class;
        Object actual = TypeHandler.createObject(classname);
        assertNull(actual);
    }

    @Test
    public void testCreateObjectValueEmpty() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object actual = TypeHandler.createObject(classname);
        String expected = "";
        assertEquals(expected, actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectValueInvalidClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.Integer";
        Class<?> clazz = String.class;
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberValueLong() throws ParseException
    {
        String str = "123";
        Long expected = 123L;
        Object actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberValueDouble() throws ParseException
    {
        String str = "123.45";
        Double expected = 123.45;
        Object actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueInvalid() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClassValueNull() throws ParseException
    {
        String classname = null;
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createClass(classname);
        assertNull(actual);
    }

    @Test
    public void testCreateClassValueEmpty() throws ParseException, ClassNotFoundException
    {
        String classname = "";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createClass(classname);
        assertNull(actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassValueInvalidClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.Integer";
        Class<?> clazz = String.class;
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValue() throws ParseException
    {
        String str = "2022-03-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLValue() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        URL expected = new URL("http://www.example.com");
        Object actual = TypeHandler.createURL(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLValueInvalid() throws ParseException
    {
        String str = "http://www.example.com/abc";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFileValue()
    {
        String str = "path/to/file.txt";
        File expected = new File(str);
        Object actual = TypeHandler.createFile(str);
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileValueNotFound() throws ParseException, FileNotFoundException
    {
        String str = "nonExistentFile.txt";
        TypeHandler.openFile(str);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileValueInvalid() throws ParseException
    {
        String str = "path/to/nonExistentDirectory/";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFilesValue() throws ParseException
    {
        String str = "path/to/file1.txt, path/to/file2.txt";
        File[] expected = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
        Object actual = TypeHandler.createFiles(str);
        assertArrayEquals(expected, (File[]) actual);
    }
}