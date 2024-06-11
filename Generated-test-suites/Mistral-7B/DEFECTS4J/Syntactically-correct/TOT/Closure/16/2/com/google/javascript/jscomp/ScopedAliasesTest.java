package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.INSTANCE);
    options.setAliasTransformationHandler(new TestAliasTransformationHandler());

    SourceFile sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  static void main(String[] args) {\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  }\n" +
        "}");

    HotSwapCompilerPass pass = new ScopedAliases(compiler, null, options.getAliasTransformationHandler());
    pass.process(null, sourceFile.getRootNode());

    List<Node> aliasDefinitions = pass.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = pass.getAliasUsages();
    List<Node> scopeCalls = pass.getScopeCalls();

    // Ensure that we have exactly one alias definition and one alias usage.
    assertEquals(1, aliasDefinitions.size());
    assertEquals(1, aliasUsages.size());

    // Ensure that the alias usage is replaced with the definition.
    Node aliasDefinition = aliasDefinitions.get(0);
    Node aliasUsage = aliasUsages.get(0);
    assertEquals(aliasDefinition, aliasUsage.getParent().getChildAtIndex(0));

    // Ensure that the alias definition is inlined.
    Node aliasDefinitionParent = aliasDefinition.getParent();
    assertTrue(aliasDefinitionParent.getChildCount() == 1);
    assertFalse(aliasDefinitionParent.isVar());
    Node aliasDefinitionCloned = aliasDefinition.cloneTree();
    assertFalse(aliasDefinitionParent.replaceChild(aliasDefinition, aliasDefinitionCloned));

    // Ensure that the scope call is removed.
    int scopeCallIndex = -1;
    for (int i = 0; i < scopeCalls.size(); ++i) {
      Node scopeCall = scopeCalls.get(i);
      if (scopeCall.getParent().getChildAtIndex(0).isIdentifier("goog.scope")) {
        scopeCallIndex = i;
        break;
      }
    }
    assertEquals(0, scopeCalls.size() - scopeCallIndex);
    assertFalse(scopeCalls.get(0).getParent().hasChild(scopeCalls.get(0)));

    // Ensure that the scoped alias is transformed.
    Node root = sourceFile.getRootNode();
    Node main = root.getChildAtIndex(0);
    Node mainBody = main.getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", mainBody.toString());

    // Ensure that the compiler does not report any errors.
    assertEquals(0, compiler.getNumDiagnostics());
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public AliasTransformation logAliasTransformation(
        String sourceFileName, SourcePosition position) {
      return new AliasTransformation() {
        @Nullable
        @Override
        public String getAliasName() {
          return "goog.dom.TagName.DIV";
        }

        @Override
        public String getAliasExpression() {
          return "DIV";
        }

        @Override
        public boolean isAliasShadow() {
          return false;
        }
      };
    }
  }
}