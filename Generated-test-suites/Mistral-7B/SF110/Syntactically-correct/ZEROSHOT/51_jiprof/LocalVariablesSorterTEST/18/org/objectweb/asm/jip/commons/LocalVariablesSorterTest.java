package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;

 @Test
 public void testConstructor() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {}
 });
 assertEquals(1, localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitVarInsnWithIntType() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, localVariablesSorter.remap(var, Type.INT_TYPE));
 }

 @Override
 public void visitEnd() {}
 });

 int[] input = {1, 2, 3, 4, 5};
 int[] expectedOutput = {5, 4, 3, 2, 1};

 localVariablesSorter.setLocalType(0, Type.INT_TYPE);
 localVariablesSorter.visitVarInsn(21, 0); // visitVarInsn with int type and var index 0
 localVariablesSorter.visitVarInsn(21, 1); // visitVarInsn with int type and var index 1
 localVariablesSorter.visitVarInsn(21, 2); // visitVarInsn with int type and var index 2
 localVariablesSorter.visitVarInsn(21, 3); // visitVarInsn with int type and var index 3
 localVariablesSorter.visitVarInsn(21, 4); // visitVarInsn with int type and var index 4

 for (int i = 0; i < input.length; i++) {
 int remappedVarIndex = localVariablesSorter.remap(i, Type.INT_TYPE);
 assertEquals(expectedOutput[i], remappedVarIndex);
 }
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, localVariablesSorter.remap(var, Type.LONG_TYPE));
 }

 @Override
 public void visitEnd() {}
 });

 long[] input = {1L, 2L, 3L, 4L, 5L};
 long[] expectedOutput = {5, 4, 3, 2, 1};

 localVariablesSorter.setLocalType(0, Type.LONG_TYPE);
 localVariablesSorter.visitVarInsn(22, 0); // visitVarInsn with long type and var index 0
 localVariablesSorter.visitVarInsn(22, 1); // visitVarInsn with long type and var index 1
 localVariablesSorter.visitVarInsn(22, 2); // visitVarInsn with long type and var index 2
 localVariablesSorter.visitVarInsn(22, 3); // visitVarInsn with long type and var index 3
 localVariablesSorter.visitVarInsn(22, 4); // visitVarInsn with long type and var index 4

 for (int i = 0; i < input.length; i++) {
 int remappedVarIndex = localVariablesSorter.remap(i, Type.LONG_TYPE);
 assertEquals(expectedOutput[i], remappedVarIndex);
 }
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, localVariablesSorter.remap(var, Type.FLOAT_TYPE));
 }

 @Override
 public void visitEnd() {}
 });

 float[] input = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
 float[] expectedOutput = {5, 4, 3, 2, 1};

 localVariablesSorter.setLocalType(0, Type.FLOAT_TYPE);
 localVariablesSorter.visitVarInsn(23, 0); // visitVarInsn with float type and var index 0
 localVariablesSorter.visitVarInsn(23, 1); // visitVarInsn with float type and var index 1
 localVariablesSorter.visitVarInsn(23, 2); // visitVarInsn with float type and var index 2
 localVariablesSorter.visitVarInsn(23, 3); // visitVarInsn with float type and var index 3
 localVariablesSorter.visitVarInsn(23, 4); // visitVarInsn with float type and var index 4

 for (int i = 0; i < input.length; i++) {
 int remappedVarIndex = localVariablesSorter.remap(i, Type.FLOAT_TYPE);
 assertEquals(expectedOutput[i], remappedVarIndex);
 }
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, localVariablesSorter.remap(var, Type.getType("java/lang/String")));
 }

 @Override
 public void visitEnd() {}
 });

 String[] input = {"hello", "world", "test", "string", "object"};
 int[] expectedOutput = {5, 4, 3, 2, 1};

 localVariablesSorter.setLocalType(0, Type.getType("java/lang/String"));
 localVariablesSorter.visitVarInsn(23, 0); // visitVarInsn with object type and var index 0
 localVariablesSorter.visitVarInsn(23, 1); // visitVarInsn with object type and var index 1
 localVariablesSorter.visitVarInsn(23, 2); // visitVarInsn with object type and var index 2
 localVariablesSorter.visitVarInsn(23, 3); // visitVarInsn with object type and var index 3
 localVariablesSorter.visitVarInsn(23, 4); // visitVarInsn with object type and var index 4

 for (int i = 0; i < input.length; i++) {
 int remappedVarIndex = localVariablesSorter.remap(i, Type.getType("java/lang/String"));
 assertEquals(expectedOutput[i], remappedVarIndex);
 }
 }

 @Test
 public void testVisitIincInsn() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitIincInsn(int var, int increment) {
 localVariablesSorter.visitIincInsn(localVariablesSorter.remap(var, Type.INT_TYPE), increment);
 }

 @Override
 public void visitEnd() {}
 });

 int[] inputVarIndices = {1, 2, 3, 4, 5};
 int[] expectedIncrementValues = {1, 2, 3, 4, 5};
 int[] inputRemappedVarIndices = {};

 for (int i = 0; i < inputVarIndices.length; i++) {
 int remappedVarIndex = localVariablesSorter.remap(inputVarIndices[i], Type.INT_TYPE);
 inputRemappedVarIndices = addIntToArray(inputRemappedVarIndices, remappedVarIndex);
 localVariablesSorter.visitIincInsn(remappedVarIndex, expectedIncrementValues[i]);
 }

 int[] output = localVariablesSorter.mapping;
 for (int i = 0; i < inputVarIndices.length; i++) {
 int expectedIndex = inputVarIndices[i] + expectedIncrementValues[i];
 assertEquals(expectedIndex, output[i]);
 }
 }

 @Test
 public void testVisitMaxs() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 localVariablesSorter.visitMaxs(maxStack, localVariablesSorter.nextLocal);
 }

 @Override
 public void visitEnd() {}
 });

 int[] inputMaxLocals = {1, 2, 3, 4, 5};
 int expectedMaxLocals = 5;

 localVariablesSorter.visitMaxs(10, 1);

 for (int i = 0; i < inputMaxLocals.length; i++) {
 int local = inputMaxLocals[i];
 localVariablesSorter.nextLocal = local;
 localVariablesSorter.visitVarInsn(21, 0); // visitVarInsn with int type and var index 0
 localVariablesSorter.visitVarInsn(21, 1); // visitVarInsn with int type and var index 1
 localVariablesSorter.visitVarInsn(21, 2); // visitVarInsn with int type and var index 2
 localVariablesSorter.visitVarInsn(21, 3); // visitVarInsn with int type and var index 3
 localVariablesSorter.visitVarInsn(21, 4); // visitVarInsn with int type and var index 4
 localVariablesSorter.visitVarInsn(21, 5); // visitVarInsn with int type and var index 5

 assertEquals(expectedMaxLocals, localVariablesSorter.nextLocal);
 }
 }

 @Test
 public void testVisitLocalVariable() {
 localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, localVariablesSorter.remap(index, Type.getType(desc.substring(1))));
 }

 @Override
 public void visitEnd() {}
 });

 String[] inputNames = {"a", "b", "c", "d", "e"};
 String[] inputDescs = {"I", "F", "J", "D", "Ljava/lang/String;"};
 String[] expectedOutputNames = {"e", "d", "c", "b", "a"};

 for (int i = 0; i < inputNames.length; i++) {
 String name = inputNames[i];
 String desc = inputDescs[i];
 localVariablesSorter.visitLocalVariable(name, desc, null, null, null, i);
 }

 String[] outputNames = new String[localVariablesSorter.mapping.length];
 for (int i = 0; i < localVariablesSorter.mapping.length; i++) {
 int index = localVariablesSorter.mapping[i];
 outputNames[i] = inputNames[index];
 }

 assertArrayEquals(expectedOutputNames, outputNames);
 }

 private int[] addIntToArray(int[] array, int value) {
 int[] result = new int[array.length + 1];
 System.arraycopy(array, 0, result, 0, array.length);
 result[array.length] = value;
 return result;
 }
}