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

public class JSStateTest {

 private JSState state;
 private JSState state1;
 private JSState state2;
 private JSOperator op;
 private JSListLogicalAtoms addL;
 private JSListLogicalAtoms delL;
 private JSListLogicalAtoms s;
 private JSListLogicalAtoms opAddL;
 private JSListLogicalAtoms opDelL;
 private JSPlan plan;
 private JSListAxioms axioms;

 @Before
 public void setUp() {
 state = new JSState();
 state1 = new JSState();
 state2 = new JSState();
 op = new JSOperator();
 addL = new JSListLogicalAtoms();
 delL = new JSListLogicalAtoms();
 s = new JSListLogicalAtoms();
 opAddL = new JSListLogicalAtoms();
 opDelL = new JSListLogicalAtoms();
 plan = new JSPlan();
 axioms = new JSListAxioms();
 }

 @Test
 public void testJSStateConstruction() {
 JSState jsState = new JSState();
 JSState jsState1 = new JSState(new StreamTokenizer(new StringReader("")));
 assertNotNull(jsState);
 assertNotNull(jsState1);
 }

 @Test
 public void testAddElementsToState() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x", "y"}, new String[]{"z"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x", "y"}, new String[]{"z"});
 s.addElement(p1);
 s.addElement(p2);
 state.addElementsToState(s);
 List<JSPredicateForm> expectedList = new ArrayList<>();
 expectedList.add(p1);
 expectedList.add(p2);
 List<JSPredicateForm> actualList = state.getElements();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testApplyOp_Fail() {
 op.addList(addL);
 op.deleteList(delL);
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 state.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 opAddL.addElement(new JSPredicateForm("p3", new String[]{"x"}, new String[]{"y"}));
 opDelL.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 JSState expectedState = new JSState();
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
 JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);
 JSState actualState = expectedState;
 JSListLogicalAtoms actualAddList = expectedAddList;
 JSListLogicalAtoms actualDelList = expectedDelList;
 assertEquals(expectedState, result.getState());
 assertEquals(expectedAddList, result.getAddList());
 assertEquals(expectedDelList, result.getDelList());
 }

 @Test
 public void testApplyOp_Pass() {
 op.addList(addL);
 op.deleteList(delL);
 state.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 state.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 opAddL.addElement(new JSPredicateForm("p3", new String[]{"x"}, new String[]{"y"}));
 opDelL.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 state1.addElement(new JSPredicateForm("p3", new String[]{"x"}, new String[]{"y"}));
 JSState expectedState = new JSState();
 JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
 expectedAddList.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}));
 expectedAddList.addElement(new JSPredicateForm("p3", new String[]{"x"}, new String[]{"y"}));
 JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
 expectedDelList.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"}));
 JSState result = state.applyOp(op, new JSSubstitution(), addL, delL);
 JSState actualState = expectedState;
 JSListLogicalAtoms actualAddList = result.getAddList();
 JSListLogicalAtoms actualDelList = result.getDelList();
 assertEquals(expectedState, actualState);
 assertEquals(expectedAddList, actualAddList);
 assertEquals(expectedDelList, actualDelList);
 }

 @Test
 public void testSatisfies() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"});
 state.addElement(p1);
 state.addElement(p2);
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSTerm("x", new String[]{"a"}));
 expectedSubstitution.addElement(new JSTerm("y", new String[]{"b"}));
 JSSubstitution actualSubstitution = state.satisfies(new JSListLogicalAtoms(new JSPredicateForm("p(x, y)", new String[]{"x", "y"}, new String[]{"z"})), new JSSubstitution(), axioms);
 assertEquals(expectedSubstitution, actualSubstitution);
 }

 @Test
 public void testSatisfiesAll() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"});
 state.addElement(p1);
 state.addElement(p2);
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSTerm("x", new String[]{"a"}));
 expectedSubstitution.addElement(new JSTerm("y", new String[]{"b"}));
 JSListSubstitution answers = state.satisfiesAll(new JSListLogicalAtoms(new JSPredicateForm("p(x, y)", new String[]{"x", "y"}, new String[]{"z"})), expectedSubstitution, axioms);
 List<JSSubstitution> actualAnswers = answers.getSubstitutionList();
 assertEquals(1, actualAnswers.size());
 assertEquals(expectedSubstitution, actualAnswers.get(0));
 }

 @Test
 public void testSatisfiesTAm() {
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"});
 JSPredicateForm p2 = new JSPredicateForm("p2", new String[]{"x"}, new String[]{"z"});
 state.addElement(p1);
 state.addElement(p2);
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.addElement(new JSTerm("x", new String[]{"a"}));
 expectedSubstitution.addElement(new JSTerm("y", new String[]{"b"}));
 JSPredicateForm t = new JSPredicateForm("p(x, y)", new String[]{"x", "y"}, new String[]{"z"});
 JSListSubstitution answers = state.satisfiesTAm(t, new JSSubstitution());
 List<JSSubstitution> actualAnswers = answers.getSubstitutionList();
 assertEquals(2, actualAnswers.size());
 JSListSubstitution expectedAnswers = new JSListSubstitution();
 expectedAnswers.addElement(expectedSubstitution);
 expectedAnswers.addElement(expectedSubstitution);
 assertEquals(expectedAnswers, answers);
 }
}