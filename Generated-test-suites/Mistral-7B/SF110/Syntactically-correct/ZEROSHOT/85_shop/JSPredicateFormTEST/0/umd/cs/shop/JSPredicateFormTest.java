package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSPredicateFormTest {

	@Test
	public void testJSPredicateFormInit() {
		String input = "!(A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		assertEquals("!(A(x) -> B(y))", pf.toStr().toString());
	}

	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInitInvalidInput1() {
		String input = "!";
		new JSPredicateForm(input);
	}

	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInitInvalidInput2() {
		String input = "!(A(x)";
		new JSPredicateForm(input);
	}

	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInitInvalidInput3() {
		String input = "!(A(x) -> B(y)) -> C";
		new JSPredicateForm(input);
	}

	@Test
	public void testJSPredicateFormInitNot() {
		String input = "not (A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		assertEquals("not (A(x) -> B(y))", pf.toStr().toString());
		assertEquals("!(A(x) -> B(y))", pf.clonePF().toStr().toString());
		assertFalse(pf.equals(new JSPredicateForm("!(A(x) -> B(y))")));
		assertTrue(pf.equals(pf.clonePF()));
	}

	@Test
	public void testJSPredicateFormInitCall() {
		String input = "call A(x) -> B(y)";
		JSPredicateForm pf = new JSPredicateForm(input);
		assertEquals("call A(x) -> B(y)", pf.toStr().toString());
		assertEquals("A(x)", pf.elementAt(1).toStr().toString());
		assertEquals("B(y)", pf.elementAt(pf.size() - 1).toStr().toString());
		assertEquals("B(y)", ((JSTerm) pf.lastElement()).toStr().toString());
		assertEquals(pf.size(), 3);
	}

	@Test(expected = JSParserError.class)
	public void testJSPredicateFormInitCallMissingRightParenthesis() {
		String input = "call A(x) -> B(y)";
		new JSPredicateForm(input);
	}

	@Test
	public void testJSPredicateFormInitNestedNot() {
		String input = "not not (A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		assertEquals("not not (A(x) -> B(y))", pf.toStr().toString());
		assertEquals("!(A(x) -> B(y))", pf.clonePF().toStr().toString());
		assertFalse(pf.equals(new JSPredicateForm("!(A(x) -> B(y))")));
		assertTrue(pf.equals(pf.clonePF()));
	}

	@Test
	public void testJSPredicateFormApplySubstitution() {
		String input1 = "A(x)";
		String input2 = "B(y)";
		String input3 = "not (A(x) -> B(y))";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSTerm term1 = new JSTerm("x");
		term1.setValue("a");
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		JSTerm term2 = new JSTerm("y");
		term2.setValue("b");
		JSSubstitution alpha = new JSSubstitution();
		alpha.addBinding("x", term1);
		alpha.addBinding("y", term2);
		JSPredicateForm expected = new JSPredicateForm("not (B(b) -> A(a))");
		JSPredicateForm result = pf3.applySubstitutionPF(alpha);
		assertEquals("not (B(b) -> A(a))", result.toStr().toString());
		assertTrue(result.equals(expected));
	}

	@Test
	public void testJSPredicateFormMatchesEmpty() {
		JSPredicateForm pf1 = new JSPredicateForm();
		JSPredicateForm pf2 = new JSPredicateForm();
		JSSubstitution beta = pf1.matches(pf2);
		assertTrue(beta.isFailure());
		assertNull(beta.getBindings());
	}

	@Test
	public void testJSPredicateFormMatchesSimple() {
		String input1 = "A(x)";
		String input2 = "A(x)";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		JSSubstitution alpha = new JSSubstitution();
		JSSubstitution beta = pf1.matches(pf2, alpha);
		assertTrue(beta.isSuccess());
		assertEquals(1, beta.size());
		assertEquals("x", beta.getBinding("x").getTerm().toString());
	}

	@Test
	public void testJSPredicateFormMatchesWithSubstitution() {
		String input1 = "A(x)";
		String input2 = "B(y)";
		String input3 = "not (A(x) -> B(y))";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSTerm term1 = new JSTerm("x");
		term1.setValue("a");
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		JSTerm term2 = new JSTerm("y");
		term2.setValue("b");
		JSPredicateForm pf3 = new JSPredicateForm(input3);
		JSSubstitution alpha = new JSSubstitution();
		alpha.addBinding("x", term1);
		alpha.addBinding("y", term2);
		JSSubstitution beta = pf3.matches(pf1, alpha);
		assertTrue(beta.isSuccess());
		assertEquals(2, beta.size());
		assertEquals("x", beta.getBinding("x").getTerm().toString());
		assertEquals("y", beta.getBinding("y").getTerm().toString());
	}

	@Test
	public void testJSPredicateFormMatchesFail() {
		String input1 = "A(x)";
		String input2 = "B(y)";
		String input3 = "not (A(x) -> B(y))";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		JSPredicateForm pf3 = new JSPredicateForm(input3);
		JSSubstitution beta = pf3.matches(pf1);
		assertTrue(beta.isFailure());
		assertNull(beta.getBindings());
	}

	@Test
	public void testJSPredicateFormToString() {
		String input = "!(A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		assertEquals("!(A(x) -> B(y))", pf.toStr().toString());
	}

	@Test
	public void testJSPredicateFormClonePF() {
		String input = "!(A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		JSPredicateForm clone = pf.clonePF();
		assertEquals("!(A(x) -> B(y))", clone.toStr().toString());
		assertTrue(pf.equals(clone));
	}

	@Test
	public void testJSPredicateFormStandarizerPredicateForm() {
		String input = "!(A(x) -> B(y))";
		JSPredicateForm pf = new JSPredicateForm(input);
		JSPredicateForm standardized = pf.standarizerPredicateForm();
		assertEquals("!(= x x) -> !(= y y)", standardized.toStr().toString());
	}

	@Test
	public void testJSPredicateFormEquals() {
		String input1 = "A(x)";
		String input2 = "A(x)";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		assertTrue(pf1.equals(pf2));
		assertTrue(pf1.equals(pf1.clonePF()));
	}

	@Test
	public void testJSPredicateFormNotEquals() {
		String input1 = "A(x)";
		String input2 = "B(y)";
		JSPredicateForm pf1 = new JSPredicateForm(input1);
		JSPredicateForm pf2 = new JSPredicateForm(input2);
		assertFalse(pf1.equals(pf2));
		assertFalse(pf1.equals(new JSPredicateForm("A(x)")));
	}

}