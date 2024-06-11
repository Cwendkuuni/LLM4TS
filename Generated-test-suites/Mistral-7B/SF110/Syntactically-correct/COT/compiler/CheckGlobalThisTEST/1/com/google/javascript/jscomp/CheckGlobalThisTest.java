package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.annotation.MockAnnotation;
import org.powermock.api.support.annotation.PowerMockAnnotation;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckGlobalThis.class})
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    checker = new Checker(compiler, null);
  }

  @Test
  public void testShouldTraverseFunction() {
    String code = "function unsafeFunction() {}";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);
    traversal.traverse(node);

    assertTrue(CheckGlobalThis.shouldTraverse(node, null, null));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    String code = "function unsafeConstructor() {}";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    JSDocInfo jsDoc = new JSDocInfo("constructor");
    node.setJSDocInfo(jsDoc);

    assertFalse(CheckGlobalThis.shouldTraverse(node, null, null));
  }

  @Test
  @PowerMockAnnotation(classnames = {CheckGlobalThis.class}, mockModes = {MockAnnotation.MODE_STRICT})
  public void testShouldNotTraverseFunctionAnnotatedWithAtThis() {
    String code = "@this function unsafeFunction() {}";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertTrue(CheckGlobalThis.shouldTraverse(node, null, null));
  }

  @Test
  @PowerMockAnnotation(classnames = {CheckGlobalThis.class}, mockModes = {MockAnnotation.MODE_STRICT})
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceToPutAThisAnnotation() {
    String code = "function unsafeFunction() { this.property = \"value\"; }";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertTrue(CheckGlobalThis.shouldTraverse(node, null, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentIsObjectLiteralKey() {
    String code = "var obj = {property: function unsafeFunction() {}};";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertFalse(CheckGlobalThis.shouldTraverse(node, null, null));
  }

  @Test
  public void testShouldReportThisKeyword() {
    String code = "var obj = {\n" +
        "  method: function() {\n" +
        "    this.property = \"value\";\n" +
        "  },\n" +
        "};\n" +
        "function unsafeFunction() {\n" +
        "  this.property = \"value\";\n" +
        "}";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    Node unsafeFunctionNode = findUnsafeFunctionNode(node);
    assertTrue(CheckGlobalThis.shouldReportThis(unsafeFunctionNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnLeftSideOfAssignment() {
    String code = "var a = function() {\n" +
        "  this.property = \"value\";\n" +
        "}";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    Node aNode = findANode(node, "a");
    assertNotNull(aNode);
    assertFalse(CheckGlobalThis.shouldReportThis(aNode));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    String code = "var a = {\n" +
        "  method: function() {\n" +
        "    this.property = this;\n" +
        "  },\n" +
        "};\n" +
        "var b = a.method;\n" +
        "b = b.bind(this);";

    Node node = new Parser().parse(code, null, null).getTree();
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    Node bNode = findANode(node, "b");
    assertNotNull(bNode);
    assertFalse(CheckGlobalThis.shouldReportThis(bNode));
  }

  private Node findUnsafeFunctionNode(Node node) {
    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
      if (n.isFunction()) {
        return n;
      }
    }
    return null;
  }

  private Node findANode(Node node, String name) {
    for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
      if (n.isName() && n.getString().equals(name)) {
        return n;
      }
    }
    for (Node n = node.getLastChild(); n != null; n = n.getPreviousSibling()) {
      if (n.isName() && n.getString().equals(name)) {
        return n;
      }
    }
    return null;
  }
}