package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class ScopeUtilsTest {

 private static DetailAST createDetailAST(int type, String text) {
 CommonTokenStream tokenStream = new CommonTokenStream(new CheckstyleLexer(new java.io.StringReader(text)));
 DetailAST ast = (DetailAST) tokenStream.getToken(0).getTree();
 return ast;
 }

 @org.junit.Test
 public void testGetScopeFromMods() {
 List<DetailAST> mods = new ArrayList<>();
 mods.add(createDetailAST(5, "public"));
 mods.add(createDetailAST(5, "protected"));
 DetailAST detailAST = createDetailAST(5, "private");
 mods.add(detailAST);
 Scope expected = Scope.PRIVATE;
 Scope scope = ScopeUtils.getScopeFromMods(new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 throw new UnsupportedOperationException();
 }

 @Override
 public DetailAST getNextSibling() {
 throw new UnsupportedOperationException();
 }

 @Override
 public List<DetailAST> getChildren() {
 return mods;
 }

 @Override
 public DetailAST getParent() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getLine() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getColumn() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getType() {
 throw new UnsupportedOperationException();
 }
 });
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetScopeFromMods_noModifiers() {
 Scope scope = ScopeUtils.getScopeFromMods(new DetailAST() {
 @Override
 public DetailAST getFirstChild() {
 throw new UnsupportedOperationException();
 }

 @Override
 public DetailAST getNextSibling() {
 throw new UnsupportedOperationException();
 }

 @Override
 public List<DetailAST> getChildren() {
 return new ArrayList<>();
 }

 @Override
 public DetailAST getParent() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getLine() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getColumn() {
 throw new UnsupportedOperationException();
 }

 @Override
 public int getType() {
 throw new UnsupportedOperationException();
 }
 });
 assertEquals(Scope.PACKAGE, scope);
 }

 @org.junit.Test(expected = RuntimeException.class)
 public void testGetScopeFromMods_invalidAST() {
 DetailAST detailAST = createDetailAST(5, "invalid");
 ScopeUtils.getScopeFromMods(detailAST);
 }

 @org.junit.Test
 public void testGetSurroundingScope() {
 DetailAST node = createDetailAST(10, "class Foo { int x; }");
 Scope expected = Scope.CLASS;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_emptyParents() {
 DetailAST node = createDetailAST(1, "root");
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertNull(scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_Class() {
 DetailAST node = createDetailAST(14, "class Foo { }");
 Scope expected = Scope.CLASS;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_Method() {
 DetailAST node = createDetailAST(15, "class Foo { void foo() { } }");
 Scope expected = Scope.METHOD;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_AnonymousClass() {
 DetailAST node = createDetailAST(136, "class Foo { void main() { new Object() { void bar() { } }; } }");
 Scope expected = Scope.ANONINNER;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_Interface() throws RecognitionException {
 DetailAST node = createDetailAST(15, "interface Foo { }");
 Scope expected = Scope.INTERFACE;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testGetSurroundingScope_Annotation() throws RecognitionException {
 DetailAST node = createDetailAST(157, "@interface Foo { }");
 Scope expected = Scope.ANNOTATION;
 Scope scope = ScopeUtils.getSurroundingScope(node);
 assertEquals(expected, scope);
 }

 @org.junit.Test
 public void testIsInInterfaceBlock() {
 DetailAST node = createDetailAST(15, "interface Foo { void foo(); }");
 assertTrue(ScopeUtils.isInInterfaceBlock(node));
 }

 @org.junit.Test
 public void testIsInInterfaceBlock_notInterface() {
 DetailAST node = createDetailAST(14, "class Foo { void foo(); }");
 assertFalse(ScopeUtils.isInInterfaceBlock(node));
 }

 @org.junit.Test
 public void testIsInAnnotationBlock() {
 DetailAST node = createDetailAST(157, "@interface Foo { int foo = 1; }");
 assertTrue(ScopeUtils.isInAnnotationBlock(node));
 }

 @org.junit.Test
 public void testIsInAnnotationBlock_notAnnotation() {
 DetailAST node = createDetailAST(14, "class Foo { void foo(); }");
 assertFalse(ScopeUtils.isInAnnotationBlock(node));
 }

 @org.junit.Test
 public void testIsInInterfaceOrAnnotationBlock() {
 DetailAST node = createDetailAST(15, "interface Foo { int foo(); @interface Bar {} }");
 assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
 }

 @org.junit.Test
 public void testIsInInterfaceOrAnnotationBlock_notInterfaceNorAnnotation() {
 DetailAST node = createDetailAST(14, "class Foo { void foo(); }");
 assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
 }

 @org.junit.Test
 public void testIsInEnumBlock() {
 DetailAST node = createDetailAST(154, "enum Foo { VALUE1, VALUE2 }");
 assertTrue(ScopeUtils.isInEnumBlock(node));
 }

 @org.junit.Test
 public void testIsInEnumBlock_notEnum() {
 DetailAST node = createDetailAST(14, "class Foo { }");
 assertFalse(ScopeUtils.isInEnumBlock(node));
 }

 @org.junit.Test
 public void testIsInCodeBlock() {
 DetailAST node = createDetailAST(9, "public class Foo { void foo() { } }");
 assertTrue(ScopeUtils.isInCodeBlock(node));
 }

 @org.junit.Test
 public void testIsInCodeBlock_notCodeBlock() {
 DetailAST node = createDetailAST(14, "class Foo { }");
 assertFalse(ScopeUtils.isInCodeBlock(node));
 }

 @org.junit.Test
 public void testIsOuterMostType() {
 DetailAST node = createDetailAST(14, "class Foo { }");
 assertTrue(ScopeUtils.isOuterMostType(node));
 }

 @org.junit.Test
 public void testIsOuterMostType_notOutermostType() {
 DetailAST node = createDetailAST(14, "class Foo { class InnerClass { } }");
 assertFalse(ScopeUtils.isOuterMostType(node));
 }

 @org.junit.Test
 public void testIsLocalVariableDef() {
 DetailAST node = createDetailAST(10, "class Foo { int x; void foo() { int y; } }");
 DetailAST localVariableDef = node.findFirstToken(10);
 assertTrue(ScopeUtils.isLocalVariableDef(localVariableDef));
 }

 @org.junit.Test
 public void testIsLocalVariableDef_notLocalVariableDef() {
 DetailAST node = createDetailAST(14, "class Foo { int x; void foo() { } }");
 DetailAST localVariableDef = node.findFirstToken(15);
 assertFalse(ScopeUtils.isLocalVariableDef(localVariableDef));
 }

 @org.junit.Test
 public void testIsClassFieldDef() {
 DetailAST node = createDetailAST(10, "class Foo { int x; }");
 DetailAST classFieldDef = node.findFirstToken(10);
 assertTrue(ScopeUtils.isClassFieldDef(classFieldDef));
 }

 @org.junit.Test
 public void testIsInScope() {
 DetailAST node = createDetailAST(14, "class Foo { void foo() { int x; } }");
 DetailAST detailAST = node.findFirstChild();
 Scope expected = Scope.METHOD;
 assertTrue(ScopeUtils.isInScope(detailAST, expected));
 }

 @org.junit.Test
 public void testIsInScope_notInScope() {
 DetailAST node = createDetailAST(14, "class Foo { int x; void foo() { } }");
 DetailAST detailAST = node.findFirstChild();
 Scope expected = Scope.CLASS;
 assertFalse(ScopeUtils.isInScope(detailAST, expected));
 }

 @org.junit.Test
 public void testIsInScope_nullScope() {
 DetailAST node = createDetailAST(14, "class Foo { int x; void foo() { } }");
 DetailAST detailAST = node.findFirstChild();
 assertFalse(ScopeUtils.isInScope(detailAST, null));
 }
}