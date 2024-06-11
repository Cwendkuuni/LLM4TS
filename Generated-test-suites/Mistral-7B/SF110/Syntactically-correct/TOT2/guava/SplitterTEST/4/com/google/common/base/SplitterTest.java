import com.google.common.base.Splitter;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on('-');
    String input = "apple-banana-orange-grape";
    Iterable<String> result = splitter.split(input);
    List<String> expected = Arrays.asList("apple", "banana", "orange", "grape");
    assertEquals(expected, result);
  }

  @Test(expected = NullPointerException.class)
  public void testOnCharSeparatorWithNullInput() {
    Splitter splitter = Splitter.on('-');
    String input = null;
    splitter.split(input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnCharSeparatorWithEmptySeparator() {
    Splitter splitter = Splitter.on('');
    String input = "apple-banana-orange-grape";
    splitter.split(input);
  }
}