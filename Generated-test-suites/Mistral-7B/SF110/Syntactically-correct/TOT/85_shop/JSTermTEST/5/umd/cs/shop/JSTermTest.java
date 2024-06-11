package umd.cs.shop;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class JSTermTest {

 @BeforeEach
 void setUp() {
 // Initialize any necessary variables or objects here
 }

 @AfterEach
 void tearDown() {
 // Perform any necessary clean-up actions here
 }

 @Test
 void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isFunction());
 assertTrue(term.isEmpty());
 }

 @Test
 void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 assertEquals("x", term.elementAt(0));
 }

 @Test
 void testConstantTerm() {
 JSTerm term = new JSTerm("2");
 assertTrue(term.isConstant());
 assertFalse(term.isVariable());
 assertFalse(term.isFunction());
 assertEquals("2", term.elementAt(0));
 }

 @Test
 void testListTerm() {
 JSTerm term = new JSTerm("(+ 1 2)");
 assertTrue(term.isFunction());
 assertEquals("+", term.elementAt(0));
 assertEquals(2, term.size());
 assertTrue(term.elementAt(1).isNumber());
 assertTrue(term.elementAt(1).equals(new JSTerm("1")));
 assertTrue(term.elementAt(2).isNumber());
 assertTrue(term.elementAt(2).equals(new JSTerm("2")));
 }

 @Test
 void testCallTerm() {
 JSTerm term = new JSTerm("(call f x)");
 assertTrue(term.isFunction());
 assertEquals("call", term.elementAt(0));
 assertEquals(2, term.size());
 assertTrue(term.elementAt(1).isVariable());
 assertTrue(term.elementAt(1).equals(new JSTerm("f")));
 assertTrue(term.elementAt(2).isVariable());
 assertTrue(term.elementAt(2).equals(new JSTerm("x")));
 }

 @Test
 void testEvalTerm() {
 JSTerm term = new JSTerm("(eval true)");
 assertTrue(term.isEval());
 assertTrue(term.isFunction());
 assertEquals("eval", term.elementAt(0));
 assertEquals(1, term.size());
 assertTrue(term.elementAt(0).isConstant());
 assertTrue(term.elementAt(0).equals(new JSTerm("true")));
 }

 @Test
 void testEvalError() {
 JSTerm term = new JSTerm("(eval x)");
 assertTrue(term.isFunction());
 assertEquals("eval", term.elementAt(0));
 assertEquals(1, term.size());
 assertTrue(term.elementAt(0).isVariable());
 assertTrue(term.elementAt(0).equals(new JSTerm("x")));
 assertThrows(JSParserError.class, () -> term.call());
 }

 @Test
 void testMatches() {
 JSTerm term1 = new JSTerm("(+ x 2)");
 JSTerm term2 = new JSTerm("(+ y 2)");
 JSTerm alpha = new JSSubstitution();
 JSTerm beta = term1.matches(term2, alpha);
 assertTrue(beta.isEmpty());

 term1 = new JSTerm("(+ x 2)");
 term2 = new JSTerm("(+ x 2)");
 beta = term1.matches(term2, alpha);
 assertTrue(beta.size() == 1);
 JSTerm pair = beta.elementAt(0);
 assertTrue(pair instanceof JSPairVarTerm);
 assertEquals("x", ((JSPairVarTerm)pair).getFirst());
 assertEquals("x", ((JSTerm)pair.getSecond()).elementAt(0));
 }

 @Test
 void testMatchesError() {
 JSTerm term1 = new JSTerm("(+ x 2)");
 JSTerm term2 = new JSTerm("(+ y 2)");
 assertThrows(JSParserError.class, () -> term1.matches(term2, new JSSubstitution()));
 }

 @Test
 void testClone() {
 JSTerm term = new JSTerm("(+ x 2)");
 JSTerm cloned = term.cloneT();
 assertEquals(term, cloned);

 term = new JSTerm("(+ x 2)");
 term.makeFunction();
 cloned = term.cloneT();
 assertNotEquals(term, cloned);
 }

 @Test
 void testApplySubstitution() {
 JSTerm term = new JSTerm("(+ x 2)");
 JSTerm instance = new JSTerm("(+ y 2)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", instance));
 JSTerm result = term.applySubstitutionT(alpha);
 assertEquals(instance, result);
 }

 @Test
 void testApplySubstitutionError() {
 JSTerm term = new JSTerm("(+ x 2)");
 assertThrows(JSParserError.class, () -> term.applySubstitutionT(null));
 }
}