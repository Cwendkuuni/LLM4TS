import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.*;

public class JSTermTest {

    @Test
    public void testEmptyConstructor() {
        JSTerm term = new JSTerm();
        // Check if the term is empty
        Assert.assertEquals("Empty term", term.size(), 0);
    }

    @Test
    public void testConstructorWithTokenizer() {
        // Assuming we have a valid StreamTokenizer instance called tokenizer
        JSTerm term = new JSTerm(tokenizer);
        // Check the term's properties based on the source code
        Assert.assertEquals("Is Eval", term.isEval, false);
        Assert.assertEquals("Is Variable", term.isVariable, false);
        Assert.assertEquals("Is Constant", term.isConstant, false);
        Assert.assertEquals("Is Function", term.isFunction, false);

        // Given the constructor's logic, we cannot check specific elements now.
        // Test individual methods or edge cases instead.
    }

    @Test
    public void testParseList() {
        // Given a test case for the parseList method

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list (+ 1 2) (+ 3 4))"));
        JSTerm term = new JSTerm();

        term.parseList(tokenizer);

        // Check the term's properties and structure
        Assert.assertEquals("Size", term.size(), 3);
        Assert.assertEquals("Term at index 0", term.elementAt(0).toString(), "list");
        Assert.assertEquals("Term at index 1", term.elementAt(1).toString(), "(+ 1 2)");
        Assert.assertEquals("Term at index 2", term.elementAt(2).toString(), "(+ 3 4)");
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("y");
        term2.makeVariable();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("x", term2));

        JSTerm result = term1.applySubstitutionT(substitution);

        Assert.assertEquals("Size", result.size(), 1);
        Assert.assertEquals("Term at index 0", result.elementAt(0).toString(), term2.toString());

        JSTerm term3 = new JSTerm();
        term3.addElement("z");
        term3.makeVariable();

        JSTerm term4 = new JSTerm();
        term4.addElement("w");
        term4.makeVariable();

        substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("x", term2));
        substitution.addElement(new JSPairVarTerm("y", term3));
        substitution.addElement(new JSPairVarTerm("z", term4));

        result = term1.applySubstitutionT(substitution);

        Assert.assertEquals("Size", result.size(), 1);
        Assert.assertEquals("Term at index 0", result.elementAt(0).toString(), term4.toString());
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("x");
        term1.makeVariable();

        JSTerm term2 = new JSTerm();
        term2.addElement("x");
        term2.makeVariable();

        JSTerm term3 = new JSTerm();
        term3.addElement("y");
        term3.makeVariable();

        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("x", term2));

        JSSubstitution result = term1.matches(term3);
        Assert.assertEquals("Matches: Failure", result.fail(), false);

        result = term1.matches(term1, substitution);
        Assert.assertEquals("Matches: Success", result.size(), 1);

        JSTerm term4 = new JSTerm();
        term4.addElement("x");
        term4.makeVariable();

        substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("x", term2));

        result = term1.matches(term4, substitution);
        Assert.assertEquals("Matches: Success", result.size(), 1);

        substitution = new JSSubstitution();
        substitution.addElement(new JSPairVarTerm("y", term3));

        result = term1.matches(term1, substitution);
        Assert.assertEquals("Matches: Failure", result.size(), 0);
    }

    // Add more test cases for other methods in the class, such as equals(), toString(), isGround(), standardizerTerm(), and call()
}