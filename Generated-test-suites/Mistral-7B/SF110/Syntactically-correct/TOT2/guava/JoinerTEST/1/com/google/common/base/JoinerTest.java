import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JoinerTest {

  @Test
  public void testAppendToWithIterable() throws IOException {
    Joiner joiner = Joiner.on(":");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Iterator<String> iterator = Arrays.asList("Hello", "World", "!").iterator();

    joiner.appendTo(outputStream, iterator);

    String expectedOutput = "Hello:" + joiner.separator + "World:" + joiner.separator + "!";
    assertEquals(expectedOutput, outputStream.toString());
  }
}