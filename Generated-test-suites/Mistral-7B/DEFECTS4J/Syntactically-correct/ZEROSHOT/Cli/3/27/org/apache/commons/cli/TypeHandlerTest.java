import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "hello";
        Object obj = new Object() {};
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertSame(obj, result);
        Assert.assertEquals("hello", result.toString());
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "hello";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertSame(str, result);
    }

    @Test
    public void testCreateValue_InvalidType() {
        String str = "hello";
        Class clazz = int.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String str = "com.example.MyClass";
        Class clazz = null;
        try {
            clazz = Class.forName(str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object result = TypeHandler.createObject(str);
        Assert.assertSame(clazz, result.getClass());
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(123, result.intValue());

        str = "456.78";
        result = TypeHandler.createNumber(str);
        Assert.assertEquals(456.78, result.doubleValue());

        str = "abc";
        result = TypeHandler.createNumber(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateClass() {
        String str = "java.util.Date";
        Class expectedClass = Date.class;
        Class result = TypeHandler.createClass(str);
        Assert.assertSame(expectedClass, result);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-03-12";
        Date expectedDate = new Date(2022, 2, 12); // March is 0-indexed in Date constructor
        Date result = TypeHandler.createDate(str);
        Assert.assertSame(expectedDate, result);

        str = "InvalidDate";
        result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String str = "http://example.com";
        URL expectedURL = new URL("http", "example.com", 80, "/path");
        URL result = TypeHandler.createURL(str);
        Assert.assertSame(expectedURL.getProtocol(), result.getProtocol());
        Assert.assertSame(expectedURL.getHost(), result.getHost());
        Assert.assertSame(expectedURL.getPort(), result.getPort());
        Assert.assertSame(expectedURL.getPath(), result.getPath());

        str = "InvalidURL";
        result = TypeHandler.createURL(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File expectedFile = new File(str);
        File result = TypeHandler.createFile(str);
        Assert.assertSame(expectedFile.getAbsolutePath(), result.getAbsolutePath());

        str = "InvalidFile";
        result = TypeHandler.createFile(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        String[] expectedFiles = new String[]{"/path/to/file1.txt", "/path/to/file2.txt"};
        File[] result = TypeHandler.createFiles(str);
        Assert.assertArrayEquals(expectedFiles, result);

        str = "InvalidFiles";
        result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}