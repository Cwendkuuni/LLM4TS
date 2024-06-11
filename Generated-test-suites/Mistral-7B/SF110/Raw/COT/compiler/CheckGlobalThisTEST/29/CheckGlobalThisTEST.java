import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.JSDocInfo.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node functionNode = new Node(FUNCTION);
    Node jsDocInfo = new Node(JSDOC_INFO);
    jsDocInfo.setProperty("isConstructor", true);
    functionNode.setJSDocInfo(jsDocInfo);

    Node result = traversal.traverseOnce(functionNode);
    assertFalse(result instanceof Error);
  }

  @Test
  public void testShouldNotTraverseConstructorWithThisAnnotation() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node functionNode = new Node(FUNCTION);
    Node jsDocInfo = new Node(JSDOC_INFO);
    jsDocInfo.setProperty("isConstructor", true);
    jsDocInfo.setProperty("hasThisType", true);
    functionNode.setJSDocInfo(jsDocInfo);

    Node result = traversal.traverseOnce(functionNode);
    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node functionNode = new Node(FUNCTION);
    Node jsDocInfo = new Node(JSDOC_INFO);
    jsDocInfo.setProperty("isConstructor", false);
    jsDocInfo.setProperty("isOverride", true);
    functionNode.setJSDocInfo(jsDocInfo);

    Node result = traversal.traverseOnce(functionNode);
    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoJSDocInfo() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node functionNode = new Node(FUNCTION);
    Node result = traversal.traverseOnce(functionNode);

    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutPrototypeAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node functionNode = new Node(FUNCTION);
    Node parentNode = new Node(BLOCK);
    Node childNode = new Node(ASSIGN);
    childNode.setFirstChild(functionNode);
    parentNode.setFirstChild(childNode);

    Node result = traversal.traverseOnce(parentNode);
    assertTrue(result instanceof Node);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnAssignmentLeftSide() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node thisNode = new Node(THIS);
    assignNode.setFirstChild(thisNode);

    Node result = traversal.traverseOnce(assignNode);
    fail("Expected error, but got: " + result);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnPropertyAccessLeftSide() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node getNode = new Node(GETPROP);
    Node thisNode = new Node(THIS);
    getNode.setLastChild(new Node(ID("prototype")));
    getNode.setFirstChild(thisNode);

    Node result = traversal.traverseOnce(getNode);
    fail("Expected error, but got: " + result);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnAssignmentRightSideWithPrototypeAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node thisNode = new Node(THIS);
    Node getNode = new Node(GETPROP);
    getNode.setLastChild(new Node(ID("prototype")));
    assignNode.setFirstChild(thisNode);
    assignNode.setLastChild(getNode);

    Node result = traversal.traverseOnce(assignNode);
    fail("Expected error, but got: " + result);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisOnAssignmentRightSideWithPrototypeAccessOnChild() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node thisNode = new Node(THIS);
    Node childNode = new Node(ID("child"));
    Node getNode = new Node(GETPROP);
    getNode.setLastChild(new Node(ID("prototype")));
    getNode.setFirstChild(childNode);
    assignNode.setFirstChild(thisNode);
    assignNode.setLastChild(getNode);

    Node result = traversal.traverseOnce(assignNode);
    fail("Expected error, but got: " + result);
  }

  @Test
  public void testShouldNotReportThisOnPrototypeAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node getNode = new Node(GETPROP);
    Node thisNode = new Node(THIS);
    getNode.setLastChild(new Node(ID("prototype")));
    getNode.setFirstChild(thisNode);

    Node result = traversal.traverseOnce(getNode);
    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldNotReportThisOnAssignmentRightSideWithoutPrototypeAccess() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node thisNode = new Node(THIS);
    Node otherNode = new Node(ID("other"));
    assignNode.setFirstChild(thisNode);
    assignNode.setLastChild(otherNode);

    Node result = traversal.traverseOnce(assignNode);
    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldNotReportThisOnAssignmentLeftSideWithPrototypeAccessOnChild() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node thisNode = new Node(THIS);
    Node childNode = new Node(ID("child"));
    Node getNode = new Node(GETPROP);
    getNode.setLastChild(new Node(ID("prototype")));
    getNode.setFirstChild(childNode);
    assignNode.setFirstChild(getNode);
    assignNode.setLastChild(thisNode);

    Node result = traversal.traverseOnce(assignNode);
    assertTrue(result instanceof Node);
  }

  @Test
  public void testShouldReportThisOnAssignmentLeftSideDeep() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node assignNode = new Node(ASSIGN);
    Node leftSide = new Node(ID("outer"));
    Node thisNode = new Node(THIS);
    Node assignOnLeftSide = new Node(ASSIGN);
    assignOnLeftSide.setFirstChild(thisNode);
    assignOnLeftSide.setLastChild(leftSide);
    assignNode.setFirstChild(assignOnLeftSide);
    assignNode.setLastChild(new Node(ID("inner")));

    Node result = traversal.traverseOnce(assignNode);
    assertTrue(result instanceof Error);
  }

  @Test
  public void testShouldReportThisOnPropertyAccessDeep() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return checkGlobalThis.shouldTraverse(this, node, parent);
      }
    };

    Node getNode = new Node(GETPROP);
    Node thisNode = new Node(THIS);
    Node getOnThis = new Node(GETPROP);
    getOnThis.setLastChild(new Node(ID("outer")));
    getNode.setFirstChild(thisNode);
    getNode.setLastChild(getOnThis);

    Node result = traversal.traverseOnce(getNode);
    assertTrue(result instanceof Error);
  }
}