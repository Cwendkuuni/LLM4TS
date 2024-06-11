import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer codeConsumer;

  @Before
  public void setup() {
    codeConsumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
    verify(codeConsumer).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    verify(codeConsumer).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    assertTrue(codeConsumer.continueProcessing());
    verify(codeConsumer).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    // This method is abstract, so can't be tested directly.
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "identifier";
    codeConsumer.addIdentifier(identifier);
    verify(codeConsumer).add(identifier);
  }

  @Test
  public void testAppend() {
    String str = "string";
    codeConsumer.append(str);
    verify(codeConsumer).append(str);
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    verify(codeConsumer).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    verify(codeConsumer).append("}");
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    verify(codeConsumer).startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    verify(codeConsumer, times(0)).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    verify(codeConsumer, times(0)).endLine();
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    verify(codeConsumer).endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    verify(codeConsumer).notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    verify(codeConsumer).append("{");
    verify(codeConsumer).endLine();
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    verify(codeConsumer).append("}");
  }

  @Test
  public void testEndBlockWithEndLine() {
    codeConsumer.endBlock(true);
    verify(codeConsumer).append("}");
    verify(codeConsumer).endLine();
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    verify(codeConsumer).add(",");
    verify(codeConsumer).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
    verify(codeConsumer, times(0)).append(";");

    codeConsumer.endStatement(true);
    verify(codeConsumer).append(";");
    verify(codeConsumer).maybeLineBreak();
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.maybeEndStatement();
    verify(codeConsumer, times(0)).append(";");

    codeConsumer.statementNeedsEnded = true;
    codeConsumer.maybeEndStatement();
    verify(codeConsumer).append(";");
    verify(codeConsumer).maybeLineBreak();
    verify(codeConsumer).endLine();
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
    verify(codeConsumer).endFunction(false);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    codeConsumer.endFunction(true);
    verify(codeConsumer).endFunction(true);
    verify(codeConsumer).endLine();
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    verify(codeConsumer).append(":");
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
    verify(codeConsumer, times(0)).append("}");
  }

  @Test
  public void testAdd() {
    String newcode = "newcode";
    codeConsumer.add(newcode);
    verify(codeConsumer).maybeEndStatement();
    verify(codeConsumer).add(newcode);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    codeConsumer.addOp(op, false);
    verify(codeConsumer).append(op);
  }

  @Test
  public void testAddNumber() {
    double x = 42.0;
    codeConsumer.addNumber(x);
    verify(codeConsumer).addNumber(x);
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    // Should return false since the default value is false
    // and we're not testing a subclass of CodeConsumer.
    assertFalse(codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node n = mock(Node.class);
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(n, statementContext);
    verify(codeConsumer).breakAfterBlockFor(n, statementContext);
    assertTrue(result);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    verify(codeConsumer).endFile();
  }

  @Test
  public void testSawFunction() {
    codeConsumer.endFunction();
    codeConsumer.sawFunction = false;
    codeConsumer.endFunction();
    assertTrue(codeConsumer.sawFunction);
  }
}