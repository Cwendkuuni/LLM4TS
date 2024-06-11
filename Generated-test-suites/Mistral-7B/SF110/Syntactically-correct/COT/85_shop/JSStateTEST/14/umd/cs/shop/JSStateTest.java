import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSListAxioms;

public class JSStateTest {

 // Test constructor
 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertTrue(jsState instanceof JSState);
 }

 // Test JSState constructor with StreamTokenizer
 @Test
 public void testJSStateStreamTokenizerConstructor() {
 JSState jsState = new JSState(new StreamTokenizer());
 assertTrue(jsState instanceof JSState);
 }

 // Test addElementsToState method
 @Test
 public void testAddElementsToState() {
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 JSState jsState = new JSState();
 s.add(new JSPredicateForm("p1"));
 s.add(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 int size = jsState.size();
 assertTrue(size == 2);
 }

 // Test apply method (not implemented)
 @Test(expected = UnsupportedOperationException.class)
 public void testApply() {
 JSState jsState = new JSState();
 JSPlan pl = new JSPlan();
 jsState.apply(pl);
 }

 // Test applyOp method
 @Test
 public void testApplyOp() {
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.add(new JSPredicateForm("p4"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 expected.add(new JSPredicateForm("p3"));
 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, result);
 }

 // Test satisfies method
 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, answer.getState());
 }

 // Test satisfies method with empty list of conditions
 @Test
 public void testSatisfiesEmptyConditions() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertTrue(answer.isEmpty());
 }

 // Test satisfiesAll method
 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 conds.add(new JSPredicateForm("p2"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 List<JSSubstitution> answers = jsState.satisfiesAll(conds, alpha, axioms).getElements();
 List<JSSubstitution> expectedAnswers = new ArrayList<>();
 expectedAnswers.add(alpha);
 assertArrayEquals(expectedAnswers.toArray(), answers.toArray());
 }

 // Test satisfiesTAm method
 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p(x) :- p1(x), p2(x)");
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1(a)"));
 jsState.add(new JSPredicateForm("p2(a)"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("a", new JSSimpleTerm("a"));
 JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);
 int expectedSize = 2;
 assertEquals(expectedSize, answers.size());
 }

 // Test contains method
 @Test
 public void testContains() {
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 JSState anotherState = new JSState();
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 assertTrue(jsState.contains(new JSPredicateForm("p1")));
 assertFalse(jsState.contains(new JSPredicateForm("p2")));
 assertFalse(anotherState.contains(new JSPredicateForm("p1")));
 }

 // Test insertElementAt method
 @Test
 public void testInsertElementAt() {
 JSState jsState = new JSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p1"));
 s.add(new JSPredicateForm("p2"));
 jsState.insertElementAt(new JSPredicateForm("p0"), 0);
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p0"));
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 assertEquals(expected, jsState);
 }

 // Test TheoremProver method with failure
 @Test
 public void testTheoremProverFailure() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 JSState jsState = new JSState();
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertNull(answer.getState());
 }

 // Test TheoremProver method with success
 @Test
 public void testTheoremProverSuccess() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected, answer.getState());
 }

 // Test TheoremProver method with multiple solutions
 @Test
 public void testTheoremProverMultipleSolutions() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.add(new JSPredicateForm("p1"));
 conds.add(new JSPredicateForm("p2"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 jsState.add(new JSPredicateForm("p3"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 expected.add(new JSPredicateForm("p3"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);
 List<JSSubstitution> expectedAnswers = new ArrayList<>();
 expectedAnswers.add(new JSSubstitution()); // empty substitution
 expectedAnswers.add(new JSSubstitution()); // substitution with p3 mapped to a
 expectedAnswers.add(new JSSubstitution()); // substitution with p3 mapped to b
 assertEquals(expectedAnswers, answers.getElements());
 }

 // Test applyOp method with empty add and delete lists
 @Test
 public void testApplyOpEmptyLists() {
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, result);
 }

 // Test applyOp method with overlapping add and delete lists
 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpOverlappingLists() {
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p1"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.add(new JSPredicateForm("p1"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 jsState.applyOp(op, new JSSubstitution(), addL, delL);
 }

 // Test applyOp method with add list containing an element not in the state
 @Test
 public void testApplyOpAddListNotInState() {
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 expected.add(new JSPredicateForm("p3"));
 JSState result = jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, result);
 }

 // Test applyOp method with delete list containing an element not in the state
 @Test
 public void testApplyOpDeleteListNotInState() {
 JSOperator op = new JSOperator("op", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.add(new JSPredicateForm("p3"));
 JSState jsState = new JSState();
 jsState.add(new JSPredicateForm("p1"));
 jsState.add(new JSPredicateForm("p2"));
 JSState expected = new JSState();
 expected.add(new JSPredicateForm("p1"));
 expected.add(new JSPredicateForm("p2"));
 jsState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expected, jsState);
 }
}