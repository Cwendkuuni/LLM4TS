import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private NodeTraversal traversal;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    rootNode = Node.objectLiteral();
    traversal = NodeTraversal.create(compiler, rootNode, mock(Scope.class));
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentElimination() {
    // Test case 1: Simple dead assignment elimination.
    Node deadAssignment = new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(1));
    rootNode.appendChild(deadAssignment);
    rootNode.appendChild(new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2)));

    deadAssignmentsElimination.process(null, rootNode);

    // The second assignment should be removed.
    assert (!rootNode.hasChild(deadAssignment));

    // Test case 2: Dead assignment elimination with conditionals.
    Node condition = new Node(Token.IF, Node.newBoolean(true), rootNode);
    Node deadAssignmentInConditional = new Node(Token.ASSIGN,
        Node.newName("x"), Node.newNumber(1));
    condition.appendChild(deadAssignmentInConditional);
    condition.appendChild(new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2)));

    rootNode.appendChild(condition);

    deadAssignmentsElimination.process(null, rootNode);

    // The second assignment in the conditional should be removed.
    assert (!condition.hasChild(deadAssignmentInConditional));

    // Test case 3: Dead assignment elimination with loops.
    Node forLoop = new Node(Token.FOR, rootNode, null, null, null, null, null);
    Node conditionInLoop = new Node(Token.IF, Node.newBoolean(true), forLoop);
    Node deadAssignmentInLoop = new Node(Token.ASSIGN,
        Node.newName("x"), Node.newNumber(1));
    conditionInLoop.appendChild(deadAssignmentInLoop);
    conditionInLoop.appendChild(new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2)));

    forLoop.appendChild(conditionInLoop);

    rootNode.appendChild(forLoop);

    deadAssignmentsElimination.process(null, rootNode);

    // The second assignment in the for loop should be removed.
    assert (!conditionInLoop.hasChild(deadAssignmentInLoop));

    // Test case 4: Dead assignment elimination with function calls.
    Node functionCall = new Node(Token.CALL, Node.newName("myFunction"),
        Node.newArray());
    Node deadAssignmentInFunctionCall = new Node(Token.ASSIGN,
        Node.newName("x"), Node.newNumber(1));
    functionCall.appendChild(deadAssignmentInFunctionCall);
    functionCall.appendChild(new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2)));

    rootNode.appendChild(functionCall);

    deadAssignmentsElimination.process(null, rootNode);

    // The second assignment in the function call should be removed.
    assert (!functionCall.hasChild(deadAssignmentInFunctionCall));

    // Test case 5: Dead assignment elimination with complex expressions.
    Node complexExpression = new Node(Token.ASSIGN, Node.newName("x"),
        new Node(Token.PLUS, new Node(Token.ASSIGN, Node.newName("y"),
            Node.newNumber(1)), Node.newNumber(2)));

    rootNode.appendChild(complexExpression);

    deadAssignmentsElimination.process(null, rootNode);

    // The complex expression should remain unchanged.
    assert (rootNode.hasChild(complexExpression));

    // Test case 6: Dead assignment elimination with variable declarations.
    Scope scope = mock(Scope.class);
    when(scope.isDeclared(Node.newName("x"), false)).thenReturn(true);
    when(scope.isDeclared(Node.newName("y"), false)).thenReturn(true);
    when(scope.getVar(Node.newName("x"))).thenReturn(new Var("x", 0, 0));
    when(scope.getVar(Node.newName("y"))).thenReturn(new Var("y", 1, 1));

    Node variableDeclaration = new Node(Token.ASSIGN,
        Node.newName("x"), Node.newName("y"));
    Node deadAssignmentInVariableDeclaration = new Node(Token.ASSIGN,
        Node.newName("y"), Node.newNumber(1));

    Node variableInitialization = new Node(Token.VAR, Node.newName("x"),
        new Node(Token.ASSIGN, Node.newName("x"), Node.newNumber(1)));
    variableInitialization.appendChild(variableDeclaration);

    Node variableDeclarationWithDeadAssignment = new Node(Token.VAR,
        Node.newName("y"),
        new Node(Token.ASSIGN, Node.newName("y"), Node.newNumber(1))).appendChild(
            variableDeclaration);
    variableDeclarationWithDeadAssignment.appendChild(variableInitialization);

    rootNode.appendChild(variableDeclarationWithDeadAssignment);

    deadAssignmentsElimination.process(null, rootNode);

    // The dead assignment should be removed.
    assert (!variableDeclaration.hasParent());
  }

  @Test
  public void testDeadAssignmentEliminationWithFunction() {
    // Test case 1: Dead assignment elimination with a function.
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilerMock);

    Node function = new Node(Token.FUNCTION, Node.newName("myFunction"),
        Node.newArray(), Node.newBlock(), null, null, null);
    Node assignment = new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(1));
    Node statement = new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2));

    function.appendChild(assignment);
    function.appendChild(statement);

    Node root = new Node(Token.BLOCK, null, Node.newBlock(), null, null, null,
        null);
    root.appendChild(function);

    NodeTraversal traversalMock = mock(NodeTraversal.class);
    when(traversalMock.getScope()).thenReturn(mock(Scope.class));
    when(traversalMock.getControlFlowGraph()).thenReturn(
        mock(ControlFlowGraph.class));
    when(traversalMock.getScopeRoot()).thenReturn(root);

    when(compilerMock.getTraversal()).thenReturn(traversalMock);

    deadAssignmentsElimination.process(null, root);

    // The dead assignment should be removed.
    assert (!function.hasChild(assignment));
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariable() {
    // Test case 1: Dead assignment elimination with a dead variable.
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compilerMock);

    Node function = new Node(Token.FUNCTION, Node.newName("myFunction"),
        Node.newArray(), Node.newBlock(), null, null, null);
    Node deadVariableDeclaration = new Node(Token.VAR, Node.newName("x"),
        Node.newArray(), Node.newBlock(), null, null, null);
    Node deadVariableAssignment = new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(1));
    Node statement = new Node(Token.ASSIGN, Node.newName("x"),
        Node.newNumber(2));

    deadVariableDeclaration.appendChild(deadVariableAssignment);
    function.appendChild(deadVariableDeclaration);
    function.appendChild(statement);

    Node root = new Node(Token.BLOCK, null, Node.newBlock(), null, null, null,
        null);
    root.appendChild(function);

    NodeTraversal traversalMock = mock(NodeTraversal.class);
    when(traversalMock.getScope()).thenReturn(mock(Scope.class));
    when(traversalMock.getControlFlowGraph()).thenReturn(
        mock(ControlFlowGraph.class));
    when(traversalMock.getScopeRoot()).thenReturn(root);

    when(compilerMock.getTraversal()).thenReturn(traversalMock);

    // Simulate a dead variable by making the variable not reachable.
    Scope scopeMock = mock(Scope.class);
    when(scopeMock.isDeclared(Node.newName("x"), false)).thenReturn(true);
    when(scopeMock.getVar(Node.newName("x"))).thenReturn(null);

    when(traversalMock.getScope()).thenReturn(scopeMock);

    deadAssignmentsElimination.process(null, root);

    // The dead variable declaration and assignment should be removed.
    assert (!function.hasChild(deadVariableDeclaration));
  }
}