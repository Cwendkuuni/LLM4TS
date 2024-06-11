import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Assert that no error was thrown
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Assert that no error was thrown
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    String code = "test";
    consumer.append(code);
    char lastChar = consumer.getLastChar();
    assertEquals(code.charAt(code.length() - 1), lastChar);
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    String identifier = "testIdentifier";
    consumer.addIdentifier(identifier);
    String expectedCode = identifier;
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    String str = "testString";
    consumer.append(str);
    String expectedCode = str;
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockEnd();
    String expectedCode = "}\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.startNewLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeLineBreak();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.maybeCutLine();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.endLine();
    String expectedCode = "\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.notePreferredLineBreak();
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    String expectedCode = "{\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    String expectedCode = "{\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndBlockWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock(true);
    String expectedCode = "{\ntest;\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.listSeparator();
    String expectedCode = ", ";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement();
    String expectedCode = "test;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.endStatement(true);
    String expectedCode = "test;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.maybeEndStatement();
    String expectedCode = "test;\n";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("function testFunction() {}");
    consumer.endFunction();
    String expectedCode = "{\nfunction testFunction() {\n}\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("function testFunction() { test(); }");
    consumer.endFunction(true);
    String expectedCode = "{\nfunction testFunction() {\ntest();\n}\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("case testCase:\n");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    String expectedCode = "case testCase:\n{\ntest\n}";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.add(" String");
    String expectedCode = "test String";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addOp("+", true);
    consumer.append(" String");
    String expectedCode = "test + String";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("test");
    consumer.addNumber(123);
    String expectedCode = "test 123";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testIsNegativeZero() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertEquals(false, consumer.isNegativeZero(0));
    assertEquals(false, consumer.isNegativeZero(-0));
    assertEquals(true, consumer.isNegativeZero(-0.0));
  }

  @Test
  public void testIsWordChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertTrue(consumer.isWordChar('_'));
    assertTrue(consumer.isWordChar('$'));
    assertTrue(consumer.isWordChar('A'));
    assertTrue(consumer.isWordChar('Z'));
    assertTrue(consumer.isWordChar('0'));
    assertTrue(consumer.isWordChar('9'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    node.setType(AbstractSyntaxTree.FOR_STATEMENT);
    assertTrue(consumer.shouldPreserveExtraBlocks());
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
    String expectedCode = "";
    String actualCode = consumer.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.appendBlockStart();
    consumer.append("function testFunction() { test(); }");
    consumer.endFunction(true);
    assertTrue(consumer.sawFunction);
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    String code = getCode();
    if (code.isEmpty()) {
      return '\u0000';
    }
    return code.charAt(code.length() - 1);
  }

  @Override
  public String getCode() {
    String code = super.getCode();
    if (code == null) {
      return "";
    }
    return code;
  }
}