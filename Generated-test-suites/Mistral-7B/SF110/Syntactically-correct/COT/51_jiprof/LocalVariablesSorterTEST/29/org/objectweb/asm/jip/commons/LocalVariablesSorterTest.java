package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Opcodes;

import org.junit.Test;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(0) {
 // Empty visit methods
 });
 }

 @Test
 public void testConstructor() {
 assertEquals(40, sorter.mapping.length);
 assertEquals(20, sorter.newLocals.length);
 assertEquals(1, sorter.nextLocal);
 assertNull(sorter.changed);
 assertNull(sorter.firstLocal);
 }

 @Test
 public void testVisitVarInsn() {
 final int var = 1;
 final Type type = Type.INT_TYPE;
 final int index = this.sorter.remap(var, type);
 sorter.visitVarInsn(Opcodes.ILOAD, index);
 assertEquals(index, sorter.remap(var, type));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithNegativeIndex() {
 final int var = -1;
 final Type type = Type.INT_TYPE;
 final int index = this.sorter.remap(var, type);
 sorter.visitVarInsn(Opcodes.ILOAD, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithInvalidType() {
 final int var = 1;
 final Type type = Type.LONG_TYPE;
 final int index = this.sorter.remap(var, type);
 sorter.visitVarInsn(Opcodes.ILOAD, index);
 }

 @Test
 public void testVisitIincInsn() {
 final int var = 1;
 final int increment = 1;
 sorter.visitIincInsn(var, increment);
 assertEquals(var + increment, this.sorter.remap(var, Type.INT_TYPE));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitIincInsnWithNegativeIndex() {
 final int var = -1;
 final int increment = 1;
 sorter.visitIincInsn(var, increment);
 }

 @Test
 public void testVisitMaxs() {
 final int maxStack = 1;
 final int maxLocals = 2;
 final int nextLocal = 3;
 this.sorter.nextLocal = nextLocal;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(nextLocal, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 final String name = "testName";
 final String desc = "I";
 final String signature = "()V";
 final Label start = new Label();
 final Label end = new Label();
 final int index = 1;
 final Type type = Type.INT_TYPE;
 final int newIndex = this.sorter.remap(index, type);
 sorter.visitLocalVariable(name, desc, signature, start, end, newIndex);
 assertEquals(newIndex, sorter.remap(index, type));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitLocalVariableWithNegativeIndex() {
 final String name = "testName";
 final String desc = "I";
 final String signature = "()V";
 final Label start = new Label();
 final Label end = new Label();
 final int index = -1;
 final Type type = Type.INT_TYPE;
 final int newIndex = this.sorter.remap(index, type);
 sorter.visitLocalVariable(name, desc, signature, start, end, newIndex);
 }

 @Test
 public void testVisitFrame() {
 final Label start = new Label();
 final Label end = new Label();
 final int maxLocals = 2;
 final Object[] local = new Object[maxLocals];
 final int nStack = 1;
 final Object[] stack = new Object[nStack];
 final int type = 0;
 sorter.visitFrame(type, maxLocals, local, nStack, stack);
 assertNotNull(sorter.newLocals);
 assertEquals(maxLocals, sorter.newLocals.length);
 }

 @Test
 public void testVisitFrameWithChanged() {
 final Label start = new Label();
 final Label end = new Label();
 final int maxLocals = 2;
 final Object[] local = new Object[maxLocals];
 final int nStack = 1;
 final Object[] stack = new Object[nStack];
 final int type = 1;
 sorter.changed = true;
 sorter.visitFrame(type, maxLocals, local, nStack, stack);
 assertNotNull(sorter.newLocals);
 assertEquals(maxLocals, sorter.newLocals.length);
 }

 @Test
 public void testNewLocal() {
 final Type type = Type.INT_TYPE;
 final int local = sorter.newLocal(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test
 public void testNewLocalWithObjectType() {
 final Type type = Type.getType("java/lang/String");
 final int local = sorter.newLocal(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test
 public void testSetLocalType() {
 final int local = 1;
 final Type type = Type.INT_TYPE;
 sorter.setLocalType(local, type);
 }

 @Test
 public void testSetFrameLocal() {
 final int local = 1;
 final Object type = "java/lang/String";
 final int newLocal = sorter.newLocal(Type.getType((String)type));
 sorter.setFrameLocal(local, type);
 assertEquals(newLocal, sorter.remap(local, Type.getType((String)type)));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetFrameLocalWithOutOfBoundsIndex() {
 final int local = 50;
 final Object type = "java/lang/String";
 sorter.setFrameLocal(local, type);
 }

 @Test
 public void testRemap() {
 final int var = 1;
 final Type type = Type.INT_TYPE;
 final int index = this.sorter.remap(var, type);
 assertEquals(index, sorter.remap(var, type));
 }

 @Test
 public void testRemapWithNullType() {
 final int var = 1;
 final int index = this.sorter.remap(var, null);
 assertEquals(var, index);
 }

 @Test
 public void testRemapWithNewLocal() {
 final int local = this.sorter.newLocal(Type.INT_TYPE);
 final int index = this.sorter.remap(local, Type.INT_TYPE);
 assertEquals(local, index);
 }

 @Test
 public void testRemapWithNewLocalObjectType() {
 final Type type = Type.getType("java/lang/String");
 final int local = this.sorter.newLocal(type);
 final int index = this.sorter.remap(local, type);
 assertEquals(local, index);
 }

 @Test
 public void testRemapWithOldLocal() {
 final int var = 1;
 final int index = this.sorter.remap(var, Type.INT_TYPE);
 final int newLocal = this.sorter.newLocal(Type.INT_TYPE);
 assertEquals(newLocal, this.sorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testRemapWithOldLocalObjectType() {
 final Type type = Type.getType("java/lang/String");
 final int var = 1;
 final int index = this.sorter.remap(var, type);
 final int newLocal = this.sorter.newLocal(type);
 assertEquals(newLocal, this.sorter.remap(var, type));
 }

 @Test
 public void testNewLocalMapping() {
 final Type type = Type.INT_TYPE;
 final int local = this.sorter.newLocalMapping(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test
 public void testNewLocalMappingWithObjectType() {
 final Type type = Type.getType("java/lang/String");
 final int local = this.sorter.newLocalMapping(type);
 assertEquals(local, sorter.remap(local, type));
 }

}