import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();

    consumer.startSourceMapping(node);
    consumer.append("Hello");
    consumer.endSourceMapping(node);

    String expectedSource = "Hello"; // Source code corresponding to the appended string
    String actualSource = consumer.getSource(); // Get the generated source code from the consumer

    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    boolean result = consumer.continueProcessing();
    assertTrue(result);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("Hello");
    char lastChar = consumer.getLastChar();

    assertEquals('o', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("Hello ");
    consumer.append("world");
    consumer.endLine();

    String expectedCode = "Hello \nworld";
    String actualCode = consumer.getSource();

    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStatementNeedsEnded() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) {");
    consumer.endStatement();
    consumer.append("}");

    String expectedCode = "if (true) {\n}\n";
    String actualCode = consumer.getSource();

    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction();

    String expectedCode = "function foo() {}";
    String actualCode = consumer.getSource();

    assertEquals(expectedCode, actualCode);
  }

  // Add more test cases for other methods as needed

  @Test
  public void testErrorHandling() {
    CodeConsumer consumer = new TestCodeConsumer();

    // Test an error scenario, for example, an attempt to append an invalid character
    consumer.append("Hello");
    consumer.append('\u0000'); // Null character

    // Assert that an error is thrown or an exception is handled appropriately
    try {
      String actualCode = consumer.getSource();
      fail("Expected an error or exception");
    } catch (RuntimeException e) {
      // Assert that the correct error message is thrown
      assertEquals("Invalid character: '\u0000'", e.getMessage());
    }
  }
}