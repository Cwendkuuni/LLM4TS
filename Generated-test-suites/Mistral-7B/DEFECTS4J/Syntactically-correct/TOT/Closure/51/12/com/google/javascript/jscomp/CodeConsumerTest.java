import org.junit.Before;
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
 verify(codeConsumer).append("//# sourceMappingURL="); // assume a default URL
 }

 @Test
 public void testEndSourceMapping() {
 codeConsumer.endSourceMapping(node);
 // no verifications needed, as append is abstract
 }

 @Test
 public void testContinueProcessing() {
 codeConsumer.continueProcessing();
 verify(codeConsumer).append(""); // assume no action taken
 }

 @Test
 public void testGetLastChar() {
 codeConsumer.beginBlock();
 codeConsumer.append("abc");
 codeConsumer.endLine();
 codeConsumer.endBlock();
 char lastChar = codeConsumer.getLastChar();
 assertEquals('}', lastChar);
 }

 @Test
 public void testAppend() {
 codeConsumer.append("hello");
 verify(codeConsumer).append("hello");
 }

 @Test
 public void testAppendWithLineBreak() {
 codeConsumer.append("hello\nworld");
 verify(codeConsumer).append("hello");
 verify(codeConsumer).startNewLine();
 verify(codeConsumer).append("world");
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
 // no verifications needed, as append is abstract
 }

 @Test
 public void testEndLine() {
 codeConsumer.endLine();
 verify(codeConsumer).endLine();
 }

 @Test
 public void testNotePreferredLineBreak() {
 codeConsumer.notePreferredLineBreak();
 // no verifications needed, as append is abstract
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
 codeConsumer.append("int x = 1;");
 codeConsumer.endStatement();
 verify(codeConsumer).append(";");
 verify(codeConsumer).maybeLineBreak();
 }

 @Test
 public void testMaybeEndStatement() {
 codeConsumer.append("int x = 1;");
 codeConsumer.maybeEndStatement();
 verify(codeConsumer).append(";");
 verify(codeConsumer).maybeLineBreak();
 }

 @Test
 public void testEndFunction() {
 codeConsumer.beginBlock();
 codeConsumer.append("function foo() {");
 codeConsumer.append("}");
 codeConsumer.endFunction();
 verify(codeConsumer).append("}");
 verify(codeConsumer, never()).endLine();
 }

 @Test
 public void testBeginCaseBody() {
 codeConsumer.beginCaseBody();
 // no verifications needed, as append is abstract
 }

 @Test
 public void testEndCaseBody() {
 codeConsumer.endCaseBody();
 // no verifications needed, as append is abstract
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 codeConsumer.shouldPreserveExtraBlocks();
 verify(codeConsumer).append(false);
 }

 @Test
 public void testBreakAfterBlockFor() {
 codeConsumer.breakAfterBlockFor(node, false);
 verify(codeConsumer).startNewLine();
 }

 @Test
 public void testEndFile() {
 codeConsumer.endFile();
 verify(codeConsumer, never()).append(""); // assume no other file contents
 }

}