package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Tests for the InlineObjectLiterals CompilerPass
 */
public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  public void testInlineObjectLiterals() throws Exception {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c();" +
        "}" +
        "function bar() {" +
        "  var obj = {" +
        "    x: null," +
        "    y: { z: 1 }" +
        "  };" +
        "  var a = obj.x;" +
        "  var b = obj.y.z;" +
        "}" +
        "function baz() {" +
        "  var obj = {" +
        "    a: null," +
        "    b: null," +
        "    c: null," +
        "    d: function() { return null; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c;" +
        "  var w = obj.d();" +
        "}" +
        "function qux() {" +
        "  var obj = {" +
        "    a: null," +
        "    b: null," +
        "    c: null," +
        "    d: null" +
        "  }";

    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Node root = parser.parse(jsCode, null, /* sourceFile */ null);
    pass.process(null, root);

    Node externs = root.getFirstChild();
    Node foo = findNodeRecursively(root, "foo");
    Node bar = findNodeRecursively(root, "bar");
    Node baz = findNodeRecursively(root, "baz");
    Node qux = findNodeRecursively(root, "qux");

    // Check that the object literals have been inlined
    checkNode("foo.js", foo, "function foo() {" +
              "  var a = 1;" +
              "  var b = 2;" +
              "  var c = function() { return 3; };" +
              "  var x = a;" +
              "  var y = b;" +
              "  var z = c();" +
              "}");

    checkNode("bar.js", bar, "function bar() {" +
              "  var x = null;" +
              "  var y = { z: 1 };" +
              "  var z = y.z;" +
              "}");

    checkNode("baz.js", baz, "function baz() {" +
              "  var a = null;" +
              "  var b = null;" +
              "  var c = null;" +
              "  var d = function() { return null; };" +
              "  var x = a;" +
              "  var y = b;" +
              "  var z = c;" +
              "  var w = d();" +
              "}");

    checkNode("qux.js", qux, "function qux() {" +
              "  var a = null;" +
              "  var b = null;" +
              "  var c = null;" +
              "  var d = null;" +
              "}");

    // Check that the variables have the correct names
    checkVariableNames("foo.js", "x", "y", "z");
    checkVariableNames("bar.js", "x", "y", "z");
    checkVariableNames("baz.js", "x", "y", "z", "w");
    checkVariableNames("qux.js", "a", "b", "c", "d");

    // Check that the object literals have been properly split
    Node fooObj = findNodeRecursively(foo, "obj");
    Node barObj = findNodeRecursively(bar, "obj");
    Node bazObj = findNodeRecursively(baz, "obj");

    checkNode("foo.js", fooObj, "var a = 1;" +
              "var b = 2;" +
              "var c = function() { return 3; };" +
              "var a_1 = a;" +
              "var b_1 = b;" +
              "var c_1 = c;" +
              "var obj = { a: a_1, b: b_1, c: c_1 };" +
              "var x = a_1;" +
              "var y = b_1;" +
              "var z = c_1();");

    checkNode("bar.js", barObj, "var x = null;" +
              "var y = { z: 1 };" +
              "var y_1 = y;" +
              "var z_1 = y_1.z;" +
              "var obj = { x: x, y: y_1 };" +
              "var x = x;" +
              "var z = z_1");

    checkNode("baz.js", bazObj, "var a = null;" +
              "var b = null;" +
              "var c = null;" +
              "var d = function() { return null; };" +
              "var a_1 = a;" +
              "var b_1 = b;" +
              "var c_1 = c" +
              "var d_1 = d;" +
              "var obj = { a: a_1, b: b_1, c: c_1, d: d_1 };" +
              "var x = a_1;" +
              "var y = b_1;" +
              "var z = c_1;" +
              "var w = d_1();");

  }

  private void checkVariableNames(String fileName, String... variableNames) {
    Node root = getRootNodeFromFile(fileName);
    for (String variableName : variableNames) {
      Node variable = findVariableByNameRecursively(root, variableName);
      checkNode(fileName, variable, "var " + variableName);
    }
  }

  private Node findVariableByNameRecursively(Node node, String variableName) {
    if (node.getType() == Token.VAR && variableName.equals(node.getString())) {
      return node;
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChildAtIndex(i);
      Node result = findVariableByNameRecursively(child, variableName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findNodeRecursively(Node node, String name) {
    if (node.getType() == Token.NAME && node.getString().equals(name)) {
      return node;
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChildAtIndex(i);
      Node result = findNodeRecursively(child, name);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private void checkNode(String fileName, Node node, String expectedCode) {
    String actualCode = getCodeFromNode(node);
    assertEquals(expectedCode, actualCode);
    assertEquals(fileName, node.getSourceName());
  }

  private String getCodeFromNode(Node node) {
    StringBuilder sb = new StringBuilder();
    NodeUtil.printTreeToString(node, sb, null, null, null, null, null);
    return sb.toString();
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }
}