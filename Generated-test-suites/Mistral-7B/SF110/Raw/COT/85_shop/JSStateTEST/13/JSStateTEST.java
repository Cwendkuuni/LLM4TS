package umd.cs.shop.test;

import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSUtil;

public class JSStateTest {

 private JSState createEmptyState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState state = createEmptyState();
 Assert.assertNull(state.getLabel());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 // Cannot test this constructor as it requires a StreamTokenizer which is not provided
 }

 @Test
 public void testAddElementsToState() {
 JSState state = createEmptyState();
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 list.addElement(new JSPredicateForm("q(y)", new JSSubstitution()));
 state.addElementsToState(list);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 expected.addElement(new JSPredicateForm("q(y)", new JSSubstitution()));
 assertTrue(expected.equals(state));
 }

 @Test
 public void testApply() {
 JSState state = createEmptyState();
 JSPlan plan = new JSPlan(); // Not implemented, will throw an exception
 JSState expected = createEmptyState();
 JSState result = state.apply(plan);
 assertEquals(expected, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyThrowsException() {
 JSState state = createEmptyState();
 JSPlan plan = new JSPlan(); // Not implemented, will throw an exception
 state.apply(plan);
 }

 @Test
 public void testApplyOp() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator("=", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState ns = createEmptyState();
 JSState expectedNS = createEmptyState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 JSState result = state.applyOp(op, alpha, addList, delList);
 JSState expectedResult = new JSState();
 expectedResult.addElementsToState(addList);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOpContainingCommonElement() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator("=", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 addList.addElement(new JSPredicateForm("p(y)", new JSSubstitution()));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 delList.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSState ns = createEmptyState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 JSState result = state.applyOp(op, alpha, addList, delList);
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p(y)", new JSSubstitution()));
 nAddL.addElement(new JSPredicateForm("p(y)", new JSSubstitution()));
 nDelL.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSListLogicalAtoms expectedNAddL = new JSListLogicalAtoms();
 expectedNAddL.addElement(new JSPredicateForm("p(y)", new JSSubstitution()));
 expectedNAddL.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 expectedResult.addElementsToState(expectedNAddL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOpEmptyLists() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator("=", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState ns = createEmptyState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 JSState result = state.applyOp(op, alpha, addList, delList);
 JSState expectedResult = new JSState();
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSatisfies() {
 JSState state = new JSState();
 state.insertElementAt(new JSPredicateForm("p(x)", new JSSubstitution()), 0);
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesWithFailure() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.assignFailure();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesAll() {
 JSState state = new JSState();
 state.insertElementAt(new JSPredicateForm("p(x)", new JSSubstitution()), 0);
 state.insertElementAt(new JSPredicateForm("q(y)", new JSSubstitution()), 1);
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 conds.addElement(new JSPredicateForm("q(y)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 expected.addElement(new JSSubstitution());
 JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesTAmWithMatch() {
 JSState state = new JSState();
 state.insertElementAt(new JSPredicateForm("p(x)", new JSSubstitution()), 0);
 JSOperator t = new JSOperator("=", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 t.setHead(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution result = state.satisfiesTAm(t, alpha);
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesTAmWithNoMatch() {
 JSState state = new JSState();
 state.insertElementAt(new JSPredicateForm("p(x)", new JSSubstitution()), 0);
 JSOperator t = new JSOperator("!=", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 t.setHead(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution result = state.satisfiesTAm(t, alpha);
 assertTrue(expected.isEmpty());
 assertTrue(result.isEmpty());
 }
}