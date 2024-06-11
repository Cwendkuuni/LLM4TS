import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSUtil;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 assertTrue(jsState instanceof JSState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(jsState.getLabel());
 assertTrue(jsState instanceof JSState);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 s.addElement(new JSPredicateForm("q(b)", new JSSymbol("q"), new JSSymbol("b")));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 expected.addElement(new JSPredicateForm("q(b)", new JSSymbol("q"), new JSSymbol("b")));
 JSListLogicalAtoms result = jsState.getList();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithDuplicateElement() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 jsState.addElementsToState(s);
 s.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 jsState.addElementsToState(s);
 }

 @Test
 public void testApply() {
 // Since the apply method is not implemented, we cannot test it directly
 // Instead, we can test if an exception is thrown when it's called
 JSState jsState = createJSState();
 JSPlan pl = new JSPlan();
 try {
 jsState.apply(pl);
 fail("Expected an exception to be thrown");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testApplyOp() {
 JSState jsState = createJSState();
 JSOperator op = new JSAdd();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms opAddL = new JSListLogicalAtoms();
 opAddL.addElement(new JSPredicateForm("q(b)", new JSSymbol("q"), new JSSymbol("b")));
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 JSState ns = jsState.applyOp(op, alpha, addL, delL);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 expected.addAll(opAddL);
 JSListLogicalAtoms result = ns.getList();
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(new JSSubstitutionPair("x", new JSSymbol("a")));
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new JSSymbol("p"), new JSSymbol("x")));
 conds.addElement(new JSPredicateForm("q(y)", new JSSymbol("q"), new JSSymbol("y")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(new JSSubstitutionPair("x", new JSSymbol("a")));
 expected.addElement(new JSSubstitutionPair("y", new JSSymbol("b")));
 JSListSubstitution result = answers.getList();
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesTAmWithMatchingPredicate() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSymbol("p"), new JSSymbol("x"));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(new JSSubstitutionPair("x", new JSSymbol("a")));
 JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
 assertEquals(expected, answers);
 }

 @Test
 public void testSatisfiesTAmWithNonMatchingPredicate() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p(a)", new JSSymbol("p"), new JSSymbol("a")));
 JSPredicateForm t = new JSPredicateForm("q(x)", new JSSymbol("q"), new JSSymbol("x"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
 assertTrue(answers.isEmpty());
 }

}