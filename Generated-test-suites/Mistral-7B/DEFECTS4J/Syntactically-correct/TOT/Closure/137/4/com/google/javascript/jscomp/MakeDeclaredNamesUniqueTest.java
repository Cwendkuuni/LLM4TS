package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the MakeDeclaredNamesUnique class.
 */
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function f() {" +
        "var x = 1, y = 1, z = 1;" +
        "function g() {" +
        "  var x = 2, y = 2, z = 2;" +
        "  function h() {" +
        "    var x = 3, y = 3, z = 3;" +
        "  }" +
        "}" +
        "var r = function() {};" +
        "function h2() {}" +
        "try {" +
        "  throw new Error('test');" +
        "} catch (e) {" +
        "  var x = 4;" +
        "}" +
        "var a = [1, 2, 3];" +
        "var b = [\"x\", \"y\", \"z\"];" +
        "function f2() {" +
        "  var x = 5;" +
        "}" +
        "var c = {'x': 1, 'y': 2, 'z': 3};" +
        "var d = {x: 1, y: 2, z: 3};";

    AbstractCompiler compiler = new FrontendApi(null, null, null).getCompiler();
    compiler.setOption("warningFileDescriptors", ImmutableSet.of());

    Node rootNode = new TokenStream(jsCode).parse();

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();

    // Traverse the abstract syntax tree to rename all local and function names
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, null, null, null);
    nodeTraversal.traverse(renamer);

    // Check the renamed names in the abstract syntax tree
    checkRenamedNames(rootNode, "x", "x0");
    checkRenamedNames(rootNode, "y", "y0");
    checkRenamedNames(rootNode, "z", "z0");
    checkRenamedNames(rootNode, "g", "g0");
    checkRenamedNames(rootNode, "h", "h0");
    checkRenamedNames(rootNode, "r", "r0");
    checkRenamedNames(rootNode, "h2", "h20");
    checkRenamedNames(rootNode, "x", "x1");
    checkRenamedNames(rootNode, "y", "y1");
    checkRenamedNames(rootNode, "z", "z1");
    checkRenamedNames(rootNode, "x", "x2");
    checkRenamedNames(rootNode, "y", "y2");
    checkRenamedNames(rootNode, "z", "z2");
    checkRenamedNames(rootNode, "x", "x3");
    checkRenamedNames(rootNode, "y", "y3");
    checkRenamedNames(rootNode, "z", "z3");
    checkRenamedNames(rootNode, "x", "x4");

    // Check that the names in the arrays and objects are not renamed
    Node aNode = findChild(rootNode, "a");
    assertArrayEquals(new String[]{"x0", "y0", "z0"}, getArrayNames(aNode));

    Node bNode = findChild(rootNode, "b");
    List<String> bNames = Lists.newArrayList();
    for (Node child = bNode.getFirstChild(); child != null; child = child.getNext()) {
      bNames.add(child.getString());
    }
    assertEquals(ImmutableSet.of("x", "y", "z"), ImmutableSet.copyOf(bNames));

    Node cNode = findChild(rootNode, "c");
    Map<String, Object> cMap = new HashMap<>();
    for (Node child = cNode.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getFirstChild().getString();
      Object value = child.getNext().getValue();
      cMap.put(key, value);
    }
    assertEquals(ImmutableMap.of("x", 1, "y", 2, "z", 3), cMap);

    Node dNode = findChild(rootNode, "d");
    Map<String, Object> dMap = new HashMap<>();
    for (Node child = dNode.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getNext().getString();
      Object value = child.getNext().getValue();
      dMap.put(key, value);
    }
    assertEquals(ImmutableMap.of("x", 1, "y", 2, "z", 3), dMap);

    Node f2Node = findChild(rootNode, "f2");
    Node xNode = findChild(f2Node, "x");
    assertNull(xNode);
  }

  private void checkRenamedNames(Node node, String name, String expectedName) {
    Node nameNode = findChild(node, name);
    Node renamedNode = findChild(node, expectedName);
    assertNotNull(renamedNode);
    assertEquals(renamedNode.getParent(), node);
    assertEquals(node, nameNode.getParent());
    assertEquals(nameNode, renamedNode);
  }

  private String[] getArrayNames(Node node) {
    List<String> names = Lists.newArrayList();
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME) {
        names.add(child.getString());
      }
    }
    return names.toArray(new String[0]);
  }

  private Node findChild(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }
}