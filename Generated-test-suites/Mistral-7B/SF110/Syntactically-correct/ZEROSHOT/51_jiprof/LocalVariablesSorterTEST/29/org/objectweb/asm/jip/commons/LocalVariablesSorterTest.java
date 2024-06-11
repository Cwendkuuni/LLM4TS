package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 }, null);

 Type type = Type.INT_TYPE;
 int varIndex = 0;
 sorter.visitVarInsn(Opcodes.ISTORE, varIndex);
 int remappedVarIndex = sorter.remap(varIndex, type);
 assertEquals(varIndex, remappedVarIndex);

 type = Type.FLOAT_TYPE;
 varIndex = 1;
 sorter.visitVarInsn(Opcodes.FSTORE, varIndex);
 remappedVarIndex = sorter.remap(varIndex, type);
 assertEquals(varIndex + 1, remappedVarIndex);

 type = Type.LONG_TYPE;
 varIndex = 2;
 sorter.visitVarInsn(Opcodes.LSTORE, varIndex);
 remappedVarIndex = sorter.remap(varIndex, type);
 assertEquals(varIndex + 2, remappedVarIndex);

 type = Type.DOUBLE_TYPE;
 varIndex = 3;
 sorter.visitVarInsn(Opcodes.DSTORE, varIndex);
 remappedVarIndex = sorter.remap(varIndex, type);
 assertEquals(varIndex + 3, remappedVarIndex);

 type = Type.getType("Ljava/lang/String;");
 varIndex = 4;
 sorter.visitVarInsn(Opcodes.ASTORE, varIndex);
 remappedVarIndex = sorter.remap(varIndex, type);
 assertEquals(varIndex + 5, remappedVarIndex);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitIincInsn(int var, int increment) {
 super.visitIincInsn(var, increment);
 }
 }, null);

 int varIndex = 0;
 sorter.visitIincInsn(varIndex, 1);
 int remappedVarIndex = sorter.remap(varIndex, Type.INT_TYPE);
 assertEquals(varIndex, remappedVarIndex);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 super.visitMaxs(maxStack, maxLocals);
 }
 }, null);

 int maxStack = 10;
 int maxLocals = 5;
 sorter.visitMaxs(maxStack, maxLocals);
 int nextLocal = sorter.nextLocal;
 int expectedNextLocal = maxStack + maxLocals;
 assertEquals(expectedNextLocal, nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 }, null);

 String name = "test";
 Type type = Type.INT_TYPE;
 int index = 0;
 Label startLabel = new Label();
 Label endLabel = new Label();
 sorter.visitLocalVariable(name, desc, signature, startLabel, endLabel, index);
 int remappedIndex = sorter.remap(index, type);
 assertEquals(index + 1, remappedIndex);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 }, null);

 Object[] local = new Object[3];
 local[0] = Opcodes.INTEGER;
 local[1] = Opcodes.FLOAT;
 local[2] = Opcodes.LONG;
 int[] expectedNewLocals = { 1, 2, 3 };
 int[] newLocals = sorter.visitFrame(type, 3, local, 1, stack).localVariables;
 assertArrayEquals(expectedNewLocals, newLocals);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUB, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }

 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 }, null);

 int i = 0;
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type stringType = Type.getType("Ljava/lang/String;");

 assertEquals(0, sorter.nextLocal);
 int local1 = sorter.newLocal(intType);
 assertEquals(1, sorter.nextLocal);
 int local2 = sorter.newLocal(floatType);
 assertEquals(3, sorter.nextLocal);
 int local3 = sorter.newLocal(longType);
 assertEquals(7, sorter.nextLocal);
 int local4 = sorter.newLocal(stringType);
 assertEquals(13, sorter.nextLocal);
 }
}