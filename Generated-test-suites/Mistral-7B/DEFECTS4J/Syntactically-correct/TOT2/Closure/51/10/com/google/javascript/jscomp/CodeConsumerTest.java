import static org.junit.Assert.assertEquals;

public class CodeConsumerTest {
  CodeConsumer codeConsumer = new CodeConsumer() {
    @Override
    char getLastChar() {
      return 'A'; // A dummy implementation for testing
    }
  };

  @Test
  public void testGetLastChar() {
    assertEquals('A', codeConsumer.getLastChar());
  }
}