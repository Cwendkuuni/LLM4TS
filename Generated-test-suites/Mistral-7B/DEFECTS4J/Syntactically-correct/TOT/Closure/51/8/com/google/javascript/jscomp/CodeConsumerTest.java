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
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    char result = consumer.getLastChar();
    assertEquals('c', result);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("hello world");
    String expected = "hello world";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expected = "{\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expected = "}\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    String expected = "\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    String expected = "";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    String expected = "";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    String expected = "\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expected = "";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expected = "{\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    String expected = "}\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    String expected = ", ";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true)");
    consumer.endStatement();
    String expected = "if (true)\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true);");
    String expected = "if (true)\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true)");
    consumer.maybeEndStatement();
    String expected = "if (true)\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testMaybeEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true);");
    consumer.maybeEndStatement();
    String expected = "if (true)\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append("}");
    consumer.endFunction();
    String expected = "function test() {\n}\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {\n}\n");
    consumer.endFunction(true);
    String expected = "function test() {\n}\n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    String expected = ": \n";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endCaseBody();
    String expected = "";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.add("hello");
    String expected = "hello";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    String expected = "foo";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", false);
    String expected = "+";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAddOpWithBinOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addOp("+", true);
    String expected = "+";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addNumber(1.23);
    String expected = "1.23";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockForTrue() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testBreakAfterBlockForFalse() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expected = "";
    assertTrue(expected.equals(consumer.getCode()));
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {}\n");
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  @Test
  public void testStatementNeedsEnded() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true)");
    consumer.maybeEndStatement();
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testStatementStarted() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("if (true)");
    consumer.maybeEndStatement();
    assertTrue(consumer.statementStarted);
  }
}

class TestCodeConsumer extends CodeConsumer {
  String code = "";

  @Override
  char getLastChar() {
    return code.charAt(code.length() - 1);
  }

  @Override
  String getCode() {
    return code;
  }
}