import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new MyCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    String expectedSource = "1:1-1:5";
    String actualSource = consumer.getSourceMapping(node);
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testContinueProcessing() {
    consumer.statementNeedsEnded = true;
    boolean result = consumer.continueProcessing();
    assertEquals(true, result);

    consumer.statementNeedsEnded = false;
    result = consumer.continueProcessing();
    assertEquals(true, result);

    consumer.statementNeedsEnded = true;
    result = consumer.continueProcessing();
    assertEquals(false, result);
  }

  @Test
  public void testGetLastChar() {
    consumer.append("abc");
    char lastChar = consumer.getLastChar();
    assertEquals('c', lastChar);

    consumer.startNewLine();
    lastChar = consumer.getLastChar();
    assertEquals('\n', lastChar);
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    String expectedResult = "hello";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testEndStatement() {
    consumer.append("if (x > 5)");
    consumer.endStatement();
    String expectedResult = "if (x > 5);";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testBeginBlock() {
    consumer.append("if (x > 5) {");
    String expectedResult = "if (x > 5) {\n";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testEndBlock() {
    consumer.append("if (x > 5) { a = b; }");
    consumer.endBlock();
    String expectedResult = "if (x > 5) {\n  a = b;\n}";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("foo");
    String expectedResult = "foo";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testAddNumber() {
    consumer.addNumber(42.42);
    String expectedResult = "42.42";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testAddOp() {
    consumer.append("1 + 2");
    consumer.addOp("+", true);
    String expectedResult = "1 + 2 +";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testEndFunction() {
    consumer.append("function foo() { a = b; }");
    consumer.endFunction();
    String expectedResult = "function foo() {\n  a = b;\n}";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.append("if (x > 5) { function foo() { a = b; } }");
    consumer.endFunction(true);
    String expectedResult = "if (x > 5) {\n  function foo() {\n    a = b;\n  }\n}";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) { case 1: }");
    consumer.beginCaseBody();
    String expectedResult = "switch (x) {\n  case 1:\n}";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("switch (x) { case 1: a = b; }");
    consumer.endCaseBody();
    String expectedResult = "switch (x) {\n  case 1:\n  a = b;\n}";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer.append("for (int i = 0; i < 5; i++) { x = i; }");
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertEquals(true, result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    Node ifStatement = new Node("IfStatement");
    Node forStatement = new Node("ForStatement");
    node.setFirstChild(ifStatement);
    ifStatement.setFirstChild(new Node("IfExpr", new Node("BinaryOperator", new Node("Identifier", "x"), new Node("Literal", 5), ">")));
    ifStatement.setLastChild(forStatement);
    forStatement.setFirstChild(new Node("ForInit", new Node("VariableDeclarationList", new Node("VariableDeclaration", new Node("Identifier", "i"), new Node("Assign", new Node("Identifier", "i"), new Node("Literal", 0))), new Node("ExpressionStatement", new Node("Assignment", new Node("Identifier", "x"), new Node("Identifier", "i"))))));
    forStatement.setLastChild(new Node("Statement", new Node("ExpressionStatement", new Node("Assignment", new Node("Identifier", "x"), new Node("Literal", 5)))));

    boolean result = consumer.breakAfterBlockFor(node, false);
    assertEquals(false, result);

    result = consumer.breakAfterBlockFor(node, true);
    assertEquals(true, result);
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    String expectedResult = "";
    String actualResult = consumer.getCode();
    assertEquals(expectedResult, actualResult);
  }
}

class MyCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    return super.getCode();
  }

  @Override
  void startNewLine() {
    super.startNewLine();
    endLine();
  }

  @Override
  void maybeCutLine() {
    super.maybeCutLine();
    maybeLineBreak();
  }

  @Override
  void endLine() {
    super.endLine();
    statementNeedsEnded = false;
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
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