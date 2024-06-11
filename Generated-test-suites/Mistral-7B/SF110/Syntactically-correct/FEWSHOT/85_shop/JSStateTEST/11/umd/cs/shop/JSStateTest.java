package umd.cs.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

 JSState createEmptyState() {
 return new JSState();
 }

 JSState createStateWithLabel(String label) {
 JSState state = new JSState();
 state.label = label;
 return state;
 }

 JSState createStateWithElements(JSListLogicalAtoms elements) {
 JSState state = new JSState();
 state.addElementsToState(elements);
 return state;
 }

 @Test
 public void testConstructors() {
 JSState emptyState = createEmptyState();
 JSState stateWithLabel = createStateWithLabel("label");
 assertNull(emptyState.getLabel());
 assertEquals("label", stateWithLabel.getLabel());
 }

 @Test
 public void testAddElementsToState() {
 JSState initialState = createEmptyState();
 JSListLogicalAtoms elements = new ArrayList<>();
 elements.add(new JSPredicateForm("p1"));
 elements.add(new JSPredicateForm("p2"));
 JSState stateWithElements = createStateWithElements(elements);

 assertEquals(0, initialState.size());
 assertEquals(2, stateWithElements.size());
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSOperator("op", new JSFunctionSymbol("f"), new ArrayList<>());
 JSListLogicalAtoms addList = new ArrayList<>();
 addList.add(new JSPredicateForm("p1"));
 JSListLogicalAtoms delList = new ArrayList<>();
 delList.add(new JSPredicateForm("p1"));

 JSState initialState = createEmptyState();
 JSState stateWithElement = createStateWithElements(new ArrayList<>());
 stateWithElement.addElement(new JSPredicateForm("p1"));

 JSState expectedResult = createStateWithElements(new ArrayList<>());
 expectedResult.addElement(new JSPredicateForm("p2"));

 JSState result = stateWithElement.applyOp(op, new JSSubstitution(), addList, delList);

 assertEquals(1, initialState.size());
 assertEquals(1, stateWithElement.size());
 assertEquals(1, expectedResult.size());
 assertEquals(1, result.size());
 assertNotSame(stateWithElement, result);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new ArrayList<>();
 conds.add(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSState initialState = createEmptyState();
 JSState stateWithElement = createStateWithElements(new ArrayList<>());
 stateWithElement.addElement(new JSPredicateForm("p1"));

 JSSubstitution answer = initialState.satisfies(conds, alpha, axioms);
 assertNotNull(answer);
 assertEquals(0, answer.size());

 answer = stateWithElement.satisfies(conds, alpha, axioms);
 assertNotNull(answer);
 assertEquals(1, answer.size());
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds1 = new ArrayList<>();
 conds1.add(new JSPredicateForm("p1"));
 JSListLogicalAtoms conds2 = new ArrayList<>();
 conds2.add(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSState initialState = createEmptyState();
 JSState stateWithElement1 = createStateWithElements(new ArrayList<>());
 stateWithElement1.addElement(new JSPredicateForm("p1"));
 JSState stateWithElement2 = createStateWithElements(new ArrayList<>());
 stateWithElement2.addElement(new JSPredicateForm("p2"));
 JSState stateWithBothElements = createStateWithElements(new ArrayList<>());
 stateWithBothElements.addElement(new JSPredicateForm("p1"));
 stateWithBothElements.addElement(new JSPredicateForm("p2"));

 List<JSSubstitution> answers1 = stateWithElement1.satisfiesAll(conds1, alpha, axioms);
 assertEquals(0, answers1.size());

 List<JSSubstitution> answers2 = stateWithElement2.satisfiesAll(conds1, alpha, axioms);
 assertEquals(0, answers2.size());

 List<JSSubstitution> answers3 = stateWithBothElements.satisfiesAll(conds1, alpha, axioms);
 assertEquals(1, answers3.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm goal = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSState initialState = createEmptyState();
 JSState stateWithElement = createStateWithElements(new ArrayList<>());
 stateWithElement.addElement(new JSPredicateForm("p1"));

 List<JSSubstitution> answers = stateWithElement.satisfiesTAm(goal, alpha);
 assertEquals(1, answers.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testApply() {
 JSPlan pl = new JSPlan();
 JSState initialState = createEmptyState();
 JSState stateWithElement = createStateWithElements(new ArrayList<>());

 JSState result = initialState.apply(pl);
 fail("apply method should not be called for JSState class");
 }
}