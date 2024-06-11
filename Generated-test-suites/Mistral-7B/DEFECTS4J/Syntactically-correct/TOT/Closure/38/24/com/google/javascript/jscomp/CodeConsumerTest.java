import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  @org.junit.Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = Node.newIntLiteral(1);
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Check that source mapping was started and ended correctly
    assertEquals(node, consumer.getLastNodeForSourceMapping());
  }

  @org.junit.Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertFalse(consumer.continueProcessing());
  }

  @org.junit.Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, World!");
    assertEquals('!', consumer.getLastChar());
  }

  @org.junit.Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.append("World!");
    assertEquals("Hello, World!", consumer.getCode());
  }

  @org.junit.Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.startNewLine();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.maybeLineBreak();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.endLine();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.notePreferredLineBreak();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    consumer.append("Hello, ");
    consumer.append("World!");
    consumer.endBlock();

    // Check that block was started and ended correctly
    assertEquals("{\nHello,\nWorld!\n}", consumer.getCode());
  }

  @org.junit.Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.beginBlock();
    consumer.append("World!");
    consumer.endBlock();

    // Check that block was started and ended correctly
    assertEquals("Hello,\n{\nWorld!\n}", consumer.getCode());
  }

  @org.junit.Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.listSeparator();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.endStatement();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("Hello, ");
    consumer.maybeEndStatement();
    consumer.append("World!");
    assertEquals("Hello,\nWorld!", consumer.getCode());
  }

  @org.junit.Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("function foo() {\n  Hello,\n}");
    consumer.endFunction();
    assertEquals("function foo() {\n  Hello,\n}\n", consumer.getCode());
  }

  @org.junit.Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeCustomer();
    consumer.beginCaseBody();
    consumer.append("Hello, World!");
    consumer.endCaseBody();

    // Check that case body was started and ended correctly
    assertEquals(":\nHello, World!\n", consumer.getCode());
  }

  @org.junit.Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.add("Hello, ");
    consumer.add("World!");
    assertEquals("Hello, World!", consumer.getCode());
  }

  @org.junit.Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("foo");
    assertEquals("foo", consumer.getCode());
  }

  @org.junit.Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("5");
    consumer.appendOp("+", false);
    consumer.append("3");
    assertEquals("5 + 3", consumer.getCode());
  }

  @org.junit.Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("5");
    consumer.addOp("+", false);
    consumer.append("3");
    assertEquals("5 + 3", consumer.getCode());
  }

  @org.junit.Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(10.5);
    assertEquals("10.5", consumer.getCode());
  }

  @org.junit.Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = Node.newIdentifier("foo");
    consumer.maybeEndStatement();
    consumer.append("if (");
    consumer.add(node);
    consumer.append(") {\n  bar();\n}");

    // Check that single-statement if and for loops should be wrapped in a block
    assertTrue(consumer.shouldPreserveExtraBlocks());

    // Test that multiple-statement if and for loops should not be wrapped in a block
    CodeConsumer consumer2 = new StubCodeConsumer();
    consumer2.append("if (");
    consumer2.add(node);
    consumer2.append(") {\n  bar();\n  baz();\n}");
    assertFalse(consumer2.shouldPreserveExtraBlocks());

    // Test that single-statement for loop should be wrapped in a block
    CodeConsumer consumer3 = new StubCodeConsumer();
    consumer3.append("for (int i = 0; i < 10; i++) {\n  bar();\n}");
    assertTrue(consumer3.shouldPreserveExtraBlocks());

    // Test that multi-statement for loop should not be wrapped in a block
    CodeConsumer consumer4 = new StubCodeConsumer();
    consumer4.append("for (int i = 0; i < 10; i++) {\n  bar();\n  baz();\n}");
    assertFalse(consumer4.shouldPreserveExtraBlocks());
  }

  @org.junit.Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node ifStatement = Node.newIfStatement(Node.newBooleanLiteral(true));
    Node statement1 = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("x"), Node.newNumberLiteral(1)));
    Node statement2 = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("y"), Node.newNumberLiteral(2)));
    Node block = Node.newBlock(Node.newList(ifStatement, statement1, statement2));
    consumer.append("while (true) {\n  ");
    consumer.add(block);
    consumer.append("}");

    // Check that line break can be added after a block if there is no statement context
    assertTrue(consumer.breakAfterBlockFor(block, false));

    // Check that line break cannot be added after a block if there is a statement context
    Node statement = Node.newExpressionStatement(Node.newAssignment(Node.newIdentifier("z"), Node.newNumberLiteral(3)));
    consumer.append("if (true) {\n  ");
    consumer.add(block);
    consumer.add(statement);
    consumer.append("}");

    assertFalse(consumer.breakAfterBlockFor(block, true));
  }

  @org.junit.Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return '\0';
  }

  @Override
  Node getLastNodeForSourceMapping() {
    return null;
  }

  @Override
  String getCode() {
    return code;
  }
}