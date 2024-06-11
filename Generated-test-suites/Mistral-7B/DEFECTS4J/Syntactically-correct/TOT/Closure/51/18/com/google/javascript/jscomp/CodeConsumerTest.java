import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Verify that startSourceMapping and endSourceMapping were called with the correct node
    assertEquals(node, consumer.lastNode);
  }

  @org.junit.Test
  public void testContinueProcessingReturnsTrueByDefault() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testContinueProcessingReturnsFalseIfDerivedClassReturnsFalse() {
    CodeConsumer consumer = new DerivedStubCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcdef");
    assertEquals("abcdef", consumer.toString());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.toString());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.toString());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.toString());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.maybeLineBreak();
    assertEquals("abcd\n", consumer.toString());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.maybeCutLine();
    assertEquals("abcd", consumer.toString());
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.toString());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.toString());
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("xyz");
    consumer.endBlock();
    assertEquals("{\nxyz}\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("xyz");
    consumer.endBlock(true);
    assertEquals("{\nxyz}\n", consumer.toString());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abc");
    consumer.listSeparator();
    consumer.append("def");
    assertEquals("abc,\ndef", consumer.toString());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.endStatement();
    assertEquals("abcd;\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.endStatement(true);
    assertEquals("abcd;\n", consumer.toString());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.maybeEndStatement();
    assertEquals("abcd;\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("xyz");
    consumer.endFunction();
    assertEquals("function foo() {\n  xyz\n}\n", consumer.toString());
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("xyz");
    consumer.endFunction(true);
    assertEquals("function foo() {\n  xyz\n}\n", consumer.toString());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("xyz");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n case:\n  xyz\n}", consumer.toString());
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.toString());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("a");
    consumer.addOp("+", false);
    consumer.append("b");
    assertEquals("a + b", consumer.toString());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(1);
    assertEquals("1", consumer.toString());
    consumer.addNumber(1.23);
    assertEquals("1.23", consumer.toString());
  }

  @org.junit.Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('b'));
    assertFalse(CodeConsumer.isWordChar(' '));
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocksReturnsFalseByDefault() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockForReturnsTrueWhenStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = Node.newLiteral("statement");
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @org.junit.Test
  public void testBreakAfterBlockForReturnsFalseWhenNotStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = Node.newLiteral("expression");
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }
}

class StubCodeConsumer extends CodeConsumer {
  String lastNodeLabel = "";

  @Override
  void startSourceMapping(Node node) {
    lastNodeLabel = "startSourceMapping: " + node.getClass().getSimpleName();
  }

  @Override
  void endSourceMapping(Node node) {
    lastNodeLabel += " endSourceMapping: " + node.getClass().getSimpleName();
    super.endSourceMapping(node);
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
    lastNodeLabel += " add: " + newcode;
  }

  @Override
  void appendBlockStart() {
    lastNodeLabel += " appendBlockStart";
  }

  @Override
  void appendBlockEnd() {
    lastNodeLabel += " appendBlockEnd";
  }

  @Override
  void startNewLine() {
    lastNodeLabel += " startNewLine";
  }

  @Override
  void maybeLineBreak() {
    lastNodeLabel += " maybeLineBreak";
  }

  @Override
  void maybeCutLine() {
    lastNodeLabel += " maybeCutLine";
  }

  @Override
  void endLine() {
    lastNodeLabel += " endLine";
  }

  @Override
  void notePreferredLineBreak() {
    lastNodeLabel += " notePreferredLineBreak";
  }

  @Override
  void beginBlock() {
    lastNodeLabel += " beginBlock";
  }

  @Override
  void endBlock(boolean shouldEndLine) {
    lastNodeLabel += " endBlock: " + shouldEndLine;
  }

  @Override
  void listSeparator() {
    lastNodeLabel += " listSeparator";
  }

  @Override
  void endStatement() {
    lastNodeLabel += " endStatement";
  }

  @Override
  void endStatement(boolean needSemiColon) {
    lastNodeLabel += " endStatement: " + needSemiColon;
  }

  @Override
  void maybeEndStatement() {
    lastNodeLabel += " maybeEndStatement";
  }

  @Override
  void endFunction() {
    lastNodeLabel += " endFunction";
  }

  @Override
  void endFunction(boolean statementContext) {
    lastNodeLabel += " endFunction: " + statementContext;
  }

  @Override
  void beginCaseBody() {
    lastNodeLabel += " beginCaseBody";
  }

  @Override
  void endCaseBody() {
    lastNodeLabel += " endCaseBody";
  }

  @Override
  void addIdentifier(String identifier) {
    lastNodeLabel += " addIdentifier: " + identifier;
    super.addIdentifier(identifier);
  }

  @Override
  void appendOp(String op, boolean binOp) {
    lastNodeLabel += " appendOp: " + op + ", binOp: " + binOp;
    super.appendOp(op, binOp);
  }

  @Override
  void addOp(String op, boolean binOp) {
    lastNodeLabel += " addOp: " + op + ", binOp: " + binOp;
    super.addOp(op, binOp);
  }

  @Override
  void addNumber(double x) {
    lastNodeLabel += " addNumber: " + x;
    super.addNumber(x);
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return false;
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return statementContext;
  }

  @Override
  void endFile() {
    lastNodeLabel += " endFile";
    super.endFile();
  }

  @Override
  void append(String str) {
    super.append(str);
    lastNodeLabel += " append: " + str;
  }

  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  boolean continueProcessing() {
    return true;
  }

  @Override
  void toString() {
    System.out.println(lastNodeLabel);
  }
}

class DerivedStubCodeConsumer extends StubCodeConsumer {
  @Override
  boolean continueProcessing() {
    return false;
  }
}