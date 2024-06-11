import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert on private fields directly, so check public methods instead.
    assertTrue(consumer.hasStartedSourceMapping());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.hasStartedSourceMapping());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
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
    consumer.append("test");
    consumer.maybeLineBreak();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.append("test");
    consumer.maybeCutLine();
    assertEquals("test\ntest", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.notePreferredLineBreak();
    assertEquals("test\n", consumer.getCode());
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
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("test{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    assertEquals("test{\n test\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.listSeparator();
    consumer.append("test");
    assertEquals("test,\n test\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    assertEquals("test;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementNoSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.endStatement(false);
    assertEquals("test\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    consumer.append("test");
    assertEquals("test;\n test\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction(true);
    assertEquals("function test() {\n}\n", consumer.getCode());
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
    consumer.append("test");
    consumer.endCaseBody();
    assertEquals(":\n test\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.add("test");
    assertEquals("testtest\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("test");
    assertEquals("test + test\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    consumer.addNumber(42);
    assertEquals("test 42\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(Character.isLetterOrDigit('a'));
    assertFalse(CodeConsumer.isWordChar(33));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForNodeWithStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForNodeWithoutStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testHasStartedSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.hasStartedSourceMapping());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);

    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }
}

class StubCodeConsumer extends CodeConsumer {
  private StringBuilder code = new StringBuilder();

  @Override
  public void append(String str) {
    code.append(str);
  }

  @Override
  public void add(String newCode) {
    code.append(newCode);
  }

  @Override
  public void appendBlockStart() {
    super.appendBlockStart();
  }

  @Override
  public void appendBlockEnd() {
    super.appendBlockEnd();
  }

  @Override
  public void startNewLine() {
    super.startNewLine();
  }

  @Override
  public void maybeLineBreak() {
    super.maybeLineBreak();
  }

  @Override
  public void maybeCutLine() {
    super.maybeCutLine();
  }

  @Override
  public void endLine() {
    super.endLine();
  }

  @Override
  public void notePreferredLineBreak() {
    super.notePreferredLineBreak();
  }

  @Override
  public void beginBlock() {
    super.beginBlock();
  }

  @Override
  public void endBlock() {
    super.endBlock();
  }

  @Override
  public void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
  }

  @Override
  public void listSeparator() {
    super.listSeparator();
  }

  @Override
  public void endStatement() {
    super.endStatement();
  }

  @Override
  public void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
  }

  @Override
  public void maybeEndStatement() {
    super.maybeEndStatement();
  }

  @Override
  public void endFunction() {
    super.endFunction();
  }

  @Override
  public void endFunction(boolean statementContext) {
    super.endFunction(statementContext);
  }

  @Override
  public void beginCaseBody() {
    super.beginCaseBody();
  }

  @Override
  public void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  public void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
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