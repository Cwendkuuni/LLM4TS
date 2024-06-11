import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = new TestCodeConsumer();
  }

  /**
   * Test for startSourceMapping and endSourceMapping methods.
   */
  @Test
  public void testSourceMapping() {
    Node node1 = Node.newNode(1, "Node1");
    Node node2 = Node.newNode(2, "Node2");
    consumer.startSourceMapping(node1);
    consumer.append("function test() {");
    consumer.endSourceMapping(node1);
    consumer.addIdentifier("x");
    consumer.add(Node.IDENTIFIER, "x");
    consumer.endSourceMapping(node2);
    consumer.append("}");

    String expected = "function test() {\n" +
        "  x;\n" +
        "}\n";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for continueProcessing method.
   */
  @Test
  public void testContinueProcessing() {
    consumer.append("function test() {");
    assertFalse(consumer.continueProcessing());
    consumer.append("return 1;");
    assertTrue(consumer.continueProcessing());
  }

  /**
   * Test for getLastChar method.
   */
  @Test
  public void testGetLastChar() {
    consumer.append("function test() {");
    assertEquals('\n', consumer.getLastChar());
    consumer.append("return 1;");
    assertEquals(';', consumer.getLastChar());
  }

  /**
   * Test for append method.
   */
  @Test
  public void testAppend() {
    consumer.append("function test() {");
    consumer.append("x = 1;");
    consumer.append("}");

    String expected = "function test() {\n" +
        "  x = 1;\n" +
        "}\n";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for startNewLine and endLine methods.
   */
  @Test
  public void testLineBreaks() {
    consumer.append("function test() {");
    consumer.startNewLine();
    consumer.append("x = 1;");
    consumer.endLine();
    consumer.startNewLine();
    consumer.append("y = 2;");
    consumer.endLine();
    consumer.append("}");

    String expected = "function test() {\n" +
        "  x = 1;\n" +
        "\n" +
        "  y = 2;\n" +
        "}\n";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for beginBlock, endBlock, and endStatement methods.
   */
  @Test
  public void testStatements() {
    consumer.append("function test() {");
    consumer.beginBlock();
    consumer.addIdentifier("x");
    consumer.add(Node.ASSIGNMENT, "=");
    consumer.addNumber(1.0);
    consumer.listSeparator();
    consumer.addIdentifier("y");
    consumer.add(Node.ASSIGNMENT, "=");
    consumer.addNumber(2.0);
    consumer.endStatement();
    consumer.append("}");

    String expected = "function test() {\n" +
        "  x = 1;\n" +
        "  y = 2;\n" +
        "}\n";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for endFunction method.
   */
  @Test
  public void testEndFunction() {
    consumer.append("function test() {");
    consumer.beginBlock();
    consumer.addIdentifier("x");
    consumer.add(Node.ASSIGNMENT, "=");
    consumer.addNumber(1.0);
    consumer.listSeparator();
    consumer.addIdentifier("y");
    consumer.add(Node.ASSIGNMENT, "=");
    consumer.addNumber(2.0);
    consumer.endStatement();
    consumer.endFunction();

    String expected = "function test() {\n" +
        "  x = 1;\n" +
        "  y = 2;\n" +
        "}";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for addNumber method.
   */
  @Test
  public void testAddNumber() {
    consumer.append("function test() {");
    consumer.beginBlock();
    consumer.addNumber(-1.5);
    consumer.listSeparator();
    consumer.addNumber(2.5);
    consumer.endStatement();
    consumer.endFunction();

    String expected = "function test() {\n" +
        "  -1.5;\n" +
        "  2.5;\n" +
        "}";

    assertEquals(expected, consumer.getCode());
  }

  /**
   * Test for isWordChar method.
   */
  @Test
  public void testIsWordChar() {
    for (char ch = 0; ch <= 127; ch++) {
      boolean result = isWordChar(ch);
      if (Character.isLetterOrDigit(ch)) {
        assertTrue(result);
      } else if (ch == '_' || ch == '$') {
        assertTrue(result);
      } else {
        assertFalse(result);
      }
    }
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    return super.getCode();
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return true;
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return statementContext;
  }
}