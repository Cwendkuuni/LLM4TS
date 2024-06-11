import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verify no output
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Verify no output
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    assertEquals("abc", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.maybeLineBreak();
    assertEquals("ab\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.maybeCutLine();
    assertEquals("ab", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock();
    assertEquals("{\nabc}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithShouldEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("abc");
    consumer.endBlock(true);
    assertEquals("{\nabc}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.listSeparator();
    assertEquals("ab,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.endStatement();
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.endStatement(true);
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.maybeEndStatement();
    assertEquals("abc;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {\nreturn 1;\n}");
    consumer.endFunction(true);
    assertEquals("{\nfunction foo() {\n  return 1;\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {\ncase 1:\n");
    consumer.beginCaseBody();
    consumer.append("return 1;\n");
    consumer.endCaseBody();
    assertEquals("switch (x) {\ncase 1:\n  return 1;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.add("cd");
    assertEquals("abcd", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1 + 1");
    consumer.addOp("+", true);
    assertEquals("1 + 1 +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("x = ");
    consumer.addNumber(1.23);
    assertEquals("x = 1.23", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    node.setType(Node.BLOCK);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    @Override
    public String getCode() {
      return code;
    }

    private String code = "";

    @Override
    abstract void add(String newcode);

    @Override
    void append(String str) {
      code += str;
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    void endBlock(boolean shouldEndLine) {
      endBlock();
      if (shouldEndLine) {
        endLine();
      }
    }

    @Override
    void endBlock() {
      code += "}\n";
    }

    @Override
    void endFile() {
      code += "}";
    }

    @Override
    void endFunction(boolean statementContext) {
      endFunction();
      if (statementContext) {
        endLine();
      }
    }

    @Override
    void endFunction() {
      code += "}\n";
    }

    @Override
    void endLine() {
      code += "\n";
    }

    @Override
    void notePreferredLineBreak() {
      // Do nothing
    }

    @Override
    void beginBlock() {
      code += "{\n";
    }

    @Override
    void beginCaseBody() {
      code += ":";
    }

    @Override
    void endCaseBody() {
      // Do nothing
    }

    @Override
    void add(String newcode) {
      code += newcode;
    }

    @Override
    void addIdentifier(String identifier) {
      code += identifier;
    }

    @Override
    void addNumber(double x) {
      code += String.valueOf(x);
    }

    @Override
    void addOp(String op, boolean binOp) {
      code += op;
    }

    @Override
    void appendBlockStart() {
      code += "{";
    }

    @Override
    void appendBlockEnd() {
      code += "}";
    }

    @Override
    void appendOp(String op, boolean binOp) {
      code += op;
    }

    @Override
    void startSourceMapping(Node node) {
      // Do nothing
    }

    @Override
    void endSourceMapping(Node node) {
      // Do nothing
    }

    @Override
    boolean continueProcessing() {
      return true;
    }

    @Override
    void maybeLineBreak() {
      // Do nothing
    }

    @Override
    void maybeCutLine() {
      // Do nothing
    }

    @Override
    void listSeparator() {
      // Do nothing
    }

    @Override
    void maybeEndStatement() {
      // Do nothing
    }

    @Override
    void endStatement(boolean needSemiColon) {
      // Do nothing
    }

    @Override
    void startNewLine() {
      // Do nothing
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

}