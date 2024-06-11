import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

 @Test
 public void testFSPathResultFile() {
 File file = new File("test.txt");
 FSPathResult result = new FSPathResult(file);
 File expected = file;

 assertEquals(expected, result.getFile());
 assertTrue(result.toString().startsWith(file.getAbsolutePath()));
 }

 @Test
 public void testFSPathResultString() {
 String str = "test";
 FSPathResult result = new FSPathResult(str);
 String expected = str;

 assertEquals(expected, result.getString());
 assertTrue(result.toString().equals(str));
 }

 @Test
 public void testFSPathResultDouble() {
 Double d = 3.14;
 FSPathResult result = new FSPathResult(d);
 Double expected = d;

 assertEquals(expected, result.getDouble(), 0.0);
 assertTrue(result.toString().equals(expected.toString()));
 }

 @Test
 public void testFSPathResultDate() {
 Date date = new Date();
 FSPathResult result = new FSPathResult(date);
 Date expected = date;

 assertEquals(expected, result.getDate());
 assertTrue(result.toString().equals(expected.toString()));
 }

 @Test
 public void testFSPathResultURI() {
 URI uri = new URI("http://www.example.com/test");
 FSPathResult result = new FSPathResult(uri);
 URI expected = uri;

 assertEquals(expected, result.getURI());
 assertTrue(result.toString().equals(expected.toString()));
 }

 @Test
 public void testFSPathResultBoolean() {
 Boolean bool = Boolean.TRUE;
 FSPathResult result = new FSPathResult(bool);
 Boolean expected = bool;

 assertEquals(expected, result.getBoolean());
 assertTrue(result.toString().equals(expected.toString()));
 }

 @Test
 public void testFSPathResultEmpty() {
 FSPathResult result = new FSPathResult();
 assertNull(result.getFile());
 assertNull(result.getString());
 assertNull(result.getDouble());
 assertNull(result.getDate());
 assertNull(result.getURI());
 assertNull(result.getBoolean());
 assertTrue(result.toString().equals("FSPathResult : empty"));
 }

 @Test
 public void testFSPathResultToStringNull() {
 FSPathResult result = new FSPathResult(null);
 assertNull(result.getFile());
 assertNull(result.getString());
 assertNull(result.getDouble());
 assertNull(result.getDate());
 assertNull(result.getURI());
 assertFalse(result.getBoolean());
 assertTrue(result.toString().equals("FSPathResult : empty"));
 }
}