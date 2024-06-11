import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParseException;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeTraversal traversal;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
    traversal = new NodeTraversal(compiler);
  }

  @Test
  public void testSafeThisUsage() {
    String source = "function Foo() {}";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeThisAssignment() {
    String source = "this = 5;";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    Node node = traversal.traverse(file.ast, null).findFirst(NodeUtil.isAssign());
    assertNotNull(node);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic);
  }

  @Test
  public void testUnsafeThisPropertyAccess() {
    String source = "var o = {}; this.x = 5;";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    Node node = traversal.traverse(file.ast, null).findFirst(NodeUtil.isGetProp());
    assertNotNull(node);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic);
  }

  @Test
  public void testUnsafeThisPrototypeAccess() {
    String source = "var o = {}; this.prototype.x = 5;";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    Node node = traversal.traverse(file.ast, null).findFirst(NodeUtil.isGetProp());
    assertNotNull(node);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic);
  }

  @Test
  public void testSafeThisPrototypeAccess() {
    String source = "function Foo() {} Foo.prototype.x = 5;";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeThisConstructor() {
    String source = "function Foo() { this.x = 5; }";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    Node node = traversal.traverse(file.ast, null).findFirst(NodeUtil.isFunction());
    assertNotNull(node);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostic);
  }

  @Test
  public void testSafeThisConstructorAnnotation() {
    String source = "function Foo@constructor() { this.x = 5; }";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeThisAnonymousFunction() {
    String source = "(function() { this.x = 5; })();";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testSafeThisAnonymousFunctionWithParent() {
    String source = "var o = {}; (function() { this.x = o; })();";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test(expected = ParseException.class)
  public void testParanormalAssignment() {
    String source = "this(5);";
    TestSourceFile file = new TestSourceFile("Test.js", source);
    compiler.compile(file, null, null);
  }
}