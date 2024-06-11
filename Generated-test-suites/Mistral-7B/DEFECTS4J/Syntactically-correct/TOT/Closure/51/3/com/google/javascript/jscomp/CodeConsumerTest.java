import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    // No verification needed, as there are no side effects in this method
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
    // No verification needed, as there are no side effects in this method
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();

    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testContinueProcessing_false() {
    CodeConsumer consumer = new TestCodeConsumerWithContinueProcessingFalse();

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.append("world");

    char lastChar = consumer.getLastChar();
    assertEquals('r', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.append("world");

    String expectedCode = "helloworld";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    String expectedCode = "{";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();

    String expectedCode = "{hello}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.startNewLine();

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.maybeLineBreak();

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("helloworld");
    consumer.maybeCutLine();

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.endLine();

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.notePreferredLineBreak();

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();

    String expectedCode = "{helloworld}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.beginBlock();
    consumer.append("world");
    consumer.endBlock();

    String expectedCode = "helloworld";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("one");
    consumer.listSeparator();
    consumer.append("two");

    String expectedCode = "onetwo\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.endStatement();
    consumer.append("world");

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("hello");
    consumer.maybeEndStatement();
    consumer.append("world");

    String expectedCode = "helloworld\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("function foo() {");
    consumer.endFunction();

    String expectedCode = "function foo() {}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("break;");
    consumer.endCaseBody();

    String expectedCode = "case 1:\nbreak;\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("if (x > y) {");
    consumer.append("break;");
    consumer.endCaseBody();

    String expectedCode = "case 1:\nif (x > y) {}\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.add("hello");
    consumer.add("world");

    String expectedCode = "helloworld";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addOp("+");
    consumer.append("1");
    consumer.addOp("+");
    consumer.append("2");

    String expectedCode = "1+1+2";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.addNumber(1.5);

    String expectedCode = "1.5";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertTrue(CodeConsumer.isWordChar('z'));

    assertFalse(CodeConsumer.isWordChar(' '));
    assertFalse(CodeConsumer.isWordChar('!'));
    assertFalse(CodeConsumer.isWordChar('%'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);
    when(node.getParent()).thenReturn(null);

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor_true() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockFor_false() {
    CodeConsumer consumer = new TestCodeConsumer();

    Node node = mock(Node.class);

    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();

    consumer.endFile();

    String expectedCode = "";
    assertEquals(expectedCode, consumer.getCode());
  }

  private String getCode() {
    return new String(testCodeConsumer.getBuffer(), 0, testCodeConsumer.getLength());
  }

  private void assertEquals(String expectedCode, String actualCode) {
    assertEquals(expectedCode, actualCode);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    private final StringBuilder buffer = new StringBuilder();

    @Override
    void append(String str) {
      buffer.append(str);
    }

    @Override
    char getLastChar() {
      return buffer.charAt(buffer.length() - 1);
    }

    @Override
    String getCode() {
      setLength(0);
      return buffer.toString();
    }

    @Override
    void endFile() {
      // Do nothing
    }
  }

  private static class TestCodeConsumerWithContinueProcessingFalse extends TestCodeConsumer {
    @Override
    boolean continueProcessing() {
      return false;
    }
  }
}