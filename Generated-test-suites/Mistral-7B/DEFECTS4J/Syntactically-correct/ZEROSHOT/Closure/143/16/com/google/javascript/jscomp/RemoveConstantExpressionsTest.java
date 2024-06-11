import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.rhino.Token.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeTraversal;

@RunWith(JUnit4.class)
public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private CompilerPass pass;
  private Result result;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {};
    pass = new RemoveConstantExpressions(compiler);
    result = new Result();
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node node = new Node(
        EXPR_STATEMENT,
        new Node(
            BINARY_EXPR,
            new Node(EXPR_RESULT, new Node(LITERAL, 1)),
            new Node(EXPR_RESULT, new Node(NAME, "foo")),
            null,
            null
        ),
        new Node(EXPR_RESULT, new Node(NAME, "bar")),
        null,
        null
    );

    Node newNode = new Node(
        EXPR_STATEMENT,
        new Node(NAME, "foo"),
        new Node(NAME, "bar"),
        null,
        null
    );

    Node originalRoot = new Node(ROOT_NODE, Arrays.asList(node));
    Node newRoot = new Node(ROOT_NODE, Arrays.asList(newNode));

    traverse(null, originalRoot, pass.getCallback());
    assertEquals(newRoot, compiler.getOutput());
    assertTrue(pass.getCallback().getResult().changed);
  }

  @Test
  public void testRemoveConstantExpressionsNoSideEffects() {
    Node node = new Node(
        EXPR_STATEMENT,
        new Node(
            BINARY_EXPR,
            new Node(EXPR_RESULT, new Node(LITERAL, 1)),
            new Node(EXPR_RESULT, new Node(NAME, "foo")),
            null,
            null
        ),
        null,
        null
    );

    Node newNode = new Node(EXPR_STATEMENT, node);

    Node originalRoot = new Node(ROOT_NODE, Arrays.asList(node));
    Node newRoot = new Node(ROOT_NODE, Arrays.asList(newNode));

    traverse(null, originalRoot, pass.getCallback());
    assertEquals(newRoot, compiler.getOutput());
    assertFalse(pass.getCallback().getResult().changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    Node node1 = new Node(
        EXPR_STATEMENT,
        new Node(ASSIGN, new Node(NAME, "x"), new Node(LITERAL, 1)),
        null,
        null,
        null
    );

    Node node2 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, new Node(EXPR_RESULT, node1), new Node(NAME, "y")),
        null,
        null,
        null
    );

    Node node3 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, node2, new Node(NAME, "z")),
        null,
        null,
        null
    );

    Node originalRoot = new Node(ROOT_NODE, Arrays.asList(node3));

    Node newNode1 = new Node(EXPR_STATEMENT, node1);
    Node newNode2 = new Node(
        EXPR_STATEMENT,
        new Node(NAME, "x"),
        new Node(EXPR_STATEMENT, new Node(NAME, "y")),
        null,
        null
    );
    Node newNode3 = new Node(EXPR_STATEMENT, newNode2);

    Node newRoot = new Node(ROOT_NODE, Arrays.asList(newNode3));

    traverse(null, originalRoot, pass.getCallback());
    assertEquals(newRoot, compiler.getOutput());
    assertTrue(pass.getCallback().getResult().changed);
  }

  @Test
  public void testRemoveConstantExpressionsNested() {
    Node node1 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, new Node(EXPR_RESULT, new Node(LITERAL, 1)), new Node(NAME, "x")),
        null,
        null,
        null
    );

    Node node2 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, new Node(EXPR_RESULT, node1), new Node(NAME, "y")),
        null,
        null,
        null
    );

    Node node3 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, new Node(EXPR_STATEMENT, new Node(NAME, "z")), node2),
        null,
        null,
        null
    );

    Node originalRoot = new Node(ROOT_NODE, Arrays.asList(node3));

    Node newNode1 = new Node(EXPR_STATEMENT, node1);
    Node newNode2 = new Node(
        EXPR_STATEMENT,
        new Node(NAME, "x"),
        new Node(EXPR_STATEMENT, new Node(NAME, "y")),
        null,
        null
    );
    Node newNode3 = new Node(EXPR_STATEMENT, newNode2);
    Node newNodeZ = new Node(EXPR_STATEMENT, new Node(NAME, "z"));

    Node newNode2Z = new Node(
        EXPR_STATEMENT,
        new Node(NAME, "y"),
        new Node(EXPR_STATEMENT, newNodeZ),
        null,
        null
    );

    Node newNode1Z = new Node(EXPR_STATEMENT, newNode1);
    Node newNode3Z = new Node(EXPR_STATEMENT, newNode1Z);

    Node newRoot = new Node(ROOT_NODE, Arrays.asList(newNode3Z));

    traverse(null, originalRoot, pass.getCallback());
    assertEquals(newRoot, compiler.getOutput());
    assertTrue(pass.getCallback().getResult().changed);
  }

  @Test
  public void testRemoveConstantExpressionsWithSideEffectCalls() {
    Node node1 = new Node(
        EXPR_STATEMENT,
        new Node(ASSIGN, new Node(NAME, "x"), new Node(NAME, "foo")),
        null,
        null,
        null
    );

    Node node2 = new Node(
        EXPR_STATEMENT,
        new Node(ASSIGN, new Node(NAME, "y"), new Node(NAME, "bar")),
        null,
        null,
        null
    );

    Node node3 = new Node(
        EXPR_STATEMENT,
        new Node(BINARY_EXPR, new Node(EXPR_RESULT, node1), node2),
        null,
        null,
        null
    );

    Node originalRoot = new Node(ROOT_NODE, Arrays.asList(node3));

    Node newNode1 = new Node(EXPR_STATEMENT, node1);
    Node newNode2 = new Node(EXPR_STATEMENT, node2);
    Node newNode3 = new Node(EXPR_STATEMENT, new Node(NAME, "foo"));
    Node newNode4 = new Node(EXPR_STATEMENT, new Node(NAME, "bar"));
    Node newNode5 = new Node(EXPR_STATEMENT, new Node(BINARY_EXPR, newNode3, newNode4));

    Node newNode1X = new Node(EXPR_STATEMENT, new Node(NAME, "x"));
    Node newNode2Y = new Node(EXPR_STATEMENT, new Node(NAME, "y"));

    Node newNode3X = new Node(EXPR_STATEMENT, newNode1X);
    Node newNode4X = new Node(EXPR_STATEMENT, newNode1X);
    Node newNode5Y = new Node(EXPR_STATEMENT, newNode2Y);

    Node newRoot = new Node(ROOT_NODE, Arrays.asList(newNode5, newNode3X, newNode4X, newNode2Y));

    traverse(null, originalRoot, pass.getCallback());
    assertEquals(newRoot, compiler.getOutput());
    assertTrue(pass.getCallback().getResult().changed);
  }
}

class RemoveConstantRValuesCallback extends AbstractPostOrderCallback {
  // Use the existing implementation from the test subject.
}