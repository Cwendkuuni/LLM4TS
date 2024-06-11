package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;
import umd.cs.shop.JSOperator.JSOperatorType;

public class JSStateTest {

 private JSState state1, state2, state3;
 private JSListLogicalAtoms list1, list2, list3, list4;
 private JSOperator addOp, delOp, op1, op2;
 private JSPlan plan;
 private JSListAxioms axioms;

 @Before
 public void setUp() {
 state1 = new JSState();
 state2 = new JSState();
 state3 = new JSState();
 list1 = new JSListLogicalAtoms();
 list2 = new JSListLogicalAtoms();
 list3 = new JSListLogicalAtoms();
 list4 = new JSListLogicalAtoms();
 addOp = new JSOperator("add", JSOperatorType.ADD);
 delOp = new JSOperator("del", JSOperatorType.DELETE);
 op1 = new JSOperator("op1", JSOperatorType.OP1);
 op2 = new JSOperator("op2", JSOperatorType.OP2);
 plan = new JSPlan();
 axioms = new JSListAxioms();
 }

 @Test
 public void testJSStateConstructor() {
 JSState actualJSState1 = new JSState();
 assertEquals("Expected JSState object", actualJSState1.getClass(), state1.getClass());
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState actualJSState2 = new JSState(new StreamTokenizer(new StringReader("")));
 assertEquals("Expected JSState object", actualJSState2.getClass(), state2.getClass());
 }

 @Test
 public void testAddElementsToState() {
 list1.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list2.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"}));
 state1.addElementsToState(list1);
 state2.addElementsToState(list2);

 list3.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 state3.addElementsToState(list3);

 list4.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 list4.addElement(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"}));
 state2.addElementsToState(list4);

 assertEquals("Expected size of state1 to be 3", 3, state1.size());
 assertEquals("Expected size of state2 to be 4", 4, state2.size());

 assertTrue("Expected state1 to contain p1", state1.contains(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"})));
 assertTrue("Expected state1 to contain p3", state1.contains(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"})));
 assertFalse("Expected state1 to not contain p2", !state1.contains(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"})));

 assertTrue("Expected state2 to contain p1, p2, and p3", state2.contains(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"})) && state2.contains(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"})) && state2.contains(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"})));
 }

 @Test
 public void testApplyOp() {
 addOp.addList().addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 delOp.deleteList().addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));

 list1.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list2.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"}));
 state1.addElementsToState(list1);
 list3.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 list4.addElement(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"}));
 list3.addElement(new JSPredicateForm("p4", new String[]{"x", "y"}, new String[]{"z"}));
 state2.addElementsToState(list3);
 list4.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list4.addElement(new JSPredicateForm("p5", new String[]{"x", "y"}, new String[]{"z"}));
 state3.addElementsToState(list4);

 JSState actualJSState = state1.applyOp(addOp, axioms, list1, list2);
 JSListLogicalAtoms addL = addOp.addList();
 JSListLogicalAtoms delL = delOp.deleteList();
 JSState expectedJSState = new JSState();
 expectedJSState.addElementsToState(addL);
 expectedJSState.addElementsToState(state2);
 expectedJSState.addElementsToState(delL);

 assertEquals("Expected applyOp to return state which is equal to expected state", expectedJSState, actualJSState);
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm goal = new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"});
 list1.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 state1.addElementsToState(list1);

 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", "1");
 alpha.assign("y", "2");
 alpha.assign("z", "3");

 JSSubstitution actualAnswer = state1.satisfies(goal, alpha, axioms);
 JSSubstitution expectedAnswer = new JSSubstitution();
 expectedAnswer.assign("x", "1");
 expectedAnswer.assign("y", "2");
 expectedAnswer.assign("z", "3");

 assertEquals("Expected satisfies method to return answer which is equal to expected answer", expectedAnswer, actualAnswer);
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm goal1 = new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"});
 JSPredicateForm goal2 = new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"});
 list1.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p3", new String[]{"x", "y"}, new String[]{"z"}));
 state1.addElementsToState(list1);

 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", "1");
 alpha.assign("y", "2");
 alpha.assign("z", "3");

 JSListSubstitution actualAnswers = state1.satisfiesAll(new JSListLogicalAtoms(goal1, goal2), alpha, axioms);
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.addElement(new JSSubstitution().assign("x", "1").assign("y", "2").assign("z", "3"));
 expectedAnswers.addElement(new JSSubstitution().assign("x", "1").assign("y", "2").assign("z", "3"));

 assertEquals("Expected satisfiesAll method to return answers which are equal to expected answers", expectedAnswers, actualAnswers);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm goal = new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"});
 list1.addElement(new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"}));
 list1.addElement(new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"}));
 state1.addElementsToState(list1);

 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", "1");
 alpha.assign("y", "2");
 alpha.assign("z", "3");

 JSListSubstitution actualAnswers = state1.satisfiesTAm(goal, alpha);
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.addElement(new JSSubstitution().assign("x", "1").assign("y", "2").assign("z", "3"));

 assertEquals("Expected satisfiesTAm method to return answers which are equal to expected answers", expectedAnswers, actualAnswers);
 }
}