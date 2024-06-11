package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {

 JSState createJSState() {
 return new JSState();
 }

 @Test
 public void testJSStateConstruction() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p1"));
 expected.addElement(new JSPredicateForm("p2"));
 assertEquals(expected, jsState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNull() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = null;
 jsState.addElementsToState(s);
 }

 @Test
 public void testJSStateContains() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 jsState.addElementsToState(s);
 assertTrue(jsState.contains(new JSPredicateForm("p1")));
 assertFalse(jsState.contains(new JSPredicateForm("p3")));
 }

 @Test
 public void testJSStateInsertElementAt() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 jsState.insertElementAt(new JSPredicateForm("p0"), 0);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p0"));
 expected.addElement(new JSPredicateForm("p1"));
 expected.addElement(new JSPredicateForm("p2"));
 assertEquals(expected, jsState);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertElementAtWithInvalidIndex() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 jsState.addElementsToState(s);
 jsState.insertElementAt(new JSPredicateForm("p0"), jsState.size() + 1);
 }

 @Test
 public void testJSStateSize() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 jsState.addElementsToState(s);
 assertEquals(1, jsState.size());
 jsState.addElementsToState(s);
 assertEquals(2, jsState.size());
 }

 @Test
 public void testJSStateApplyOp() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.addElement(new JSPredicateForm("p1"));
 JSState expected = createJSState();
 expected.addElement(new JSPredicateForm("p2"));
 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, result);
 }

 @Test
 public void testJSStateSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertNull(answer);
 }

 @Test
 public void testJSStateSatisfiesWithSatisfiyingConditions() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(alpha, answer);
 }

 @Test
 public void testJSStateSatisfiesAll() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 conds.addElement(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answer = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(alpha, answer.elementAt(0));
 }

 @Test
 public void testJSStateSatisfiesTAm() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSPredicateForm t = new JSPredicateForm("p(x) :- p1(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answer = jsState.satisfiesTAm(t, alpha);
 assertEquals(0, answer.size());
 }

 @Test
 public void testJSStateSatisfiesTAmWithMatchingAtom() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSPredicateForm t = new JSPredicateForm("p(x) :- p1(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answer = jsState.satisfiesTAm(t, alpha);
 assertEquals(1, answer.size());
 }

}