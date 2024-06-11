import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {

  private CodeConsumer consumer;

  @Before
  public void setup() {
    consumer = new StubCodeConsumer();
  }

  @Test
  public void testStartSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    // Cannot assert anything here as this method is abstract
  }

  @Test
  public void testEndSourceMapping() {
    Node node = new Node();
    consumer.startSourceMapping(node);
    consumer.endSourceMapping(node);
    // Cannot assert anything here as these methods are abstract
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(consumer.continueProcessing());
  }

  @Test
  public void testGetLastChar() {
    // Cannot test this method directly as it is abstract
  }

  @Test
  public void testAppend() {
    consumer.append("hello");
    assertEquals('l', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockStart() {
    consumer.appendBlockStart();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testAppendBlockEnd() {
    consumer.appendBlockEnd();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testStartNewLine() {
    consumer.startNewLine();
    // Cannot assert anything here as there is no last character yet
  }

  @Test
  public void testMaybeLineBreak() {
    consumer.maybeLineBreak();
    // Cannot assert anything here as nothing is appended yet
  }

  @Test
  public void testMaybeCutLine() {
    // Cannot test this method directly as it is abstract
  }

  @Test
  public void testEndLine() {
    consumer.endLine();
    // Cannot assert anything here as there is no last character yet
  }

  @Test
  public void testNotePreferredLineBreak() {
    // Cannot test this method directly as it is abstract
  }

  @Test
  public void testBeginBlock() {
    consumer.beginBlock();
    assertEquals('{', consumer.getLastChar());
  }

  @Test
  public void testEndBlock() {
    consumer.beginBlock();
    consumer.append("hello");
    consumer.endBlock();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testListSeparator() {
    consumer.listSeparator();
    assertEquals(',', consumer.getLastChar());
  }

  @Test
  public void testEndStatement() {
    consumer.append("hello");
    consumer.endStatement();
    // Cannot assert anything here as there is no ';' added yet
    consumer.maybeEndStatement();
    assertEquals(';', consumer.getLastChar());
  }

  @Test
  public void testEndFunction() {
    consumer.beginBlock();
    consumer.append("function foo() {}");
    consumer.endFunction();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testBeginCaseBody() {
    consumer.beginBlock();
    consumer.append("case");
    consumer.beginCaseBody();
    consumer.append("return 0;");
    consumer.endCaseBody();
    assertEquals('}', consumer.getLastChar());
  }

  @Test
  public void testAdd() {
    consumer.append("hello");
    consumer.add("world");
    assertEquals('w', consumer.getLastChar());
  }

  @Test
  public void testAddOp() {
    consumer.append("1 + 2");
    consumer.addOp("+", false);
    assertEquals('+', consumer.getLastChar());

    consumer.append("1 + 2");
    consumer.addOp("+", true);
    assertEquals('+', consumer.getLastChar());
  }

  @Test
  public void testAddNumber() {
    consumer.append("1 + ");
    consumer.addNumber(2.5);
    assertEquals('.', consumer.getLastChar());
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(consumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    // Cannot test this method directly as it depends on the context
  }

  @Test
  public void testEndFile() {
    consumer.endFile();
    // Cannot assert anything here as there is no last character yet
  }
}

class StubCodeConsumer extends CodeConsumer {
  @Override
  public char getLastChar() {
    return '\0';
  }
}