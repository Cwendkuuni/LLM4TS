package org.objectweb.asm.jip.commons;

import static org.junit.Assert.*;

import org.objectweb.asm.jip.*;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitEnd() {}
 });
 }

 @Test
 public void testVisitVarInsn() {
 // Test int
 sorter.visitVarInsn(Opcodes.ISTORE, 1);
 assertEquals(1, sorter.mapping[1]);
 // Test float
 sorter.visitVarInsn(Opcodes.FSTORE, 2);
 assertEquals(3, sorter.mapping[2]);
 // Test long
 sorter.visitVarInsn(Opcodes.LSTORE, 3);
 assertEquals(5, sorter.mapping[3]);
 // Test double
 sorter.visitVarInsn(Opcodes.DSTORE, 4);
 assertEquals(7, sorter.mapping[4]);
 // Test object
 sorter.visitVarInsn(Opcodes.ASTORE, 5);
 assertEquals(9, sorter.mapping[5]);
 }

 @Test
 public void testVisitIincInsn() {
 sorter.visitIincInsn(1, 1);
 assertEquals(1, sorter.mapping[1] + 1);
 }

 @Test
 public void testVisitMaxs() {
 final int maxStack = 5;
 final int maxLocals = 10;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 // Test int
 sorter.visitLocalVariable("i", Type.INT_TYPE.getDescriptor(), null, null, null, 1);
 assertEquals(1, sorter.mapping[1]);
 // Test float
 sorter.visitLocalVariable("f", Type.FLOAT_TYPE.getDescriptor(), null, null, null, 2);
 assertEquals(3, sorter.mapping[2]);
 // Test long
 sorter.visitLocalVariable("l", Type.LONG_TYPE.getDescriptor(), null, null, null, 3);
 assertEquals(5, sorter.mapping[3]);
 // Test double
 sorter.visitLocalVariable("d", Type.DOUBLE_TYPE.getDescriptor(), null, null, null, 4);
 assertEquals(7, sorter.mapping[4]);
 // Test object
 sorter.visitLocalVariable("o", "Ljava/lang/Object;", null, null, null, 5);
 assertEquals(9, sorter.mapping[5]);
 }

 @Test
 public void testVisitFrame() {
 final Object[] oldLocals = new Object[] { Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.LONG, Opcodes.DOUBLE, null, null, null, null };
 final Object[] locals = new Object[] { Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.DOUBLE, null, null, null, null, null };
 final int maxStack = 10;
 final int maxLocals = 10;
 final Object[] stack = new Object[5];
 final Label start = new Label();
 final Label end = new Label();
 sorter.visitFrame(Opcodes.FRAME_TYPE_NONE, maxLocals, locals, maxStack, stack);
 for (int i = 0; i < maxLocals; i++) {
 assertEquals(i + 1, sorter.mapping[i + 1]);
 }
 sorter.visitFrame(Opcodes.FRAME_TYPE_NONE, maxLocals, oldLocals, maxStack, stack);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithType() {
 sorter.visitFrame(Opcodes.FRAME_TYPE, maxLocals, locals, maxStack, stack);
 }

 @Test
 public void testNewLocal() {
 final Type intType = Type.INT_TYPE;
 final Type floatType = Type.FLOAT_TYPE;
 final Type longType = Type.LONG_TYPE;
 final Type doubleType = Type.DOUBLE_TYPE;
 final Type objectType = Type.getType("java/lang/String");
 final int intLocal = sorter.newLocal(intType);
 assertEquals(12, sorter.mapping[intLocal]);
 final int floatLocal = sorter.newLocal(floatType);
 assertEquals(13, sorter.mapping[floatLocal]);
 final int longLocal = sorter.newLocal(longType);
 assertEquals(14, sorter.mapping[longLocal]);
 final int doubleLocal = sorter.newLocal(doubleType);
 assertEquals(15, sorter.mapping[doubleLocal]);
 final int objectLocal = sorter.newLocal(objectType);
 assertEquals(16, sorter.mapping[objectLocal]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 sorter.newLocal(Type.BOOLEAN_TYPE);
 }

 @Test
 public void testSetLocalType() {
 final int local = 14;
 final Type type = Type.LONG_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(type, sorter.mapping[local + 1]);
 }

 @Test
 public void testSetFrameLocal() {
 final int local = 15;
 final Object type = "java/lang/String";
 sorter.setFrameLocal(local, type);
 assertEquals("java/lang/String", sorter.mapping[local + 1]);
 }

 @Test
 public void testRemap() {
 final int var = 7;
 final Type type = Type.DOUBLE_TYPE;
 final int expected = 11;
 assertEquals(expected, sorter.remap(var, type));
 }

 @Test
 public void testNewLocalMapping() {
 final Type type = Type.DOUBLE_TYPE;
 final int expected = 16;
 assertEquals(expected, sorter.newLocalMapping(type));
 }
}