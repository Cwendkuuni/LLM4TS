import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 private StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));

 @Test
 public void testEmptyConstructor() {
 JSTerm jsterm = new JSTerm();
 assertEquals("", jsterm.toString());
 }

 @Test
 public void testTermConstructor() {
 JSTerm jsterm = new JSTerm(tokenizer);
 assertEquals("+", jsterm.elementAt(0).toString());
 assertEquals("x", jsterm.elementAt(1).toString());
 assertEquals("3", jsterm.elementAt(2).toString());
 }

 @Test
 public void testListConstructor() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 jsterm.addElement("+");
 jsterm.addElement(new JSTerm(tokenizer));
 jsterm.addElement("3");
 assertEquals("[x, +, Term(@[(), x, 3])]", jsterm.toStr().toString());
 }

 @Test
 public void testParseList() {
 JSTerm jsterm = new JSTerm();
 JSTerm listTerm = jsterm.parseList(tokenizer);
 assertEquals("[nil]", listTerm.toString());

 tokenizer.reset();
 tokenizer.pushBack('(');
 tokenizer.nextToken();
 tokenizer.nextToken();
 tokenizer.nextToken();
 listTerm = jsterm.parseList(tokenizer);
 assertEquals("[nil, x]", listTerm.toString());

 tokenizer.reset();
 tokenizer.pushBack('(');
 tokenizer.nextToken();
 tokenizer.nextToken();
 tokenizer.nextToken();
 tokenizer.nextToken();
 tokenizer.nextToken();
 listTerm = jsterm.parseList(tokenizer);
 assertEquals("[nil, x, Term(@[(), x, 3])]", listTerm.toString());
 }

 @Test
 public void testPrintList() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 jsterm.addElement("+");
 jsterm.addElement(new JSTerm(tokenizer));
 jsterm.addElement("3");
 jsterm.printList(jsterm);
 assertEquals(" x ) ", sysOut.getHistory().get(sysOut.getHistory().size() - 1));
 }

 @Test
 public void testPrint() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 jsterm.addElement("+");
 jsterm.addElement(new JSTerm(tokenizer));
 jsterm.addElement("3");
 jsterm.print();
 assertEquals("(x + Term(@[(), x, 3]))", jsterm.toString());
 }

 @Test
 public void testCloneT() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 jsterm.addElement("+");
 jsterm.addElement(new JSTerm(tokenizer));
 jsterm.addElement("3");
 JSTerm clonedTerm = jsterm.cloneT();
 assertEquals("(x + Term(@[(), x, 3]))", clonedTerm.toString());
 }

 @Test
 public void testApplySubstitutionT() {
 JSTerm jsterm = new JSTerm();
 JSSubstitution alpha = new JSSubstitution();
 JSTerm termToSubstitute = new JSTerm();
 termToSubstitute.addElement("x");
 termToSubstitute.addElement("y");
 alpha.addElement(new JSPair("x", termToSubstitute));
 JSTerm result = jsterm.applySubstitutionT(alpha);
 assertEquals("(y)", result.toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInput() {
 JSTerm jsterm = new JSTerm();
 tokenizer.reset();
 tokenizer.nextToken();
 jsterm = new JSTerm(tokenizer);
 fail("Expected JSParserError but did not receive one.");
 }

 @Test
 public void testMatches() {
 JSTerm jsterm = new JSTerm();
 JSTerm term1 = new JSTerm();
 term1.addElement("x");
 term1.addElement("+");
 term1.addElement(new JSTerm(tokenizer));
 term1.addElement("3");JSTerm term2 = new JSTerm();term2.addElement("x");term2.addElement("x");term2.addElement(new JSTerm(tokenizer));term2.addElement("3");JSSubstitution substitution = jsterm.matches(term1);assertEquals(true, substitution.isEmpty());

 substitution = jsterm.matches(term2);assertEquals("Failures: \nFailures:\nFailure: Term(@[x, +, Term(@[x, +, Term(@[(), x, 3]))])), Term(@[x, x, Term(@[(), x, 3]))] \n", substitution.toString());
 }

 @Test
 public void testEquals() {
 JSTerm jsterm1 = new JSTerm();
 JSTerm jsterm2 = new JSTerm();
 assertEquals(true, jsterm1.equals(jsterm1));
 assertEquals(false, jsterm1.equals(jsterm2));

 jsterm1.addElement("x");
 jsterm2.addElement("x");
 assertEquals(true, jsterm1.equals(jsterm2));

 jsterm1.addElement("+");
 jsterm2.addElement("*");
 assertEquals(false, jsterm1.equals(jsterm2));

 jsterm1.addElement(new JSTerm(tokenizer));
 jsterm2.addElement(new JSTerm(tokenizer));
 assertEquals(true, jsterm1.equals(jsterm2));

 jsterm1.addElement("3");
 jsterm2.addElement("4");
 assertEquals(false, jsterm1.equals(jsterm2));
 }
}