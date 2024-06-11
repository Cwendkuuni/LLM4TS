package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;

public class JSStateTest {

 private final JSState createState() {
 return new JSState();
 }

 @Test
 public void testJSStateEmptyConstructor() {
 JSState state = createState();
 assertNull(state.getLabel());
 assertTrue(state.isEmpty());
 }

 @Test
 public void testJSStateTokenizerConstructor() throws IOException {
 JSState state = new JSState(new StringReader("(state :label \"InitialState\")"));
 assertEquals("InitialState", state.getLabel());
 assertTrue(state.isEmpty());
 }

 @Test
 public void testJSStateAddElementsToState() {
 JSState state = createState();
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 list.addElement(new JSPredicateForm("q(y)", new JSSubstitution()));
 state.addElementsToState(list);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 expected.addElement(new JSPredicateForm("q(y)", new JSSubstitution()));
 List<JSPredicateForm> elements = state.getElements();
 assertEquals(expected.size(), elements.size());
 for (int i = 0; i < elements.size(); ++i) {
 assertEquals(expected.elementAt(i), elements.get(i));
 }
 }

 @Test
 public void testJSStateApply() {
 JSState state = createState();
 JSPlan plan = new JSPlan();
 JSState expected = createState();
 JSState result = state.apply(plan);
 assertEquals(expected, result);
 JSUtil.println("Unexpected application of JSState.apply(JSPlan)");
 }

 @Test
 public void testJSStateApplyOp() {
 JSState state = createState();
 JSOperator op = new JSOperator("op", new JSSubstitution(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("r(z)", new JSSubstitution()));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 expectedAddList.addElement(new JSPredicateForm("r(z)", new JSSubstitution()));
 JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p(x)", new JSSubstitution()));
 JSListLogicalAtoms add = op.addList();
 JSListLogicalAtoms del = op.deleteList();
 JSState resultState = state.applyOp(op, new JSSubstitution(), addList, delList);
 JSListLogicalAtoms addResult = resultState.getAddList();
 JSListLogicalAtoms delResult = resultState.getDelList();
 JSListLogicalAtoms stateElements = state.getElements();
 assertEquals(expectedAddList.size(), addResult.size());
 for (int i = 0; i < addResult.size(); ++i) {
 assertEquals(expectedAddList.elementAt(i), addResult.elementAt(i));
 }
 assertEquals(expectedDelList.size(), delResult.size());
 for (int i = 0; i < delResult.size(); ++i) {
 assertEquals(expectedDelList.elementAt(i), delResult.elementAt(i));
 }
 assertEquals(expectedState.size(), resultState.size());
 for (int i = 0; i < expectedState.size(); ++i) {
 JSPredicateForm expectedElement = expectedState.elementAt(i);
 JSPredicateForm resultElement = resultState.elementAt(i);
 assertEquals(expectedElement, resultElement);
 }
 }

 @Test
 public void testJSStateSatisfies() {
 JSState state = createState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution expected = new JSSubstitution();
 JSSubstitution result = state.satisfies(conds, alpha, axioms);
 assertEquals(expected, result);
 JSUtil.println("Unexpected satisfaction result of JSState.satisfies(JSListLogicalAtoms, JSSubstitution, JSListAxioms)");
 }

 @Test
 public void testJSStateSatisfiesAll() {
 JSState state = createState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 JSListLogicalAtoms add = new JSListLogicalAtoms();
 JSListLogicalAtoms del = new JSListLogicalAtoms();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected.size(), answers.size());
 for (int i = 0; i < answers.size(); ++i) {
 JSSubstitution answer = answers.elementAt(i);
 JSListLogicalAtoms answerElements = answer.getElements();
 assertEquals(expected.size(), answerElements.size());
 for (int j = 0; j < expected.size(); ++j) {
 JSPredicateForm expectedElement = expected.elementAt(j);
 JSPredicateForm answerElement = answerElements.elementAt(j);
 assertEquals(expectedElement, answerElement);
 }
 }
 }

 @Test
 public void testJSStateSatisfiesTAm() {
 JSState state = createState();
 JSPredicateForm t = new JSPredicateForm("p(x)", new JSSubstitution());
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesTAm(t, alpha);
 assertEquals(answers.isEmpty(), false);
 JSPredicateForm expected = new JSPredicateForm("p(x)", alpha);
 assertEquals(expected, answers.elementAt(0).getElements().get(0));
 }

}