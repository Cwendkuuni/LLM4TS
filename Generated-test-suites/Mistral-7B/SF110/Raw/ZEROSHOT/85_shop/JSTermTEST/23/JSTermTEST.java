import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 private StreamTokenizer tokenizer;
 private JSTerm term;

 @Before
 public void setUp() {
 tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
 term = new JSTerm(tokenizer);
 }

 @Test
 public void testIsVariable() {
 assertTrue(term.isVariable());
 }

 @Test
 public void testIsConstant() {
 assertFalse(term.isConstant());
 }

 @Test
 public void testIsFunction() {
 assertTrue(term.isFunction());
 }

 @Test
 public void testIsEval() {
 assertFalse(term.isEval());
 }

 @Test
 public void testSize() {
 assertEquals(2, term.size());
 }

 @Test
 public void testPrint() {
 String expected = "+ x 3\n";
 term.print();
 assertEquals(expected, JSUtil.output.toString());
 }

 @Test
 public void testCloneT() {
 JSTerm clonedTerm = term.cloneT();
 assertEquals(term.toString(), clonedTerm.toString());
 }

 @Test
 public void testApplySubstitutionT() {
 JSSubstitution substitution = new JSSubstitution();
 substitution.addEntry(new JSPair("x", new JSTerm(new StreamTokenizer(new StringReader("5")))));
 JSTerm result = term.applySubstitutionT(substitution);
 String expected = "+(5 3)";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testMatches() {
 JSTerm otherTerm = new JSTerm(new StreamTokenizer(new StringReader("(+ y z)")));
 JSSubstitution substitution = term.matches(otherTerm);
 assertFalse(substitution.isEmpty());
 }

 @Test
 public void testEquals() {
 JSTerm otherTerm = new JSTerm(new StreamTokenizer(new StringReader("(+ x 3)")));
 assertTrue(term.equals(otherTerm));
 }

 @Test
 public void testIsGround() {
 assertFalse(term.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm standardizedTerm = term.standardizerTerm();
 String expected = "+(?x1 (+ x 3))";
 assertEquals(expected, standardizedTerm.toString());
 }

 @Test
 public void testCall() {
 JSTerm result = term.call();
 String expected = "5";
 assertEquals(expected, result.toString());
 }

}