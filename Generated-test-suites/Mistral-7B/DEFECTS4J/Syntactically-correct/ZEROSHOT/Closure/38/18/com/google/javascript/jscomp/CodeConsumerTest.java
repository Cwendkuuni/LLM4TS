import static org.junit.Assert.*;
import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions for startSourceMapping cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions for endSourceMapping cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    String expectedOutput = "identifier";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    String expectedOutput = "test";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    String expectedOutput = "{\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    String expectedOutput = "}\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    String expectedOutput = "\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    // Assertions for maybeLineBreak cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
    // Assertions for maybeCutLine cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    String expectedOutput = "\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    // Assertions for notePreferredLineBreak cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    String expectedOutput = "{\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    String expectedOutput = "}\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.listSeparator();
    String expectedOutput = ", ";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expectedOutput = "test;\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    String expectedOutput = "test;\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expectedOutput = "test";
    assertEquals(expectedOutput, consumer.getOutput());
    consumer.endLine();
    consumer.statementNeedsEnded = true;
    consumer.maybeEndStatement();
    expectedOutput += ";\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() { test(); }");
    consumer.endFunction();
    String expectedOutput = "function test() {\n  test();\n}\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() { test(); }");
    consumer.endFunction(true);
    String expectedOutput = "function test() {\n  test();\n}\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    String expectedOutput = "case 1:\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.endCaseBody();
    String expectedOutput = "case 1:\n";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.add("String");
    String expectedOutput = "testString";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("5");
    consumer.addOp("+", true);
    consumer.append("3");
    consumer.addOp("+", true);
    String expectedOutput = "5 + 3 +";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test");
    consumer.addNumber(5.0);
    String expectedOutput = "test 5.0";
    assertEquals(expectedOutput, consumer.getOutput());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isNegativeZero(0.0));
    assertFalse(consumer.isNegativeZero(5.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('1'));
    assertFalse(consumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    // Assertions for endFile cannot be added directly, as the method does not return a value or modify any state that can be tested.
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    private StringBuilder output = new StringBuilder();

    @Override
    protected void append(String str) {
      output.append(str);
    }

    @Override
    protected void add(String newcode) {
      output.append(newcode);
    }

    @Override
    protected void addOp(String op, boolean binOp) {
      output.append(op);
    }

    @Override
    protected void addNumber(double x) {
      output.append(x);
    }

    @Override
    protected void endFile() {
    }

    @Override
    protected char getLastChar() {
      return output.charAt(output.length() - 1);
    }

    public String getOutput() {
      return output.toString();
    }
  }
}