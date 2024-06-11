import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

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
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endStatement();
    assertEquals(";\n", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeEndStatement();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @org.junit.Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("test");
    assertEquals("test", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addOp("+", false);
    assertEquals("+", consumer.getCode());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(123.45);
    assertEquals("123.45\n", consumer.getCode());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = mock(Node.class);
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  static class StubCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    void add(String s) {
      code.append(s);
    }

    @Override
    void append(String s) {
      code.append(s);
    }

    @Override
    char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    String getCode() {
      return code.toString();
    }
  }
}