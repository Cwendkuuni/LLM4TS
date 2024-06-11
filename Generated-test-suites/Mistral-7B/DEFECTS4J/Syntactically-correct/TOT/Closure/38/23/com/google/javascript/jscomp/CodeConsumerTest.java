import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMappingAndEndSourceMapping() {
    CodeConsumer consumer = new MyCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append("hello");
    consumer.endSourceMapping(node);

    String expectedSourceMap = "hello//# sourceMappingURL=data:application/json";
    String actualSourceMap = consumer.getSourceMap();
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new MyCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    char lastChar = consumer.getLastChar();
    assertEquals('o', lastChar);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    String expectedCode = "hello";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockStartAndBlockEnd() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();
    String expectedCode = "{\nhello\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.startNewLine();
    consumer.append("world");
    String expectedCode = "hello\nworld";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.maybeLineBreak();
    String expectedCode = "hello\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.append("world");
    consumer.maybeCutLine();
    String expectedCode = "hello\nworld";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.endLine();
    String expectedCode = "hello\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.notePreferredLineBreak();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();
    String expectedCode = "{\nhello\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.beginBlock();
    consumer.append("world");
    consumer.endBlock();
    String expectedCode = "hello{\nworld\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.listSeparator();
    consumer.append("world");
    String expectedCode = "hello,\nworld";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement();
    String expectedCode = "hello;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.endStatement(true);
    String expectedCode = "hello;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("hello");
    consumer.maybeEndStatement();
    String expectedCode = "hello";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);

    consumer.append(";");
    consumer.maybeEndStatement();
    expectedCode += ";\n";
    actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.beginBlock();
    consumer.append("function helloWorld() {");
    consumer.append(" return \"hello world\";");
    consumer.endFunction();
    String expectedCode = "{\nfunction helloWorld() {\n return \"hello world\";\n}\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("function helloWorld() {");
    consumer.endFunction(true);
    String expectedCode = "function helloWorld() {\n}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append(" case 1:");
    consumer.append(" return 1;");
    consumer.endCaseBody();
    String expectedCode = "switch (x) {\n case 1:\n return 1;\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append(" case 1:");
    consumer.append(" return 1;");
    consumer.endCaseBody();
    consumer.append(" case 2:");
    String expectedCode = "switch (x) {\n case 1:\n return 1;\n case 2:\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("int a = 1 + ");
    consumer.add("2");
    consumer.append(";");
    String expectedCode = "int a = 1 + 2;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("int a = 1 + ");
    consumer.addOp("2", true);
    consumer.append(";");
    String expectedCode = "int a = 1 + 2;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.append("int a = ");
    consumer.addNumber(1.5);
    consumer.append(";");
    String expectedCode = "int a = 2;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new MyCodeConsumer();
    boolean result = consumer.shouldPreserveExtraBlocks();
    assertFalse(result);
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new MyCodeConsumer();
    Node node = new Node();
    consumer.beginBlock();
    consumer.append("if (condition) {");
    consumer.append(" statement1;");
    boolean result = consumer.breakAfterBlockFor(node, false);
    assertFalse(result);

    consumer.append(" statement2;");
    result = consumer.breakAfterBlockFor(node, true);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new MyCodeConsumer();
    consumer.endFile();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }
}

class MyCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public String getCode() {
    String code = super.getCode();
    if (sawFunction) {
      code = code.substring(0, code.length() - 1);
    }
    return code;
  }

  @Override
  public String getSourceMap() {
    String sourceMap = super.getSourceMap();
    if (sawFunction) {
      sourceMap = sourceMap.substring(0, sourceMap.length() - 1);
    }
    return sourceMap;
  }
}