import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.CommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "hello";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "123";
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123, result);

        str = "456.78";
        clazz = Double.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(456.78, result);

        str = "true";
        clazz = Boolean.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(true, result);

        str = "java.lang.String";
        clazz = String.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("java.lang.String", result);

        str = "org.apache.commons.cli.TypeHandler";
        clazz = TypeHandler.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);

        str = "com.nonexistent.package.NonexistentClass";
        clazz = NonexistentClass.class; // assume NonexistentClass is a nonexistent class
        result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.util.Date";
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = (Number) TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(123, result.intValue());

        str = "456";
        result = (Number) TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(456, result.intValue());

        str = "789.123";
        result = (Number) TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(789, result.intValue());
        Assert.assertEquals(123, result.floatValue());

        str = "0.123";
        result = (Number) TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(0, result.intValue());
        Assert.assertEquals(0.123, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-12-31";
        Date result = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(result);

        str = "2022-12-31 23:59:59";
        result = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(result);

        str = "2022-12-31 23:59:59.123";
        result = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(result);

        str = "invalid date";
        result = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL result = (URL) TypeHandler.createValue(urlString, URL.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("http", result.getProtocol());
        Assert.assertEquals("/example.com", result.getPath());

        urlString = "https://example.com:8080/path/to/file.html?query=foo";
        result = (URL) TypeHandler.createValue(urlString, URL.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("https", result.getProtocol());
        Assert.assertEquals("/example.com:8080/path/to/file.html", result.getPath());
        Assert.assertEquals("query=foo", result.getQuery());

        urlString = "invalid url";
        result = (URL) TypeHandler.createValue(urlString, URL.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFile() {
        String filePath = "path/to/file.txt";
        File result = (File) TypeHandler.createValue(filePath, File.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());

        filePath = "path/to/nonexistent/file.txt";
        result = (File) TypeHandler.createValue(filePath, File.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "path/to/file1.txt,path/to/file2.txt";
        File[] results = (File[]) TypeHandler.createValue(filePaths, File[].class);
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.length);
        for (File file : results) {
            Assert.assertNotNull(file);
        }

        filePaths = "path/to/nonexistent/file1.txt,path/to/nonexistent/file2.txt";
        results = (File[]) TypeHandler.createValue(filePaths, File[].class);
        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.length);
    }
}