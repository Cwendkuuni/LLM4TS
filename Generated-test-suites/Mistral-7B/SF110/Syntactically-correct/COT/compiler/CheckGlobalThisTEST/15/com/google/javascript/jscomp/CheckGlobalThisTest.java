import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckGlobalThis.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    functionNode.setJSDocInfo(new JSDocInfo(functionNode, "constructor"));

    Node parentNode = new Node(Token.BLOCK, null, null, null);
    parentNode.addChild(functionNode);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(parentNode);

    traverser.traverse(rootNode);

    assertTrue("Function annotated with 'constructor' should be traversed", checker.assignLhsChild == null);
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThisOrInterfaceOrOverride() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    functionNode.setJSDocInfo(new JSDocInfo(functionNode, ""));
    functionNode.addJSDocAnnotation("@this");

    Node parentNode = new Node(Token.BLOCK, null, null, null);
    parentNode.addChild(functionNode);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(parentNode);

    traverser.traverse(rootNode);

    assertTrue("Function annotated with '@this' should not be traversed", checker.assignLhsChild != null);

    // test for function annotated with @interface or @override
    functionNode.setJSDocInfo(new JSDocInfo(functionNode, ""));
    functionNode.addJSDocAnnotation("@interface");

    traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };
    traverser.traverse(rootNode);
    assertTrue("Function annotated with '@interface' should not be traversed", checker.assignLhsChild != null);

    functionNode.clearAnnotations();
    functionNode.addJSDocAnnotation("@override");

    traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };
    traverser.traverse(rootNode);
    assertTrue("Function annotated with '@override' should not be traversed", checker.assignLhsChild != null);
  }

  @Test
  public void testShouldNotTraverseFunctionOutsideOfScriptOrBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    Node functionNode = new Node(Token.FUNCTION, null, null, null);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(functionNode);

    traverser.traverse(rootNode);

    assertTrue("Function outside of script or block should not be traversed", checker.assignLhsChild == null);

    // test for function as a child of an assignment
    functionNode = new Node(Token.FUNCTION, null, null, null);
    Node assignNode = new Node(Token.ASSIGN, null, null, null);
    assignNode.addChild(functionNode);

    Node parentNode = new Node(Token.SCRIPT, null, null, null);
    parentNode.addChild(assignNode);

    traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };
    traverser.traverse(parentNode);

    assertTrue("Function as a child of an assignment should not be traversed", checker.assignLhsChild == null);
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    Node objectLiteral = new Node(Token.OBJECT_LITERAL, null, null, null);

    Node prototypeProperty = new Node(Token.STRING, "prototype", null, null);
    Node prototypePrototype = new Node(Token.NAME, "prototype", null, null);
    prototypePrototype.setJSDocInfo(new JSDocInfo(prototypePrototype, ""));
    prototypePrototype.addJSDocAnnotation("lends");

    Node property = new Node(Token.GET_PROP, null, null, null);
    property.addChild(prototypeProperty);
    property.addChild(prototypePrototype);
    objectLiteral.addChild(property);

    Node functionNode = new Node(Token.FUNCTION, null, null, null);

    Node parentNode = new Node(Token.BLOCK, null, null, null);
    parentNode.addChild(objectLiteral);
    parentNode.addChild(functionNode);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(parentNode);

    traverser.traverse(rootNode);

    assertTrue("Function lent to prototype should not be traversed", checker.assignLhsChild == null);
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }
    };

    Node functionNode = new Node(Token.FUNCTION, null, null, null);

    Node parentNode = new Node(Token.BLOCK, null, null, null);
    parentNode.addChild(functionNode);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(parentNode);

    traverser.traverse(rootNode);

    assertTrue("Function with no logical place to put 'this' annotation should be traversed", checker.assignLhsChild == null);
  }

  @Test(expected = AssertionError.class)
  public void testReportGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node globalThisNode = new Node(Token.THIS, null, null, null);
    functionNode.addChild(globalThisNode);

    Node parentNode = new Node(Token.BLOCK, null, null, null);
    parentNode.addChild(functionNode);

    Node rootNode = new Node(Token.SCRIPT, null, null, null);
    rootNode.addChild(parentNode);

    NodeTraversal traverser = new NodeTraversal(compiler) {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n == globalThisNode && checker.shouldReportThis(n)) {
          throw new AssertionError("Expected this check to not report global 'this'");
        }
        checker.visit(this, n, parent);
      }
    };
    traverser.traverse(rootNode);
  }
}