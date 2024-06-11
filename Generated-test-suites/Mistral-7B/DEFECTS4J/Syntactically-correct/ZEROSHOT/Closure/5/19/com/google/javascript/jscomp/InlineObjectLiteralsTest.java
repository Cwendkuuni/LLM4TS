import static org.junit.Assert.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Behavior;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SupplierFunction;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

public class InlineObjectLiteralsTest {

  private static final String VAR_PREFIX = "JSCompiler_object_inline_";

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  public InlineObjectLiteralsTest() {
    this.compiler = new Compiler();
    this.safeNameIdSupplier = new SupplierFunction<String>() {
      private int id = 0;

      @Override
      public String get() {
        return Integer.toString(++id);
      }
    };
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testInlineObjectLiterals() {
    String testCode =
        "function foo() {\n" +
        "  var obj1 = {a: 1, b: 2};\n" +
        "  var obj2 = {c: obj1, d: obj1.a};\n" +
        "  var obj3 = {e: obj1.b, f: obj1};\n" +
        "  var obj4 = {g: obj2, h: obj3};\n" +
        "  return obj4;\n" +
        "}";

    Node root = Node.parse(testCode);

    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    pass.process(Node.externalRoot(), root);

    Node result = compiler.getCompiledResult();

    // Check that the object literals have been inlined.
    assertFalse(result.isObjectLit());

    // Check that the inlined values have been given unique names.
    Map<String, String> expectedVarNames = newHashMap();
    expectedVarNames.put("a", VAR_PREFIX + "obj1_a_0");
    expectedVarNames.put("b", VAR_PREFIX + "obj1_b_1");
    expectedVarNames.put("c", VAR_PREFIX + "obj2_c_2");
    expectedVarNames.put("d", VAR_PREFIX + "obj2_d_3");
    expectedVarNames.put("e", VAR_PREFIX + "obj3_e_4");
    expectedVarNames.put("f", VAR_PREFIX + "obj3_f_5");
    expectedVarNames.put("g", VAR_PREFIX + "obj4_g_6");
    expectedVarNames.put("h", VAR_PREFIX + "obj4_h_7");

    Scope scope = root.getFirstChild().getLastChild();

    for (Node n = scope.getFirstChild(); n != null; n = n.getNextSibling()) {
      if (n.isVar()) {
        Var var = (Var) n;
        String varName = var.name.getString();
        String varValue = var.getInitialValue().getString();
        String expectedVarName = expectedVarNames.get(varName);
        assertEquals(expectedVarName, varValue);
      }
    }
  }

  private static class Compiler implements AbstractCompiler {

    @Override
    public void reportCodeChange() {
      // No-op
    }

    @Override
    public Node externalRoot() {
      return Node.NULL;
    }

    @Override
    public Node getCompiledResult() {
      return Node.NULL;
    }

    @Override
    public NodeTraversal createTraversal(Node root, Node externs) {
      return null;
    }

    @Override
    public void process(Node externs, Node root) {
      // No-op
    }

    @Override
    public void setCodingConvention(CodingConvention convention) {
      // No-op
    }

    @Override
    public void setOption(String key, Object value) {
      // No-op
    }

    @Override
    public void setWarningLevel(int warningLevel) {
      // No-op
    }

    @Override
    public void setWarningPrinter(WarningPrinter warningPrinter) {
      // No-op
    }

    @Override
    public void setSourceFile(String fileName) {
      // No-op
    }

    @Override
    public void setSourceOutputFile(String fileName) {
      // No-op
    }

    @Override
    public void setSourceOutputFormat(int format, int sourceMapVersion) {
      // No-op
    }

    @Override
    public void setSourceOutputMapFile(String fileName) {
      // No-op
    }
  }
}