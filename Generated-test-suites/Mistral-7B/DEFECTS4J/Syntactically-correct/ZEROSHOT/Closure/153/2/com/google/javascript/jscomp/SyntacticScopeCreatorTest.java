import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SyntacticScopeCreatorTest {

 @Mock
 private AbstractCompiler compiler;

 private ScopeCreator scopeCreator;
 private Scope rootScope;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 scopeCreator = new SyntacticScopeCreator(compiler);
 rootScope = new Scope(null);
 }

 @Test
 public void testCreateScope() {
 Node rootNode = Node.newNode(Token.SCRIPT, "test");
 Scope actualScope = scopeCreator.createScope(rootNode, rootScope);
 assertEquals(rootScope, actualScope.getParent());
 }

 @Test
 public void testCreateScopeWithRedeclarationHandler() {
 RedeclarationHandler redeclarationHandler = mock(RedeclarationHandler.class);
 ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
 Node rootNode = Node.newNode(Token.SCRIPT, "test");
 Scope actualScope = scopeCreator.createScope(rootNode, rootScope);
 assertEquals(rootScope, actualScope.getParent());
 }

 @Test
 public void testScanRootFunction() {
 String sourceName = "test.js";
 Node rootNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode = Node.newNameNode("foo");
 Node args = Node.newNode(Token.LP, null);
 Node body = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode);
 rootNode.appendChild(args);
 rootNode.appendChild(body);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());

 assertEquals(1, actualScope.getVars().size());
 Scope.Var declaredVar = actualScope.getVars().get(0);
 assertEquals("foo", declaredVar.getName());
 assertEquals(null, declaredVar.getInitializer());
 assertEquals(JSType.UNKNOWN, declaredVar.getType());
 assertEquals(null, declaredVar.getParentNode());
 assertEquals(null, declaredVar.getNode());
 assertEquals(sourceName, declaredVar.getInput().getName());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootFunctionWithDuplicateDeclaration() {
 String sourceName = "test.js";
 Node rootNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode1 = Node.newNameNode("foo");
 Node args1 = Node.newNode(Token.LP, null);
 Node body1 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode1);
 rootNode.appendChild(args1);
 rootNode.appendChild(body1);
 Node fnNameNode2 = Node.newNameNode("foo");
 Node args2 = Node.newNode(Token.LP, null);
 Node body2 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode2);
 rootNode.appendChild(args2);
 rootNode.appendChild(body2);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());

 assertEquals(1, actualScope.getVars().size());
 Scope.Var declaredVar = actualScope.getVars().get(0);
 assertEquals("foo", declaredVar.getName());
 assertEquals(null, declaredVar.getInitializer());
 assertEquals(JSType.UNKNOWN, declaredVar.getType());
 assertEquals(null, declaredVar.getParentNode());
 assertEquals(null, declaredVar.getNode());
 assertEquals(sourceName, declaredVar.getInput().getName());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootFunctionWithDuplicateDeclarationAndRedeclarationHandler() {
 String sourceName = "test.js";
 Node rootNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode1 = Node.newNameNode("foo");
 Node args1 = Node.newNode(Token.LP, null);
 Node body1 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode1);
 rootNode.appendChild(args1);
 rootNode.appendChild(body1);
 Node fnNameNode2 = Node.newNameNode("foo");
 Node args2 = Node.newNode(Token.LP, null);
 Node body2 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode2);
 rootNode.appendChild(args2);
 rootNode.appendChild(body2);
 Scope parentScope = new Scope(null);
 ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, mock(RedeclarationHandler.class));
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());

 assertEquals(1, actualScope.getVars().size());
 Scope.Var declaredVar = actualScope.getVars().get(0);
 assertEquals("foo", declaredVar.getName());
 assertEquals(null, declaredVar.getInitializer());
 assertEquals(JSType.UNKNOWN, declaredVar.getType());
 assertEquals(null, declaredVar.getParentNode());
 assertEquals(null, declaredVar.getNode());
 assertEquals(sourceName, declaredVar.getInput().getName());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootFunctionWithDuplicateDeclarationAndRedeclarationHandlerWithError() {
 String sourceName = "test.js";
 Node rootNode = Node.newFunctionNode(Token.FUNCTION, null, null, null, null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode1 = Node.newNameNode("foo");
 Node args1 = Node.newNode(Token.LP, null);
 Node body1 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode1);
 rootNode.appendChild(args1);
 rootNode.appendChild(body1);
 Node fnNameNode2 = Node.newNameNode("foo");
 Node args2 = Node.newNode(Token.LP, null);
 Node body2 = Node.newBlockNode(null);
 rootNode.appendChild(fnNameNode2);
 rootNode.appendChild(args2);
 rootNode.appendChild(body2);
 Scope parentScope = new Scope(null);
 RedeclarationHandler redeclarationHandler = mock(RedeclarationHandler.class);
 when(redeclarationHandler.onRedeclaration(any(Scope.class), eq("foo"), any(Node.class),
         any(Node.class), any(Node.class), any(Node.class)))
         .thenThrow(new RuntimeException());
 ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
 try {
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 fail("Expected an exception to be thrown.");
 } catch (RuntimeException e) {
 // expected
 }
 assertEquals(parentScope, actualScope.getParent());
 verify(compiler, never()).report(any(JSError.class));
 }

 @Test
 public void testScanRootFunctionWithVariableDeclaration() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode = Node.newNameNode("foo");
 Node assignmentNode = Node.newNameNode("foo");
 assignmentNode.setType(Token.ASSIGN);
 rootNode.appendChild(fnNameNode);
 rootNode.appendChild(assignmentNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());

 assertEquals(1, actualScope.getVars().size());
 Scope.Var declaredVar = actualScope.getVars().get(0);
 assertEquals("foo", declaredVar.getName());
 Node initializerNode = declaredVar.getInitializer();
 assertEquals(assignmentNode, initializerNode);
 assertEquals(JSType.UNKNOWN, declaredVar.getType());
 assertEquals(null, declaredVar.getParentNode());
 assertEquals(null, declaredVar.getNode());
 assertEquals(sourceName, declaredVar.getInput().getName());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootFunctionWithVariableDeclarationWithType() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node fnNameNode = Node.newNameNode("foo");
 Node assignmentNode = Node.newNameNode("foo");
 assignmentNode.setType(Token.ASSIGN);
 Node typeNode = Node.newNameNode("number");
 typeNode.setType(Token.TYPE);
 rootNode.appendChild(fnNameNameNode);
 rootNode.appendChild(assignmentNode);
 rootNode.appendChild(typeNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());

 assertEquals(1, actualScope.getVars().size());
 Scope.Var declaredVar = actualScope.getVars().get(0);
 assertEquals("foo", declaredVar.getName());
 Node initializerNode = declaredVar.getInitializer();
 assertEquals(assignmentNode, initializerNode);
 JSType variableType = declaredVar.getType();
 assertEquals(JSType.NUMBER, variableType);
 assertEquals(null, declaredVar.getParentNode());
 assertEquals(null, declaredVar.getNode());
 assertEquals(sourceName, declaredVar.getInput().getName());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootScript() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootWithControlStructures() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node ifNode = Node.newIfStatement(null, null, null);
 Node ifStatementElseNode = ifNode.getElseNode();
 Node elseifNode = ifStatementElseNode.getFirstChild();
 Node elseNode = ifStatementElseNode.getNext();
 rootNode.appendChild(ifNode);
 rootNode.appendChild(elseifNode);
 rootNode.appendChild(elseNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }

 @Test
 public void testScanRootWithStatementBlocks() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node statementBlock = Node.newBlockNode(null);
 rootNode.appendChild(statementBlock);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }

 @Test
 public void testScanChildWithControlStructures() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node ifNode = Node.newIfStatement(null, null, null);
 Node ifStatementElseNode = ifNode.getElseNode();
 Node elseifNode = ifStatementElseNode.getFirstChild();
 Node elseNode = ifStatementElseNode.getNext();
 Node childNode = Node.newNameNode("foo");
 rootNode.appendChild(ifNode);
 rootNode.appendChild(elseifNode);
 rootNode.appendChild(elseNode);
 rootNode.appendChild(childNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }

 @Test
 public void testScanChildWithStatementBlocks() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node statementBlock = Node.newBlockNode(null);
 Node childNode = Node.newNameNode("foo");
 rootNode.appendChild(statementBlock);
 rootNode.appendChild(childNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }

 @Test
 public void testScanChildWithVariableDeclarations() {
 String sourceName = "test.js";
 Node rootNode = Node.newBlockNode(null);
 rootNode.setProp(Node.SOURCENAME_PROP, sourceName);
 Node statementBlock = Node.newBlockNode(null);
 Node childNode = Node.newNameNode("foo");
 Node assignmentNode1 = Node.newNameNode("foo");
 assignmentNode1.setType(Token.ASSIGN);
 Node assignmentNode2 = Node.newNameNode("bar");
 assignmentNode2.setType(Token.ASSIGN);
 statementBlock.appendChild(assignmentNode1);
 statementBlock.appendChild(assignmentNode2);
 rootNode.appendChild(statementBlock);
 rootNode.appendChild(childNode);
 Scope parentScope = new Scope(null);
 Scope actualScope = scopeCreator.createScope(rootNode, parentScope);
 assertEquals(parentScope, actualScope.getParent());
 assertEquals(0, actualScope.getVars().size());
 verify(compiler).report(never());
 }
}

interface RedeclarationHandler {
 void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber);
}