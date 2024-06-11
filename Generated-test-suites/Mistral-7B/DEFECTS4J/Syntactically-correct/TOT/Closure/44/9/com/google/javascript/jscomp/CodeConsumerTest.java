import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    CodeConsumer consumer = mock(CodeConsumer.class);

    boolean result = consumer.continueProcessing();
    verify(consumer).continueProcessing();
    verify(consumer).continueProcessing(); // should not call continueProcessing twice
    assert(result);
  }

  @Test
  public void testGetLastChar() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      char getLastChar() {
        return 'a';
      }
    };

    char lastChar = consumer.getLastChar();
    assert(lastChar == 'a');
  }

  @Test
  public void testAppend() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void append(String str) {
        super.append(str);
      }
    };

    consumer.append("hello");

    // Test that the last character was appended correctly
    char lastChar = consumer.getLastChar();
    assert(lastChar == 'o');
  }

  @Test
  public void testAppendBlockStart() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void appendBlockStart() {
        super.appendBlockStart();
        append("TEST");
      }
    };

    consumer.beginBlock();

    String output = consumer.getCode();
    assert(output.contains("{TEST}"));
  }

  @Test
  public void testEndBlock() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endBlock(boolean shouldEndLine) {
        super.endBlock(shouldEndLine);
        append("TEST");
      }
    };

    consumer.beginBlock();
    consumer.endBlock(true);

    String output = consumer.getCode();
    assert(output.contains("{TEST}"));
    assert(output.contains("}"));
  }

  @Test
  public void testMaybeEndStatement() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void maybeEndStatement() {
        super.maybeEndStatement();
        append(";");
      }
    };

    consumer.append("if (true) ");
    consumer.maybeEndStatement();

    String output = consumer.getCode();
    assert(output.contains("if (true) {;}"));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean shouldPreserveExtraBlocks() {
        return true;
      }
    };

    consumer.beginBlock();
    consumer.append("System.out.println(\"Hello world\")");
    consumer.endBlock();

    String output = consumer.getCode();
    assert(output.contains("{"));
    assert(output.contains("System.out.println(\"Hello world\");}"));
  }

  @Test
  public void testBreakAfterBlockFor() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      boolean breakAfterBlockFor(Node n, boolean statementContext) {
        return true;
      }
    };

    // Simulate a FOR statement
    consumer.append("for (int i = 0; i < 10; i++) {");
    consumer.append(" i++;");
    consumer.endBlock();

    String output = consumer.getCode();
    assert(output.contains("for (int i = 0; i < 10; i++) {"));
    assert(output.contains("}"));
    assert(output.contains("\n")); // line break after the block
  }

  @Test
  public void testEndFile() {
    CodeConsumer consumer = new CodeConsumer() {
      @Override
      void endFile() {
        append("System.out.println(\"Hello world\")");
      }
    };

    String output = consumer.getCode();
    assert(output.contains("System.out.println(\"Hello world\")"));
  }
}