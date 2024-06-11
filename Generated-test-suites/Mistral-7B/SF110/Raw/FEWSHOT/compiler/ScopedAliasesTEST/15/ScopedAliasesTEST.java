package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Scope.Var.Initialization;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.apache.commons.cli.ScopedAliases;
import org.apache.commons.cli.ScopedAliases.Traversal.AliasUsage;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * Test cases for the ScopedAliases class
 */
public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        // Prepare mock objects
        CompilerOptions compilerOptions = mock(CompilerOptions.class);
       when(compilerOptions.getOptions().getAliasTransformations())
                .thenReturn(Sets.newHashSet(AliasTransformation.GOOGLE_SCOPE));

        AliasTransformationHandler transformationHandler = mock(
                AliasTransformationHandler.class);
        when(transformationHandler.logAliasTransformation(
                anyString(), any(SourcePosition.class)))
                .thenAnswer(new Answer<AliasTransformation>() {
                    @Override
                    public AliasTransformation answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        String fileName = (String) args[0];
                        SourcePosition position = (SourcePosition) args[1];
                        return new AliasTransformation(fileName, position, null);
                    }
                });

        AbstractCompiler compiler = mock(AbstractCompiler.class);
        PreprocessorSymbolTable preprocessorSymbolTable = mock(
                PreprocessorSymbolTable.class);

        ScopedAliases scopedAliases = new ScopedAliases(compiler,
                preprocessorSymbolTable, transformationHandler);

        // Test empty root node
        Node root = Node.nullLiteral();
        scopedAliases.process(null, root);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());

        // Test valid goog.scope block
        Node rootWithScope = createNode("function() { " +
                "var goog = goog || {}; " +
                "goog.foo = goog.foo || {}; " +
                "goog.foo.bar = function() {}; " +
                "goog.scope(function() { " +
                "  var baz = goog.foo.bar; " +
                "}); " +
                "}");
        scopedAliases.process(null, rootWithScope);
        assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals("goog.foo.bar",
                scopedAliases.getAliasDefinitionsInOrder().get(0).getName());
        assertEquals(1, scopedAliases.getAliasUsages().size());
        assertEquals(new AliasedNode(
                scopedAliases.getAliasUsages().get(0).getNode(),
                Node.name("goog.foo.bar")),
                scopedAliases.getAliasUsages().get(0));
        assertEquals(1, scopedAliases.getScopeCalls().size());

        // Test invalid goog.scope block (missing anonymous function)
        Node rootWithMissingFunction = createNode("function() { " +
                "var goog = goog || {}; " +
                "goog.scope(");
        scopedAliases.process(null, rootWithMissingFunction);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());
        assertEquals(1, scopedAliases.getErrors().size());
        assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
                scopedAliases.getErrors().get(0).getType().getName());

        // Test invalid goog.scope block (non-anonymous function)
        Node rootWithNonAnonymousFunction = createNode("function() { " +
                "var goog = goog || {}; " +
                "goog.scope(function foo() {})");
        scopedAliases.process(null, rootWithNonAnonymousFunction);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());
        assertEquals(1, scopedAliases.getErrors().size());
        assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
                scopedAliases.getErrors().get(0).getType().getName());

        // Test invalid goog.scope block (function with parameters)
        Node rootWithFunctionWithParameters = createNode("function() { " +
                "var goog = goog || {}; " +
                "goog.scope(function foo(x, y) {})");
        scopedAliases.process(null, rootWithFunctionWithParameters);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());
        assertEquals(1, scopedAliases.getErrors().size());
        assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
                scopedAliases.getErrors().get(0).getType().getName());

        // Test invalid goog.scope block (function with return statement)
        Node rootWithFunctionWithReturn = createNode("function() { " +
                "var goog = goog || {}; " +
                "goog.scope(function foo() { return 1; })");
        scopedAliases.process(null, rootWithFunctionWithReturn);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());
        assertEquals(1, scopedAliases.getErrors().size());
        assertEquals("JSC_GOOG_SCOPE_USES_RETURN",
                scopedAliases.getErrors().get(0).getType().getName());

        // Test invalid goog.scope block (function with throw statement)
        Node rootWithFunctionWithThrow = createNode("function() { " +
                "var goog = goog || {}; " +
                "try { goog.scope(function foo() { throw new Error(); })") ;
        scopedAliases.process(null, rootWithFunctionWithThrow);
        assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
        assertEquals(0, scopedAliases.getAliasUsages().size());
        assertEquals(0, scopedAliases.getScopeCalls().size());
        assertEquals(1, scopedAliases.getErrors().size());
        assertEquals("JSC_GOOG_SCOPE_USES_THROW",
                scopedAliases.getErrors().get(0).getType().getName());
    }

    private Node createNode(String code) {
        Node root = new Node(code);
        Parser parser = new Parser();
        parser.parse(root, null, null, null);
        return root;
    }
}