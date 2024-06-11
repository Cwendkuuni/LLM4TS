import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeType.ASSIGN;
import static com.google.javascript.jscomp.NodeType.BLOCK;
import static com.google.javascript.jscomp.NodeType.NAME;
import static com.google.javascript.jscomp.NodeType.SCRIPT;
import static com.google.javascript.jscomp.NodeType.STRING_KEY;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  private Checker checker;
  private AbstractCompiler compiler;
  private Node root;
  private SourceFile sourceFile;

  @Mock
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    compiler.addPasses(new CheckGlobalThis(compiler));
    checker = compiler.getChecker();

    sourceFile = new SourceFile("Test.js", "test");
    root = NodeUtil.parseJsDocLess(sourceFile.getText(), null, null);
  }

  @Test
  public void testNoGlobalThis() {
    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node functionCallNode = new Node(NAME, null, null, null, null, null, functionNode, null);
    Node functionCallPropertyAccessNode = new Node(NAME, null, null, "call", null, null, functionCallNode, null);
    Node functionCallPropertyAccessAssignmentNode = new Node(ASSIGN, null, null, null, null, null, functionCallPropertyAccessNode, null);
    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    blockNode.appendChild(functionCallPropertyAccessAssignmentNode);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testGlobalThisOnLeftSideOfAssignment() {
    Node assignmentNode = new Node(ASSIGN, null, null, null, null, null, null, null);
    Node thisNode = new Node(Token.THIS, null, null, null, null, null, null, null);
    assignmentNode.appendChild(thisNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);
    blockNode.appendChild(assignmentNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(checker.getDiagnostics().containsKey(diagnosticType));
  }

  @Test
  public void testGlobalThisOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = new Node(NAME, null, null, "prototype", null, null, null, null);
    Node thisNode = new Node(Token.THIS, null, null, null, null, null, null, null);
    Node parentPropertyAccessNode = new Node(NAME, null, null, "prototype", null, null, thisNode, null);
    Node parentPropertyAccessNodeStringKey = new Node(NAME, null, STRING_KEY, "prototype", null, null, parentPropertyAccessNode, null);
    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    blockNode.appendChild(parentPropertyAccessNodeStringKey);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertTrue(checker.getDiagnostics().containsKey(diagnosticType));
  }

  @Test
  public void testConstructorFunction() {
    Node constructorFunctionNode = new Node(NAME, null, null, "constructor", null, null, null, null);
    Node constructorFunctionJsDocInfo = new Node(JSDocInfo.CONSTRUCTOR, null, null, null, null, null, constructorFunctionNode, null);
    constructorFunctionNode.setJSDocInfo(constructorFunctionJsDocInfo);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node thisAnnotationNode = new Node(JSDocInfo.THIS, null, null, null, null, null, functionNode, null);
    functionNode.setJSDocInfo(thisAnnotationNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionInsideObjectLiteral() {
    Node objectLiteralNode = new Node(NAME, null, null, null, null, null, null, null);
    Node propertyNameNode = new Node(NAME, null, null, "x", null, null, null, null);
    Node valueNode = new Node(NAME, null, null, "function() {}", null, null, null, null);
    Node propertyValueNode = new Node(NAME, null, null, null, null, null, valueNode, null);
    objectLiteralNode.appendChild(propertyNameNode);
    objectLiteralNode.appendChild(propertyValueNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);
    blockNode.appendChild(objectLiteralNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionLentToPrototype() {
    Node objectLiteralNode = new Node(NAME, null, null, null, null, null, null, null);
    Node prototypePropertyNameNode = new Node(NAME, null, null, "prototype", null, null, null, null);
    Node prototypePropertyValueNode = new Node(NAME, null, null, "function() {}", null, null, null, null);
    Node prototypePropertyValueNodeJsDocInfo = new Node(JSDocInfo.LENDS, null, null, null, null, null, prototypePropertyValueNode, null);
    prototypePropertyValueNode.setJSDocInfo(prototypePropertyValueNodeJsDocInfo);
    Node prototypePropertyNameNodeJsDocInfo = new Node(JSDocInfo.PROPERTY, null, null, null, null, null, prototypePropertyNameNode, null);
    prototypePropertyNameNode.setJSDocInfo(prototypePropertyNameNodeJsDocInfo);
    Node prototypeNameNode = new Node(NAME, null, null, "prototype", null, null, null, null);
    prototypeNameNode.setJSDocInfo(new Node(JSDocInfo.OBJECT, null, null, null, null, null, prototypeNameNode, null));
    objectLiteralNode.appendChild(prototypeNameNode);
    objectLiteralNode.appendChild(prototypePropertyNameNode);
    objectLiteralNode.appendChild(prototypePropertyValueNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);
    blockNode.appendChild(objectLiteralNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);
    blockNode.appendChild(functionNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionAnnotatedWithOverride() {
    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node overrideAnnotationNode = new Node(JSDocInfo.OVERRIDE, null, null, null, null, null, functionNode, null);
    functionNode.setJSDocInfo(overrideAnnotationNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionAnnotatedWithInterface() {
    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node interfaceAnnotationNode = new Node(JSDocInfo.INTERFACE, null, null, null, null, null, functionNode, null);
    functionNode.setJSDocInfo(interfaceAnnotationNode);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithPrototypeMethod() {
    Node prototypeNode = new Node(NAME, null, null, null, null, null, null, null);
    prototypeNode.setJSDocInfo(new Node(JSDocInfo.PROTOTYPE, null, null, null, null, null, prototypeNode, null));

    Node functionNode = new Node(NAME, null, null, null, null, null, null, null);
    Node prototypeFunctionJsDocInfo = new Node(JSDocInfo.METHOD, null, null, null, null, null, functionNode, null);
    functionNode.setJSDocInfo(prototypeFunctionJsDocInfo);

    Node blockNode = new Node(BLOCK, null, null, null, null, null, null, null);
    root.appendChild(blockNode);
    blockNode.appendChild(prototypeNode);
    prototypeNode.appendChild(functionNode);

    NodeTraversal t = new NodeTraversal(checker, root, traversal);
    checker.process(t);

    assertTrue(checker.getDiagnostics().isEmpty());
  }
}