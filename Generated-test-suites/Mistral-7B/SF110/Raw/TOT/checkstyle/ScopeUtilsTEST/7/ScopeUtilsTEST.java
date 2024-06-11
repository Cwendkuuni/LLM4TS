package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
 DetailAST modsAST = new BasicNodeList<>();
 modsAST.add(new StringLiteral("public"));
 DetailAST result = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(Scope.PUBLIC, result);

 modsAST.clear();
 modsAST.add(new StringLiteral("protected"));
 result = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(Scope.PROTECTED, result);

 modsAST.clear();
 modsAST.add(new StringLiteral("private"));
 result = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(Scope.PRIVATE, result);
 }

 @Test
 public void testGetScopeFromMods_NoMatchingModifierFound() {
 DetailAST modsAST = new BasicNodeList<>();
 modsAST.add(new StringLiteral("default"));
 DetailAST result = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(Scope.PACKAGE, result);
 }

 @Test
 public void testGetScopeFromMods_MultipleModifiersPresent() {
 DetailAST modsAST = new BasicNodeList<>();
 modsAST.add(new StringLiteral("public"));
 modsAST.add(new StringLiteral("protected"));
 DetailAST result = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(Scope.PUBLIC, result);
 }

 @Test
 public void testGetSurroundingScope_ClassLevelModifierPresent() {
 DetailAST nodeAST = new BasicNodeList<>();
 DetailAST modsAST = new BasicNodeList<>();
 modsAST.add(new StringLiteral("public"));
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertNull(result);

 nodeAST.add(modsAST);
 nodeAST.add(new IdentifierToken("MyClass"));
 result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.PUBLIC, result);
 }

 @Test
 public void testGetSurroundingScope_PackageLevelModifierPresent() {
 DetailAST nodeAST = new BasicNodeList<>();
 DetailAST modsAST = new BasicNodeList<>();
 DetailAST packageNameAST = new StringLiteral("package com.example.mypackage");
 modsAST.add(new PackageDeclarationToken(null, packageNameAST));
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.PACKAGE, result);
 }

 @Test
 public void testGetSurroundingScope_NoModifierPresent() {
 DetailAST nodeAST = new BasicNodeList<>();
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertNull(result);
 }

 @Test
 public void testGetSurroundingScope_AnonymousClass() {
 DetailAST nodeAST = new BasicNodeList<>();
 DetailAST classDeclarationAST = new ClassDeclarationToken(null, null, null, new List(), null, null, null);
 nodeAST.add(classDeclarationAST);
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.ANONINNER, result);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST nodeAST = new InterfaceDeclarationToken(null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isInInterfaceBlock(nodeAST));
 }

 @Test
 public void testIsInInterfaceBlock_NotAnInterface() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isInInterfaceBlock(nodeAST));
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST nodeAST = new AnnotationToken(null, "org.junit.Test");
 assertTrue(ScopeUtils.isInAnnotationBlock(nodeAST));
 }

 @Test
 public void testIsInAnnotationBlock_NotAnAnnotation() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isInAnnotationBlock(nodeAST));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST nodeAST = new InterfaceDeclarationToken(null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_NotAnInterfaceNorAnnotation() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));
 }

 @Test
 public void testIsInInterfaceBlock_NestedInterface() {
 DetailAST nodeAST = new InterfaceDeclarationToken(null, null, null, null, null, null, null);
 nodeAST.add(new ClassDeclarationToken(null, null, null, null, null, null, null));
 DetailAST innerInterfaceAST = new InterfaceDeclarationToken(null, null, null, null, null, null, null);
 nodeAST.add(innerInterfaceAST);
 assertTrue(ScopeUtils.isInInterfaceBlock(innerInterfaceAST));
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST nodeAST = new EnumDeclarationToken(null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isInEnumBlock(nodeAST));
 }

 @Test
 public void testIsInEnumBlock_NotAnEnum() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isInEnumBlock(nodeAST));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST nodeAST = new MethodDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isInCodeBlock(nodeAST));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isOuterMostType(nodeAST));
 }

 @Test
 public void testIsOuterMostType_NestedType() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 DetailAST nestedTypeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 nodeAST.add(nestedTypeAST);
 assertFalse(ScopeUtils.isOuterMostType(nestedTypeAST));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST nodeAST = new VariableDeclarationToken(new int[0][], true, null, null, null, null, null, null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isLocalVariableDef(nodeAST));
 }

 @Test
 public void testIsLocalVariableDef_NotALocalVariable() {
 DetailAST nodeAST = new ClassDeclarationToken(null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isLocalVariableDef(nodeAST));
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST nodeAST = new FieldDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isClassFieldDef(nodeAST));
 }

 @Test
 public void testIsClassFieldDef_ClassField() {
 DetailAST nodeAST = new FieldDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 DetailAST classFieldAST = new VariableDeclarationToken(new int[0][], false, null, null, null, null, null, null, null, null, null, null, null, null, null);
 nodeAST.add(classFieldAST);
 assertTrue(ScopeUtils.isClassFieldDef(classFieldAST));
 }

 @Test
 public void testIsInScope() {
 DetailAST ast = new ClassDeclarationToken(null, null, null, new List(), null, null, null);
 Scope scope = Scope.PRIVATE;
 DetailAST node = new FieldDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 assertTrue(ScopeUtils.isInScope(ast, scope));
 DetailAST node2 = new MethodDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 assertFalse(ScopeUtils.isInScope(ast, scope));
 }

 @Test
 public void testGetSurroundingScope_MethodDeclaration() {
 DetailAST nodeAST = new MethodDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertNull(result);
 }

 @Test
 public void testGetSurroundingScope_MethodDeclarationWithModifier() {
 DetailAST nodeAST = new MethodDeclarationToken(null, null, new List(), null, null, null, null, null, null, null, null, null, null, null, null);
 DetailAST modifierAST = new StringLiteral("public");
 DetailAST modifierListAST = new BasicNodeList<>();
 modifierListAST.add(modifierAST);
 nodeAST.setTokenList(new BasicNodeList<>(modifierListAST, nodeAST.getTokenList()));
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.PUBLIC, result);
 }

 @Test
 public void testGetSurroundingScope_MethodDeclarationWithMultipleModifiers() {
 DetailAST nodeAST = new MethodDeclarationToken(null, null, new List(), null, null, null, null, null, null, null, null, null, null, null, null);
 DetailAST modifier1AST = new StringLiteral("public");
 DetailAST modifier2AST = new StringLiteral("protected");
 DetailAST modifierListAST = new BasicNodeList<>();
 modifierListAST.add(modifier1AST);
 modifierListAST.add(modifier2AST);
 nodeAST.setTokenList(new BasicNodeList<>(modifierListAST, nodeAST.getTokenList()));
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.PUBLIC, result);
 }

 @Test
 public void testGetSurroundingScope_MethodDeclarationWithAnnotations() {
 DetailAST nodeAST = new MethodDeclarationToken(null, null, null, null, null, null, null, null, null, null, null, new List(), null, null, null);
 DetailAST annotationAST = new AnnotationToken(null, "org.junit.Test");
 DetailAST annotationListAST = new BasicNodeList<>();
 annotationListAST.add(annotationAST);
 nodeAST.setTokenList(new BasicNodeList<>(annotationListAST, nodeAST.getTokenList()));
 DetailAST result = ScopeUtils.getSurroundingScope(nodeAST);
 assertEquals(Scope.PUBLIC, result); // method accessibility is not considered when annotations are present
 }
}