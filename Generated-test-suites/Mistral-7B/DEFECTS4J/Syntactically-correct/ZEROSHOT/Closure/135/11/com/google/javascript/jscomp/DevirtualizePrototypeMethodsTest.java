import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.rhino.NodeUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private CompilerTestCase testCase;
  private SimpleDefinitionFinder definitionFinder;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = new CompilerPassManager(compiler);
    testCase = new CompilerTestCase();
    definitionFinder = new SimpleDefinitionFinder(compiler);
  }

  @org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    // Given
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    passManager.addPass(new DevirtualizePrototypeMethods(compiler));

    Definition definition = new Definition("a", Node.objectLiteral(), null);
    definition.setRValue(Node.functionExpression(null, null, "o", "a, b, c", null));
    DefinitionSite definitionSite = new DefinitionSite(NodeUtil.getLastChild(root), definition, null);

    definitionFinder.process(externs, root);
    rewriteDefinitionIfEligible(definitionSite, definitionFinder);

    // When
    Node result = passManager.process(externs, root, null);

    // Then
    assertNull(definitionSite.node.getFirstChild());
    Node function = definitionSite.node.getLastChild();
    Node newNameNode = function.getFirstChild();
    Node newName = newNameNode.getLastChild();
    Node argList = function.getFirstChild().getNext();
    Node thisArg = argList.getFirstChild();

    // newNameNode should have been added to root
    testCase.checkTopLevelDeclarations(result, Arrays.asList(newNameNode));
    // function should have been moved to top level
    assertNull(definitionSite.node.getParent());
    // newName should have been added to function
    assertNull(function.getPreviousSibling());
    // argList should have "this" replaced by newName
    Node expectedArgList = Node.newArrayLiteral(newName, Node.OBJECT_LITERAL);
    Node actualArgList = NodeUtil.deepClone(argList);
    NodeUtil.compareTrees(expectedArgList, actualArgList, testCase);
    // function type should have "this" replaced by newName
    FunctionType expectedFunctionType =
        new FunctionType(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), null, Arrays.asList(newName, NodeUtil.getObjectTypeFromNode(root), NodeUtil.getObjectTypeFromNode(root), NodeUtil.getObjectTypeFromNode(root)));
    FunctionType actualFunctionType = (FunctionType) function.getJSType();
    assertEquals(expectedFunctionType, actualFunctionType);
  }

  private void rewriteDefinitionIfEligible(DefinitionSite definitionSite, SimpleDefinitionFinder definitionFinder) {
    definitionFinder.getDefinition(definitionSite).setModule(null);
    process(externs, root);
    rewriteDefinitionIfEligible(definitionSite, definitionFinder);
  }

  private void process(Node externs, Node root) {
    definitionFinder.process(externs, root);
    passManager.runPasses(externs, root);
  }
}