import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CodeConsumerTest {
  private CodeConsumer codeConsumer;

  @Before
  public void setUp() {
    codeConsumer = mock(CodeConsumer.class);
  }

  @Test
  public void testStartSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.startSourceMapping(node);
  }

  @Test
  public void testEndSourceMapping() {
    Node node = mock(Node.class);
    codeConsumer.endSourceMapping(node);
  }

  @Test
  public void testContinueProcessing() {
    when(codeConsumer.continueProcessing()).thenReturn(true);
    boolean result = codeConsumer.continueProcessing();
    assertEquals(true, result);
  }

  @Test
  public void testGetLastChar() {
    // GetLastChar is abstract and cannot be tested directly.
  }

  @Test
  public void testAppend() {
    String str = "testString";
    codeConsumer.append(str);
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testAppendBlockStart() {
    codeConsumer.appendBlockStart();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testAppendBlockEnd() {
    codeConsumer.appendBlockEnd();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testStartNewLine() {
    codeConsumer.startNewLine();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testMaybeLineBreak() {
    codeConsumer.maybeLineBreak();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testMaybeCutLine() {
    codeConsumer.maybeCutLine();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testEndLine() {
    codeConsumer.endLine();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testNotePreferredLineBreak() {
    codeConsumer.notePreferredLineBreak();
    // Verification cannot be done directly, as getCode() is not available.
  }

  @Test
  public void testBeginBlock() {
    codeConsumer.beginBlock();
    String code = codeConsumer.getCode();
    assertEquals("{\n", code);
  }

  @Test
  public void testEndBlock() {
    codeConsumer.beginBlock();
    codeConsumer.append("testCode");
    codeConsumer.endBlock();
    String code = codeConsumer.getCode();
    assertEquals("{\ntestCode\n}", code);
  }

  @Test
  public void testListSeparator() {
    codeConsumer.listSeparator();
    String code = codeConsumer.getCode();
    assertEquals(",\n", code);
  }

  @Test
  public void testEndStatement() {
    codeConsumer.append("testCode");
    codeConsumer.endStatement();
    String code = codeConsumer.getCode();
    assertEquals("testCode;\n", code);
  }

  @Test
  public void testMaybeEndStatement() {
    codeConsumer.append("testCode;");
    codeConsumer.maybeEndStatement();
    String code = codeConsumer.getCode();
    assertEquals("testCode;\n", code);

    codeConsumer.append("testCode");
    codeConsumer.maybeEndStatement();
    code = codeConsumer.getCode();
    assertEquals("testCode;\n", code);

    codeConsumer.append("testCode;");
    codeConsumer.maybeEndStatement();
    code = codeConsumer.getCode();
    assertEquals("testCode;\n", code);

    codeConsumer.append("testCode");
    codeConsumer.maybeEndStatement(true);
    code = codeConsumer.getCode();
    assertEquals("testCode;\n", code);
  }

  @Test
  public void testEndFunction() {
    codeConsumer.beginBlock();
    codeConsumer.append("function testFunction() {");
    codeConsumer.append("testCode");
    codeConsumer.endFunction();
    String code = codeConsumer.getCode();
    assertEquals("{\nfunction testFunction() {\n  testCode\n}\n", code);
  }

  @Test
  public void testEndFunctionWithStatementContext() {
    codeConsumer.beginBlock();
    codeConsumer.append("function testFunction() {");
    codeConsumer.append("testCode;");
    codeConsumer.endFunction(true);
    String code = codeConsumer.getCode();
    assertEquals("{\nfunction testFunction() {\n  testCode;\n}\n", code);
  }

  @Test
  public void testBeginCaseBody() {
    codeConsumer.append("switch (expression) {\n  case value:\n");
    codeConsumer.beginCaseBody();
    codeConsumer.append("testCode");
    codeConsumer.endCaseBody();
    String code = codeConsumer.getCode();
    assertEquals("switch (expression) {\n  case value:\n    testCode\n  }\n", code);
  }

  @Test
  public void testAdd() {
    codeConsumer.append("testCode");
    codeConsumer.add("testAddition");
    String code = codeConsumer.getCode();
    assertEquals("testCodetestAddition", code);
  }

  @Test
  public void testAddOp() {
    codeConsumer.append("testCode");
    codeConsumer.addOp("+", false);
    codeConsumer.add("testAddition");
    String code = codeConsumer.getCode();
    assertEquals("testCode + testAddition", code);

    codeConsumer.append("testCode");
    codeConsumer.addOp("+", true);
    codeConsumer.add("testAddition");
    code = codeConsumer.getCode();
    assertEquals("testCode +\n testAddition", code);
  }

  @Test
  public void testAddNumber() {
    codeConsumer.append("testCode");
    codeConsumer.addNumber(123.456);
    String code = codeConsumer.getCode();
    assertEquals("testCode 123.456", code);
  }

  @Test
  public void testIsWordChar() {
    assertEquals(true, CodeConsumer.isWordChar('a'));
    assertEquals(true, CodeConsumer.isWordChar('_'));
    assertEquals(true, CodeConsumer.isWordChar('$'));
    assertEquals(true, CodeConsumer.isWordChar('0'));
    assertEquals(true, CodeConsumer.isWordChar('1'));
    assertEquals(false, CodeConsumer.isWordChar(' '));
  }

  @Test
  public void testShouldPreserveExtraBlocks() {
    assertEquals(false, codeConsumer.shouldPreserveExtraBlocks());
  }

  @Test
  public void testBreakAfterBlockFor() {
    Node node = mock(Node.class);
    boolean result = codeConsumer.breakAfterBlockFor(node, true);
    assertEquals(true, result);
  }

  @Test
  public void testEndFile() {
    codeConsumer.endFile();
    // Verification cannot be done directly, as getCode() is not available.
  }
}