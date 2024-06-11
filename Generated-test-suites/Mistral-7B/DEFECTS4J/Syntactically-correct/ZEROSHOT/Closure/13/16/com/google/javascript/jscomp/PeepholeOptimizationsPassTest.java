import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeType.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.Nodeops;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testPeepholeOptimizationsPass() {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler,
        new ConstFolding(), new DeadCodeRemoval(), new SparseArrayOptimization());

    Node root = Nodeops.newObjectLiteral();
    Node optimized = pass.getCompiler().getRootNode();

    pass.process(null, root);

    // Basic test that the pass ran
    assertEquals(optimized, root.getParent().getFirstChild());

    // Test specific optimizations
    Node variableNode = Nodeops.newVariable(IDENT, "x");
    Node functionNode = Nodeops.newFunction(NULL, ImmutableList.of(), ImmutableList.of(NOP), variableNode);

    Node unoptimizedFunction = Nodeops.newFunction(ASSIGN, ImmutableList.of(IDENT, IDENT), ImmutableList.of(), functionNode);
    Node optimizedFunction = Nodeops.newFunction(ASSIGN, ImmutableList.of(IDENT, IDENT), ImmutableList.of(), Nodeops.newIdent(IDENT));

    Node unoptimizedNode = Nodeops.newAssign(ASSIGN, IDENT, Nodeops.newCall(CALL, IDENT, ImmutableList.of(unoptimizedFunction)));
    Node optimizedNode = Nodeops.newAssign(ASSIGN, IDENT, Nodeops.newCall(CALL, IDENT, ImmutableList.of(optimizedFunction)));

    Node unoptimizedRoot = Nodeops.newBlock(null, ImmutableList.of(unoptimizedNode));
    Node optimizedRoot = Nodeops.newBlock(null, ImmutableList.of(optimizedNode));

    pass.process(null, unoptimizedRoot);

    Node unoptimizedVariable = unoptimizedRoot.getFirstChild().getFirstChild();
    Node optimizedVariable = optimizedRoot.getFirstChild().getFirstChild();
    Node unoptimizedCall = unoptimizedRoot.getFirstChild().getLastChild();
    Node optimizedCall = optimizedRoot.getFirstChild().getLastChild();

    // ConstFolding test
    assertEquals(optimizedVariable, unoptimizedVariable);

    // DeadCodeRemoval test
    assertNull(optimizedCall);

    // SparseArrayOptimization test
    Node sparseArrayNode = Nodeops.newProperty(ASSIGN, IDENT, Nodeops.newArrayLiteral(INT_LIT, INT_LIT, INT_LIT, INT_LIT), null);
    Node unoptimizedSparseArray = Nodeops.newAssign(ASSIGN, IDENT, Nodeops.newAssign(ASSIGN, Nodeops.newIdent(IDENT), sparseArrayNode, NULL, INT_LIT));

    Node optimizedSparseArray = Nodeops.newAssign(ASSIGN, IDENT, Nodeops.newIdent(IDENT));
    Node optimizedSparseArrayAssignment = Nodeops.newAssign(ASSIGN, optimizedSparseArray, Nodeops.newProperty(ASSIGN, optimizedSparseArray, INT_LIT, INT_LIT));

    Node unoptimizedSparseArrayRoot = Nodeops.newBlock(null, ImmutableList.of(unoptimizedSparseArrayAssignment));
    Node optimizedSparseArrayRoot = Nodeops.newBlock(null, ImmutableList.of(optimizedSparseArrayAssignment));

    pass.process(null, unoptimizedSparseArrayRoot);

    Node unoptimizedSparseArrayChild = unoptimizedSparseArray.getLastChild();
    Node optimizedSparseArrayChild = optimizedSparseArray.getLastChild();

    // SparseArrayOptimization test
    assertEquals(optimizedSparseArrayChild, unoptimizedSparseArrayChild);
  }

}