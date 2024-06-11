package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }

    @Test
    public void testCheckGlobalThisConstructor() {
        // No specific tests for the constructor
    }

    @Test
    public void testShouldTraverseFunctionWithoutThisAnnotation() {
        NodeTraversal traversal = MockNodeTraversal.create();
        Node functionNode = MockNode.newFunction("functionWithoutThisAnnotation() {}");
        Node parentNode = MockNode.newAssign("assignNode", null, functionNode);

        assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
    }

    @Test
    public void testShouldTraverseFunctionWithThisAnnotationButNotConstructor() {
        NodeTraversal traversal = MockNodeTraversal.create();
        Node constructorNode = MockNode.newFunction("@constructor functionWithThisAnnotationButNotConstructor() {}");
        Node parentNode = MockNode.newAssign("assignNode", null, constructorNode);

        assertFalse(checkGlobalThis.shouldTraverse(traversal, constructorNode, parentNode));
    }

    @Test
    public void testShouldReportThisAssignmentToPrototypeProperty() {
        NodeTraversal traversal = MockNodeTraversal.create();
        Node assignNode = MockNode.newAssign("assignNode", new Node("prototype"), new Node("this"));

        Node functionNode = new Node("function() { this.prototype.property = 'value'; }");

        Node parentNode = MockNode.newAssign("parentNode", null, functionNode);

        checkGlobalThis.visit(traversal, parentNode, assignNode);

        assertTrue(traversal.hasError());
        DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
        assertEquals(DiagnosticType.warning, diagnosticType);
    }

    @Test
    public void testShouldNotReportThisAssignmentToNonPrototypeObject() {
        NodeTraversal traversal = MockNodeTraversal.create();
        Node assignNode = MockNode.newAssign("assignNode", new Node("nonPrototypeObject"), new Node("this"));

        Node functionNode = new Node("function() { this.nonPrototypeObject = 'value'; }");

        Node parentNode = MockNode.newAssign("parentNode", null, functionNode);

        checkGlobalThis.visit(traversal, parentNode, assignNode);

        assertFalse(traversal.hasError());
    }

    @Test
    public void testReportErrorForGlobalThisKeyword() {
        NodeTraversal traversal = MockNodeTraversal.create();
        Node globalThisNode = new Node("this");

        checkGlobalThis.visit(traversal, null, globalThisNode);

        assertTrue(traversal.hasError());
        DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
        assertEquals(DiagnosticType.warning, diagnosticType);
    }
}