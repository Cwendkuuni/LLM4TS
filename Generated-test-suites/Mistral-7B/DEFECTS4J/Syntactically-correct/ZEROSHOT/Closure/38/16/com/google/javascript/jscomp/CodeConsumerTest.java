import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot check the result directly as the implementation is abstract
    assertTrue(consumer.statementNeedsEnded);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = new StubCodeConsumer();
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    assertFalse(consumer.statementNeedsEnded);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    assertEquals('f', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("bar");
    assertEquals("bar", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockStart();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.appendBlockEnd();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.maybeCutLine();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.notePreferredLineBreak();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndBlockWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endBlock(true);
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.listSeparator();
    assertEquals(",\n", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    consumer.endStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndStatementWithoutSemicolon() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    consumer.endStatement(false);
    assertEquals("foo", consumer.getCode());
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    consumer.maybeEndStatement();
    assertEquals("foo", consumer.getCode());
    consumer.append(";");
    consumer.maybeEndStatement();
    assertEquals("foo;\n", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction();
    assertEquals("}\n", consumer.getCode());
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFunction(true);
    assertEquals("\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginCaseBody();
    assertEquals(":\n", consumer.getCode());
  }

  @Test
  public void testEndCaseBody() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endCaseBody();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("foo");
    consumer.add("bar");
    assertEquals("foobar", consumer.getCode());
  }

  @Test
  public void testAddIdentifier() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addIdentifier("identifier");
    assertEquals("identifier", consumer.getCode());
  }

  @Test
  public void testAppendOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("10");
    consumer.appendOp("+", false);
    consumer.append("5");
    assertEquals("10+5", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("10");
    consumer.addOp("+", false);
    consumer.append("5");
    assertEquals("10+5", consumer.getCode());
  }

  @Test
  public void testAddOpWithLineBreak() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.append("10");
    consumer.addOp("+", true);
    consumer.append("5");
    assertEquals("10+\n5", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.addNumber(10.5);
    assertEquals("10.5", consumer.getCode());
  }

  @Test
  public void testIsNegativeZero() {
    assertEquals(false, isNegativeZero(0));
    assertEquals(true, isNegativeZero(-0));
  }

  @Test
  public void testIsWordChar() {
    assertTrue(isWordChar('_'));
    assertTrue(isWordChar('$'));
    assertTrue(isWordChar('a'));
    assertTrue(isWordChar('z'));
    assertTrue(isWordChar('1'));
    assertTrue(isWordChar('9'));
    assertFalse(isWordChar(' '));
    assertFalse(isWordChar('.'));
    assertFalse(isWordChar('='));
    assertFalse(isWordChar('!'));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new StubCodeConsumer();
    assertTrue(consumer.breakAfterBlockFor(new Node(), false));
    assertFalse(consumer.breakAfterBlockFor(new Node(), true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.endFile();
    assertEquals("", consumer.getCode());
  }

  @Test
  public void testSawFunction() {
    CodeConsumer consumer = new StubCodeConsumer();
    consumer.beginFunction();
    consumer.endFunction();
    assertFalse(consumer.sawFunction);

    consumer.beginFunction();
    consumer.endFunction();
    consumer.beginFunction();
    consumer.endFunction();
    assertTrue(consumer.sawFunction);
  }

  private static class StubCodeConsumer extends CodeConsumer {
    private StringBuilder code = new StringBuilder();

    @Override
    public void add(String newcode) {
      code.append(newcode);
    }

    @Override
    public String getCode() {
      return code.toString();
    }

    @Override
    public char getLastChar() {
      return code.charAt(code.length() - 1);
    }

    @Override
    public void append(String str) {
      code.append(str);
    }

    @Override
    public void appendBlockStart() {
      code.append("{\n");
    }

    @Override
    public void appendBlockEnd() {
      code.append("}\n");
    }

    @Override
    public void startNewLine() {
      code.append("\n");
    }

    @Override
    public void maybeLineBreak() {
      // NOP
    }

    @Override
    public void maybeCutLine() {
      // NOP
    }

    @Override
    public void endLine() {
      code.append("\n");
    }

    @Override
    public void notePreferredLineBreak() {
      // NOP
    }

    @Override
    public void beginBlock() {
      code.append("{\n");
      endLine();
    }

    @Override
    public void endBlock() {
      code.append("}\n");
    }

    @Override
    public void endBlock(boolean shouldEndLine) {
      code.append("}\n");
      if (shouldEndLine) {
        endLine();
      }
    }

    @Override
    public void listSeparator() {
      code.append(",\n");
    }

    @Override
    public void endStatement() {
      code.append(";\n");
      statementNeedsEnded = false;
    }

    @Override
    public void endStatement(boolean needSemiColon) {
      if (needSemiColon) {
        code.append(";\n");
        statementNeedsEnded = false;
      } else {
        endLine();
      }
    }

    @Override
    public void maybeEndStatement() {
      if (statementNeedsEnded) {
        code.append(";\n");
        endLine();
        statementNeedsEnded = false;
      } else if (statementStarted) {
        statementNeedsEnded = true;
      }
    }

    @Override
    public void endFunction() {
      code.append("}\n");
    }

    @Override
    public void endFunction(boolean statementContext) {
      sawFunction = true;
      if (statementContext) {
        endLine();
      }
    }

    @Override
    public void beginCaseBody() {
      code.append(":\n");
    }

    @Override
    public void endCaseBody() {
      // NOP
    }
  }
}