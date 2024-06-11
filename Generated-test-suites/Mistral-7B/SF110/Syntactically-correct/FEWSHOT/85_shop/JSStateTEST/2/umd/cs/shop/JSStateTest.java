package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

 JSState emptyState = new JSState();
 JSState state1 = new JSState();
 JSState state2 = new JSState();
 JSState state3 = new JSState();
 JSState state4 = new JSState();
 JSState state5 = new JSState();
 JSState state6 = new JSState();
 JSListLogicalAtoms list1 = new JSListLogicalAtoms();
 JSListLogicalAtoms list2 = new JSListLogicalAtoms();
 JSListLogicalAtoms list3 = new JSListLogicalAtoms();
 JSListLogicalAtoms list4 = new JSListLogicalAtoms();
 JSListLogicalAtoms list5 = new JSListLogicalAtoms();
 JSSubstitution subs1 = new JSSubstitution();
 JSSubstitution subs2 = new JSSubstitution();
 JSSubstitution subs3 = new JSSubstitution();
 JSSubstitution subs4 = new JSSubstitution();
 JSSubstitution subs5 = new JSSubstitution();
 JSSubstitution subs6 = new JSSubstitution();
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 JSSubstitution gamma = new JSSubstitution();
 JSSubstitution delta = new JSSubstitution();
 JSSubstitution epsilon = new JSSubstitution();
 JSSubstitution zeta = new JSSubstitution();
 JSSubstitution eta = new JSSubstitution();
 JSSubstitution theta = new JSSubstitution();
 JSSubstitution iota = new JSSubstitution();
 JSSubstitution kappa = new JSSubstitution();

 @Test
 public void testJSStateConstructors() {
 JSState state = new JSState();
 assertNotNull(state);
 JSState stateWithTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
 assertNotNull(stateWithTokenizer);
 }

 @Test
 public void testAddElementsToState() {
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list2.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 list3.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list4.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 list4.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 state1.addElementsToState(list1);
 state2.addElementsToState(list2);
 state3.addElementsToState(list3);
 state4.addElementsToState(list4);
 assertEquals(1, state1.size());
 assertEquals(1, state2.size());
 assertEquals(1, state3.size());
 assertEquals(2, state4.size());
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm goal = new JSPredicateForm("p(x)", Arrays.asList("x"));
 alpha.addElement(new JSSubstitutionVariable("x", "a"));
 subs1 = state1.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNull(subs1.fail());
 subs1 = state1.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNotNull(subs1.size());
 assertEquals(1, subs1.size());
 subs2 = state2.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNull(subs2.fail());
 subs2 = state2.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNotNull(subs2.size());
 assertEquals(0, subs2.size());
 subs3 = state3.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNull(subs3.fail());
 subs3 = state3.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNotNull(subs3.size());
 assertEquals(1, subs3.size());
 subs4 = state4.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNull(subs4.fail());
 subs4 = state4.satisfies(Arrays.asList(goal), alpha, new JSListAxioms());
 assertNotNull(subs4.size());
 assertEquals(2, subs4.size());
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm goal1 = new JSPredicateForm("p(x)", Arrays.asList("x"));
 JSPredicateForm goal2 = new JSPredicateForm("q(x)", Arrays.asList("x"));
 alpha.addElement(new JSSubstitutionVariable("x", "a"));
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list2.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 list3.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list3.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 list4.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list5.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 list5.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 state1.addElementsToState(list1);
 state2.addElementsToState(list2);
 state3.addElementsToState(list3);
 state4.addElementsToState(list4);
 state5.addElementsToState(list5);
 subs1 = state1.satisfiesAll(Arrays.asList(goal1, goal2), alpha, new JSListAxioms());
 assertNotNull(subs1.size());
 assertEquals(0, subs1.size());
 subs2 = state2.satisfiesAll(Arrays.asList(goal1, goal2), alpha, new JSListAxioms());
 assertNotNull(subs2.size());
 assertEquals(0, subs2.size());
 subs3 = state3.satisfiesAll(Arrays.asList(goal1, goal2), alpha, new JSListAxioms());
 assertNotNull(subs3.size());
 assertEquals(1, subs3.size());
 subs4 = state4.satisfiesAll(Arrays.asList(goal1, goal2), alpha, new JSListAxioms());
 assertNotNull(subs4.size());
 assertEquals(2, subs4.size());
 subs5 = state5.satisfiesAll(Arrays.asList(goal1, goal2), alpha, new JSListAxioms());
 assertNotNull(subs5.size());
 assertEquals(3, subs5.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm goal = new JSPredicateForm("p(x)", Arrays.asList("x"));
 alpha.addElement(new JSSubstitutionVariable("x", "a"));
 list1.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 list2.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 state1.addElementsToState(list1);
 state2.addElementsToState(list2);
 subs1 = state1.satisfiesTAm(goal, alpha);
 assertNotNull(subs1.size());
 assertEquals(1, subs1.size());
 subs2 = state2.satisfiesTAm(goal, alpha);
 assertNotNull(subs2.size());
 assertEquals(0, subs2.size());
 }

 @Test
 public void testApplyOp() {
 JSPredicateForm op = new JSPredicateForm(":Operator op", Arrays.asList(":Operator op1"));
 JSSubstitution alpha1 = new JSSubstitution();
 alpha1.assign("op1", new JSSubstitutionVariable("x", "a"));
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p(x)", Arrays.asList("x")));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 delList.addElement(new JSPredicateForm("q(x)", Arrays.asList("x")));
 JSState stateOp = new JSState();
 stateOp.addElementsToState(addList);
 stateOp.addElementsToState(delList);
 JSState result = state1.applyOp(op, alpha1, addList, delList);
 assertEquals(2, result.size());
 }

 @Test
 public void testApply() {
 JSPlan plan = new JSPlan();
 JSState result = state1.apply(plan);
 JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
 assertNull(result);
 }
}