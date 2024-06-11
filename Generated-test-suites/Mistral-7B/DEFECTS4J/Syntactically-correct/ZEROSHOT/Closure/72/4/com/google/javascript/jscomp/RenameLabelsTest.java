import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.visit;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Test file for RenameLabels class.
 */
public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String code =
        "function foo() {" +
            "  a: {" +
            "    b: break a;" +
            "  }" +
            "  c: {" +
            "    d: break c;" +
            "  }" +
            "}";

    Compiler compiler = new TestCompiler();
    CompilerOptions options = CompilerOptions.getDefault();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.setOptions(options);

    Node ast = AbstractCompiler.parse(code, null, null);
    renameLabels.process(compiler.getExterns(), ast);

    NodeTraversal nodeTraversal = new NodeTraversal(ast, compiler);
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    visit(nodeTraversal, ast, null, processLabels);

    String expectedCode =
        "function foo() {" +
            "  a_: {" +
            "    b_: break a_;" +
            "  }" +
            "  c_: {" +
            "    d_: break c_;" +
            "  }" +
            "}";

    String actualCode = NodeUtil.getSource(ast);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testRenameLabelsWithEmptyNamespace() {
    String code = "function foo() { a: break a; }";

    Compiler compiler = new TestCompiler();
    CompilerOptions options = CompilerOptions.getDefault();
    RenameLabels renameLabels = new RenameLabels(compiler, new EmptyNameSupplier(), true);
    renameLabels.setOptions(options);

    Node ast = AbstractCompiler.parse(code, null, null);
    renameLabels.process(compiler.getExterns(), ast);

    NodeTraversal nodeTraversal = new NodeTraversal(ast, compiler);
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    visit(nodeTraversal, ast, null, processLabels);

    String expectedCode = "function foo() { a_: break a_; }";

    String actualCode = NodeUtil.getSource(ast);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testRenameLabelsWithRemoveUnused() {
    String code = "function foo() { a: break a; }";

    Compiler compiler = new TestCompiler();
    CompilerOptions options = CompilerOptions.getDefault();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    renameLabels.setOptions(options);

    Node ast = AbstractCompiler.parse(code, null, null);
    renameLabels.process(compiler.getExterns(), ast);

    NodeTraversal nodeTraversal = new NodeTraversal(ast, compiler);
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    visit(nodeTraversal, ast, null, processLabels);

    String expectedCode = "function foo() {}";

    String actualCode = NodeUtil.getSource(ast);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testRenameLabelsWithDeepNesting() {
    String code = "function foo() {" +
        "  a: {" +
        "    b: {" +
        "      c: {" +
        "        d: break c;" +
        "      }" +
        "    }" +
        "  }" +
        "}";

    Compiler compiler = new TestCompiler();
    CompilerOptions options = CompilerOptions.getDefault();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.setOptions(options);

    Node ast = AbstractCompiler.parse(code, null, null);
    renameLabels.process(compiler.getExterns(), ast);

    NodeTraversal nodeTraversal = new NodeTraversal(ast, compiler);
    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    visit(nodeTraversal, ast, null, processLabels);

    String expectedCode = "function foo() {" +
        "  a_: {" +
        "    b_: {" +
        "      c_: {" +
        "        d_: break c_;" +
        "      }" +
        "    }" +
        "  }" +
        "}";

    String actualCode = NodeUtil.getSource(ast);
    assertEquals(expectedCode, actualCode);
  }

  private static class DefaultNameSupplier implements RenameLabels.Supplier<String> {
    // NameGenerator is used to create safe label names.
    final NameGenerator nameGenerator =
        new NameGenerator(new HashSet<String>(), "", null);

    @Override
    public String get() {
      return nameGenerator.generateNextName();
    }
  }

  private static class EmptyNameSupplier implements RenameLabels.Supplier<String> {
    @Override
    public String get() {
      return "";
    }
  }
}