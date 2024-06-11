import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Verify that no error is thrown
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Verify that no error is thrown
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertEquals(' ', consumer.getLastChar());
    consumer.append("hello");
    assertEquals('h', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    String expected = "hello";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.startNewLine();
    String expected = "hello\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeLineBreak();
    String expected = "hello\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeCutLine();
    String expected = "hello\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endLine();
    String expected = "hello\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expected = "\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    String expected = "hello{\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    consumer.endBlock();
    String expected = "hello{\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    consumer.endBlock(true);
    String expected = "hello{\n}\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    String expected = "hello,\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    String expected = "hello;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(true);
    String expected = "hello;\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.maybeEndStatement();
    String expected = "hello;";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    consumer.endFunction();
    String expected = "hello{\n}function(){\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    consumer.endFunction(true);
    String expected = "hello{\n}function(){\n}";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginCaseBody();
    String expected = "hello:\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.beginCaseBody();
    consumer.endCaseBody();
    String expected = "hello:\n";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello");
    consumer.add("world");
    String expected = "helloworld";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1");
    consumer.addOp("+", true);
    consumer.append("2");
    String expected = "1+2";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1");
    consumer.addNumber(2.5);
    String expected = "1.5";
    assertEquals(expected, consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testBreakAfterBlockForWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expected = "";
    assertEquals(expected, consumer.getCode());
  }

  private String getCode() {
    return ((TestCodeConsumer) consumer).code;
  }

  private static class TestCodeConsumer extends CodeConsumer {
    String code = "";
  }
}