import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Add some code to check if source mapping is correctly started
    consumer.append("function test() {");
    consumer.endSourceMapping(node);
    assertEquals("test.js:1:0", node.getSourceInfo().getSourceName() + ":" + node.getSourceInfo().getLineNumber() + ":" + node.getSourceInfo().getSourceColumnNumber());
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new TestCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.append("function test() {");
    consumer.endSourceMapping(node);
    consumer.startSourceMapping(node);
    consumer.append(" var x = 1;");
    consumer.endSourceMapping(node);
    assertEquals("test.js:1:0", node.getSourceInfo().getSourceName() + ":" + node.getSourceInfo().getLineNumber());
    assertEquals("test.js:2:5", node.getSourceInfo().getSourceColumnNumber() == 5 ? node.getSourceInfo().getSourceName() + ":" + node.getSourceInfo().getLineNumber() + ":" + node.getSourceInfo().getSourceColumnNumber() : "Unexpected source column number");
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new TestCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1;");
    consumer.append("}");
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.addIdentifier("x");
    consumer.append(" var x = 1;");
    consumer.append("}");
    assertEquals("x", consumer.getLastCode());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1;");
    consumer.append("}");
    String expectedCode = "function test() {\n  var x = 1;\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    String expectedCode = "function test() {\n{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.append(" var x = 1;");
    consumer.appendBlockEnd();
    String expectedCode = "function test() {\n{\n  var x = 1;\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.startNewLine();
    consumer.append(" var y = 2");
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.maybeLineBreak();
    consumer.append(" var y = 2");
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.maybeCutLine();
    consumer.append(" var y = 2");
    String expectedCode = "function test() {\n  var x = 1\n  var y = 2\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.endLine();
    consumer.append(" var y = 2");
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.notePreferredLineBreak();
    consumer.append(" var y = 2");
    String expectedCode = "function test() {\n  var x = 1\n  var y = 2\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.beginBlock();
    String expectedCode = "function test() {\n{\n";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.append(" var x = 1;");
    consumer.endBlock();
    String expectedCode = "function test() {\n{\n  var x = 1;\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndBlockWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.append(" var x = 1;");
    consumer.endBlock(true);
    String expectedCode = "function test() {\n{\n  var x = 1;\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1,");
    consumer.append(" y = 2");
    consumer.listSeparator();
    consumer.append(" var z = 3");
    String expectedCode = "function test() {\n  var x = 1,\n  y = 2,\n  z = 3\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.endStatement();
    consumer.append(" var y = 2");
    consumer.endStatement();
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2;\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndStatementWithSemicolon() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.endStatement(true);
    consumer.append(" var y = 2");
    consumer.endStatement();
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2;\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.append(" var x = 1");
    consumer.maybeEndStatement();
    consumer.append(" var y = 2");
    consumer.maybeEndStatement();
    String expectedCode = "function test() {\n  var x = 1;\n  var y = 2;\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.append(" var x = 1;");
    consumer.endFunction();
    String expectedCode = "function test() {\n{\n  var x = 1;\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.append(" var x = 1;");
    consumer.endFunction(true);
    String expectedCode = "function test() {\n{\n  var x = 1;\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.beginCaseBody();
    String expectedCode = "function test() {\n{\n  case:\n  }}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.appendBlockStart();
    consumer.beginCaseBody();
    consumer.endCaseBody();
    String expectedCode = "function test() {\n{\n  case:\n  }\n}\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.add("x = 1");
    String expectedCode = "function test() {\n  x = 1\n}";
    assertEquals(expectedCode, consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.addOp("+", true);
    consumer.addNumber(1);
    consumer.addOp("+", true);
    consumer.addNumber(2);
    consumer.append("}");
    String expectedCode = "function test() {\n  return 3;\n}";
    assertEquals(expectedCode, consumer.getCode());
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
    assertFalse(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new TestCodeConsumer();
    consumer.append("function test() {");
    consumer.endFile();
    String expectedCode = "function test() {\n}";
    assertEquals(expectedCode, consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return super.getLastChar();
  }

  @Override
  public String getCode() {
    return super.getCode();
  }
}