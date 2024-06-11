import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setUp() {
    consumer = new TestCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = Node.newNode("Identifier('test')");
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);

    // Verify that no source mapping information was added to the code
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testContinueProcessing() {
    consumer.setContinueProcessing(false);
    assertTrue(consumer.continueProcessing());

    // Derived classes should still be able to return false
    CodeConsumer derivedConsumer = new DerivedCodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(derivedConsumer.continueProcessing());
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    assertEquals("hello", consumer.getCode());

    consumer.append(" world");
    assertEquals("helloworld", consumer.getCode());

    consumer.append("!");
    assertEquals("helloworld!", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.append("function test() {");
    assertEquals("function test() {\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    consumer.addIdentifier("x");
    consumer.endStatement();
    consumer.addIdentifier("= 5;");
    assertEquals("x;\n", consumer.getCode());

    consumer.addIdentifier("if (x > 0) {");
    consumer.endStatement();
    consumer.addIdentifier("x++;");
    consumer.endStatement();
    consumer.addIdentifier("}");
    assertEquals("if (x > 0) {\n  x++;\n}\n", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    consumer.addIdentifier("if (x > 0) {");
    consumer.maybeEndStatement();
    consumer.addIdentifier("x++;");
    consumer.endStatement();
    consumer.addIdentifier("}");
    assertEquals("if (x > 0) {\n  x++;\n}\n", consumer.getCode());

    consumer.addIdentifier("x = 5;");
    consumer.maybeEndStatement();
    consumer.addIdentifier(";");
    consumer.endStatement();
    assertEquals("x = 5;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    consumer.append("function test() {");
    consumer.endFunction();
    assertEquals("function test() {\n}\n", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.addIdentifier("case 5:");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 5:\n}\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.addIdentifier("case 5:");
    consumer.addIdentifier("x++;");
    consumer.endCaseBody();
    assertEquals("switch (x) {\n  case 5:\n    x++;\n}\n", consumer.getCode());
  }

  @Test
  public void testAdd() {
    consumer.addIdentifier("x = 5 + 3 + ");
    consumer.addIdentifier("10;");
    assertEquals("x = 5 + 3 + 10;\n", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    consumer.addIdentifier("x = 5 + 3 + ");
    consumer.addOp("10", true);
    assertEquals("x = 5 + 3 + 10;\n", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    consumer.addIdentifier("x = 5 + ");
    consumer.addNumber(3.14);
    consumer.addIdentifier(" + 10;");
    assertEquals("x = 5 + 3.14 + 10;\n", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    Node node = Node.newNode("if (x < 0) { x = -x; }");
    CodeConsumer consumer = new TestCodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    consumer.startSourceMapping(node);
    consumer.beginBlock();
    consumer.append("if (x < 0) {");
    consumer.beginBlock();
    consumer.addIdentifier("x = -x;");
    consumer.endBlock();
    consumer.endBlock();
    consumer.endSourceMapping(node);

    assertEquals("if (x < 0) {\n  if (x < 0) {\n    x = -x;\n  }\n}\n", consumer.getCode());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = Node.newNode("if (true) { x = 5; }");
    assertTrue(consumer.breakAfterBlockFor(node, true));
    assertEquals("if (true) {\n  x = 5;\n}\n", consumer.getCode());

    assertFalse(consumer.breakAfterBlockFor(node, false));
    assertEquals("if (true) { x = 5; }\n", consumer.getCode());
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }
}

class TestCodeConsumer extends CodeConsumer {
  @Override
  char getLastChar() {
    return super.getLastChar();
  }

  @Override
  void add(String newcode) {
    super.add(newcode);
  }

  @Override
  String getCode() {
    return super.getCode();
  }
}

class DerivedCodeConsumer extends CodeConsumer {
  // Derived class implementation here
}