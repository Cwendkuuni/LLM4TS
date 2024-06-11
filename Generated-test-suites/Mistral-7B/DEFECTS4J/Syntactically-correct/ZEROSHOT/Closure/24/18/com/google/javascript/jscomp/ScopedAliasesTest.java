package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest extends JSTestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(ScopedAliasesTest.class);
  }

  @Override
  protected Compiler getCompiler() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALLOW_GOOG_SCOPE_ALIASES);
    return new Compiler(options);
  }

  public void testScopedAliases() {
    String sourceCode = "" +
        "package test;\n" +
        "\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "\n" +
        "  dom.createElement(DIV);\n" +
        "});\n";

    SourceFile sourceFile = new SourceFile("Test.js", sourceCode);
    Node rootNode = getCompiler().parse(sourceFile, null);

    Traversal traversal = new Traversal();
    getCompiler().process(rootNode, null);

    assertTrue(traversal.hasErrors());

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("DIV", aliasDefinitionsInOrder.get(1).getString());

    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      AliasedNode aliasNode = (AliasedNode) aliasUsage;
      Node aliasDefinition = aliasNode.getAliasDefinition();
      assertEquals("goog.dom", aliasDefinition.getString());
      assertEquals("dom", aliasNode.getAliasReference().getString());
    }

    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node anonymousFnNode = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFnNode.getChildCount());
    Node functionBody = anonymousFnNode.getChildAtIndex(0);
    assertEquals(0, functionBody.getChildCount());

    assertFalse(traversal.hasErrors());
  }
}