import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Utf8Test {
   @Test
   public void testEncodedLength() {
      String sequence = "Test String";
      int expected = Utf8.encodedLength(sequence);
      int actual = Utf8.encodedLength(sequence);
      assertEquals("Test string encoded length is incorrect", expected, actual);
   }
}