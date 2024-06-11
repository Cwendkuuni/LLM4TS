import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = NodeTraversal.create(new CompilerOptions(), checker, null);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.createFunction("testFunction");
    Node parent = Node.createBlock();
    parent.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionNode, parent));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructorNode = Node.createFunction("testFunction", null, true);

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, constructorNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionWithThisAnnotation = Node.createFunction("testFunction", null, null, new JSDocInfo(null, "this"));

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionWithThisAnnotation, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionWithOverrideAnnotation = Node.createFunction("testFunction", null, null, new JSDocInfo(null, null, null, "override"));

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionWithOverrideAnnotation, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node functionWithInterfaceAnnotation = Node.createFunction("testFunction", null, null, new JSDocInfo(null, null, null, "interface"));

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionWithInterfaceAnnotation, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node functionNode = Node.createFunction("testFunction");

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeParent() {
    Node functionNode = Node.createFunction("testFunction");
    Node objectNode = Node.createObjectLiteral();
    objectNode.addChildToEnd(Node.createProperty("prototype", functionNode));

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionNode, objectNode));
  }

  @Test
  public void testShouldTraverseFunctionWithObjectLiteralParent() {
    Node functionNode = Node.createFunction("testFunction");
    Node objectNode = Node.createObjectLiteral();
    objectNode.addChildToEnd(Node.createProperty("property", functionNode));

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, functionNode, objectNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignmentNode = Node.createAssign(Node.createVar("prototype"), Node.createThis());
    Node objectNode = Node.createObjectLiteral();

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubproperty() {
    Node assignmentNode = Node.createAssign(Node.createProperty(Node.createProperty("prototype", "subProperty"), ""), Node.createThis());
    Node objectNode = Node.createObjectLiteral();

    assertFalse(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode));
  }

  @Test
  public void testShouldTraverseAssignmentToNonPrototypeProperty() {
    Node assignmentNode = Node.createAssign(Node.createProperty("property", ""), Node.createThis());
    Node objectNode = Node.createObjectLiteral();

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode));
  }

  @Test
  public void testShouldTraverseAssignmentToNonPrototypeSubproperty() {
    Node assignmentNode = Node.createAssign(Node.createProperty(Node.createProperty("property", "subProperty"), ""), Node.createThis());
    Node objectNode = Node.createObjectLiteral();

    assertTrue(checker.shouldTraverse(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node assignmentNode = Node.createAssign(Node.createProperty("property", ""), Node.createThis());
    Node objectNode = Node.createObjectLiteral();

    when(compiler.report(NodeUtil.makeError(assignmentNode, DiagnosticType.warning("JSC_UNUSED_EXPRESSION"))))
        .thenReturn(null);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, null, objectNode);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode);

    Node expectedError = NodeUtil.makeError(assignmentNode, GLOBAL_THIS);
    assertTrue(compiler.report(expectedError) != null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithChild() {
    Node assignNode = Node.createAssign(Node.createProperty("property", ""), Node.createThis());
    Node parentNode = Node.createAssign(Node.createProperty("property", assignNode), Node.createThis());

    when(compiler.report(NodeUtil.makeError(parentNode, DiagnosticType.warning("JSC_UNUSED_EXPRESSION"))))
        .thenReturn(null);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, null, Node.createBlock());

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, parentNode, Node.createBlock());

    Node expectedError = NodeUtil.makeError(assignNode, GLOBAL_THIS);
    assertTrue(compiler.report(expectedError) != null);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignmentNode = Node.createAssign(Node.createProperty("property", Node.createThis()));
    Node objectNode = Node.createObjectLiteral();

    when(compiler.report(NodeUtil.makeError(assignmentNode, DiagnosticType.warning("JSC_UNUSED_EXPRESSION"))))
        .thenReturn(null);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, null, objectNode);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, assignmentNode, objectNode);

    assertFalse(compiler.report(NodeUtil.makeError(assignmentNode, GLOBAL_THIS)) != null);
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccess() {
    Node propertyAccessNode = Node.createGetProp(Node.createThis(), "property");
    Node objectNode = Node.createObjectLiteral();

    when(compiler.report(NodeUtil.makeError(propertyAccessNode, DiagnosticType.warning("JSC_UNUSED_EXPRESSION"))))
        .thenReturn(null);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, null, objectNode);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, propertyAccessNode, objectNode);

    assertFalse(compiler.report(NodeUtil.makeError(propertyAccessNode, GLOBAL_THIS)) != null);
  }

  @Test
  public void testShouldReportThisOnPropertyAccessWithChild() {
    Node getPropNode = Node.createGetProp(Node.createThis(), Node.createProperty("property", Node.createProperty("subProperty", "")));
    Node objectNode = Node.createObjectLiteral();

    when(compiler.report(NodeUtil.makeError(getPropNode, DiagnosticType.warning("JSC_UNUSED_EXPRESSION"))))
        .thenReturn(null);

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, null, Node.createBlock());

    checker.visit(new NodeTraversal(compiler) {
      @Override
      public NodeTraversal traverse(NodeTraversal t, Node n, Node parent) {
        return null;
      }
    }, getPropNode, Node.createBlock());

    Node expectedError = NodeUtil.makeError(getPropNode, GLOBAL_THIS);
    assertTrue(compiler.report(expectedError) != null);
  }
}