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
import umd.cs.shop.JSSubstitutionAlpha;
import umd.cs.shop.JSSubstitutionFailure;
import umd.cs.shop.JSTState;
import umd.cs.shop.JSTStateAnswer;
import umd.cs.shop.JSTStateAnswerAdd;
import umd.cs.shop.JSTStateAnswerDel;

public class JSStateTest {

 private JSState state;
 private JSState state1;
 private JSState state2;
 private JSState state3;
 private JSOperator op;
 private JSPlan plan;
 private JSListLogicalAtoms list;
 private JSListLogicalAtoms list1;
 private JSListLogicalAtoms list2;
 private JSListLogicalAtoms list3;
 private JSListAxioms axioms;
 private JSSubstitution alpha;
 private JSSubstitution alpha1;
 private JSSubstitution alpha2;
 private JSSubstitution beta;

 @Before
 public void setUp() {
 state = new JSState();
 state1 = new JSState();
 state2 = new JSState();
 state3 = new JSState();
 op = new JSOperator("op");
 plan = new JSPlan();
 list = new JSListLogicalAtoms();
 list1 = new JSListLogicalAtoms();
 list2 = new JSListLogicalAtoms();
 list3 = new JSListLogicalAtoms();
 axioms = new JSListAxioms();
 alpha = new JSSubstitution();
 alpha1 = new JSSubstitution();
 alpha2 = new JSSubstitution();
 beta = new JSSubstitution();
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = new JSState();
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
 JSState jsState = new JSState(tokenizer);
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 list.addElement(new JSPredicateForm("p1"));
 list.addElement(new JSPredicateForm("p2"));
 state1.addElementsToState(list);
 assertEquals(2, state1.size());
 list1.addElement(new JSPredicateForm("p3"));
 state2.addElementsToState(list1);
 assertEquals(1, state2.size());
 }

 @Test
 public void testApply() {
 fail("JSState.apply() is not implemented yet.");
 }

 @Test
 public void testApplyOp() {
 list.addElement(new JSPredicateForm("p1"));
 list1.addElement(new JSPredicateForm("p2"));
 list2.addElement(new JSPredicateForm("p3"));
 list3.addElement(new JSPredicateForm("p4"));
 state1.addElementsToState(list);
 state2.addElementsToState(list1);
 state3.addElementsToState(list2);
 state3.addElementsToState(list3);
 op.addList(list2);
 op.deleteList(list3);
 JSSubstitutionAlpha alpha = new JSSubstitutionAlpha();
 JSSubstitutionFailure failure = new JSSubstitutionFailure();
 alpha.addElement(new JSSubstitutionAnswerAdd("p1", "q1"));
 alpha.addElement(new JSSubstitutionAnswerDel("p3", "q3"));
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p1"));
 expectedState.addElement(new JSPredicateForm("p2"));
 expectedState.addElement(new JSPredicateForm("q3"));
 JSTStateAnswer expectedAnswer = new JSTStateAnswer(expectedState, new JSListLogicalAtoms(), new JSListLogicalAtoms());
 JSTStateAnswer actualAnswer = state1.applyOp(op, alpha, list, list);
 assertEquals(expectedAnswer.getState(), actualAnswer.getState());
 }

 @Test
 public void testSatisfies() {
 list.addElement(new JSPredicateForm("p1"));
 list1.addElement(new JSPredicateForm("p1"));
 list2.addElement(new JSPredicateForm("p2"));
 state1.addElementsToState(list);
 state2.addElementsToState(list1);
 state3.addElementsToState(list2);
 axioms.addElement(new JSAxiom("p1 <- p1", new JSSubstitution()));
 axioms.addElement(new JSAxiom("p2 <- p1", new JSSubstitution()));
 JSUtil.setFlagLevel(3);
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addElement(new JSPredicateForm("p1"));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 expectedDelL.addElement(new JSPredicateForm("p2"));
 JSTStateAnswer expectedAnswer = new JSTStateAnswer(expectedState, expectedAddL, expectedDelL);
 JSSubstitution alpha = new JSSubstitution();
 JSTStateAnswer actualAnswer = state1.satisfies(new JSListLogicalAtoms("p1"), alpha, axioms);
 assertEquals(expectedAnswer.getState(), actualAnswer.getState());
 assertEquals(expectedAddL, actualAnswer.getAddList());
 assertEquals(expectedDelL, actualAnswer.getDelList());
 }

 @Test
 public void testSatisfiesAll() {
 list.addElement(new JSPredicateForm("p1"));
 list1.addElement(new JSPredicateForm("p1"));
 list2.addElement(new JSPredicateForm("p2"));
 state1.addElementsToState(list);
 state2.addElementsToState(list1);
 state3.addElementsToState(list2);
 axioms.addElement(new JSAxiom("p1 <- p1", new JSSubstitution()));
 axioms.addElement(new JSAxiom("p1 <- p2", new JSSubstitution()));
 axioms.addElement(new JSAxiom("p2 <- p1", new JSSubstitution()));
 JSUtil.setFlagLevel(3);
 JSState expectedState = new JSState();
 expectedState.addElement(new JSPredicateForm("p1"));
 expectedState.addElement(new JSPredicateForm("p2"));
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 expectedAnswer.addElement(new JSSubstitutionAnswerAdd("p1", "q1"));
 expectedAnswer.addElement(new JSSubstitutionAnswerAdd("p2", "q2"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSSubstitutionAnswerAdd("p1", "q1"));
 alpha.addElement(new JSSubstitutionAnswerAdd("p2", "q2"));
 JSListSubstitution actualAnswer = state1.satisfiesAll(new JSListLogicalAtoms("p1,p2"), alpha, axioms);
 assertEquals(expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesTAm() {
 list.addElement(new JSPredicateForm("p1"));
 list1.addElement(new JSPredicateForm("p2"));
 state1.addElementsToState(list);
 state2.addElementsToState(list1);
 op = new JSOperator("op");
 op.addList(list1);
 JSPredicateForm t = new JSPredicateForm("p1");
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSSubstitutionAnswerAdd expectedAddAnswer1 = new JSSubstitutionAnswerAdd("p1", "q1");
 JSSubstitutionAnswerAdd expectedAddAnswer2 = new JSSubstitutionAnswerAdd("p2", "q2");
 expectedAnswer.addElement(expectedAddAnswer1);
 expectedAnswer.addElement(expectedAddAnswer2);
 JSUtil.setFlagLevel(7);
 JSListSubstitution actualAnswer = state1.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, actualAnswer);
 }
}