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
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;

public class JSStateTest {

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 JSState jsStateWithTokenizer = new JSState(new StreamTokenizer(new StringReader("")));
 assertNotNull(jsStateWithTokenizer);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 s.add(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 expected.add(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 assertEquals(expected, jsState);
 }

 @Test
 public void testApplyOpWhenAddListIsEmpty() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = new JSState();
 assertEquals(expected, result);
 }

 @Test
 public void testApplyOpWhenDeleteListIsEmpty() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = jsState;
 assertEquals(expected, result);
 }

 @Test
 public void testApplyOpWhenAddListContainsElementAlreadyInState() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = jsState;
 assertEquals(expected, result);
 }

 @Test
 public void testApplyOpWhenAddListDoesNotContainElementAlreadyInState() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 jsState.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = new JSListLogicalAtoms(jsState.elementAt(0), addL.get(0));
 assertEquals(expected, result);
 }

 @Test
 public void testApplyOpWhenDeleteListContainsElementAlreadyInState() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 expectedAddL.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 expectedDelL.add(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms add = op.addList();
 JSListLogicalAtoms del = op.deleteList();
 JSSubstitution alpha = new JSSubstitution();
 jsState.addElementsToState(expectedAddL);
 jsState.addElementsToState(expectedDelL);
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = new JSState();
 assertEquals(expected, result);
 }

 @Test
 public void testApplyOpWhenDeleteListDoesNotContainElementAlreadyInState() {
 JSState jsState = new JSState();
 JSOperator op = new JSOperator("op", new String[]{"x", "y"}, new String[]{"z"});
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 JSListLogicalAtoms add = op.addList();
 JSListLogicalAtoms del = op.deleteList();
 JSSubstitution alpha = new JSSubstitution();
 jsState.addElementsToState(expectedAddL);
 jsState.addElementsToState(expectedDelL);
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 JSState expected = new JSState(jsState.elements().clone());
 for (Object o : expectedAddL.elements()) {
 if (!expected.contains(o)) {
 expected.addElement(o);
 }
 }
 for (Object o : expectedDelL.elements()) {
 if (expected.contains(o)) {
 expected.removeElement(o);
 }
 }
 assertEquals(expected, result);
 }

 @Test
 public void testSatisfiesWhenConditionIsEmpty() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesWhenConditionsAreSatisfiedByState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 jsState.addElement(expected.get(0));
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertTrue(result.equals(alpha));
 }

 @Test
 public void testSatisfiesWhenConditionsAreNotSatisfiedByState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertFalse(result.isAssigned());
 }

 @Test
 public void testSatisfiesAllWhenConditionIsEmpty() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testSatisfiesAllWhenConditionsAreSatisfiedByState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 jsState.addElement(expected.get(0));
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(1, result.size());
 }

 @Test
 public void testSatisfiesTAmWhenAtomIsSatisfiedByState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 jsState.addElement(expected.get(0));
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm t = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(1, result.size());
 }

 @Test
 public void testSatisfiesTAmWhenAtomIsNotSatisfiedByState() {
 JSState jsState = new JSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSPredicateForm t = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSListSubstitution result = jsState.satisfiesTAm(t, new JSSubstitution());
 assertTrue(result.isEmpty());
 }
}