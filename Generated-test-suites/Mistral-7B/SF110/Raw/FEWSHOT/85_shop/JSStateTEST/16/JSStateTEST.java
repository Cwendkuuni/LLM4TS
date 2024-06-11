package umd.cs.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

 JSState createEmptyState() {
 return new JSState();
 }

 JSState createStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.label = label;
 return jsState;
 }

 JSState createStateWithElements(JSListLogicalAtoms elements) {
 JSState jsState = new JSState();
 jsState.addElementsToState(elements);
 return jsState;
 }

 JSState createStateWithLabelAndElements(String label, JSListLogicalAtoms elements) {
 JSState jsState = new JSState();
 jsState.label = label;
 jsState.addElementsToState(elements);
 return jsState;
 }

 @Test
 public void testConstructors() {
 JSState jsState1 = new JSState();
 JSState jsState2 = new JSState(null);
 assertEquals(0, jsState1.size());
 assertEquals(0, jsState2.size());
 }

 @Test
 public void testGetLabel() {
 JSState jsState = createStateWithLabel("Label1");
 assertEquals("Label1", jsState.getLabel());
 }

 @Test
 public void testSetLabel() {
 JSState jsState = createEmptyState();
 jsState.label = "Label1";
 assertEquals("Label1", jsState.getLabel());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetLabelThrowsException() {
 JSState jsState = createEmptyState();
 try {
 jsState.setLabel(null);
 } catch (UnsupportedOperationException e) {
 fail("Expected UnsupportedOperationException but got " + e.getClass());
 }
 }

 @Test
 public void testSize() {
 JSState jsState = createEmptyState();
 assertEquals(0, jsState.size());
 JSState jsStateWithElements = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 assertEquals(2, jsStateWithElements.size());
 }

 @Test
 public void testIsEmpty() {
 JSState jsState1 = createEmptyState();
 assertTrue(jsState1.isEmpty());
 JSState jsState2 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 assertFalse(jsState2.isEmpty());
 }

 @Test
 public void testContains() {
 JSState jsState = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 assertTrue(jsState.contains(new JSPredicateForm("p1")));
 assertFalse(jsState.contains(new JSPredicateForm("p3")));
 }

 @Test
 public void testInsertElementAt() {
 JSState jsState = createEmptyState();
 jsState.insertElementAt(new JSPredicateForm("p1"), 0);
 assertEquals(1, jsState.size());
 JSState jsState2 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 jsState2.insertElementAt(new JSPredicateForm("p0"), 0);
 assertEquals(3, jsState2.size());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createEmptyState();
 JSListLogicalAtoms elements = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2")));
 jsState.addElementsToState(elements);
 assertEquals(2, jsState.size());
 }

 @Test
 public void testApply() {
 fail("JSState.apply(JSPlan) method is not implemented.");
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSAdd();
 JSListLogicalAtoms addL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p3")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSState state1 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 JSState state2 = state1.applyOp(op, new JSSubstitution(), addL, delL);
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3")));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1")));
 JSListLogicalAtoms actualAddL = state2.getAddList();
 JSListLogicalAtoms actualDelL = state2.getDelList();
 assertEquals(expectedAddL, actualAddL);
 assertEquals(expectedDelL, actualDelL);
 assertEquals(3, state2.size());
 }

 @Test
 public void testSatisfies() {
 fail("JSState.satisfies(JSListLogicalAtoms, JSSubstitution, JSListAxioms) method is not implemented.");
 }

 @Test
 public void testSatisfiesAll() {
 fail("JSState.satisfiesAll(JSListLogicalAtoms, JSSubstitution, JSListAxioms) method is not implemented.");
 }

 @Test
 public void testSatisfiesTAm() {
 JSState jsState = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1(x, y)."), new JSPredicateForm("p2(y, z)."))));
 JSPredicateForm goal = new JSPredicateForm("p1(a, b).");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", "a");
 alpha.addBinding("y", "b");
 JSListSubstitution satisfiers = jsState.satisfiesTAm(goal, alpha);
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 expectedAnswer.addElement(alpha);
 assertEquals(expectedAnswer, satisfiers);
 }

 @Test
 public void testToString() {
 JSState jsState = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 String expectedString = "{JSState state = [p1, p2], addList = [], delList = []}";
 assertEquals(expectedString, jsState.toString());
 }

 @Test
 public void testHashCode() {
 JSState jsState1 = createEmptyState();
 JSState jsState2 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"))));
 JSState jsState3 = jsState2;
 JSState jsState4 = new JSState(jsState2);
 List<JSPredicateForm> elements = new ArrayList<>();
 elements.add(new JSPredicateForm("p1"));
 JSState jsState5 = createStateWithElements(new JSListLogicalAtoms(elements));
 assertEquals(jsState1.hashCode(), jsState1.hashCode());
 assertEquals(jsState1.hashCode(), jsState2.hashCode());
 assertNotEquals(jsState1.hashCode(), jsState3.hashCode());
 assertNotEquals(jsState1.hashCode(), jsState4.hashCode());
 assertNotEquals(jsState1.hashCode(), jsState5.hashCode());
 }

 @Test
 public void testEquals() {
 JSState jsState1 = createEmptyState();
 JSState jsState2 = createEmptyState();
 JSState jsState3 = new JSState(jsState1);
 JSState jsState4 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"))));
 JSState jsState5 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"))));
 JSState jsState6 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p2"))));
 JSState jsState7 = createStateWithElements(new JSListLogicalAtoms(Arrays.asList(new JSPredicateForm("p1"), new JSPredicateForm("p2"))));
 assertEquals(jsState1, jsState1);
 assertEquals(jsState1, jsState3);
 assertNotEquals(jsState1, jsState2);
 assertNotEquals(jsState1, jsState4);
 assertNotEquals(jsState1, jsState5);
 assertNotEquals(jsState1, jsState6);
 assertNotEquals(jsState1, jsState7);
 }
}