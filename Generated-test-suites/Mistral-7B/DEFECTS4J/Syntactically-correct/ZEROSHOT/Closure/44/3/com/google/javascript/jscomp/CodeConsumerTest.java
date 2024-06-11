import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Check if startSourceMapping was called
    assertTrue(consumer.sourceMappingStarted);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Check if endSourceMapping was called and sourceMappingEnded was reset
    assertFalse(consumer.sourceMappingStarted);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    // Check if continueProcessing returns true by default
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    char lastChar = consumer.getLastChar();
    assertEquals('t', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    String expected = "test";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    String expected = "{\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    String expected = ", ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementWithoutNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(false);
    String expected = "test;";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expected = "test;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatementWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement(true);
    String expected = "test;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    String expected = "}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    String expected = ": ";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("test");
    String expected = "test";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", false);
    String expected = "+";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1.23);
    String expected = "1.23";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    node.setType(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  // Test sawFunction variable
  @Test
  public void testEndFunction_sawFunction_true() {
    CodeConsumer consumer = new StubCodeConsumerWithSawFunction();
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  // Inner class for testing sawFunction variable
  private static class StubCodeConsumerWithSawFunction extends StubCodeConsumer {
    boolean sawFunction = false;

    @Override
    void endFunction(boolean statementContext) {
      sawFunction = true;
      super.endFunction(statementContext);
    }
  }
}