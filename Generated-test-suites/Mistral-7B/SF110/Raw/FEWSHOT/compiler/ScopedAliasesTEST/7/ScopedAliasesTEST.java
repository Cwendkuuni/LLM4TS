package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        // Test case for valid usage of ScopedAliases
        String sourceCode = "goog.scope(function() {\n" +
                "  var x = goog.foo;\n" +
                "  goog.scope(function() {\n" +
                "    var y = goog.foo;\n" +
                "    var z = goog.bar;\n" +
                "  });\n" +
                "  var a = goog.bar;\n" +
                "});";

        AbstractCompiler compiler = new Compiler();
        PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
        AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        Node root = new Parser(new SourceFile("Test.java", sourceCode)).parse();
        scopedAliases.process(null, root);

        List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
        List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
        List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

        assertEquals(3, aliasDefinitionsInOrder.size());
        assertEquals(2, scopeCalls.size());
        assertEquals(3, aliasUsages.size());

        Node xNode = aliasDefinitionsInOrder.get(0);
        Node yNode = aliasDefinitionsInOrder.get(1);
        Node zNode = aliasDefinitionsInOrder.get(2);
        Node fooNode = xNode.getParent().getLastChild();

        assertEquals("foo", fooNode.getString());
        assertEquals("goog.foo", xNode.getQualifiedName());
        assertEquals("goog.foo", yNode.getQualifiedName());

        Node googScopeNode = scopeCalls.get(0);
        Node innerGoogScopeNode = scopeCalls.get(1);

        assertEquals("goog.scope", googScopeNode.getFirstChild().getString());
        assertEquals(1, googScopeNode.getChildCount());
        assertEquals(2, innerGoogScopeNode.getChildCount());
        assertEquals("function() {}", innerGoogScopeNode.getLastChild().getString());

        AliasUsage aliasUsage1 = aliasUsages.get(0);
        AliasUsage aliasUsage2 = aliasUsages.get(1);
        AliasUsage aliasUsage3 = aliasUsages.get(2);

        assertEquals(xNode, aliasUsage1.aliasReference);
        assertEquals(fooNode, aliasUsage1.aliasDefinition);

        assertEquals(yNode, aliasUsage2.aliasReference);
        assertEquals(fooNode, aliasUsage2.aliasDefinition);

        assertEquals(a, aliasUsage3.aliasReference);
        assertEquals(zNode, aliasUsage3.aliasDefinition);
    }

    @Test
    public void testScopedAliasesImproperUsage() {
        // Test case for improper usage of ScopedAliases
        String sourceCode = "goog.scope(function() {\n" +
                "  var x = goog.foo;\n" +
                "  goog.scope(function() {\n" +
                "    var y = goog.foo;\n" +
                "    goog.scope(function() {});\n" +
                "  });\n" +
                "});";

        AbstractCompiler compiler = new Compiler();
        PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
        AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        Node root = new Parser(new SourceFile("Test.java", sourceCode)).parse();
        scopedAliases.process(null, root);

        List<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
        List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
        List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

        assertEquals(2, aliasDefinitionsInOrder.size());
        assertEquals(2, scopeCalls.size());
        assertEquals(2, aliasUsages.size());

        Node xNode = aliasDefinitionsInOrder.get(0);
        Node yNode = aliasDefinitionsInOrder.get(1);
        Node fooNode = xNode.getParent().getLastChild();

        assertEquals("foo", fooNode.getString());
        assertEquals("goog.foo", xNode.getQualifiedName());
        assertEquals("goog.foo", yNode.getQualifiedName());

        Node googScopeNode1 = scopeCalls.get(0);
        Node googScopeNode2 = scopeCalls.get(1);

        assertEquals("goog.scope", googScopeNode1.getFirstChild().getString());
        assertEquals(1, googScopeNode1.getChildCount());
        assertEquals("function() {}", googScopeNode1.getLastChild().getString());

        assertEquals("goog.scope", googScopeNode2.getFirstChild().getString());
        assertEquals(1, googScopeNode2.getChildCount());
        assertTrue(googScopeNode2.getLastChild().isNull());

        AliasUsage aliasUsage1 = aliasUsages.get(0);
        AliasUsage aliasUsage2 = aliasUsages.get(1);

        assertEquals(xNode, aliasUsage1.aliasReference);
        assertEquals(fooNode, aliasUsage1.aliasDefinition);

        assertEquals(yNode, aliasUsage2.aliasReference);
        assertEquals(fooNode, aliasUsage2.aliasDefinition);

        // Check for improper usage error
        assertTrue(scopedAliases.getTraversal().hasErrors());
        assertEquals(1, scopedAliases.getTraversal().getErrorCount());
        assertEquals(
                "JSC_GOOG_SCOPE_USED_IMPROPERLY",
                scopedAliases.getTraversal().getErrors().get(0).getType().getName());
    }
}