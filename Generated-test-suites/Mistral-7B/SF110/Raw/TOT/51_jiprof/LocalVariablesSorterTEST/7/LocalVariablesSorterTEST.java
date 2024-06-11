package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, 0);
 }
 });
 Type type = Type.INT_TYPE;
 localVariablesSorter.visitVarInsn(21, 0); // int i = 5;
 localVariablesSorter.visitVarInsn(21, 1); // int j = 10;
 localVariablesSorter.visitVarInsn(22, 2); // long l = 1L;
 localVariablesSorter.visitVarInsn(24, 3); // double d = 3.14;
 localVariablesSorter.visitVarInsn(23, 4); // float f = 2.718F;
 localVariablesSorter.visitVarInsn(56, 5); // Object obj = new Object();
 localVariablesSorter.visitVarInsn(54, 6); // int k = 15;
 localVariablesSorter.visitVarInsn(55, 7); // long m = 2L;
 localVariablesSorter.visitVarInsn(57, 8); // double n = 4.2;
 localVariablesSorter.visitVarInsn(56, 9); // Object q = new Object();

 int[] expectedLocals = {5, 10, 0, 3, 2, 1, 7, 8, 9};
 int[] actualLocals = new int[localVariablesSorter.nextLocal];
 localVariablesSorter.visitMaxs(4, actualLocals.length);
 assertArrayEquals(expectedLocals, actualLocals);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null) {
 @Override
 public void visitIincInsn(int var, int increment) {
 super.visitIincInsn(0, 5);
 }
 });
 localVariablesSorter.visitIincInsn(0, 10); // i += 10;
 int expectedLocal = 15;
 int actualLocal = localVariablesSorter.nextLocal;
 assertEquals(expectedLocal, actualLocal);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 super.visitMaxs(1, 5);
 }
 });
 int[] expectedLocals = {0, 0, 0, 0, 0};
 int[] actualLocals = new int[localVariablesSorter.nextLocal];
 localVariablesSorter.visitMaxs(4, actualLocals.length);
 assertArrayEquals(expectedLocals, actualLocals);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitLocalVariableWithExpandedFrames() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(1, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 localVariablesSorter.visitFrame(0, 1, null, 1, null);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Object[] locals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.LONG};
 Object[] stack = {Opcodes.INTEGER, Opcodes.INTEGER};
 localVariablesSorter.visitFrame(0, 4, locals, 2, stack);
 Object[] expectedLocals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.LONG};
 Object[] actualLocals = new Object[localVariablesSorter.nextLocal];
 localVariablesSorter.visitFrame(0, actualLocals.length, null, 2, stack);
 assertArrayEquals(expectedLocals, actualLocals);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type objectType = Type.getType("java/lang/String");
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(floatType);
 localVariablesSorter.newLocal(floatType);
 localVariablesSorter.newLocal(longType);
 localVariablesSorter.newLocal(longType);
 localVariablesSorter.newLocal(doubleType);
 localVariablesSorter.newLocal(doubleType);
 localVariablesSorter.newLocal(objectType);
 localVariablesSorter.newLocal(objectType);
 int[] expectedLocals = {11, 12, 13, 14, 15, 16, 17, 18, 19};
 int[] actualLocals = new int[localVariablesSorter.nextLocal];
 localVariablesSorter.visitMaxs(4, actualLocals.length);
 assertArrayEquals(expectedLocals, actualLocals);
 }
}