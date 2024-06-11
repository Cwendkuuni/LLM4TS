package org.objectweb.asm.jip.commons;

import org.objectweb.asm.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 mv = new Bytecode();
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testVisitVarInsnWithInt() {
 final int var = 1;
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, var);
 final int expected = var;
 Assert.assertEquals(expected, localVariablesSorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitVarInsnWithLong() {
 final int var = 1;
 localVariablesSorter.visitVarInsn(Opcodes.LLOAD, var);
 final int expected = 2 * var;
 Assert.assertEquals(expected, localVariablesSorter.remap(var, Type.LONG_TYPE));
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 final int var = 1;
 localVariablesSorter.visitVarInsn(Opcodes.FLOAD, var);
 final int expected = 2 * var + Type.FLOAT_SIZE;
 Assert.assertEquals(expected, localVariablesSorter.remap(var, Type.FLOAT_TYPE));
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 final int var = 1;
 localVariablesSorter.visitVarInsn(Opcodes.DLOAD, var);
 final int expected = 2 * var + Type.DOUBLE_SIZE;
 Assert.assertEquals(expected, localVariablesSorter.remap(var, Type.DOUBLE_TYPE));
 }

 @Test
 public void testVisitVarInsnWithObject() {
 final String objName = "TestObject";
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 final int expected = localVariablesSorter.newLocalMapping(Type.getObjectType(objName));
 Assert.assertEquals(expected, localVariablesSorter.remap(0, Type.getObjectType(objName)));
 }

 @Test
 public void testVisitIincInsn() {
 final int var = 1;
 localVariablesSorter.visitIincInsn(var, 1);
 final int expected = var;
 Assert.assertEquals(expected, localVariablesSorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 final int maxStack = 1;
 final int maxLocals = 2;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 final int expected = maxLocals;
 Assert.assertEquals(expected, localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 final String name = "testName";
 final String desc = "I";
 final String signature = null;
 final Label start = new Label();
 final Label end = new Label();
 final int index = 1;
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 final int expected = index;
 Assert.assertEquals(expected, localVariablesSorter.remap(index, Type.getType(desc)));
 }

 @Test
 public void testVisitFrame() {
 final Object[] locals = new Object[1];
 locals[0] = Opcodes.LONG;
 final int nLocal = locals.length;
 final int nStack = 0;
 final Object[] stack = new Object[0];
 localVariablesSorter.visitFrame(Opcodes.FRAME_TYPE, nLocal, locals, nStack, stack);
 final int expected = nLocal;
 Assert.assertEquals(expected, localVariablesSorter.nextLocal);
 }
}