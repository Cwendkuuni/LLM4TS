package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;
import umd.cs.shop.JSUtil;

public class JSStateTest {

 private JSState createState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState state = createState();
 assertNull(state.getLabel());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState state = new JSState(new StreamTokenizer());
 assertNull(state.getLabel());
 }

 @Test
 public void testAddElementsToState() {
 JSState state = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 s.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 state.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 expected.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 JSListLogicalAtoms actual = state.toList();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullList() {
 JSState state = new JSState();
 state.addElementsToState(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateDuplicateElement() {
 JSState state = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 state.addElementsToState(s);
 s.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 }

 @Test
 public void testApply() {
 JSState state = createState();
 JSPlan pl = new JSPlan();
 JSState expected = new JSState();
 JSState actual = state.apply(pl);
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyThrowsUnsupportedOperationException() {
 JSState state = new JSState();
 JSPlan pl = new JSPlan();
 JSState expected = null;
 JSState actual = state.apply(pl);
 assertNotSame(expected, actual);
 }

 @Test
 public void testApplyOp() {
 JSState state1 = createState();
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState state2 = new JSState();
 state2.addElementsToState(addL);
 state2.addElementsToState(delL);
 JSState expected = new JSState();
 JSState actual = state1.applyOp(op, null, addL, delL);
 JSListLogicalAtoms actualAddL = (JSListLogicalAtoms) actual.getAddList();
 JSListLogicalAtoms actualDelL = (JSListLogicalAtoms) actual.getDelList();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 expectedDelL.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 assertEquals(expectedAddL, actualAddL);
 assertEquals(expectedDelL, actualDelL);
 }

 @Test(expected = NullPointerException.class)
 public void testApplyOpWithNullAddList() {
 JSState state = new JSState();
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = null;
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expected = new JSState();
 JSState actual = state.applyOp(op, null, addL, delL);
 }

 @Test(expected = NullPointerException.class)
 public void testApplyOpWithNullDelList() {
 JSState state = new JSState();
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = null;
 JSState expected = new JSState();
 JSState actual = state.applyOp(op, null, addL, delL);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpWithEmptyLists() {
 JSState state = new JSState();
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expected = new JSState();
 JSState actual = state.applyOp(op, null, addL, delL);
 }

 @Test
 public void testSatisfies() {
 JSState state = createState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesWithNullConditionsList() {
 JSState state = new JSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = new JSListSubstitution();
 JSListSubstitution actual = state.satisfies(null, alpha, axioms);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesWithNullSubstitution() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = new JSListSubstitution();
 JSListSubstitution actual = state.satisfies(conds, null, axioms);
 }

 @Test
 public void testSatisfiesAll() {
 JSState state = createState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, true);
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSatisfiesAllWithEmptyConditionsList() {
 JSState state = new JSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesAll(new JSListLogicalAtoms(), alpha, axioms);
 }

 @Test
 public void testSatisfiesTAm() {
 JSState state = createState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"y"});
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution satisfiers = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 JSListSubstitution expected = new JSListSubstitution();
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAmMatchesAnAtom() {
 JSState state = new JSState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"y"});
 JSPredicateForm expectedAtom = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 state.addElement(expectedAtom);
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAmMatchesMultipleAtoms() {
 JSState state = new JSState();
 JSPredicateForm t1 = new JSPredicateForm("p(x)", new String[]{"x"}, new String[]{"y"});
 JSPredicateForm t2 = new JSPredicateForm("q(x)", new String[]{"x"}, new String[]{"z"});
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 state.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}));
 state.addElement(new JSPredicateForm("q1", new String[]{"x"}, new String[]{"z"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 expected.addElement(new JSSubstitution());
 JSListSubstitution actual = state.satisfiesTAm(t1, alpha);
 assertEquals(expected, actual);
 JSListSubstitution actual2 = state.satisfiesTAm(t2, alpha);
 assertEquals(expected, actual2);
 }

}