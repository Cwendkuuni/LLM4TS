package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilationResult;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
    @Test
    public void testScopedAliases() {
        AbstractCompiler compiler = new AbstractCompiler() {};
        AbstractCompiler.ProcessingPhase phase = AbstractCompiler.ProcessingPhase.PARSE_AND_TYPE;
        CompilerOptions options = new CompilerOptions();
        options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
        HotSwapCompilerPass compilerPass = new ScopedAliases(compiler, null, transformationHandler);

        String inputCode = "goog.scope(function() {\n" +
                "  var dom = goog.dom;\n" +
                "  var DIV = dom.TagName.DIV;\n" +
                "  dom.createElement(DIV);\n" +
                "});";

        CompilationResult result = compiler.compile(inputCode, phase, options);

        Traversal traversal = new Traversal();
        Node root = result.getRootNode();
        NodeTraversal.traverse(compiler, root, traversal);

        List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
        List<AliasUsage> aliasUsages = traversal.getAliasUsages();
        List<Node> scopeCalls = traversal.getScopeCalls();

        // Check the number of alias definitions, usages and scope calls.
        assertEquals(3, aliasDefinitionsInOrder.size());
        assertEquals(3, aliasUsages.size());
        assertEquals(1, scopeCalls.size());

        // Check the first alias definition.
        Node aliasDefinition1 = aliasDefinitionsInOrder.get(0);
        assertEquals("goog.dom", aliasDefinition1.getString());
        Node aliasDefinition1Parent = aliasDefinition1.getParent();
        assertEquals("var", aliasDefinition1Parent.getString());
        assertEquals("dom", aliasDefinition1.getFirstChild().getString());

        // Check the first alias usage.
        AliasUsage aliasUsage1 = aliasUsages.get(0);
        assertEquals("dom", aliasUsage1.aliasReference.getString());
        Node aliasDefinition1AliasedNode = ((AliasedNode) aliasUsage1).aliasDefinition;
        assertEquals("goog.dom", aliasDefinition1AliasedNode.getString());

        // Check the first scope call.
        Node scopeCall = scopeCalls.get(0);
        Node scopeCallParent = scopeCall.getParent();
        assertEquals("function() {...}", scopeCallParent.getString());
        assertEquals("goog.scope", scopeCall.getFirstChild().getString());

        // Check if there are no errors.
        assertFalse(traversal.hasErrors());
    }

    @Test
    public void testScopedAliasesErrors() {
        AbstractCompiler compiler = new AbstractCompiler() {};
        AbstractCompiler.ProcessingPhase phase = AbstractCompiler.ProcessingPhase.PARSE_AND_TYPE;
        CompilerOptions options = new CompilerOptions();
        options.setAliasTransformation(AliasTransformation.GOOGLE_SCOPE);
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};
        HotSwapCompilerPass compilerPass = new ScopedAliases(compiler, null, transformationHandler);

        String inputCode = "goog.scope(function() {\n" +
                "  var x = goog.x;\n" + // GOOG_SCOPE_ALIAS_REDEFINED
                "  var y = goog.y;\n" + // GOOG_SCOPE_ALIAS_REDEFINED
                "  goog.scope(function() {\n" + // GOOG_SCOPE_USED_IMPROPERLY
                "    var z = goog.z;\n" + // GOOG_SCOPE_ALIAS_REDEFINED
                "  });\n" +
                "});\n";

        CompilationResult result = compiler.compile(inputCode, phase, options);

        Traversal traversal = new Traversal();
        Node root = result.getRootNode();
        NodeTraversal.traverse(compiler, root, traversal);

        // Check the number of alias definitions, usages and scope calls.
        assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
        assertEquals(0, traversal.getAliasUsages().size());
        assertEquals(1, traversal.getScopeCalls().size());

        // Check if there are errors.
        assertTrue(traversal.hasErrors());

        List<Node> errors = result.getErrors();
        assertEquals(4, errors.size());

        Node firstError = errors.get(0);
        assertEquals(DiagnosticType.ERROR, firstError.getType());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", firstError.getMessage());
        assertEquals("x", firstError.getJscMessage());

        Node secondError = errors.get(1);
        assertEquals(DiagnosticType.ERROR, secondError.getType());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", secondError.getMessage());
        assertEquals("y", secondError.getJscMessage());

        Node thirdError = errors.get(2);
        assertEquals(DiagnosticType.ERROR, thirdError.getType());
        assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", thirdError.getMessage());

        Node fourthError = errors.get(3);
        assertEquals(DiagnosticType.ERROR, fourthError.getType());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", fourthError.getMessage());
        assertEquals("z", fourthError.getJscMessage());
    }
}