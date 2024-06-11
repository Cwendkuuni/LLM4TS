package umd.cs.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSStateTest {

 JSListLogicalAtoms createJSListLogicalAtoms() {
 return new JSListLogicalAtoms();
 }

 JSState createJSState() {
 return new JSState();
 }

 JSState createJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 JSState newJSStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.setLabel(label);
 return jsState;
 }

 @Test
 public void testJSState() {
 // Test empty JSState
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 assertEquals(0, jsState.size());

 // Test JSState with label
 JSState jsStateWithLabel = newJSStateWithLabel("testLabel");
 assertEquals("testLabel", jsStateWithLabel.getLabel());

 // Test adding elements to JSState
 JSListLogicalAtoms listToAdd = createJSListLogicalAtoms();
 listToAdd.addElement(new JSPredicateForm("p1"));
 listToAdd.addElement(new JSPredicateForm("p2"));
 jsStateWithLabel.addElementsToState(listToAdd);
 assertEquals(2, jsStateWithLabel.size());
 }

 @Test
 public void testApplyOp() {
 JSOperator op = new JSAddOperator();
 JSListLogicalAtoms addList = createJSListLogicalAtoms();
 addList.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms delList = createJSListLogicalAtoms();
 JSState jsState = createJSState();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms resultAddList = createJSListLogicalAtoms();
 resultAddList.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms resultDelList = createJSListLogicalAtoms();

 jsState.applyOp(op, null, addList, delList);
 assertEquals(expectedResult.size(), jsState.size());
 assertEquals(expectedResult.elementAt(0).toString(), jsState.elementAt(0).toString());
 assertEquals(resultAddList.size(), addList.size());
 assertEquals(resultDelList, delList);
 }

 @Test
 public void testSatisfies() {
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, createJSState(), alpha, false);

 JSState jsState = createJSState();
 JSListSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertTrue(answer.isEmpty());

 // Test satisfiability with a single substitution
 JSPredicateForm p1 = new JSPredicateForm("p1(x)");
 JSExpression x = new JSVariableExpression("x");
 alpha.addElement(new JSBinding("x", new JSConstantExpression(1)));
 satisfiers = axioms.TheoremProver(conds, jsState, alpha, false);
 assertTrue(!satisfiers.isEmpty());
 answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(satisfiers.elementAt(0), answer.elementAt(0));
 }

 @Test
 public void testSatisfiesAll() {
 JSListLogicalAtoms conds = createJSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1(x)"));
 conds.addElement(new JSPredicateForm("p2(y)"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 List<JSSubstitution> satisfiers = new ArrayList<>();
 satisfiers.add(new JSSubstitution());
 satisfiers.add(new JSSubstitution());
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 expectedAnswer.addElement(new JSSubstitution());
 expectedAnswer.addElement(new JSSubstitution());

 JSState jsState = createJSState();
 JSListSubstitution answer = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswer.size(), answer.size());
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t = new JSPredicateForm("p1(x) -> p2(y)");
 JSExpression x = new JSVariableExpression("x");
 JSExpression y = new JSVariableExpression("y");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution satisfiers = new JSListSubstitution();
 satisfiers.addElement(new JSSubstitution());
 JSPredicateForm p1 = new JSPredicateForm("p1(1)");
 JSPredicateForm p2 = new JSPredicateForm("p2(2)");
 JSState jsState = newJSStateWithLabel("testLabel");
 jsState.addElement(p1);
 jsState.addElement(p2);

 JSListSubstitution answer = jsState.satisfiesTAm(t, alpha);
 assertFalse(answer.isEmpty());
 }
}