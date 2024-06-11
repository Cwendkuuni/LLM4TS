import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestingHelper;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = ParserTestingHelper.parseJavaScript("test.js",
        "function evil() { this.useful = undefined; }");
    compiler.process(rootNode, null);
  }

  @After
  public void tearDown() {
    compiler = null;
    rootNode = null;
  }

  /**
   * Test method for the {@link CheckGlobalThis#CheckGlobalThis(AbstractCompiler, CheckLevel)} constructor.
   */
  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    assertNotNull(checker);
  }

  /**
   * Test method for the {@link CheckGlobalThis#shouldTraverse(NodeTraversal, Node, Node)} method.
   */
  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    Node functionNode = rootNode.getFirstChild();

    // Test function that is not a constructor.
    Node functionNodeNoConstructor = rootNode.getFirstChild().getNext();
    assertTrue(checker.shouldTraverse(traversal, functionNodeNoConstructor, null));

    // Test function that is a constructor.
    JSDocInfo jsDoc = functionNode.getJSDocInfo();
    if (jsDoc != null && jsDoc.isConstructor()) {
      Node constructorFunctionNode = functionNode;
      assertFalse(checker.shouldTraverse(traversal, constructorFunctionNode, null));
    } else {
      fail("Function should be a constructor.");
    }

    // Test function with @this annotation.
    functionNode = functionNode.getNext();
    jsDoc = functionNode.getJSDocInfo();
    if (jsDoc != null && jsDoc.hasThisType()) {
      Node constructorFunctionNode = functionNode;
      assertFalse(checker.shouldTraverse(traversal, constructorFunctionNode, null));
    } else {
      fail("Function should have @this annotation.");
    }
  }

  /**
   * Test method for the {@link CheckGlobalThis#shouldReportThis(Node, Node)} method.
   */
  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    Node functionNode = rootNode.getFirstChild();

    // Test THIS on the left side of an assignment.
    Node assignNode = functionNode.getNext();
    Node lhs = assignNode.getFirstChild();
    Node rhs = assignNode.getLastChild();

    assertFalse(checker.shouldReportThis(lhs, null));
    assertTrue(checker.shouldReportThis(rhs, assignNode));

    // Test THIS on the right side of an assignment to a property.
    Node assignNode2 = rootNode.getLastChild();
    lhs = assignNode2.getFirstChild();
    rhs = assignNode2.getLastChild();

    assertTrue(checker.shouldReportThis(lhs, assignNode2));
    assertFalse(checker.shouldReportThis(rhs, null));
  }

  /**
   * Test method for the {@link CheckGlobalThis#visit(NodeTraversal, Node, Node)} method.
   */
  @Test
  public void testVisit() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    NodeTraversal traversal = new NodeTraversal(rootNode, checker);
    Node functionNode = rootNode.getFirstChild();

    // Test function that is not a constructor.
    Node visitNode1 = functionNode;
    checker.visit(traversal, visitNode1, null);
    assertEquals(0, compiler.getDiagnostics().size());

    // Test function that is a constructor.
    functionNode = functionNode.getNext();
    JSDocInfo jsDoc = functionNode.getJSDocInfo();
    if (jsDoc != null && jsDoc.isConstructor()) {
      Node constructorFunctionNode = functionNode;
      checker.visit(traversal, constructorFunctionNode, null);
      assertEquals(0, compiler.getDiagnostics().size());
    } else {
      fail("Function should be a constructor.");
    }

    // Test function with @this annotation.
    functionNode = functionNode.getNext();
    jsDoc = functionNode.getJSDocInfo();
    if (jsDoc != null && jsDoc.hasThisType()) {
      Node constructorFunctionNode = functionNode;
      checker.visit(traversal, constructorFunctionNode, null);
      assertEquals(0, compiler.getDiagnostics().size());
    } else {
      fail("Function should have @this annotation.");
    }

    // Test function with a dangerous use of the global this object.
    functionNode = rootNode.getLastChild();
    Node dangerousThisNode = functionNode.getFirstChild();

    checker.visit(traversal, dangerousThisNode, null);
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic);
  }
}