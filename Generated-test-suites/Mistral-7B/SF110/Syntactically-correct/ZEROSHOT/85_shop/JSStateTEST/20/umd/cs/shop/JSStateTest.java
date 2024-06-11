package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateFormElement;

public class JSStateTest {

 private JSState state;
 private JSListLogicalAtoms list;
 private JSListLogicalAtoms addList;
 private JSListLogicalAtoms delList;
 private JSOperator op;
 private JSPlan plan;
 private JSSubstitution alpha;
 private JSListAxioms axioms;

 @Before
 public void setUp() {
 state = new JSState();
 list = new JSListLogicalAtoms();
 addList = new JSListLogicalAtoms();
 delList = new JSListLogicalAtoms();
 op = new JSOperator();
 plan = new JSPlan();
 alpha = new JSSubstitution();
 axioms = new JSListAxioms();
 }

 @Test
 public void testJSStateConstructor() {
 JSState actualJSState = new JSState();
 JSState expectedJSState = new JSState();
 assertEquals("JSState objects should be equal when constructed", expectedJSState, actualJSState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
JSState actualJSState = new JSState(tokenizer);
JSState expectedJSState = new JSState();
 assertEquals("JSState objects should be equal when constructed with tokenizer", expectedJSState, actualJSState);
 }

 @Test
 public void testAddElementsToState() {
 addList.addElement(new JSPredicateFormElement("p1"));
 addList.addElement(new JSPredicateFormElement("p2"));
 state.addElementsToState(list);
 state.addElementsToState(addList);
 List<JSPredicateFormElement> expectedElements = new ArrayList<>();
 expectedElements.add(new JSPredicateFormElement("p1"));
 expectedElements.add(new JSPredicateFormElement("p2"));
 List<JSPredicateFormElement> actualElements = new ArrayList<>();
 for (Object obj : state.toArray()) {
 actualElements.add((JSPredicateFormElement) obj);
 }
 assertEquals("States elements should match after addElementsToState method call", expectedElements, actualElements);
 }

 @Test
 public void testApply() {
 fail("JSState apply method is not implemented");
 }

 @Test
 public void testApplyOp() {
 JSOperator addOp = new JSOperator("+", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSOperator delOp = new JSOperator("-", new JSListLogicalAtoms(), new JSListLogicalAtoms());
 addList.addElement(new JSPredicateFormElement("p1"));
 delList.addElement(new JSPredicateFormElement("p2"));
 state.addElementsToState(addList);
 state.addElementsToState(delList);
 JSTState expectedState = new JSTState(new JSState(), new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSTState actualState = state.applyOp(op, alpha, addList, delList);
 assertEquals("States should be equal after applyOp method call", expectedState, actualState);
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm cond = new JSPredicateForm("p(X)", null);
 JSPredicateForm atom1 = new JSPredicateForm("p(a)", null);
 JSPredicateForm atom2 = new JSPredicateForm("p(b)", null);
 list.addElement(atom1);
 list.addElement(atom2);
 state.addElementsToState(list);
 JSSubstitution alphaSubstitution = new JSSubstitution();
 alphaSubstitution.addElement(new JSSubstitutionElement("X", "a"));
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitutionElement("X", "a"));
 JSSubstitution actualAnswer = state.satisfies(cond, alpha, axioms);
 assertEquals("Satisfies method should return expected answer", expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm cond1 = new JSPredicateForm("p(X)", null);
 JSPredicateForm cond2 = new JSPredicateForm("q(X)", null);
 JSPredicateForm atom1 = new JSPredicateForm("p(a)", null);
 JSPredicateForm atom2 = new JSPredicateForm("p(b)", null);
 JSPredicateForm atom3 = new JSPredicateForm("q(c)", null);
 list.addElement(atom1);
 list.addElement(atom2);
 state.addElementsToState(list);
 JSSubstitution alphaSubstitution = new JSSubstitution();
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitutionElement("X", "a"));
 expectedAnswer.addElement(new JSSubstitutionElement("X", "b"));
 expectedAnswer.addElement(new JSSubstitutionElement("X", "c"));
 JSListSubstitution satisfiers = axioms.TheoremProver(new JSListLogicalAtoms(cond1, cond2), state, alphaSubstitution, true);
 JSListSubstitution actualAnswer = state.satisfiesAll(cond1, alpha, axioms);
 assertEquals("SatisfiesAll method should return expected answer", expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p(X)", null);
 JSPredicateForm atom = new JSPredicateForm("p(a)", null);
 list.addElement(atom);
 state.addElementsToState(list);
 JSSubstitution alphaSubstitution = new JSSubstitution();
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSSubstitutionElement("X", "a"));
 JSListSubstitution actualAnswer = state.satisfiesTAm(t, alpha);
 assertEquals("SatisfiesTAm method should return expected answer", expectedAnswer, actualAnswer);
 }
}