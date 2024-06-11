import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
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
    consumer.append("abc");
    assertEquals('c', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
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
    consumer.append("abcdefg");
    consumer.maybeLineBreak();
    assertEquals("abcdefg\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcd");
    consumer.maybeCutLine();
    assertEquals("abcd\n", consumer.getCode());
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
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    consumer.listSeparator();
    consumer.append("def");
    assertEquals("abc,\ndef\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.endStatement();
    assertEquals("int x = 1;\n", consumer.getCode());
    consumer.append("int y = 2;\n");
    consumer.endStatement();
    consumer.append("int z = 3;\n");
    consumer.endStatement();
    assertEquals("int x = 1;\nint y = 2;\nint z = 3;\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.maybeEndStatement();
    assertEquals("int x = 1;\n", consumer.getCode());
    consumer.append("int y = 2;\n");
    consumer.maybeEndStatement();
    consumer.append("int z = 3;\n");
    consumer.maybeEndStatement();
    assertEquals("int x = 1;\nint y = 2;\nint z = 3;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function foo() {");
    consumer.appendBlockStart();
    consumer.append("return 1;\n");
    consumer.endBlock();
    consumer.append("}");
    consumer.endFunction();
    assertEquals("function foo() {\nreturn 1;\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {\ncase 1:\n");
    consumer.beginCaseBody();
    consumer.append("return 1;\n");
    consumer.endCaseBody();
    consumer.append("break;\n}");
    assertEquals("switch (x) {\ncase 1:\n  return 1;\n  break;\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.add("int y = 2;\n");
    assertEquals("int x = 1;\nint y = 2;\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.addOp("+", false);
    consumer.append("int y = 2;\n");
    consumer.addOp("+", false);
    consumer.addNumber(3.5);
    assertEquals("int x = 1;\nint y = 2;\nint z = 5.5;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 1;\n");
    consumer.addNumber(2);
    assertEquals("int x = 1;\nint y = 2;\n", consumer.getCode());
    consumer.addNumber(-2.5);
    assertEquals("int x = 1;\nint y = -1.5;\n", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isNegativeZero(-0.0));
    assertFalse(consumer.isNegativeZero(0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('1'));
    assertFalse(consumer.isWordChar(' '));
    assertFalse(consumer.isWordChar('('));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.breakAfterBlockFor(null, false));
    assertFalse(consumer.breakAfterBlockFor(new Node(), false));
    assertTrue(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getCode().charAt(super.getCode().length() - 1);
  }

  @Override
  String getCode() {
    return super.code;
  }
}