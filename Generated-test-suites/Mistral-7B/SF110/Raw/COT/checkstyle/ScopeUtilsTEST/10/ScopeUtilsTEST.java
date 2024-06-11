import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode() {
 return new DetailAST();
 }

 @Test
 public void testGetScopeFromMods_Public() {
 DetailAST mods = createTestNode();
 DetailAST publicToken = createTestNode();
 publicToken.setText("public");
 mods.addChild(publicToken);
 Scope expected = PUBLIC;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_Protected() {
 DetailAST mods = createTestNode();
 DetailAST protectedToken = createTestNode();
 protectedToken.setText("protected");
 mods.addChild(protectedToken);
 Scope expected = PROTECTED;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetScopeFromMods_Private() {
 DetailAST mods = createTestNode();
 DetailAST privateToken = createTestNode();
 privateToken.setText("private");
 mods.addChild(privateToken);
 Scope expected = PRIVATE;
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetScopeFromMods_NoModifiers() {
 DetailAST mods = createTestNode();
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 fail("Expected IllegalStateException but got: " + actual);
 }

 @Test
 public void testGetScopeFromMods_MultipleModifiers() {
 DetailAST mods = createTestNode();
 DetailAST publicToken = createTestNode();
 publicToken.setText("public");
 DetailAST protectedToken = createTestNode();
 protectedToken.setText("protected");
 DetailAST privateToken = createTestNode();
 privateToken.setText("private");
 mods.addChild(publicToken);
 mods.addChild(protectedToken);
 mods.addChild(privateToken);
 Scope expected = PUBLIC; // assume public takes precedence
 Scope actual = ScopeUtils.getScopeFromMods(mods);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Class() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(15); // TypeDeclaration
 node.setParent(parent);
 DetailAST mods = parent.findFirstToken(5);
 Scope expected = PACKAGE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Method() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 DetailAST mods = parent.findFirstToken(5);
 Scope expected = CLASS;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Variable() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(9); // VariableDeclaration
 node.setParent(parent);
 Scope expected = CLASS;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_Annotation() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(157); // Annotation
 node.setParent(parent);
 Scope expected = ANNOTATION;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_InnerClass() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(136); // AnonymousInnerClassDeclaration
 node.setParent(parent);
 Scope expected = ANONINNER;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_EmptyParent() {
 DetailAST node = createTestNode();
 Scope expected = PACKAGE;
 Scope actual = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSurroundingScope_NullNode() {
 Scope actual = ScopeUtils.getSurroundingScope(null);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testIsInInterfaceBlock_Interface() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(15); // InterfaceDeclaration
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock_Class() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_Annotation() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(157); // Annotation
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInAnnotationBlock_Class() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Interface() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(15); // InterfaceDeclaration
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Annotation() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(157); // Annotation
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_Class() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_Enum() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(154); // EnumDeclaration
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInEnumBlock_Class() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isInEnumBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_CodeBlock() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(9); // MethodDeclaration
 DetailAST body = createTestNode();
 body.setType(11); // Block
 node.setParent(body);
 parent.addChild(body);
 boolean expected = true;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInCodeBlock_NonCodeBlock() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isInCodeBlock(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_OuterMostType() {
 DetailAST node = createTestNode();
 boolean expected = true;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsOuterMostType_NonOuterMostType() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isOuterMostType(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariable() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(7); // VariableDeclaration
 node.setParent(parent);
 boolean expected = true;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsLocalVariableDef_NonLocalVariable() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 boolean expected = false;
 boolean actual = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_ClassField() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 DetailAST field = createTestNode();
 field.setType(10); // FieldDeclaration
 node.setChild(field);
 boolean expected = true;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsClassFieldDef_NonClassField() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(14); // ClassDeclaration
 node.setParent(parent);
 DetailAST method = createTestNode();
 method.setType(14); // MethodDeclaration
 node.setChild(method);
 boolean expected = false;
 boolean actual = ScopeUtils.isClassFieldDef(node);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInScope_InScope() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(15); // InterfaceDeclaration
 DetailAST mods = parent.findFirstToken(5);
 DetailAST publicToken = createTestNode();
 publicToken.setText("public");
 mods.addChild(publicToken);
 DetailAST ast = createTestNode();
 ast.setParent(node);
 Scope expected = PUBLIC;
 boolean actual = ScopeUtils.isInScope(ast, expected);
 assertTrue(actual);
 }

 @Test
 public void testIsInScope_NotInScope() {
 DetailAST node = createTestNode();
 DetailAST parent = createTestNode();
 parent.setType(15); // InterfaceDeclaration
 DetailAST mods = parent.findFirstToken(5);
 DetailAST publicToken = createTestNode();
 publicToken.setText("public");
 mods.addChild(publicToken);
 DetailAST ast = createTestNode();
 ast.setParent(node);
 Scope expected = PROTECTED;
 boolean actual = ScopeUtils.isInScope(ast, expected);
 assertFalse(actual);
 }
}