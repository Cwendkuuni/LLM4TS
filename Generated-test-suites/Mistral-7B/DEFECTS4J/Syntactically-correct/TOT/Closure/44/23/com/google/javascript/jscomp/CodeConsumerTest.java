import org.junit.Test;
import static org.mockito.Mockito.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {

  @Test
  public void testStartSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.startSourceMapping(node);
    verify(consumer, times(1)).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    CodeConsumer consumer = mock(CodeConsumer.class);
    Node node = mock(Node.class);

    consumer.endSourceMapping(node);
    verify(consumer, times(1)).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      public boolean continueProcessing() {
        return false;
      }
    };

    assertFalse(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      private char lastChar = '\u0000';

      @Override
      abstract char getLastChar();

      @Override
      public char getLastChar() {
        return lastChar;
      }

      public void append(String str) {
        lastChar = str.charAt(str.length() - 1);
      }
    };

    consumer.append("test");
    assertEquals('t', consumer.getLastChar());
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      abstract void append(String str);

      @Override
      public void append(String str) {
        sb.append(str);
      }

      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    assertEquals("test", consumer.getCode());
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void appendBlockStart() {
        sb.append("{");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.appendBlockStart();
    assertEquals("{", consumer.getCode());
  }

  @Test
  public void testAppendBlockEnd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void appendBlockEnd() {
        sb.append("}");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.appendBlockEnd();
    assertEquals("}", consumer.getCode());
  }

  @Test
  public void testStartNewLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void startNewLine() {
        sb.append("\n");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.startNewLine();
    assertEquals("\n", consumer.getCode());
  }

  @Test
  public void testMaybeLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void maybeLineBreak() {
        startNewLine();
      }
    };

    consumer.append("test");
    consumer.maybeLineBreak();
    consumer.append("test2");
    assertEquals("\n test\n test2", consumer.getCode());
  }

  @Test
  public void testMaybeCutLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void maybeCutLine() {
        sb.append("\n");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.maybeCutLine();
    consumer.append("test2");
    assertEquals("test\ntest2", consumer.getCode());
  }

  @Test
  public void testEndLine() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void endLine() {
        sb.append("\n");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.endLine();
    consumer.append("test2");
    assertEquals("test\ntest2", consumer.getCode());
  }

  @Test
  public void testNotePreferredLineBreak() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void notePreferredLineBreak() {
        sb.append("\n");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.notePreferredLineBreak();
    consumer.append("test2");
    assertEquals("test\ntest2", consumer.getCode());
  }

  @Test
  public void testBeginBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void beginBlock() {
        sb.append("{");
        endLine();
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.beginBlock();
    assertEquals("{\n", consumer.getCode());
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void endBlock() {
        sb.append("}");
        endLine();
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.beginBlock();
    consumer.append("test");
    consumer.endBlock();
    assertEquals("{\ntest\n}", consumer.getCode());
  }

  @Test
  public void testListSeparator() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void listSeparator() {
        sb.append(",");
        maybeLineBreak();
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.listSeparator();
    consumer.append("test2");
    assertEquals("test,\ntest2", consumer.getCode());
  }

  @Test
  public void testEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void endStatement() {
        sb.append(";");
        maybeLineBreak();
        statementNeedsEnded = false;
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.endStatement();
    consumer.append("test2");
    assertEquals("test;\ntest2", consumer.getCode());
  }

  @Test
  public void testEndFunction() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void endFunction() {
        sb.append("}");
        endLine();
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.beginBlock();
    consumer.append("function test() {");
    consumer.append("test2");
    consumer.endFunction();
    assertEquals("{\ntest() {\n test2\n}\n}", consumer.getCode());
  }

  @Test
  public void testBeginCaseBody() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void beginCaseBody() {
        sb.append(":");
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("switch (x) {");
    consumer.beginCaseBody();
    consumer.append("test");
    consumer.endCaseBody();
    consumer.append("default:");
    consumer.append("test2");
    consumer.endBlock();
    assertEquals("switch (x) {\n :\ntest\n default:\ntest2\n}", consumer.getCode());
  }

  @Test
  public void testAdd() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void add(String newcode) {
        sb.append(newcode);
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("test");
    consumer.add("test2");
    assertEquals("testtest2", consumer.getCode());
  }

  @Test
  public void testAddOp() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void addOp(String op, boolean binOp) {
        sb.append(op);
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.append("1");
    consumer.addOp("+", false);
    consumer.append("2");
    consumer.addOp("+", true);
    consumer.append("3");
    assertEquals("1 + 2 + 3", consumer.getCode());
  }

  @Test
  public void testAddNumber() {
    CodeConsumer consumer = new CodeConsumer() {
      private StringBuilder sb = new StringBuilder();

      @Override
      void addNumber(double x) {
        sb.append(String.valueOf(x));
      }

      @Override
      public String getCode() {
        return sb.toString();
      }
    };

    consumer.addNumber(1.2);
    consumer.append(" + ");
    consumer.addNumber(2.3);
    assertEquals("1.2 + 2.3", consumer.getCode());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };

    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumer();
    Node node = mock(Node.class);

    assertTrue(consumer.breakAfterBlockFor(node, false));
    assertTrue(consumer.breakAfterBlockFor(node, true));
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFile() {
        // No-op
      }
    };

    consumer.endFile();
  }
}