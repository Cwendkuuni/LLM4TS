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
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSListSubstitution;

public class JSStateTest {

 private JSState createEmptyState() {
 return new JSState();
 }

 private JSState createStateWithLabel(String label) {
 JSState state = new JSState();
 state.setLabel(label);
 return state;
 }

 private JSState createStateWithElement(JSState state, JSPredicateForm predicateForm) {
 state.addElement(predicateForm);
 return state;
 }

 private JSState createStateWithElements(JSState state, JSPredicateForm... predicateForms) {
 for (JSPredicateForm predicateForm : predicateForms) {
 state = createStateWithElement(state, predicateForm);
 }
 return state;
 }

 private JSState createStateWithLabelAndElement(String label, JSPredicateForm predicateForm) {
 JSState state = createStateWithLabel(label);
 state = createStateWithElement(state, predicateForm);
 return state;
 }

 private JSState createStateWithLabelAndElements(String label, JSPredicateForm... predicateForms) {
 JSState state = createStateWithLabel(label);
 return createStateWithElements(state, predicateForms);
 }

 private JSState createStateWithAddList(JSState state, JSListLogicalAtoms addList) {
 JSState result = new JSState();
 JSState ns = new JSState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 result = result.applyOp(new JSOperator(), new JSSubstitution(), state, addList);
 ns = (JSState)result.getNS();
 nAddL = (JSListLogicalAtoms)result.getNAddL();
 nDelL = (JSListLogicalAtoms)result.getNDelL();
 result = new JSState(ns, nAddL, nDelL);
 return result;
 }

 private JSState createStateWithDeleteList(JSState state, JSListLogicalAtoms delList) {
 JSState result = new JSState();
 JSState ns = new JSState();
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 result = result.applyOp(new JSOperator(), new JSSubstitution(), state, null, delList);
 ns = (JSState)result.getNS();
 nAddL = (JSListLogicalAtoms)result.getNAddL();
 nDelL = (JSListLogicalAtoms)result.getNDelL();
 result = new JSState(ns, nAddL, nDelL);
 return result;
 }

 private JSState createStateWithConditionsAndSubstitution(JSState state, JSListLogicalAtoms conditions, JSSubstitution substitution) {
 JSListSubstitution answers = state.satisfiesAll(conditions, substitution, new JSListAxioms());
 return (JSState)answers.elementAt(0);
 }

 @Test
 public void testEmptyState() {
 JSState state = createEmptyState();
 assertTrue(state.size() == 0);
 }

 @Test
 public void testCreateStateWithLabel() {
 String label = "testLabel";
 JSState state = createStateWithLabel(label);
 assertEquals(label, state.getLabel());
 }

 @Test
 public void testCreateStateWithLabelAndElement() {
 String label = "testLabel";
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSState state = createStateWithLabelAndElement(label, predicateForm);
 assertEquals(label, state.getLabel());
 assertEquals(1, state.size());
 }

 @Test
 public void testCreateStateWithLabelAndElements() {
 String label = "testLabel";
 JSPredicateForm[] predicateForms = new JSPredicateForm[2];
 for (int i = 0; i < predicateForms.length; ++i) {
 predicateForms[i] = new JSPredicateForm();
 }
 JSState state = createStateWithLabelAndElements(label, predicateForms);
 assertEquals(label, state.getLabel());
 assertEquals(predicateForms.length, state.size());
 }

 @Test
 public void testApplyOp_addList() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSListLogicalAtoms addList = new JSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p3"));
 JSState result = createStateWithAddList(state, addList);
 JSListLogicalAtoms nAddL = (JSListLogicalAtoms)result.getNAddL();
 JSListLogicalAtoms nDelL = (JSListLogicalAtoms)result.getNDelL();
 JSState ns = (JSState)result.getNS();
 JSListLogicalAtoms expectedNAddL = new JSListLogicalAtoms();
 expectedNAddL.addElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSListLogicalAtoms expectedNDelL = new JSListLogicalAtoms();
 assertEquals(expectedNAddL, nAddL);
 assertEquals(expectedNDelL, nDelL);
 assertEquals(3, ns.size());
 }

 @Test
 public void testApplyOp_deleteList() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"), new JSPredicateForm("p3"));
 JSListLogicalAtoms delList = new JSListLogicalAtoms();
 delList.addElement(new JSPredicateForm("p2"));
 JSState result = createStateWithDeleteList(state, delList);
 JSListLogicalAtoms nAddL = (JSListLogicalAtoms)result.getNAddL();
 JSListLogicalAtoms nDelL = (JSListLogicalAtoms)result.getNDelL();
 JSState ns = (JSState)result.getNS();
 JSListLogicalAtoms expectedNAddL = new JSListLogicalAtoms();
 expectedNAddL.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms expectedNDelL = new JSListLogicalAtoms();
 expectedNDelL.addElement(new JSPredicateForm("p2"));
 assertEquals(expectedNAddL, nAddL);
 assertEquals(expectedNDelL, nDelL);
 assertEquals(1, ns.size());
 }

 @Test
 public void testSatisfies() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSPredicateForm goal = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = state.satisfies(new JSListLogicalAtoms(goal), alpha, axioms);
 assertTrue(answers.size() > 0);
 }

 @Test
 public void testSatisfies_fail() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSPredicateForm goal = new JSPredicateForm("p3");
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = state.satisfies(new JSListLogicalAtoms(goal), alpha, axioms);
 assertTrue(answers.isEmpty());
 }

 @Test
 public void testSatisfiesAll() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSPredicateForm[] conditions = new JSPredicateForm[2];
 conditions[0] = new JSPredicateForm("p1");
 conditions[1] = new JSPredicateForm("p3");
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution answers = state.satisfiesAll(new JSListLogicalAtoms(conditions), alpha, axioms);
 assertEquals(1, answers.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSPredicateForm goal = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesTAm(goal, alpha);
 assertEquals(1, answers.size());
 }

 @Test
 public void testSatisfiesTAm_fail() {
 JSState state = createStateWithElements(new JSPredicateForm("p1"), new JSPredicateForm("p2"));
 JSPredicateForm goal = new JSPredicateForm("p3");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesTAm(goal, alpha);
 assertEquals(0, answers.size());
 }
}