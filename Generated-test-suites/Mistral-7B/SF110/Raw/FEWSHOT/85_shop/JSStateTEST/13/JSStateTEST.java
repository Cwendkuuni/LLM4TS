package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {

 JSState createJSState() {
 return new JSState();
 }

 JSState createJSStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.label = label;
 return jsState;
 }

 @Test
 public void testJSState() {
 JSState jsState = createJSState();
 assertNull(jsState.label);
 assertEquals(0, jsState.size());
 }

 @Test
 public void testJSStateWithLabel() {
 JSState jsState = createJSStateWithLabel("testLabel");
 assertEquals("testLabel", jsState.label);
 }

 @Test
 public void testAddElementsToState() {
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 list.addElement(new JSPredicateForm("p2", new JSVariable("x"), new JSVariable("z")));
 JSState state = new JSState();
 state.addElementsToState(list);
 assertEquals(2, state.size());
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSState state = createJSState();
 state.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 state.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));

 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertEquals(state.size(), result.size());
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds1 = new JSListLogicalAtoms();
 conds1.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 JSListLogicalAtoms conds2 = new JSListLogicalAtoms();
 conds2.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 state.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));

 JSListSubstitution answers = state.satisfiesAll(conds1, alpha, axioms);
 assertEquals(state.size(), answers.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y"));
 JSSubstitution alpha = new JSSubstitution();
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 state.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));

 JSListSubstitution answers = state.satisfiesTAm(t, alpha);
 assertEquals(state.size(), answers.size());
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSOperator("op", new JSVariable("x"), new JSVariable("y"), new JSVariable("z"), null);
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3", new JSVariable("x"), new JSVariable("y")));
 addL.addElement(new JSPredicateForm("p4", new JSVariable("y"), new JSVariable("z")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 JSListLogicalAtoms opAddL = new JSListLogicalAtoms();
 opAddL.addElement(new JSPredicateForm("p5", new JSVariable("x"), new JSVariable("w")));
 opAddL.addElement(new JSPredicateForm("p6", new JSVariable("w"), new JSVariable("z")));
 JSListLogicalAtoms opDelL = new JSListLogicalAtoms();
 opDelL.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));
 JSState state = new JSState();
 state.addElement(new JSPredicateForm("p1", new JSVariable("x"), new JSVariable("y")));
 state.addElement(new JSPredicateForm("p2", new JSVariable("y"), new JSVariable("z")));

 JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(2, result.size());
 }

 @Test
 public void testApply() {
 JSPlan pl = new JSPlan();
 JSState state = new JSState();
 JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
 fail();
 }
}