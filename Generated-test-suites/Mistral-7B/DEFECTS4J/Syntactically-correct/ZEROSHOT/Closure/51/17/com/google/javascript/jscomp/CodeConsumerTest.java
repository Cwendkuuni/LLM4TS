import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.startSourceMapping(node);
    verify(consumer).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.endSourceMapping(node);
    verify(consumer).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };
    assertTrue(consumer.continueProcessing());
    CodeConsumer spy = mock(CodeConsumer.class);
    verify(spy).continueProcessing();
    spy.continueProcessing = () -> false;
    assertFalse(spy.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.append("abc");
    char lastChar = consumer.getLastChar();
    assertTrue(lastChar == 'c');
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.addIdentifier("identifier");
    String expected = "identifier";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testAppend() {
    String expected = "string";
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void append(String str) {
        super.append(str);
      }
    };
    consumer.append(expected);
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.appendBlockStart();
    String expected = "{";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.appendBlockEnd();
    String expected = "}";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.startNewLine();
    String expected = "\n";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testMaybeLineBreak() {
    // Test case not applicable as the method is abstract.
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endLine();
    String expected = "\n";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testNotePreferredLineBreak() {
    // Test case not applicable as the method is abstract.
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.beginBlock();
    String expected = "{";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endBlock();
    String expected = "}";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.listSeparator();
    String expected = ", ";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endStatement();
    String expected = ";";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testEndStatementWithSemiColon() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endStatement(true);
    String expected = ";";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testMaybeEndStatement() {
    String sourceBefore = "";
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void append(String str) {
        super.append(str);
        sourceBefore += str;
      }

      @Override
      public void endStatement() {
        super.endStatement();
        sourceBefore += ";";
      }

      @Override
      public void endStatement(boolean needSemiColon) {
        super.endStatement(needSemiColon);
        sourceBefore += (needSemiColon ? ";" : "");
      }
    };
    consumer.append("statement");
    consumer.maybeEndStatement();
    String expected = "statement;";
    String actual = consumer.getSource();
    assertTrue(actual.equals(expected));
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endFunction();
    String expected = "}";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endFunction(true);
    String expected = "}";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.beginCaseBody();
    String expected = ":";
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testEndCaseBody() {
    // Test case not applicable as the method is empty.
  }

  @Test
  public void testAdd() {
    String expected = "string";
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void append(String str) {
        super.append(str);
      }
    };
    consumer.add("string");
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testAddOp() {
    String expected = "+";
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void appendOp(String op, boolean binOp) {
        super.appendOp(op, binOp);
      }

      @Override
      public void addOp(String op, boolean binOp) {
        super.addOp(op, binOp);
      }
    };
    consumer.addOp("+", false);
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testAddNumber() {
    double number = 123.45;
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public void addNumber(double x) {
        super.addNumber(x);
      }
    };
    consumer.addNumber(number);
    String expected = Double.toString(number);
    String actual = consumer.getSource();
    assertTrue(actual.contains(expected));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(CodeConsumer.isWordChar('a'));
    assertTrue(CodeConsumer.isWordChar('_'));
    assertTrue(CodeConsumer.isWordChar('1'));
    assertFalse(CodeConsumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };
    assertTrue(consumer.shouldPreserveExtraBlocks());

    CodeConsumer consumerFalse = new CodeConsumer() {
      @Override
      public boolean shouldPreserveExtraBlocks() {
        return false;
      }
    };
    assertFalse(consumerFalse.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);
    consumer.breakAfterBlockFor(node, true);
    verify(consumer).breakAfterBlockFor(node, true);

    CodeConsumer consumerFalse = mock(CodeConsumer.class);
    consumerFalse.breakAfterBlockFor(node, true);
    verify(consumerFalse).breakAfterBlockFor(node, true);
    consumerFalse.breakAfterBlockFor = (n, sc) -> false;
    consumerFalse.breakAfterBlockFor(node, true);
    verify(consumerFalse, never()).breakAfterBlockFor(node, true);
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {};
    consumer.endFile();
    // An empty string is returned.
    String actual = consumer.getSource();
    assertTrue(actual.isEmpty());
  }
}