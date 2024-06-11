package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSListSubstitution;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 private JSState state;
 private JSListLogicalAtoms list;
 private JSListLogicalAtoms addList;
 private JSListLogicalAtoms delList;
 private JSOperator op;
 private JSPlan plan;
 private JSListAxioms axioms;
 private JSSubstitution alpha;

 @Before
 public void setUp() {
 state = new JSState();
 list = new JSListLogicalAtoms();
 addList = new JSListLogicalAtomas();
 delList = new JSListLogicalAtoms();
 op = new JSOperator();
 plan = new JSPlan();
 axioms = new JSListAxioms();
 alpha = new JSSubstitution();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorTokenizer() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 list.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 list.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 state.addElementsToState(list);
 JSListLogicalAtoms result = new JSListLogicalAtoms();
 result.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 result.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 assertEquals(result, state);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateNullElements() {
 list.addElement(null);
 state.addElementsToState(list);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateEmptyList() {
 list = new JSListLogicalAtoms();
 state.addElementsToState(list);
 }

 @Test
 public void testApply() {
 fail("apply method not implemented yet");
 }

 @Test
 public void testApplyOp() {
 addList.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 delList.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
 expectedAddList.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 expectedDelList.addElement(new JSPredicateForm("p(y)", new String[]{"y"}));
 op.setAddList(addList);
 op.setDeleteList(delList);
 JSState expectedState = new JSState();
 expectedState.addElementsToState(expectedAddList);
 JSState result = state.applyOp(op, alpha, list, delList);
 assertEquals(expectedState, result);
 }

 @Test
 public void testApplyOpNullLists() {
 op.setAddList(null);
 op.setDeleteList(null);
 JSState result = state.applyOp(op, alpha, list, delList);
 assertEquals(state, result);
 }

 @Test
 public void testApplyOpEmptyLists() {
 addList = new JSListLogicalAtoms();
 delList = new JSListLogicalAtoms();
 op.setAddList(addList);
 op.setDeleteList(delList);
 JSState result = state.applyOp(op, alpha, list, delList);
 assertEquals(state, result);
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm condition = new JSPredicateForm("p(x)", new String[]{"x"});
 JSListLogicalAtoms expectedList = new JSListLogicalAtoms();
 expectedList.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSSubstitutionElement("a", "b"));
 JSListSubstitution satisfiers = axioms.TheoremProver(condition, state, alpha, false);
 JSSubstitution result = state.satisfies(condition, alpha, axioms);
 assertEquals(expectedSubstitution, result);
 assertEquals(expectedList, state);
 }

 @Test
 public void testSatisfiesNullCondition() {
 JSSubstitution result = state.satisfies(null, alpha, axioms);
 assertNull(result);
 }

 @Test
 public void testSatisfiesEmptyList() {
 JSListLogicalAtoms emptyList = new JSListLogicalAtoms();
 JSSubstitution result = state.satisfies(emptyList, alpha, axioms);
 assertNull(result);
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms condition1 = new JSListLogicalAtoms();
 condition1.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 condition1.addElement(new JSPredicateForm("p(b)", new String[]{"b"}));
 JSListLogicalAtoms condition2 = new JSListLogicalAtoms();
 condition2.addElement(new JSPredicateForm("p(b)", new String[]{"b"}));
 JSListLogicalAtoms expectedList = new JSListLogicalAtoms();
 expectedList.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 expectedList.addElement(new JSPredicateForm("p(b)", new String[]{"b"}));
 JSListSubstitution satisfiers1 = axioms.TheoremProver(condition1, state, alpha, false);
 JSListSubstitution satisfiers2 = axioms.TheoremProver(condition2, state, alpha, false);
 JSListSubstitution answers = state.satisfiesAll(condition1, alpha, axioms);
 JSListSubstitution result = new JSListSubstitution();
 result.addElement(new JSSubstitution());
 result.addElement(new JSSubstitution());
 assertEquals(answers, result);
 assertEquals(expectedList, state);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm condition = new JSPredicateForm("p(x)", new String[]{"x"});
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSSubstitutionElement("x", "a"));
 JSListSubstitution satisfiers = axioms.TheoremProver(condition, state, alpha, false);
 JSListSubstitution result = state.satisfiesTAm(condition, alpha);
 assertEquals(expectedSubstitution, result.elementAt(0));
 }

 @Test
 public void testSatisfiesTAmEmptyList() {
 JSListSubstitution result = state.satisfiesTAm(new JSPredicateForm("p(x)", new String[]{"x"}), alpha);
 assertEmpty(result);
 }

 @Test
 public void testSatisfiesTAmNullCondition() {
 JSListSubstitution result = state.satisfiesTAm(null, alpha);
 assertEmpty(result);
 }
}