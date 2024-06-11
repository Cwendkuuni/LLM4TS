package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSPairVarTerm;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        String input = "(* x y)";
        List<String> expectedTermList = new ArrayList<>();
        expectedTermList.add("list");
        expectedTermList.add("call");
        expectedTermList.add(".");
        expectedTermList.add("x");
        expectedTermList.add(".");
        expectedTermList.add("y");

        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        List<JSTerm> actualTermList = term.cloneT().getChildren();

        for (int i = 0; i < expectedTermList.size(); ++i) {
            assertEquals(expectedTermList.get(i), actualTermList.get(i).toString());
        }
    }

    @Test(expected = JSParserError.class)
    public void testJSTermConstructorInvalidInput() {
        String input = "(* x)";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));
    }

    @Test
    public void testJSTermParseList() {
        String input = "(list 1 2 3)";
        List<String> expectedTermList = new ArrayList<>();
        expectedTermList.add("list");
        expectedTermList.add("nil");
        expectedTermList.add(".");
        expectedTermList.add("1");
        expectedTermList.add(".");
        expectedTermList.add("2");
        expectedTermList.add(".");
        expectedTermList.add("3");
        expectedTermList.add("nil");

        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        List<JSTerm> actualTermList = term.cloneT().getChildren();

        for (int i = 0; i < expectedTermList.size(); ++i) {
            assertEquals(expectedTermList.get(i), actualTermList.get(i).toString());
        }
    }

    @Test(expected = JSParserError.class)
    public void testJSTermParseListMissingRightParenthesis() {
        String input = "(list 1 2";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));
    }

    @Test
    public void testJSTermParseFunctionCall() {
        String input = "(* x y)";
        List<String> expectedTermList = new ArrayList<>();
        expectedTermList.add("list");
        expectedTermList.add("call");
        expectedTermList.add(".");
        expectedTermList.add("x");
        expectedTermList.add(".");
        expectedTermList.add("y");

        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        List<JSTerm> actualTermList = term.cloneT().getChildren();

        for (int i = 0; i < expectedTermList.size(); ++i) {
            assertEquals(expectedTermList.get(i), actualTermList.get(i).toString());
        }
    }

    @Test(expected = JSParserError.class)
    public void testJSTermParseFunctionCallMissingRightParenthesis() {
        String input = "* x y)";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));
    }

    @Test
    public void testJSTermEquals() {
        String input1 = "(+ 1 2)";
        String input2 = "(+ 1 2)";
        JSTerm term1 = new JSTerm();
        term1.parse(new StringReader(input1));

        JSTerm term2 = new JSTerm();
        term2.parse(new StringReader(input2));

        assertTrue(term1.equals(term2));
    }

    @Test
    public void testJSTermNotEquals() {
        String input1 = "(+ 1 2)";
        String input2 = "(* 1 2)";
        JSTerm term1 = new JSTerm();
        term1.parse(new StringReader(input1));

        JSTerm term2 = new JSTerm();
        term2.parse(new StringReader(input2));

        assertFalse(term1.equals(term2));
    }

    @Test
    public void testJSTermMatches() {
        String term1Input = "(+ x y)";
        String term2Input = "(* x z)";

        JSTerm term1 = new JSTerm();
        term1.parse(new StringReader(term1Input));

        JSTerm term2 = new JSTerm();
        term2.parse(new StringReader(term2Input));

        JSSubstitution substitution = term1.matches(term2);

        assertTrue(substitution.isEmpty());

        term2Input = "(+ x y)";
        term2 = new JSTerm();
        term2.parse(new StringReader(term2Input));

        substitution = term1.matches(term2);

        List<JSPairVarTerm> expectedSubstitutionList = new ArrayList<>();
        expectedSubstitutionList.add(new JSPairVarTerm("x", "x"));
        expectedSubstitutionList.add(new JSPairVarTerm("y", "y"));

        assertEquals(expectedSubstitutionList, substitution.getSubstitutionList());
    }

    @Test
    public void testJSTermCloneT() throws IOException, JSParserError {
        String input = "(+ x y)";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        JSTerm clonedTerm = term.cloneT();

        List<JSTerm> termList1 = clonedTerm.getChildren();
        List<JSTerm> termList2 = term.getChildren();

        for (int i = 0; i < termList1.size(); ++i) {
            assertEquals(termList2.get(i).toString(), termList1.get(i).toString());
        }
    }

    @Test
    public void testJSTermApplySubstitutionT() throws IOException, JSParserError {
        String term1Input = "(+ x y)";
        String term2Input = "(* z w)";

        JSTerm term1 = new JSTerm();
        term1.parse(new StringReader(term1Input));

        JSTerm term2 = new JSTerm();
        term2.parse(new StringReader(term2Input));

        JSTerm term3 = term1.applySubstitutionT(new JSSubstitution());
        term3.addVariable("x", "z");
        term3.addVariable("y", "w");

        String expectedTerm3Input = "(* z w)";

        assertEquals(expectedTerm3Input, term3.toString());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        String input = "(+ x y)";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        JSTerm standardizedTerm = term.standardizerTerm();

        String expectedStandardizedTermInput = "(* x1 y1)";

        assertEquals(expectedStandardizedTermInput, standardizedTerm.toString());
    }

    @Test
    public void testJSTermCall() {
        String input = "(* (+ 1 2) 3)";
        JSTerm term = new JSTerm();
        term.parse(new StringReader(input));

        JSTerm result = term.call();

        String expectedResultInput = "(+ 1 3)";

        assertEquals(expectedResultInput, result.toString());
    }
}