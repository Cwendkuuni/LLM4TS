package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.Option;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.BaseTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link InlineObjectLiterals}.
 */
public class InlineObjectLiteralsTest extends BaseTest {

  private static final CompilerPass INLINE_OBJECT_LITERALS =
      new InlineObjectLiterals(mockCompiler(), mockNameIdSupplier());

  private static final AbstractCompiler MOCK_COMPILER = mock(AbstractCompiler.class);
  private static final Supplier<String> MOCK_SAFE_NAME_ID_SUPPLIER = mock(
      Supplier.class);

  @Override
  protected void setup() {
    super.setup();
    when(MOCK_COMPILER.getCodingConvention()).thenReturn(mockCodingConvention());
  }

  @Override
  protected void setUp() {
    super.setUp();

    CompilerPassManager passManager = new CompilerPassManager(MOCK_COMPILER,
        new CompilerOptions());
    passManager.addPass(Option.INLINE_OBJECT_LITERALS, INLINE_OBJECT_LITERALS);
    COMPILER.setPassManager(passManager);
  }

  /**
   * Tests that a simple object literal inline passes.
   */
  public void testSimpleObjectLiteralInline() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: 1,"
        + "    b: 2,"
        + "    c: 3"
        + "  };"
        + "  x.a = 5;"
        + "  x.b = 6;"
        + "  x.c = 7;"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    Node result = COMPILER.compileSingleFile(externs, null, null);

    Node expected = parse("function f() {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var c = 3;"
        + "  return {"
        + "    a: a,"
        + "    b: b,"
        + "    c: c"
        + "  }"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline fails if the object literal
   * contains a self-referential property.
   */
  public void testSelfReferentialObjectLiteralInlineFailure() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: 1,"
        + "    b: x.a"
        + "  };"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    try {
      COMPILER.compileSingleFile(externs, null, null);
      fail("Expected an exception: Self-referential assignments are not "
          + "supported.");
    } catch (RuntimeException e) {
      assertEquals("Self-referential assignments are not supported.", e.getMessage());
    }
  }

  /**
   * Tests that an object literal inline fails if the object literal
   * contains a circular reference.
   */
  public void testCircularObjectLiteralInlineFailure() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: {b: x},"
        + "    b: 2"
        + "  };"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    try {
      COMPILER.compileSingleFile(externs, null, null);
      fail("Expected an exception: Circular references are not "
          + "supported.");
    } catch (RuntimeException e) {
      assertEquals("Circular references are not supported.", e.getMessage());
    }
  }

  /**
   * Tests that an object literal inline fails if the object literal
   * is assigned to a constant.
   */
  public void testConstantObjectLiteralAssignment() {
    String input = "function f() {"
        + "  const x = {"
        + "    a: 1,"
        + "    b: 2,"
        + "    c: 3"
        + "  };"
        + "  x.a = 5;"
        + "  x.b = 6;"
        + "  x.c = 7;"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    try {
      COMPILER.compileSingleFile(externs, null, null);
      fail("Expected an exception: Constants cannot be reassigned.");
    } catch (RuntimeException e) {
      assertEquals("Constants cannot be reassigned.", e.getMessage());
    }
  }

  /**
   * Tests that an object literal inline leaves a property access
   * unchanged.
   */
  public void testPropertyAccessUnchanged() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: 1,"
        + "    b: {"
        + "      c: 2"
        + "    }"
        + "  };"
        + "  return x.b.c;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    Node result = COMPILER.compileSingleFile(externs, null, null);

    Node expected = parse("function f() {"
        + "  return x.b.c;"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect simple variable
   * assignments.
   */
  public void testSimpleVariableAssignmentUnchanged() {
    String input = "function f() {"
        + "  var x = 1;"
        + "  var y = {"
        + "    a: 2"
        + "  };"
        + "  x = y.a;"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    Node result = COMPILER.compileSingleFile(externs, null, null);

    Node expected = parse("function f() {"
        + "  var x = 2;"
        + "  return x;"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect calls to
   * functions that use the object literal as a 'this' value.
   */
  public void testObjectLiteralAsThisValueUnchanged() {
    String input = "function Foo() {"
        + "  this.x = {"
        + "    a: 1,"
        + "    b: 2"
        + "  };"
        + "  this.m = function() {"
        + "    return this.x.a;"
        + "  };"
        + "}";

    Node externs = parse(input);
    Node root = new Node(Token.BLOCK, Collections.emptyList(), null);
    root.addChild(externs);

    Node result = COMPILER.compileSingleFile(externs, root, null);

    Node expected = parse("function Foo() {"
        + "  this.x = {"
        + "    a: 1,"
        + "    b: 2"
        + "  };"
        + "  this.m = function() {"
        + "    return this.x.a;"
        + "  };"
        + "  return this.m;"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect calls to
   * functions that use the object literal as an argument.
   */
  public void testObjectLiteralAsArgumentUnchanged() {
    String input = "function f(o) {"
        + "  return o.a;"
        + "}";

    Node externs = parse(input);
    Node root = new Node(Token.BLOCK, Collections.emptyList(), null);
    root.addChild(externs);

    Node object = new Node(Token.OBJECTLIT,
        List.of(new Node(Token.PROPS, List.of())), null);
    root.addChild(object);
    object.addChild(new Node(Token.PROP, List.of(Node.newString(Token.NAME, "a")),
        null));
    object.addChild(new Node(Token.PROP, List.of(Node.newString(Token.NAME, "b")),
        null));

    Node result = COMPILER.compileSingleFile(externs, root, null);

    Node expected = parse(input);

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect calls to
   * functions that use the object literal as a constructor.
   */
  public void testObjectLiteralAsConstructorUnchanged() {
    String input = "function Foo() {"
        + "  this.a = 1;"
        + "  this.b = 2;"
        + "}"
        + "function Bar() {"
        + "  this.x = new Foo();"
        + "}"
        + "new Bar();";

    Node externs = parse(input);
    Node root = new Node(Token.SCRIPT, Collections.emptyList(), null);
    root.addChild(externs);

    Node result = COMPILER.compileSingleFile(externs, root, null);

    Node expected = parse(input);

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect calls to
   * functions that use the object literal as a property.
   */
  public void testObjectLiteralAsPropertyUnchanged() {
    String input = "function f(o) {"
        + "  return o.x.a;"
        + "}";

    Node externs = parse(input);
    Node root = new Node(Token.BLOCK, Collections.emptyList(), null);
    root.addChild(externs);

    Node object = new Node(Token.OBJECTLIT,
        List.of(new Node(Token.PROP, List.of(Node.newString(Token.NAME, "x")),
            null), new Node(Token.PROP, List.of(Node.newString(Token.NAME, "a")),
            null)), null);

    root.addChild(object);

    Node result = COMPILER.compileSingleFile(externs, root, null);

    Node expected = parse(input);

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect variable
   * assignments to properties of the object literal.
   */
  public void testVariableAssignmentToObjectLiteralProperty() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: 1,"
        + "    b: 2"
        + "  };"
        + "  x.c = 3;"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    Node result = COMPILER.compileSingleFile(externs, null, null);

    Node expected = parse("function f() {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var c = 3;"
        + "  return {"
        + "    a: a,"
        + "    b: b,"
        + "    c: c"
        + "  }"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  /**
   * Tests that an object literal inline does not affect function
   * declarations inside the object literal.
   */
  public void testFunctionDeclarationInsideObjectLiteral() {
    String input = "function f() {"
        + "  var x = {"
        + "    a: 1,"
        + "    g: function() {"
        + "      return 1;"
        + "    }"
        + "  };"
        + "  return x;"
        + "}";

    Node externs = parse(input);
    Node root = externs;

    Node result = COMPILER.compileSingleFile(externs, null, null);

    Node expected = parse("function f() {"
        + "  var a = 1;"
        + "  var g = function() {"
        + "    return 1;"
        + "  }"
        + "  var x = {"
        + "    a: a,"
        + "    g: g"
        + "  }"
        + "  return x;"
        + "}");

    NodeUtil.assertNodesEqual(expected, result);
  }

  private static AbstractCompiler mockCompiler() {
    return MOCK_COMPILER;
  }

  private static Supplier<String> mockNameIdSupplier() {
    return MOCK_SAFE_NAME_ID_SUPPLIER;
  }

  private static CodingConvention mockCodingConvention() {
    return new CodingConvention() {
      @Override
      public boolean isExported(String name) {
        return false;
      }

      @Override
      public void visit(Node node, NodeVisitor visitor) {
        visitor.visit(node);
      }
    };
  }
}