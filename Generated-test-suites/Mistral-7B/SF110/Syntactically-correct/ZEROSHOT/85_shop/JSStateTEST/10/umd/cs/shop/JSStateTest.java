package umd.cs.shop.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListLogicalAtoms.JSPredicateForm;
import umd.cs.shop.JSUtil;

public class JSStateTest {

 private JSState state;
 private JSListLogicalAtoms list;
 private JSOperator op;
 private JSPlan plan;
 private JSSubstitution alpha;
 private JSListLogicalAtoms addL;
 private JSListLogicalAtoms delL;

 @Before
 public void setUp() {
 state = new JSState();
 list = new JSListLogicalAtoms();
 op = new JSOperator();
 plan = new JSPlan();
 alpha = new JSSubstitution();
 addL = new JSListLogicalAtoms();
 delL = new JSListLogicalAtoms();
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
 JSListLogicalAtoms inputList = new JSListLogicalAtoms();
 inputList.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 inputList.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
 state.addElementsToState(inputList);
 JSPredicateForm expected = new JSPredicateForm("p(x)", new String[]{"x"});
 assertTrue(state.contains(expected));
 assertFalse(state.contains(new JSPredicateForm("r(x)", new String[]{"x"})));
 }

 @Test
 public void testApplyOp_NullInput() {
 JSState result = state.applyOp(null, alpha, addL, delL);
 assertNull(result);
 }

 @Test
 public void testApplyOp_EmptyLists() {
 JSState result = state.applyOp(op, alpha, addL, delL);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testApplyOp_IdenticalLists() {
 addL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 delL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 JSState result = state.applyOp(op, alpha, addL, delL);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testApplyOp_DifferentLists() {
 addL.addElement(new JSPredicateForm("p(x)", new String[]{"x"}));
 delL.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
 JSState result = state.applyOp(op, alpha, addL, delL);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm condition = new JSPredicateForm("p(x)", new String[]{"x"});
 JSSubstitution expected = new JSSubstitution();
 expected.assign("x", "a");
 JSListLogicalAtoms inputList = new JSListLogicalAtoms();
 inputList.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 inputList.addElement(new JSPredicateForm("q(x)", new String[]{"x"}));
 state.addElementsToState(inputList);
 JSSubstitution actual = state.satisfies(condition, alpha, new JSListAxioms());
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm condition1 = new JSPredicateForm("p(x)", new String[]{"x"});
 JSPredicateForm condition2 = new JSPredicateForm("q(x)", new String[]{"x"});
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSListSubstitution());
 expected.addElement(new JSListSubstitution());
 JSListLogicalAtoms inputList = new JSListLogicalAtoms();
 inputList.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 inputList.addElement(new JSPredicateForm("q(b)", new String[]{"b"}));
 state.addElementsToState(inputList);
 JSListSubstitution actual = state.satisfiesAll(new JSListLogicalAtoms(condition1, condition2), alpha, new JSListAxioms());
 assertEquals(expected, actual);
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm goal = new JSPredicateForm("p(x)", new String[]{"x"});
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.assign("x", "a");
 JSListLogicalAtoms inputList = new JSListLogicalAtoms();
 inputList.addElement(new JSPredicateForm("p(a)", new String[]{"a"}));
 state.addElementsToState(inputList);
 JSListSubstitution actualSubstitutions = state.satisfiesTAm(goal, alpha);
 assertEquals(expectedSubstitution, actualSubstitutions.elementAt(0));
 }
}