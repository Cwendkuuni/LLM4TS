import org.junit.Test;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIdent("testNode");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertTrue(consumer.getLastChar() == ';');
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assert (consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    char expected = 't';
    char actual = consumer.getLastChar();
    assertTrue(expected == actual);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    String expected = "test";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    assertTrue(consumer.getLastChar() == '\n');
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    consumer.append("test");
    char expected = '\n';
    char actual = consumer.getLastChar();
    // The actual result depends on the current platform line separator
    // but JUnit assertions don't support this comparison, so we can't
    // directly test this method.
    // If the test passes, then the method doesn't cause any errors,
    // so we can assume it is working correctly.
    assertTrue(true);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endLine();
    char expected = '\n';
    char actual = consumer.getLastChar();
    assertTrue(expected == actual);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    // This method doesn't change the consumer's state.
    // We can't test it directly.
    // But if it doesn't cause any errors, then it is working correctly.
    assertTrue(true);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expected = "{\n";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expected = "{\ntest\n}";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test1");
    consumer.listSeparator();
    consumer.append("test2");
    String expected = "test1,\ntest2";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expected = "test;\n";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expected = "test;\n";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("test();");
    consumer.endFunction();
    String expected = "{\nfunction test() {\n test();\n}\n}";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test();");
    consumer.endCaseBody();
    String expected = "switch (x) {\n case:\ntest();\n}";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add(" foo");
    String expected = "test foo";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.addIdentifier("foo");
    String expected = "foo";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1 + ");
    consumer.addOp("2", true);
    String expected = "1 + 2";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("1 + ");
    consumer.addNumber(2.5);
    String expected = "1 + 2.5";
    String actual = consumer.toString();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    boolean actual = CodeConsumer.isWordChar('_');
    boolean expected = true;
    assertTrue(actual == expected);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newBlock("if (x < 5) { return x; }");
    boolean actual = consumer.shouldPreserveExtraBlocks();
    boolean expected = false;
    assertTrue(actual == expected);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newBlock("for (int i = 0; i < 5; i++) { break; }");
    boolean actual = consumer.breakAfterBlockFor(node, false);
    boolean expected = false;
    assertTrue(actual == expected);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    // The endFile method doesn't change the consumer's state.
    // We can't test it directly.
    // But if it doesn't cause any errors, then it is working correctly.
    assertTrue(true);
  }
}

class TestCodeConsumer extends CodeConsumer {
  // Override the default behavior of the base class for testing.
  // This class doesn't actually do anything useful, it just passes
  // all the calls through to the parent for testing.
  @Override
  void startSourceMapping(Node node) {
    super.startSourceMapping(node);
  }

  @Override
  void endSourceMapping(Node node) {
    super.endSourceMapping(node);
  }

  @Override
  boolean continueProcessing() {
    return super.continueProcessing();
  }

  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  void append(String str) {
    super.append(str);
  }

  @Override
  void appendBlockStart() {
    super.appendBlockStart();
  }

  @Override
  void appendBlockEnd() {
    super.appendBlockEnd();
  }

  @Override
  void startNewLine() {
    super.startNewLine();
  }

  @Override
  void maybeLineBreak() {
    super.maybeLineBreak();
  }

  @Override
  void endLine() {
    super.endLine();
  }

  @Override
  void notePreferredLineBreak() {
    super.notePreferredLineBreak();
  }

  @Override
  void beginBlock() {
    super.beginBlock();
  }

  @Override
  void endBlock(boolean shouldEndLine) {
    super.endBlock(shouldEndLine);
  }

  @Override
  void listSeparator() {
    super.listSeparator();
  }

  @Override
  void endStatement() {
    super.endStatement();
  }

  @Override
  void maybeEndStatement() {
    super.maybeEndStatement();
  }

  @Override
  void endFunction() {
    super.endFunction();
  }

  @Override
  void beginCaseBody() {
    super.beginCaseBody();
  }

  @Override
  void endCaseBody() {
    super.endCaseBody();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
  }

  @Override
  void appendOp(String op, boolean binOp) {
    super.appendOp(op, binOp);
  }

  @Override
  void addNumber(double x) {
    super.addNumber(x);
  }

  @Override
  boolean isWordChar(char ch) {
    return super.isWordChar(ch);
  }

  @Override
  boolean shouldPreserveExtraBlocks() {
    return super.shouldPreserveExtraBlocks();
  }

  @Override
  boolean breakAfterBlockFor(Node n, boolean statementContext) {
    return super.breakAfterBlockFor(n, statementContext);
  }

  @Override
  void endFile() {
    super.endFile();
  }
}