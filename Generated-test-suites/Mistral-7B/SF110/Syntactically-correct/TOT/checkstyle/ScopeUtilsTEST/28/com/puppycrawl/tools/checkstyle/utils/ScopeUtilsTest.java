import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
  DetailAST astMods = createAST("public protected private class MyClass { }");
  Scope expectedScope = Scope.PROTECTED;
  Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_NoModifiers() {
  DetailAST astMods = createAST("class MyClass { }");
  Scope expectedScope = Scope.PACKAGE;
  Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_EmptyModifiersList() {
  DetailAST astMods = createAST("public class MyClass { }");
  Scope expectedScope = Scope.PUBLIC;
  Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_MultipleModifiers() {
  DetailAST astMods = createAST("public protected private class MyClass { }");
  Scope expectedScope = Scope.PROTECTED;
  Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_InvalidModifier() {
  DetailAST astMods = createAST("public invalid protected private class MyClass { }");
  Scope expectedScope = Scope.PACKAGE;
  Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInClass() {
  DetailAST astNode = createAST("public void method() { }");
  Scope expectedScope = Scope.CLASS;
  Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInInterface() {
  DetailAST astNode = createAST("interface MyInterface { void method(); }");
  Scope expectedScope = Scope.INTERFACE;
  Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInEnum() {
  DetailAST astNode = createAST("enum MyEnum { VALUE; }");
  Scope expectedScope = Scope.ENUM;
  Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInAnonymousClass() {
  DetailAST astNode = createAST("new Object() { void method() { } }");
  Scope expectedScope = Scope.ANONINNER;
  Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInAnnotation() {
  DetailAST astNode = createAST("@annotation { void method() { } }");
  Scope expectedScope = Scope.ANNOTATION;
  Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { void method() { } }");
  DetailAST astCodeBlock = astNode.findFirstToken(9);
  Scope expectedScope = Scope.CODEBLOCK;
  Scope actualScope = ScopeUtils.getSurroundingScope(astCodeBlock);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_MethodInScopeBlock() {
  DetailAST astNode = createAST("public class MyClass { private void method() { } }");
  DetailAST astScopeBlock = astNode.findFirstToken(5);
  Scope expectedScope = Scope.PRIVATE;
  Scope actualScope = ScopeUtils.getSurroundingScope(astScopeBlock);
  assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_MethodInInterface() {
  DetailAST astNode = createAST("interface MyInterface { void method(); }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_MethodInClass() {
  DetailAST astNode = createAST("public class MyClass { void method() { } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_MethodInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { interface MyInterface { void method(); } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_MethodInAnnotation() {
  DetailAST astNode = createAST("@annotation { void method() { } }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_MethodInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { @annotation { void method() { } } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInAnnotationBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_MethodInInterface() {
  DetailAST astNode = createAST("interface MyInterface { void method(); }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_MethodInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { interface MyInterface { void method(); } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInEnumBlock_MethodInEnum() {
  DetailAST astNode = createAST("enum MyEnum { VALUE; }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInEnumBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInEnumBlock_MethodInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { enum MyEnum { VALUE; } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInEnumBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { void method() { } }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isInCodeBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInCodeBlock_NotInCodeBlock() {
  DetailAST astNode = createAST("public class MyClass { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isInCodeBlock(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsOuterMostType() {
  DetailAST astNode = createAST("public class MyClass { }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isOuterMostType(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsOuterMostType_NotOuterMostType() {
  DetailAST astNode = createAST("public class MyOuterClass { public class MyInnerClass { } }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isOuterMostType(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsLocalVariableDef_LocalVariableDef() {
  DetailAST astNode = createAST("int x = 5;");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isLocalVariableDef(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsLocalVariableDef_NotLocalVariableDef() {
  DetailAST astNode = createAST("class MyClass { int x; }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isLocalVariableDef(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsClassFieldDef_ClassFieldDef() {
  DetailAST astNode = createAST("class MyClass { int x; }");
  boolean expectedResult = true;
  boolean actualResult = ScopeUtils.isClassFieldDef(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsClassFieldDef_NotClassFieldDef() {
  DetailAST astNode = createAST("class MyClass { }");
  boolean expectedResult = false;
  boolean actualResult = ScopeUtils.isClassFieldDef(astNode);
  assertTrue(expectedResult == actualResult);
 }

 @Test
 public void testIsInScope_MethodInScope() {
  DetailAST astNode = createAST("public class MyClass { private void method() { } }");
  DetailAST astScopeBlock = astNode.findFirstToken(5);
  Scope expectedScope = Scope.PRIVATE;
  boolean actualResult = ScopeUtils.isInScope(astNode, expectedScope);
  assertTrue(actualResult == true);
 }

 @Test
 public void testIsInScope_MethodNotInScope() {
  DetailAST astNode = createAST("public class MyClass { private void method() { } }");
  DetailAST astScopeBlock = createAST("public class MyClass { }");
  Scope expectedScope = Scope.PACKAGE;
  boolean actualResult = ScopeUtils.isInScope(astNode, expectedScope);
  assertTrue(actualResult == false);
 }

 private DetailAST createAST(String code) {
  JavaCCParser parser = new JavaCCParser();
  return parser.Parse(code, null);
 }

}