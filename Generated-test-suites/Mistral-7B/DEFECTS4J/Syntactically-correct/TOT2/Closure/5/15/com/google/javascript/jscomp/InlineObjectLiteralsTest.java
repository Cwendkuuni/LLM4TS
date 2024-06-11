import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test");

    Node root = Node.objectLiteral();
    root.setFirstChild(Node.prop("a", Node.numberLiteral(1)));
    root.setNext(Node.prop("b", Node.numberLiteral(2)));

    Node externs = Node.objectLiteral();
    compiler.compile(externs, root, options);

    pass.process(externs, root);

    Node expected = Node.objectLiteral();
    expected.setFirstChild(Node.prop("a", Node.name("a_1_test")));
    expected.setNext(Node.prop("b", Node.name("b_1_test")));

    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testInlineComplexObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test");

    Node root = Node.objectLiteral();
    root.setFirstChild(Node.prop("a", Node.objectLiteral()));
    Node a = root.getFirstChild();
    a.setFirstChild(Node.prop("x", Node.numberLiteral(1)));
    a.setNext(Node.prop("y", Node.numberLiteral(2)));

    root.setNext(Node.prop("b", Node.objectLiteral()));
    Node b = root.getNext();
    b.setFirstChild(Node.prop("z", Node.numberLiteral(3)));
    b.setNext(Node.prop("w", Node.numberLiteral(4)));

    Node externs = Node.objectLiteral();
    compiler.compile(externs, root, options);

    pass.process(externs, root);

    Node expected = Node.objectLiteral();
    expected.setFirstChild(Node.prop("a_x_1_test", Node.numberLiteral(1)));
    expected.setNext(Node.prop("a_y_1_test", Node.numberLiteral(2)));
    expected.setNext(Node.prop("b_z_2_test", Node.numberLiteral(3)));
    expected.setNext(Node.prop("b_w_2_test", Node.numberLiteral(4)));

    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testInlineObjectLiteralWithPropertiesReferencingEachOther() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test");

    Node root = Node.objectLiteral();
    Node a = Node.objectLiteral();
    a.setFirstChild(Node.prop("x", Node.stringLiteral("a")));
    a.setNext(Node.prop("y", Node.objectLiteral()));
    Node y = Node.objectLiteral();
    y.setFirstChild(Node.prop("z", Node.stringLiteral("b")));
    y.setNext(Node.prop("w", Node.name("a.x")));
    a.setNext(y);

    root.setFirstChild(Node.prop("a", a));

    Node externs = Node.objectLiteral();
    compiler.compile(externs, root, options);

    pass.process(externs, root);

    Node expected = Node.objectLiteral();
    Node aExp = Node.objectLiteral();
    aExp.setFirstChild(Node.prop("x_1_test", Node.stringLiteral("a")));
    aExp.setNext(Node.prop("y", Node.objectLiteral()));
    Node yExp = Node.objectLiteral();
    yExp.setFirstChild(Node.prop("z_2_test", Node.stringLiteral("b")));
    yExp.setNext(Node.prop("w_2_test", Node.name("aExp.x_1_test")));
    aExp.setNext(yExp);

    expected.setFirstChild(Node.prop("a_1_test", aExp));

    assertEquals(expected.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithCycle() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test");

    Node root = Node.objectLiteral();
    Node a = Node.objectLiteral();
    a.setFirstChild(Node.prop("x", Node.name("a")));
    a.setNext(Node.prop("y", Node.objectLiteral()));
    Node y = Node.objectLiteral();
    y.setFirstChild(Node.prop("z", Node.name("a")));
    a.setNext(y);

    root.setFirstChild(Node.prop("a", a));

    Node externs = Node.objectLiteral();
    compiler.compile(externs, root, options);

    pass.process(externs, root);

    fail("Expected an AssertionError due to infinite recursion");
  }
}