package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitutionList;

public class JSStateTest {

 private JSState state;
 private JSListLogicalAtoms list1;
 private JSListLogicalAtoms list2;
 private JSListLogicalAtoms list3;
 private JSOperator op;
 private JSPlan plan;
 private JSListAxioms axioms;
 private JSSubstitution alpha;

 @Before
 public void setUp() {
 state = new JSState();
 list1 = new JSListLogicalAtoms();
 list2 = new JSListLogicalAtoms();
 list3 = new JSListLogicalAtoms();
 op = new JSOperator();
 plan = new JSPlan();
 axioms = new JSListAxioms();
 alpha = new JSSubstitution();
 }

 @Test
 public void testConstructor() {
 JSState state1 = new JSState();
 JSState state2 = new JSState(new StreamTokenizer());
 assertEquals(0, state1.size());
 assertEquals(0, state2.size());
 }

 @Test
 public void testAddElementsToState() {
 list1.addElement(new JSPredicateForm("p", new Object[]{}));
 list1.addElement(new JSPredicateForm("q", new Object[]{}));
 list1.addElement(new JSPredicateForm("r", new Object[]{}));
 state.addElementsToState(list1);
 assertEquals(3, state.size());
 }

 @Test
 public void testApply() {
 fail("JSState.apply() not implemented");
 }

 @Test
 public void testApplyOp() {
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p", new Object[]{}));
 expectedState.addElement(new JSPredicateForm("r", new Object[]{}));
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("q", new Object[]{}));
 addList.addElement(new JSPredicateForm("s", new Object[]{}));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 delList.addElement(new JSPredicateForm("r", new Object[]{}));
 op = new JSOperator(":op", new Object[]{}, new JSListLogicalAtoms[]{addList, delList});
 JSState result = state.applyOp(op, alpha, list1, list2);
 assertEquals(expectedState, result);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitution("x", new Object[]{"y"}));
 JSSubstitution answer = state.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new Object[]{}));
 conds.addElement(new JSPredicateForm("q", new Object[]{}));
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitution("x", new Object[]{"y"}));
 expectedAnswer.addElement(new JSSubstitution("z", new Object[]{"w"}));
 JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswer, answers.elementAt(0));
 assertEquals(2, answers.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm goal = new JSPredicateForm("p", new Object[]{});
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitution("x", new Object[]{"y"}));
 JSListSubstitution answers = state.satisfiesTAm(goal, alpha);
 assertEquals(expectedAnswer, answers.elementAt(0));
 }
}