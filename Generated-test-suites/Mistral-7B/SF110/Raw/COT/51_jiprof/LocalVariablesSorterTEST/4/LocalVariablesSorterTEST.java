package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 mv = mock(MethodVisitor.class);
 localVariablesSorter = new LocalVariablesSorter(0, "", mv);
 }

 @Test
 public void testVisitVarInsnWithInt() {
 final int var = 0;
 final Type type = Type.INT_TYPE;
 localVariablesSorter.visitVarInsn(21, var);
 assertEquals(var, localVariablesSorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 final int var = 0;
 final Type type = Type.FLOAT_TYPE;
 localVariablesSorter.visitVarInsn(23, var);
 assertEquals(var, localVariablesSorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithLong() {
 final int var = 0;
 final Type type = Type.LONG_TYPE;
 localVariablesSorter.visitVarInsn(22, var);
 assertEquals(var, localVariablesSorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 final int var = 0;
 final Type type = Type.DOUBLE_TYPE;
 localVariablesSorter.visitVarInsn(24, var);
 assertEquals(var, localVariablesSorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithObject() {
 final int var = 0;
 final Type type = Type.getType("java/lang/String");
 localVariablesSorter.visitVarInsn(25, var);
 assertEquals(var, localVariablesSorter.remap(var, type));
 }

 @Test
 public void testVisitIincInsn() {
 final int var = 0;
 final int increment = 1;
 localVariablesSorter.visitIincInsn(var, increment);
 verify(mv).visitIincInsn(localVariablesSorter.remap(var, Type.INT_TYPE), increment);
 }

 @Test
 public void testVisitMaxs() {
 final int maxStack = 1;
 final int maxLocals = 2;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 verify(mv).visitMaxs(maxStack, localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariableWithNullName() {
 final String name = null;
 final String desc = "I";
 final String signature = null;
 final Label start = null;
 final Label end = null;
 final int index = 0;
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 assertNull(name);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitLocalVariableWithInvalidType() {
 final String name = "";
 final String desc = "I";
 final String signature = null;
 final Label start = null;
 final Label end = null;
 final int index = 0;
 localVariablesSorter.visitLocalVariable(name, desc, "Ljava/lang/String;", start, end, index);
 }

 @Test
 public void testVisitLocalVariableWithValidData() {
 final String name = "test";
 final String desc = "I";
 final String signature = null;
 final Label start = null;
 final Label end = null;
 final int index = 0;
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 final int newIndex = localVariablesSorter.remap(index, Type.getType(desc));
 assertEquals(newIndex, localVariablesSorter.mv.getLocalVariable(name, desc, signature).index);
 }

 @Test
 public void testVisitFrameWithValidData() {
 final int type = -1;
 final int nLocal = 1;
 final Object[] local = new Object[1];
 final int nStack = 1;
 final Object[] stack = new Object[1];
 localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
 verify(mv).visitFrame(type, nLocal, local, nStack, stack);
 }

 @Test
 public void testVisitFrameWithChangedData() {
 final int type = -1;
 final int nLocal = 1;
 final Object[] oldLocal = new Object[]{null};
 final Object[] local = new Object[1];
 final int nStack = 1;
 final Object[] stack = new Object[1];
 localVariablesSorter.setChanged(true);
 localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
 final Object[] newLocal = new Object[localVariablesSorter.newLocals.length];
 System.arraycopy(localVariablesSorter.newLocals, 0, newLocal, 0, localVariablesSorter.newLocals.length);
 assertEquals(newLocal, local);
 }

 @Test
 public void testNewLocalWithIntType() {
 final Type type = Type.INT_TYPE;
 final int local = localVariablesSorter.newLocal(type);
 assertEquals(local, localVariablesSorter.remap(local, type));
 }

 @Test
 public void testNewLocalWithFloatType() {
 final Type type = Type.FLOAT_TYPE;
 final int local = localVariablesSorter.newLocal(type);
 assertEquals(local, localVariablesSorter.remap(local, type));
 }

 @Test
 public void testNewLocalWithLongType() {
 final Type type = Type.LONG_TYPE;
 final int local = localVariablesSorter.newLocal(type);
 assertEquals(local, localVariablesSorter.remap(local, type));
 }

 @Test
 public void testNewLocalWithDoubleType() {
 final Type type = Type.DOUBLE_TYPE;
 final int local = localVariablesSorter.newLocal(type);
 assertEquals(local, localVariablesSorter.remap(local, type));
 }

 @Test
 public void testNewLocalWithObjectType() {
 final Type type = Type.getType("java/lang/String");
 final int local = localVariablesSorter.newLocal(type);
 assertEquals(local, localVariablesSorter.remap(local, type));
 }
}