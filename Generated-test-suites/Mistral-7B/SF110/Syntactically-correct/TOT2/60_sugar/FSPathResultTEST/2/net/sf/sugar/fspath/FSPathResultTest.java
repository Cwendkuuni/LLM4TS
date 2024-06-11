package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

  @Test
  public void testFSPathResultConstruction() {
    // Test construction with File
    File file = new File("testFile.txt");
    FSPathResult result = new FSPathResult(file);
    assertEquals(file, result.getFile());

    // Test construction with String
    String string = "testString";
    FSPathResult stringResult = new FSPathResult(string);
    assertEquals(string, stringResult.getString());

    // Test construction with Double
    Double doubleValue = 3.14;
    FSPathResult doubleResult = new FSPathResult(doubleValue);
    assertEquals(doubleValue, doubleResult.getDouble());

    // Test construction with Date
    Date date = new Date();
    FSPathResult dateResult = new FSPathResult(date);
    assertEquals(date, dateResult.getDate());

    // Test construction with URI
    URI uri = new URI("http://example.com");
    FSPathResult uriResult = new FSPathResult(uri);
    assertEquals(uri, uriResult.getURI());

    // Test construction with Boolean
    Boolean booleanValue = true;
    FSPathResult booleanResult = new FSPathResult(booleanValue);
    assertEquals(booleanValue, booleanResult.getBoolean());
  }

  @Test
  public void testFSPathResultNullConstructor() {
    try {
      new FSPathResult(null);
      fail("Exception expected");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testFSPathResultGetFileNullResult() {
    FSPathResult result = new FSPathResult(null);
    assertNull(result.getFile());
  }

  // Add similar test cases for getString(), getDouble(), getDate(), getURI(), getBoolean(), and toString()

  @Test
  public void testFSPathResultToStringWithNullResult() {
    FSPathResult result = new FSPathResult(null);
    assertEquals("FSPathResult : empty", result.toString());
  }
}