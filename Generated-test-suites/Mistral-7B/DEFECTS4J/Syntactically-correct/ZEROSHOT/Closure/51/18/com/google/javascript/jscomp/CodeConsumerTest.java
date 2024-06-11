import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;
import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert anything here as startSourceMapping is empty
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert anything here as endSourceMapping is empty
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    // Cannot assert anything as maybeLineBreak is empty
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    // Cannot assert anything as maybeCutLine is empty
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    // Cannot assert anything as notePreferredLineBreak is empty
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLineTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (true) {");
    consumer.endStatement();
    assertEquals("if (true) {\n", consumer.getCode());
    consumer.endStatement(true);
    assertEquals("if (true) {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foo\nbar", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("1 + 2 +");
    consumer.addOp("3", false);
    assertEquals("1 + 2 + 3\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("1 + 2 +");
    consumer.addOp("3", true);
    assertEquals("1 + 2 + (\n3\n)", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1.2);
    assertEquals("1.2\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('B'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    StubCodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    StubCodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    // True when statementContext is true and false otherwise
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    // Nothing is appended to the code
  }

  @Test
  public void testStatementNeedsEndedAndStatementStartedAreFalseInitially() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.statementNeedsEnded);
    assertFalse(consumer.statementStarted);
  }

  @Test
  public void testSawFunctionIsFalseInitially() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.sawFunction);
  }
}

class StubCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public void append(String str) {
    code.append(str);
  }

  @Override
  public void add(String newcode) {
    super.add(newcode);
  }

  @Override
  public void appendOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  public void startSourceMapping(Node node) {
    super.startSourceMapping(node);
  }

  @Override
  public void endSourceMapping(Node node) {
    super.endSourceMapping(node);
  }

  @Override
  public void startBlock() {
    super.beginBlock();
  }

  @Override
  public void endBlock() {
    super.endBlock();
  }

  @Override
  public void endFunction() {
    super.endFunction();
  }

  @Override
  public void endFile() {
    super.endFile();
  }

  @Override
  public char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  public String getCode() {
    return code.toString();
  }
}