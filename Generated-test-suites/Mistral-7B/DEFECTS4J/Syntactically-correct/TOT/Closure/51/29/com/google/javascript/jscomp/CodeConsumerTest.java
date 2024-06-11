import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertTrue(consumer.statementStarted);
    assertTrue(consumer.statementNeedsEnded);
    assertFalse(consumer.sawFunction);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("world");
    assertEquals("world", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
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
    consumer.append("hello");
    consumer.maybeLineBreak();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeCutLine();
    assertEquals("hello", consumer.getCode());
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
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();
    assertEquals("{\nhello\n}", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock(true);
    assertEquals("{\nhello\n}", consumer.getCode());
    consumer.endLine();
    assertEquals("{\nhello\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");
    assertEquals("hello,\nworld\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(true);
    assertEquals("hello;\n", consumer.getCode());
    consumer.endLine();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.endStatement();
    consumer.append("}");
    consumer.endFunction();
    assertEquals("{\nfunction foo() {\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("hello");
    consumer.endCaseBody();
    consumer.append("}");
    assertEquals("switch (x) {\n case :\n hello\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("hello");
    assertEquals("hello", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("2 + 2");
    consumer.addOp("+", true);
    assertEquals("2 + 2 +", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(3.14159265359);
    assertEquals("3.14159265359", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('0'));
    assertFalse(CodeConsumer.isWordChar('.'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    node.setType(Node.FOR);
    node.setStart(3);
    node.setEnd(5);
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  private String getCode() {
    return consumer.getCode();
  }
}

class TestCodeConsumer extends CodeConsumer {
  String code = "";

  @Override
  void append(String str) {
    code += str;
  }

  @Override
  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  String getCode() {
    return code;
  }
}