package umd.cs.shop.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListAxioms;
import umd.cs.shop.JSPredicateForm;

public class JSStateTest {

 private JSState state;
 private JSState state1;
 private JSState state2;
 private JSOperator op;
 private JSListLogicalAtoms addL;
 private JSListLogicalAtoms delL;
 private JSPlan plan;
 private JSListAxioms axioms;
 private JSListLogicalAtoms conds;
 private JSPredicateForm t;
 private JSSubstitution alpha;

 @Before
 public void setUp() {
 state = new JSState();
 state1 = new JSState();
 state2 = new JSState();
 op = new JSOperator();
 addL = new JSListLogicalAtoms();
 delL = new JSListLogicalAtoms();
 plan = new JSPlan();
 axioms = new JSListAxioms();
 conds = new JSListLogicalAtoms();
 t = new JSPredicateForm();
 alpha = new JSSubstitution();
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
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 state.addElementsToState(new JSListLogicalAtoms(p1, p2));
 assertEquals(2, state.size());
 }

 @Test
 public void testApply() {
 fail("apply method not implemented in JSState class");
 }

 @Test
 public void testApplyOp() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSPredicateForm p3 = new JSPredicateForm("p3", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 state.addElement(p1);
 state1.addElement(p2);
 addL.addElement(p3);
 delL.addElement(p2);
 JSState expectedResult = new JSState();
 expectedResult.addElement(p1);
 JSState result = state.applyOp(op, alpha, addL, delL);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSListLogicalAtoms conds = new JSListLogicalAtoms(p1, p2);
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSSubstitution answer = state.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, answer);
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSListLogicalAtoms conds = new JSListLogicalAtoms("first", p1, p2);
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswer, answers);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm t1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, new String[]{"z"});
 JSListLogicalAtoms conds = new JSListLogicalAtoms(t1);
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSListSubstitution answers = state.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, answers);
 }
}