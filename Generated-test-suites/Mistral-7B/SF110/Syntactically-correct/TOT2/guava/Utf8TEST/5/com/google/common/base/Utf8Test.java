import static org.junit.Assert.*;
import com.google.common.base.*;

public class Utf8Test {

  @Test
  public void testEncodedLength() {
    String input = "hello world";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthGeneral() {
    String input = "test נועה";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLengthGeneral(input, 0);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthNegativeInputLength() {
    String input = "test";
    int expected = 0;
    int actual = Utf8.encodedLength(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthNullInput() {
    int expected = 0;
    int actual = Utf8.encodedLength(null);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthInvalidUtf8Sequence() {
    String input = "test\u0000";
    int expected = -1;
    int actual = Utf8.encodedLength(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthSurrogatePair() {
    String input = "test🌎";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLengthGeneral(input, 0);
    assertEquals(expected, actual);
  }

  @Test
  public void testIsWellFormed() {
    byte[] input = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33 };
    boolean expected = true;
    boolean actual = Utf8.isWellFormed(input);
    assertTrue(expected == actual);
  }

  @Test
  public void testIsWellFormedSlowPathSimple() {
    byte[] input = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, -1 };
    boolean expected = false;
    boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
    assertTrue(expected == actual);
  }

  @Test
  public void testIsWellFormedSlowPathMalformed() {
    byte[] input = new byte[] { 72, 101, 108, 108, 111, 32, -1, 119, 111, 114, 108, 100, 33 };
    boolean expected = false;
    boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
    assertTrue(expected == actual);
  }

  @Test
  public void testIsWellFormedSlowPathValid() {
    byte[] input = new byte[] { 72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, -65 };
    boolean expected = true;
    boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
    assertTrue(expected == actual);
  }

  @Test
  public void testIsWellFormedSlowPathEmpty() {
    byte[] input = new byte[] {};
    boolean expected = true;
    boolean actual = Utf8.isWellFormedSlowPath(input, 0, input.length);
    assertTrue(expected == actual);
  }

  @Test
  public void testIsWellFormedSlowPathNullInput() {
    boolean expected = true;
    boolean actual = Utf8.isWellFormedSlowPath(null, 0, 0);
    assertTrue(expected == actual);
  }
}