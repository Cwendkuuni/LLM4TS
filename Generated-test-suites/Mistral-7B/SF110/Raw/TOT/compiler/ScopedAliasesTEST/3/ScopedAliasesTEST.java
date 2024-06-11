package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.Tests.compile;
import static com.google.javascript.rhino.JSDocInfo.Type.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class ScopedAliasesTest extends CompilationTestBase {

  @Test
  public void testScopedAliases() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var dom = goog.dom;\n"
        + "    var DIV = dom.TagName.DIV;\n"
        + "    goog.scope(function() {\n"
        + "      var alias = goog.dom;\n"
        + "      assertTrue(alias === dom);\n"
        + "      assertTrue(goog.dom.createElement(DIV).getNodeType() == Node.ELEMENT_NODE);\n"
        + "    });\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    checkNoErrors(root);

    // Check that MyClass is defined correctly.
    Var myClassVar = node.getChildAtIndex(0).getFirstChild().asVar();
    assertEquals(VAR_FUNCTION, myClassVar.getInitialValue().getType());

    // Check that aliases are applied correctly.
    List<Node> aliasDefinitionsInOrder =
        ((ScopedAliases) compiler.getPassManager().getPass(ScopedAliases.class))
            .getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("dom", aliasDefinitionsInOrder.get(1).getString());

    // Check that aliasUsages are applied correctly.
    List<ScopedAliases.AliasUsage> aliasUsages =
        ((ScopedAliases) compiler.getPassManager().getPass(ScopedAliases.class))
            .getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertEquals(aliasDefinitionsInOrder.get(0), aliasUsages.get(0).aliasDefinition);
    assertEquals(aliasDefinitionsInOrder.get(1), aliasUsages.get(1).aliasDefinition);

    // Check that scopeCalls are correctly removed.
    List<Node> scopeCalls =
        ((ScopedAliases) compiler.getPassManager().getPass(ScopedAliases.class))
            .getScopeCalls();
    assertEquals(2, scopeCalls.size());
    assertEquals(aliasDefinitionsInOrder.get(0), scopeCalls.get(0).getLastChild());
    assertEquals(aliasDefinitionsInOrder.get(1), scopeCalls.get(1).getLastChild());

    // Check that scopes are correctly collapsed.
    Node myClassScopeCall = null;
    for (Node scopeCall : scopeCalls) {
      Node n = scopeCall.getFirstChild();
      if (n.getString().equals("MyClass")) {
        myClassScopeCall = scopeCall;
        break;
      }
    }
    assertNotNull(myClassScopeCall);
    Node myClassScopeBlock = myClassScopeCall.getLastChild();
    assertTrue(myClassScopeBlock.getChildCount() == 0);

    // Check that aliases are expanded correctly in type nodes.
    Node typeNode = root.getFirstChild().getLastChild().getLastChild();
    assertEquals(TYPE_OBJECT, typeNode.getType());
    List<Node> members = typeNode.getFirstChild().asList();
    assertEquals(2, members.size());
    Node myClassVarNode = members.get(0);
    assertEquals(TYPE_FUNCTION, myClassVarNode.getType());
    Node myClassPrototypeNode = members.get(1);
    Node myClassPrototypeMembersNode = myClassPrototypeNode.getFirstChild();
    assertEquals(TYPE_OBJECT, myClassPrototypeMembersNode.getType());
    List<Node> myClassPrototypeMembers = myClassPrototypeMembersNode.asList();
    assertEquals(2, myClassPrototypeMembers.size());
    Node aliasVarNode = myClassPrototypeMembers.get(0);
    assertEquals(TYPE_VAR, aliasVarNode.getType());
    assertEquals("dom", aliasVarNode.getString());
    Node aliasTypeNode = myClassPrototypeMembers.get(1);
    assertEquals(TYPE_OBJECT, aliasTypeNode.getType());
    List<Node> aliasTypeMembers = aliasTypeNode.asList();
    assertEquals(1, aliasTypeMembers.size());
    Node aliasVarTypeNode = aliasTypeMembers.get(0);
    assertEquals(TYPE_OBJECT, aliasVarTypeNode.getType());
    List<Node> aliasVarTypeMembers = aliasVarTypeNode.asList();
    assertEquals(1, aliasVarTypeMembers.size());
    Node aliasTypeNode2 = aliasVarTypeMembers.get(0);
    assertEquals(TYPE_FUNCTION, aliasTypeNode2.getType());
    assertEquals("createElement", aliasTypeNode2.getFirstChild().getString());
  }

  @Test
  public void testImproperUsage() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var dom = goog.dom;\n"
        + "    dimensio = 10;\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY: The call to goog.scope must be alone in a single statement.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testBadParameters() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var x = function() {\n"
        + "    };\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS: The call to goog.scope must take only a single parameter.  It must be an anonymous function that itself takes no parameters.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testThisReference() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    this.x = 1;\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_REFERENCES_THIS: The body of a goog.scope function cannot reference 'this'.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testReturnStatement() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    return 1;\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_USES_RETURN: The body of a goog.scope function cannot use 'return'.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testThrowStatement() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  try {\n"
        + "    goog.scope(function() {\n"
        + "      throw new Error('Test');\n"
        + "    });\n"
        + "  } catch (e) {\n"
        + "    // Do nothing.\n"
        + "  }\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_USES_THROW: The body of a goog.scope function cannot use 'throw'.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testAliasRedefined() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var a = 1;\n"
        + "    goog.scope(function() {\n"
        + "      var a = 2;\n"
        + "    });\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED: The alias 'a' is assigned a value more than once.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testNonAliasLocal() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var x = 1;\n"
        + "  });\n"
        + "}\n";

    Node node = parse(input);

    AbstractCompiler compiler = new Compiler(getTestOptions());
    compiler.process(new SourceFile("Test.java", ""), node);

    Node root = compiler.getCompilationUnit();
    assertEquals(1, getNumErrors(root));
    assertEquals(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL: The local variable 'x' is in a goog.scope and is not an alias.",
        getError(root, 0).getMessage());
  }

  @Test
  public void testAliasTransformation() throws IOException {
    String input = ""
        + "/** @constructor */\n"
        + "function MyClass() {\n"
        + "  goog.scope(function() {\n"
        + "    var goog = {\n"
        + "      dom: {\n"
        + "        TagName: {\n"
        + "          DIV: 42\n"
        + "        }\n"
        + "      }\n"
        + "    };\n"
        + "    goog.scope(function() {\n"
        + "      var dom = goog.dom;\n"
        + "      assertTrue(dom.TagName.DIV === 42);\n"
        + "    });\n"
        + "  });\n"
        + "}\n";

    AliasTransformation aliasTransformation = new AliasTransformation();
    aliasTransformation.setAliasTransformationHandler(new AliasTransformationHandler() {
      @Override
      public void addAlias(String name, String expandedName) {
        System.out.println(String.format("Alias %s expanded to %s", name, expandedName));
      }
    });

    AbstractCompiler compiler = new Compiler(
        getTestOptions().withOptions(AliasTransformation.class, aliasTransformation));
    compiler.process(new SourceFile("Test.java", ""), parse(input));

    Node root = compiler.getCompilationUnit();
    checkNoErrors(root);
  }
}