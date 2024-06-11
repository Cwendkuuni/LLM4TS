import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

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
    consumer.append("abcd");
    assertEquals('d', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abcdefg");
    String expected = "abcdefg";
    for (int i = 0; i < expected.length(); i++) {
      assertEquals(expected.charAt(i), consumer.getLastChar());
    }
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    consumer.append("abc");
    String expected = "\nabc";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.maybeLineBreak();
    consumer.append("b");
    String expected = "a\nb";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.maybeCutLine();
    consumer.append("cd");
    String expected = "ac\nd";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.endLine();
    String expected = "ab\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.append("ab");
    String expected = "\nab";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expected = "{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.endBlock();
    String expected = "}\nab";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.endBlock(true);
    String expected = "}\n\nab";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("ab");
    consumer.listSeparator();
    consumer.append("cd");
    String expected = "ab,\ncd";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.endStatement();
    String expected = "a;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementNoSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.endStatement(false);
    String expected = "a";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.maybeEndStatement();
    String expected = "a";
    assertEquals(expected, consumer.getCode());

    consumer.append("b");
    consumer.maybeEndStatement();
    String expectedCode = "a;\nb";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.append("}");
    consumer.endFunction();
    String expected = "function foo() {})";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function foo() {");
    consumer.append("a;");
    consumer.endFunction(true);
    String expected = "function foo() {\na;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    String expected = "case 1:\n{";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case 1:");
    consumer.beginCaseBody();
    consumer.append("a;");
    consumer.endCaseBody();
    String expected = "case 1:\n{\na;\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.add("b");
    String expected = "ab";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.addIdentifier("foo");
    String expected = "afoo";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.addOp("+", true);
    consumer.append("b");
    String expected = "a+b";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("a");
    consumer.addNumber(1.23);
    String expected = "a1.23";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('A'));
    assertTrue(isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    TestCodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    node.setType(Node.ROOT);
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  String getCode() {
    return super.getCode();
  }
}