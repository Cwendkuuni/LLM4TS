import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createIfStatement;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createVariable;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.GraphTraversal.NodeScope;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test_util.TestBase;
import com.google.javascript.rhino.Node;

public class DeadAssignmentsEliminationTest extends TestBase {

  public void testDeadAssignmentElmination() {
    CompilerOptions options = new CompilerOptions(null);
    options.setAssumptions(ImmutableMap.of(CompilerOptions.Assumption.DEAD_CODE, true));

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createList(
                NodeFactory.createVariable("x", createNumber(-1)),
                NodeFactory.createAssign(createName("x"), createNumber(0)),
                NodeFactory.createAssign(createName("x"), createNumber(1)),
                NodeFactory.createIfStatement(
                    createName("x"),
                    createBlock(NodeFactory.createAssign(createName("x"), createNumber(2)),
                        createReturnStatement(createNumber(3))),
                    NodeFactory.createBlock()
                ),
                NodeFactory.createAssign(createName("x"), createNumber(4))
            )
        )
    );

    NodeScope scope = NodeTraversal.createRootScope(root);
    Scope globalScope = scope;
    Node n = NodeTraversal.traverse(compiler, root, new NodeTraversal.RootScopeVisitor(scope));
    traverse(compiler, n, pass);

    assertEquals("Dead assignment elimination should have removed the first assignment", 1,
        NodeUtil.getAssignmentCount(n));

    FlowState state = n.getAnnotation();
    Var x = globalScope.getVar("x");
    assertTrue("Variable should be dead", state.getOut().isDead(x));

    Node deadCode = NodeUtil.getFirstChildOfType(n, Node.YIELD);
    assertFalse("Dead code should have been removed", deadCode != null);
  }

  public void testDeadAssignmentElminationInsideFunction() {
    CompilerOptions options = new CompilerOptions(null);
    options.setAssumptions(ImmutableMap.of(CompilerOptions.Assumption.DEAD_CODE, true));

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration(
                "test",
                NodeFactory.createList(NodeFactory.createName("x")),
                NodeFactory.createBlock(
                    NodeFactory.createList(
                        createVariable("x", createNumber(-1)),
                        NodeFactory.createAssign(createName("x"), createNumber(0)),
                        NodeFactory.createAssign(createName("x"), createNumber(1)),
                        NodeFactory.createIfStatement(
                            createName("x"),
                            createBlock(NodeFactory.createAssign(createName("x"), createNumber(2)),
                                createReturnStatement(createNumber(3))),
                            NodeFactory.createBlock()
                        ),
                        NodeFactory.createAssign(createName("x"), createNumber(4))
                    )
                )
            )
        )
    );

    NodeScope scope = NodeTraversal.createRootScope(root);
    Scope globalScope = scope;
    Node n = NodeTraversal.traverse(compiler, root, new NodeTraversal.RootScopeVisitor(scope));
    traverse(compiler, n, pass);

    assertEquals("Dead assignment elimination should have removed the first assignment", 1,
        NodeUtil.getAssignmentCount(n));

    FlowState state = n.getAnnotation();
    Var x = globalScope.getVar("x");
    assertTrue("Variable should be dead", state.getOut().isDead(x));

    Node deadCode = NodeUtil.getFirstChildOfType(n, Node.YIELD);
    assertFalse("Dead code should have been removed", deadCode != null);
  }

  public void testDeadAssignmentElminationWithFunctionCall() {
    CompilerOptions options = new CompilerOptions(null);
    options.setAssumptions(ImmutableMap.of(CompilerOptions.Assumption.DEAD_CODE, true));

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration(
                "test",
                NodeFactory.createList(),
                NodeFactory.createBlock(
                    NodeFactory.createList(
                        NodeFactory.createAssign(createName("x"), createName("y")),
                        NodeFactory.createAssign(createName("y"), createNumber(2)),
                        NodeFactory.createAssign(createName("x"), createNumber(0)),
                        NodeFactory.createReturnStatement(createName("x"))
                    )
                )
            ),
            NodeFactory.createFunctionDeclaration(
                "test2",
                NodeFactory.createList(),
                NodeFactory.createBlock(
                    NodeFactory.createList(
                        NodeFactory.createVariable("y", createNumber(-1)),
                        NodeFactory.createAssign(createName("y"), createNumber(1)),
                        NodeFactory.createAssign(createName("y"), createNumber(2)),
                        NodeFactory.createCall(createName("test"), NodeFactory.createList()),
                        NodeFactory.createAssign(createName("x"), createNumber(3))
                    )
                )
            )
        )
    );

    NodeScope scope = NodeTraversal.createRootScope(root);
    Scope globalScope = scope;
    Node n = NodeTraversal.traverse(compiler, root, new NodeTraversal.RootScopeVisitor(scope));
    traverse(compiler, n, pass);

    assertEquals("Dead assignment elimination should have removed the first assignment", 2,
        NodeUtil.getAssignmentCount(n));

    FlowState state = n.getAnnotation();
    Var x = globalScope.getVar("x");
    Var y = globalScope.getVar("y");
    assertTrue("Variable x should be dead", state.getOut().isDead(x));
    assertTrue("Variable y should be dead", state.getOut().isDead(y));

    Node deadCode = NodeUtil.getFirstChildOfType(n, Node.YIELD);
    assertFalse("Dead code should have been removed", deadCode != null);
  }
}