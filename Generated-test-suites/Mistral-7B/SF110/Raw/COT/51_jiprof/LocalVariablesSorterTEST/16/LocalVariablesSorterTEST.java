package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 private byte[] classData = new byte[] {
 // ... class data goes here, use a ClassReader to read it
 };

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 // empty method body
 }
 });
 assertTrue(sorter.changed == false);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });

 Type type = Type.INT_TYPE;
 sorter.visitVarInsn(Opcodes.ISTORE, 0);
 int remappedVar = sorter.remap(0, type);
 assertEquals("The remapped variable should be the same as the original one.", 0, remappedVar);
 }

 @Test
 public void testVisitVarInsnWithLongAndDouble() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });

 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 sorter.visitVarInsn(Opcodes.LSTORE, 0);
 int remappedVarLong = sorter.remap(0, longType);
 assertEquals("The remapped variable for long should be even.", remappedVarLong, remappedVarLong + 1);
 sorter.visitVarInsn(Opcodes.DSTORE, 0);
 int remappedVarDouble = sorter.remap(0, doubleType);
 assertEquals("The remapped variable for double should be even.", remappedVarDouble, remappedVarDouble + 1);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitIincInsn(int var, int increment) {
 sorter.visitIincInsn(var, increment);
 }
 });

 Type intType = Type.INT_TYPE;
 int originalLocalVariable = 0;
 int increment = 1;
 sorter.visitIincInsn(originalLocalVariable, increment);
 int remappedVar = sorter.remap(originalLocalVariable, intType);
 int expectedRemappedVar = sorter.nextLocal - intType.getSize();
 assertEquals("The remapped variable should be updated.", expectedRemappedVar, remappedVar);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 super.visitMaxs(maxStack, maxLocals);
 }
 });

 int maxStack = 5;
 int maxLocals = 10;
 int expectedNextLocal = 15;
 sorter.visitMaxs(maxStack, maxLocals);
 int nextLocal = sorter.nextLocal;
 assertEquals("The next local should be updated.", expectedNextLocal, nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });

 String name = "name";
 Type intType = Type.INT_TYPE;
 Label startLabel = new Label();
 Label endLabel = new Label();
 int index = 0;
 Type descriptor = Type.getType(intType.getDescriptor() + "B");
 sorter.visitLocalVariable(name, descriptor.getDescriptor(), signature, startLabel, endLabel, index);
 int remappedIndex = sorter.remap(index, intType);
 assertEquals("The remapped index should be the same as the original one.", index, remappedIndex);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });

 Object[] originalLocals = new Object[5];
 Object[] stackElements = new Object[5];
 sorter.visitFrame(Opcodes.FRAME_CODE, 5, originalLocals, 5, stackElements);
 Object[] newLocals = sorter.newLocals;
 assertEquals("The new locals should be the same as the original ones.", Arrays.toString(originalLocals), Arrays.toString(newLocals));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithoutExpandFrames() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 sorter.visitFrame(Opcodes.FRAME_CODE, 5, new Object[0], 5, new Object[0]);
 fail("Should throw an IllegalStateException.");
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 int localVariable1 = sorter.newLocal(Type.INT_TYPE);
 int localVariable2 = sorter.newLocal(Type.INT_TYPE);
 int localVariable3 = sorter.newLocal(Type.INT_TYPE);
 int localVariable4 = sorter.newLocal(Type.INT_TYPE);
 int localVariable5 = sorter.newLocal(Type.INT_TYPE);
 int localVariable6 = sorter.newLocal(Type.LONG_TYPE);
 int localVariable7 = sorter.newLocal(Type.DOUBLE_TYPE);
 int localVariable8 = sorter.newLocal(Type.FLOAT_TYPE);
 int localVariable9 = sorter.newLocal(Type.getType("java/lang/String"));
 int localVariable10 = sorter.newLocal(Type.getType("java/lang/Object"));
 }
 });

 int expectedNextLocal1 = 7;
 int expectedNextLocal2 = 8;
 int expectedNextLocal3 = 9;
 int expectedNextLocal4 = 10;
 int expectedNextLocal5 = 11;
 int expectedNextLocal6 = 12;
 int expectedNextLocal7 = 13;
 int expectedNextLocal8 = 14;
 int expectedNextLocal9 = 15;
 int expectedNextLocal10 = 16;
 assertEquals("The next local should be updated.", expectedNextLocal1, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable1", "I", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal2, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable2", "I", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal3, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable3", "I", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal4, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable4", "I", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal5, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable5", "I", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal6, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable6", "J", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal7, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable7", "D", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal8, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable8", "F", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal9, sorter.nextLocal);
 sorter.visitLocalVariable("localVariable9", "Ljava/lang/String;", null, new Label(), new Label(), 0);
 assertEquals("The next local should be updated.", expectedNextLocal10, sorter.nextLocal);
 }

 @Test
 public void testSetLocalType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 int localVariable1 = sorter.newLocal(Type.INT_TYPE);
 int localVariable2 = sorter.newLocal(Type.INT_TYPE);
 }
 });

 int localVariable1 = 0;
 int localVariable2 = 1;
 Type intType = Type.INT_TYPE;
 sorter.setLocalType(localVariable1, intType);
 sorter.setLocalType(localVariable2, intType);
 assertEquals("The setLocalType should not change the local variable index.", localVariable1, sorter.remap(localVariable1, intType));
 assertEquals("The setLocalType should not change the local variable index.", localVariable2, sorter.remap(localVariable2, intType));
 }

 @Test
 public void testSetFrameLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 int localVariable1 = sorter.newLocal(Type.INT_TYPE);
 int localVariable2 = sorter.newLocal(Type.INT_TYPE);
 }
 });

 int localVariable1 = 0;
 int localVariable2 = 1;
 String intTypeDescriptor = "I";
 Object intObject1 = new Integer(0);
 Object intObject2 = new Integer(1);
 sorter.setFrameLocal(localVariable1, intObject1);
 sorter.setFrameLocal(localVariable2, intObject2);
 Object[] newLocals = sorter.newLocals;
 assertEquals("The new locals should have the correct values.", intObject1, newLocals[localVariable1]);
 assertEquals("The new locals should have the correct values.", intObject2, newLocals[localVariable2]);
 }

 @Test
 public void testRemap() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 int localVariable1 = sorter.newLocal(Type.INT_TYPE);
 int localVariable2 = sorter.newLocal(Type.INT_TYPE);
 }
 });

 int localVariable1 = 0;
 int localVariable2 = 1;
 Type intType = Type.INT_TYPE;
 int originalMapping1 = sorter.remap(localVariable1, intType);
 int originalMapping2 = sorter.remap(localVariable2, intType);
 assertEquals("The original mappings should be different.", originalMapping1, originalMapping2);
 assertEquals("The remapped variable should be the same as the original one.", localVariable1, sorter.remap(originalMapping1, intType));
 assertEquals("The remapped variable should be the same as the original one.", localVariable2, sorter.remap(originalMapping2, intType));
 }

 @Test
 public void testNewLocalMapping() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {
 int localVariable1 = sorter.newLocal(Type.INT_TYPE);
 int localVariable2 = sorter.newLocal(Type.INT_TYPE);
 }
 });

 int localVariable1 = 0;
 int localVariable2 = 1;
 Type intType = Type.INT_TYPE;
 int newMapping1 = sorter.newLocalMapping(intType);
 int newMapping2 = sorter.newLocalMapping(intType);
 assertEquals("The new mappings should be different.", newMapping1, newMapping2);
 assertEquals("The new mapping should be the next local variable index.", newMapping1, sorter.nextLocal - intType.getSize());
 assertEquals("The new mapping should be the next local variable index.", newMapping2, sorter.nextLocal - intType.getSize());
 }
}