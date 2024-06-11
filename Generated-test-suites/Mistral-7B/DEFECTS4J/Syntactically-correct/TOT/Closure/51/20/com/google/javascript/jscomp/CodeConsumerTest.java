import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertEquals(consumer.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(node)), consumer.getSourceMapping());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertEquals("", consumer.getSourceMapping());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("hello world");
    assertEquals("hello world", consumer.toString());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.startNewLine();
    consumer.append("efgh");
    assertEquals("abcd\nefgh", consumer.toString());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.maybeLineBreak();
    consumer.append("efgh");
    assertEquals("abcd\nefgh", consumer.toString());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.maybeCutLine();
    consumer.append("efgh");
    assertEquals("abcd\nefgh", consumer.toString());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.endLine();
    consumer.append("efgh");
    assertEquals("abcd\nefgh", consumer.toString());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.append("efgh");
    assertEquals("abcd\nefgh", consumer.toString());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertTrue(consumer.toString().startsWith("{"));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.beginBlock();
    consumer.append("efgh");
    consumer.endBlock();
    assertEquals("abcd{\nefgh}\n", consumer.toString());
  }

  @Test
  public void testEndBlockWithSemicolon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.beginBlock();
    consumer.append("efgh");
    consumer.endBlock(true);
    assertEquals("abcd{\nefgh};\n", consumer.toString());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.listSeparator();
    consumer.append("efgh");
    assertEquals("abcd,\nefgh", consumer.toString());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (condition) {");
    consumer.endStatement();
    consumer.append("}");
    assertEquals("if (condition) {\n}\n", consumer.toString());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (condition) {");
    consumer.endStatement(true);
    consumer.append("}");
    assertEquals("if (condition) {;\n}\n", consumer.toString());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("if (condition) {");
    consumer.append("}");
    consumer.maybeEndStatement();
    consumer.append("else {");
    consumer.append("}");
    assertEquals("if (condition) {}\nelse {}\n", consumer.toString());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {");
    consumer.endFunction();
    consumer.append("}");
    assertEquals("function foo() {\n}\n", consumer.toString());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {");
    consumer.append("return 1;");
    consumer.endFunction(true);
    consumer.append("}");
    assertEquals("function foo() {\n  return 1;\n}\n", consumer.toString());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("switch (expression) {");
    consumer.beginCaseBody();
    consumer.append("case 1:");
    consumer.append("break;");
    consumer.endCaseBody();
    consumer.append("}");
    assertEquals("switch (expression) {\n  case 1:\n}\n", consumer.toString());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.add("efgh");
    assertEquals("abcdefgh", consumer.toString());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("abcd");
    consumer.addIdentifier("foo");
    assertEquals("abcdfoo", consumer.toString());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("3 + 4");
    consumer.addOp("+", true);
    assertEquals("3 + 4 +", consumer.toString());
  }

  @Test
  public void testAddOpWithoutLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("3 + 4");
    consumer.addOp("+", false);
    assertEquals("3+4", consumer.toString());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("123 + ");
    consumer.addNumber(4.5);
    assertEquals("123 + 4.5", consumer.toString());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.toString());
  }

  static class StubCodeConsumer extends CodeConsumer {
    @Override
    char getLastChar() {
      return getSource().charAt(getPosition() - 1);
    }

    @Override
    void add(String newcode) {
      super.add(newcode);
    }

    @Override
    void append(String str) {
      super.append(str);
    }

    @Override
    void appendOp(String op, boolean binOp) {
      super.addOp(op, binOp);
    }

    @Override
    void addNumber(double x) {
      super.addNumber(x);
    }

    @Override
    void addIdentifier(String identifier) {
      super.addIdentifier(identifier);
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
    void startSourceMapping(Node node) {
      super.startSourceMapping(node);
    }

    @Override
    void endSourceMapping(Node node) {
      super.endSourceMapping(node);
    }

    @Override
    boolean continueProcessing() {
      return super.continueProcessing();
    }

    @Override
    String getSourceMapping() {
      return "";
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
    void appendBlockStart() {
      super.appendBlockStart();
    }

    @Override
    void appendBlockEnd() {
      super.appendBlockEnd();
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
  }
}