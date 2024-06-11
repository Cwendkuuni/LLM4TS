import static org.junit.Assert.assertEquals;
import com.google.common.base.Utf8;

public class Utf8Test {

  @Test
  public void testEncodedLength() {
    CharSequence sequence = "Hello, World!";
    int expectedLength = Utf8.encodedLength(sequence);
    int actualLength = sequence.length();
    assertEquals("Encoded length should match the length of the given sequence", expectedLength, actualLength);
  }
}