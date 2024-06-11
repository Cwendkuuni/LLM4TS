import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.Node;

import org.junit.Test;

public class CodeConsumerTest {

 private CodeConsumer consumer;

 @Before
 public void setUp() {
 consumer = new TestCodeConsumer();
 }

 /**
 * Test for startSourceMapping method.
 */
 @Test
 public void testStartSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 assertNotNull(consumer.getLastChar());
 }

 /**
 * Test for endSourceMapping method.
 */
 @Test
 public void testEndSourceMapping() {
 Node node = new Node();
 consumer.startSourceMapping(node);
 consumer.endSourceMapping(node);
 assertNull(consumer.getLastChar());
 }

 /**
 * Test for continueProcessing method.
 */
 @Test
 public void testContinueProcessing() {
 assertTrue(consumer.continueProcessing());
 }

 /**
 * Test for getLastChar method with statementNeedsEnded as false.
 */
 @Test
 public void testGetLastCharWithStatementNeedsEndedFalse() {
 consumer.append("abc");
 assertEquals('c', consumer.getLastChar());
 }

 /**
 * Test for getLastChar method with statementNeedsEnded as true.
 */
 @Test
 public void testGetLastCharWithStatementNeedsEndedTrue() {
 consumer.append("abc");
 consumer.endStatement();
 assertEquals('\n', consumer.getLastChar());
 }

 /**
 * Test for append method.
 */
 @Test
 public void testAppend() {
 consumer.append("abc");
 assertEquals("abc", consumer.getCode());
 }

 /**
 * Test for appendBlockStart method.
 */
 @Test
 public void testAppendBlockStart() {
 consumer.appendBlockStart();
 assertEquals("{\n", consumer.getCode());
 }

 /**
 * Test for appendBlockEnd method.
 */
 @Test
 public void testAppendBlockEnd() {
 consumer.appendBlockStart();
 consumer.append("xyz");
 consumer.appendBlockEnd();
 assertEquals("{\nxyz}\n", consumer.getCode());
 }

 /**
 * Test for startNewLine method.
 */
 @Test
 public void testStartNewLine() {
 consumer.append("abc");
 consumer.startNewLine();
 consumer.append("def");
 assertEquals("abc\ndef", consumer.getCode());
 }

 /**
 * Test for maybeLineBreak method.
 */
 @Test
 public void testMaybeLineBreak() {
 consumer.append("abcd");
 consumer.maybeLineBreak();
 consumer.append("efg");
 assertEquals("abcd\nefg", consumer.getCode());
 }

 /**
 * Test for maybeCutLine method.
 */
 @Test
 public void testMaybeCutLine() {
 consumer.append("abcd");
 consumer.maybeCutLine();
 consumer.append("efg");
 assertEquals("abcd\nefg", consumer.getCode());
 }

 /**
 * Test for endLine method.
 */
 @Test
 public void testEndLine() {
 consumer.append("abc");
 consumer.endLine();
 consumer.append("def");
 assertEquals("abc\ndef", consumer.getCode());
 }

 /**
 * Test for notePreferredLineBreak method.
 */
 @Test
 public void testNotePreferredLineBreak() {
 consumer.append("abcd");
 consumer.notePreferredLineBreak();
 consumer.append("efg");
 assertEquals("abcd\nefg", consumer.getCode());
 }

 /**
 * Test for beginBlock method.
 */
 @Test
 public void testBeginBlock() {
 consumer.append("abc");
 consumer.beginBlock();
 consumer.append("def");
 consumer.endBlock();
 assertEquals("abc{\ndef}\n", consumer.getCode());
 }

 /**
 * Test for endBlock method with shouldEndLine as false.
 */
 @Test
 public void testEndBlockWithShouldEndLineFalse() {
 consumer.append("abc");
 consumer.beginBlock();
 consumer.append("def");
 consumer.endBlock(false);
 consumer.append("ghi");
 assertEquals("abc{\ndef}\nghi", consumer.getCode());
 }

 /**
 * Test for endBlock method with shouldEndLine as true.
 */
 @Test
 public void testEndBlockWithShouldEndLineTrue() {
 consumer.append("abc");
 consumer.beginBlock();
 consumer.append("def");
 consumer.endBlock(true);
 assertEquals("abc{\ndef}\n", consumer.getCode());
 }

 /**
 * Test for listSeparator method.
 */
 @Test
 public void testListSeparator() {
 consumer.append("abc");
 consumer.listSeparator();
 consumer.append("def");
 assertEquals("abc,\ndef", consumer.getCode());
 }

 /**
 * Test for endStatement method.
 */
 @Test
 public void testEndStatement() {
 consumer.append("abc");
 consumer.endStatement();
 assertEquals("abc;\n", consumer.getCode());
 }

 /**
 * Test for endStatement method with needSemiColon as false.
 */
 @Test
 public void testEndStatementWithNeedSemiColonFalse() {
 consumer.append("abc");
 consumer.endStatement(false);
 assertEquals("abc", consumer.getCode());
 }

 /**
 * Test for maybeEndStatement method.
 */
 @Test
 public void testMaybeEndStatement() {
 consumer.append("abc");
 consumer.append("def");
 consumer.maybeEndStatement();
 assertEquals("abcdef", consumer.getCode());
 consumer.endStatement();
 assertEquals("abc;\ndef", consumer.getCode());
 }

 /**
 * Test for endFunction method.
 */
 @Test
 public void testEndFunction() {
 consumer.append("function foo() {}\n");
 consumer.endFunction();
 assertEquals("function foo() {}\n}", consumer.getCode());
 }

 /**
 * Test for endFunction method with statementContext as true.
 */
 @Test
 public void testEndFunctionWithStatementContextTrue() {
 consumer.append("function foo() {}\n");
 consumer.beginBlock();
 consumer.append("bar();");
 consumer.endFunction(true);
 assertEquals("function foo() {}\n}", consumer.getCode());
 }

 /**
 * Test for beginCaseBody method.
 */
 @Test
 public void testBeginCaseBody() {
 consumer.append("switch (x) { case 1: }");
 consumer.beginCaseBody();
 consumer.append("xyz");
 consumer.endCaseBody();
 assertEquals("switch (x) { case 1:\n  xyz}\n", consumer.getCode());
 }

 /**
 * Test for add method with isWordChar as true.
 */
 @Test
 public void testAddWithIsWordCharTrue() {
 consumer.append("function foo(x) { return x + ");
 consumer.addIdentifier("y");
 consumer.append("; }");
 assertEquals("function foo(x) { return x + y; }", consumer.getCode());
 }

 /**
 * Test for add method with isWordChar as false.
 */
 @Test
 public void testAddWithIsWordCharFalse() {
 consumer.append("function foo(x) { return x + ");
 consumer.add("42");
 consumer.append("; }");
 assertEquals("function foo(x) { return x + 42; }", consumer.getCode());
 }

 /**
 * Test for addOp method with binOp as false.
 */
 @Test
 public void testAddOpWithBinOpFalse() {
 consumer.append("function foo(x) { return x + ");
 consumer.addOp("+", false);
 consumer.append("5");
 consumer.append("; }");
 assertEquals("function foo(x) { return x + +5; }", consumer.getCode());
 }

 /**
 * Test for addOp method with binOp as true.
 */
 @Test
 public void testAddOpWithBinOpTrue() {
 consumer.append("function foo(x) { return x + ");
 consumer.addOp("+", true);
 consumer.append("5");
 consumer.append("; }");
 assertEquals("function foo(x) { return x + 5; }", consumer.getCode());
 }

 /**
 * Test for addNumber method.
 */
 @Test
 public void testAddNumber() {
 consumer.append("function foo(x) { return x + ");
 consumer.addNumber(3.14);
 consumer.append("; }");
 assertEquals("function foo(x) { return x + 3.14; }", consumer.getCode());
 }

 /**
 * Test for isWordChar method.
 */
 @Test
 public void testIsWordChar() {
 assertTrue(CodeConsumer.isWordChar('a'));
 assertTrue(CodeConsumer.isWordChar('_'));
 assertTrue(CodeConsumer.isWordChar('$'));
 assertTrue(CodeConsumer.isWordChar('A'));
 assertTrue(CodeConsumer.isWordChar('1'));
 assertTrue(CodeConsumer.isWordChar('_1'));
 assertFalse(CodeConsumer.isWordChar(' '));
 assertFalse(CodeConsumer.isWordChar('('));
 }

 /**
 * Test for shouldPreserveExtraBlocks method.
 */
 @Test
 public void testShouldPreserveExtraBlocks() {
 assertTrue(consumer.shouldPreserveExtraBlocks());
 }

 /**
 * Test for breakAfterBlockFor method.
 */
 @Test
 public void testBreakAfterBlockFor() {
 Node node = new Node();
 node.setType(Node.BLOCK);
 assertTrue(consumer.breakAfterBlockFor(node, false));
 assertFalse(consumer.breakAfterBlockFor(node, true));
 }
}