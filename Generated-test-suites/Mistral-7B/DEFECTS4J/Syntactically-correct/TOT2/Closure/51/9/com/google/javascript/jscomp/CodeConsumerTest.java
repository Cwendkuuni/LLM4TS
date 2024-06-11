import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {
  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("hello world");
    assertEquals('r', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello world");
    String actual = consumer.getCode();
    assertEquals("hello world", actual);
  }

  @Test
  public void testAppendBlockStartAndAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("function test() {");
    consumer.appendBlockEnd();
    String actual = consumer.getCode();
    assertEquals("function test() {\n", actual);
  }

  @Test
  public void testStartNewLineAndEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello ");
    consumer.startNewLine();
    consumer.append("world");
    String actual = consumer.getCode();
    assertEquals("hello \nworld", actual);
  }

  @Test
  public void testMaybeLineBreakAndMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("hello world");
    consumer.maybeLineBreak();
    consumer.maybeCutLine();
    String actual = consumer.getCode();
    assertEquals("hello world\n", actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String actual = consumer.getCode();
    assertEquals("\n", actual);
  }

  @Test
  public void testBeginBlockAndEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.endBlock();
    String actual = consumer.getCode();
    assertEquals("function test() {\n}", actual);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    consumer.listSeparator();
    consumer.addIdentifier("function");
    String actual = consumer.getCode();
    assertEquals("test,\nfunction", actual);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int a = 5;");
    consumer.endStatement();
    String actual = consumer.getCode();
    assertEquals("int a = 5;\n", actual);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("return 5;");
    consumer.endFunction();
    String actual = consumer.getCode();
    assertEquals("function test() {\n  return 5;\n}", actual);
  }

  @Test
  public void testBeginCaseBodyAndEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("case 5:");
    consumer.append("break;");
    consumer.endCaseBody();
    consumer.append("}");
    String actual = consumer.getCode();
    assertEquals("switch (x) {\n  case 5:\n    break;\n}", actual);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("test");
    String actual = consumer.getCode();
    assertEquals("test", actual);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+");
    String actual = consumer.getCode();
    assertEquals("+", actual);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(5.5);
    String actual = consumer.getCode();
    assertEquals("5.5", actual);
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('1'));
    assertFalse(isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(new TestCodeConsumer().shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    assertFalse(new TestCodeConsumer().breakAfterBlockFor(null, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
  }

  @Test(expected = AssertionError.class)
  public void testContinueProcessingException() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
    consumer.append("return 5;");
    consumer.endStatement();
    consumer.add("x = 10;");
    consumer.endBlock();
    consumer.endFile();
  }

  @Test(expected = AssertionError.class)
  public void testAppendException() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("function test() {");
    consumer.append("return 5;");
    consumer.appendBlockEnd();
    consumer.add("x = 10;");
  }

  @Test(expected = AssertionError.class)
  public void testGetLastCharException() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("function test() {");
    consumer.append("return 5;");
    consumer.append("}");
    consumer.add("x = 10;");
    consumer.endStatement();
    consumer.endBlock();
    consumer.endFile();
    consumer.getLastChar();
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  void add(String newcode) {
    super.add(newcode);
  }

  @Override
  String getCode() {
    return super.code;
  }
}