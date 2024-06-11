import static com.google.javascript.jscomp.CompilerPasses.CrossModuleMethodMotion.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerTestHelper;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  private CompilerTestHelper helper;

  @Before
  public void setUp() throws Exception {
    helper = new CompilerTestHelper(new CompilerOptions());
  }

  @Test
  public void testMoveMethodAcrossModules() throws Exception {
    String source = "var Foo = function() {};\n" +
        "Foo.prototype.bar = function() { return 1; };\n" +
        "var A = function() {};\n" +
        "A.prototype.baz = function() { return this.bar(); };\n" +
        "var B = function(a) { this.a = a; };\n" +
        "B.prototype.qux = function() { return this.a.baz(); };";

    SourceFile input = helper.getSourceFile("Input.js", source);

    AbstractCompiler compiler = helper.createCompiler();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);

    CompilerResult result = helper.testCompilerPass(input, crossModuleMethodMotion);

    Node root = result.getCompiled();

    // Check that the method was moved to the correct module
    Node aPrototype = helper.findNodeInTree(root, "A.prototype");
    Node qux = helper.findNodeInTree(root, "B.prototype.qux");
    Node bar = helper.findNodeInTree(root, "Foo.prototype.bar");
    Node baz = helper.findNodeInTree(root, "A.prototype.baz");

    // Check that the method was stubbed in the original module
    assertNotNull(baz);
    Node stub = helper.findNodeInTree(root, "JSCompiler_stubMethod(*.*)");
    assertEquals(baz, stub.getParent());

    // Check that the method was unstubbed in the deeper module
    assertNotNull(qux);
    Node unstub = helper.findNodeInTree(root, "JSCompiler_unstubMethod(*.*)");
    assertEquals(qux, unstub.getParent().getChildAt(1));
    Node body = unstub.getChildAt(1);
    assertEquals(bar, body);
  }

  @Test
  public void testMoveMethodToSameModule() throws Exception {
    String source = "var Foo = function() {};\n" +
        "Foo.prototype.bar = function() { return 1; };\n" +
        "var A = function() {};\n" +
        "A.prototype.baz = function() { return this.bar(); };\n" +
        "A.prototype.qux = function() { return this.bar(); };";

    SourceFile input = helper.getSourceFile("Input.js", source);

    AbstractCompiler compiler = helper.createCompiler();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);

    CompilerResult result = helper.testCompilerPass(input, crossModuleMethodMotion);

    Node root = result.getCompiled();

    // Check that the method was not moved since it's in the same module
    Node aPrototype = helper.findNodeInTree(root, "A.prototype");
    Node qux = helper.findNodeInTree(root, "A.prototype.qux");
    Node bar = helper.findNodeInTree(root, "Foo.prototype.bar");
    Node baz = helper.findNodeInTree(root, "A.prototype.baz");

    // Check that no stubbing or unstubbing occurred
    assertNull(baz);
    assertNull(qux);
  }
}