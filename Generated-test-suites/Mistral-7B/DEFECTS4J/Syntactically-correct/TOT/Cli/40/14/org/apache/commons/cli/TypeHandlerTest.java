import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        Object actual = TypeHandler.createValue("test", String.class);
        assertEquals("test", actual);
    }

    @Test
    public void testCreateValueStringClassObject() throws ParseException
    {
        String className = "java.util.Date";
        Object actual = TypeHandler.createValue("2022-12-31", Class.forName(className));
        assertInstanceOf(Date.class, actual);
        assertEquals("Sat Dec 31 00:00:00 GMT 2022", actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalid() throws ParseException
    {
        String className = "InvalidClass";
        TypeHandler.createValue("test", Class.forName(className));
    }

    @Test
    public void testCreateValueStringClassNull() throws ParseException
    {
        String className = null;
        assertNull(TypeHandler.createValue("test", className));
    }

    @Test
    public void testCreateValueStringClassArrayOfString() throws ParseException
    {
        String[] expected = {"test1", "test2", "test3"};
        Class<?> clazz = String[].class;
        Object actual = TypeHandler.createValue("test1,test2,test3", clazz);
        assertArrayEquals(expected, (String[]) actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassArrayInvalid() throws ParseException
    {
        String[] expected = {"test1", "test2", "test3"};
        Class<?> clazz = Integer[].class;
        TypeHandler.createValue("test1,test2,test3", clazz);
    }

    @Test
    public void testCreateFile()
    {
        File expected = new File("test.txt");
        String path = "test.txt";
        File actual = TypeHandler.createFile(path);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInvalidPath() throws ParseException
    {
        String path = "test:/test.txt";
        TypeHandler.createFile(path);
    }

    @Test
    public void testCreateURL()
    {
        URL expected = new URL("http://test.com/test.html");
        String urlString = "http://test.com/test.html";
        URL actual = TypeHandler.createURL(urlString);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidFormat() throws ParseException
    {
        String urlString = "test.com/test.html";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.util.Date";
        Object expected = new Date();
        Object actual = TypeHandler.createObject(className);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals("Sat Dec 31 00:00:00 GMT 2022", actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClass() throws ParseException
    {
        String className = "InvalidClass";
        TypeHandler.createObject(className);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectNoDefaultConstructor() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.io.FileInputStream";
        TypeHandler.createObject(className);
    }
}