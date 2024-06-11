import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object obj = new Object() {
        };
        String str = "test";
        Object actual = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(str, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringWrongClass() throws ParseException {
        String str = "test";
        TypeHandler.createValue(str, String[].class);
    }

    @Test
    public void testCreateValueStringString() throws ParseException {
        String expected = "test";
        String str = "test";
        String actual = (String) TypeHandler.createValue(str, String.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException {
        String expected = "test";
        String str = "test";
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException, InstantiationException, IllegalAccessException {
        String expectedClassName = "java.lang.String";
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        Assert.assertNotNull(obj);
        Assert.assertEquals(expectedClassName, obj.getClass().getName());
        Object actual = TypeHandler.createValue(classname, String.class);
        Assert.assertSame(obj, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderWrongClass() throws ParseException {
        String str = "test";
        Object actual = TypeHandler.createValue(str, int.class);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() throws ParseException {
        String str = "123";
        Number expected = 123;
        Number actual = (Number) TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(expected.longValue(), actual.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderNumberValueInvalid() throws ParseException {
        String str = "test";
        TypeHandler.createValue(str, Number.class);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = "2022-12-31";
        Date expected = format.parse(str);
        Date actual = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringPatternOptionBuilderDateValueUnsupported() throws ParseException {
        String str = "test";
        TypeHandler.createValue(str, Date.class);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
        String expectedClassName = "java.lang.String";
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object actual = TypeHandler.createValue(classname, clazz);
        Assert.assertSame(clazz, actual.getClass());
        Assert.assertEquals(expectedClassName, actual.getClass().getName());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() throws FileNotFoundException {
        String str = "test.txt";
        File file = new File(str);
        FileInputStream fileInputStream = new FileInputStream(file);
        File actual = (File) TypeHandler.createValue(str, File.class);
        Assert.assertEquals(file, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderFileValueNotFound() throws FileNotFoundException {
        String str = "test.txt";
        TypeHandler.createValue(str, File.class);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() throws FileNotFoundException {
        String str = "test.txt";
        File file = new File(str);
        Assert.assertTrue(file.exists());
        File actual = (File) TypeHandler.openFile(str);
        Assert.assertEquals(file, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderExistingFileValueNotFound() throws FileNotFoundException {
        String str = "test.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String[] files = new String[]{"test1.txt", "test2.txt"};
        File[] expected = new File[2];
        expected[0] = new File("test1.txt");
        expected[1] = new File("test2.txt");
        File[] actual = (File[]) TypeHandler.createValues(String.join(":", files), File[].class);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() throws ParseException {
        String str = "http://test.com";
        URL expected = new URL(str);
        URL actual = (URL) TypeHandler.createValue(str, URL.class);
        Assert.assertEquals(expected, actual);
    }
}