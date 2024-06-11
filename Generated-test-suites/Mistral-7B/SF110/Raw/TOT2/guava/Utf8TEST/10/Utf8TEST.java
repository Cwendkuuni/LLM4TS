import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

  @Test
  public void testEncodedLength() {
    CharSequence sequence = "Hello World";
    int expected = Utf8.encodedLength(sequence);
    int actual = Utf8.encodedLength(sequence);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthEmptyString() {
    CharSequence sequence = "";
    int expected = Utf8.encodedLength(sequence);
    int actual = Utf8.encodedLength(sequence);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthNullSequence() {
    CharSequence sequence = null;
    Utf8.encodedLength(sequence);
  }

  @Test
  public void testEncodedLengthInvalidSequence() {
    CharSequence sequence = "\uD800\uDC00";
    int expected = Utf8.encodedLength(sequence);
    int actual = Utf8.encodedLength(sequence);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthGeneral() {
    CharSequence sequence = "Mojito දීල්ලපොත්";
    int expected = Utf8.encodedLengthGeneral(sequence.toCharArray(), 0);
    int actual = Utf8.encodedLengthGeneral(sequence, 0);
    assertEquals(expected, actual);
  }

  @Test
  public void testIsWellFormed() {
    byte[] bytes = new byte[] { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
    boolean expected = Utf8.isWellFormed(bytes);
    boolean actual = Utf8.isWellFormed(bytes);
    assertTrue(expected);
    assertTrue(actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsWellFormedNullBytes() {
    byte[] bytes = null;
    Utf8.isWellFormed(bytes);
  }

  @Test
  public void testIsWellFormedInvalidBytes() {
    byte[] bytes = new byte[] { 0x41, 0x80, 0x42, 0x80 };
    boolean expected = false;
    boolean actual = Utf8.isWellFormed(bytes);
    assertFalse(expected);
    assertFalse(actual);
  }

  @Test
  public void testIsWellFormedInvalidLength() {
    byte[] bytes = new byte[] { 0x48, 0x65, 0x6C, 0x6C, 0x6F, (byte) 0xFF };
    boolean expected = false;
    boolean actual = Utf8.isWellFormed(bytes, 0, 5);
    assertFalse(expected);
    assertFalse(actual);
  }

  @Test
  public void testIsWellFormedSlowPath() {
    byte[] bytes = new byte[] { 0x41, 0x80, 0x42, 0x80, 0x43, 0x80 };
    boolean expected = false;
    boolean actual = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
    assertFalse(expected);
    assertFalse(actual);
  }

  @Test
  public void testIsWellFormedSlowPathValid() {
    byte[] bytes = new byte[] { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
    boolean expected = true;
    boolean actual = Utf8.isWellFormedSlowPath(bytes, 0, bytes.length);
    assertTrue(expected);
    assertTrue(actual);
  }
}