package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSState;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;

public class JSStateTest {

 private JSState createNewJSState() {
 return new JSState();
 }

 private JSState createJSStateFromTokenizer(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createNewJSState();
 assertNotNull(jsState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateConstructorWithNullTokenizer() {
 StreamTokenizer tokenizer = null;
 JSState jsState = createJSStateFromTokenizer(tokenizer);
 fail("Should throw IllegalArgumentException for null tokenizer");
 }

 @Test
 public void testAddElementsToState() {
 JSState state = createNewJSState();
 JSListLogicalAtoms list = new JSListLogicalAtoms();
 list.addElement(new JSPredicateForm("p", new String[]{"x", "y"}, new String[]{"int", "int"}, "1 = x + y"));
 list.addElement(new JSPredicateForm("q", new String[]{"z"}, new String[]{"int"}, "z >= 0"));

 state.addElementsToState(list);
 JSPredicateForm expected = list.elementAt(0);
 JSPredicateForm actual = state.elementAt(0);
 assertEquals(expected.getLabel(), actual.getLabel());
 assertEquals(expected.getPredicateSymbol(), actual.getPredicateSymbol());
 assertEquals(expected.getArguments(), actual.getArguments());
 assertArrayEquals(expected.getTypes(), actual.getTypes());
 }

 @Test
 public void testApply() {
 JSState state = createNewJSState();
 JSPlan plan = new JSPlan();
 JSUtil.flag20("JSState.apply(JSPlan) not implemented yet");
 JSState expected = createNewJSState();
 JSState actual = state.apply(plan);
 assertSame(expected, actual);
 }

 @Test
 public void testApplyOp() {
 JSState state = createNewJSState();
 JSOperator op = new JSOperator(":", new String[]{"int", "int"}, new String[]{"int"}, "x + y");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p", new String[]{"x", "y"}, new String[]{"int", "int"}, "1 = x + y"));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 JSListLogicalAtoms opAddL = addList; // for simplicity, assume opAddL = addList
 JSListLogicalAtoms opDelL = new JSListLogicalAtoms();
 opDelL.addElement(new JSPredicateForm("q", new String[]{"z"}, new String[]{"int"}, "z >= 0"));
 JSState ns = new JSState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();

 state.applyOp(op, alpha, addList, delList);

 // Check state:
 for (int i = 0; i < state.size(); ++i) {
 JSPredicateForm el = state.elementAt(i);
 // Check if el is in opAddL:
 boolean isInOpAddL = false;
 for (int j = 0; j < opAddL.size(); ++j) {
 if (opAddL.elementAt(j).getLabel().equalsIgnoreCase(el.getLabel())) {
 isInOpAddL = true;
 break;
 }
 }
 if (!isInOpAddL) {
 fail("El not in opAddL: " + el.getLabel());
 }
 }

 // Check nAddL:
 for (int i = 0; i < nAddL.size(); ++i) {
 JSPredicateForm el = nAddL.elementAt(i);
 // Check if el is in state but not in opDelL:
 boolean isInState = false;
 boolean isInOpDelL = false;
 for (int j = 0; j < state.size(); ++j) {
 if (state.elementAt(j).getLabel().equalsIgnoreCase(el.getLabel())) {
 isInState = true;
 if (opDelL.contains(state.elementAt(j))) {
 isInOpDelL = true;
 break;
 }
 }
 }
 if (!isInState || isInOpDelL) {
 fail("El not in expected state or not in opDelL: " + el.getLabel());
 }
 nAddL.addElements(opAddL);
 }

 // Check delL:
 for (int i = 0; i < delList.size(); ++i) {
 JSPredicateForm el = delList.elementAt(i);
 // Check if el is in state but not in opAddL:
 boolean isInState = false;
 boolean isInOpAddL = false;
 for (int j = 0; j < state.size(); ++j) {
 if (state.elementAt(j).getLabel().equalsIgnoreCase(el.getLabel())) {
 isInState = true;
 if (opAddL.contains(state.elementAt(j))) {
 isInOpAddL = true;
 break;
 }
 }
 }
 if (isInState && !isInOpAddL) {
 fail("El in state but not in opAddL: " + el.getLabel());
 }
 }

 JSState actual = state;
 JSState expected = ns;
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfies() {
 JSState state = createNewJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new String[]{"x", "y"}, new String[]{"int", "int"}, "1 = x + y"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();

 JSSubstitution answer = state.satisfies(conds, alpha, axioms);
 assertEquals(answer, new JSSubstitution());
 }

 @Test
 public void testSatisfiesWithSuccess() {
 JSState state = createNewJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p", new String[]{"x", "y"}, new String[]{"int", "int"}, "1 = x + y"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution satisfiers = axioms.TheoremProver(conds, state, alpha, false);
 assertFalse(satisfiers.fail());
 JSSubstitution answer = new JSSubstitution();
 answer.addElements(satisfiers.elementAt(0));
 assertEquals(answer, state.satisfies(conds, alpha, axioms));
 }
}