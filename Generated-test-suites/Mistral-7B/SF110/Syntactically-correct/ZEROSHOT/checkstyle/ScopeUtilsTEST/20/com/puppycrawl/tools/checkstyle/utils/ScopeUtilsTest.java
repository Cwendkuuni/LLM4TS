package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createASTNode(String nodeText) {
 return new DetailAST() {
 @Override
 public String getText() {
 return nodeText;
 }
 };
 }

 @Test
 public void testGetScopeFromMods() {
 final DetailAST modsAST = new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 return createASTNode("public");
 }
 };
 final Scope expectedScope = Scope.PUBLIC;
 final Scope scope = ScopeUtils.getScopeFromMods(modsAST);
 assertEquals(expectedScope, scope);

 final DetailAST modsAST2 = new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 return createASTNode("protected");
 }
 };
 final Scope expectedScope2 = Scope.PROTECTED;
 final Scope scope2 = ScopeUtils.getScopeFromMods(modsAST2);
 assertEquals(expectedScope2, scope2);

 final DetailAST modsAST3 = new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 return createASTNode("private");
 }
 };
 final Scope expectedScope3 = Scope.PRIVATE;
 final Scope scope3 = ScopeUtils.getScopeFromMods(modsAST3);
 assertEquals(expectedScope3, scope3);

 final DetailAST modsAST4 = new DetailAST();
 final Scope expectedScope4 = Scope.PACKAGE;
 final Scope scope4 = ScopeUtils.getScopeFromMods(modsAST4);
 assertEquals(expectedScope4, scope4);
 }

 @Test
 public void testGetSurroundingScope() {
 final DetailAST node = new DetailAST();
 final Scope expectedScope = Scope.INVALID;
 final Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, scope);

 final DetailAST nodeParent1 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("ClassDef");
 }
 };
 final DetailAST nodeChild1 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("MethodDef");
 }
 };
 final DetailAST nodeParent2 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("InterfaceDef");
 }
 };
 final DetailAST nodeParent3 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("PackageDeclaration");
 }
 };
 final DetailAST nodeChild2 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("ClassOrInterfaceDeclaration");
 }
 };
 final DetailAST nodeChild3 = new DetailAST() {
 @Override
 public DetailAST getType() {
 return nodeChild1;
 }
 };
 final Scope scope1 = ScopeUtils.getSurroundingScope(nodeChild1);
 assertEquals(Scope.CLASS, scope1);
 final Scope scope2 = ScopeUtils.getSurroundingScope(nodeParent1);
 assertEquals(Scope.PACKAGE, scope2);
 final Scope scope3 = ScopeUtils.getSurroundingScope(nodeParent2);
 assertEquals(Scope.PACKAGE, scope3);
 final Scope scope4 = ScopeUtils.getSurroundingScope(nodeParent3);
 assertEquals(Scope.INVALID, scope4);
 final Scope scope5 = ScopeUtils.getSurroundingScope(nodeChild2);
 assertEquals(Scope.PACKAGE, scope5);
 final Scope scope6 = ScopeUtils.getSurroundingScope(nodeChild3);
 assertEquals(Scope.METHOD, scope6);
 }

 @Test
 public void testIsInInterfaceBlock() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("InterfaceDef");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isInInterfaceBlock(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassDef");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isInInterfaceBlock(node2);
 assertEquals(expectedResult2, result2);

 final DetailAST node3 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return node1;
 }
 };
 final boolean expectedResult3 = true;
 final boolean result3 = ScopeUtils.isInInterfaceBlock(node3);
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testIsInAnnotationBlock() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("Annotation");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isInAnnotationBlock(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassDef");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isInAnnotationBlock(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("InterfaceDef");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isInInterfaceOrAnnotationBlock(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassDef");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isInInterfaceOrAnnotationBlock(node2);
 assertEquals(expectedResult2, result2);

 final DetailAST node3 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("Annotation");
 }
 };
 final boolean expectedResult3 = true;
 final boolean result3 = ScopeUtils.isInInterfaceOrAnnotationBlock(node3);
 assertEquals(expectedResult3, result3);
 }

 @Test
 public void testIsInEnumBlock() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("Enum");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isInEnumBlock(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassDef");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isInEnumBlock(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsInCodeBlock() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("Statement");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isInCodeBlock(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("TypeDeclaration");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isInCodeBlock(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsOuterMostType() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassDef");
 }
 };
 final boolean expectedResult1 = false;
 final boolean result1 = ScopeUtils.isOuterMostType(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("PackageDeclaration");
 }
 };
 final boolean expectedResult2 = true;
 final boolean result2 = ScopeUtils.isOuterMostType(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsLocalVariableDef() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("LocalVariableDeclaration");
 }
 };
 final boolean expectedResult1 = true;
 final boolean result1 = ScopeUtils.isLocalVariableDef(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("FieldDeclaration");
 }
 };
 final boolean expectedResult2 = false;
 final boolean result2 = ScopeUtils.isLocalVariableDef(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsClassFieldDef() {
 final DetailAST node1 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("ClassOrInterfaceDeclaration");
 }
 };
 final boolean expectedResult1 = false;
 final boolean result1 = ScopeUtils.isClassFieldDef(node1);
 assertEquals(expectedResult1, result1);

 final DetailAST node2 = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return createASTNode("FieldDeclaration");
 }
 };
 final boolean expectedResult2 = true;
 final boolean result2 = ScopeUtils.isClassFieldDef(node2);
 assertEquals(expectedResult2, result2);
 }

 @Test
 public void testIsInScope() {
 final DetailAST nodeChild = new DetailAST() {
 @Override
 public DetailAST getParent() {
 return new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("ClassOrInterfaceDeclaration");
 }
 };
 }
 };
 final Scope expectedResult = Scope.CLASS;
 final DetailAST nodeParent = new DetailAST() {
 @Override
 public DetailAST getType() {
 return createASTNode("PackageDeclaration");
 }
 };
 final boolean result = ScopeUtils.isInScope(nodeChild, expectedResult);
 assertEquals(expectedResult, result ? nodeChild.getScope(Scope.DEFAULT) : null);
 final boolean result2 = ScopeUtils.isInScope(nodeParent, expectedResult);
 assertEquals(false, result2);
 }
}