package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSState;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 private JSState createEmptyState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState state = new JSState();
 assertNull(state.getLabel());
 assertEquals(0, state.size());
 }

 @Test
 public void testJSStateConstructorStreamTokenizer() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(state.getLabel());
 assertEquals(0, state.size());
 }

 @Test
 public void testAddElementsToState() {
 JSState state = new JSState();
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p", new Object[]{}));
 list.addElement(new JSPredicateForm("q", new Object[]{}));
 state.addElementsToState(list);
 assertEquals(2, state.size());
 }

 @Test
 public void testApply() {
 JSState state = new JSState();
 JSPlan plan = new JSPlan();
 JSState result = state.apply(plan);
 fail("Method apply not implemented.");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyOpNullState() {
 JSOperator op = new JSOperator("+", new Class[]{JSSubstitution.class, JSListLogicalAtoms.class, JSListLogicalAtoms.class});
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState state = null;
 JSTState result = state.applyOp(op, alpha, addList, delList);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyOpNullOp() {
 JSState state = new JSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSOperator op = null;
 JSTState result = state.applyOp(op, alpha, addList, delList);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyOpNullAddList() {
 JSState state = new JSState();
 JSSubstitution alpha = new JSSubstitution();
 JSOperator op = new JSOperator("+", new Class[]{JSSubstitution.class, JSListLogicalAtoms.class, JSListLogicalAtoms.class});
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSTState result = state.applyOp(op, alpha, null, delList);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApplyOpNullDelList() {
 JSState state = new JSState();
 JSSubstitution alpha = new JSSubstitution();
 JSOperator op = new JSOperator("+", new Class[]{JSSubstitution.class, JSListLogicalAtoms.class, JSListLogicalAtoms.class});
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSTState result = state.applyOp(op, alpha, addList, null);
 }

 @Test
 public void testApplyOp() {
 JSState state = new JSState();
 JSState expectedState = new JSState();
 JSOperator op = new JSOperator("+", new Class[]{JSSubstitution.class, JSListLogicalAtoms.class, JSListLogicalAtoms.class});
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p", new Object[]{}));
 JSListLogicalAtoms stateCopy = state.clone();
 state.addElementsToState(addList);
 JSTate result = state.applyOp(op, alpha, addList, delList);
 assertEquals(expectedState, result.getState());
 assertEquals(addList, result.getAddedList());
 assertEquals(delList, result.getDeletedList());
 }

 @Test
 public void testSatisfies() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertFalse(result.isAssignedFailure());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesWithFailure() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 result.assignFailure();
 assertEquals(result, state.satisfies(conds, alpha, axioms));
 }

 @Test
 public void testSatisfiesAll() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 conds.addElement(new JSPredicateForm("q", new Object[]{}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAllWithSuccess() {
 JSState state = new JSState();
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p", new Object[]{}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedState, result.getSubstitution());
 }

 @Test
 public void testSatisfiesTAm() {
 JSState state = new JSState();
 JSPredicateForm goal = new JSPredicateForm("p(a)", new Object[]{new Object[]{"a", Integer.TYPE, 1}});
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms result = state.satisfiesTAm(goal, alpha);
 assertEquals(0, result.size());
 }

 @Test
 public void testSatisfiesTAmWithSuccess() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p(a)", new Object[]{new Object[]{"a", Integer.TYPE, 1}}));
 JSPredicateForm goal = new JSPredicateForm("p(b)", new Object[]{new Object[]{"b", Integer.TYPE, 1}});
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms result = state.satisfiesTAm(goal, alpha);
 assertEquals(1, result.size());
 }

}