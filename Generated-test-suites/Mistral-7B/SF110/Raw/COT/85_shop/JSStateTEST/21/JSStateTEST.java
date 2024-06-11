package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSStateTest {

 private JSState createJSState() {
 return new JSState();
 }

 private JSState createJSState(String label) {
 JSState jsState = new JSState();
 jsState.setLabel(label);
 return jsState;
 }

 private JSState createJSState(StreamTokenizer tokenizer) {
 return new JSState(tokenizer);
 }

 @Test
 public void testJSStateConstructor() {
 JSState jsState = createJSState();
 assertNull(jsState.getLabel());
 JSState jsStateWithLabel = createJSState("testLabel");
 assertEquals("testLabel", jsStateWithLabel.getLabel());
 }

 @Test
 public void testJSStateAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.add(new JSPredicateForm("p", new JSTerm("x"), new JSTerm("y"), new JSTerm("z")));
 s.add(new JSPredicateForm("q", new JSTerm("a"), new JSTerm("b"), new JSTerm("c")));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.add(new JSPredicateForm("p", new JSTerm("x"), new JSTerm("y"), new JSTerm("z")));
 expected.add(new JSPredicateForm("q", new JSTerm("a"), new JSTerm("b"), new JSTerm("c")));
 assertEquals(expected, jsState);

 JSListLogicalAtoms s1 = new JSListLogicalAtoms();
 s1.add(new JSPredicateForm("p", new JSTerm("x1"), new JSTerm("y1"), new JSTerm("z1")));
 JSListLogicalAtoms s2 = new JSListLogicalAtoms();
 s2.add(new JSPredicateForm("q", new JSTerm("a1"), new JSTerm("b1"), new JSTerm("c1")));
 jsState.addElementsToState(s1);
 jsState.addElementsToState(s2);
 JSListLogicalAtoms expected2 = new JSListLogicalAtoms();
 expected2.addAll(expected);
 expected2.addAll(s1);
 expected2.addAll(s2);
 assertEquals(expected2, jsState);
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateAddElementsToStateNullList() {
 JSState jsState = createJSState();
 JSListLogicalAtoms nullList = null;
 jsState.addElementsToState(nullList);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateAddElementToStateNullElement() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 JSPredicateForm nullElement = null;
 jsState.addElementsToState(s);
 s.add(nullElement);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test
 public void testJSStateApply() {
 JSState jsState = createJSState();
 JSPlan pl = new JSPlan();
 JSState expected = createJSState();
 JSUtil.flag20("<STATE>.apply(<JSPlan>) not implemented yet");
 // Replace this line with the expected implementation and assert the result
 assertEquals(expected, jsState.apply(pl));
}

 @Test
 public void testJSStateApplyOp() throws IOException {
 JSState jsState = createJSState();
 JSOperator op = new JSAdd();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 addL.add(new JSPredicateForm("p", new JSTerm("x"), new JSTerm("y"), new JSTerm("z")));
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSListLogicalAtoms opAddL = new JSListLogicalAtoms();
 opAddL.add(new JSPredicateForm("r", new JSTerm("x"), new JSTerm("y"), new JSTerm("z")));
 JSListLogicalAtoms opDelL = new JSListLogicalAtoms();
 opDelL.add(new JSPredicateForm("q", new JSTerm("a"), new JSTerm("b"), new JSTerm("c")));
 JSState ns = jsState.applyOp(op, alpha, addL, delL);
 JSListLogicalAtoms nAddL = new JSListLogicalAtoms();
 JSListLogicalAtoms nDelL = new JSListLogicalAtoms();
 JSListLogicalAtoms expectedAddL = new JSListLogicalAtoms();
 expectedAddL.addAll(addL);
 expectedAddL.add(new JSPredicateForm("r", new JSTerm("x"), new JSTerm("y"), new JSTerm("z")));
 JSListLogicalAtoms expectedDelL = new JSListLogicalAtoms();
 expectedDelL.addAll(delL);
 expectedDelL.add(new JSPredicateForm("q", new JSTerm("a"), new JSTerm("b"), new JSTerm("c")));
 JSState expectedNS = new JSState();
 expectedNS.addElementsToState(expectedAddL);
 expectedNS.addElementsToState(expectedDelL);
 assertEquals(expectedNS, ns);
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateApplyOpNullInput() {
 JSState jsState = createJSState();
 JSOperator op = null;
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 jsState.applyOp(op, alpha, addL, delL);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateApplyOpNullAlpha() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 jsState.applyOp(op, null, addL, delL);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateApplyOpNullAddL() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 jsState.applyOp(op, alpha, null, delL);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test(expected = IllegalArgumentException.class)
 public void testJSStateApplyOpNullDelL() {
 JSState jsState = createJSState();
 JSOperator op = new JSOperator();
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 jsState.applyOp(op, alpha, addL, null);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test
 public void testJSStateSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expectedAnswer = new JSSubstitution();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, jsState, alpha, false);
 if (satisfiers.fail()) {
 expectedAnswer.assignFailure();
 }
 else {
 expectedAnswer = (JSSubstitution)alpha.clone();
 expectedAnswer.addElements(satisfiers.elementAt(0));
 }
 JSListSubstitution actualAnswer = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expectedAnswer, actualAnswer);
}

 @Test
 public void testJSStateSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSListSubstitution satisfiers = axioms.TheoremProver(conds, jsState, alpha, true);
 for (int i = 0; i < satisfiers.size(); ++i) {
 JSSubstitution beta = (JSSubstitution)satisfiers.elementAt(i).clone();
 beta.addElements((JSSubstitution)alpha.clone());
 expectedAnswer.addElement(beta);
 }
 JSListSubstitution actualAnswer = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expectedAnswer, actualAnswer);
}

 @Test
 public void testJSStateSatisfiesTAm() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p", new JSTerm("x"), new JSTerm("y"), new JSTerm("z"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSListSubstitution satisfiers = new JSListSubstitution();
 for (int i = 0; i < jsState.size(); ++i) {
 JSPredicateForm el = jsState.elementAt(i);
 JSSubstitution subs = t.matches(el, alpha);
 if (!subs.fail()) {
 expectedAnswer.addElement(subs);
 }
 }
 JSListSubstitution actualAnswer = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, actualAnswer);
}

 @Test
 public void testJSStateSatisfiesTAmEmptyList() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p", new JSTerm("x"), new JSTerm("y"), new JSTerm("z"));
 JSSubstitution alpha = new JSSubstitution();
 JSListSubstitution expectedAnswer = new JSListSubstitution();
 JSListSubstitution satisfiers = new JSListSubstitution();
 JSListSubstitution actualAnswer = jsState.satisfiesTAm(t, alpha);
 assertEquals(expectedAnswer, actualAnswer);
}

}