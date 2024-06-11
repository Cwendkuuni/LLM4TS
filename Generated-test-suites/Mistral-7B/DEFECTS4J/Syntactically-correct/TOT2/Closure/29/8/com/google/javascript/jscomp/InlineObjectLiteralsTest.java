import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @ org.junit.Test
  public void testInlineObjectLiterals() {
    // Given: A simple object literal
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: {d: 3}" +
        "  };" +
        "  return obj.c.d;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node externs = NodeUtil.parseExterns(Collections.emptyList());
    Node root = NodeUtil.parse(sourceCode);

    // When: Running the InlineObjectLiterals compiler pass
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "inline_var_");
    inlineObjectLiterals.process(externs, root);

    // Then: The object literal should have been inlined
    NodeTraversal traversal = new NodeTraversal(root);
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, inlineObjectLiterals.new InliningBehavior()) {
      @Override
      public void afterReferenceCollected(Var var, Reference ref) {
        if (var.isGlobal()) {
          fail("The InlineObjectLiterals pass should not inline global variables.");
        }
        if (var.isExtern()) {
          fail("The InlineObjectLiterals pass should not inline extern variables.");
        }
        if (ref.isLvalue()) {
          fail("The InlineObjectLiterals pass should not inline assign statements.");
        }
      }
    };

    inlineObjectLiterals.new InliningBehavior().process(externs, root, callback);

    // Assert: All object literals have been inlined and assigned to individual variables
    for (Map.Entry<String, Node> entry : inlineObjectLiterals.new InliningBehavior().getVarmap().entrySet()) {
      String varName = entry.getKey();
      String expectedVarName = "inline_var_" + varName + "_";
      Node varNode = traversal.getTopLevelScope().getVar(varName);
      Node varValueNode = varNode.getFirstChild();
      Node inlinedNode = traversal.getTopLevelScope().getVar(expectedVarName).getFirstChild();
      assertEquals("The variable '" + varName + "' should have been assigned to the variable '" + expectedVarName + "'.", inlinedNode, varValueNode);
    }

    // Assert: The original object literal reference has been replaced by the assigned variables
    Node expectedObjectLiteral = NodeUtil.newObjectLiteralNode(ImmutableMap.of("a", NodeUtil.newNumberNode(1), "b", NodeUtil.newNumberNode(2), "c", NodeUtil.newObjectLiteralNode(ImmutableMap.of("d", NodeUtil.newNumberNode(3)))));
    Node objectLiteralNode = root.getFirstChild();
    assertFalse("The original object literal should have been replaced.", objectLiteralNode.isObjectLit());
    Node objectLiteralReference = objectLiteralNode;
    while (objectLiteralReference.getParent().isGetProp()) {
      objectLiteralReference = objectLiteralReference.getParent();
    }
    Node varReference = null;
    for (Var var : traversal.getTopLevelScope().getVars()) {
      if (var.name.startsWith("inline_var_")) {
        Node varNode = var.getFirstChild();
        if (varNode.isName() && varNode.getString().equals("c")) {
          varReference = traversal.getTopLevelScope().getVar(var.name).getFirstChild();
          break;
        }
      }
    }
    Node actualObjectLiteralReference = varReference;
    while (actualObjectLiteralReference.isGetProp()) {
      actualObjectLiteralReference = actualObjectLiteralReference.getParent();
    }
    Node actualObjectLiteral = actualObjectLiteralReference.getFirstChild();
    assertTrue("The object literal reference should have been replaced by the assigned variables.", expectedObjectLiteral.equals(actualObjectLiteral));

    // Edge cases and exceptions
    // Given: An object literal with a self-referential property
    sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: obj.a," +
        "      d: 2" +
        "    }" +
        "  };" +
        "  return obj.b.d;" +
        "}";

    // When: Running the InlineObjectLiterals compiler pass
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "inline_var_");
    inlineObjectLiterals.process(externs, NodeUtil.parse(sourceCode));

    // Then: The self-referential property should not have been inlined
    Node selfReferentialObjectLiteralNode = traversal.getTopLevelScope().getVar("obj").getFirstChild();
    Node selfReferentialObjectLiteral = selfReferentialObjectLiteralNode;
    while (selfReferentialObjectLiteral.isGetProp()) {
      selfReferentialObjectLiteral = selfReferentialObjectLiteral.getParent();
    }
    assertFalse("The self-referential property should not have been inlined.", selfReferentialObjectLiteral.isObjectLit());

    // Given: An object literal with an initializer that is not a variable
    sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {init: function() {return 2;}}" +
        "  };" +
        "  return obj.b.init();" +
        "}";

    // When: Running the InlineObjectLiterals compiler pass
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "inline_var_");
    inlineObjectLiterals.process(externs, NodeUtil.parse(sourceCode));

    // Then: The initializer should not have been inlined
    Node initializerObjectLiteralNode = traversal.getTopLevelScope().getVar("obj").getFirstChild();
    Node initializerObjectLiteral = initializerObjectLiteralNode;
    while (initializerObjectLiteral.isGetProp()) {
      initializerObjectLiteral = initializerObjectLiteral.getParent();
    }
    assertFalse("The initializer should not have been inlined.", initializerObjectLiteral.isObjectLit());

    // Given: An object literal with a property with the same name as a variable
    sourceCode = "var a = 1;" +
        "function foo() {" +
        "  var obj = {" +
        "    a: 2," +
        "  };" +
        "  return obj.a;" +
        "}";

    // When: Running the InlineObjectLiterals compiler pass
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "inline_var_");
    inlineObjectLiterals.process(externs, NodeUtil.parse(sourceCode));

    // Then: The property should have been assigned to a different variable
    Node objectLiteralNodeWithPropertyA = traversal.getTopLevelScope().getVar("obj").getFirstChild();
    Node varNodeWithPropertyA = traversal.getTopLevelScope().getVar("a");
    Node propertyA = varNodeWithPropertyA.getFirstChild();
    Node expectedVarNameForPropertyA = "inline_var_a_";
    Node expectedPropertyANode = NodeUtil.newNameNode(expectedVarNameForPropertyA + "_");
    Node actualPropertyANode = objectLiteralNodeWithPropertyA.getFirstChild();
    while (actualPropertyANode.isGetProp()) {
      actualPropertyANode = actualPropertyANode.getParent();
    }
    assertEquals("The property 'a' should have been assigned to a different variable.", expectedPropertyANode, actualPropertyANode);

    // Given: An object literal with a property with the same name as a function
    sourceCode = "function a() {}" +
        "function foo() {" +
        "  var obj = {" +
        "    a: 2," +
        "  };" +
        "  return obj.a;" +
        "}";

    // When: Running the InlineObjectLiterals compiler pass
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "inline_var_");
    inlineObjectLiterals.process(externs, NodeUtil.parse(sourceCode));

    // Then: The property should have been assigned to a different variable
    Node objectLiteralNodeWithPropertyA = traversal.getTopLevelScope().getVar("obj").getFirstChild();
    Node varNodeWithFunctionA = traversal.getTopLevelScope().getVar("a");
    Node propertyA = objectLiteralNodeWithPropertyA.getFirstChild();
    Node expectedVarNameForPropertyA = "inline_var_a_func_";
    Node expectedPropertyANode = NodeUtil.newNameNode(expectedVarNameForPropertyA + "_");
    Node actualPropertyANode = propertyA;
    while (actualPropertyANode.isGetProp()) {
      actualPropertyANode = actualPropertyANode.getParent();
    }
    assertEquals("The property 'a' should have been assigned to a different variable.", expectedPropertyANode, actualPropertyANode);
  }
}