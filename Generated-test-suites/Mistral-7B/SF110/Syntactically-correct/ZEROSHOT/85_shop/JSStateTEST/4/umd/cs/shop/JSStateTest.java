package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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

 private JSState createStateWithLabel(String label) {
 JSState state = new JSState();
 state.label = label;
 return state;
 }

 private JSState createStateWithElements(JSPredicateForm... elements) {
 JSState state = new JSState();
 for (JSPredicateForm element : elements) {
 state.addElement(element);
 }
 return state;
 }

 @Test
 public void testConstructor() {
 JSState state = new JSState();
 assertNull(state.label);
 assertEquals(0, state.size());
 }

 @Test
 public void testConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a)"));
 JSState state = new JSState(tokenizer);
 assertEquals("(a)", state.toString());
 }

 @Test
 public void testAddElementsToState() {
 JSState state1 = createEmptyState();
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p"));
 list.addElement(new JSPredicateForm("q"));
 state1.addElementsToState(list);
 JSState expectedState = createStateWithElements(new JSPredicateForm("p"), new JSPredicateForm("q"));
 assertEquals(expectedState, state1);

 JSState state2 = createStateWithElements(new JSPredicateForm("p"), new JSPredicateForm("p"));
 JSListLogicalAtoms list2 = new JSListLogicalAtoms();
 list2.addElement(new JSPredicateForm("q"));
 state2.addElementsToState(list2);
 JSState expectedState2 = createStateWithElements(new JSPredicateForm("p"), new JSPredicateForm("p"), new JSPredicateForm("q"));
 assertEquals(expectedState2, state2);
 }

 @Test
 public void testApplyOpWithEmptyState() {
 JSOperator op = new JSOperator("op", new JSPredicateForm("p"), Arrays.asList(new JSPredicateForm("q")));
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSState expectedState = createEmptyState();
 JSTState result = new JSTState(expectedState, new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSTState actualState = new JSState().applyOp(op, alpha, addL, delL);
 assertEquals(result, actualState);
 }

 @Test
 public void testApplyOpWithStateContainingElementToBeAdded() {
 JSOperator op = new JSOperator("op", new JSPredicateForm("p"), Arrays.asList(new JSPredicateForm("q")));
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState state = createStateWithElements(new JSPredicateForm("p"));
 JSTate expectedState = new JSState().addElement(new JSPredicateForm("q")).applyOp(op, new JSSubstitution(), addL, delL);
 JSTate actualState = state.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expectedState, actualState);
 }

 @Test
 public void testApplyOpWithStateContainingElementToBeDeleted() {
 JSOperator op = new JSOperator("op", new JSPredicateForm("p"), Arrays.asList(new JSPredicateForm("q")));
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("r"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 delL.addElement(new JSPredicateForm("p"));
 JSState state = createStateWithElements(new JSPredicateForm("p"), new JSPredicateForm("q"), new JSPredicateForm("r"));
 JSTate expectedState = new JSState().addElement(new JSPredicateForm("r")).applyOp(op, new JSSubstitution(), addL, delL);
 JSTate actualState = state.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expectedState, actualState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpWithNullOp() {
 JSState state = createStateWithElements(new JSPredicateForm("p"));
 state.applyOp(null, new JSSubstitution(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpWithNullAddL() {
 JSOperator op = new JSOperator("op", new JSPredicateForm("p"), Arrays.asList(new JSPredicateForm("q")));
 JSState state = createStateWithElements(new JSPredicateForm("p"));
 state.applyOp(op, new JSSubstitution(), null, new JSListLogicalAtoms());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOpWithNullDelL() {
 JSOperator op = new JSOperator("op", new JSPredicateForm("p"), Arrays.asList(new JSPredicateForm("q")));
 JSState state = createStateWithElements(new JSPredicateForm("p"));
 state.applyOp(op, new JSSubstitution(), new JSListLogicalAtoms(), null);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p(a, b)"));
 JSState state = createStateWithElements(new JSPredicateForm("p(a, a)"), new JSPredicateForm("p(a, b)"), new JSPredicateForm("p(b, a)"), new JSPredicateForm("p(b, b)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSSubstitution actualAnswer = state.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, actualAnswer);

 JSListLogicalAtoms conds2 = new JSListLogicalAtoms();
 conds2.addElement(new JSPredicateForm("p(a, b)"));
 JSState state2 = createStateWithElements(new JSPredicateForm("p(a, a)"), new JSPredicateForm("p(b, a)"), new JSPredicateForm("p(b, b)"));
 JSSubstitution actualAnswer2 = state2.satisfies(conds2, alpha, axioms);
 JSSubstitution expectedAnswer2 = new JSSubstitution();
 expectedAnswer2.assignFailure();
 assertNotEquals(expectedAnswer2, actualAnswer2);
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAnswers = new JSListLogicalAtoms();
 expectedAnswers.addElement(new JSPredicateForm("q(a, b)"));
 expectedAnswers.addElement(new JSPredicateForm("r(b, c)"));
 JSState state = createStateWithElements(new JSPredicateForm("p(a, a)"), new JSPredicateForm("q(a, b)"), new JSPredicateForm("r(b, b)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
 JSListSubstitution expectedAnswers2 = new JSListSubstitution();
 expectedAnswers2.addElement(new JSSubstitution().addElement(new JSPredicateForm("a", "a1")).addElement(new JSPredicateForm("b", "b1")))
 .addElement(new JSSubstitution().addElement(new JSPredicateForm("b", "b2")).addElement(new JSPredicateForm("c", "c1")));
 assertEquals(expectedAnswers2, answers);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p(a, a)");
 JSState state = createStateWithElements(new JSPredicateForm("p(a, a)"), new JSPredicateForm("p(a, b)"), new JSPredicateForm("p(b, a)"), new JSPredicateForm("p(b, b)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 JSListSubstitution actualAnswers = state.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswers, actualAnswers);
 }

 @Test
 public void testSatisfiesTAmWithFailure() {
 JSPredicateForm t = new JSPredicateForm("p(a, c)");
 JSState state = createStateWithElements(new JSPredicateForm("p(a, a)"), new JSPredicateForm("p(a, b)"), new JSPredicateForm("p(b, a)"), new JSPredicateForm("p(b, b)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.assignFailure();
 JSListSubstitution actualAnswers = state.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswers, actualAnswers);
 }
}