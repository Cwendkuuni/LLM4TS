package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSStateWithLabel(String label) {
 JSState jsState = new JSState();
 jsState.label = label;
 return jsState;
 }

 private JSState createJSStateWithElement(JSState jsState, JSPredicateForm predicateForm) {
 jsState.insertElementAt(predicateForm, 0);
 return jsState;
 }

 private JSState createJSStateWithElements(JSState jsState, JSListLogicalAtoms jsListLogicalAtoms) {
 for (int i = 0; i < jsListLogicalAtoms.size(); ++i) {
 jsState = createJSStateWithElement(jsState, jsListLogicalAtoms.elementAt(i));
 }
 return jsState;
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNull(jsState.label);
 assertTrue(jsState.size() == 0);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 // Cannot test this method as it requires a StreamTokenizer object
}

 @Test
 public void testAddElementsToState() {
 JSState initialJSState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1"));
 s.addElement(new JSPredicateForm("p2"));
 JSState expectedJSState = createJSStateWithElements(createJSState(), s);
 initialJSState.addElementsToState(s);
 assertEquals(expectedJSState.size(), initialJSState.size());
 for (short i = 0; i < expectedJSState.size(); ++i) {
 assertEquals(expectedJSState.elementAt(i).getForm(), initialJSState.elementAt(i).getForm());
 }
}

 @Test
 public void testApply() {
 // Cannot test this method as it is not implemented
}

 @Test
 public void testApplyOp() {
 JSState initialJSState = createJSStateWithElement(createJSState(), new JSPredicateForm("p1"));
 JSOperator op = new JSOperator(":op");
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.addElement(new JSPredicateForm("p2"));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState expectedNS = createJSState();
 expectedNS.addElement(new JSPredicateForm("p1"));
 JSState result = initialJSState.applyOp(op, new JSSubstitution(), addL, delL);
 assertEquals(expectedNS.size(), result.size());
 for (short i = 0; i < expectedNS.size(); ++i) {
 assertEquals(expectedNS.elementAt(i).getForm(), result.elementAt(i).getForm());
 }
}

 @Test
 public void testSatisfies() {
 JSState initialJSState = createJSStateWithElement(createJSState(), new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSSubstitution answer = initialJSState.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer.size(), answer.size());
 for (int i = 0; i < expectedAnswer.size(); ++i) {
 assertEquals(expectedAnswer.elementAt(i).getForm(), answer.elementAt(i).getForm());
 }
}

 @Test
 public void testSatisfiesAll() {
 JSState initialJSState = createJSStateWithElement(createJSState(), new JSPredicateForm("p1"));
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 conds.addElement(new JSPredicateForm("p1"));
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.addElement(new JSSubstitution());
 JSListSubstitution answers = initialJSState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswers.size(), answers.size());
 for (int i = 0; i < expectedAnswers.size(); ++i) {
 assertEquals(expectedAnswers.elementAt(i).getForm(), answers.elementAt(i).getForm());
 }
}

 @Test
 public void testSatisfiesTAm() {
 JSState initialJSState = createJSStateWithElement(createJSState(), new JSPredicateForm("p1"));
 JSPredicateForm t = new JSPredicateForm("(p1 <- p2)");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.addElement(new JSSubstitution());
 JSListSubstitution answers = initialJSState.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswers.size(), answers.size());
 for (int i = 0; i < expectedAnswers.size(); ++i) {
 assertEquals(expectedAnswers.elementAt(i).getForm(), answers.elementAt(i).getForm());
 }
}

 @Test(expected = IllegalStateException.class)
 public void testAddElementToEmptyStateWithDuplicateElement() {
 JSState jsState = createJSState();
 JSPredicateForm predicateForm = new JSPredicateForm("p1");
 jsState.addElement(predicateForm);
 jsState.addElement(predicateForm);
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullElementList() {
 JSState jsState = createJSState();
 JSListLogicalAtoms jsListLogicalAtoms = null;
 jsState.addElementsToState(jsListLogicalAtoms);
}

 @Test(expected = NullPointerException.class)
 public void testAddElementToStateWithNullElement() {
 JSState jsState = createJSState();
 JSPredicateForm predicateForm = null;
 jsState.addElement(predicateForm);
}
}