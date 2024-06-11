package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.addChild(new TokenAST("public", 0, "public"));
 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_noPublicModifier() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_multipleModifiers() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.addChild(new TokenAST("public", 0, "public"));
 ast.addChild(new TokenAST("protected", 0, "protected"));
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_privateModifier() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.addChild(new TokenAST("private", 0, "private"));
 Scope expectedScope = Scope.PRIVATE;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_inPackageScope() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.setType(AST.PROGRAM);
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 ast.addChild(testMethod);
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getSurroundingScope(testMethod);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_inClassScope() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.setType(AST.PROGRAM);
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 Scope expectedScope = Scope.CLASSES;
 Scope actualScope = ScopeUtils.getSurroundingScope(testClass);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_inMethodScope() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.setType(AST.PROGRAM);
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 Scope expectedScope = Scope.METHODS;
 Scope actualScope = ScopeUtils.getSurroundingScope(testMethod);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_inAnonymousInnerClassScope() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 ast.setType(AST.PROGRAM);
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST anonymousInnerClass = new BasicAST();
 anonymousInnerClass.setType(AST.CLASS_DEF);
 anonymousInnerClass.setTokenStart(300);
 anonymousInnerClass.setTokenEnd(400);
 anonymousInnerClass.setParent(testClass);
 Scope expectedScope = Scope.ANONINNER;
 Scope actualScope = ScopeUtils.getSurroundingScope(anonymousInnerClass);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_inInterfaceBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST interfaceDeclaration = new BasicAST();
 interfaceDeclaration.setType(AST.INTERFACE_DEF);
 interfaceDeclaration.setName("MyInterface");
 ast.addChild(interfaceDeclaration);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(interfaceDeclaration);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_notInInterfaceBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_inAnnotationBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST annotationDeclaration = new BasicAST();
 annotationDeclaration.setType(AST.ANNOTATION_DEF);
 annotationDeclaration.setName("@MyAnnotation");
 ast.addChild(annotationDeclaration);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(annotationDeclaration);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_notInAnnotationBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_inInterfaceBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST interfaceDeclaration = new BasicAST();
 interfaceDeclaration.setType(AST.INTERFACE_DEF);
 interfaceDeclaration.setName("MyInterface");
 ast.addChild(interfaceDeclaration);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(interfaceDeclaration);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_inAnnotationBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST annotationDeclaration = new BasicAST();
 annotationDeclaration.setType(AST.ANNOTATION_DEF);
 annotationDeclaration.setName("@MyAnnotation");
 ast.addChild(annotationDeclaration);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(annotationDeclaration);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_notInInterfaceOrAnnotationBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_inEnumBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST enumDeclaration = new BasicAST();
 enumDeclaration.setType(AST.ENUM_DEF);
 enumDeclaration.setName("MyEnum");
 ast.addChild(enumDeclaration);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(enumDeclaration);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInEnumBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_notInEnumBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInEnumBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_inCodeBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST statement = new StatementAST();
 statement.setType(AST.ASSIGNMENT);
 ast.addChild(statement);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(ast);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInCodeBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_notInCodeBlock() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 DetailAST testMethod = new BasicAST();
 testMethod.setType(AST.METHOD_DEF);
 testMethod.setName("testMethod");
 testMethod.setTokenStart(100);
 testMethod.setTokenEnd(200);
 testMethod.setParent(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInCodeBlock(testMethod);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_isOuterMostType() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST program = new BasicAST();
 program.setType(AST.PROGRAM);
 ast.setParent(program);
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isOuterMostType(ast);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_notIsOuterMostType() {
 DetailAST ast = new BasicAST(); // an empty DetailAST for testing
 DetailAST packageDeclaration = new BasicAST();
 packageDeclaration.setType(AST.PACKAGE);
 ast.addChild(packageDeclaration);
 DetailAST testClass = new BasicAST();
 testClass.setType(AST.CLASS_DEF);
 testClass.setName("TestClass");
 ast.addChild(testClass);
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isOuterMostType(testClass);
 assertEquals(expectedResult, actualResult);
 }
}