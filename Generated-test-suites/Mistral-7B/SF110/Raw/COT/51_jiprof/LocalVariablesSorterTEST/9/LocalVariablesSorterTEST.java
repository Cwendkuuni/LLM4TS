package org.objectweb.asm.jip.commons.test;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @Test
 public void testLocalVariablesSorterConstructor() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 Assert.assertNotNull(sorter.mapping);
 Assert.assertNotNull(sorter.newLocals);
 Assert.assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(Opcodes.ALOAD, 0);
 Assert.assertEquals(0, sorter.remap(0, Type.getType("Ljava/lang/Object;")));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameNoExpand() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitFrame(Opcodes.FRAME_same, 0, new Object[0], 0, new Object[0]);
 }

 @Test
 public void testVisitFrameExpand() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitFrame(Opcodes.FRAME_same, 1, new Object[] { Type.getType("I") }, 0, new Object[0]);
 Assert.assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitLocalVariable("test", "I", null, new Label(0), new Label(1), 0);
 Assert.assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitLocalVariableWithSignature() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitLocalVariable("test", "II", null, new Label(0), new Label(1), 0);
 Assert.assertEquals(0, sorter.remap(0, Type.TYPE_INT_ARRAY));
 }

 @Test
 public void testVisitIincInsn() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitIincInsn(0, 1);
 Assert.assertEquals(1, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitMaxs(5, 10);
 Assert.assertEquals(10, sorter.nextLocal);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnInvalidOpcode() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(99, 0);
 }

 @Test
 public void testVisitVarInsnWithLong() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(Opcodes.LLOAD, 0);
 Assert.assertEquals(0, sorter.remap(0, Type.LONG_TYPE));
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(Opcodes.DLOAD, 0);
 Assert.assertEquals(0, sorter.remap(0, Type.DOUBLE_TYPE));
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(Opcodes.FLOAD, 0);
 Assert.assertEquals(0, sorter.remap(0, Type.FLOAT_TYPE));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithNullType() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 sorter.visitVarInsn(Opcodes.ALOAD, 0);
 sorter.visitVarInsn(Opcodes.ALOAD, 0); // second time with null type
 }

 @Test
 public void testNewLocal() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final Type intType = Type.INT_TYPE;
 final int newLocal = sorter.newLocal(intType);
 Assert.assertEquals(1, newLocal);
 }

 @Test
 public void testNewLocalWithObjectType() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final Type objectType = Type.getType("Ljava/lang/String;");
 final int newLocal = sorter.newLocal(objectType);
 Assert.assertEquals(1, newLocal);
 }

 @Test
 public void testSetLocalType() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final int local = sorter.newLocal(Type.INT_TYPE);
 sorter.setLocalType(local, Type.FLOAT_TYPE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLocalTypeWithInvalidType() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final int local = sorter.newLocal(Type.INT_TYPE);
 sorter.setLocalType(local, Type.getObjectType("java/lang/String"));
 }

 @Test
 public void testSetFrameLocal() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final int local = sorter.newLocal(Type.INT_TYPE);
 final Object object = new Object();
 sorter.setFrameLocal(local, object);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetFrameLocalWithNullObject() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final int local = sorter.newLocal(Type.INT_TYPE);
 sorter.setFrameLocal(local, null);
 }

 @Test
 public void testRemapWithInvalidVar() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 Assert.assertEquals(-1, sorter.remap(10, Type.INT_TYPE));
 }

 @Test
 public void testRemapWithValidVar() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final int local = sorter.newLocal(Type.INT_TYPE);
 Assert.assertEquals(local, sorter.remap(local, Type.INT_TYPE));
 }

 @Test
 public void testNewLocalMapping() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final Type intType = Type.INT_TYPE;
 final int newLocal = sorter.newLocalMapping(intType);
 Assert.assertEquals(sorter.nextLocal, newLocal);
 }

 @Test
 public void testNewLocalMappingWithObjectType() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {});
 final Type objectType = Type.getType("Ljava/lang/String;");
 final int newLocal = sorter.newLocalMapping(objectType);
 Assert.assertEquals(sorter.nextLocal + objectType.getSize(), newLocal);
 }

}