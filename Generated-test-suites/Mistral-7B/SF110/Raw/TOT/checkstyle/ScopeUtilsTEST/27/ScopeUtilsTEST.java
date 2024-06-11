package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;
import antlr.collections.List;

import org.junit.Test;

public class ScopeUtilsTest {

 // Test for getScopeFromMods method
 @Test
 public void testGetScopeFromMods() {
 AST modsAST = createModifiersAST("public protected private");
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(expectedScope, actualScope);
 }

 // Test for getScopeFromMods method with empty modifiers list
 @Test
 public void testGetScopeFromModsEmptyList() {
 Scope actualScope = ScopeUtils.getScopeFromMods(null);
 assertEquals(Scope.PACKAGE, actualScope);
 }

 // Test for getScopeFromMods method with incorrect modifier
 @Test(expected = IllegalArgumentException.class)
 public void testGetScopeFromModsInvalidModifier() {
 AST modsAST = createModifiersAST("invalid");
 ScopeUtils.getScopeFromMods(modsAST);
 }

 // Test for getSurroundingScope method
 @Test
 public void testGetSurroundingScope() {
 DetailAST node = createNodeWithParents("ClassType Identifier ModifierList");
 Scope expectedScope = Scope.CLASS;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 // Test for getSurroundingScope method with empty parent node
 @Test
 public void testGetSurroundingScopeEmptyParent() {
 DetailAST node = createNodeWithParents();
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertNull(actualScope);
 }

 // Test for getSurroundingScope method with different scope types
 @Test
 public void testGetSurroundingScopeDifferentScopes() {
 DetailAST node = createNodeWithParents("AnnotationType Identifier ModifierList", "ClassType Identifier ModifierList");
 Scope expectedScope = Scope.ANNOTATIONTYPE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 // Test for isInInterfaceBlock method
 @Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createNodeWithParents("InterfaceType Identifier InterfaceBody", "ClassType Identifier MethodDeclaration");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInInterfaceBlock method with empty parent node
 @Test
 public void testIsInInterfaceBlockEmptyParent() {
 DetailAST node = createNodeWithParents();
 boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
 assertFalse(actualResult);
 }

 // Test for isInAnnotationBlock method
 @Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createNodeWithParents("AnnotationType Identifier MemberDeclaration", "ClassType Identifier MethodDeclaration");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInAnnotationBlock method with empty parent node
 @Test
 public void testIsInAnnotationBlockEmptyParent() {
 DetailAST node = createNodeWithParents();
 boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
 assertFalse(actualResult);
 }

 // Test for isInInterfaceOrAnnotationBlock method
 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createNodeWithParents("InterfaceType Identifier InterfaceBody", "ClassType Identifier MethodDeclaration", "AnnotationType Identifier MemberDeclaration");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInInterfaceOrAnnotationBlock method with empty parent node
 @Test
 public void testIsInInterfaceOrAnnotationBlockEmptyParent() {
 DetailAST node = createNodeWithParents();
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertFalse(actualResult);
 }

 // Test for isInEnumBlock method
 @Test
 public void testIsInEnumBlock() {
 DetailAST node = createNodeWithParents("EnumType Identifier EnumBody", "ClassType Identifier MethodDeclaration");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInEnumBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInEnumBlock method with empty parent node
 @Test
 public void testIsInEnumBlockEmptyParent() {
 DetailAST node = createNodeWithParents();
 boolean actualResult = ScopeUtils.isInEnumBlock(node);
 assertFalse(actualResult);
 }

 // Test for isInCodeBlock method
 @Test
 public void testIsInCodeBlock() {
 DetailAST node = createNodeWithParents("CompilationUnit", "ClassType Identifier MethodDeclaration", "StatementList", "IfStatement");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInCodeBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInCodeBlock method with empty parent node
 @Test
 public void testIsInCodeBlockEmptyParent() {
 DetailAST node = createNodeWithParents();
 boolean actualResult = ScopeUtils.isInCodeBlock(node);
 assertFalse(actualResult);
 }

 // Test for isOuterMostType method
 @Test
 public void testIsOuterMostType() {
 DetailAST node = createNodeWithParents("CompilationUnit", "PackageDeclaration", "ClassType Identifier MethodDeclaration");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isOuterMostType(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isOuterMostType method with non-outermost type node
 @Test
 public void testIsOuterMostTypeNonOutermostType() {
 DetailAST node = createNodeWithParents("ClassType Identifier MethodDeclaration", "ClassType Identifier FieldDeclaration");
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isOuterMostType(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isLocalVariableDef method
 @Test
 public void testIsLocalVariableDef() {
 DetailAST node = createNodeWithParents("ClassType Identifier MethodDeclaration", "VariableDeclarationList", "VariableDeclarator", "Identifier");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isLocalVariableDef method with non-variable node
 @Test
 public void testIsLocalVariableDefNonVariable() {
 DetailAST node = createNodeWithParents("ClassType Identifier MethodDeclaration", "StatementList", "IfStatement", "IfCondition");
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isClassFieldDef method
 @Test
 public void testIsClassFieldDef() {
 DetailAST node = createNodeWithParents("ClassType Identifier FieldDeclaration", "FieldDeclarationList", "VariableDeclarator", "Identifier");
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isClassFieldDef(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isClassFieldDef method with non-field node
 @Test
 public void testIsClassFieldDefNonField() {
 DetailAST node = createNodeWithParents("ClassType Identifier MethodDeclaration", "StatementList", "IfStatement", "IfCondition");
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isClassFieldDef(node);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInScope method
 @Test
 public void testIsInScope() {
 DetailAST ast = createNodeWithParents("ClassType Identifier MethodDeclaration", "StatementList", "IfStatement", "IfCondition", "Identifier");
 Scope scope = Scope.CLASS;
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInScope(ast, scope);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInScope method with different scope
 @Test
 public void testIsInScopeDifferentScope() {
 DetailAST ast = createNodeWithParents("ClassType Identifier MethodDeclaration", "StatementList", "IfStatement", "IfCondition", "Identifier");
 Scope scope = Scope.PACKAGE;
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInScope(ast, scope);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInScope method with null scope
 @Test
 public void testIsInScopeNullScope() {
 DetailAST ast = createNodeWithParents("ClassType Identifier MethodDeclaration", "StatementList", "IfStatement", "IfCondition", "Identifier");
 Scope scope = null;
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInScope(ast, scope);
 assertEquals(expectedResult, actualResult);
 }

 // Test for isInScope method with null ast
 @Test
 public void testIsInScopeNullAst() {
 Scope scope = Scope.PACKAGE;
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInScope(null, scope);
 assertEquals(expectedResult, actualResult);
 }

 private List createModifiersAST(String... modifiers) {
 List modifiersList = new List();
 for (String modifier : modifiers) {
 AST modifierAST = new AST();
 modifierAST.setType(14); // TokenType.MODIFIER
 modifierAST.setText(modifier);
 modifiersList.add(modifierAST);
 }
 return modifiersList;
 }

 private DetailAST createNodeWithParents(String... nodeTypes) {
 DetailAST node = new DetailAST();
 DetailAST parent = createNodeWithParents();
 node.setType(getTypeCode(nodeTypes[0]));
 node.setParent(parent);
 parent.addChild(node);
 return node;
 }

 private DetailAST createNodeWithParents(String nodeType, String... parentNodeTypes) {
 DetailAST node = new DetailAST();
 DetailAST parent = createNodeWithParents();
 node.setType(getTypeCode(nodeType));
 parent.setType(getTypeCode(parentNodeTypes[0]));
 node.setParent(parent);
 for (int i = 1; i < parentNodeTypes.length; i++) {
 DetailAST grandParent = parent;
 parent = new DetailAST();
 parent.setType(getTypeCode(parentNodeTypes[i]));
 parent.setParent(grandParent);
 node.setParent(parent);
 grandParent.addChild(parent);
 }
 return node;
 }

 private int getTypeCode(String nodeType) {
 int typeCode;
 switch (nodeType) {
 case "CompilationUnit":
 typeCode = 9;
 break;
 case "PackageDeclaration":
 typeCode = 13;
 break;
 case "ClassType":
 typeCode = 14;
 break;
 case "InterfaceType":
 typeCode = 15;
 break;
 case "EnumType":
 typeCode = 154;
 break;
 case "AnnotationType":
 typeCode = 157;
 break;
 case "Identifier":
 typeCode = 35;
 break;
 case "ModifierList":
 typeCode = 5;
 break;
 case "MethodDeclaration":
 typeCode = 11;
 break;
 case "StatementList":
 typeCode = 12;
 break;
 case "IfStatement":
 typeCode = 136;
 break;
 case "IfCondition":
 typeCode = 143;
 break;
 case "VariableDeclarationList":
 typeCode = 7;
 break;
 case "VariableDeclarator":
 typeCode = 156;
 break;
 default:
 typeCode = -1;
 }
 return typeCode;
 }
}