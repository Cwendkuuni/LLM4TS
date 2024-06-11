package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 JSState jsStateWithTokenizer = createJSState(new StreamTokenizer(new StringReader("")));
 assertNull(jsStateWithTokenizer.getLabel());
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expectedL = new JSListLogicalAtoms();
 expectedL.addElement(new JSPredicateForm("p1"));
 expectedL.addElement(new JSPredicateForm("p2"));
 assertEquals(expectedL, jsState.getJSListLogicalAtoms());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullList() {
 JSState jsState = createJSState();
 jsState.addElementsToState(null);
 }

 @Test
 public void testApplyOp_GivenNullState() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = createJSState();
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOp_GivenEmptyStateAndEmptyAddList() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = createJSState();
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOp_GivenStateWithSingleElement() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOp_GivenStateWithTwoElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p3"));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 assertEquals(expectedAddL, result.getJSListLogicalAtoms());
 }

 @Test
 public void testApplyOp_GivenStateWithDuplicateElements() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p1"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOp_GivenStateWithDuplicateElementsInAddList() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p1"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p1"));
 addL.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testApplyOp_GivenStateWithEmptyDeleteList() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 expectedResult.addElement(new JSPredicateForm("p2"));
 expectedResult.addElement(new JSPredicateForm("p3"));
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyOp_GivenNullDeleteList() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSOperator op = new JSOperator("op");
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p3"));
 JSListLogicalAtoms delL = null;
 JSState expectedResult = new JSState();
 expectedResult.addElement(new JSPredicateForm("p1"));
 expectedResult.addElement(new JSPredicateForm("p2"));
 expectedResult.addElement(new JSPredicateForm("p3"));
 try {
 JSState result = jsState.applyOp(op, alpha, addL, delL);
 fail("Exception not thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals(expectedResult, jsState);
 }

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesWithSuccess() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.addElement(new JSPredicateForm("x", "y"));
 JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 conds.addElement(new JSPredicateForm("p2"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSListSubstitution answer = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesTAm() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1"));
 jsState.addElement(new JSPredicateForm("p2"));
 JSPredicateForm t = new JSPredicateForm("p1(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSListSubstitution answer = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesTAmWithSuccess() {
 JSState jsState = createJSState();
 jsState.addElement(new JSPredicateForm("p1(x)"));
 jsState.addElement(new JSPredicateForm("p2(x)"));
 JSPredicateForm t = new JSPredicateForm("p1(x)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPredicateForm("x", "y"));
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 expectedAnswer.addElement(new JSSubstitution(new JSPredicateForm("x", "y")));
 JSListSubstitution answer = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, answer);
 }
}