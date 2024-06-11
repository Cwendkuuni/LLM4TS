import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.endSourceMapping(node);
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
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
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
    consumer.append("ab");
    consumer.endBlock();
    assertEquals("{\nab\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("ab");
    consumer.endBlock(true);
    assertEquals("{\nab\n}", consumer.getCode());
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
    consumer.append("ab");
    consumer.endStatement();
    assertEquals("ab;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.endStatement(true);
    assertEquals("ab;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {}\n");
    consumer.endFunction(true);
    assertEquals("function foo() {\n}\n", consumer.getCode());
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
    consumer.append("ab");
    consumer.endCaseBody();
    assertEquals(":\nab\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.add("cd");
    assertEquals("abcd", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("ab");
    consumer.addIdentifier("cd");
    assertEquals("abcd", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("a");
    consumer.addOp("+", true);
    consumer.append("b");
    assertEquals("a + b", consumer.getCode());
  }

  @Test
  public void testAddOpWithSpace() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("a");
    consumer.addOp("+", true);
    consumer.appendSpace();
    consumer.append("b");
    assertEquals("a + b", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("a");
    consumer.addNumber(1.23);
    assertEquals("a 1.23", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {}\n");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return 'a';
  }

  @Override
  void append(String str) {
    super.append(str);
  }

  @Override
  void appendBlockStart() {
    super.appendBlockStart();
  }

  @Override
  void appendBlockEnd() {
    super.appendBlockEnd();
  }

  @Override
  void startNewLine() {
    super.startNewLine();
  }

  @Override
  void maybeLineBreak() {
    super.maybeLineBreak();
  }

  @Override
  void maybeCutLine() {
    super.maybeCutLine();
  }

  @Override
  void endLine() {
    super.endLine();
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
  }

  @Override
  void beginBlock() {
    super.beginBlock();
  }

  @Override
  void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
  }

  @Override
  void listSeparator() {
    super.listSeparator();
  }

  @Override
  void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
  }

  @Override
  void appendOp(String op, boolean binOp) {
    super.appendOp(op, binOp);
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return false;
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return false;
  }

  @Override
  void endFile() {
    super.endFile();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
  }

  @Override
  void appendSpace() {
    super.append(" ");
  }

  @Override
  String getCode() {
    return super.getCode();
  }
}