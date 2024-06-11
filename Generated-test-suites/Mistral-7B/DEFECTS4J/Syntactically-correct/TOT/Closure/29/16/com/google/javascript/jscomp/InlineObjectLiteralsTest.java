package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.NodeTreeMatcher.isAssign;
import static com.google.javascript.rhino.NodeTreeMatcher.isCall;
import static com.google.javascript.rhino.NodeTreeMatcher.isGetProp;
import static com.google.javascript.rhino.NodeTreeMatcher.isObjectLit;
import static com.google.javascript.rhino.NodeTreeMatcher.isSetProp;
import static com.google.javascript.rhino.NodeTreeMatcher.isVar;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.processing.ProcessTester;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private static AbstractCompiler createCompiler() {
    return new AbstractCompiler() {
      @Override
      public CompilerPassManager getPassManager() {
        return new CompilerPassManager(this) {
          @Override
          public void addPass(int priority, CompilerPass pass) {
            addPass(InlineObjectLiterals.class, priority, pass);
          }
        };
      }
    };
  }

  public static void main(String[] args) throws Exception {
    ProcessTester tester = new ProcessTester();
    Compiler compiler = createCompiler();

    String source = "function f(o) {" +
      "  var a = o.a;" +
      "  var b = o.b;" +
      "  return [a, b, a + b];" +
      "}" +
      "var x = {a: 1, b: 2};" +
      "var y = {b: 2, a: 1};" +
      "var r = f(x); // [1, 2, 3]" +
      "var s = f(y); // [2, 1, 3]";

    String expected = "function f(o) {" +
      "  var a = o.a;" +
      "  var b = o.b;" +
      "  return [a, b, a + b];" +
      "}" +
      "var x = {" +
      "  a: JSCompiler_object_inline_a_1," +
      "  b: JSCompiler_object_inline_b_2" +
      "};" +
      "var y = {" +
      "  b: JSCompiler_object_inline_b_1," +
      "  a: JSCompiler_object_inline_a_1" +
      "};" +
      "var r = f(x); // [1, 2, 3]" +
      "var s = f(y); // [1, 2, 3]";

    List<String> sourceFiles = Lists.newArrayList("Test.js");
    List<String> options = Lists.newArrayList("-d", "Test");

    Node root = tester.processString(compiler, source, sourceFiles, options);

    String actual = tester.getSourceUnit(root);

    Map<String, Object> declarations = Maps.newLinkedHashMap();
    Map<String, Node> references = Maps.newHashMap();

    Iterator<Node> children = root.iterateChildren();
    while (children.hasNext()) {
      Node node = children.next();
      if (isVar(node)) {
        String name = node.getString();
        declarations.put(name, node);
        String varName = node.getName();
        references.put(varName, node);
      }
    }

    Node functionNode = null;
    children = root.iterateChildren();
    while (children.hasNext()) {
      Node node = children.next();
      if (isCall(node) && node.getFirstChild().isName() && node.getFirstChild().getString().equals("f")) {
        functionNode = node;
        break;
      }
    }

    List<Reference> referencesInFunction = Lists.newArrayList();
    Iterator<Node> childrenFunction = functionNode.iterateChildren();
    while (childrenFunction.hasNext()) {
      Node node = childrenFunction.next();
      if (isGetProp(node)) {
        String varName = node.getLastChild().getString();
        referencesInFunction.add(new Reference(functionNode,
            node, null, null, null, null, null));
      }
    }

    ReferenceCollection referenceCollection = new ReferenceCollection();
    new ReferenceCollectingCallback(compiler, new ObjectLiteralsBehavior())
        .process(root, referenceCollection);

    CompilerPassManager passManager = compiler.getPassManager();
    passManager.runPasses(root, InlineObjectLiterals.class, null);

    String actualInline = tester.getSourceUnit(root);

    // Check if all variables have been inlined
    for (Map.Entry<String, Node> entry : references.entrySet()) {
      String name = entry.getKey();
      Node node = entry.getValue();
      Node inlineNode = null;
      for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
        if (isVar(child) && child.getName().equals(name)) {
          inlineNode = child;
          break;
        }
      }
      // Check if the variable is present in the function
      for (Reference reference : referencesInFunction) {
        if (reference.getName().equals(name)) {
          // Check if the variable has been replaced with the inlined variables
          Node replacement = reference.getParent();
          Node replacementChild = null;
          if (isSetProp(replacement)) {
            replacementChild = replacement.getFirstChild();
          } else if (isAssign(replacement)) {
            replacementChild = replacement.getFirstChild();
          }
          if (replacementChild != null && inlineNode != null) {
            Node inlineChild = inlineNode.getFirstChild();
            if (inlineChild != null && replacementChild.isName()) {
              String inlineName = inlineChild.getName();
              assertThat(replacementChild.getString(), is(inlineName));
            }
          }
          break;
        }
      }
    }

    // Check if all objects have been inlined
    for (Map.Entry<String, Object> entry : declarations.entrySet()) {
      String name = entry.getKey();
      Node node = entry.getValue();
      if (isObjectLit(node)) {
        String actualInlineVariables = "";
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
          if (child.isVar()) {
            String varName = child.getName();
            Node variable = references.get(varName);
            String inlineVariableName = node.getFirstChildByName(varName).getName();
            actualInlineVariables += inlineVariableName + ",";
          }
        }

        String expectedInlineVariables = "";
        for (Map.Entry<String, String> inlineEntry : referenceCollection.getInlineMap().entrySet()) {
          String inlineName = inlineEntry.getValue();
          String variableName = inlineName.substring(InlineObjectLiterals.VAR_PREFIX.length());
          expectedInlineVariables += variableName + ",";
        }

        String expectedInlineVariablesTrimmed = expectedInlineVariables.substring(0, expectedInlineVariables.length() - 1);
        String actualInlineVariablesTrimmed = actualInlineVariables.substring(0, actualInlineVariables.length() - 1);

        assertThat(expectedInlineVariablesTrimmed, is(actualInlineVariablesTrimmed));
      }
    }

    // Check if all objects have been inlined in the function
    for (Reference reference : referenceCollection.getInlineMap().values()) {
      Node node = reference.getNode();
      if (node != null && isObjectLit(node)) {
        String actualInlineObject = tester.getSourceUnit(node.getFirstChild());
        String expectedInlineObject = expected.substring(expected.indexOf(nameOf(node)) + nameOf(node).length()).replace("JSCompiler_object_inline_", "").replaceAll(",|\\}", "");
        assertThat(actualInlineObject, is(expectedInlineObject));
      }
    }

    System.out.println("Expected:");
    System.out.println(expected);
    System.out.println("Actual:");
    System.out.println(actualInline);
    System.out.println("Passed tests.");

  }

  private static String nameOf(Node node) {
    if (node.isVar()) {
      return node.getName();
    }
    if (node.isGetProp()) {
      return node.getLastChild().getString();
    }
    return "";
  }
}

class ObjectLiteralsBehavior extends InliningBehavior {
  @Override
  public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
    super.afterExitScope(t, referenceMap);
    referenceMap.clear();
  }
}