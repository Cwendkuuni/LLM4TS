import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(org.junit.categories.IT)
public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertNotNull(result.getString());
        assertEquals(string, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertNotNull(result.getDouble());
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        FSPathResult result = new FSPathResult(date);
        assertNotNull(result.getDate());
        assertEquals(sdf.format(date), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        String scheme = "http";
        String host = "example.com";
        int port = 80;
        String path = "/test";
        String queryString = "?name=John&age=30";
        URI uri = new URI(scheme, null, host, port, path, queryString, null);
        FSPathResult result = new FSPathResult(uri);
        assertNotNull(result.getURI());
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertNotNull(result.getBoolean());
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullFile() {
        File file = null;
        FSPathResult result = new FSPathResult(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultEmptyString() {
        String string = "";
        FSPathResult result = new FSPathResult(string);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultZeroDouble() {
        Double doubleValue = 0.0;
        FSPathResult result = new FSPathResult(doubleValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDate() {
        Date date = null;
        FSPathResult result = new FSPathResult(date);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFSPathResultNonExistentFile() {
        File file = new File("non_existent_file.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
        fail("Expected FileNotFoundException but got a valid FSPathResult instance");
    }

    @Test(expected = SecurityException.class)
    public void testFSPathResultNoAccessFile() {
        String fileName = "/etc/passwd";
        File file = new File(fileName);
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
        fail("Expected SecurityException but got a valid FSPathResult instance");
    }

    // Test large file size
    @Test
    public void testFSPathResultLargeFile() {
        File largeFile = new File("large_file.dat", new Random().nextInt(10 * 1024 * 1024));
        FSPathResult result = new FSPathResult(largeFile);
        assertNotNull(result.getFile());
        assertEquals(largeFile.getAbsolutePath(), result.toString());
    }

    // Test string with special characters
    @Test
    public void testFSPathResultSpecialCharactersString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);
        assertNotNull(result.getString());
        assertEquals(string, result.toString());
    }

    // Test invalid date format
    @Test(expected = ParseException.class)
    public void testFSPathResultInvalidDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa");
        String dateString = "29/12/2022 12:00:00pm";
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            // ignore
        }

        FSPathResult result = new FSPathResult(date);
        fail("Expected ParseException but got a valid FSPathResult instance");
    }

    // Test invalid URI scheme
    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultInvalidURIScheme() {
        String scheme = "ftp:";
        String host = "example.com";
        int port = 80;
        String path = "/test";
        String queryString = "?name=John&age=30";
        URI uri = new URI(scheme, null, host, port, path, queryString, null);
        FSPathResult result = new FSPathResult(uri);
        fail("Expected IllegalArgumentException but got a valid FSPathResult instance");
    }

}