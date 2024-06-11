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
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSUtil;

public class JSStateTest {

 private JSState createEmptyState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstructor() {
 JSState state = new JSState();
 assertNull(state.getLabel());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(state.getLabel());
 }

 @Test
 public void testAddElementsToState() {
 JSState state = createEmptyState();
 JSListLogicalAtoms list = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
 state.addElementsToState(list);
 List<JSPredicateForm> expected = Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 List<JSPredicateForm> actual = new ArrayList<>(state.size());
 for (int i = 0; i < state.size(); ++i) {
 actual.add(state.elementAt(i));
 }
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullList() {
 JSState state = createEmptyState();
 state.addElementsToState(null);
 }

 @Test
 public void testApplyOpNullOp() {
 JSState state = createEmptyState();
 JSOperator op = null;
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expected = new JSState();
 JSState actual = state.applyOp(op, null, addL, delL);
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyOpEmptyLists() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"), null);
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expected = new JSState();
 JSState actual = state.applyOp(op, null, addL, delL);
 assertEquals(expected, actual);
 }

 @Test
 public void testApplyOpNonEmptyLists() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"), null);
 JSListLogicalAtoms addL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("q1"), new JSPredicateForm("q2")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSState expected = new JSState();
 expected.addElement(new JSPredicateForm("p2"));
 expected.addElement(new JSPredicateForm("p3"));
 expected.addElements(addL);
 JSState actual = state.applyOp(op, null, addL, delL);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpNullAddList() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"), null);
 JSListLogicalAtoms addL = null;
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState actual = state.applyOp(op, null, addL, delL);
 assertNull(actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpNullDelList() {
 JSState state = createEmptyState();
 JSOperator op = new JSOperator(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"), null);
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = null;
 JSState actual = state.applyOp(op, null, addL, delL);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesNullConditions() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesEmptyConditions() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesSingleConditionSatisfied() {
 JSState state = createEmptyState();
 state.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(new JSPredicateForm("p1"));
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesSingleConditionNotSatisfied() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.assignFailure();
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesMultipleConditionsSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2, p3")), "state1");
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.addElements(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesMultipleConditionsNotSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2, p3")), "state1");
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p4")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.assignFailure();
 JSSubstitution actual = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAllNullConditions() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesAllEmptyConditions() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAllSingleConditionSatisfied() {
 JSState state = createEmptyState();
 state.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAllSingleConditionNotSatisfied() {
 JSState state = createEmptyState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAllMultipleConditionsSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2, p3")), "state1");
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution());
 expected.addElement(new JSSubstitution());
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAllMultipleConditionsNotSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2, p3")), "state1");
 JSListLogicalAtoms conds = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p4"), new JSPredicateForm("p5")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 JSListSubstitution actual = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAmNullState() {
 JSState state = null;
 JSPredicateForm t = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesTAmEmptyState() {
 JSState state = new JSState();
 JSPredicateForm t = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertNull(actual);
 }

 @Test
 public void testSatisfiesTAmSingleConditionSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1")), "state1");
 JSPredicateForm t = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertEquals(new JSListSubstitution(Arrays.asList(alpha)), actual);
 }

 @Test
 public void testSatisfiesTAmSingleConditionNotSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1")), "state1");
 JSPredicateForm t = new JSPredicateForm("p2");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertEquals(new JSListSubstitution(), actual);
 }

 @Test
 public void testSatisfiesTAmMultipleConditionsSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2")), "state1");
 JSPredicateForm t = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertEquals(new JSListSubstitution(Arrays.asList(alpha, new JSSubstitution())), actual);
 }

 @Test
 public void testSatisfiesTAmMultipleConditionsNotSatisfied() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("p1, p2")), "state1");
 JSPredicateForm t = new JSPredicateForm("p3");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution actual = state.satisfiesTAm(t, alpha);
 assertEquals(new JSListSubstitution(), actual);
 }
}