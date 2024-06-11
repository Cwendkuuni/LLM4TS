import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = Node.newNode("identifier");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Verify no exceptions were thrown and the code was not modified
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer.append("Hello, world!");
    String expected = "Hello, world!";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("foo = 42;");
    consumer.endStatement();
    String expected = "foo = 42;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.append("if (x > 0) {");
    consumer.append(" x++;");
    consumer.endBlock();
    String expected = "if (x > 0) {\n  x++;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.append("if (x > 0) {");
    consumer.beginBlock();
    consumer.append(" x++;");
    consumer.endBlock();
    String expected = "if (x > 0) {\n  x++;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("foo = 42;");
    consumer.startNewLine();
    consumer.append("bar = 43;");
    String expected = "foo = 42;\nbar = 43;";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    consumer.continueProcessing = () -> false;
    consumer.append("foo = 42;");
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidAppend() {
    consumer.append("foo = 42;x");
  }

  @Test
  public void testGetLastChar() {
    consumer.append("foo = 42;");
    char lastChar = consumer.getLastChar();
    assertEquals(';', lastChar);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginCaseBody();
    String expected = ":";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.beginCaseBody();
    consumer.append("x++;");
    consumer.endCaseBody();
    String expected = ":";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.addOp("+", false);
    consumer.addOp("++", false);
    consumer.addOp("++", true);
    String expected = "+ ++";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(3.14159265358979323846);
    String expected = "3.14159265358979323846";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    Node node = Node.newNode("if (x > 0) {x++;}");
    consumer.shouldPreserveExtraBlocks = () -> true;
    consumer.endBlockFor(node, true);
    String expected = "if (x > 0) {\n  x++;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = Node.newNode("if (x > 0) {x++;}");
    consumer.breakAfterBlockFor = (n, statementContext) -> true;
    consumer.endBlockFor(node, false);
    String expected = "if (x > 0) {\n  x++;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getCode().charAt(super.getCode().length() - 1);
  }

  @Override
  String getCode() {
    return super.code;
  }
}