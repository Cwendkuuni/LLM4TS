import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, value);
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        String str = "123";
        Class<?> clazz = Integer.class;
        Object value = TypeHandler.createValue(str, clazz);
        assertEquals(123, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidString() throws ParseException {
        String str = "abc";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassname() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateObject_ValidClassname() throws ParseException {
        String classname = String.class.getName();
        Object value = TypeHandler.createObject(classname);
        assertTrue(value instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumber_LongValue() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123, number.longValue());
    }

    @Test
    public void testCreateNumber_DoubleValue() throws ParseException {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.01);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClassname() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateClass_ValidClassname() throws ParseException, ClassNotFoundException {
        String classname = String.class.getName();
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateDate_InvalidDateString() throws ParseException {
        String str = "InvalidDate";
        TypeHandler.createDate(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_NotYetImplemented() throws ParseException {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_ValidURLString() throws ParseException {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURLString() throws ParseException {
        String str = "invalid://www.example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_ValidFileString() {
        String str = "test.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(file.exists());
        assertFalse(file.isDirectory());
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidFileString() {
        String str = "invalid_file:";
        TypeHandler.createFile(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_FileNotFound() {
        String str = "non_existent_file.txt";
        TypeHandler.createFile(str);
    }

    @Test
    public void testCreateFiles_ValidFilePaths() throws Exception {
        String str = "test1.txt, test2.txt";
        File[] files = TypeHandler.createFiles(str);
        assertEquals(2, files.length);
        for (File file : files) {
            assertTrue(file.exists());
            assertFalse(file.isDirectory());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_NotYetImplemented() throws Exception {
        String str = "test1.txt, test2.txt";
        TypeHandler.createFiles(str);
    }
}