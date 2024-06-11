import static org.junit.Assert.*;
import static com.google.common.base.Splitter.*;

public class SplitterTest {

  @Test
  public void testOn() {
    String input = "Hello,World!";
    Splitter splitter = on(' ');
    Iterable<String> result = splitter.split(input);
    List<String> expected = Arrays.asList("Hello", "World!");
    assertEquals(expected, result);
  }
}