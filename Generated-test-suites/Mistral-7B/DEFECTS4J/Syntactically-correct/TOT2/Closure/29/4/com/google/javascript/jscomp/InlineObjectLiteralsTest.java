import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  public static void main(String[] args) throws Exception {
    runTest(InlineObjectLiteralsTest.class);
  }

  public InlineObjectLiteralsTest() throws Exception {
    super(new InlineObjectLiterals(getDefaultCompiler(), getNameIdSupplier()));
  }

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(getDefaultCompiler(), getNameIdSupplier());
  }

  /**
   * Test case for the basic functionality of InlineObjectLiterals pass.
   */
  public void testInlineObjectLiteralsBasic() throws Exception {
    String source = "function f() {" +
        "  var o = {" +
        "    a: 1" +
        "  };" +
        "  var x = o.a;" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }

  /**
   * Test case for the edge case where object literals have properties with the same name.
   */
  public void testInlineObjectLiteralsSamePropertyName() throws Exception {
    String source = "function f() {" +
        "  var o1 = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  var o2 = {" +
        "    a: 3," +
        "    b: 4" +
        "  };" +
        "  var x = o1.a;" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }

  /**
   * Test case for the edge case where keys in the object literal are not valid identifiers.
   */
  public void testInlineObjectLiteralsInvalidKeys() throws Exception {
    String source = "function f() {" +
        "  var o = {" +
        "    [1]: 1," +
        "    a-b: 2" +
        "  };" +
        "  var x = o[1];" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }

  /**
   * Test case for the edge case where object literals are nested.
   */
  public void testInlineObjectLiteralsNested() throws Exception {
    String source = "function f() {" +
        "  var o1 = {" +
        "    a: {" +
        "      b: 1" +
        "    }" +
        "  };" +
        "  var x = o1.a.b;" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }

  /**
   * Test case for the edge case where an object literal is used as a parameter.
   */
  public void testInlineObjectLiteralsAsParameter() throws Exception {
    String source = "function f(o) {" +
        "  return o.a;" +
        "}" +
        "function g() {" +
        "  return f({a: 1});" +
        "}" +
        "g();";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }

  /**
   * Test case for the edge case where an object literal is used as a return value.
   */
  public void testInlineObjectLiteralsAsReturnValue() throws Exception {
    String source = "function f() {" +
        "  return {" +
        "    a: 1" +
        "  }";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return {" +
        "  a: 1" +
        "};", root);
  }

  /**
   * Test case for the edge case where an object literal is
   * referenced before it is defined.
   */
  public void testInlineObjectLiteralsBeforeDefined() throws Exception {
    String source = "function f() {" +
        "  var x = o.a;" +
        "  var o = {" +
        "    a: 1" +
        "  };" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    expectException("ReferenceError: o is not defined", root);
  }

  /**
   * Test case for the edge case where an object literal is assigned
   * a circular reference.
   */
  public void testInlineObjectLiteralsCircularReference() throws Exception {
    String source = "function f() {" +
        "  var o1 = {" +
        "    a: o2" +
        "  };" +
        "  var o2 = {" +
        "    a: o1" +
        "  };" +
        "  return o1;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    expectException("Circular structure detected", root);
  }

  /**
   * Test case for the edge case where an object literal is assigned
   * an object with a self-referential property.
   */
  public void testInlineObjectLiteralsSelfReferentialProperty() throws Exception {
    String source = "function f() {" +
        "  var o = {" +
        "    x: {" +
        "      y: o" +
        "    }" +
        "  };" +
        "  return o.x.y;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    expectException("Circular structure detected", root);
  }

  /**
   * Test case for the edge case where an object literal is assigned
   * a property with a self-referential value.
   */
  public void testInlineObjectLiteralsSelfReferentialValue() throws Exception {
    String source = "function f() {" +
        "  var o = {" +
        "    a: {}" +
        "  };" +
        "  o.a.a = o;" +
        "  return o.a;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    expectException("Circular structure detected", root);
  }

  /**
   * Test case for the edge case where an object literal is defined in a try-catch block.
   */
  public void testInlineObjectLiteralsInTryCatch() throws Exception {
    String source = "function f() {" +
        "  try {" +
        "    var o = {" +
        "      a: 1" +
        "    };" +
        "  } catch (e) {" +
        "    // do nothing" +
        "  }" +
        "  var x = o.a;" +
        "  return x;" +
        "}";

    Node root = parse(source);
    runPasses(root);
    assertCompilesTo("return 1;", root);
  }
}