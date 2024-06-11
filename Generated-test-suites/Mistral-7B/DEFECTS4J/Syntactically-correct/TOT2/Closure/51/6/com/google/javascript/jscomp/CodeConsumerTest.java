import org.junit.Test;
import static org.junit.Assert.*;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.startSourceMapping(null);
    assertTrue(consumer.statementNeedsEnded == false);
  }

  // ... test cases for all methods

}