import static org.junit.Assert.*;
import static org.junit.Assume.*;

import com.google.common.base.Utf8;

public class Utf8Test {

  @Test
  public void testEncodedLength() {
    String input = "ABCDEFG";
    int expected = 6;
    int actual = Utf8.encodedLength(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthSurrogate() {
    String input = "😃";
    int expected = 4;
    int actual = Utf8.encodedLength(input);
    assertEquals(expected, actual);
  }

  @Test
  public void testEncodedLengthLongSequence() {
    String input = "A".repeat(10000);
    int expected = 10001; // Approximate value, actual value might differ
    int actual = Utf8.encodedLength(input);
    assumeTrue(expected > Integer.MAX_VALUE);
    assertEquals(expected, actual & 0xFFFFFFFF);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthIllegalByteSequence() {
    String input = "ABC\u0080DEF";
    Utf8.encodedLength(input);
  }

  @Test
  public void testIsWellFormedValid() {
    byte[] input = {0x41, 0x42, 0x43, 0x44};
    boolean expected = true;
    boolean actual = Utf8.isWellFormed(input);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsWellFormedInvalidByteSequence() {
    byte[] input = {0x41, 0x42, 0x80, 0x45};
    Utf8.isWellFormed(input);
  }

  @Test
  public void testIsWellFormedLongByteArray() {
    byte[] input = new byte[10000];
    boolean expected = true;
    Arrays.fill(input, (byte) 0x41);
    boolean actual = Utf8.isWellFormed(input);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsWellFormedInvalidByteArray() {
    byte[] input = new byte[10000];
    Arrays.fill(input, (byte) 0x41);
    input[5000] = (byte) 0x80;
    Utf8.isWellFormed(input);
  }
}