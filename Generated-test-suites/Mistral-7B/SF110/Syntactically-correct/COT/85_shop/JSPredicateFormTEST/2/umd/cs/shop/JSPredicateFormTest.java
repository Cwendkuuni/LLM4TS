package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInitEmpty() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(0, pf.size());
    }

    @Test
    public void testJSPredicateFormInitSingleTerm() {
        JSPredicateForm pf = new JSPredicateForm("P(x)");
        assertEquals("P(x)", pf.elementAt(0));
        assertEquals(1, pf.size());
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitInvalidInput() {
        JSPredicateForm pf = new JSPredicateForm("(x + 1)");
    }

    @Test
    public void testJSPredicateFormInitNot() {
        JSPredicateForm pf = new JSPredicateForm("not (P(x))");
        assertEquals("not", pf.elementAt(0));
        assertEquals(1, pf.size());
        assertEquals("P(x)", pf.elementAt(1).toString());
    }

    @Test
    public void testJSPredicateFormInitNotSubForm() {
        JSPredicateForm pf = new JSPredicateForm("not (P(x) -> Q(y))");
        assertEquals("not", pf.elementAt(0));
        assertEquals(2, pf.size());
        assertEquals("P(x)", pf.elementAt(1).toString());
        assertEquals("Q(y)", pf.elementAt(2).toString());
    }

    @Test
    public void testJSPredicateFormInitCall() {
        JSPredicateForm pf = new JSPredicateForm("(call f(x))");
        assertEquals("call", pf.elementAt(0));
        assertEquals(1, pf.size());
        JSTerm term = (JSTerm) pf.elementAt(1);
        assertEquals("f", term.getFunctionSymbol());
        assertEquals("x", term.getArgumentAt(0));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitCallInvalidInput() {
        JSPredicateForm pf = new JSPredicateForm("call (x + 1)");
    }

    @Test
    public void testJSPredicateFormPrint() {
        JSPredicateForm pf = new JSPredicateForm("P(x) -> Q(y)");
        pf.print();
        assertTrue(System.getProperty("line.separator").isEmpty()
                || System.out.toString().contains("(P(x) -> Q(y))"));
    }

    @Test
    public void testJSPredicateFormToStr() {
        JSPredicateForm pf = new JSPredicateForm("P(x) -> Q(y)");
        String expected = "((P(x) -> Q(y)))";
        assertEquals(expected, pf.toStr().toString());
    }

    @Test
    public void testJSPredicateFormClonePF() {
        JSPredicateForm pf = new JSPredicateForm("P(x) -> Q(y)");
        JSPredicateForm pfClone = pf.clonePF();
        assertEquals(pf.size(), pfClone.size());
        for (int i = 0; i < pf.size(); i++) {
            assertEquals(pf.elementAt(i), pfClone.elementAt(i));
        }
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        JSPredicateForm pf = new JSPredicateForm("P(x) -> Q(y)");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addBinding("x", new JSTerm("a"));
        alpha.addBinding("y", new JSTerm("b"));
        JSPredicateForm expected = new JSPredicateForm("P(a) -> Q(b)");
        JSPredicateForm result = pf.applySubstitutionPF(alpha);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.elementAt(i), result.elementAt(i));
        }
    }

    @Test
    public void testJSPredicateFormMatches() {
        JSPredicateForm pf1 = new JSPredicateForm("P(x) -> Q(y)");
        JSPredicateForm pf2 = new JSPredicateForm("R(z) -> S(w)");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        JSTerm termX = new JSTerm("x");
        JSTerm termY = new JSTerm("y");
        JSTerm termZ = new JSTerm("z");
        JSTerm termW = new JSTerm("w");
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("z", termZ);
        alpha.addBinding("w", termW);

        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("y", termW);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termW);
        alpha.addBinding("z", termZ);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termZ);
        alpha.addBinding("y", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("z", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("z", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termY);
        alpha.addBinding("y", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termW);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.equals(alpha));

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("z", termX);
        alpha.addBinding("w", termZ);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termY);
        alpha.addBinding("z", termY);
        alpha.addBinding("w", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termW);
        alpha.addBinding("z", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termW);
        alpha.addBinding("z", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termY);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termY);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termW);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termW);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.equals(alpha));

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termX);
        alpha.addBinding("w", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termY);
        alpha.addBinding("w", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termW);
        alpha.addBinding("z", termX);
        alpha.addBinding("w", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termW);
        alpha.addBinding("z", termY);
        alpha.addBinding("w", termX);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termX);
        alpha.addBinding("w", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());

        alpha.clear();
        alpha.addBinding("x", termW);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termY);
        alpha.addBinding("w", termY);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.equals(alpha));

        alpha.clear();
        alpha.addBinding("x", termX);
        alpha.addBinding("y", termX);
        alpha.addBinding("z", termX);
        alpha.addBinding("w", termW);
        alpha.addBinding("v", termW);
        beta = pf1.matches(pf2, alpha);
        assertTrue(beta.isFailure());
    }

    @Test
    public void testJSPredicateFormEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("P(x) -> Q(y)");
        JSPredicateForm pf2 = new JSPredicateForm("P(x) -> Q(y)");
        JSPredicateForm pf3 = new JSPredicateForm("R(x) -> S(y)");
        assertTrue(pf1.equals(pf2));
        assertFalse(pf1.equals(pf3));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm("not (P(x) | Q(y))");
        JSPredicateForm expected = new JSPredicateForm("not (or (P(x)) (Q(y)))");
        JSPredicateForm result = pf.standarizerPredicateForm();
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.elementAt(i), result.elementAt(i));
        }
    }
}