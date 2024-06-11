import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append("test");
    consumer.endSourceMapping(node);
    String expectedSource = "test"; // the source code at the position where startSourceMapping was called
    String actualSource = consumer.getSourceForNode(node); // get the source code generated for the node
    assertEquals(expectedSource, actualSource);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing()); // derived class should return false to stop further processing
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    char lastChar = consumer.getLastChar();
    assertEquals('t', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    String expectedCode = "test";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockStartAndAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expectedCode = "{\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    consumer.append("another test");
    String expectedCode = "test\nother test";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.append("another test");
    String expectedCode = "test\nanother test";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.append("another test");
    consumer.maybeCutLine();
    String expectedCode = "test\nanother test";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    String expectedCode = "test\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.notePreferredLineBreak();
    String expectedCode = "test\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expectedCode = "{\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expectedCode = "{\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expectedCode = "test1,\ntest2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.endStatement();
    String expectedCode = "test1;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.endStatement(true);
    String expectedCode = "test1;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.maybeEndStatement();
    consumer.append("test2");
    String expectedCode = "test1;\ntest2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function testFunction() {");
    consumer.append("return 1;");
    consumer.endFunction();
    String expectedCode = "{\nfunction testFunction() {\n  return 1;\n}\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expectedCode = "switch (x) {\n case:\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expectedCode = "switch (x) {\n case:\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.add("test2");
    String expectedCode = "test1test2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addOp("+", true);
    consumer.append("test2");
    String expectedCode = "test1 + test2";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addNumber(1.0);
    String expectedCode = "test1 1.0";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node singleStatementNode = new Node();
    singleStatementNode.setType(Node.STMT);
    singleStatementNode.setStatementType(Node.EXPRESSION_STATEMENT);
    singleStatementNode.setExpression(new Node("print", new Node("42")));
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertTrue(result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node loopNode = new Node();
    loopNode.setType(Node.STMT);
    loopNode.setStatementType(Node.FOR_STATEMENT);
    Node bodyNode = new Node();
    bodyNode.setType(Node.STMT);
    bodyNode.setStatementType(Node.EXPRESSION_STATEMENT);
    bodyNode.setExpression(new Node("print", new Node("42")));
    loopNode.setBody(bodyNode);
    boolean result = consumer.breakAfterBlockFor(loopNode, true);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.endFile();
    String expectedCode = "test1";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }
}

class TestCodeConsumer extends CodeConsumer {
  // empty implementation
}