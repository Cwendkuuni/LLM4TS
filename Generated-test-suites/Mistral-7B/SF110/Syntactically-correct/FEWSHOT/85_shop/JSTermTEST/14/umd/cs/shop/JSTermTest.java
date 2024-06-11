package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

    @Test
    public void testJSTermConstruction() {
        // Test default constructor
        JSTerm term = new JSTerm();
        assertNull(term.elementAt(0));
        assertFalse(term.isConstant());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());

        // Test constructor with StreamTokenizer
        JSTerm tokenizerTerm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals("+", tokenizerTerm.elementAt(0));
        assertTrue(tokenizerTerm.isFunction());
        assertFalse(tokenizerTerm.isVariable());
        assertFalse(tokenizerTerm.isConstant());
        assertFalse(tokenizerTerm.isEval());
    }

    @Test
    public void testJSTermIsVariable() {
        JSTerm variableTerm = new JSTerm(new StreamTokenizer(new StringReader("x"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(variableTerm.isVariable());
        assertTrue(variableTerm.isVariable());
        assertFalse(variableTerm.isFunction());
        assertFalse(variableTerm.isConstant());
        assertFalse(variableTerm.isEval());
    }

    @Test
    public void testJSTermIsConstant() {
        JSTerm constantTerm = new JSTerm(new StreamTokenizer(new StringReader("3"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(constantTerm.isConstant());
        assertFalse(constantTerm.isVariable());
        assertFalse(constantTerm.isFunction());
        assertFalse(constantTerm.isEval());

        JSTerm constantTerm2 = new JSTerm(new StreamTokenizer(new StringReader("'foo'"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(constantTerm2.isConstant());
        assertFalse(constantTerm2.isVariable());
        assertFalse(constantTerm2.isFunction());
        assertFalse(constantTerm2.isEval());
    }

    @Test
    public void testJSTermIsFunction() {
        JSTerm functionTerm = new JSTerm(new StreamTokenizer(new StringReader("(+)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(functionTerm.isFunction());
        assertFalse(functionTerm.isVariable());
        assertFalse(functionTerm.isConstant());
        assertFalse(functionTerm.isEval());

        JSTerm functionTerm2 = new JSTerm(new StreamTokenizer(new StringReader("(cons)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(functionTerm2.isFunction());
        assertFalse(functionTerm2.isVariable());
        assertFalse(functionTerm2.isConstant());
        assertFalse(functionTerm2.isEval());
    }

    @Test
    public void testJSTermIsEval() {
        JSTerm evalTerm = new JSTerm(new StreamTokenizer(new StringReader("(eval 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertFalse(evalTerm.isEval());

        JSTerm evalTerm2 = new JSTerm(new StreamTokenizer(new StringReader("(eval (+ 1 2))"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(evalTerm2.isEval());
    }

    @Test
    public void testJSTermClone() {
        JSTerm originalTerm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm clonedTerm = originalTerm.cloneT();
        assertEquals(originalTerm, clonedTerm);

        originalTerm.addElement("3");
        assertNotEquals(originalTerm, clonedTerm);
    }

    @Test
    public void testJSTermApplySubstitution() {
        JSTerm originalTerm = new JSTerm(new StreamTokenizer(new StringReader("(x)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm substituteTerm = new JSTerm(new StreamTokenizer(new StringReader("(y)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("x", substituteTerm));

        JSTerm resultTerm = originalTerm.applySubstitutionT(substitution);
        assertEquals(substituteTerm, resultTerm);

        originalTerm.addElement("3");
        resultTerm = originalTerm.applySubstitutionT(substitution);
        assertEquals("3", resultTerm.elementAt(0).toString());
    }

    @Test
    public void testJSTermMatches() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(y)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(x)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPair("x", term2));

        JSSubstitution result = term1.matches(term3, substitution);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSTermEquals() {
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };

        assertEquals(term1, term2);
        assertNotEquals(term1, term3);
    }

    @Test
    public void testJSTermPrint() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        String expectedOutput = "+ 1 2 \n";
        String actualOutput = "";
        JSUtil.output = new StringWriter();
        term.print();
        actualOutput = JSUtil.output.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJSTermSize() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals(3, term.size());
    }

    @Test
    public void testJSTermElementAt() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals("+", term.elementAt(0).toString());
        assertEquals("1", term.elementAt(1).toString());
        assertEquals("2", term.elementAt(2).toString());
    }

    @Test
    public void testJSTermAddElement() {
        JSTerm term = new JSTerm();
        term.addElement("x");
        assertEquals("x", term.elementAt(0).toString());
    }

    @Test
    public void testJSTermAddElements() {
        JSTerm term = new JSTerm();
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        term.addElements(term2);
        assertEquals("+", term.elementAt(0).toString());
        assertEquals("1", term.elementAt(1).toString());
        assertEquals("2", term.elementAt(2).toString());
    }

    @Test
    public void testJSTermMakeFunction() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        term.makeFunction();
        assertFalse(term.isVariable());
        assertTrue(term.isFunction());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermMakeVariable() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        term.makeVariable();
        assertTrue(term.isVariable());
        assertFalse(term.isFunction());
        assertFalse(term.isConstant());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermMakeConstant() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("3"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        term.makeConstant();
        assertTrue(term.isConstant());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
    }

    @Test
    public void testJSTermMakeEval() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(eval 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        term.makeEval(true);
        assertTrue(term.isEval());
        assertFalse(term.isVariable());
        assertFalse(term.isFunction());
        assertFalse(term.isConstant());
    }

    @Test
    public void testJSTermToString() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        String expectedOutput = "+ 1 2 \n";
        String actualOutput = term.toStr().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testJSTermIsGround() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertFalse(term.isGround());

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(term.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm standardizedTerm = term.standardizerTerm();
        assertEquals("(+ ?x 2)", standardizedTerm.elementAt(0).toString());
    }

    @Test
    public void testJSTermCall() {
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(+ x 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        JSTerm resultTerm = term.call();
        assertEquals("(+ ?x 2)", resultTerm.elementAt(0).toString());

        term = new JSTerm(new StreamTokenizer(new StringReader("(- x)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        resultTerm = term.call();
        assertEquals("(- ?x)", resultTerm.elementAt(0).toString());

        term = new JSTerm(new StreamTokenizer(new StringReader("(* x 2)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        resultTerm = term.call();
        assertEquals("(* ?x 2)", resultTerm.elementAt(0).toString());

        term = new JSTerm(new StreamTokenizer(new StringReader("(+ (+ 1 2) 3)"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        resultTerm = term.call();
        assertEquals("(+ 1 5)", resultTerm.elementAt(0).toString());

        term = new JSTerm(new StreamTokenizer(new StringReader("(not (= x x))"))) {
            @Override
            public void pushBack() {
                throw new UnsupportedOperationException();
            }
        };
        resultTerm = term.call();
        assertEquals("(not (= ?x ?x))", resultTerm.elementAt(0).toString());
    }
}