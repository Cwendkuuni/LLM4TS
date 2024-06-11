package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FSPathResultTest {

  @Test
  public void testGetFile() {
    File file = new File("path/to/file");
    FSPathResult result = new FSPathResult(file);

    File expected = file;
    File actual = result.getFile();

    assertEquals(expected, actual);
  }

  // Edge cases and exception handling tests follow

  @Test(expected = NullPointerException.class)
  public void testNullInputFile() {
    FSPathResult result = new FSPathResult(null);

    assertNull(result.getFile());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputString() {
    FSPathResult result = new FSPathResult(null);

    assertEquals("FSPathResult : empty", result.getString());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputDouble() {
    FSPathResult result = new FSPathResult(null);

    assertNull(result.getDouble());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputDate() {
    FSPathResult result = new FSPathResult(null);

    assertNull(result.getDate());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputURI() {
    FSPathResult result = new FSPathResult(null);

    assertNull(result.getURI());
  }

  @Test(expected = NullPointerException.class)
  public void testNullInputBoolean() {
    FSPathResult result = new FSPathResult(null);

    assertNull(result.getBoolean());
  }

  @Test
  public void testEmptyFile() {
    File file = new File("");
    FSPathResult result = new FSPathResult(file);

    assertEquals("", result.getFile().getAbsolutePath());
  }

  @Test
  public void testToStringNullFileName() {
    FSPathResult result = new FSPathResult(null);

    assertEquals("FSPathResult : empty", result.toString());
  }

  @Test
  public void testToStringEmptyString() {
    FSPathResult result = new FSPathResult("");

    assertEquals("FSPathResult : empty", result.toString());
  }

  @Test
  public void testToStringNullDate() {
    FSPathResult result = new FSPathResult(new Date());

    result.resultDate = null;

    assertEquals("FSPathResult : empty", result.toString());
  }

  @Test
  public void testToStringNullDouble() {
    FSPathResult result = new FSPathResult(new Double(1.0));

    result.resultDouble = null;

    assertEquals("FSPathResult : empty", result.toString());
  }

  @Test
  public void testToStringNullURI() {
    FSPathResult result = new FSPathResult(new URI("http://www.example.com"));

    result.resultURI = null;

    assertEquals("FSPathResult : empty", result.toString());
  }

  @Test
  public void testToStringNullBoolean() {
    FSPathResult result = new FSPathResult(Boolean.TRUE);

    result.resultBoolean = null;

    assertEquals("FSPathResult : empty", result.toString());
  }
}