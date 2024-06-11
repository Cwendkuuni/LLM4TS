import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckLevel;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;

 @Before
 public void setUp() {
 compiler = new Compiler();
 checker = new Checker(new AbstractCompiler() {
 @Override
 public Compiler getCompiler() {
 return compiler;
 }
 }, CheckLevel.WARNINGS);
 checker.addProcessor(new CheckGlobalThis(checker, CheckLevel.WARNINGS));
 }

 @Test
 public void testSafeUsage() {
 Node node = Node.newNode(Token.FUNCTION, new int[] {Token.FUNCTION, Token.LBRACE}, new int[] {Token.RBRACE}, null);
 Node result = checker.check(node, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testConstructorAnnotation() {
 Node constructorNode = Node.newNode(Token.FUNCTION, new int[] {Token.FUNCTION, Token.LBRACE, Token.KEYWORD, Token.IDENTIFIER, Token.RBRACE}, new int[] {Token.RBRACE}, null);
 constructorNode.setJSDocInfo(new JSDocInfo(true, null, null, null, null));
 Node result = checker.check(constructorNode, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testThisAnnotation() {
 Node thisAnnotatedNode = Node.newNode(Token.FUNCTION, new int[] {Token.FUNCTION, Token.IDENTIFIER, Token.LPAREN, Token.RPAREN, Token.LBRACE, Token.KEYWORD, Token.IDENTIFIER, Token.COLON, Token.AT_KEYWORD, Token.IDENTIFIER, Token.RPAREN, Token.LBRACE, Token.RBRACE}, new int[] {Token.RBRACE}, null);
 thisAnnotatedNode.setJSDocInfo(new JSDocInfo(null, null, "@this", null, null));
 Node result = checker.check(thisAnnotatedNode, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testFunctionWithNoAnnotation() {
 Node functionNode = Node.newNode(Token.FUNCTION, new int[] {Token.FUNCTION, Token.IDENTIFIER, Token.LPAREN, Token.RPAREN, Token.LBRACE, Token.RBRACE}, new int[] {Token.RBRACE}, null);
 Node result = checker.check(functionNode, null);
 assertFalse(result.getDiagnostics().isEmpty());
 assertTrue(result.getDiagnostics().size() == 1);
 assertEquals(DiagnosticType.WARNING, result.getDiagnostics().get(0).getType());
 }

 @Test
 public void testAssignmentToLeftSide() {
 Node assignNode = Node.newNode(Token.ASSIGN, new int[] {Token.NAME, Token.IDENTIFIER, Token.OPERATOR, Token.THIS}, new int[] {Token.SEMICOLON}, null);
 Node leftSide = assignNode.getFirstChild();
 Node result = checker.check(leftSide, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testPropertyAccessOnLeftSide() {
 Node assignNode = Node.newNode(Token.ASSIGN, new int[] {Token.NAME, Token.IDENTIFIER, Token.OPERATOR, Token.THIS}, new int[] {Token.SEMICOLON}, null);
 Node propertyAccess = assignNode.getLastChild();
 Node propertyName = propertyAccess.getFirstChild();
 propertyName.setQualifiedName("prototype");
 Node result = checker.check(propertyName, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testNestedAssignment() {
 Node assignNode = Node.newNode(Token.ASSIGN, new int[] {Token.NAME, Token.IDENTIFIER, Token.OPERATOR, Token.THIS}, new int[] {Token.LPAREN, Token.NAME, Token.IDENTIFIER, Token.RPAREN, Token.SEMICOLON}, null);
 Node nestedAssign = assignNode.getLastChild();
 Node nestedAssignPropertyAccess = nestedAssign.getLastChild();
 Node nestedAssignPropertyName = nestedAssignPropertyAccess.getFirstChild();
 nestedAssignPropertyName.setQualifiedName("prototype");
 Node result = checker.check(nestedAssignPropertyName, null);
 assertTrue(result.getDiagnostics().isEmpty());
 }

 @Test
 public void testGlobalThis() {
 Node globalThisNode = Node.newNode(Token.THIS, null, null, null);
 Node result = checker.check(globalThisNode, null);
 assertFalse(result.getDiagnostics().isEmpty());
 assertTrue(result.getDiagnostics().size() == 1);
 assertEquals(DiagnosticType.WARNING, result.getDiagnostics().get(0).getType());
 }
}