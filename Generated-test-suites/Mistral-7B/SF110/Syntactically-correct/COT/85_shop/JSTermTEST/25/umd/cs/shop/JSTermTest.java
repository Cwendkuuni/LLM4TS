import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSTermUtil;

public class JSTermTest {

    @Test
    public void testConstructor() {
        JSTerm term = new JSTerm();
        Assert.assertFalse(term.isEval);
        Assert.assertFalse(term.isVariable);
        Assert.assertFalse(term.isConstant);
        Assert.assertFalse(term.isFunction);

        JSTerm termFromTokenizer = JSTermUtil.createTestTerm();
        JSTerm newTerm = new JSTerm(termFromTokenizer.getTokenizer());
        Assert.assertEquals(termFromTokenizer, newTerm);
    }

    @Test
    public void testIsVariable() {
        JSTerm variableTerm = JSTermUtil.createTestVariableTerm();
        Assert.assertTrue(variableTerm.isVariable());

        JSTerm constantTerm = JSTermUtil.createTestConstantTerm();
        Assert.assertFalse(constantTerm.isVariable());

        JSTerm functionTerm = JSTermUtil.createTestFunctionTerm();
        Assert.assertFalse(functionTerm.isVariable());
    }

    @Test
    public void testIsConstant() {
        JSTerm constantVariableTerm = JSTermUtil.createTestConstantVariableTerm();
        Assert.assertTrue(constantVariableTerm.isConstant());

        JSTerm variableTerm = JSTermUtil.createTestVariableTerm();
        Assert.assertFalse(variableTerm.isConstant());

        JSTerm constantTerm = JSTermUtil.createTestConstantTerm();
        Assert.assertTrue(constantTerm.isConstant());
    }

    @Test
    public void testIsFunction() {
        JSTerm functionTerm = JSTermUtil.createTestFunctionTerm();
        Assert.assertTrue(functionTerm.isFunction());

        JSTerm constantTerm = JSTermUtil.createTestConstantTerm();
        Assert.assertFalse(constantTerm.isFunction());

        JSTerm variableTerm = JSTermUtil.createTestVariableTerm();
        Assert.assertFalse(variableTerm.isFunction());
    }

    @Test
    public void testIsEval() {
        JSTerm term = JSTermUtil.createTestTerm();
        Assert.assertFalse(term.isEval());

        JSTerm evalTerm = new JSTerm();
        evalTerm.makeEval(true);
        Assert.assertTrue(evalTerm.isEval());
    }

    @Test
    public void testMakeFunction() {
        JSTerm term = JSTermUtil.createTestTerm();
        term.makeFunction();
        Assert.assertTrue(term.isFunction());
        Assert.assertFalse(term.isVariable);
        Assert.assertFalse(term.isConstant);
        Assert.assertFalse(term.isEval);
    }

    @Test
    public void testMakeVariable() {
        JSTerm term = JSTermUtil.createTestTerm();
        term.makeVariable();
        Assert.assertTrue(term.isVariable());
        Assert.assertFalse(term.isConstant);
        Assert.assertFalse(term.isFunction);
        Assert.assertFalse(term.isEval);
    }

    @Test
    public void testMakeConstant() {
        JSTerm term = JSTermUtil.createTestTerm();
        term.makeConstant();
        Assert.assertTrue(term.isConstant());
        Assert.assertFalse(term.isVariable);
        Assert.assertFalse(term.isFunction);
        Assert.assertFalse(term.isEval);
    }

    @Test
    public void testMakeEval() {
        JSTerm term = JSTermUtil.createTestTerm();
        term.makeEval(true);
        Assert.assertTrue(term.isEval());
        Assert.assertFalse(term.isVariable);
        Assert.assertFalse(term.isConstant);
        Assert.assertFalse(term.isFunction);
    }

    @Test
    public void testParseList() {
        JSTerm term = JSTermUtil.createTestTerm();
        JSTerm listTerm = new JSTerm();
        listTerm.addElement(term);
        listTerm.addElement(JSTermUtil.createTestTerm());
        JSTerm parsedList = term.parseList(term.getTokenizer());
        Assert.assertEquals(listTerm, parsedList);

        JSTerm emptyList = new JSTerm();
        emptyList.addElement("nil");
        emptyList.makeConstant();
        JSTerm parsedEmptyList = term.parseList(term.getTokenizer());
        Assert.assertEquals(emptyList, parsedEmptyList);
    }

    @Test
    public void testPrint() {
        JSTerm term = JSTermUtil.createTestTerm();
        String expectedOutput = "(" + term.elementAt(0) + " ";
        for (int i = 1; i < term.size(); ++i) {
            expectedOutput += term.elementAt(i).toStr();
        }
        expectedOutput += ")";
        String actualOutput = "";
        try (StringWriter sw = new StringWriter()) {
            JSUtil.printTo(term, sw);
            actualOutput = sw.toString();
        }
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloneT() {
        JSTerm term = JSTermUtil.createTestTerm();
        JSTerm clonedTerm = term.cloneT();
        Assert.assertEquals(term, clonedTerm);

        term.addElement(JSTermUtil.createTestTerm());
        clonedTerm = term.cloneT();
        Assert.assertEquals(term, clonedTerm);
        Assert.assertNotSame(term, clonedTerm);
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = JSTermUtil.createTestTerm();
        JSTerm substitutedTerm = term.applySubstitutionT(new JSSubstitution());
        Assert.assertEquals(term, substitutedTerm);

        term.addElement(JSTermUtil.createTestVariableTerm());
        JSTerm newTerm = JSTermUtil.createTestTerm();
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairTerm(term, newTerm));
        substitutedTerm = term.applySubstitutionT(substitution);
        Assert.assertEquals(newTerm, substitutedTerm);
    }

    @Test
    public void testMatches() {
        JSTerm term1 = JSTermUtil.createTestTerm();
        JSTerm term2 = JSTermUtil.createTestTerm();
        JSSubstitution substitution = term1.matches(term2);
        Assert.assertEquals(term1, term2);
        Assert.assertNull(substitution);

        term1.addElement(JSTermUtil.createTestConstantTerm("a"));
        term2.addElement(JSTermUtil.createTestVariableTerm());
        substitution = term1.matches(term2);
        Assert.assertNotNull(substitution);
        Assert.assertEquals(1, substitution.size());
        Assert.assertEquals("a", substitution.elementAt(0).getValue().toString());
    }

    @Test(expected = JSParserError.class)
    public void testConstructorWithError() {
        JSTerm term = new JSTerm();
        if (!JSUtil.readToken(term.getTokenizer(), "Expecting Term")) {
            throw new JSParserError();
        }
        term.parseList(term.getTokenizer());
    }
}