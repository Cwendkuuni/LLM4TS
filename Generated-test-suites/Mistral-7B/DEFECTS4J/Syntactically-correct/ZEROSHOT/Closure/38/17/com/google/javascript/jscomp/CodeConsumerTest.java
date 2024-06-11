import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.Node;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessingReturnsFalse() {
    CodeConsumer consumer = new FailingCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertEquals(' ', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello world");
    assertEquals("hello world", consumer.getCode());
  }

  @Test
  public void testAppendString() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello ");
    consumer.append("world");
    assertEquals("helloworld", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.endBlock();
    assertEquals("{\n}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockShouldEndLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock(true);
    assertEquals("{\nhello\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");
    assertEquals("hello,\nworld", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(true);
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.maybeEndStatement();
    consumer.append("world");
    assertEquals("helloworld\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("hello world");
    consumer.endFunction();
    assertEquals("function test() {\nhello world\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function test() {\nhello world\n}");
    consumer.endFunction(true);
    assertEquals("function test() {\nhello world;\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("switch (x) {\ncase 1:\n");
    consumer.beginCaseBody();
    consumer.append("hello world\n");
    consumer.endCaseBody();
    assertEquals("switch (x) {\ncase 1:\nhello world\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello ");
    consumer.add("world");
    assertEquals("helloworld", consumer.getCode());
  }

  @Test
  public void testAddWithPrecedingCharacter() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello ");
    consumer.add("world");
    assertEquals("helloworld", consumer.getCode());

    consumer.getCode().replaceAll("\n", "");
    consumer.append("+");
    consumer.add("world");
    assertEquals("hello +world", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("test ");
    consumer.addIdentifier("var");
    consumer.append(" x;\n");
    assertEquals("test var x;\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForWhenStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.STMT);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForIfStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.IF);
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForForStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.FOR);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  private static class DummyCodeConsumer extends CodeConsumer {
    private final StringBuilder code = new StringBuilder();

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    void add(String newcode) {
      code.append(newcode);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }

  private static class FailingCodeConsumer extends CodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}