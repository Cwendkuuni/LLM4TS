import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    String source = "function Foo() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testUnsafePropertyAccess() {
    String source = "function Foo() { return this.x; }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().get(0).getMessage().startsWith(
        "dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeAssignment() {
    String source = "function Foo() { this = {}; }";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().get(0).getMessage().startsWith(
        "dangerous use of the global 'this' object"));
  }

  @Test
  public void testFunctionWithJsDocAnnotation() {
    String source = "/**\n * @constructor\n */\n function Foo() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testFunctionWithJsDocThisAnnotation() {
    String source = "/**\n * @this {Object}\n */\n function Foo() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertTrue(compiler.getErrors().isEmpty());
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    String source = "function Foo() {}";
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    SourceFile input = new SourceFile("Test.js", source);
    Node root = input.parseTree();
    NodeFactory nodeFactory = new NodeFactory();
    CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(root, check).traverse();
    compiler.process(input, options);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}