package umd.cs.shop.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTate;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitutionList;
import umd.cs.shop.JSTerm;

public class JSStateTest {

 private JSState state;
 private JSState state1;
 private JSState state2;
 private JSState state3;
 private JSListLogicalAtoms list;
 private JSListLogicalAtoms list1;
 private JSListLogicalAtoms list2;
 private JSListLogicalAtoms list3;
 private JSListLogicalAtoms addList;
 private JSListLogicalAtoms delList;
 private JSOperator op;
 private JSPlan plan;
 private JSSubstitution alpha;
 private JSSubstitution beta;
 private JSSubstitution gamma;
 private JSSubstitution delta;
 private JSListSubstitution answers;
 private JSListSubstitution satisfiers;
 private JSListSubstitution satisfiers1;
 private JSListSubstitution satisfiers2;
 private JSListAxioms axioms;

 @Before
 public void setUp() {
 state = new JSState();
 state1 = new JSState();
 state2 = new JSState();
 state3 = new JSState();
 list = new JSListLogicalAtoms();
 list1 = new JSListLogicalAtoms();
 list2 = new JSListLogicalAtoms();
 list3 = new JSListLogicalAtoms();
 addList = new JSListLogicalAtoms();
 delList = new JSListLogicalAtoms();
 op = new JSOperator();
 plan = new JSPlan();
 alpha = new JSSubstitution();
 beta = new JSSubstitution();
 gamma = new JSSubstitution();
 delta = new JSSubstitution();
 answers = new JSListSubstitution();
 satisfiers = new JSListSubstitution();
 satisfiers1 = new JSListSubstitution();
 satisfiers2 = new JSListSubstitution();
 axioms = new JSListAxioms();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 list.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 list.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 state.addElementsToState(list);
 assertEquals(2, state.size());
 assertFalse(state.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 assertFalse(state.contains(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")))));
 assertTrue(state.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 assertTrue(state.contains(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")))));
 }

 @Test
 public void testApplyOp_whenOpAddLIsEmptyAndDelLIsEmpty_thenReturnEmptyState() {
 op.setAddList(addList);
 op.setDeleteList(delList);
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(0, result.size());
 }

 @Test
 public void testApplyOp_whenOpAddLContainsAnElementNotInStateAndDelLIsEmpty_thenReturnNewStateWithAddedElement() {
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 addList.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(1, result.size());
 assertTrue(result.contains(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")))));
 assertFalse(result.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 }

 @Test
 public void testApplyOp_whenOpDelLContainsAnElementNotInStateAndOpAddLIsEmpty_thenReturnNewStateWithoutRemovedElement() {
 list2.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(1, result.size());
 assertTrue(result.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 assertFalse(result.contains(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")))));
 }

 @Test
 public void testApplyOp_whenOpAddLContainsAnElementAlreadyInState_thenReturnNewStateWithoutDuplicateElement() {
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 addList.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(1, result.size());
 assertTrue(result.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 }

 @Test
 public void testApplyOp_whenOpDelLContainsAnElementNotInState_thenReturnNewStateWithRemovedElement() {
 list2.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(0, result.size());
 }

 @Test
 public void testApplyOp_whenOpAddLAndOpDelLContainCommonElements_thenReturnNewStateWithDifferences() {
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 list1.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 list2.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 addList.addElement(new JSPredicateForm("r(z)", Arrays.asList(new JSTerm("z"))));
 delList.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 JSTate result = state.applyOp(op, alpha, list1, list2);
 assertEquals(2, result.size());
 assertTrue(result.contains(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")))));
 assertFalse(result.contains(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")))));
 assertTrue(result.contains(new JSPredicateForm("r(z)", Arrays.asList(new JSTerm("z")))));
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm p = new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")));
 JSPredicateForm q = new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")));
 list.addElement(p);
 list.addElement(q);
 satisfiers = axioms.TheoremProver(Arrays.asList(p), state, alpha, false);
 assertEquals(1, satisfiers.size());
 beta = (JSSubstitution)satisfiers.elementAt(0);
 assertEquals("x", beta.variableValue("x"));
 assertEquals("a", beta.variableValue("y"));
 answers = state.satisfies(Arrays.asList(p), alpha, axioms);
 assertEquals(1, answers.size());
 assertEquals(beta, answers.elementAt(0));
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm p = new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")));
 JSPredicateForm q = new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y")));
 list.addElement(p);
 list.addElement(q);
 satisfiers = axioms.TheoremProver(Arrays.asList(p), state, alpha, true);
 assertEquals(1, satisfiers.size());
 beta = (JSSubstitution)satisfiers.elementAt(0);
 gamma = new JSSubstitution();
 gamma.addElement(beta);
 answers = state.satisfiesAll(Arrays.asList(p), alpha, axioms);
 assertEquals(1, answers.size());
 assertEquals(gamma, answers.elementAt(0));
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm p = new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x")));
 list.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 satisfiers = state.satisfiesTAm(p, alpha);
 assertEquals(0, satisfiers.size());
 }

 @Test
 public void testSatisfiesTAm_whenStateContainsMatchingAtom_thenReturnSubstitution() {
 list.addElement(new JSPredicateForm("p(x)", Arrays.asList(new JSTerm("x"))));
 satisfiers = state.satisfiesTAm(p, alpha);
 beta = (JSSubstitution)satisfiers.elementAt(0);
 assertEquals("a", beta.variableValue("x"));
 }

 @Test
 public void testSatisfiesTAm_whenStateDoesNotContainMatchingAtom_thenReturnEmptyList() {
 list.addElement(new JSPredicateForm("q(y)", Arrays.asList(new JSTerm("y"))));
 satisfiers = state.satisfiesTAm(p, alpha);
 assertEquals(0, satisfiers.size());
 }
}