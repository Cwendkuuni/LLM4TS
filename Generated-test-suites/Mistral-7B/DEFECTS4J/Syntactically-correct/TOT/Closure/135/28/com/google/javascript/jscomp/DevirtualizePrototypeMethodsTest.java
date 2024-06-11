import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Decompiler;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionSite;
import com.google.javascript.jscomp.SimpleUseSite;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link DevirtualizePrototypeMethods} class.
 */
public class DevirtualizePrototypeMethodsTest {

  private static final String CLASS_NAME = "DevirtualizePrototypeMethods";
  private static final String PROPERTY_NAME = "prototypeMethod";
  private static final String METHOD_NAME = "accumulate";
  private static final int ARGUMENTS_COUNT = 1;
  private static final int EXPECTED_STATIC_METHOD_INDEX = 0;
  private static final int EXPECTED_THIS_INDEX = 0;
  private static final String EXPECTED_STATIC_METHOD_NAME = "JSCompiler_StaticMethods_accumulate";

  private Node root;
  private Node externs;
  private Compiler compiler;
  private JSTypeRegistry typeRegistry;
  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() throws Exception {
    root = NodeFactory.compileString(
        "function A() {} " +
            "A.prototype.accumulate = function(value) { return this.total += value; }; " +
            "var a = new A(); " +
            "var total = a.accumulate(2);");
    externs = NodeFactory.compileString("{}");
    compiler = new Compiler();
    typeRegistry = compiler.getTypeRegistry();
    nodeFactory = compiler.getNodeFactory();
    options = new CompilerOptions();
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    // Given
    SimpleDefinitionFinder definitionFinder = new SimpleDefinitionFinder(compiler);
    definitionFinder.process(externs, root);

    SimpleDefinitionSite definitionSite = null;
    for (SimpleDefinitionSite site : definitionFinder.getDefinitionSites()) {
      if (site.node.toString().contains(PROPERTY_NAME) &&
          site.definition.getName().toString().contains(METHOD_NAME)) {
        definitionSite = site;
        break;
      }
    }

    assertNotNull(definitionSite);

    // When
    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    // Then
    Node rewrittenRoot = compiler.getOutput();
    assertNotNull(rewrittenRoot);

    Node methodNode = findMethodNodeByName(rewrittenRoot, EXPECTED_STATIC_METHOD_NAME);
    assertNotNull(methodNode);

    List<Node> methodArguments = methodNode.getChildren();
    assertEquals(ARGUMENTS_COUNT, methodArguments.size());

    Node thisArgument = methodArguments.get(EXPECTED_THIS_INDEX);
    assertEquals(Token.NAME, thisArgument.getType());
    assertEquals(EXPECTED_STATIC_METHOD_NAME + "$self", thisArgument.getString());

    Node methodBody = methodNode.getLastChild();
    String methodBodyString = methodBody.toString();
    assertTrue(methodBodyString.contains("this.total += value;"));

    Decompiler decompiler = new Decompiler();
    String expectedJavaCode = decompile(methodBody);
    String actualJavaCode = decompiler.decompile(methodNode);
    assertEquals(expectedJavaCode, actualJavaCode);
  }

  /**
   * Helper method to find a node with given name in the given node.
   */
  private Node findMethodNodeByName(Node node, String name) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.isFunction()) {
        String methodName = child.getFirstChild().getString();
        if (methodName.equals(name)) {
          return child;
        }
      } else if (child.isObject()) {
        for (Node method : child.getChildren()) {
          if (method.isFunction()) {
            String methodName = method.getFirstChild().getString();
            if (methodName.equals(name)) {
              return method;
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Helper method to decompile given node to Java code.
   */
  private String decompile(Node node) {
    Decompiler decompiler = new Decompiler();
    return decompiler.decompile(node).getCode();
  }
}