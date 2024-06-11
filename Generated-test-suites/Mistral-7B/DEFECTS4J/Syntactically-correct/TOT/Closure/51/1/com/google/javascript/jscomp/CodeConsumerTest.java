import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new MyCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = Node.newNode("someNode");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    consumer.continueProcessing = () -> false;
    boolean result = consumer.continueProcessing();
    assertEquals(false, result);
  }

  @Test
  public void testGetLastChar() {
    consumer.append("abc");
    char result = consumer.getLastChar();
    assertEquals('c', result);
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    String expected = "hello";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    String expected = "{\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    String expected = "}\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    String expected = "\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    String expected = "\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    String expected = "\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    String expected = "{\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testListSeparator() {
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expected = "test1,\ntest2";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testEndStatement() {
    consumer.append("test = 5;");
    consumer.endStatement();
    String expected = "test = 5;\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.append("test = 5;");
    consumer.maybeEndStatement();
    consumer.append("test2 = 10;");
    String expected = "test = 5;\ntest2 = 10;\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.sawFunction = false;
    consumer.endFunction();
    String expected = "function test() {\n}";
    String result = consumer.getCode();
    assertEquals(expected, result);
    assertEquals(false, consumer.sawFunction);

    consumer.sawFunction = true;
    consumer.endFunction(true);
    expected = "function test() {\n}\n";
    result = consumer.getCode();
    assertEquals(expected, result);
    assertEquals(true, consumer.sawFunction);
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test = 5;");
    consumer.endCaseBody();
    String expected = "switch (x) {\n  case:\ntest = 5;\n}";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testEndCaseBody() {
  }

  @Test
  public void testAddIdentifier() {
    consumer.append("test = ");
    consumer.addIdentifier("testVariable");
    String expected = "test = testVariable;\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testAddOp() {
    consumer.append("test = ");
    consumer.addOp("+ 5", false);
    String expected = "test = + 5;\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testAddNumber() {
    consumer.append("test = ");
    consumer.addNumber(5.5);
    String expected = "test = 5.5;\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

  @Test
  public void testIsWordChar() {
    for (char c = 'A'; c <= 'Z'; c++) {
      assertEquals(true, CodeConsumer.isWordChar(c));
    }

    for (char c = 'a'; c <= 'z'; c++) {
      assertEquals(true, CodeConsumer.isWordChar(c));
    }

    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));

    for (char c = '0'; c <= '9'; c++) {
      assertEquals(true, CodeConsumer.isWordChar(c));
    }

    assertEquals(false, CodeConsumer.isWordChar('.'));
    assertEquals(false, CodeConsumer.isWordChar(','));
    assertEquals(false, CodeConsumer.isWordChar('('));
    assertEquals(false, CodeConsumer.isWordChar(')'));
    assertEquals(false, CodeConsumer.isWordChar('{'));
    assertEquals(false, CodeConsumer.isWordChar('}'));
    assertEquals(false, CodeConsumer.isWordChar(';'));
    assertEquals(false, CodeConsumer.isWordChar('/'));
    assertEquals(false, CodeConsumer.isWordChar('\\'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertEquals(false, consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = Node.newBlock("{ test(); }");
    boolean result = consumer.breakAfterBlockFor(node, false);
    assertEquals(false, result);

    result = consumer.breakAfterBlockFor(node, true);
    assertEquals(true, result);
  }

  @Test
  public void testEndFile() {
    consumer.append("// Some code...");
    consumer.endFile();
    String expected = "// Some code...\n";
    String result = consumer.getCode();
    assertEquals(expected, result);
  }

}

class MyCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  void getCode() {
    // Do nothing
  }
}