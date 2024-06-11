import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JSTermTest {

	@Test
	public void testJSTermEmptyConstructor() {
		JSTerm term = new JSTerm();
		assertTrue(term.isEmpty());
		assertFalse(term.isVariable());
		assertFalse(term.isConstant());
		assertFalse(term.isFunction());
		assertFalse(term.isEval());
		assertEquals("", term.toStr().toString());
	}

	@Test
	public void testJSTermTokenizerConstructor() {
		String input = "(* 3 ( + 2 4 ))";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertEquals(3, term.size());
		assertFalse(term.isVariable());
		assertFalse(term.isConstant());
		assertTrue(term.isFunction());
		assertTrue(term.isEval());

		assertEquals("*", term.elementAt(0));
		assertEquals("3", term.elementAt(1).toStr().toString());
		JSTerm secondTerm = term.elementAt(2);
		assertEquals("+", secondTerm.elementAt(0));
		assertEquals("2", secondTerm.elementAt(1).toStr().toString());
		assertEquals("4", secondTerm.elementAt(2).toStr().toString());
	}

	@Test
	public void testJSTermParseList() {
		String input = "(cons 1 2)";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertEquals(3, term.size());
		assertTrue(term.isFunction());
		assertEquals("cons", term.elementAt(0));
		assertEquals("1", term.elementAt(1).toStr().toString());
		assertEquals("2", term.elementAt(2).toStr().toString());
	}

	@Test
	public void testJSTermApplySubstitution() {
		JSTerm a = new JSTerm();
		a.makeVariable();
		a.addElement("a");

		JSTerm b = new JSTerm();
		b.makeVariable();
		b.addElement("b");

		JSTerm term = new JSTerm();
		term.makeFunction();
		term.addElement("add");
		term.addElement(a);
		term.addElement(b);

		JSSubstitution alpha = new JSSubstitution();
		alpha.addElement(new JSPairVarTerm(b, a));

		JSTerm result = term.applySubstitutionT(alpha);

		assertEquals("add", result.elementAt(0));
		assertEquals("a", result.elementAt(1).toStr().toString());
		assertEquals("a", result.elementAt(2).toStr().toString());
	}

	@Test
	public void testJSTermEquals() {
		JSTerm a = new JSTerm();
		a.makeVariable();
		a.addElement("a");

		JSTerm b = new JSTerm();
		b.makeVariable();
		b.addElement("b");

		JSTerm c = new JSTerm();
		c.makeVariable();
		c.addElement("a");

		JSTerm d = new JSTerm();
		d.makeConstant();
		d.addElement("3");

		JSTerm e = new JSTerm();
		e.makeFunction();
		e.addElement("add");
		e.addElement(a);
		e.addElement(b);

		JSTerm f = new JSTerm();
		f.makeFunction();
		f.addElement("add");
		f.addElement(c);
		f.addElement(b);

		assertTrue(a.equals(a));
		assertTrue(b.equals(b));
		assertTrue(c.equals(c));
		assertTrue(d.equals(d));
		assertFalse(a.equals(b));
		assertFalse(a.equals(c));
		assertFalse(a.equals(d));
		assertTrue(e.equals(f));
	}

	@Test
	public void testJSTermParseListWithDots() {
		String input = "(cons . (cons 1 2))";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertEquals(4, term.size());
		assertTrue(term.isFunction());
		assertEquals("cons", term.elementAt(0));
		assertEquals(".", term.elementAt(1));
		assertEquals("cons", term.elementAt(2).toStr().toString());
		assertEquals("1", term.elementAt(3).toStr().toString());
		assertEquals("2", term.elementAt(4).toStr().toString());
	}

	@Test
	public void testJSTermToString() {
		JSTerm term = new JSTerm();
		term.makeConstant();
		term.addElement("true");

		assertEquals("true", term.toStr().toString());
	}

	@Test
	public void testJSTermCall() {
		String input = "(+ 1 2)";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertEquals(2, term.size());
		assertEquals("+", term.elementAt(0));
		assertEquals("1", term.elementAt(1).toStr().toString());
		assertEquals(1, term.elementAt(2).size());
		assertEquals("1", term.elementAt(2).elementAt(0).toStr().toString());

		assertEquals(1, term.call().size());
		assertEquals("1", term.call().elementAt(0).toStr().toString());
	}

	@Test
	public void testJSTermStandardizerTerm() {
		String input = "(* 3 ( + 2 4 ))";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertEquals("*", term.elementAt(0).toStr().toString());
		assertEquals("3", term.elementAt(1).toStr().toString());
		assertEquals("+", term.elementAt(2).elementAt(0).toStr().toString());
		assertEquals("2", term.elementAt(2).elementAt(1).toStr().toString());
		assertEquals("4", term.elementAt(2).elementAt(2).toStr().toString());

		JSTerm standardized = term.standardizerTerm();

		assertEquals("*", standardized.elementAt(0).toStr().toString());
		assertEquals("3", standardized.elementAt(1).toStr().toString());
		assertEquals(3, standardized.size() - 1);
		assertEquals("+", standardized.elementAt(2).elementAt(0).toStr().toString());
		assertEquals("2", standardized.elementAt(2).elementAt(1).toStr().toString());
		assertEquals("4", standardized.elementAt(2).elementAt(2).toStr().toString());
		assertEquals("X1", standardized.elementAt(3).toStr().toString());
		assertEquals("X2", standardized.elementAt(4).toStr().toString());
		assertEquals("X3", standardized.elementAt(5).toStr().toString());
	}

	@Test
	public void testJSTermIsGround() {
		String input = "(+ 1 2)";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertFalse(term.isGround());

		input = "true";
		term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertTrue(term.isGround());

		input = "(cons 1 2)";
		term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertFalse(term.isGround());

		input = "(not true)";
		term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		assertTrue(term.isGround());
	}

	@Test
	public void testJSTermMatches() {
		String input1 = "(* 3 ( + 2 4 ))";
		JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader(input1)));

		String input2 = "(* 3 ( + x y ))";
		JSTerm term2 = new JSTerm();
		term2.makeVariable();
		term2.addElement("x");
		term2.addElement("+");
		term2.addElement(new JSTerm());
		term2.addElement(new JSTerm());

		JSSubstitution substitution = term1.matches(term2);

		assertTrue(substitution.isEmpty());

		input1 = "(+ 1 2)";
		term1 = new JSTerm(new StreamTokenizer(new StringReader(input1)));

		input2 = "(+ x y)";
		term2 = new JSTerm();
		term2.makeVariable();
		term2.addElement("x");
		term2.addElement("+");
		term2.addElement(new JSTerm());
		term2.addElement(new JSTerm());

		substitution = term1.matches(term2);

		assertEquals("1", substitution.elementAt(0).elementAt(1).toStr().toString());
		assertEquals("2", substitution.elementAt(1).elementAt(1).toStr().toString());
	}

	@Test
	public void testJSTermClone() {
		String input = "(+ 1 2)";
		JSTerm term = new JSTerm(new StreamTokenizer(new StringReader(input)));

		JSTerm clone = term.cloneT();

		assertEquals("+", clone.elementAt(0).toStr().toString());
		assertEquals("1", clone.elementAt(1).toStr().toString());
		assertEquals("2", clone.elementAt(2).toStr().toString());
		assertEquals(2, clone.size());
		assertEquals(true, clone.isFunction());
		assertEquals(true, clone.isEval());
	}
}