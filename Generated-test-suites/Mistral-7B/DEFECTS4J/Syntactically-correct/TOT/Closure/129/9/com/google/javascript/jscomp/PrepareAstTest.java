import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  @Test
  public void testPrepareAstNormalization() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    String sourceCode = "function foo() { return 42; }";
    Node root = AbstractCompiler.parseJavaScript(sourceCode, null);

    prepareAst.process(null, root);

    Node expectedRoot = IR.function("foo", null, null, null, null, null, null, null);
    expectedRoot.setSourcePosition(sourceCode, 0, 0);

    // Check that the root node has been normalized to the expected form.
    assertEquals(expectedRoot.toString(), root.toString());

    // Check that the prepareAst process method does not change the tree when checkOnly is true.
    PrepareAst checkOnlyPrepareAst = new PrepareAst(compiler, true);
    Node checkOnlyRoot = AbstractCompiler.parseJavaScript(sourceCode, null);
    checkOnlyPrepareAst.process(null, checkOnlyRoot);
    assertEquals(checkOnlyRoot.toString(), sourceCode);
  }

  @Test
  public void testPrepareAstNormalizationWithControlStructures() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    String sourceCode =
        "if (true) { return 42; } else { return 43; }";
    Node root = AbstractCompiler.parseJavaScript(sourceCode, null);

    prepareAst.process(null, root);

    Node expectedRoot = IR.if_(
        IR.literal(true),
        IR.return_(IR.literal(42)),
        IR.return_(IR.literal(43))
    );
    expectedRoot.setSourcePosition(sourceCode, 0, 0);

    // Check that the root node has been normalized to the expected form.
    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testPrepareAstNormalizationWithObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    String sourceCode =
        "var obj = {\n" +
        "  a: function() {\n" +
        "    return 42;\n" +
        "  },\n" +
        "  b: 43\n" +
        "}";
    Node root = AbstractCompiler.parseJavaScript(sourceCode, null);

    prepareAst.process(null, root);

    Node expectedRoot = IR.objectLit(
        IR.property("a", IR.function("a", null, null, null, null, null, null, null)),
        IR.property("b", IR.literal(43))
    );
    expectedRoot.setSourcePosition(sourceCode, 0, 0);

    // Check that the root node has been normalized to the expected form.
    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testPrepareAstNormalizationWithControlStructuresAndObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    String sourceCode =
        "if (true) {\n" +
        "  var obj = {\n" +
        "    a: function() {\n" +
        "      return 42;\n" +
        "    },\n" +
        "    b: 43\n" +
        "  };\n" +
        "  return obj.a();\n" +
        "} else {\n" +
        "  return 44;\n" +
        "}";
    Node root = AbstractCompiler.parseJavaScript(sourceCode, null);

    prepareAst.process(null, root);

    Node expectedRoot = IR.if_(
        IR.literal(true),
        IR.objectLit(
            IR.property("a", IR.function("a", null, null, null, null, null, null, null)),
            IR.property("b", IR.literal(43))
        ),
        IR.literal(44)
    );
    expectedRoot.setSourcePosition(sourceCode, 0, 0);

    // Check that the root node has been normalized to the expected form.
    assertEquals(expectedRoot.toString(), root.toString());
  }
}