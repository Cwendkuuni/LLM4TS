package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        AbstractCompiler compiler = new TestCompiler();
        AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
        ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

        Node root = createTestNode("test.js");
        scopedAliases.process(null, root);

        // No errors reported
        verifyNoErrors(compilationResult(compiler));

        // Check that alias definitions were processed in the correct order
        List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
        Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
        Node googAliasDefinition = aliasDefinitionsInOrder.get(1);
        Node DIVAliasDefinition = aliasDefinitionsInOrder.get(2);

        assertSame(root.getChildAtIndex(0), domAliasDefinition);
        assertSame(root.getChildAtIndex(1), googAliasDefinition);
        assertSame(root.getChildAtIndex(2), DIVAliasDefinition);

        // Check that alias usages were applied correctly
        List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
        Node domElementCreateCall = aliasUsages.get(0).getAliasedNode();
        Node googDomCall = aliasUsages.get(1).getAliasedNode();

        assertSame(domElementCreateCall.getFirstChild().getLastChild(), DIVAliasDefinition);
        assertSame(googDomCall.getFirstChild().getLastChild(), googAliasDefinition);

        // Check that goog.scope calls were removed
        List<Node> scopeCalls = scopedAliases.getScopeCalls();
        assertTrue(scopeCalls.isEmpty());

        // Check that alias definitions were removed
        assertTrue(root.getChildAtIndex(0).getParent().equals(root));
        assertTrue(root.getChildAtIndex(1).getParent().equals(root));
        assertTrue(root.getChildAtIndex(2).getParent().equals(root));

        // Check that scopes were collapsed
        Node googScopeCall = findNodeWithName(root, "goog.scope");
        assertNull(googScopeCall);

        Node testScopeCall = findNodeWithName(root, "testScope");
        Node testScopeBlock = testScopeCall.getLastChild();
        Node testScopeVariable = testScopeBlock.getFirstChild();
        Node testScopeVariableValue = testScopeVariable.getFirstChild();

        Node testVariableDefinition = findNodeWithName(root, "testVariable");
        Node testVariableInitialValue = testVariableDefinition.getFirstChild();
        Node testVariableValue = testVariableInitialValue.getLastChild();

        Node testScopeVariableAlias = testScopeVariable.getLastChild();
        Node testVariableAlias = testVariableDefinition.getLastChild();

        assertSame(testScopeBlock.getParent(), testScopeCall.getParent());
        assertSame(testScopeCall.getParent(), testVariableDefinition.getParent());
        assertSame(testVariableValue.getParent(), testVariableAlias.getParent());
        assertSame(testVariableAlias.getParent(), testVariableDefinition.getParent());
        assertSame(testVariableValue, testScopeVariableValue);
    }

    private static Node createTestNode(String fileName) {
        String testCode = "goog.scope(function() {\n" +
                "  var dom = goog.dom;\n" +
                "  var goog = this;\n" +
                "  var DIV = dom.TagName.DIV;\n" +
                "  var testVariable = goog.x;\n" +
                "  testVariable = goog.dom.createElement(DIV);\n" +
                "  var testScope = (function() {\n" +
                "    var testVariable = 0;\n" +
                "    return {\n" +
                "      testVariable: testVariable\n" +
                "    };\n" +
                "  })();\n" +
                "})\n" +
                "testScope.testVariable = 1;\n" +
                "goog.scope(function() {\n" +
                "  var testVariable = testScope.testVariable;\n" +
                "});\n";

        Node root = new Node(Node.SCRIPT_NODE, null, null, null, null, null, null, null, null);
        Node script = new Node(Node.CALL_EXPR_NODE, null, null, null, null, null, null, null, null);
        Node scriptName = new Node(Node.IDENT_NODE, null, fileName, null, null, null, null, null, null);
        script.setSourcePosition(new SourcePosition<AliasTransformation>() {});
        script.setFirstChild(scriptName);
        root.setFirstChild(script);

        Node jsCompiler = new Node(Node.IDENT_NODE, null, "JSCompiler", null, null, null, null, null, null);
        Node compile = new Node(Node.CALL_EXPR_NODE, null, null, null, null, null, null, null, null);
        compile.setFirstChild(jsCompiler);
        compile.setLastChild(script);
        root.setLastChild(compile);

        Node testCodeNode = new Node(Node.CALL_EXPR_NODE, null, null, null, null, null, null, null, null);
        testCodeNode.setSourcePosition(new SourcePosition<AliasTransformation>() {});
        testCodeNode.setFirstChild(new Node(Node.IDENT_NODE, null, "goog.load", null, null, null, null, null, null));
        testCodeNode.setLastChild(new Node(Node.STRING_LITERAL_NODE, null, testCode, null, null, null, null, null, null));
        script.getLastChild().setFirstChild(testCodeNode);

        return root;
    }

    private static Node findNodeWithName(Node node, String name) {
        for (Node current = node; current != null; current = current.getNext()) {
            if (current.isCall() && current.getFirstChild().isQualifiedName() &&
                    current.getFirstChild().getString().equals(name)) {
                return current;
            }
        }
        return null;
    }

    private static void verifyNoErrors(List<DiagnosticType> errors) {
        assertTrue(errors.isEmpty());
    }
}

class TestCompiler extends AbstractCompiler {
    @Override
    public void report(NodeTraversal t, Node node, DiagnosticType error, String... arguments) {
        throw new RuntimeException("Unexpected error: " + error + " " + String.join(" ", arguments));
    }
}

interface AliasTransformationHandler {
    AliasTransformation logAliasTransformation(String fileName, SourcePosition position);
}