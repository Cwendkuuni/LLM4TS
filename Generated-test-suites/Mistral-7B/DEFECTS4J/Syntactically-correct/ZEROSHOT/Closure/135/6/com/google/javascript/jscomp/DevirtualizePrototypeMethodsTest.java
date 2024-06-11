import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.compileWithPasses;
import static com.google.javascript.jscomp.CompilerPasses.DEVIRTUALIZE_PROTOTYPE_METHODS;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.common.collect.Lists.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.TypeFactory;

public class DevirtualizePrototypeMethodsTest {
  private Compiler compiler;
  private AbstractCompiler abstractCompiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler();
    abstractCompiler = new AbstractCompiler(options);
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    Node root = parseJavaScript("" +
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value };\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n");

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(abstractCompiler);
    defFinder.process(NULL_NODE, root);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    assertEquals(1, definitionSites.size());

    DefinitionSite definitionSite = definitionSites.get(0);
    Node definitionNode = definitionSite.node;

    CompilerPass pass = new DevirtualizePrototypeMethods(abstractCompiler);
    CompilerPasses passes = compileWithPasses(root, ImmutableMap.of(DEVIRTUALIZE_PROTOTYPE_METHODS, pass));

    CompilerResults results = passes.getResults();
    Node transformedRoot = results.getCompiledSource();

    Node transformedDefinitionNode = findNodeRecursively(definitionNode, transformedRoot, false);
    assertNotNull(transformedDefinitionNode);

    String transformedMethodName = getTransformedMethodName(definitionNode, transformedRoot);
    assertEquals("JSCompiler_StaticMethods_accumulate", transformedMethodName);

    Node transformedCallSiteNode = findNodeRecursively(definitionSite.useSite.node, transformedRoot, false);
    assertNotNull(transformedCallSiteNode);

    Node transformedCallSiteFunctionNode = transformedCallSiteNode.getFirstChild();
    Node transformedCallSiteThisNode = transformedCallSiteFunctionNode.getFirstChild();
    assertEquals("a", transformedCallSiteThisNode.getString());

    Node transformedCallSiteMethodNode = transformedCallSiteFunctionNode.getLastChild();
    assertEquals(transformedMethodName, transformedCallSiteMethodNode.getString());

    FunctionType transformedFunctionType = (FunctionType) transformedCallSiteFunctionNode.getJSType();
    assertEquals(2, transformedFunctionType.getParameters().size());
    assertEquals(JSTypeNative.ANY, transformedFunctionType.getParameters().get(0).getType());
    assertEquals(JSTypeNative.NUMBER, transformedFunctionType.getParameters().get(1).getType());
    assertEquals(JSTypeNative.NUMBER, transformedFunctionType.getReturnType());
  }

  private String getTransformedMethodName(Node definitionNode, Node transformedRoot) {
    String methodName = definitionNode.getLastChild().getString();
    String rewrittenMethodName = new DevirtualizePrototypeMethods(abstractCompiler.getAbstractCompiler()).getRewrittenMethodName(methodName);
    Node transformedMethodDefinitionNode = findNodeRecursively(definitionNode, transformedRoot, false);
    return transformedMethodDefinitionNode.getLastChild().getString();
  }

  private Node findNodeRecursively(Node node, Node root, boolean followLinks) {
    if (node == root) {
      return node;
    }
    if (node.getParent() == null) {
      return null;
    }
    if (followLinks && node.getJSType() instanceof ObjectType &&
        node.getJSType().getJSTypeOfProperty("$link").isFunction()) {
      return ((FunctionType) node.getJSType().getJSTypeOfProperty("$link")).getBody();
    }
    return findNodeRecursively(node.getParent(), root, true);
  }
}