package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSOperator;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTState;

public class JSStateTest {

private JSState createJSState() {
JSState state = new JSState();
JSPredicateForm atom1 = new JSPredicateForm("p(a)", Arrays.asList("p", "a"));
JSPredicateForm atom2 = new JSPredicateForm("q(b)", Arrays.asList("q", "b"));
JSPredicateForm atom3 = new JSPredicateForm("r(c)", Arrays.asList("r", "c"));
state.addElement(atom1);
state.addElement(atom2);
state.addElement(atom3);
return state;
}

@Test
public void testJSStateConstructor() {
JSState state = new JSState();
assertNotNull(state);
}

@Test
public void testJSStateConstructorWithTokenizer() {
JSState state = new JSState(new StreamTokenizer(new StringReader("")));
assertNotNull(state);
}

@Test
public void testAddElementsToState() {
JSState state1 = createJSState();
JSListLogicalAtoms list = new JSListLogicalAtoms();
list.addElement(new JSPredicateForm("s(d)", Arrays.asList("s", "d")));
JSState state2 = new JSState();
state2.addElementsToState(list);
state1.addElementsToState(list);
List<JSPredicateForm> expected = Arrays.asList(atom1, atom2, atom3, new JSPredicateForm("s(d)", Arrays.asList("s", "d")));
assertEquals(expected.size(), state1.size());
assertEquals(expected.get(3), state1.elementAt(state1.size() - 1));
}

@Test(expected = IllegalArgumentException.class)
public void testAddElementsToStateWithDuplicateElement() {
JSState state = createJSState();
JSPredicateForm atom1 = new JSPredicateForm("p(a)", Arrays.asList("p", "a"));
state.addElement(atom1);
state.addElementsToState(Arrays.asList(atom1));
fail("Should throw IllegalArgumentException with message 'Duplicate element found'");
}

@Test
public void testApply() {
fail("JSState.apply(JSPlan) method not implemented");
}

@Test
public void testApplyOp() {
JSState state = createJSState();
JSOperator op = new JSOperator(":operator", Arrays.asList(":operator"), Arrays.asList(":OpList"), Arrays.asList(":OpDeleteList", ":OpAddList"));
JSListLogicalAtoms addList = new JSListLogicalAtoms();
addList.addElement(new JSPredicateForm("s(d)", Arrays.asList("s", "d")));
JSListLogicalAtoms delList = new JSListLogicalAtoms();
JSState expectedState = new JSState();
expectedState.addElement(new JSPredicateForm("p(a)", Arrays.asList("p", "a")));
expectedState.addElement(new JSPredicateForm("q(b)", Arrays.asList("q", "b")));
expectedState.addElement(new JSPredicateForm("r(c)", Arrays.asList("r", "c")));
expectedState.addElement(new JSPredicateForm("s(d)", Arrays.asList("s", "d")));
JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
expectedAddList.addElement(new JSPredicateForm("p(a)", Arrays.asList("p", "a")));
expectedAddList.addElement(new JSPredicateForm("q(b)", Arrays.asList("q", "b")));
expectedAddList.addElement(new JSPredicateForm("r(c)", Arrays.asList("r", "c")));
expectedAddList.addElement(new JSPredicateForm("s(d)", Arrays.asList("s", "d")));
JSListLogicalAtoms expectedDelList = new JSListLogicalAtoms();
JSState ns = state.applyOp(op, new JSSubstitution(), addList, delList);
assertEquals(expected.size(), ns.size());
for (int i = 0; i < expected.size(); ++i) {
assertEquals(expected.get(i), ns.elementAt(i));
}
assertEquals(expectedAddList.size(), ns.addList().size());
assertEquals(expectedAddList.get(0), ns.addList().elementAt(0));
assertEquals(expectedDelList.size(), ns.deleteList().size());
assertEquals(expectedDelList.get(0), ns.deleteList().elementAt(0));
}

@Test
public void testSatisfies() {
fail("JSState.satisfies(JSListLogicalAtoms, JSSubstitution, JSListAxioms) method not implemented");
}

@Test
public void testSatisfiesAll() {
fail("JSState.satisfiesAll(JSListLogicalAtoms, JSSubstitution, JSListAxioms) method not implemented");
}

@Test
public void testSatisfiesTAm() {
JSState state = createJSState();
JSPredicateForm goal = new JSPredicateForm("p(a)", Arrays.asList("p", "a"));
JSSubstitution alpha = new JSSubstitution();
JSSubstitution expected = new JSSubstitution();
expected.addElement(new JSPredicateForm("p(a)", Arrays.asList("p", "a")));
expected.addElement(new JSPredicateForm("a", Arrays.asList("a", "a")));
List<JSPredicateForm> answers = state.satisfiesTAm(goal, alpha);
assertEquals(expected.size(), answers.size());
for (int i = 0; i < expected.size(); ++i) {
assertEquals(expected.get(i), answers.get(i));
}
}
}