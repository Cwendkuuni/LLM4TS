package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitutionList;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 // Test constructor and getters
 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNull(jsState.getLabel());
 }

 @Test
 public void testJSStateLabelConstructor() {
 JSState jsState = new JSState("testLabel");
 assertEquals("testLabel", jsState.getLabel());
 }

 // Test addElementsToState method
 @Test
 public void testAddElementsToStateBasic() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms(new JSPredicateForm("p(x)", new JSSubstitution()));
 assertEquals(expected, jsState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullArgument() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = null;
 jsState.addElementsToState(s);
 fail("Expected IllegalArgumentException");
 }

 // Test apply method
 @Test
 public void testApplyNullPlan() {
 JSState jsState = new JSState();
 JSPlan pl = null;
 JSState expectedResult = new JSState();
 JSState result = jsState.apply(pl);
 assertEquals(expectedResult, result);
 }

 // Test applyOp method - Basic scenario
 @Test
 public void testApplyOpBasic() {
 JSState jsState = new JSState(new StringReader("(q(x))"));
 JSOperator op = new JSOperator(new StringReader(":operator (x : R) :: (y : R) -> (x = y)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms(new JSPredicateForm("r(a)", alpha), new JSPredicateForm("r(b)", alpha));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSTState expectedResult = new JSTState(jsState, addL, delL);
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 // Test applyOp method - Contains element in both AddL and State
 @Test
 public void testApplyOpContains() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSOperator op = new JSOperator(new StringReader(":operator (x : R) :: (y : R) -> (x = y)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms(new JSPredicateForm("p(a)", alpha), new JSPredicateForm("p(b)", alpha));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState(jsState, addL, delL);
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 // Test applyOp method - Contains element in both DelL and State
 @Test
 public void testApplyOpDelete() {
 JSState jsState = new JSState(new StringReader("(q(x)) (p(x))"));
 JSOperator op = new JSOperator(new StringReader(":operator (x : R) :: (y : R) -> (x = y)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms(new JSPredicateForm("p(x)", alpha));
 JSState expectedResult = new JSState(jsState, addL, delL);
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 // Test satisfies method - Empty conds
 @Test
 public void testSatisfiesEmptyConditions() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedResult = new JSSubstitution();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 // Test satisfies method - Satisfies single condition
 @Test
 public void testSatisfiesSingleCondition() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms(new JSPredicateForm("p(a)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedResult = new JSSubstitution(new JSPredicateForm("p(a)", new JSSubstitution()));
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 // Test satisfiesAll method - Empty conds
 @Test
 public void testSatisfiesAllEmptyConditions() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 // Test satisfiesAll method - Single condition
 @Test
 public void testSatisfiesAllSingleCondition() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms(new JSPredicateForm("p(a)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedResult = new JSListSubstitution(new JSSubstitution(new JSPredicateForm("p(a)", new JSSubstitution())));
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedResult, result);
 }

 // Test satisfiesTAm method - Empty state
 @Test
 public void testSatisfiesTAmEmptyState() {
 JSState jsState = new JSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSubstitution());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedResult = new JSListSubstitution();
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, result);
 }

 // Test satisfiesTAm method - Satisfies single atom
 @Test
 public void testSatisfiesTAmSingleAtom() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSPredicateForm t = new JSPredicateForm("p(a)", new JSSubstitution());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedResult = new JSListSubstitution(new JSSubstitution(new JSPredicateForm("p(a)", new JSSubstitution())));
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, result);
 }

 // Test satisfiesTAm method - Multiple atoms
 @Test
 public void testSatisfiesTAmMultipleAtoms() {
 JSState jsState = new JSState(new StringReader("(p(x)) (q(x))"));
 JSPredicateForm t = new JSPredicateForm("p(a) :: q(b)", new JSSubstitution());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedResult = new JSListSubstitution(new JSSubstitution(new JSPredicateForm("p(a)", new JSSubstitution())), new JSSubstitution(new JSPredicateForm("q(b)", new JSSubstitution())));
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedResult, result);
 }

 // Test contains method
 @Test
 public void testContains() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSPredicateForm el = new JSPredicateForm("p(a)", new JSSubstitution());
 JSState expectedResult = new JSState(new StringReader("(p(x)) (p(a))"));
 JSState result = jsState.insertElementAt(el, 0);
 assertTrue(result.contains(el));
 }

 // Test contains method - False
 @Test
 public void testContainsFalse() {
 JSState jsState = new JSState(new StringReader("(p(x))"));
 JSPredicateForm el = new JSPredicateForm("q(a)", new JSSubstitution());
 JSState result = jsState;
 assertFalse(result.contains(el));
 }
}