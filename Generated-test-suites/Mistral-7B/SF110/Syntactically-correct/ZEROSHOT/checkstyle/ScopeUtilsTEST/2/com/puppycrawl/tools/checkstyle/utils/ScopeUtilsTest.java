package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createTestNode(String text) {
  AstFactory factory = new AstFactory();
  return (DetailAST) factory.createAST(txt -> txt.equals(text), null);
 }

 @Test
 public void testGetScopeFromMods() {
  DetailAST mods = createTestNode("public class Test {}");
  Scope expected = Scope.PUBLIC;
  Scope actual = ScopeUtils.getScopeFromMods(mods);
  assertEquals("getScopeFromMods should return Scope.PUBLIC", expected, actual);

  mods = createTestNode("protected class Test {}");
  expected = Scope.PROTECTED;
  actual = ScopeUtils.getScopeFromMods(mods);
  assertEquals("getScopeFromMods should return Scope.PROTECTED", expected, actual);

  mods = createTestNode("private class Test {}");
  expected = Scope.PRIVATE;
  actual = ScopeUtils.getScopeFromMods(mods);
  assertEquals("getScopeFromMods should return Scope.PRIVATE", expected, actual);

  mods = createTestNode("class Test {}");
  actual = ScopeUtils.getScopeFromMods(mods);
  assertEquals("getScopeFromMods should return Scope.PACKAGE", Scope.PACKAGE, actual);
 }

 @Test
 public void testGetSurroundingScope() {
  DetailAST classDeclaration = createTestNode("public class Outer { public class Inner {}}");
  DetailAST innerClass = classDeclaration.findFirstToken(1);

  Scope expected = Scope.ANONINNER;
  Scope actual = ScopeUtils.getSurroundingScope(innerClass);
  assertEquals("getSurroundingScope should return Scope.ANONINNER", expected, actual);

  classDeclaration = createTestNode("public class Outer { public interface Inner {}}");
  innerClass = classDeclaration.findFirstToken(1);

  expected = Scope.INTERFACE;
  actual = ScopeUtils.getSurroundingScope(innerClass);
  assertEquals("getSurroundingScope should return Scope.INTERFACE", expected, actual);

  classDeclaration = createTestNode("public class Outer { public enum Inner {}}");
  innerClass = classDeclaration.findFirstToken(1);

  expected = Scope.ENUM;
  actual = ScopeUtils.getSurroundingScope(innerClass);
  assertEquals("getSurroundingScope should return Scope.ENUM", expected, actual);

  classDeclaration = createTestNode("public class Outer { public void method() { int x; }}");
  DetailAST variableDeclaration = classDeclaration.findFirstToken(2);

  expected = Scope.LOCAL;
  actual = ScopeUtils.getSurroundingScope(variableDeclaration);
  assertEquals("getSurroundingScope should return Scope.LOCAL", expected, actual);

  classDeclaration = createTestNode("public class Outer { public static int x = 0; }}");
  DetailAST fieldDeclaration = classDeclaration.findFirstToken(2);

  expected = Scope.CLASS;
  actual = ScopeUtils.getSurroundingScope(fieldDeclaration);
  assertEquals("getSurroundingScope should return Scope.CLASS", expected, actual);

  classDeclaration = createTestNode("public class Outer {}");
  DetailAST emptyNode = classDeclaration.findFirstToken(1);

  actual = ScopeUtils.getSurroundingScope(emptyNode);
  assertNull("getSurroundingScope should return null for empty node", actual);
 }

 @Test
 public void testIsInInterfaceBlock() {
  DetailAST interfaceDeclaration = createTestNode("public interface Inner {}");
  assertTrue("isInInterfaceBlock should return true for interface declaration", ScopeUtils.isInInterfaceBlock(interfaceDeclaration));

  DetailAST classDeclaration = createTestNode("public class Outer {}");
  assertFalse("isInInterfaceBlock should return false for class declaration", ScopeUtils.isInInterfaceBlock(classDeclaration));
 }

 @Test
 public void testIsInAnnotationBlock() {
  DetailAST annotation = createTestNode("@Annotation");
  assertTrue("isInAnnotationBlock should return true for annotation", ScopeUtils.isInAnnotationBlock(annotation));

  DetailAST classDeclaration = createTestNode("public class Outer {}");
  assertFalse("isInAnnotationBlock should return false for class declaration", ScopeUtils.isInAnnotationBlock(classDeclaration));
 }

 @Test
 public void testIsInScope() {
  DetailAST classDeclaration = createTestNode("public class Outer { public class Inner { int x; }}");
  DetailAST innerVariable = classDeclaration.findFirstToken(2).findFirstToken(2);

  Scope expected = Scope.ANONINNER;
  assertTrue("isInScope should return true for inner variable", ScopeUtils.isInScope(innerVariable, expected));

  DetailAST outerVariable = classDeclaration.findFirstToken(1).findFirstToken(1);
  Scope actual = ScopeUtils.isInScope(outerVariable, Scope.ANONINNER);
  assertFalse("isInScope should return false for outer variable in wrong scope", actual != null);
 }

 @Test
 public void testIsOuterMostType() {
  DetailAST classDeclaration = createTestNode("public class Outer {}");
  assertTrue("isOuterMostType should return true for top-level class", ScopeUtils.isOuterMostType(classDeclaration));

  DetailAST nestedClassDeclaration = createTestNode("public class Outer { public class Inner {} }");
  assertFalse("isOuterMostType should return false for nested class", ScopeUtils.isOuterMostType(nestedClassDeclaration));
 }

 @Test
 public void testIsLocalVariableDef() {
  DetailAST variableDeclaration = createTestNode("int x = 0;");
  assertTrue("isLocalVariableDef should return true for local variable definition", ScopeUtils.isLocalVariableDef(variableDeclaration));

  DetailAST fieldDeclaration = createTestNode("public int x;");
  assertFalse("isLocalVariableDef should return false for field declaration", ScopeUtils.isLocalVariableDef(fieldDeclaration));
 }

 @Test
 public void testIsClassFieldDef() {
  DetailAST fieldDeclaration = createTestNode("public int x;");
  assertTrue("isClassFieldDef should return true for field declaration", ScopeUtils.isClassFieldDef(fieldDeclaration));

  DetailAST variableDeclaration = createTestNode("int x = 0;");
  assertFalse("isClassFieldDef should return false for local variable definition", ScopeUtils.isClassFieldDef(variableDeclaration));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
  DetailAST interfaceDeclaration = createTestNode("public interface Inner {}");
  assertTrue("isInInterfaceOrAnnotationBlock should return true for interface declaration", ScopeUtils.isInInterfaceOrAnnotationBlock(interfaceDeclaration));

  DetailAST annotation = createTestNode("@Annotation");
  assertTrue("isInInterfaceOrAnnotationBlock should return true for annotation", ScopeUtils.isInInterfaceOrAnnotationBlock(annotation));

  DetailAST classDeclaration = createTestNode("public class Outer {}");
  assertFalse("isInInterfaceOrAnnotationBlock should return false for class declaration", ScopeUtils.isInInterfaceOrAnnotationBlock(classDeclaration));
 }

 @Test
 public void testIsInEnumBlock() {
  DetailAST enumDeclaration = createTestNode("public enum Inner {}");
  assertTrue("isInEnumBlock should return true for enum declaration", ScopeUtils.isInEnumBlock(enumDeclaration));

  DetailAST classDeclaration = createTestNode("public class Outer {}");
  assertFalse("isInEnumBlock should return false for class declaration", ScopeUtils.isInEnumBlock(classDeclaration));
 }

 @Test
 public void testIsInCodeBlock() {
  DetailAST codeBlock = createTestNode("public class Outer { int x = 0; }");
  DetailAST firstToken = codeBlock.getFirstToken();

  assertTrue("isInCodeBlock should return true for code block", ScopeUtils.isInCodeBlock(firstToken));

  DetailAST interfaceDeclaration = createTestNode("public interface Inner {}");
  DetailAST firstTokenInterface = interfaceDeclaration.getFirstToken();

  assertFalse("isInCodeBlock should return false for interface declaration", ScopeUtils.isInCodeBlock(firstTokenInterface));
 }
}