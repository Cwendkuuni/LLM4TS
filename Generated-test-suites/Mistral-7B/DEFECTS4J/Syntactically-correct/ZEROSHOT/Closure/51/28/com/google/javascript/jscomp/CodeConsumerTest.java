import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verification goes here
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Verification goes here
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());

    consumer.statementNeedsEnded = false;
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("123");
    assertEquals('3', consumer.getLastChar());
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
    consumer.appendBlockStart();
    consumer.append("some code");
    consumer.appendBlockEnd();
    assertEquals("{\nsome code}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("hello");
    consumer.startNewLine();
    consumer.append("world");
    assertEquals("hello\nworld", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("hello");
    consumer.maybeLineBreak();
    consumer.append("world");
    assertEquals("hello\nworld", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("helloworld");
    consumer.maybeCutLine();
    consumer.append("new line");
    assertEquals("helloworld\nnew line", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.append("hello");
    consumer.endLine();
    consumer.append("world");
    assertEquals("hello\nworld", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("hello");
    consumer.notePreferredLineBreak();
    consumer.append("world");
    assertEquals("hello\nworld", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("some code");
    consumer.endBlock();
    assertEquals("{\nsome code}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithEndLine() {
    consumer.beginBlock();
    consumer.append("some code");
    consumer.endBlock(true);
    assertEquals("{\nsome code}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("1");
    consumer.listSeparator();
    consumer.append("2");
    assertEquals("1,\n2", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("if (x == y) ");
    consumer.endStatement();
    consumer.append("return 1;");
    consumer.endStatement();
    consumer.append("}");
    assertEquals("if (x == y) \nreturn 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("if (x == y) ");
    consumer.endStatement(true);
    consumer.append("return 1;");
    consumer.endStatement();
    consumer.append("}");
    assertEquals("if (x == y) \nreturn 1;\n}", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("if (x == y) ");
    consumer.maybeEndStatement();
    consumer.append("return 1;");
    consumer.maybeEndStatement();
    consumer.append("}");
    assertEquals("if (x == y) \nreturn 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    consumer.append("}");
    assertEquals("{\nfunction foo() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction(true);
    consumer.append("}");
    assertEquals("{\nfunction foo() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {\n  case 1:\n    ");
    consumer.beginCaseBody();
    consumer.append("break;\n  }");
    assertEquals("switch (x) {\n  case 1:\n    \n  }\n}", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("switch (x) {\n  case 1:\n    ");
    consumer.beginCaseBody();
    consumer.append("break;\n  }");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 1:\n    \n  }\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("hello");
    consumer.add(" world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.append("x = ");
    consumer.addIdentifier("foo");
    assertEquals("x = foo", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    consumer.append("2 + ");
    consumer.addOp("3", false);
    assertEquals("2 + 3", consumer.getCode());
  }

  @Test
  public void testAddOpWithBinOp() {
    consumer.append("2 + ");
    consumer.addOp("3", true);
    assertEquals("2 + 3", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("x = ");
    consumer.addNumber(2.5);
    assertEquals("x = 2.5", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());

    consumer = new TestCodeConsumerWithShouldPreserveExtraBlocksFalse();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
    Node node = new Node();
    node.setType(Node.BLOCK);
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getCode().charAt(super.getCode().length() - 1);
  }

  @Override
  public String getCode() {
    return "";
  }
}

class TestCodeConsumerWithShouldPreserveExtraBlocksFalse extends CodeConsumer {
  @Override
  public boolean shouldPreserveExtraBlocks() {
    return false;
  }
}