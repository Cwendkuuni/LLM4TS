package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class JSTermTest {

    @Test
    public void testJSTermConstruction() {
        JSTerm term = new JSTerm();
        assertFalse(term.isVariable());
        assertFalse(term.isConstant());
        assertFalse(term.isFunction());
        assertFalse(term.isEval());
        assertNull(term.elementAt(0));
        assertFalse(term.isEmpty());
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("")); // empty input
        JSTerm termFromInput = new JSTerm(tokenizer);
        assertFalse(termFromInput.isVariable());
        assertFalse(termFromInput.isConstant());
        assertFalse(termFromInput.isFunction());
        assertFalse(termFromInput.isEval());
        assertNull(termFromInput.elementAt(0));
        assertFalse(termFromInput.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("Term")); // valid term
        JSTerm validTerm = new JSTerm(tokenizer);
        assertTrue(validTerm.isTerm());
        assertEquals("Term", validTerm.elementAt(0));
        assertFalse(validTerm.isVariable());
        assertFalse(validTerm.isConstant());
        assertFalse(validTerm.isFunction());
        assertFalse(validTerm.isEval());
        assertTrue(validTerm.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(Term1)")); // term with empty list
        JSTerm termWithEmptyList = new JSTerm(tokenizer);
        assertTrue(termWithEmptyList.isTerm());
        assertEquals("Term1", termWithEmptyList.elementAt(0).elementAt(0));
        assertTrue(termWithEmptyList.isList());
        assertTrue(termWithEmptyList.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(Term1 Term2)")); // term with list
        JSTerm termWithList = new JSTerm(tokenizer);
        assertTrue(termWithList.isTerm());
        assertEquals("Term1", termWithList.elementAt(0).elementAt(0));
        assertTrue(termWithList.isList());
        List<JSTerm> list = Arrays.asList(termWithList.elementAt(1));
        assertEquals("Term2", list.get(0).elementAt(0));
        assertFalse(list.get(0).isEmpty());
    }

    @Test
    public void testJSTermVariableConstruction() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x")); // valid variable name
        JSTerm variable = new JSTerm(tokenizer);
        assertTrue(variable.isVariable());
        assertEquals("x", variable.elementAt(0));
        assertFalse(variable.isConstant());
        assertFalse(variable.isFunction());
        assertFalse(variable.isEval());
        assertTrue(variable.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("y")); // valid variable name
        JSTerm anotherVariable = new JSTerm(tokenizer);
        assertTrue(anotherVariable.isVariable());
        assertEquals("y", anotherVariable.elementAt(0));
        assertFalse(anotherVariable.isConstant());
        assertFalse(anotherVariable.isFunction());
        assertFalse(anotherVariable.isEval());
        assertTrue(anotherVariable.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("z")); // valid variable name
        JSTerm thirdVariable = new JSTerm(tokenizer);
        assertTrue(thirdVariable.isVariable());
        assertEquals("z", thirdVariable.elementAt(0));
        assertFalse(thirdVariable.isConstant());
        assertFalse(thirdVariable.isFunction());
        assertFalse(thirdVariable.isEval());
        assertTrue(thirdVariable.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("x1")); // valid variable name with number suffix
        JSTerm variableWithNumberSuffix = new JSTerm(tokenizer);
        assertTrue(variableWithNumberSuffix.isVariable());
        assertEquals("x1", variableWithNumberSuffix.elementAt(0));
        assertFalse(variableWithNumberSuffix.isConstant());
        assertFalse(variableWithNumberSuffix.isFunction());
        assertFalse(variableWithNumberSuffix.isEval());
        assertTrue(variableWithNumberSuffix.isEmpty());
    }

    @Test
    public void testJSTermConstantConstruction() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("3")); // valid constant
        JSTerm constant = new JSTerm(tokenizer);
        assertFalse(constant.isVariable());
        assertFalse(constant.isConstant());
        assertFalse(constant.isFunction());
        assertFalse(constant.isEval());
        assertEquals("3", constant.elementAt(0));
        assertTrue(constant.isEmpty());
        assertTrue(constant.isConstant());

        tokenizer = new StreamTokenizer(new StringReader("4.5")); // valid constant with decimal
        JSTerm constantWithDecimal = new JSTerm(tokenizer);
        assertFalse(constantWithDecimal.isVariable());
        assertFalse(constantWithDecimal.isConstant());
        assertFalse(constantWithDecimal.isFunction());
        assertFalse(constantWithDecimal.isEval());
        assertEquals("4.5", constantWithDecimal.elementAt(0));
        assertTrue(constantWithDecimal.isEmpty());
        assertTrue(constantWithDecimal.isConstant());

        tokenizer = new StreamTokenizer(new StringReader("5e3")); // valid constant with exponential
        JSTerm constantWithExponential = new JSTerm(tokenizer);
        assertFalse(constantWithExponential.isVariable());
        assertFalse(constantWithExponential.isConstant());
        assertFalse(constantWithExponential.isFunction());
        assertFalse(constantWithExponential.isEval());
        assertEquals("5e3", constantWithExponential.elementAt(0));
        assertTrue(constantWithExponential.isEmpty());
        assertTrue(constantWithExponential.isConstant());

        tokenizer = new StreamTokenizer(new StringReader("true")); // valid boolean constant
        JSTerm booleanConstant = new JSTerm(tokenizer);
        assertFalse(booleanConstant.isVariable());
        assertFalse(booleanConstant.isConstant());
        assertFalse(booleanConstant.isFunction());
        assertFalse(booleanConstant.isEval());
        assertEquals("true", booleanConstant.elementAt(0));
        assertTrue(booleanConstant.isEmpty());
        assertTrue(booleanConstant.isConstant());

        tokenizer = new StreamTokenizer(new StringReader("false")); // valid boolean constant
        JSTerm anotherBooleanConstant = new JSTerm(tokenizer);
        assertFalse(anotherBooleanConstant.isVariable());
        assertFalse(anotherBooleanConstant.isConstant());
        assertFalse(anotherBooleanConstant.isFunction());
        assertFalse(anotherBooleanConstant.isEval());
        assertEquals("false", anotherBooleanConstant.elementAt(0));
        assertTrue(anotherBooleanConstant.isEmpty());
        assertTrue(anotherBooleanConstant.isConstant());
    }

    @Test
    public void testJSTermFunctionConstruction() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function
        JSTerm function = new JSTerm(tokenizer);
        assertTrue(function.isFunction());
        assertEquals("+(x y)", function.elementAt(0));
        assertTrue(function.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(* x y)")); // valid function
        JSTerm anotherFunction = new JSTerm(tokenizer);
        assertTrue(anotherFunction.isFunction());
        assertEquals("(* x y)", anotherFunction.elementAt(0));
        assertTrue(anotherFunction.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(- x y)")); // valid function
        JSTerm thirdFunction = new JSTerm(tokenizer);
        assertTrue(thirdFunction.isFunction());
        assertEquals("(- x y)", thirdFunction.elementAt(0));
        assertTrue(thirdFunction.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(/ x y)")); // valid function
        JSTerm fourthFunction = new JSTerm(tokenizer);
        assertTrue(fourthFunction.isFunction());
        assertEquals("/(x y)", fourthFunction.elementAt(0));
        assertTrue(fourthFunction.isEmpty());
    }

    @Test(expected = JSParserError.class)
    public void testJSTermInvalidConstruction() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Term")); // missing left parenthesis
        JSTerm term = new JSTerm(tokenizer);
        fail("Expected JSParserError");
    }

    @Test(expected = JSParserError.class)
    public void testJSTermInvalidFunctionName() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(InvalidFunction x)")); // invalid function name
        JSTerm term = new JSTerm(tokenizer);
        fail("Expected JSParserError");
    }

    @Test
    public void testJSTermEvaluable() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function
        JSTerm function = new JSTerm(tokenizer);
        assertTrue(function.isFunction());
        assertFalse(function.isEvaluable());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function with makeEvaluable call
        JSTerm evaluableFunction = new JSTerm(tokenizer);
        evaluableFunction.makeEvaluable(true);
        assertTrue(evaluableFunction.isFunction());
        assertTrue(evaluableFunction.isEvaluable());

        tokenizer = new StreamTokenizer(new StringReader("x")); // valid variable
        JSTerm variable = new JSTerm(tokenizer);
        assertTrue(variable.isVariable());
        assertFalse(variable.isConstant());
        assertFalse(variable.isFunction());
        assertFalse(variable.isEvaluable());

        tokenizer = new StreamTokenizer(new StringReader("3")); // valid constant
        JSTerm constant = new JSTerm(tokenizer);
        assertTrue(constant.isConstant());
        assertFalse(constant.isVariable());
        assertFalse(constant.isFunction());
        assertFalse(constant.isEvaluable());

        tokenizer = new StreamTokenizer(new StringReader("true")); // valid boolean constant
        JSTerm booleanConstant = new JSTerm(tokenizer);
        assertTrue(booleanConstant.isConstant());
        assertFalse(booleanConstant.isVariable());
        assertFalse(booleanConstant.isFunction());
        assertFalse(booleanConstant.isEvaluable());
    }

    @Test
    public void testJSTermIsGround() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x")); // valid variable
        JSTerm variable = new JSTerm(tokenizer);
        assertFalse(variable.isGround());

        tokenizer = new StreamTokenizer(new StringReader("3")); // valid constant
        JSTerm constant = new JSTerm(tokenizer);
        assertTrue(constant.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function
        JSTerm function = new JSTerm(tokenizer);
        assertFalse(function.isGround());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function with ground operands
        JSTerm groundFunction = new JSTerm(tokenizer);
        groundFunction.elementAt(1).makeConstant();
        groundFunction.elementAt(2).makeConstant();
        assertTrue(groundFunction.isGround());
    }

    @Test
    public void testJSTermStandardizerTerm() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x")); // valid variable
        JSTerm variable = new JSTerm(tokenizer);
        JSTerm standardizedVariable = variable.standardizerTerm();
        assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedVariable.elementAt(0));
        assertTrue(standardizedVariable.isVariable());
        assertTrue(standardizedVariable.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function
        JSTerm function = new JSTerm(tokenizer);
        JSTerm standardizedFunction = function.standardizerTerm();
        assertEquals("+(x" + String.valueOf(JSJshopVars.VarCounter) + " x" + String.valueOf(JSJshopVars.VarCounter + 1) + ")", standardizedFunction.elementAt(0));
        assertTrue(standardizedFunction.isFunction());
        assertTrue(standardizedFunction.isEmpty());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y z)")); // valid function with multiple variables
        JSTerm functionWithMultipleVariables = new JSTerm(tokenizer);
        JSTerm standardizedFunctionWithMultipleVariables = functionWithMultipleVariables.standardizerTerm();
        assertEquals("+(x" + String.valueOf(JSJshopVars.VarCounter) + " x" + String.valueOf(JSJshopVars.VarCounter + 1) + " x" + String.valueOf(JSJshopVars.VarCounter + 2) + ")", standardizedFunctionWithMultipleVariables.elementAt(0));
        assertTrue(standardizedFunctionWithMultipleVariables.isFunction());
        assertTrue(standardizedFunctionWithMultipleVariables.isEmpty());
    }

    @Test
    public void testJSTermCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x y)")); // valid function
        JSTerm function = new JSTerm(tokenizer);
        JSTerm functionCall = function.call();
        assertEquals("+(x y)", functionCall.toStr().toString());

        tokenizer = new StreamTokenizer(new StringReader("(+ x y z)")); // valid function with multiple arguments
        JSTerm functionWithMultipleArguments = new JSTerm(tokenizer);
        JSTerm functionCallWithMultipleArguments = functionWithMultipleArguments.call();
        assertEquals("+(x y z)", functionCallWithMultipleArguments.toStr().toString());

        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(not x)")); // valid unary operator
        JSTerm unaryOperator = new JSTerm(tokenizer2);
        JSTerm unaryOperatorCall = unaryOperator.call();
        assertEquals("not(x)", unaryOperatorCall.toStr().toString());

        StreamTokenizer tokenizer3 = new StreamTokenizer(new StringReader("(* x 2)")); // valid binary operator
        JSTerm binaryOperator = new JSTerm(tokenizer3);
        JSTerm binaryOperatorCall = binaryOperator.call();
        assertEquals("*(x 2)", binaryOperatorCall.toStr().toString());
    }
}