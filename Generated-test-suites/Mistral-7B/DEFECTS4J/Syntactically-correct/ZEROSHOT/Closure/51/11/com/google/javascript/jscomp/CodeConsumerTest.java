import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    assertNotNull(consumer.getSourceMapping());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertNull(consumer.getSourceMapping());
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    assertEquals('o', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());
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
    consumer.append("hello");
    consumer.maybeLineBreak();
    assertEquals("hello\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("helloworld");
    consumer.maybeCutLine();
    assertEquals("helloworld\n", consumer.getCode());
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
    assertTrue(consumer.shouldBreakLine());
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
    consumer.appendBlockStart();
    consumer.append("hello");
    consumer.endBlock();
    assertEquals("{\nhello}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("hello");
    consumer.endBlock(true);
    assertEquals("{\nhello}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    assertEquals("hello,\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithNewline() {
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
    assertEquals("hello;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function hello() {");
    consumer.append("hello();");
    consumer.endFunction();
    assertEquals("function hello() {\nhello();\n}", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithNewline() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function hello() {");
    consumer.append("hello();");
    consumer.endFunction(true);
    assertEquals("function hello() {\nhello();\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 0:");
    consumer.beginCaseBody();
    assertEquals("case 0:\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("case 0:");
    consumer.beginCaseBody();
    consumer.append("hello");
    consumer.endCaseBody();
    assertEquals("case 0:\nhello\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.add(" world");
    assertEquals("hello world\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x + y");
    consumer.addOp("+", true);
    assertEquals("x + y +\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("x = ");
    consumer.addNumber(1.23);
    assertEquals("x = 1.23\n", consumer.getCode());
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('$'));
    assertTrue(CodeConsumer.isWordChar('5'));
    assertFalse(CodeConsumer.isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new DummyCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new DummyCodeConsumer();
    Node node = new Node();
    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testStatementNeedsEnded() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    assertFalse(consumer.statementNeedsEnded());
    consumer.append("world");
    assertTrue(consumer.statementNeedsEnded());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new DummyCodeConsumer();
    consumer.append("function hello() {");
    consumer.endFunction();
    assertTrue(consumer.sawFunction());
  }

}

class DummyCodeConsumer extends CodeConsumer {
  private static final String CODE = "";

  @Override
  public void append(String str) {
    super.append(str);
    getCode().concat(str);
  }

  @Override
  public void add(String newcode) {
    super.add(newcode);
    getCode().concat(newcode);
  }

  @Override
  public void addOp(String op, boolean binOp) {
    super.addOp(op, binOp);
    getCode().concat(op);
  }

  @Override
  public void addNumber(double x) {
    super.addNumber(x);
    getCode().concat(String.valueOf(x));
  }

  @Override
  public char getLastChar() {
    return getCode().charAt(getCode().length() - 1);
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public SourceMapping getSourceMapping() {
    return null;
  }
}