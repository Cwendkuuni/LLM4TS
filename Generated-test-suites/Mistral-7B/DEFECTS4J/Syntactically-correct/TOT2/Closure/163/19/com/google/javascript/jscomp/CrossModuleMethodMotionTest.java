import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversingCompiler;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    // Given
    Compiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    Node root = parseJsSource("class A {" +
            "  function foo() {" +
            "    return 'A.foo';" +
            "  }" +
            "}", null);

    // When
    pass.process(null, root);
    Node result = compiler.getResult();

    // Then
    Node aProto = getPrototype(result, "A");
    Node foo = getMethod(aProto, "foo");
    assertNotNull(foo);
    assertEquals("function () {" +
            "  return [\"A.foo\"];" +
            "}", foo.toPrint());

    // Test for edge case: move method to a deeper module
    Node deepModule = getModule(result, "deep");
    Node deepProto = getPrototype(deepModule, "A");
    assertNull(getMethod(deepProto, "foo"));

    // Test for exception handling: try to move a non-function property
    Node bar = newNode("class A {}", null).getChild(0).getFirstChild();
    bar.setProperty("b", newNode("42", null));
    Node root2 = newNode("class A {}", bar);
    pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    pass.process(null, root2);
    result = compiler.getResult();
    assertEquals("class A {}", result.toPrint());
  }
}