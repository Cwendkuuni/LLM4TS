import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CodeConsumerTest {

 private CodeConsumer codeConsumer;
 private Node node;

 @Before
 public void setup() {
 codeConsumer = mock(CodeConsumer.class);
 node = mock(Node.class);
 }

 @Test
 public void testStartSourceMapping() {
 codeConsumer.startSourceMapping(node);
 verify(codeConsumer).append("// Source mapping start " + node.getClass().getName() + " " + node.uniqueId());
 }

 @Test
 public void testEndSourceMapping() {
 codeConsumer.endSourceMapping(node);
 verify(codeConsumer).append("// Source mapping end " + node.getClass().getName() + " " + node.uniqueId());
 }

 @Test
 public void testContinueProcessing() {
 codeConsumer.continueProcessing();
 verify(codeConsumer, never()).returnFalse();
 }

 @Test
 public void testGetLastChar() {
 codeConsumer.append("abc");
 verify(codeConsumer).getLastChar();
 assertEquals('c', codeConsumer.getLastChar());
 }

 @Test
 public void testAppend() {
 codeConsumer.append("Hello");
 verify(codeConsumer).append("Hello");
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
 verify(codeConsumer).append("\n");
 }

 @Test
 public void testMaybeLineBreak() {
 codeConsumer.maybeLineBreak();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testMaybeCutLine() {
 codeConsumer.maybeCutLine();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testEndLine() {
 codeConsumer.endLine();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testNotePreferredLineBreak() {
 codeConsumer.notePreferredLineBreak();
 verifyZeroInteractions(codeConsumer);
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
 public void testListSeparator() {
 codeConsumer.listSeparator();
 verify(codeConsumer).append(",");
 verify(codeConsumer).maybeLineBreak();
 }

 @Test
 public void testEndStatement() {
 codeConsumer.endStatement();
 verify(codeConsumer).endLine();
 }

 @Test
 public void testMaybeEndStatement() {
 codeConsumer.maybeEndStatement();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testEndFunction() {
 codeConsumer.endFunction();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testBeginCaseBody() {
 codeConsumer.beginCaseBody();
 verify(codeConsumer).append(":");
 }

 @Test
 public void testEndCaseBody() {
 codeConsumer.endCaseBody();
 verifyZeroInteractions(codeConsumer);
 }

 @Test
 public void testAdd() {
 codeConsumer.add("String");
 verify(codeConsumer).append("String");
 }

 @Test
 public void testAddOp() {
 codeConsumer.addOp("+", false);
 verify(codeConsumer).append("+");
 }

 @Test
 public void testAddNumber() {
 codeConsumer.addNumber(3.14);
 verify(codeConsumer).append("3.14");
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 verify(codeConsumer).shouldPreserveExtraBlocks();
 verify(codeConsumer).assertEquals(false);
 }

 @Test
 public void testBreakAfterBlockFor() {
 codeConsumer.breakAfterBlockFor(node, true);
 verify(codeConsumer).assertEquals(true);
 }

 @Test
 public void testEndFile() {
 codeConsumer.endFile();
 verifyZeroInteractions(codeConsumer);
 }
}