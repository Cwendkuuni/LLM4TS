import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newNode("someNode");
    consumer.startSourceMapping(node);
    // add assertions here to check the behavior of startSourceMapping
    consumer.endSourceMapping(node);
  }

  @org.junit.Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newNode("someNode");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // add assertions here to check the behavior of endSourceMapping
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    // add assertions here to check the behavior of continueProcessing
    consumer.setContinueProcessing(false);
    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    char lastChar = consumer.getLastChar();
    assertEquals('t', lastChar);
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    consumer.append("newLine");
    String expected = "test\nnewLine";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.append("newLine");
    String expected = "test\nnewLine";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    consumer.append("newLine");
    String expected = "test\nnewLine";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    String expected = "test\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.notePreferredLineBreak();
    String expected = "test\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expected = "{{\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndBlockWithNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    String expected = "{\ntest\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.listSeparator();
    consumer.append("newTest");
    String expected = "test,\nnewTest\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndStatementWithNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    String expected = "test;\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function testFunction() {");
    consumer.append("return 1;");
    consumer.endFunction();
    String expected = "{\nfunction testFunction() {\n  return 1;\n}\n";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true) {");
    consumer.beginBlock();
    consumer.append("function testFunction() {");
    consumer.append("return 1;");
    consumer.endFunction(true);
    consumer.append("}");
    consumer.endBlock();
    String expected = "if (true) {\n  {\n    function testFunction() {\n      return 1;\n    }\n  }\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (1) {");
    consumer.beginCaseBody();
    consumer.append("return 1;");
    consumer.endCaseBody();
    consumer.append("}");
    String expected = "{\nswitch (1) {\n  case 1:\n    return 1;\n  }\n}";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add("newTest");
    String expected = "testnewTest";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append("newTest");
    String expected = "test + newTest";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(1.5);
    String expected = "test1.5";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(-0);
    boolean result = consumer.isNegativeZero(consumer.getLastNumber());
    assertTrue(result);
  }

  @org.junit.Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    for (char ch = 'a'; ch <= 'z'; ch++) {
      consumer.append(String.valueOf(ch));
      assertTrue(consumer.isWordChar(consumer.getLastChar()));
    }
    for (char ch = '0'; ch <= '9'; ch++) {
      consumer.append(String.valueOf(ch));
      assertTrue(consumer.isWordChar(consumer.getLastChar()));
    }
    consumer.append("_");
    assertTrue(consumer.isWordChar(consumer.getLastChar()));
    consumer.append("$");
    assertTrue(consumer.isWordChar(consumer.getLastChar()));
    consumer.append(" ");
    assertFalse(consumer.isWordChar(consumer.getLastChar()));
    consumer.append("\\");
    assertTrue(consumer.isWordChar(consumer.getLastChar()));
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node statement = Node.newNode("statement");
    Node expression = Node.newNode("expression");
    Node ifStatement = Node.newExpressionStatement(expression);
    Node forStatement = Node.newForStatement(statement, null, null, null);
    consumer.setShouldPreserveExtraBlocks(true);
    consumer.appendBlockStart();
    consumer.append(ifStatement.toSource());
    consumer.appendBlockStart();
    consumer.append(forStatement.toSource());
    consumer.appendBlockEnd();
    String expected = "{%s\n{\n%s\n}\n{\n%s\n}\n}";
    String actual = consumer.getCode();
    String formatted = String.format(expected, ifStatement.toSource(), ifStatement.toSource(), forStatement.toSource());
    assertEquals(formatted, actual);
  }

  @org.junit.Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
    Node statement = Node.newVariableDeclarationStatement(Node.newArrayType("int", 1), Node.newIdentifier("arr"), Node.newArrayInitializer(Node.newNumber(1)));
    assertFalse(consumer.breakAfterBlockFor(statement, false));
    assertFalse(consumer.breakAfterBlockFor(statement, true));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endFile();
    String expected = "test";
    String actual = consumer.getCode();
    assertEquals(expected, actual);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  void startSourceMapping(Node node) {
    // add implementation here
  }

  @Override
  void endSourceMapping(Node node) {
    // add implementation here
  }

  @Override
  boolean continueProcessing() {
    // add implementation here
  }

  @Override
  char getLastChar() {
    return super.getLastChar();
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
  void endBlock() {
    super.endBlock();
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
  void endStatement() {
    super.endStatement();
  }

  @Override
  void endStatement(boolean needSemiColon) {
    super.endStatement(needSemiColon);
  }

  @Override
  void maybeEndStatement() {
    super.maybeEndStatement();
  }

  @Override
  void endFunction() {
    super.endFunction();
  }

  @Override
  void endFunction(boolean statementContext) {
    super.endFunction(statementContext);
  }

  @Override
  void beginCaseBody() {
    super.beginCaseBody();
  }

  @Override
  void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
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
    return true;
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return true;
  }

  @Override
  void endFile() {
    super.endFile();
  }
}