import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assertions not possible here as the implementation is abstract
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assertions not possible here as the implementation is abstract
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
    // Assertions not possible here as the implementation is abstract
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    // Assertions not possible here as the implementation is abstract
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("statement");
    consumer.endBlock();
    assertEquals("{\nstatement\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    consumer.beginBlock();
    consumer.append("statement");
    consumer.endBlock(true);
    assertEquals("{\nstatement\n}", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    consumer.append("statement1");
    consumer.listSeparator();
    consumer.append("statement2");
    assertEquals("statement1,\nstatement2\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("statement");
    consumer.endStatement();
    assertEquals("statement;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    consumer.append("statement;");
    consumer.endStatement();
    assertEquals("statement;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("statement");
    consumer.maybeEndStatement();
    assertEquals("statement\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function FunctionName() {");
    consumer.append("statement1");
    consumer.endFunction();
    assertEquals("function FunctionName() {\nstatement1\n}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithNewline() {
    consumer.beginBlock();
    consumer.append("function FunctionName() {");
    consumer.append("statement1");
    consumer.endFunction(true);
    assertEquals("function FunctionName() {\nstatement1\n}", consumer.getCode());
    assertEquals('\n', consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("case label:");
    consumer.beginCaseBody();
    consumer.append("statement");
    consumer.endCaseBody();
    assertEquals("case label:\nstatement\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("statement1");
    consumer.add("statement2");
    assertEquals("statement1statement2\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("statement1");
    consumer.addOp("+", true);
    consumer.append("statement2");
    assertEquals("statement1 + statement2\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("statement1");
    consumer.addNumber(123.456);
    assertEquals("statement1 123.456\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('0'));
    assertFalse(isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = new Node();
    node.setType(Node.FOR_STMT);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    Node node = new Node();
    node.setType(Node.FOR_STMT);
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testGetCode() {
    consumer.append("statement");
    String code = consumer.getCode();
    assertEquals("statement\n", code);
  }

  @Test
  public void testSawFunction() {
    consumer.beginBlock();
    consumer.append("function FunctionName() {");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
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