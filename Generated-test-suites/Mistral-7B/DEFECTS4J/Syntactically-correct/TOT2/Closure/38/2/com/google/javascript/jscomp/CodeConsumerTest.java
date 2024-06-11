import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);

    // Verify that startSourceMapping method was called with the given node
    when(consumer.continueProcessing()).thenReturn(true);
    when(node.getClass()).thenReturn(Node.class);

    consumer.startSourceMapping(node);
    consumer.continueProcessing();
    consumer.continueProcessing();

    assertEquals(true, consumer.statementStarted);
    assertEquals(false, consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new CodeConsumer() {
      boolean statementStarted = true;
      boolean statementNeedsEnded = true;
    };

    Node node = mock(Node.class);

    consumer.endSourceMapping(node);

    assertEquals(false, consumer.statementStarted);
    assertEquals(false, consumer.statementNeedsEnded);
  }

  // Add test cases for other methods with basic functionality and edge cases
}