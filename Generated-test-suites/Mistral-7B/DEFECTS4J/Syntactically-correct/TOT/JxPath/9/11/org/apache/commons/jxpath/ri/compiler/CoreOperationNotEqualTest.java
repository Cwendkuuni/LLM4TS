package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ExpressionFactory;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.PathExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompareTest;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest extends CoreOperationCompareTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationNotEqual operation;

    @Before
    public void setUp() throws Exception {
        arg1 = createNumberExpression(1);
        arg2 = createNumberExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testEqualValues() {
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when values are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualValues() {
        EvalContext context = createContext();
        arg1 = createNumberExpression(1);
        arg2 = createNumberExpression(2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when values are not equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualNodes() {
        Document doc = createDocument();
        Node node1 = doc.createProcessingInstruction("xml", "version='1.0'");
        Node node2 = doc.createProcessingInstruction("xml", "version='1.0'");
        arg1 = createNodeInfoExpression(node1);
        arg2 = createNodeInfoExpression(node2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        context.setNode(new NodeInfo(doc.getDocumentElement()));
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when nodes are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualNodes() {
        Document doc = createDocument();
        Node node1 = doc.createProcessingInstruction("xml", "version='1.0'");
        Node node2 = doc.createProcessingInstruction("xml-declaration", "version='1.0'");
        arg1 = createNodeInfoExpression(node1);
        arg2 = createNodeInfoExpression(node2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        context.setNode(new NodeInfo(doc.getDocumentElement()));
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when nodes are not equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqualStrings() {
        arg1 = createStringExpression("hello");
        arg2 = createStringExpression("hello");
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when strings are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testNotEqualStrings() {
        arg1 = createStringExpression("hello");
        arg2 = createStringExpression("world");
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when strings are not equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testNullArg1() {
        arg1 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        try {
            Object result = operation.computeValue(context);
            fail("computeValue should have thrown an exception with a null arg1");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullArg2() {
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when arg2 is null", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareNullArg1AndNullArg2() {
        arg1 = null;
        arg2 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!" + arg1 + " and " + arg2 + " should return true when both are null", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareNullArg1AndNonNullArg2() {
        arg1 = null;
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!" + arg1 + " and " + arg2 + " should return true when arg1 is null", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareNonnullArg1AndNullArg2() {
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!" + arg1 + " and " + arg2 + " should return false when arg2 is null", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareBooleanValues() {
        arg1 = createBooleanExpression(true);
        arg2 = createBooleanExpression(false);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when values are of different booleans", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareBooleanValuesEqual() {
        arg1 = createBooleanExpression(true);
        arg2 = createBooleanExpression(true);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when boolean values are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareDifferentNumberTypes() {
        arg1 = createNumberExpression(1.0);
        arg2 = createNumberExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when different number types", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareStringsOfDifferentLengths() {
        arg1 = createStringExpression("hello");
        arg2 = createStringExpression("helloworld");
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when strings are of different lengths", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareStringsWithMismatchedTypes() {
        arg1 = createNumberExpression(1);
        arg2 = createStringExpression("hello");
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when types are mismatched", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithMismatchedTypes() {
        arg1 = createStringExpression("1");
        arg2 = createNumberExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when types are mismatched", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithSameTypes() {
        arg1 = createNumberExpression(1);
        arg2 = createNumberExpression(2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when types and values are different", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithSameTypesAndEqualValues() {
        arg1 = createNumberExpression(1);
        arg2 = createNumberExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when types and values are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithSameTypesAndEqualValuesAndSamePath() {
        Expression path1 = createPathExpression("/a[1]/b[1]/c[1]");
        Expression path2 = createPathExpression("/a[1]/b[1]/c[1]");
        arg1 = new VariableExpression("$a", path1, new NumberValue(1.0));
        arg2 = new VariableExpression("$a", path2, new NumberValue(1.0));
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when types, values, and paths are equal", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithSameTypesAndEqualValuesAndDifferentPaths() {
        Expression path1 = createPathExpression("/a[1]/b[1]/c[1]");
        Expression path2 = createPathExpression("/a[1]/d[1]/e[1]");
        arg1 = new VariableExpression("$a", path1, new NumberValue(1.0));
        arg2 = new VariableExpression("$a", path2, new NumberValue(1.0));
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertFalse("!=" + arg1 + " and " + arg2 + " should return false when types, values, and paths are equal but paths are different", result.equals(Boolean.TRUE));
    }

    @Test
    public void testCompareExpressionsWithSameTypesAndDifferentValues() {
        arg1 = createNumberExpression(1);
        arg2 = createNumberExpression(2);
        operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = operation.computeValue(context);
        assertTrue("!=" + arg1 + " and " + arg2 + " should return true when types and values are different", result.equals(Boolean.TRUE));
    }
}