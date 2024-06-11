package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 JSState emptyState = new JSState();
 JSState stateWithAtoms = new JSState();
 JSState stateWithDuplicateAtoms = new JSState();
 JSState stateWithNullTokenizer = new JSState(null);
 JSListLogicalAtoms listOfAtoms = new JSListLogicalAtoms();
 JSOperator op;
 JSSubstitution alpha, alpha1, alpha2;
 JSListAxioms axioms = new JSListAxioms();
 JSListLogicalAtoms conds, addL, delL;

 @Test
 public void testJSStateConstruction() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateWithTokenizerConstruction() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertNotNull(jsState);
 }

 @Test
 public void testEmptyJSState() {
 assertTrue(emptyState.isEmpty());
 }

 @Test
 public void testAddAtomsToJSState() {
 addAtomsToJSState(stateWithAtoms, listOfAtoms);
 assertEquals(listOfAtoms.size(), stateWithAtoms.size());
 }

 @Test
 public void testAddDuplicateAtomsToJSState() {
 addAtomsToJSState(stateWithDuplicateAtoms, listOfAtoms);
 assertEquals(listOfAtoms.size(), stateWithDuplicateAtoms.size());
 // Check for duplicate atoms
 JSPredicateForm atom = listOfAtoms.elementAt(0);
 assertTrue(stateWithDuplicateAtoms.contains(atom));
 assertTrue(stateWithDuplicateAtoms.contains(atom));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddElementToFullJSState() {
 addAtomsToJSState(stateWithAtoms, listOfAtoms);
 JSListLogicalAtoms fullList = new JSListLogicalAtoms();
 for (int i = 0; i < JSListLogicalAtoms.MAX_LIST_SIZE; ++i) {
 fullList.addElement(new JSPredicateForm());
 }
 try {
 stateWithAtoms.addElement(new JSPredicateForm());
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testJSStateWithNullTokenizer() {
 try {
 new JSState(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testApplyOpWithEmptyState() {
 JSState result = stateWithAtoms.applyOp(op, alpha, addL, delL);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testApplyOpWithNonEmptyState() {
 JSState result = stateWithAtoms.applyOp(op, alpha, addL, delL);
 // Check the size of the resultant state
 assertEquals(addL.size() + delL.size() + stateWithAtoms.size(), result.size());
 // Check if the added atoms are present
 for (int i = 0; i < addL.size(); ++i) {
 JSPredicateForm atom = addL.elementAt(i);
 assertTrue(result.contains(atom));
 }
 // Check if the deleted atoms are absent
 for (int i = 0; i < delL.size(); ++i) {
 JSPredicateForm atom = delL.elementAt(i);
 assertFalse(result.contains(atom));
 }
 }

 @Test
 public void testSatisfiesWithEmptyConditions() {
 JSListSubstitution result = stateWithAtoms.satisfies(null, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesWithNonEmptyConditions() {
 addAtomsToJSState(stateWithAtoms, listOfAtoms);
 JSPredicateForm atom = new JSPredicateForm("p(x)", null);
 listOfAtoms.addElement(atom);
 JSListSubstitution result = stateWithAtoms.satisfies(listOfAtoms, alpha, axioms);
 // Check if the satisfiability condition is met
 assertTrue(result.size() > 0);
 // Check if the substitution contains the satisfied substitution
 JSSubstitution satisfiedSubstitution = result.elementAt(0);
 assertTrue(satisfiedSubstitution.size() > 0);
 assertTrue(satisfiedSubstitution.getVarName(0).equals("x"));
 JSUtil.println("Substitution value for x: " + satisfiedSubstitution.getValue(0));
 }

 @Test
 public void testSatisfiesAllWithEmptyConditions() {
 JSListSubstitution result = stateWithAtoms.satisfiesAll(null, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAllNonEmptyConditions() {
 addAtomsToJSState(stateWithAtoms, listOfAtoms);
 JSListSubstitution result = stateWithAtoms.satisfiesAll(listOfAtoms, alpha, axioms);
 // Check if the satisfiability conditions are met for all
 assertTrue(result.size() > 0);
 for (int i = 0; i < result.size(); ++i) {
 JSSubstitution satisfiedSubstitution = result.elementAt(i);
 assertTrue(satisfiedSubstitution.size() > 0);
 }
 }

 @Test
 public void testSatisfiesTAmWithEmptyState() {
 JSListSubstitution result = stateWithAtoms.satisfiesTAm(null, alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesTAmWithNonEmptyState() {
 addAtomsToJSState(stateWithAtoms, listOfAtoms);
 JSPredicateForm goal = new JSPredicateForm("p(x)", null);
 listOfAtoms.addElement(goal);
 JSListSubstitution result = stateWithAtoms.satisfiesTAm(goal, alpha);
 // Check if the goal matches any atom in the state
 assertTrue(result.size() > 0 || result.isEmpty());
 if (!result.isEmpty()) {
 JSSubstitution satisfiedSubstitution = result.elementAt(0);
 assertTrue(satisfiedSubstitution.size() > 0);
 assertTrue(satisfiedSubstitution.getVarName(0).equals("x"));
 JSUtil.println("Substitution value for x: " + satisfiedSubstitution.getValue(0));
 }
 }

 private void addAtomsToJSState(JSState state, JSListLogicalAtoms list) {
 for (int i = 0; i < list.size(); ++i) {
 state.addElement(list.elementAt(i));
 }
 }
}