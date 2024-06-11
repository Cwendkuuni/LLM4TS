package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.ErrorMessage;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition.DiagnosticType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.putAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String file, SourcePosition position) {
        return new AliasTransformation() {
          @Override
          public List<String> getAliases() {
            return Lists.newArrayList("goog.dom", "DIV");
          }
        };
      }
    });

    AbstractCompiler compiler = new AbstractCompiler(options);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    // Test empty script
    Node root = Node.newProgram();
    scopedAliases.process(null, root);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());

    // Test valid script
    String script =
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";
    Node root2 = Node.fromSource(script);
    scopedAliases.process(null, root2);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals("goog.dom", scopedAliases.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("DIV", scopedAliases.getAliasUsages().get(0).getAliasDefinition().getString());

    // Test invalid script - goog.scope used improperly
    script = "goog.scope(function() {\n" +
        "  goog.scope('function() { var x = 1; }');\n" +
        "});";
    Node root3 = Node.fromSource(script);
    scopedAliases.process(null, root3);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must be alone in a single statement.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - goog.scope has bad parameters
    script = "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  });\n" +
        "});";
    Node root4 = Node.fromSource(script);
    scopedAliases.process(null, root4);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter. " +
            "It must be an anonymous function that itself takes no parameters.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - goog.scope references this
    script = "goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "});";
    Node root5 = Node.fromSource(script);
    scopedAliases.process(null, root5);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS: The body of a goog.scope function cannot reference 'this'.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - goog.scope uses return
    script = "goog.scope(function() {\n" +
        "  return 1;\n" +
        "});";
    Node root6 = Node.fromSource(script);
    scopedAliases.process(null, root6);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN: The body of a goog.scope function cannot use 'return'.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - goog.scope uses throw
    script = "goog.scope(function() {\n" +
        "  throw new Error('test');\n" +
        "});";
    Node root7 = Node.fromSource(script);
    scopedAliases.process(null, root7);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW: The body of a goog.scope function cannot use 'throw'.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - alias redefined
    script = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "  var x = 2;\n" +
        "});";
    Node root8 = Node.fromSource(script);
    scopedAliases.process(null, root8);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias 'x' is assigned a value more than once.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test invalid script - non-alias local
    script = "goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "});";
    Node root9 = Node.fromSource(script);
    scopedAliases.process(null, root9);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL: The local variable 'x' is in a goog.scope and is not an alias.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test valid script with type nodes
    script = "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      TagName: {\n" +
        "        DIV: function() {}\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  goog.dom.TagName.DIV();\n" +
        "});";
    Node root10 = Node.fromSource(script);
    scopedAliases.process(null, root10);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals("goog.dom", scopedAliases.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("DIV", scopedAliases.getAliasUsages().get(0).getAliasDefinition().getString());
    assertEquals("DIV", scopedAliases.getAliasUsages().get(0).applyAlias().getString());

    // Test valid script with errors
    script = "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      TagName: {\n" +
        "        DIV: function() {}\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  });\n" +
        "  goog.dom.TagName.DIV();\n" +
        "});";
    Node root11 = Node.fromSource(script);
    scopedAliases.process(null, root11);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter. " +
            "It must be an anonymous function that itself takes no parameters.",
        scopedAliases.getDiagnostics().get(0).getMessage());

    // Test valid script with errors and type nodes
    script = "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      TagName: {\n" +
        "        DIV: function() {}\n" +
        "      }\n" +
        "    }\n" +
        "  };\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  });\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  DIV();\n" +
        "});";
    Node root12 = Node.fromSource(script);
    scopedAliases.process(null, root12);
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(2, scopedAliases.getDiagnostics().size());
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter. " +
            "It must be an anonymous function that itself takes no parameters.",
        scopedAliases.getDiagnostics().get(0).getMessage());
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL: The local variable 'DIV' is in a goog.scope and is not an alias.",
        scopedAliases.getDiagnostics().get(1).getMessage());
  }
}