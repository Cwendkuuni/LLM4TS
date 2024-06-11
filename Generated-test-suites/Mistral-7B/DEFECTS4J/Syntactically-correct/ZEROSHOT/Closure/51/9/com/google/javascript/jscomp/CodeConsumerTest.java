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
    // Cannot assert on anything here as this method has no return value
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert on anything here as this method has no return value
  }

  @Test
  public void testContinueProcessing() {
    assertFalse(consumer.continueProcessing());
    consumer.continueProcessing();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    consumer.append("hello");
    assertEquals('l', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    consumer.append("world");
    assertEquals("world", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.beginBlock();
    consumer.append("x = 1;");
    consumer.endBlock();
    consumer.appendBlockEnd();
    assertEquals("{\nx = 1;\n}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.append("x = 1;");
    consumer.startNewLine();
    assertEquals("x = 1;\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.append("x = 1;");
    consumer.maybeLineBreak();
    assertTrue(consumer.getCode().endsWith("\n"));
  }

  @Test
  public void testMaybeCutLine() {
    consumer.append("x = 1;");
    consumer.maybeCutLine();
    assertFalse(consumer.getCode().endsWith("\n"));
  }

  @Test
  public void testEndLine() {
    consumer.append("x = 1");
    consumer.endLine();
    assertEquals("x = 1;\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.append("x = 1");
    consumer.notePreferredLineBreak();
    assertEquals("x = 1;\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    consumer.append("x = 1;");
    consumer.endBlock();
    assertEquals("{\nx = 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("x = 1;");
    consumer.endBlock();
    consumer.endBlock();
    assertEquals("{\nx = 1;\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    consumer.beginBlock();
    consumer.append("x = 1;");
    consumer.endBlock(true);
    assertEquals("{\nx = 1;\n}", consumer.getCode());
    consumer.append("y = 2;");
    consumer.endBlock();
    assertEquals("{\nx = 1;\n}y = 2;\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.append("x = 1");
    consumer.listSeparator();
    consumer.append("y = 2");
    assertEquals("x = 1,\ny = 2;\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.append("x = 1;");
    consumer.endStatement();
    assertEquals("x = 1;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    consumer.append("x = 1");
    consumer.endStatement(true);
    assertEquals("x = 1;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("x = 1;");
    consumer.maybeEndStatement();
    consumer.append("y = 2;");
    assertEquals("x = 1;\ny = 2;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.append("x = 1;");
    consumer.endFunction(true);
    assertEquals("{\nfunction foo() {\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("x = 1;");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case:\nx = 1;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.append("x = 1");
    consumer.add(" + 2");
    assertEquals("x = 1 + 2;\n", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    consumer.append("x = 1")
        .addIdentifier("foo")
        .append(";");
    assertEquals("x = 1foo;\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.append("x = 1")
        .addOp("+", true)
        .append("2")
        .append(";");
    assertEquals("x = 1 + 2;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.append("x = 1")
        .addNumber(2.5)
        .append(";");
    assertEquals("x = 1.5;\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    consumer.append("for (x = 0; x < 10; x++) {")
        .append("x++;")
        .append("}")
        .append(";");
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    consumer.append("if (x < 10) {")
        .append("x++;")
        .append("}")
        .append("else {")
        .append("y++;")
        .append("}");
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
    assertFalse(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    consumer.append("function foo() {}\n");
    consumer.endFile();
    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    public char getLastChar() {
      return super.getCode().charAt(super.getCode().length() - 1);
    }

    @Override
    public String getCode() {
      return code;
    }

    private String code;

    @Override
    public void add(String newcode) {
      code += newcode;
    }
  }
}