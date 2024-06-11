package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;
import org.junit.*;

public class ScopeUtilsTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any class level initialization here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Perform any clean up after tests here
 }

 @Before
 public void setUp() {
 // Set up any per test initialization here
 }

 @After
 public void tearDown() {
 // Perform any clean up after test here
 }

 @Test
 public void testGetScopeFromMods() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(expectedScope, actualScope);

 ast.setText("protected class MyClass { }"); // change the text of the mock DetailAST
 expectedScope = Scope.PROTECTED;
 actualScope = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(expectedScope, actualScope);

 ast.setText("private class MyClass { }"); // change the text of the mock DetailAST
 expectedScope = Scope.PRIVATE;
 actualScope = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_noModifiers() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("class MyClass { }"); // set the text of the mock DetailAST

 Scope actualScope = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(Scope.PACKAGE, actualScope);
 }

 @Test
 public void testGetScopeFromMods_noModifiers_packageStatement() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("package com.example.mypackage;"); // set the text of the mock DetailAST

 Scope actualScope = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(Scope.PACKAGE, actualScope);
 }

 @Test
 public void testGetSurroundingScope_publicClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 Scope expectedScope = Scope.PUBLIC;
 DetailAST node = ast; // set the node to test
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_publicMethod() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { public void myMethod() { } }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstToken(1); // set the node to test (the public keyword)
 Scope expectedScope = Scope.PUBLIC;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_privateMethod() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { private void myMethod() { } }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstToken(2); // set the node to test (the private keyword)
 Scope expectedScope = Scope.PRIVATE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_privateClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("private class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 Scope expectedScope = Scope.PRIVATE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_protectedClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("protected class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_packageClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("package com.example.mypackage; class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild(); // set the node to test (the package statement)
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_interface() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("interface MyInterface { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 Scope expectedScope = Scope.INTERFACE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_annotation() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("@MyAnnotation"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 Scope expectedScope = Scope.ANNOTATION;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_anonymousClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("new MyClass() { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 Scope expectedScope = Scope.ANONINNER;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_interface() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("interface MyInterface { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_class() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_annotation() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("@MyAnnotation"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_class() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_interface() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("interface MyInterface { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_class() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_enum() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("enum MyEnum { VALUE }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInEnumBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_class() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInEnumBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_codeBlock() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable = 0; }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstToken(1); // set the node to test (the opening curly brace)
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInCodeBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_comment() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { // This is a comment }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstToken(1); // set the node to test (the opening curly brace)
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInCodeBlock(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyOuterClass { public class MyInnerClass { } }"); // set the text of the mock DetailAST

 DetailAST node = ast; // set the node to test (the outer class)
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isOuterMostType(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_innerClass() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyOuterClass { public class MyInnerClass { } }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild(); // set the node to test (the inner class)
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isOuterMostType(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable; }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild().findFirstToken(1); // set the node to test (the int keyword)
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_classField() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable = 0; }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild().findFirstToken(1); // set the node to test (the int keyword)
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isLocalVariableDef(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable = 0; }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild(); // set the node to test (the class declaration)
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isClassFieldDef(node);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable = 0; }"); // set the text of the mock DetailAST

 DetailAST node = ast.findFirstChild().findFirstToken(1); // set the node to test (the int keyword)
 Scope expectedScope = Scope.CLASS;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInScope(node, expectedScope));
 }

 @Test
 public void testIsInScope_notInScope() {
 DetailAST ast = new DetailAST(); // create a mock DetailAST
 ast.setText("public class MyClass { int myVariable = 0; }"); // set the text of the mock DetailAST

 DetailAST node = new DetailAST(); // create another mock DetailAST
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInScope(node, Scope.CLASS);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope_outerScope() {
 DetailAST outerAst = new DetailAST(); // create a mock DetailAST for the outer class
 outerAst.setText("public class MyOuterClass { int myVariable = 0; }"); // set the text of the mock DetailAST
 DetailAST innerAst = new DetailAST(); // create a mock DetailAST for the inner class
 innerAst.setText("public class MyInnerClass { int myInnerVariable = 0; }"); // set the text of the mock DetailAST
 DetailAST node = innerAst.findFirstChild().findFirstToken(1); // set the node to test (the int keyword)

 DetailAST outerNode = outerAst; // set the outer node (the outer class)
 while (outerNode != null) {
 Scope expectedScope = outerNode.getType() == 14 ? Scope.CLASS : Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getSurroundingScope(node);
 assertTrue(ScopeUtils.isInScope(node, expectedScope));
 outerNode = outerNode.getParent();
 }
 }
}