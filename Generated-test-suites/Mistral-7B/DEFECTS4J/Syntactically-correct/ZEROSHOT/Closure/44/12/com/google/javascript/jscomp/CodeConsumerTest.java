import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
  }

  @org.junit.Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testContinueProcessingWhenFalse() {
    CodeConsumer consumer = new TestCodeConsumerFalse();

    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");

    assertEquals('c', consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("abc");

    assertEquals("abc", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockStart();

    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.appendBlockEnd();

    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.startNewLine();

    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeLineBreak();

    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.maybeCutLine();

    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endLine();

    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();

    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();

    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endBlock();

    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endBlock(true);

    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.listSeparator();

    assertEquals(",\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("foo");
    consumer.endStatement();

    assertEquals("foo;\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("foo");
    consumer.endStatement(true);

    assertEquals("foo;\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("foo");
    consumer.maybeEndStatement();

    assertEquals("foo", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function foo() {");
    consumer.endFunction();

    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function foo() {");
    consumer.endFunction(true);

    assertEquals("function foo() {\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginCaseBody();

    assertEquals(":\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endCaseBody();

    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.add("abc");

    assertEquals("abc", consumer.getCode());
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addIdentifier("identifier");

    assertEquals("identifier", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addOp("+", false);

    assertEquals("+", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOpBin() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addOp("+", true);

    assertEquals("+\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addNumber(1.0);

    assertEquals("1.0\n", consumer.getCode());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getParent()).thenReturn(null);
    when(node.getTag()).thenReturn(Node.FORIN);

    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();

    assertEquals("", consumer.getCode());
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private String code = "";

    @Override
    public void append(String str) {
      super.append(str);
      code += str;
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    public String getCode() {
      return code;
    }
  }

  private static class TestCodeConsumerFalse extends TestCodeConsumer {
    @Override
    public boolean continueProcessing() {
      return false;
    }
  }
}