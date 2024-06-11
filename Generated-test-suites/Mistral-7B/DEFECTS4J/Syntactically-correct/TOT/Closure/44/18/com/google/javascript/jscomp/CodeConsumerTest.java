import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    String expectedSourceMap = "{\"version\":1,\"file\":\"TestFile.js\",\"sources\":[\"TestFile.js\"],\"sourcesContent\":{\"TestFile.js\":\"1\\n\"},\"names\":[],\"mappings\":[";
    String actualSourceMap = consumer.getSourceMap();
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    String expectedSourceMap = "{\"version\":1,\"file\":\"TestFile.js\",\"sources\":[\"TestFile.js\"],\"sourcesContent\":{\"TestFile.js\":\"1\\n\"},\"names\":[],\"mappings\":[]}";
    String actualSourceMap = consumer.getSourceMap();
    assertEquals(expectedSourceMap, actualSourceMap);
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
    char expectedLastChar = 'c';
    char actualLastChar = consumer.getLastChar();
    assertEquals(expectedLastChar, actualLastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("abc");
    String expectedOutput = "abc";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expectedOutput = "{\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expectedOutput = "}\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    String expectedOutput = "\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    String expectedOutput = "";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    String expectedOutput = "\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    String expectedOutput = "\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expectedOutput = "";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expectedOutput = "{\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endBlock();
    String expectedOutput = "\n}";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append(" int x = 42; ");
    consumer.endBlock(true);
    String expectedOutput = "{\nint x = 42;\n}";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    String expectedOutput = ", ";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.endStatement();
    String expectedOutput = "int x = 42;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.endStatement(true);
    String expectedOutput = "int x = 42;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.maybeEndStatement();
    String expectedOutput = "int x = 42;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFunction();
    String expectedOutput = "}";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function testFunction() {\nint x = 42;\n}");
    consumer.endFunction(true);
    String expectedOutput = "function testFunction() {\nint x = 42;\n}\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    String expectedOutput = ": ";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginCaseBody();
    consumer.append("int x = 42;");
    consumer.endCaseBody();
    String expectedOutput = ": \nint x = 42;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.add("int y = 43;");
    String expectedOutput = "int x = 42;\nint y = 43;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.addOp("+", true);
    consumer.append("int y = 43;");
    String expectedOutput = "int x = 42;\nint y = 43;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("int x = 42;");
    consumer.addNumber(43.5);
    String expectedOutput = "int x = 42;\n";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertEquals(false, consumer.isNegativeZero(0.0));
    assertEquals(true, consumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('a'));
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('z'));
    assertTrue(consumer.isWordChar('9'));
    assertFalse(consumer.isWordChar('('));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = Node.newIntLiteral(42);
    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endFile();
    String expectedOutput = "";
    String actualOutput = consumer.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }
}

class TestCodeConsumer extends CodeConsumer {
  private StringBuilder output = new StringBuilder();
  private StringBuilder sourceMap = new StringBuilder();

  @Override
  void append(String str) {
    output.append(str);
  }

  @Override
  void startNewLine() {
    output.append('\n');
  }

  @Override
  void endLine() {
  }

  @Override
  char getLastChar() {
    return output.charAt(output.length() - 1);
  }

  @Override
  void add(String newcode) {
    output.append(newcode);
  }

  @Override
  void appendBlockStart() {
    output.append("{\n");
    sourceMap.append("{\"version\":1,\"file\":\"TestFile.js\",\"sources\":[\"TestFile.js\"],\"sourcesContent\":{\"TestFile.js\":\"");
    sourceMap.append(output.toString());
    sourceMap.append("\"},\"names\":[],\"mappings\":[]}");
  }

  @Override
  void appendBlockEnd() {
    output.append("}\n");
  }

  @Override
  void startSourceMapping(Node node) {
    sourceMap.append(",\"sourcesFile\":\"");
    sourceMap.append(node.getClass().getSimpleName());
    sourceMap.append(".java\",\"line\":");
    sourceMap.append(node.getLineNumber());
    sourceMap.append(",\"column\":");
    sourceMap.append(node.getLineOffsetInFile());
    sourceMap.append("}");
  }

  @Override
  void endSourceMapping(Node node) {
  }

  @Override
  boolean continueProcessing() {
    return true;
  }

  @Override
  String getOutput() {
    String outputString = output.toString();
    output = new StringBuilder();
    return outputString;
  }

  @Override
  String getSourceMap() {
    String sourceMapString = sourceMap.toString();
    sourceMap = new StringBuilder();
    return sourceMapString;
  }
}