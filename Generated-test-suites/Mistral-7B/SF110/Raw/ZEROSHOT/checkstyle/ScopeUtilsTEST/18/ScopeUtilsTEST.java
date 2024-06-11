package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ScopeUtilsTest {

 private DetailAST createDetailAST(String text) {
 return new DetailAST() {
 @Override
 public String toString() {
 return text;
 }
 };
 }

 @Test
 public void testGetScopeFromMods() {
 List<DetailAST> mods = new ArrayList<>();
 mods.add(createDetailAST("public"));
 mods.add(createDetailAST("protected"));
 mods.add(createDetailAST("private"));
 mods.add(createDetailAST("class"));
 mods.add(createDetailAST("interface"));
 mods.add(createDetailAST("enum"));

 DetailAST ast = createDetailAST("method");
 for (Scope expected : new Scope[] { Scope.PACKAGE, Scope.PUBLIC, Scope.PROTECTED, Scope.PRIVATE }) {
 DetailAST modsAST = new DetailAST() {
 @Override
 public List<AST> children() {
 return mods;
 }
 };
 DetailAST parentAST = new DetailAST() {
 @Override
 public int getType() {
 return 1; // node type for testing
 }
 };
 ast.setParent(parentAST);
 parentAST.setFirstChild(modsAST);
 Scope actual = ScopeUtils.getScopeFromMods(ast.getFirstChild());
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testGetSurroundingScope_class() {
 DetailAST ast = createDetailAST("class Test { int a; }");
 DetailAST expected = new DetailAST() {
 @Override
 public int getType() {
 return 14; // class type for testing
 }
 };
 Scope actual = ScopeUtils.getSurroundingScope(ast);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_method() {
 DetailAST ast = createDetailAST("class Test { int a() { } }");
 DetailAST expected = new DetailAST() {
 @Override
 public int getType() {
 return 8; // method type for testing
 }
 };
 Scope actual = ScopeUtils.getSurroundingScope(ast);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_interfaceMethod() {
 DetailAST ast = createDetailAST("interface Test { int a(); }");
 DetailAST expected = new DetailAST() {
 @Override
 public int getType() {
 return 15; // interface type for testing
 }
 };
 Scope actual = ScopeUtils.getSurroundingScope(ast);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_annotation() {
 DetailAST ast = createDetailAST("@interface Test { int a(); }");
 DetailAST expected = new DetailAST() {
 @Override
 public int getType() {
 return 157; // annotation type for testing
 }
 };
 Scope actual = ScopeUtils.getSurroundingScope(ast);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSurroundingScope_anonymousClass() {
 DetailAST ast = createDetailAST("class Test { void method() { new Object() { int a; } }; }");
 DetailAST expected = new DetailAST() {
 @Override
 public int getType() {
 return 136; // anonymous class type for testing
 }
 };
 Scope actual = ScopeUtils.getSurroundingScope(ast);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsInInterfaceBlock() {
 DetailAST astInInterface = createDetailAST("interface Test {}");
 assertTrue(ScopeUtils.isInInterfaceBlock(astInInterface));

 DetailAST astNotInInterface = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isInInterfaceBlock(astNotInInterface));
 }

 @Test
 public void testIsInAnnotationBlock() {
 DetailAST astInAnnotation = createDetailAST("@annotation Test {}");
 assertTrue(ScopeUtils.isInAnnotationBlock(astInAnnotation));

 DetailAST astNotInAnnotation = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isInAnnotationBlock(astNotInAnnotation));
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST astInInterfaceOrAnnotation = createDetailAST("interface Test { @annotation Test2 {} }");
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astInInterfaceOrAnnotation));

 DetailAST astNotInInterfaceOrAnnotation = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNotInInterfaceOrAnnotation));
 }

 @Test
 public void testIsInEnumBlock() {
 DetailAST astInEnum = createDetailAST("enum Test {}");
 assertTrue(ScopeUtils.isInEnumBlock(astInEnum));

 DetailAST astNotInEnum = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isInEnumBlock(astNotInEnum));
 }

 @Test
 public void testIsInCodeBlock() {
 DetailAST astInCodeBlock = createDetailAST("{ int a; }");
 assertTrue(ScopeUtils.isInCodeBlock(astInCodeBlock));

 DetailAST astNotInCodeBlock = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isInCodeBlock(astNotInCodeBlock));
 }

 @Test
 public void testIsOuterMostType() {
 DetailAST astOuterMostType = createDetailAST("class Test {}");
 assertTrue(ScopeUtils.isOuterMostType(astOuterMostType));

 DetailAST astNotOuterMostType = createDetailAST("class Test { class Inner {} }");
 assertFalse(ScopeUtils.isOuterMostType(astNotOuterMostType));
 }

 @Test
 public void testIsLocalVariableDef() {
 DetailAST astLocalVariableDef = createDetailAST("int a;");
 assertTrue(ScopeUtils.isLocalVariableDef(astLocalVariableDef));

 DetailAST astNotLocalVariableDef = createDetailAST("class Test {}");
 assertFalse(ScopeUtils.isLocalVariableDef(astNotLocalVariableDef));
 }

 @Test
 public void testIsClassFieldDef() {
 DetailAST astClassFieldDef = createDetailAST("class Test { int a; }");
 assertTrue(ScopeUtils.isClassFieldDef(astClassFieldDef));

 DetailAST astNotClassFieldDef = createDetailAST("class Test { int a() { } }");
 assertFalse(ScopeUtils.isClassFieldDef(astNotClassFieldDef));
 }

 @Test
 public void testIsInScope() {
 DetailAST astInScope = createDetailAST("class Test { private int a; }");
 Scope scope = Scope.PRIVATE;
 DetailAST testAST = new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 return astInScope;
 }
 };
 assertTrue(ScopeUtils.isInScope(testAST, scope));

 DetailAST astNotInScope = createDetailAST("class Test { public int a; }");
 scope = Scope.PRIVATE;
 DetailAST testAST2 = new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 return astNotInScope;
 }
 };
 assertFalse(ScopeUtils.isInScope(testAST2, scope));
 }
}