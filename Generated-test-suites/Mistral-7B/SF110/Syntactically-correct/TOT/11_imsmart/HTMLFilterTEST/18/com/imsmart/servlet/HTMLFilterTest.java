
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
  @Test
  public void testFilterNullMessage() {
    String actual = HTMLFilter.filter(null);
    assertNull(actual);
  }

  @Test
  public void testFilterEmptyMessage() {
    String message = "";
    String actual = HTMLFilter.filter(message);
    assertEquals("", actual);
  }

  @Test
  public void testFilterSimpleMessage() {
    String message = "Hello World!";
    String actual = HTMLFilter.filter(message);
    String expected = "Hello World!";
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWithSpecialCharacters() {
    String message = "<Hello World!>&";
    String actual = HTMLFilter.filter(message);
    String expected = "&lt;Hello World!&gt;&amp;";
    assertEquals(expected, actual);
  }

  @Test
  public void testFilterWithLongMessage() {
    String message = "This is a long message that tests the filtering of special characters.< This message also contains multiple occurrences of special characters. >";
    String actual = HTMLFilter.filter(message);
    String expected = "This is a long message that tests the filtering of special characters.&lt; This message also contains multiple occurrences of special characters.&gt;";
    assertEquals(expected, actual);
  }

  @Test(expected = NullPointerException.class)
  public void testFilterWithNullCharArray() {
    char[] content = null;
    String actual = new String(HTMLFilter.filter(content));
    fail("Filtering a null char array should throw a NullPointerException");
  }
}
