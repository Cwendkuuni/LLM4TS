import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

/**
 * Test class for CodeConsumer.
 */
public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // assert nothing
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // assert nothing
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    consumer.endLine();
    assertEquals("identifier", consumer.code);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.code);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.endLine();
    assertEquals("{\n", consumer.code);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\n test\n}\n", consumer.code);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.startNewLine();
    consumer.endLine();
    assertEquals("test\n\n", consumer.code);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.endLine();
    assertEquals("test\n", consumer.code);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeCutLine();
    consumer.endLine();
    assertEquals("test\n", consumer.code);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    assertEquals("test\n", consumer.code);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    consumer.endLine();
    assertEquals("test\n", consumer.code);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\n test\n}\n", consumer.code);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\n test\n}\n", consumer.code);
    consumer.endBlock(true);
    consumer.endLine();
    assertEquals("{\n test\n}\n{\n}", consumer.code);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test1,\n test2\n", consumer.code);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    consumer.endLine();
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    consumer.endLine();
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.maybeEndStatement();
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test1;\n test2", consumer.code);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endFunction();
    consumer.endBlock();
    consumer.endLine();
    assertEquals("{\n test\n}\n", consumer.code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endFunction(true);
    consumer.endLine();
    assertEquals("test;\n", consumer.code);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test:");
    consumer.beginCaseBody();
    consumer.append("testBody");
    consumer.endLine();
    assertEquals("test:\n testBody\n", consumer.code);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test:");
    consumer.beginCaseBody();
    consumer.append("testBody");
    consumer.endLine();
    consumer.endCaseBody();
    consumer.endLine();
    assertEquals("test:\n testBody\n", consumer.code);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.add("test2");
    consumer.endLine();
    assertEquals("test1 test2\n", consumer.code);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.addOp("+", true);
    consumer.append("test2");
    consumer.endLine();
    assertEquals("test1 + test2\n", consumer.code);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(123);
    consumer.endLine();
    assertEquals("test 123\n", consumer.code);
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0.0));
    assertEquals(true, isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('z'));
    assertTrue(isWordChar('0'));
    assertTrue(isWordChar('9'));
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar('('));
    assertFalse(isWordChar(')'));
    assertFalse(isWordChar('+'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.code);
  }

  private static class TestCodeConsumer extends CodeConsumer {
    String code = "";
  }
}