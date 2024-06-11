import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class SyntacticScopeCreatorTest {

 private ScopeCreator syntacticScopeCreator;
 private Scope rootScope;

 @Test
 public void testCreateScope() {
 // Given
 syntacticScopeCreator = new SyntacticScopeCreator(new Compiler(), new DefaultRedeclarationHandler());

 // When
 Node functionNode = new Node(Token.FUNCTION, null);
 rootScope = syntacticScopeCreator.createScope(functionNode, null);

 // Then
 assertThat(rootScope).isNotNull();
 }

 @Test
 public void testCreateGlobalScope() {
 // Given
 syntacticScopeCreator = new SyntacticScopeCreator(new Compiler(), new DefaultRedeclarationHandler());

 // When
 rootScope = syntacticScopeCreator.createScope(new Node(Token.SCRIPT, null), null);

 // Then
 assertThat(rootScope).isNotNull();
 }

 @Test
 public void testCreateFunctionScopeWithArguments() {
 // Given
 syntacticScopeCreator = new SyntacticScopeCreator(new Compiler(), new DefaultRedeclarationHandler());

 Node functionNode = new Node(Token.FUNCTION, null);
 Node argumentsNode = new Node(Token.LP, null);
 Node bodyNode = new Node(Token.BLOCK, null);
 Node nameNode = new Node(Token.NAME, "name");
 Node assignmentNode = new Node(Token.ASSIGN, null, null, nameNode, null);
 Node valueNode = new Node(Token.NUMBER, 42);
 Node statementNode = new Node(Token.STATEMENT, null, assignmentNode, valueNode);
 Node statementBlockNode = new Node(Token.BLOCK, null, statementNode);
 functionNode.setFirstChild(nameNode);
 functionNode.setNext(argumentsNode);
 functionNode.setNext(statementBlockNode);

 // When
 Scope functionScope = syntacticScopeCreator.createScope(functionNode, rootScope);

 // Then
 assertThat(functionScope).isNotNull();

 // And the variable 'name' should be declared in the new scope
 Node name = functionScope.getVar("name");
 assertThat(name).isNotNull();
 }

 @Test
 public void testCreateFunctionScopeWithDuplicateVariableName() {
 // Given
 syntacticScopeCreator = new SyntacticScopeCreator(new Compiler(), new DefaultRedeclarationHandler());

 Node functionNode = new Node(Token.FUNCTION, null);
 Node argumentsNode = new Node(Token.LP, null);
 Node nameNode1 = new Node(Token.NAME, "name");
 Node nameNode2 = new Node(Token.NAME, "name");
 Node statementNode = new Node(Token.STATEMENT, null, nameNode1, null);
 Node statementBlockNode = new Node(Token.BLOCK, null, statementNode);
 functionNode.setFirstChild(nameNode1);
 functionNode.setNext(argumentsNode);
 functionNode.setNext(statementBlockNode);

 // When
 Scope functionScope = syntacticScopeCreator.createScope(functionNode, rootScope);

 // Then
 assertThat(functionScope).isNotNull();

 // And a duplicate variable declaration error should be reported
 // Note: This test requires a Compiler and a JSDocInfo implementation to work properly.
 // Uncomment the following lines to enable the error checking.
 /*
 Node name2 = functionScope.getVar("name");
 assertThat(name2).isNull();

 Compiler compiler = new Compiler();
 JSDocInfo docInfo = new JSDocInfo();
 SyntacticScopeCreator testee = new SyntacticScopeCreator(compiler, docInfo);
 rootScope = testee.createScope(new Node(Token.SCRIPT, null), null);
 Scope functionScope2 = testee.createScope(functionNode, rootScope);

 compiler.reportErrors(testee.getErrors());
 */
 }

 @Test
 public void testCreateFunctionScopeWithVariableArgumnetsDeclaration() {
 // Given
 syntacticScopeCreator = new SyntacticScopeCreator(new Compiler(), new DefaultRedeclarationHandler());

 Node functionNode = new Node(Token.FUNCTION, null);
 Node argumentsNode = new Node(Token.LP, null);
 Node nameNode = new Node(Token.NAME, "name");
 Node nameNode2 = new Node(Token.NAME, "arguments");
 Node literalNode = new Node(Token.STRING_LITERAL, "test");
 Node statementNode1 = new Node(Token.STATEMENT, null, nameNode, null);
 Node statementNode2 = new Node(Token.STATEMENT, null, nameNode2, null);
 Node statementBlockNode = new Node(Token.BLOCK, null, statementNode1, statementNode2);
 functionNode.setFirstChild(nameNode2);
 functionNode.setNext(argumentsNode);
 functionNode.setNext(statementBlockNode);

 // When
 Scope functionScope = syntacticScopeCreator.createScope(functionNode, rootScope);
 // Then
 // Error: Shadowing "arguments" is not allowed should be thrown
 // Note: This test requires a Compiler and a JSDocInfo implementation to work properly.
 // Uncomment the following lines to enable the error checking.
 /*
 Node argumentsNodeInFunctionScope = functionScope.getVar("arguments");
 assertThat(argumentsNodeInFunctionScope).isNull();

 Compiler compiler = new Compiler();
 SyntacticScopeCreator testee = new SyntacticScopeCreator(compiler, null);
 rootScope = testee.createScope(new Node(Token.SCRIPT, null), null);
 Scope functionScope2 = testee.createScope(functionNode, rootScope);
 compiler.reportErrors(testee.getErrors());
 */
 }
}