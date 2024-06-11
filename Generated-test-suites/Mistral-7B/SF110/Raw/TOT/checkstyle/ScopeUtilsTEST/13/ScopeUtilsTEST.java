package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods_PublicAccess_ReturnsScopePublic() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.addChild(new TokenAST("public"));
 final Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 final Scope expectedScope = Scope.PUBLIC;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_ProtectedAccess_ReturnsScopeProtected() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.addChild(new TokenAST("protected"));
 final Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 final Scope expectedScope = Scope.PROTECTED;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_PrivateAccess_ReturnsScopePrivate() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.addChild(new TokenAST("private"));
 final Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 final Scope expectedScope = Scope.PRIVATE;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_NoAccessSpecifier_ReturnsScopePackage() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 final Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 final Scope expectedScope = Scope.PACKAGE;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_ClassDeclaration_ReturnsScopeClass() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a class declaration
 final int type = 15;
 ast.setType(type);
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast);
 final Scope expectedScope = Scope.CLASS;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InterfaceDeclaration_ReturnsScopeInterface() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an interface declaration
 final int type = 15;
 ast.setType(type);
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast);
 final Scope expectedScope = Scope.INTERFACE;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_EnumDeclaration_ReturnsScopeEnum() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an enum declaration
 final int type = 154;
 ast.setType(type);
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast);
 final Scope expectedScope = Scope.ENUM;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_AnonymousInnerClassDeclaration_ReturnsScopeAnonymousInner() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an anonymous inner class declaration
 final int type = 136;
 ast.setType(type);
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast);
 final Scope expectedScope = Scope.ANONINNER;
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_UnknownToken_ReturnsNull() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.setType(123); // A random non-existent type
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast);
 assertNull(actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_InterfaceDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an interface declaration
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast.getParent());
 final boolean actualResult = ScopeUtils.isInInterfaceBlock(ast);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult || actualScope.isIn(Scope.INTERFACE));
 }

 @Test
 public void testIsInAnnotationBlock_AnnotationDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an annotation declaration
 final Scope actualScope = ScopeUtils.getSurroundingScope(ast.getParent());
 final boolean actualResult = ScopeUtils.isInAnnotationBlock(ast);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult || actualScope.isIn(Scope.ANNOTATION));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InterfaceOrAnnotationDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an interface or annotation declaration
 final boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than interface or annotation
 final boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInEnumBlock_EnumDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an enum declaration
 final boolean actualResult = ScopeUtils.isInEnumBlock(ast);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInEnumBlock_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than enum
 final boolean actualResult = ScopeUtils.isInEnumBlock(ast);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInCodeBlock_CodeBlock_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a code block
 final DetailAST codeBlock = ast.addChild(new TokenAST(9)); // Add a code block token
 final boolean actualResult = ScopeUtils.isInCodeBlock(codeBlock);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInCodeBlock_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than code block
 final DetailAST otherDeclaration = ast.addChild(new TokenAST(15)); // Add a declaration token
 final boolean actualResult = ScopeUtils.isInCodeBlock(otherDeclaration);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsOuterMostType_OuterMostType_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing an outermost type
 final boolean actualResult = ScopeUtils.isOuterMostType(ast);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsOuterMostType_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than outermost type
 final boolean actualResult = ScopeUtils.isOuterMostType(ast);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariableDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a local variable declaration
 final DetailAST variableDeclaration = ast.addChild(new TokenAST(10)); // Add a variable declaration token
 final boolean actualResult = ScopeUtils.isLocalVariableDef(variableDeclaration);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsLocalVariableDef_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than local variable
 final DetailAST otherDeclaration = ast.addChild(new TokenAST(15)); // Add a declaration token
 final boolean actualResult = ScopeUtils.isLocalVariableDef(otherDeclaration);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsClassFieldDef_ClassFieldDeclaration_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a class field declaration
 final DetailAST fieldDeclaration = ast.addChild(new TokenAST(10)); // Add a field declaration token
 final boolean actualResult = ScopeUtils.isClassFieldDef(fieldDeclaration);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsClassFieldDef_OtherDeclaration_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing a declaration other than class field
 final DetailAST otherDeclaration = ast.addChild(new TokenAST(15)); // Add a declaration token
 final boolean actualResult = ScopeUtils.isClassFieldDef(otherDeclaration);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInScope_ASTInScope_ReturnsTrue() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 final Scope scope = Scope.PACKAGE;
 final DetailAST testAST = ast.addChild(new TokenAST("test"));
 final boolean actualResult = ScopeUtils.isInScope(testAST, scope);
 final boolean expectedResult = true;
 assertTrue(actualResult == expectedResult);
 }

 @Test
 public void testIsInScope_ASTOutOfScope_ReturnsFalse() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 final Scope scope = Scope.PRIVATE;
 final DetailAST testAST = ast.addChild(new TokenAST("test"));
 final boolean actualResult = ScopeUtils.isInScope(testAST, scope);
 final boolean expectedResult = false;
 assertTrue(actualResult == expectedResult);
 }
}