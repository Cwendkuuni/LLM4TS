import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

  private static Node createVariableDeclaration(Node parent, String variableName, boolean isAssignment) {
    Node varNode = new Node(Token.VAR);
    varNode.setParent(parent);
    varNode.setString(variableName);

    if (isAssignment) {
      Node assign = new Node(Token.ASSIGN);
      assign.setParent(varNode);
      Node value = new Node(Token.NUMBER, 42);
      assign.setFirstChild(varNode);
      assign.setLastChild(value);
      return assign;
    }

    return varNode;
  }

  private static Node createFunctionNode(Node parent) {
    Node function = new Node(Token.FUNCTION);
    function.setParent(parent);
    Node body = new Node(Token.BLOCK);
    function.setBody(body);
    return function;
  }

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.SIMPLE_OPTIMIZATIONS);
    compiler = new Compiler(options);
  }

  private Compiler compiler;

  @org.junit.Test
  public void testCollapseSimpleDeclarations() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", false);
    Node varB = createVariableDeclaration(function, "b", false);

    Node expected = new Node(Token.VAR, Arrays.asList(varA, varB), null);
    Node collapsed = new Node(Token.VAR, ImmutableList.of(), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isVar() && n == expected.getFirstChild()) {
          Node actual = n;
          assertEquals("Variable declaration 'a' collapsed correctly.", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseAssignmentWithVariableDeclaration() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", true);
    Node varB = createVariableDeclaration(function, "b", false);

    Node expected = new Node(Token.ASSIGN, varB, varA);
    Node collapsed = new Node(Token.VAR, ImmutableList.of(varA), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign() && n == expected) {
          Node actual = n;
          assertEquals("Variable declaration 'b' collapsed correctly.", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseAssignmentWithVariableDeclarationWithoutCollapsing() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", true);
    Node varB = createVariableDeclaration(function, "b", false);

    Node expected = new Node(Token.ASSIGN, varB, varA);
    Node collapsed = new Node(Token.ASSIGN, varB, varA);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign() && n == expected) {
          Node actual = n;
          assertEquals("Variable declaration 'b' not collapsed.", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseMultipleAssignmentsWithVariableDeclaration() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", true);
    Node varB = createVariableDeclaration(function, "b", false);
    Node varC = createVariableDeclaration(function, "c", false);

    Node expected = new Node(Token.ASSIGN, varB, varA);
    Node assignBtoA = expected.getFirstChild();

    expected = new Node(Token.ASSIGN, varC, varA);
    Node assignCtoA = expected.getFirstChild();

    Node collapsed = new Node(Token.ASSIGN, varC, varB);
    Node assignCtoB = collapsed.getFirstChild();

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isAssign() && n == assignBtoA) {
          Node actual = n;
          assertEquals("Variable declaration 'b' collapsed into 'a'", expected, actual);
          return;
        }

        if (n.isAssign() && n == assignCtoB) {
          Node actual = n;
          assertEquals("Variable declaration 'c' collapsed into 'b'", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseMultipleAssignmentsWithVariableDeclarations() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", true);
    Node varB = createVariableDeclaration(function, "b", false);
    Node varC = createVariableDeclaration(function, "c", false);

    Node expected = new Node(Token.ASSIGN, varB, varA);
    Node assignBtoA = expected.getFirstChild();

    expected = new Node(Token.ASSIGN, varC, varA);
    Node assignCtoA = expected.getFirstChild();

    Node collapsed = new Node(Token.VAR,
        ImmutableList.of(
            new Node(Token.VAR, Arrays.asList(varA), null),
            new Node(Token.ASSIGN, varC, varB)
        ), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isVar() && n == varA) {
          Node actual = n;
          assertEquals("Variable 'a' collapses to a single declaration.", expected, actual);
          return;
        }

        if (n.isAssign() && n == assignBtoA) {
          Node actual = n;
          assertEquals("Variable declaration 'b' collapsed into 'a'", expected, actual);
          return;
        }

        if (n.isAssign() && n == assignCtoB) {
          Node actual = n;
          assertEquals("Variable declaration 'c' collapsed into 'b'", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseDeclarationsInsideFunction() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", false);
    Node varB = createVariableDeclaration(function, "b", false);
    Node varC = createVariableDeclaration(function, "c", false);

    Node expected = new Node(Token.BLOCK, ImmutableList.of(
        new Node(Token.VAR, Arrays.asList(varA, varB, varC), null)
    ), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && n == expected) {
          Node actual = n;
          assertEquals("Variables collapses inside a function.", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseDeclarationsInsideConditional() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node ifStatement = new Node(Token.IF);
    globals.addChildToBack(ifStatement);

    Node condition = new Node(Token.LOGICAL_OR);
    ifStatement.setCondition(condition);

    Node variableA = createVariableDeclaration(ifStatement, "a", false);
    Node variableB = createVariableDeclaration(ifStatement, "b", false);

    Node expected = new Node(Token.BLOCK, ImmutableList.of(variableA, variableB), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && n == expected) {
          Node actual = n;
          assertEquals("Variables collapses inside a conditional.", expected, actual);
          return;
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapseDeclarationsWithJSDocInfo() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node function = createFunctionNode(globals);
    Node varA = createVariableDeclaration(function, "a", false);
    Node varB = createVariableDeclaration(function, "b", false);

    Node expected = new Node(Token.BLOCK, ImmutableList.of(varA, varB), null);

    Node docInfoA = new JSDocInfo();
    docInfoA.setTagInfo("@param {number} a A description for variable a.");
    varA.setJSDocInfo(docInfoA);

    Node docInfoB = new JSDocInfo();
    docInfoB.setTagInfo("@param {number} b A description for variable b.");
    varB.setJSDocInfo(docInfoB);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    Node varAWithDocInfo = null;
    Node varBWithDocInfo = null;

    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isVar() && n == varA) {
          varAWithDocInfo = n;
          return;
        }

        if (n.isVar() && n == varB) {
          varBWithDocInfo = n;
          return;
        }

        if (n.isBlock() && n == expected) {
          Node actual = n;
          assertEquals("Variables collapses with JSDocInfo.", expected, actual);
          return;
        }
      }
    });

    Node varAWithDocInfoCollapsed = varAWithDocInfo;
    Node varBWithDocInfoCollapsed = varBWithDocInfo;

    if (varAWithDocInfoCollapsed != null && varBWithDocInfoCollapsed != null) {
      Node collapsedBlock = new Node(Token.BLOCK, ImmutableList.of(varAWithDocInfoCollapsed, varBWithDocInfoCollapsed), null);
      Node docInfoCollapsed = collapsedBlock.getFirstChild();
      docInfoCollapsed.setTagInfo("@param {number} a,b A description for variables a and b.");
      expected.setJSDocInfo(docInfoCollapsed);

      traverser = new NodeTraversal(compiler, root);
      traverser.traverse(new AbstractPostOrderCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
          if (n.isBlock() && n == expected) {
            Node actual = n;
            assertEquals("Collapsed variables with JSDocInfo.", expected, actual);
            return;
          }
        }
      });
    }
  }

  @org.junit.Test
  public void testCollapseDeclarationsInsideLoop() {
    Node root = new Node(Token.PROGRAM);
    Node globals = new Node(Token.BLOCK);
    root.setBody(globals);

    Node forStatement = new Node(Token.FOR);
    globals.addChildToBack(forStatement);

    Node initialization = new Node(Token.BLOCK);
    forStatement.setInitialization(initialization);

    Node variableA = createVariableDeclaration(initialization, "a", false);
    Node variableB = createVariableDeclaration(initialization, "b", false);

    Node expected = new Node(Token.BLOCK, ImmutableList.of(variableA, variableB), null);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    NodeTraversal traverser = new NodeTraversal(compiler, root);
    traverser.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && n == expected) {
          Node actual = n;
          assertEquals("Variables collapses inside a loop.", expected, actual);
          return;
        }
      }
    });
  }
}