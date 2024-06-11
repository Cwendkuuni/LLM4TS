import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.Class;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        Object stringObject = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", stringObject);
    }

    @Test
    public void testCreateValue_StringClass() {
        Class clazz = String.class;
        Object object = TypeHandler.createValue("test", clazz);
        Assert.assertEquals("test", object);

        clazz = File.class;
        File file = TypeHandler.createValue("/test/file.txt", clazz);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());

        clazz = Date.class;
        Date date = TypeHandler.createValue("2023-03-12", clazz);
        Assert.assertNotNull(date);

        clazz = Integer.class;
        Number number = TypeHandler.createValue("123", clazz);
        Assert.assertEquals((Integer) 123, number);

        clazz = Double.class;
        number = TypeHandler.createValue("3.1415926535897932384626433832795", clazz);
        Assert.assertEquals((Double) 3.1415926535897932384626433832795, number);

        clazz = URL.class;
        try {
            URL url = TypeHandler.createValue("http://www.example.com", clazz);
            Assert.assertNotNull(url);
            Assert.assertEquals("http://www.example.com", url.toExternalForm());
        } catch (MalformedURLException e) {
            Assert.fail("MalformedURLException expected, but got: " + e.getMessage());
        }

        clazz = File[].class;
        String[] filePaths = new String[]{"/test/file1.txt", "/test/file2.txt"};
        File[] files = TypeHandler.createValue(StringUtils.join(filePaths, File.pathSeparator), clazz);
        Assert.assertArrayEquals(new File[]{new File("/test/file1.txt"), new File("/test/file2.txt")}, files);

        clazz = Class.class;
        try {
            Class testClass = ClassUtils.forName("java.lang.String", null);
            Object instance = TypeHandler.createValue(testClass.getName(), clazz);
            Assert.assertNotNull(instance);
            Assert.assertTrue(instance instanceof Class);
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException expected, but got: " + e.getMessage());
        }

        clazz = Object.class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = Number.class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = Date.class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = URL.class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = File.class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = File[].class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));

        clazz = Class[].class;
        Assert.assertNull(TypeHandler.createValue("test", clazz));
    }
}