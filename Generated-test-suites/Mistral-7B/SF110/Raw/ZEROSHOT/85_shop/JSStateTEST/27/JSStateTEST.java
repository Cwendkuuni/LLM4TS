package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTState;
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
 public void testJSStateConstructorWithTokenizer() {
 JSState state = new JSState(new StreamTokenizer(new StringReader("")));
 assertNull(state.getLabel());
 assertEquals(0, state.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateAddElementsToStateWithNullList() {
 JSState state = new JSState();
 state.addElementsToState(null);
 }

 @Test
 public void testJSStateAddElementsToState() {
 JSState state = new JSState();
 List<JSPredicateForm> list = new ArrayList<>();
 list.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms s = new JSListLogicalAtoms(list);
 state.addElementsToState(s);
 List<JSPredicateForm> expectedList = new ArrayList<>();
 expectedList.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 List<JSPredicateForm> actualList = state.toList();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testJSStateApply() {
 JSState state = new JSState();
 JSPlan plan = new JSPlan();
 JSState result = state.apply(plan);
 fail("JSState apply method is not implemented yet.");
 }

 @Test
 public void testJSStateApplyOpWithNullOperators() {
 JSState state = new JSState();
 JSOperator nullOperator1 = null;
 JSOperator nullOperator2 = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSTState result = state.applyOp(nullOperator1, alpha, addList, delList);
 fail("JSState applyOp method is not implemented yet.");
 }

 @Test
 public void testJSStateApplyOp() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSOperator op = new JSOperator("op", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("q1", new String[]{"z"}, new String[]{"w"}));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSState result = state.applyOp(op, alpha, addList, delList);
 assertEquals(expectedState, result.getState());
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 expectedAddList.addElement(new JSPredicateForm("q1", new String[]{"z"}, new String[]{"w"}));
 JSListLogicalAtoms actualAddList = result.getAddList();
 assertEquals(expectedAddList, actualAddList);
 JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
 JSListLogicalAtoms actualDelList = result.getDelList();
 assertEquals(expectedDelList, actualDelList);
 }

 @Test
 public void testJSStateSatisfies() {
 JSState state = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = state.satisfies(conds, alpha, axioms);
 assertTrue(satisfiers.isEmpty());
 }

 @Test
 public void testJSStateSatisfiesWithSatisfiers() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"z"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSPredicateForm("x", new String[]{}, new String[]{"z"}));
 expectedAnswers.addElement(expectedSubstitution);
 assertEquals(expectedAnswers, satisfiers);
 }

 @Test
 public void testJSStateSatisfiesTAmWithMatch() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSPredicateForm t = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution satisfiers = state.satisfiesTAm(t, alpha);
 List<JSSubstitution> expectedAnswers = new ArrayList<>();
 expectedAnswers.add(alpha);
 assertEquals(expectedAnswers, satisfiers);
 }

 @Test
 public void testJSStateSatisfiesTAmWithNoMatch() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSPredicateForm t = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"});
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution satisfiers = state.satisfiesTAm(t, alpha);
 assertTrue(satisfiers.isEmpty());
 }

 @Test
 public void testJSStateSatisfiesAll() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = state.satisfiesAll(conds, alpha, axioms);
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSPredicateForm("x", new String[]{}, new String[]{"y"}));
 expectedAnswers.addElement(expectedSubstitution);
 assertEquals(expectedAnswers, satisfiers);
 }

 @Test
 public void testJSStateSatisfiesAllWithMultipleSatisfiers() {
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 state.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = state.satisfiesAll(conds, alpha, axioms);
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 JSSubstitution expectedSubstitution1 = new JSSubstitution();
 expectedSubstitution1.addElement(new JSPredicateForm("x", new String[]{}, new String[]{"y"}));
 JSSubstitution expectedSubstitution2 = new JSSubstitution();
 expectedSubstitution2.addElement(new JSPredicateForm("x", new String[]{}, new String[]{"y"}));
 expectedAnswers.addElement(expectedSubstitution1);
 expectedAnswers.addElement(expectedSubstitution2);
 assertEquals(expectedAnswers, satisfiers);
 }

}