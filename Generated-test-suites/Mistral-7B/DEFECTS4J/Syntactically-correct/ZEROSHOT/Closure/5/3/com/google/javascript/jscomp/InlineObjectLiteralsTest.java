import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(mockCompiler(), mockNameIdSupplier());
  }

  @Override
  protected CompilerOptions getOptions() {
    return newCompilerOptions();
  }

  @Override
  protected CompilerPassManager getPassManager() {
    return new CompilerPassManager(getOptions());
  }

  private AbstractCompiler mockCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new NoOpCodingConvention());
    return compiler;
  }

  private Supplier<String> mockNameIdSupplier() {
    return mock(Supplier.class);
  }

  public void testInlineSimpleObject() throws Exception {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  return obj.a + obj.b;";

    Node root = parseAndParseToAbstractSyntaxTree(input);

    runPasses(root);

    Node result = getSourceNode(root);
    Node expected = newNode("function foo() {" +
        "  var a = JSCompiler_object_inline_a_" + getNextId() + ";" +
        "  var b = JSCompiler_object_inline_b_" + getNextId() + ";" +
        "  var c = JSCompiler_object_inline_c_" + getNextId() + ";" +
        "  return a + b;" +
        "}");

    compareTrees(expected, result);
  }

  public void testInlineComplexObject() throws Exception {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      x: 1," +
        "      y: 2," +
        "      z: 3" +
        "    }," +
        "    b: {" +
        "      x: 4," +
        "      y: 5," +
        "      z: 6" +
        "    }" +
        "  };" +
        "  return obj.a.x + obj.b.y;" +
        "}";

    Node root = parseAndParseToAbstractSyntaxTree(input);

    runPasses(root);

    Node result = getSourceNode(root);
    Node expected = newNode("function foo() {" +
        "  var a_x = JSCompiler_object_inline_a_x_" + getNextId() + ";" +
        "  var a_y = JSCompiler_object_inline_a_y_" + getNextId() + ";" +
        "  var a_z = JSCompiler_object_inline_a_z_" + getNextId() + ";" +
        "  var b_x = JSCompiler_object_inline_b_x_" + getNextId() + ";" +
        "  var b_y = JSCompiler_object_inline_b_y_" + getNextId() + ";" +
        "  var b_z = JSCompiler_object_inline_b_z_" + getNextId() + ";" +
        "  return a_x + b_y;" +
        "}");

    compareTrees(expected, result);
  }

  public void testDontInlineNonObject() throws Exception {
    String input = "function foo() {" +
        "  var x = 1;" +
        "  var obj = {" +
        "    a: x" +
        "  }";

    Node root = parseAndParseToAbstractSyntaxTree(input);

    runPasses(root);

    Node result = getSourceNode(root);
    assertNull("Expected no inlining", getChildByPropertyPath(result, "foo.obj.a"));
  }

  public void testDontInlineSelfReferentialObject() throws Exception {
    String input = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj.a" +
        "    }" +
        "  };" +
        "  return obj.a.b;" +
        "}";

    Node root = parseAndParseToAbstractSyntaxTree(input);

    runPasses(root);

    Node result = getSourceNode(root);
    assertNull("Expected no inlining", getChildByPropertyPath(result, "foo.obj.a.b"));
  }

  public void testDontInlineObjectWithGetter() throws Exception {
    String input = "function foo() {" +
        "  var obj = {" +
        "    get a() {" +
        "      return 1;" +
        "    }" +
        "  };" +
        "  return obj.a;" +
        "}";

    Node root = parseAndParseToAbstractSyntaxTree(input);

    runPasses(root);

    Node result = getSourceNode(root);
    assertNull("Expected no inlining", getChildByPropertyPath(result, "foo.obj.a"));
  }

  private Node getChildByPropertyPath(Node node, String propertyPath) {
    String[] properties = propertyPath.split("\\.");
    Node current = node;
    for (String property : properties) {
      Node child = null;
      if (current.isObjectLit()) {
        for (Node prop = current.getFirstChild(); prop != null;
             prop = prop.getNext()) {
          if (prop.getString().equals(property)) {
            child = prop.getFirstChild();
            break;
          }
        }
      } else if (current.isGetProp()) {
        child = current.getFirstChild();
      }
      if (child == null) {
        return null;
      }
      current = child;
    }
    return current;
  }

  private Node newNode(String nodeText) {
    return new Node(newToken(Token.TOKEN_TEXT, nodeText, null));
  }

  private int getNextId() {
    int id = 0;
    for (int i = 0; ; i++) {
      String name = String.format("JSCompiler_object_inline_%d_", i);
      if (!getDeclaredVariables().containsKey(name)) {
        return i;
      }
    }
  }
}