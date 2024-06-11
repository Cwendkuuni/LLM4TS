import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 private JSTerm createTerm(String input) {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 return new JSTerm(tokenizer);
 }

 @Test
 public void testJSTermConstructor() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEmpty());
 term = new JSTerm("true");
 assertFalse(term.isEmpty());
 assertEquals("true", term.toString());
 }

 @Test
 public void testJSTermConstructorWithTokenizer() {
 String input = "true";
 JSTerm term = createTerm(input);
 assertEquals(input, term.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSTermConstructorWithInvalidInput() {
 String input = "Expecting Term";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSTermParseListWithMissingElements() {
 String input = "(list)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 new JSTerm(tokenizer);
 }

 @Test
 public void testJSTermParseList() {
 String input = "(list 1 2 3)";
 JSTerm term = createTerm(input);
 assertEquals("[1, 2, 3]", term.toString());
 }

 @Test
 public void testJSTermParseFunctionCall() {
 String input = "(call sqrt 4)";
 JSTerm term = createTerm(input);
 assertEquals("[call sqrt [4]]", term.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSTermParseFunctionCallWithMissingFunction() {
 String input = "(call)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSTermParseFunctionCallWithMissingArgument() {
 String input = "(call sqrt)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSTermParseFunctionCallWithInvalidArguments() {
 String input = "(call sqrt (list 1 2 3 4))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 new JSTerm(tokenizer);
 }

 @Test
 public void testJSTermEquals() {
 JSTerm term1 = createTerm("(list 1 2)");
 JSTerm term2 = createTerm("(list 1 2)");
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testJSTermNotEquals() {
 JSTerm term1 = createTerm("(list 1 2)");
 JSTerm term2 = createTerm("(list 1 3)");
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testJSTermSize() {
 JSTerm term = createTerm("(list 1 2 3 4)");
 assertEquals(4, term.size());
 }

 @Test
 public void testJSTermElementAt() {
 JSTerm term = createTerm("(list 1 2 3 4)");
 assertEquals("1", term.elementAt(0).toString());
 assertEquals("3", term.elementAt(2).toString());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJSTermElementAtWithInvalidIndex() {
 JSTerm term = createTerm("(list 1 2 3 4)");
 term.elementAt(10);
 }

 @Test
 public void testJSTermToStr() {
 JSTerm term = createTerm("(list 1 2 3 4)");
 assertEquals("[1, 2, 3, 4]", term.toStr().toString());
 }

 @Test
 public void testJSTermIsGround() {
 JSTerm term = createTerm("(list 1 2 3)");
 assertFalse(term.isGround());
 term = createTerm("(list 1 2)");
 assertTrue(term.isGround());
 }

 @Test
 public void testJSTermStandardizerTerm() {
 JSTerm term = createTerm("(list x y z)");
 JSTerm stdTerm = term.standardizerTerm();
 assertEquals("[?x, ?y, ?z]", stdTerm.toString());
 }

 @Test
 public void testJSTermCall() {
 JSTerm term = createTerm("(call add 1 2)");
 JSTerm result = term.call();
 assertEquals("3", result.toString());
 }

 @Test
 public void testJSTermCallWithUnaryOperator() {
 JSTerm term = createTerm("(call not true)");
 JSTerm result = term.call();
 assertEquals("false", result.toString());
 }

 @Test
 public void testJSTermCallWithBinaryOperator() {
 JSTerm term = createTerm("(call add 1 2)");
 JSTerm result = term.call();
 assertEquals("3", result.toString());
 }

}