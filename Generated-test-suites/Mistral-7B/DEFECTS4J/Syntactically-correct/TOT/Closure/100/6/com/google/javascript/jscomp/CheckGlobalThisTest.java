import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    compiler = new Compiler();
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testGlobalThis() {
    String code = "function evil() { this.useful = 42; }";
    SourceFile source = Parser.parseJs(new NodeFactory(), new SourceFile("test.js", code), null);
    Node root = source.getTree();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(1, checker.getNumDiagnostics());
  }

  @Test
  public void testConstructor() {
    String code = "function EvilConstructor() { this.useful = 42; }";
    SourceFile source = Parser.parseJs(new NodeFactory(), new SourceFile("test.js", code), null);
    Node root = source.getTree();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testThisAnnotation() {
    String code = "/** @this {MyType} */ function EvilThis() { this.useful = 42; }";
    SourceFile source = Parser.parseJs(new NodeFactory(), new SourceFile("test.js", code), null);
    Node root = source.getTree();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testPrototypeMethod() {
    String code = "function MyType() {} MyType.prototype.evil = function() { this.useful = 42; }";
    SourceFile source = Parser.parseJs(new NodeFactory(), new SourceFile("test.js", code), null);
    Node root = source.getTree();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testNestedAssignments() {
    String code = "(a = this).property.evil = 42;";
    NodeFactory nodeFactory = new NodeFactory();
    SourceFile source = new SourceFile("test.js", null);
    Node root = nodeFactory.parseJs(code, source);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumDiagnostics());
  }

  @Test
  public void testGlobalThisInsideFunction() {
    String code = "function evil() { function inner() { this.useful = 42; } }";
    SourceFile source = Parser.parseJs(new NodeFactory(), new SourceFile("test.js", code), null);
    Node root = source.getTree();
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();
    assertEquals(1, checker.getNumDiagnostics());
  }
}