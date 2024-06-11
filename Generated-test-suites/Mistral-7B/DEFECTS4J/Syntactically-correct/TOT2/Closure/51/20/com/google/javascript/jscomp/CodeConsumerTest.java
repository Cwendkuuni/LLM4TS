import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CodeConsumer.*;

import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer = new TestCodeConsumer();

 @Test
 public void testStartSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 }

 @Test
 public void testEndSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 }

 @Test
 public void testContinueProcessing() {
 boolean result = consumer.continueProcessing();
 assertTrue(result);
 }

 @Test
 public void testGetLastChar() {
 consumer.append("hello");
 char lastChar = consumer.getLastChar();
 assertEquals('l', lastChar);
 }

 @Test
 public void testAppend() {
 consumer.append("abc");
 String code = consumer.getCode();
 assertEquals("abc", code);
 }

 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 String code = consumer.getCode();
 assertEquals("{", code.substring(code.length() - 1));
 }

 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockEnd();
 String code = consumer.getCode();
 assertEquals("}", code.substring(code.length() - 1));
 }

 @Test
 public void testStartNewLine() {
 consumer.startNewLine();
 String code = consumer.getCode();
 assertEquals("\n", code.substring(code.length() - 1));
 }

 @Test
 public void testMaybeCutLine() {
 consumer.maybeCutLine();
 String code = consumer.getCode();
 assertEquals("\n", code.substring(code.length() - 1));
 }

 @Test
 public void testEndLine() {
 consumer.endLine();
 String code = consumer.getCode();
 assertEquals("\n", code.substring(code.length() - 1));
 }

 @Test
 public void testNotePreferredLineBreak() {
 consumer.notePreferredLineBreak();
 String code = consumer.getCode();
 assertEquals("\n", code.substring(code.length() - 1));
 }

 @Test
 public void testBeginBlock() {
 consumer.beginBlock();
 String code = consumer.getCode();
 assertEquals("{", code.substring(code.length() - 1));
 }

 @Test
 public void testEndBlock() {
 consumer.endBlock();
 String code = consumer.getCode();
 assertEquals("}", code.substring(code.length() - 1));
 }

 @Test
 public void testListSeparator() {
 consumer.listSeparator();
 String code = consumer.getCode();
 assertEquals(",", code.substring(code.length() - 1));
 }

 @Test
 public void testEndStatement() {
 consumer.append("x = 1;");
 consumer.endStatement();
 String code = consumer.getCode();
 assertEquals("x = 1;\n", code);
 }

 @Test
 public void testMaybeEndStatement() {
 consumer.append("x = 1;");
 consumer.maybeEndStatement();
 String code = consumer.getCode();
 assertEquals("x = 1;\n", code);
 }

 @Test
 public void testEndFunction() {
 consumer.append("function test() {}\n");
 consumer.endFunction();
 String code = consumer.getCode();
 assertEquals("function test() {\n}\n", code);
 }

 @Test
 public void testBeginCaseBody() {
 consumer.beginCaseBody();
 String code = consumer.getCode();
 assertEquals(":\n", code.substring(code.length() - 3));
 }

 @Test
 public void testEndCaseBody() {
 consumer.endCaseBody();
 String code = consumer.getCode();
 assertEquals("", code.substring(code.length() - 1));
 }

 @Test
 public void testAdd() {
 consumer.addIdentifier("test");
 String code = consumer.getCode();
 assertEquals("test", code.substring(code.length() - 5));
 }

 @Test
 public void testAddOp() {
 consumer.append("1 + 1");
 consumer.addOp("+", true);
 String code = consumer.getCode();
 assertEquals("1 + 1+\n", code);
 }

 @Test
 public void testAddNumber() {
 consumer.addNumber(3.14);
 String code = consumer.getCode();
 assertEquals("3.14\n", code.substring(code.length() - 5));
 }

 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 node.setTokenType(Node.FOR);
 consumer.breakAfterBlockFor(node, false);
 String code = consumer.getCode();
 assertFalse(code.substring(code.length() - 1).equals("\n"));
 }

 @Test
 public void testEndFile() {
 consumer.endFile();
 String code = consumer.getCode();
 assertEquals("", code);
 }
}

class TestCodeConsumer extends CodeConsumer {
 @Override public char getLastChar() {
 return super.getCode().charAt(super.getCode().length() - 1);
 }

 @Override public String getCode() {
 return super.code;
 }
}