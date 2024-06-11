import static org.junit.Assert.*;
import com.google.common.base.Utf8;

public class Utf8Test {

  @Test
  public void testEncodedLength() {
    String input = "hello world";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals("Encoded length should match", expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthEmptyString() {
    String input = "";
    Utf8.encodedLength(input);
  }

  @Test
  public void testEncodedLengthAsciiString() {
    String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals("Encoded length should match", expected, actual);
  }

  @Test
  public void testEncodedLengthHighSurrogateString() {
    String input = "𐅀𐅁𐅂𐅃𐅄𐅅";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals("Encoded length should match", expected, actual);
  }

  @Test
  public void testEncodedLengthLowSurrogateString() {
    String input = "𐀀𐀁𐀂𐀃𐀄𐀅";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals("Encoded length should match", expected, actual);
  }

  @Test
  public void testEncodedLengthSurrogatePairString() {
    String input = "𐀏𐀯𐀱𐀲𐀳𐀴";
    int expected = Utf8.encodedLength(input);
    int actual = Utf8.encodedLength(input);
    assertEquals("Encoded length should match", expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthInvalidUtf8String() {
    String input = "hello☻";
    Utf8.encodedLength(input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthNullInput() {
    Utf8.encodedLength(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLengthOverflowInput() {
    String input = "𐀀𐀁𐀂𐀃𐀄𐀅𐀀𐀁𐀂𐀃𐀄𐀅𐀀𐀁𐀂𐀃𐀄𐀅";
    Utf8.encodedLength(input);
  }
}