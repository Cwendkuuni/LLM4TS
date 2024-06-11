import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode() {
 // Create a test DetailAST node for the methods in ScopeUtils class
 return new DetailAST();
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST mods = createTestNode();
 mods.setType(5); // Set type as Modifier
 DetailAST scopeFromMods = ScopeUtils.getScopeFromMods(mods);

 assertEquals(Scope.PACKAGE, scopeFromMods);
 
 DetailAST publicMod = createTestNode();
 publicMod.setText("public");
 mods.addChild(publicMod);
 DetailAST scopeFromPublicMod = ScopeUtils.getScopeFromMods(mods);

 assertEquals(Scope.PUBLIC, scopeFromPublicMod);

 DetailAST protectedMod = createTestNode();
 protectedMod.setText("protected");
 mods.addChild(protectedMod);
 DetailAST scopeFromProtectedMod = ScopeUtils.getScopeFromMods(mods);

 assertEquals(Scope.PROTECTED, scopeFromProtectedMod);

 DetailAST privateMod = createTestNode();
 privateMod.setText("private");
 mods.addChild(privateMod);
 DetailAST scopeFromPrivateMod = ScopeUtils.getScopeFromMods(mods);

 assertEquals(Scope.PRIVATE, scopeFromPrivateMod);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromModsNullMods() {
 ScopeUtils.getScopeFromMods(null);
 }

 @Test
 public void testGetScopeFromModsEmptyMods() {
 DetailAST mods = createTestNode();
 DetailAST scopeFromEmptyMods = ScopeUtils.getScopeFromMods(mods);

 assertEquals(Scope.PACKAGE, scopeFromEmptyMods);
 }

 @Test
 public void testGetSurroundingScope() {
 DetailAST node = createTestNode();
 DetailAST surroundingScope = ScopeUtils.getSurroundingScope(node);

 assertNull(surroundingScope);

 DetailAST parent1 = createTestNode();
 parent1.setType(14); // Set type as ClassDecl
 parent1.addChild(node);
 DetailAST scopeFromParent1 = ScopeUtils.getSurroundingScope(node);

 assertEquals(Scope.CLASS, scopeFromParent1);

 DetailAST parent2 = createTestNode();
 parent2.setType(15); // Set type as InterfaceDecl
 parent2.addChild(parent1);
 parent2.addChild(node);
 DetailAST scopeFromParent2 = ScopeUtils.getSurroundingScope(node);

 assertEquals(Scope.INTERFACE, scopeFromParent2);

 DetailAST parent3 = createTestNode();
 parent3.setType(154); // Set type as Enum
 parent3.addChild(parent2);
 parent3.addChild(node);
 DetailAST scopeFromParent3 = ScopeUtils.getSurroundingScope(node);

 assertEquals(Scope.ENUM, scopeFromParent3);

 DetailAST parent4 = createTestNode();
 parent4.setType(157); // Set type as Annotation
 parent4.addChild(parent3);
 parent4.addChild(node);
 DetailAST scopeFromParent4 = ScopeUtils.getSurroundingScope(node);

 assertEquals(Scope.ANNOTATION, scopeFromParent4);

 DetailAST parent5 = createTestNode();
 parent5.setType(136); // Set type as AnonymousClassDeclaration
 parent5.addChild(node);
 DetailAST scopeFromParent5 = ScopeUtils.getSurroundingScope(node);

 assertEquals(Scope.ANONINNER, scopeFromParent5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSurroundingScopeNullNode() {
 ScopeUtils.getSurroundingScope(null);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createTestNode();
 boolean inInterfaceBlock = ScopeUtils.isInInterfaceBlock(node);

 assertFalse(inInterfaceBlock);

 DetailAST parent1 = createTestNode();
 parent1.setType(15); // Set type as InterfaceDecl
 parent1.addChild(node);
 boolean inInterfaceBlockParent1 = ScopeUtils.isInInterfaceBlock(node);

 assertTrue(inInterfaceBlockParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInInterfaceBlockNullNode() {
 ScopeUtils.isInInterfaceBlock(null);
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createTestNode();
 boolean inAnnotationBlock = ScopeUtils.isInAnnotationBlock(node);

 assertFalse(inAnnotationBlock);

 DetailAST parent1 = createTestNode();
 parent1.setType(157); // Set type as Annotation
 parent1.addChild(node);
 boolean inAnnotationBlockParent1 = ScopeUtils.isInAnnotationBlock(node);

 assertTrue(inAnnotationBlockParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInAnnotationBlockNullNode() {
 ScopeUtils.isInAnnotationBlock(null);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createTestNode();
 boolean inInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

 assertFalse(inInterfaceOrAnnotationBlock);

 DetailAST parent1 = createTestNode();
 parent1.setType(15); // Set type as InterfaceDecl
 parent1.addChild(node);
 boolean inInterfaceOrAnnotationBlockParent1 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

 assertTrue(inInterfaceOrAnnotationBlockParent1);

 DetailAST parent2 = createTestNode();
 parent2.setType(157); // Set type as Annotation
 parent2.addChild(node);
 boolean inInterfaceOrAnnotationBlockParent2 = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

 assertTrue(inInterfaceOrAnnotationBlockParent2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInInterfaceOrAnnotationBlockNullNode() {
 ScopeUtils.isInInterfaceOrAnnotationBlock(null);
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST node = createTestNode();
 boolean inEnumBlock = ScopeUtils.isInEnumBlock(node);

 assertFalse(inEnumBlock);

 DetailAST parent1 = createTestNode();
 parent1.setType(154); // Set type as Enum
 parent1.addChild(node);
 boolean inEnumBlockParent1 = ScopeUtils.isInEnumBlock(node);

 assertTrue(inEnumBlockParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInEnumBlockNullNode() {
 ScopeUtils.isInEnumBlock(null);
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST node = createTestNode();
 boolean inCodeBlock = ScopeUtils.isInCodeBlock(node);

 assertFalse(inCodeBlock);

 DetailAST parent1 = createTestNode();
 parent1.setType(9); // Set type as ClassBodyDeclaration
 parent1.addChild(node);
 boolean inCodeBlockParent1 = ScopeUtils.isInCodeBlock(node);

 assertTrue(inCodeBlockParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInCodeBlockNullNode() {
 ScopeUtils.isInCodeBlock(null);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST node = createTestNode();
 boolean isOuterMostType = ScopeUtils.isOuterMostType(node);

 assertTrue(isOuterMostType);

 DetailAST parent1 = createTestNode();
 parent1.setType(14); // Set type as ClassDecl
 parent1.addChild(node);
 boolean isOuterMostTypeParent1 = ScopeUtils.isOuterMostType(node);

 assertFalse(isOuterMostTypeParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsOuterMostTypeNullNode() {
 ScopeUtils.isOuterMostType(null);
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = createTestNode();
 boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);

 assertFalse(isLocalVariableDef);

 DetailAST parent1 = createTestNode();
 parent1.setType(10); // Set type as VariableDeclaration
 parent1.addChild(node);
 boolean isLocalVariableDefParent1 = ScopeUtils.isLocalVariableDef(node);

 assertTrue(isLocalVariableDefParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsLocalVariableDefNullNode() {
 ScopeUtils.isLocalVariableDef(null);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST node = createTestNode();
 boolean isClassFieldDef = ScopeUtils.isClassFieldDef(node);

 assertFalse(isClassFieldDef);

 DetailAST parent1 = createTestNode();
 parent1.setType(10); // Set type as VariableDeclaration
 parent1.addChild(node);
 parent1.setType(10); // Set type as ClassVariableDeclaration
 boolean isClassFieldDefParent1 = ScopeUtils.isClassFieldDef(node);

 assertTrue(isClassFieldDefParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsClassFieldDefNullNode() {
 ScopeUtils.isClassFieldDef(null);
 }

 @Test
 public void testIsInScope() {
 DetailAST ast = createTestNode();
 Scope scope = Scope.INTERFACE;
 DetailAST node = createTestNode();
 boolean inScope = ScopeUtils.isInScope(ast, scope);

 assertFalse(inScope);

 DetailAST parent1 = createTestNode();
 parent1.setType(15); // Set type as InterfaceDecl
 parent1.addChild(ast);
 DetailAST nodeInScopeParent1 = createTestNode();
 parent1.addChild(node);
 DetailAST parent2 = createTestNode();
 parent2.setType(Scope.INTERFACE);
 parent2.addChild(ast);
 boolean inScopeParent1 = ScopeUtils.isInScope(node, parent2);

 assertTrue(inScopeParent1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInScopeNullAst() {
 ScopeUtils.isInScope(null, Scope.INTERFACE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsInScopeNullScope() {
 ScopeUtils.isInScope(createTestNode(), null);
 }
}