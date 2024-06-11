import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
    verify(codeConsumer, times(1)).startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
    verify(codeConsumer, times(1)).endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    when(codeConsumer.continueProcessing()).thenReturn(true);
    assertTrue(codeConsumer.continueProcessing());
    verify(codeConsumer, times(1)).continueProcessing();

    when(codeConsumer.continueProcessing()).thenReturn(false);
    assertFalse(codeConsumer.continueProcessing());
    verify(codeConsumer, times(1)).continueProcessing();
  }

  @Test
  public void testGetLastChar() {
    // Not testing getLastChar directly as it's an abstract method
    // and we don't have a concrete implementation in the test file.
    // Instead, we'll test the methods that call getLastChar.

    String str = "test";
    codeConsumer.append(str);
    char lastChar = codeConsumer.getLastChar();
    assertEquals(str.charAt(str.length() - 1), lastChar);
  }

  @Test
  public void testAppend() {
    String str = "test";
    codeConsumer.append(str);
    verify(codeConsumer, times(1)).append(str);
  }

  @Test
  public void testAddIdentifier() {
    String identifier = "test";
    codeConsumer.addIdentifier(identifier);
    verify(codeConsumer, times(1)).append(identifier);
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    verify(codeConsumer, times(1)).append("{");
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    verify(codeConsumer, times(1)).append("}");
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    verify(codeConsumer, times(1)).startNewLine();
  }

  @Test
  public void testMaybeLineBreak() {
    // Not testing maybeLineBreak directly as it's an abstract method
    // and we don't have a concrete implementation in the test file.
    // Instead, we'll test the methods that call maybeLineBreak.

    codeConsumer.maybeLineBreak();
    verify(codeConsumer, times(1)).maybeCutLine();
  }

  @Test
  public void testMaybeCutLine() {
    // Not testing maybeCutLine directly as it's an abstract method
    // and we don't have a concrete implementation in the test file.
    // Instead, we'll test the methods that call maybeCutLine.

    codeConsumer.maybeCutLine();
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    verify(codeConsumer, times(1)).notePreferredLineBreak();
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    verify(codeConsumer, times(1)).beginBlock();

    codeConsumer.beginBlock(true);
    verify(codeConsumer, times(1)).append("}");
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testEndBlock() {
    codeConsumer.endBlock();
    verify(codeConsumer, times(1)).append("}");

    codeConsumer.endBlock(true);
    verify(codeConsumer, times(1)).append("}");
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    verify(codeConsumer, times(1)).append(",");
    verify(codeConsumer, times(1)).maybeLineBreak();
  }

  @Test
  public void testEndStatement() {
    codeConsumer.endStatement();
    verify(codeConsumer, times(1)).endStatement(false);

    codeConsumer.endStatement(true);
    verify(codeConsumer, times(1)).append(";");
    verify(codeConsumer, times(1)).maybeLineBreak();
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testEndFunction() {
    codeConsumer.endFunction();
    verify(codeConsumer, times(1)).endFunction(false);

    codeConsumer.endFunction(true);
    verify(codeConsumer, times(1)).endLine();
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.beginCaseBody();
    verify(codeConsumer, times(1)).append(":");
  }

  @Test
  public void testEndCaseBody() {
    codeConsumer.endCaseBody();
    // EndCaseBody does not append anything, so no need to verify.
  }

  @Test
  public void testAdd() {
    String newcode = "test";
    codeConsumer.add(newcode);
    verify(codeConsumer, times(1)).add(newcode);

    char prev = 'a';
    codeConsumer.add(newcode);
    char lastChar = codeConsumer.getLastChar();
    assertEquals(newcode.charAt(newcode.length() - 1), lastChar);
    assertEquals(prev, newcode.charAt(newcode.length() - 2));
  }

  @Test
  public void testAppendOp() {
    String op = "+";
    codeConsumer.appendOp(op, false);
    verify(codeConsumer, times(1)).append(op);

    String opBin = "+";
    codeConsumer.appendOp(opBin, true);
    verify(codeConsumer, times(1)).append(opBin);
  }

  @Test
  public void testAddOp() {
    String op = "+";
    codeConsumer.addOp(op, false);
    verify(codeConsumer, times(1)).appendOp(op, false);

    String opBin = "+";
    codeConsumer.addOp(opBin, true);
    verify(codeConsumer, times(1)).appendOp(opBin, true);

    char prev = '+';
    codeConsumer.addOp(opBin, true);
    char lastChar = codeConsumer.getLastChar();
    assertEquals(opBin.charAt(0), lastChar);
    assertEquals(prev, opBin.charAt(opBin.length() - 1));
  }

  @Test
  public void testAddNumber() {
    double x = 123.45;
    codeConsumer.addNumber(x);
    verify(codeConsumer, times(1)).addNumber(x);

    char prev = '.';
    codeConsumer.addNumber(x);
    char lastChar = codeConsumer.getLastChar();
    assertEquals(Character.isDigit(lastChar) || lastChar == 'E' || lastChar == '-', true);
    assertEquals(prev, lastChar);
  }

  @Test
  public void testIsNegativeZero() {
    double x = 0.0;
    assertTrue(CodeConsumer.isNegativeZero(x));

    double xNegative = -0.0;
    assertFalse(CodeConsumer.isNegativeZero(xNegative));
  }

  @Test
  public void testIsWordChar() {
    char ch1 = '_';
    assertTrue(CodeConsumer.isWordChar(ch1));

    char ch2 = '$';
    assertTrue(CodeConsumer.isWordChar(ch2));

    char ch3 = 'a';
    assertTrue(CodeConsumer.isWordChar(ch3));

    char ch4 = 'z';
    assertTrue(CodeConsumer.isWordChar(ch4));

    char ch5 = '0';
    assertTrue(CodeConsumer.isWordChar(ch5));

    char ch6 = '9';
    assertTrue(CodeConsumer.isWordChar(ch6));

    char ch7 = 'A';
    assertTrue(CodeConsumer.isWordChar(ch7));

    char ch8 = 'Z';
    assertTrue(CodeConsumer.isWordChar(ch8));

    char ch9 = '\\';
    assertTrue(CodeConsumer.isWordChar(ch9));

    char ch10 = ' ';
    assertFalse(CodeConsumer.isWordChar(ch10));

    char ch11 = '-';
    assertFalse(CodeConsumer.isWordChar(ch11));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertTrue(codeConsumer.shouldPreserveExtraBlocks());
    verify(codeConsumer, times(1)).shouldPreserveExtraBlocks();
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean statementContext = true;
    boolean result = codeConsumer.breakAfterBlockFor(node, statementContext);
    assertEquals(statementContext, result);
    verify(codeConsumer, times(1)).breakAfterBlockFor(node, statementContext);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    verify(codeConsumer, times(1)).endFile();
  }
}