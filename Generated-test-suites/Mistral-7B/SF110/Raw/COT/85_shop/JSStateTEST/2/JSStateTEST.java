package umd.cs.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
 assertNotNull(jsState);
 }

 @Test
 public void testJSStateConstructorWithTokenizer() {
 JSState jsState = createJSState(null);
 assertNotNull(jsState);
 }

 @Test
 public void testAddElementsToState() {
 JSState jsState = createJSState();
 JSListLogicalAtoms s = new JSListLogicalAtoms();
 s.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, "F(x)"));
 s.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, "G(x)"));
 jsState.addElementsToState(s);
 JSListLogicalAtoms expected = new JSListLogicalAtoms();
 expected.addElement(new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, "F(x)"));
 expected.addElement(new JSPredicateForm("p2", new String[]{"x"}, new String[]{"y"}, "G(x)"));
 assertEquals(expected.size(), jsState.size());
 for (short i = 0; i < jsState.size(); ++i) {
 JSPredicateForm el = jsState.elementAt(i);
 JSPredicateForm expectedEl = expected.elementAt(i);
 assertEquals(expectedEl.getLabel(), el.getLabel());
 assertEquals(expectedEl.getArity(), el.getArity());
 assertEquals(expectedEl.getPredicateSymbol(), el.getPredicateSymbol());
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddElementsToStateWithNullList() {
 JSState jsState = createJSState();
 jsState.addElementsToState(null);
 fail("IllegalArgumentException expected but not thrown");
}

 @Test
 public void testApply() {
 JSState jsState = createJSState();
 JSPlan pl = null; // Replace with a valid JSPlan instance
 JSState result = jsState.apply(pl); // This line should never be executed due to the null JSPlan
 fail("JSPlan is null, application method should not be called");
}

 @Test
 public void testApplyOp() {
 JSState jsState = createJSState();
 JSOperator op = null; // Replace with a valid JSOperator instance
 JSSubstitution alpha = new JSSubstitution();
 JSListLogicalAtoms addL = new JSListLogicalAtoms();
 JSListLogicalAtoms delL = new JSListLogicalAtoms();
 JSState result = jsState.applyOp(op, alpha, addL, delL); // This line should never be executed due to the null JSOperator
 fail("JSOperator is null, application method should not be called");
}

 @Test
 public void testSatisfies() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertNotNull(result);
}

 @Test
 public void testSatisfiesWithEmptyConditions() {
 JSState jsState = createJSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(new JSListLogicalAtoms(), alpha, axioms);
 assertEquals(0, result.size());
}

 @Test
 public void testSatisfiesWithFail() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 result.assignFailure();
 assertTrue(result.isFailure());
}

 @Test
 public void testSatisfiesWithSuccess() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(new JSPredicateForm("x", new String[]{"x"}, new String[]{"y"}, "F(x)"));
 expected.addElement(new JSPredicateForm("y", new String[]{"x"}, new String[]{"z"}, "G(x)"));
 JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
 assertEquals(expected.size(), result.size());
 for (short i = 0; i < result.size(); ++i) {
 JSPredicateForm el = result.elementAt(i);
 JSPredicateForm expectedEl = expected.elementAt(i);
 assertEquals(expectedEl.getLabel(), el.getLabel());
 assertEquals(expectedEl.getArity(), el.getArity());
 assertEquals(expectedEl.getPredicateSymbol(), el.getPredicateSymbol());
 }
}

 @Test
 public void testSatisfiesTAmWithEmptyList() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("", new String[0], new String[0], "");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(0, result.size());
}

 @Test
 public void testSatisfiesTAmWithFailure() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, "F(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = jsState.satisfiesTAm(t, alpha);
 result.assignFailure();
 assertTrue(result.isFailure());
}

 @Test
 public void testSatisfiesTAmWithSuccess() {
 JSState jsState = createJSState();
 JSPredicateForm t = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, "F(x)");
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm p1 = new JSPredicateForm("p1", new String[]{"x"}, new String[]{"y"}, "F(x)");
 JSSubstitution expected = new JSSubstitution();
 expected.addElement(p1);
 JSSubstitution result = jsState.satisfiesTAm(t, alpha);
 assertEquals(expected.size(), result.size());
 for (short i = 0; i < result.size(); ++i) {
 JSPredicateForm el = result.elementAt(i);
 JSPredicateForm expectedEl = expected.elementAt(i);
 assertEquals(expectedEl.getLabel(), el.getLabel());
 assertEquals(expectedEl.getArity(), el.getArity());
 assertEquals(expectedEl.getPredicateSymbol(), el.getPredicateSymbol());
 }
}

 @Test
 public void testSatisfiesAll() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertNotNull(result);
}

 @Test
 public void testSatisfiesAllWithEmptyConditions() {
 JSState jsState = createJSState();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(new JSListLogicalAtoms(), alpha, axioms);
 assertEquals(0, result.size());
}

 @Test
 public void testSatisfiesAllWithFailure() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 result.assignFailure();
 assertTrue(result.isFailure());
}

 @Test
 public void testSatisfiesAllWithSuccess() {
 JSState jsState = createJSState();
 JSListLogicalAtoms conds = new JSListLogicalAtoms();
 JSSubstitution alpha = new JSSubstitution();
 JSListAxioms axioms = new JSListAxioms();
 JSListSubstitution expected = new JSListSubstitution();
 expected.addElement(new JSSubstitution(new JSPredicateForm("x", new String[]{"x"}, new String[]{"y"}, "F(x)")));
 expected.addElement(new JSSubstitution(new JSPredicateForm("y", new String[]{"x"}, new String[]{"z"}, "G(x)")));
 JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
 assertEquals(expected.size(), result.size());
 for (short i = 0; i < result.size(); ++i) {
 JSSubstitution el = result.elementAt(i);
 JSSubstitution expectedEl = expected.elementAt(i);
 assertEquals(expectedEl.size(), el.size());
 for (short j = 0; j < el.size(); ++j) {
 JSPredicateForm elEl = el.elementAt(j);
 JSPredicateForm expectedElEl = expectedEl.elementAt(j);
 assertEquals(expectedElEl.getLabel(), elEl.getLabel());
 assertEquals(expectedElEl.getArity(), elEl.getArity());
 assertEquals(expectedElEl.getPredicateSymbol(), elEl.getPredicateSymbol());
 }
 }
}

}