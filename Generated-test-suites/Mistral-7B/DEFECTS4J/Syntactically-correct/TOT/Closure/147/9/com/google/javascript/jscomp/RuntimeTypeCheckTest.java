import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.RuntimeTypeCheck.ALPHA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.UnionType;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Unit tests for {@link RuntimeTypeCheck}.
 */
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testAddMarkersWithSimpleInterface() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {}" +
        "Foo.prototype.implements__Interface = true;" +
        "FooBar.prototype.implements__Interface = true;" +
        "function foo(x) {}" +
        "function foo(x) {" +
          "if (x instanceof Foo) {" +
            "return 42;" +
          "} else {" +
            "assertType('Foo', x, 'x');" +
            "return 0;" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddMarkersWithSimpleInterface");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(3, foo.getChildCount());

    Node markers = foo.getChild(2);
    assertEquals(2, markers.getChildCount());

    assertEquals("Foo", markers.getChild(0).getFirstChild().getString());
    assertEquals("Interface", markers.getChild(1).getFirstChild().getString());
  }

  @Test
  public void testAddMarkersWithMultipleInterfaces() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {}" +
        "Foo.prototype.implements__Interface1 = true;" +
        "Foo.prototype.implements__Interface2 = true;" +
        "FooBar.prototype.implements__Interface1 = true;" +
        "FooBar.prototype.implements__Interface3 = true;" +
        "function foo(x) {}" +
        "function foo(x) {" +
          "if (x instanceof Foo || x instanceof FooBar) {" +
            "return 42;" +
          "} else {" +
            "assertType('Foo', x, 'x');" +
            "assertType('FooBar', x, 'x');" +
            "return 0;" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddMarkersWithMultipleInterfaces");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(3, foo.getChildCount());

    Node markers = foo.getChild(2);
    assertEquals(4, markers.getChildCount());

    TreeSet<String> markerNames = Sets.newTreeSet();
    Iterables.addAll(markerNames, ALPHA);
    markerNames.add("implements__Interface1");
    markerNames.add("implements__Interface2");
    markerNames.add("implements__Interface3");

    for (Node marker : markers.getChildren()) {
      String markerName = marker.getFirstChild().getString();
      assertTrue(markerNames.contains(markerName));
    }
  }

  @Test
  public void testAddMarkersWithErrorScenario() throws IOException {
    String input = "function Foo() {" +
        "this.implementedInterface = true;" +
        "}" +
        "function FooBar() {}" +
        "function foo(x) {}" +
        "function foo(x) {" +
          "if (x instanceof Foo) {" +
            "return 42;" +
          "} else {" +
            "assertType('Foo', x, 'x');" +
            "return 0;" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddMarkersWithErrorScenario");
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Exception exception = assertThrows(RuntimeException.class, () -> {
      runtimeTypeCheck.process(null, root);
    });

    assertEquals("Class Foo does not have a prototype property.", exception.getMessage());
  }

  @Test
  public void testAddChecksWithValueTypes() throws IOException {
    String input = "function Foo() {}" +
        "function foo(x) {" +
          "assertEquals('number', typeof x, 'x is a number');" +
          "assertEquals(1, x, 'x is 1');" +
          "checkType(x, 'number');" +
          "assertEquals(1, x, 'x is still 1');" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddChecksWithValueTypes");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(4, foo.getChildCount());

    Node checkTypeCall = foo.getChild(3);
    assertEquals("checkType", checkTypeCall.getFirstChild().getString());
    assertEquals(2, checkTypeCall.getChildCount());

    Node valueChecker = checkTypeCall.getChild(1);
    assertEquals("number", valueChecker.getFirstChild().getString());

    Node check = checkTypeCall.getChild(2);
    assertTrue(NodeUtil.isFunctionCall(check));
    assertEquals("x", check.getFirstChild().getString());
  }

  @Test
  public void testAddChecksWithClassTypes() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {}" +
        "function Foo() {}" +
        "function foo(x) {" +
          "if (x instanceof Foo) {" +
            "assertEquals(Foo.class, x.constructor, 'x is an instance of Foo');" +
            "checkType(x, new ObjectType(Foo.class));" +
          "} else {" +
            "assertFalse(x instanceof Foo, 'x is not an instance of Foo');" +
            "checkType(x, new ObjectType(FooBar.class));" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddChecksWithClassTypes");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(6, foo.getChildCount());

    Node checkTypeCall1 = foo.getChild(3);
    assertSame(checkTypeCall1.getFirstChild(),
        NodeUtil.newQualifiedNameNode("Foo", -1, -1));

    Node checkTypeCall2 = foo.getChild(5);
    assertSame(checkTypeCall2.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooBar", -1, -1));
  }

  @Test
  public void testAddChecksWithInterfaceTypes() throws IOException {
    String input = "function Foo() {}" +
        "interface IFoo {" +
        "  method1();" +
        "}" +
        "function FooBar() {}" +
        "function Foo implements IFoo {" +
        "  method1() {" +
        "  }" +
        "}" +
        "function foo(x) {" +
          "assertEquals(IFoo.class, x.getClass(), 'x is an instance of IFoo');" +
          "checkType(x, new ObjectType(IFoo.class));" +
        "}" +
        "function bar(x) {" +
          "assertFalse(x instanceof IFoo, 'x is not an instance of IFoo');" +
          "checkType(x, new ObjectType(FooBar.class));" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddChecksWithInterfaceTypes");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(5, foo.getChildCount());

    Node checkTypeCall1 = foo.getChild(3);
    assertSame(checkTypeCall1.getFirstChild(),
        NodeUtil.newQualifiedNameNode("IFoo", -1, -1));

    Node checkTypeCall2 = foo.getChild(4);
    assertSame(checkTypeCall2.getFirstChild(),
        NodeUtil.newQualifiedNameNode("Foo", -1, -1));

    Node bar = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null).getNext();
    assertEquals(3, bar.getChildCount());

    Node checkTypeCall3 = bar.getChild(2);
    assertSame(checkTypeCall3.getFirstChild(),
        NodeUtil.newQualifiedNameNode("IFoo", -1, -1));

    Node checkTypeCall4 = bar.getChild(3);
    assertSame(checkTypeCall4.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooBar", -1, -1));
  }

  @Test
  public void testAddChecksWithUnionTypes() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {}" +
        "function FooOrBar() {}" +
        "function foo(x) {" +
          "if (x instanceof Foo || x instanceof FooOrBar) {" +
            "checkType(x, new UnionType(new ObjectType(Foo.class), new ObjectType(FooOrBar.class)));" +
          "} else {" +
            "assertFalse(x instanceof Foo, 'x is not an instance of Foo');" +
            "assertFalse(x instanceof FooOrBar, 'x is not an instance of FooOrBar');" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddChecksWithUnionTypes");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(6, foo.getChildCount());

    Node unionType = (UnionType) foo.getChild(3).getJSType();
    assertEquals(2, unionType.getAlternates().size());

    Node checkTypeCall1 = foo.getChild(3);
    assertSame(checkTypeCall1.getFirstChild(),
        NodeUtil.newQualifiedNameNode("Foo", -1, -1));

    Node checkTypeCall2 = foo.getChild(5);
    assertSame(checkTypeCall2.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooOrBar", -1, -1));
  }

  @Test
  public void testAddChecksWithNestedUnionTypes() throws IOException {
    String input = "function Foo() {}" +
        "function FooBar() {}" +
        "function FooOrBar() {}" +
        "function FooOrBaz() {}" +
        "function FooOrBarOrBaz() {}" +
        "function foo(x) {" +
          "if (x instanceof Foo || x instanceof FooOrBar || x instanceof FooOrBaz || x instanceof FooOrBarOrBaz) {" +
            "checkType(x, new UnionType(new ObjectType(Foo.class), new UnionType(new ObjectType(FooOrBar.class), new UnionType(new ObjectType(FooOrBaz.class), new ObjectType(FooOrBarOrBaz.class)))));" +
          "} else {" +
            "assertFalse(x instanceof Foo, 'x is not an instance of Foo');" +
            "assertFalse(x instanceof FooOrBar, 'x is not an instance of FooOrBar');" +
            "assertFalse(x instanceof FooOrBaz, 'x is not an instance of FooOrBaz');" +
            "assertFalse(x instanceof FooOrBarOrBaz, 'x is not an instance of FooOrBarOrBaz');" +
          "}" +
        "}";

    Node root = NodeUtil.parseJavascript(input, "testAddChecksWithNestedUnionTypes");
    new RuntimeTypeCheck(compiler, null).process(null, root);

    Node foo = Iterables.getFirst(NodeUtil.getFnDeclarations(root), null);
    assertEquals(8, foo.getChildCount());

    Node unionType = (UnionType) foo.getChild(3).getJSType();
    UnionType nestedUnionType = (UnionType) unionType.getAlternates().get(0);
    assertEquals(3, nestedUnionType.getAlternates().size());

    Node checkTypeCall1 = foo.getChild(3);
    assertSame(checkTypeCall1.getFirstChild(),
        NodeUtil.newQualifiedNameNode("Foo", -1, -1));

    Node checkTypeCall2 = foo.getChild(5);
    assertSame(checkTypeCall2.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooOrBar", -1, -1));

    Node checkTypeCall3 = foo.getChild(6);
    assertSame(checkTypeCall3.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooOrBaz", -1, -1));

    Node checkTypeCall4 = foo.getChild(7);
    assertSame(checkTypeCall4.getFirstChild(),
        NodeUtil.newQualifiedNameNode("FooOrBarOrBaz", -1, -1));
  }
}