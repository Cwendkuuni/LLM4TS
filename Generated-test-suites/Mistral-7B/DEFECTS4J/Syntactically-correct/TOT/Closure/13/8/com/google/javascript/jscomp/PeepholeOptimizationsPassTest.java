import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerParameters;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private CompilerParameters params;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    params = new CompilerParameters();
    pass = new PeepholeOptimizationsPass(compiler, new MyPeepholeOptimization1(), new MyPeepholeOptimization2());
  }

  @Test
  public void testEmpty() {
    SourceFile source = new SourceFile("test.js", new StringWriter());
    Node root = new Node(Node.EMPTY);
    pass.process(params.getDefaultExterns(), root);
    assertNotNull(pass.getCompiler());
  }

  @Test
  public void testSimple() {
    SourceFile source = new SourceFile("test.js", new StringWriter());
    Node root = new Node("function foo() { return 42; }");
    pass.process(params.getDefaultExterns(), root);
    assertNull(pass.getCompiler().getChangeHandler().getLastChange());
  }

  @Test
  public void testChange() {
    SourceFile source = new SourceFile("test.js", new StringWriter());
    Node root = new Node("function foo() { return 42; }");
    pass.process(params.getDefaultExterns(), root);
    Node changedNode = new Node("function foo() { return 43; }");

    when(compiler.addChangeHandler(any(CodeChangeHandler.class)))
        .thenReturn(pass.getCompiler().getChangeHandler());
    when(compiler.getCompilerState()).thenReturn(new CompilerState());

    pass.visit(root);
    assertNull(pass.getCompiler().getChangeHandler().getLastChange());

    Node newNode = new Node("function foo() { return 43; }");
    when(compiler.getCompilerState().getRoot()).thenReturn(newNode);

    pass.visit(root);
    assertNotNull(pass.getCompiler().getChangeHandler().getLastChange());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    pass = new PeepholeOptimizationsPass(null, new MyPeepholeOptimization1(), new MyPeepholeOptimization2());
  }

  @Test(expected = NullPointerException.class)
  public void testNullPeepholeOptimization() {
    pass = new PeepholeOptimizationsPass(compiler, null, new MyPeepholeOptimization2());
  }

  @Test
  public void testMultipleOptimizations() {
    SourceFile source = new SourceFile("test.js", new StringWriter());
    Node root = new Node("function foo() { return 42; }");
    pass.process(params.getDefaultExterns(), root);

    Node changedNode = new Node("function foo() { return 'hello'; }");
    when(compiler.addChangeHandler(any(CodeChangeHandler.class)))
        .thenReturn(pass.getCompiler().getChangeHandler());
    when(compiler.getCompilerState()).thenReturn(new CompilerState());

    pass.visit(root);
    assertNull(pass.getCompiler().getChangeHandler().getLastChange());

    Node newNode = new Node("function foo() { return 'hello'; }");
    when(compiler.getCompilerState().getRoot()).thenReturn(newNode);

    pass.visit(root);
    assertNotNull(pass.getCompiler().getChangeHandler().getLastChange());
  }

  private static class StringWriter extends PrintWriter {
    public StringWriter() {
      super(new StringWriter());
    }

    private static class StringWriter extends java.io.StringWriter {
    }
  }

  private static class MyPeepholeOptimization1
      implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      if (n.isNumberLiteral()) {
        return new Node("42");
      }
      return n;
    }

    @Override
    public void beginTraversal(CompilerPass compilerPass) {
    }

    @Override
    public void endTraversal(CompilerPass compilerPass) {
    }
  }

  private static class MyPeepholeOptimization2
      implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      if (n.isFunction()) {
        Node body = n.getFirstChild();
        Node newBody = new Node("return 42;");
        Node newFunction = new Node("function", n.getFirstChild(), newBody);
        return newFunction;
      }
      return n;
    }

    @Override
    public void beginTraversal(CompilerPass compilerPass) {
    }

    @Override
    public void endTraversal(CompilerPass compilerPass) {
    }
  }
}