package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private static AbstractCompiler createCompiler() {
    return new CompilerOptions()
        .withWarningLevel(CompilerOptions.WarningLevel.QUIET)
        .withCompilerPass(new InlineObjectLiterals(
            new NoOpCompiler(),
            () -> NodeUtils.uniqueNameGenerator().generateName()))
        .newCompiler();
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    String jsCode = "var obj = {a: 1, b: {c: 2, d: 3}};\n" +
        "function foo() {\n" +
        "  var x = obj.a;\n" +
        "  var y = obj.b.c;\n" +
        "  var z = obj.b.d;\n" +
        "  return x + y + z;\n" +
        "}";

    Node root = createCompiler().compile(ImmutableList.of(jsCode), null)[0];

    // Check that object literal is inlined correctly
    Node objNode = NodeUtils.findSingleVariableDeclarationByName(root, "obj");
    Node objValueNode = objNode.getFirstChild();
    Node inlineNode = NodeUtils.findSingleStatementByType(objValueNode.getFirstChild(), Node.EXPR_STATEMENT);
    List<Node> vars = NodeUtils.findVariablesByType(inlineNode, Node.VAR_DECLARATION);
    Map<String, Node> expectedVarMap = ImmutableMap.<String, Node>builder()
        .put("a", NodeUtils.findSingleVariableDeclarationByName(root, "a"))
        .put("b_c", NodeUtils.findSingleVariableDeclarationByName(root, "b.c"))
        .put("b_d", NodeUtils.findSingleVariableDeclarationByName(root, "b.d"))
        .build();

    for (Node varNode : vars) {
      String name = NodeUtils.getIdentifierName(varNode.getFirstChild());
      Node expectedValue = expectedVarMap.get(name);
      Node value = varNode.getLastChild();
      assertEquals("Unexpected variable value for variable " + name,
          expectedValue, value);
    }

    // Check that object literal properties are assigned to the corresponding
    // variables
    List<Node> statements = NodeUtils.findAllStatementsByType(root, Node.EXPR_STATEMENT);
    for (int i = 0; i < statements.size(); i++) {
      Node statement = statements.get(i);
      if (statement.getFirstChild().isAssign()) {
        Node left = statement.getFirstChild().getFirstChild();
        Node right = statement.getFirstChild().getLastChild();
        if (right.isObjectLit()) {
          for (Node child = right.getFirstChild(); child != null;
               child = child.getNext()) {
            String key = child.getString();
            Node keyNode = NodeUtils.findSingleVariableDeclarationByName(
                root, key).getFirstChild();
            Node valueNode = NodeUtils.findSingleStatementByType(
                root, Node.EXPR_STATEMENT)
                .getFirstChild();
            Node value = valueNode.getFirstChild();
            if (value.isGetProp()) {
              Node expectedValueNode = expectedVarMap.get(key).getFirstChild();
              assertEquals("Unexpected value for object property " + key,
                  expectedValueNode, value);
            }
          }
        }
      }
    }

    // Check that function calls in the body of the function are updated
    Node fooCallNode = NodeUtils.findSingleStatementByType(root, Node.CALL_EXPRESSION)
        .getFirstChild();
    List<Node> fooCallArgs = NodeUtils.getCallArguments(fooCallNode);
    for (int i = 0; i < fooCallArgs.size(); i++) {
      Node argNode = fooCallArgs.get(i);
      if (argNode.isGetProp()) {
        String key = argNode.getLastChild().getString();
        Node expectedValueNode = expectedVarMap.get(key).getFirstChild();
        Node valueNode = argNode.getParent().getFirstChild();
        Node value = valueNode.getFirstChild();
        assertEquals("Unexpected value for function call argument " + key,
            expectedValueNode, value);
      }
    }
  }

  private static class NoOpCompiler implements AbstractCompiler {

    @Override
    public Node compile(List<String> sourceUnits, List<SourceFile> sourceFiles) {
      return null;
    }

    @Override
    public void reportCodeChange() {
      // Do nothing
    }
  }
}