package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testConstructor() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test:(I)V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitMethodEnd() {
 super.visitMethodEnd();
 }
 });
 assertEquals(20, localVariablesSorter.newLocals.length);
 assertEquals(40, localVariablesSorter.mapping.length);
 assertNotNull(localVariablesSorter.mapping);
 assertNotNull(localVariablesSorter.newLocals);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test:(I)V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, localVariablesSorter.remap(var, null));
 }

 @Override
 public void visitMethodEnd() {
 super.visitMethodEnd();
 }
 });
 Type intType = Type.INT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type objectType = Type.getObjectType("java/lang/Object");
 Type floatType = Type.FLOAT_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;

 int local1 = localVariablesSorter.newLocal(intType);
 int local2 = localVariablesSorter.newLocal(intType);
 int local3 = localVariablesSorter.newLocal(intType);
 int local4 = localVariablesSorter.newLocal(intType);
 int local5 = localVariablesSorter.newLocal(longType);
 int local6 = localVariablesSorter.newLocal(longType);
 int local7 = localVariablesSorter.newLocal(floatType);
 int local8 = localVariablesSorter.newLocal(floatType);
 int local9 = localVariablesSorter.newLocal(doubleType);
 int local10 = localVariablesSorter.newLocal(doubleType);
 int local11 = localVariablesSorter.newLocal(objectType);
 int local12 = localVariablesSorter.newLocal(objectType);
 int local13 = localVariablesSorter.newLocal(objectType);
 int local14 = localVariablesSorter.newLocal(objectType);
 int local15 = localVariablesSorter.newLocal(objectType);
 int local16 = localVariablesSorter.newLocal(objectType);
 int local17 = localVariablesSorter.newLocal(objectType);
 int local18 = localVariablesSorter.newLocal(objectType);
 int local19 = localVariablesSorter.newLocal(objectType);
 int local20 = localVariablesSorter.newLocal(objectType);

 assertEquals(local1, localVariablesSorter.remap(1, intType));
 assertEquals(local2, localVariablesSorter.remap(2, intType));
 assertEquals(local3, localVariablesSorter.remap(3, intType));
 assertEquals(local4, localVariablesSorter.remap(4, intType));
 assertEquals(local5, localVariablesSorter.remap(5, longType));
 assertEquals(local6, localVariablesSorter.remap(6, longType));
 assertEquals(local7, localVariablesSorter.remap(7, floatType));
 assertEquals(local8, localVariablesSorter.remap(8, floatType));
 assertEquals(local9, localVariablesSorter.remap(9, doubleType));
 assertEquals(local10, localVariablesSorter.remap(10, doubleType));
 assertEquals(local11, localVariablesSorter.remap(11, objectType));
 assertEquals(local12, localVariablesSorter.remap(12, objectType));
 assertEquals(local13, localVariablesSorter.remap(13, objectType));
 assertEquals(local14, localVariablesSorter.remap(14, objectType));
 assertEquals(local15, localVariablesSorter.remap(15, objectType));
 assertEquals(local16, localVariablesSorter.remap(16, objectType));
 assertEquals(local17, localVariablesSorter.remap(17, objectType));
 assertEquals(local18, localVariablesSorter.remap(18, objectType));
 assertEquals(local19, localVariablesSorter.remap(19, objectType));
 assertEquals(local20, localVariablesSorter.remap(20, objectType));
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test:(I)V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 localVariablesSorter.setFrameLocal(localVariablesSorter.remap(index, Type.getObjectType("java/lang/Object")), Opcodes.TOP);
 }
 @Override
 public void visitIincInsn(int var, int increment) {
 localVariablesSorter.visitIincInsn(localVariablesSorter.remap(var, Type.INT_TYPE), increment);
 }

 @Override
 public void visitMethodEnd() {
 super.visitMethodEnd();
 }
 });
 Type intType = Type.INT_TYPE;
 int local1 = localVariablesSorter.newLocal(intType);
 int local2 = localVariablesSorter.newLocal(intType);

 localVariablesSorter.visitLocalVariable("test", null, null, null, null, 0);
 localVariablesSorter.setFrameLocal(0, Opcodes.TOP);
 localVariablesSorter.visitLocalVariable("test2", null, null, null, null, local1);
 localVariablesSorter.setFrameLocal(local1, Opcodes.TOP);
 localVariablesSorter.visitIincInsn(local1, 1);
 localVariablesSorter.visitLocalVariable("test3", null, null, null, null, local2);
 localVariablesSorter.setFrameLocal(local2, Opcodes.TOP);
 localVariablesSorter.visitIincInsn(local2, 2);

 assertEquals(local1 + 1, localVariablesSorter.remap(1, intType));
 assertEquals(local2 + 2, localVariablesSorter.remap(2, intType));
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test:(I)V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 localVariablesSorter.visitMaxs(maxStack, localVariablesSorter.nextLocal);
 }

 @Override
 public void visitMethodEnd() {
 super.visitMethodEnd();
 }
 });
 int maxLocals = localVariablesSorter.nextLocal + 1;
 int maxStack = 1;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxLocals, localVariablesSorter.mapping[maxLocals / 2]);
 assertEquals(maxStack, localVariablesSorter.mapping[maxLocals / 2 + 1]);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test:(I)V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 }

 @Override
 public void visitMethodEnd() {
 super.visitMethodEnd();
 }
 });
 Type intType = Type.INT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type objectType = Type.getObjectType("java/lang/Object");
 int local1 = localVariablesSorter.newLocal(intType);
 int local2 = localVariablesSorter.newLocal(intType);
 int local3 = localVariablesSorter.newLocal(intType);
 int local4 = localVariablesSorter.newLocal(intType);
 int local5 = localVariablesSorter.newLocal(longType);
 int local6 = localVariablesSorter.newLocal(longType);
 int local7 = localVariablesSorter.newLocal(floatType);
 int local8 = localVariablesSorter.newLocal(floatType);
 int local9 = localVariablesSorter.newLocal(doubleType);
 int local10 = localVariablesSorter.newLocal(doubleType);
 int local11 = localVariablesSorter.newLocal(objectType);
 int local12 = localVariablesSorter.newLocal(objectType);
 int local13 = localVariablesSorter.newLocal(objectType);
 int local14 = localVariablesSorter.newLocal(objectType);
 int local15 = localVariablesSorter.newLocal(objectType);
 int local16 = localVariablesSorter.newLocal(objectType);
 int local17 = localVariablesSorter.newLocal(objectType);
 int local18 = localVariablesSorter.newLocal(objectType);
 int local19 = localVariablesSorter.newLocal(objectType);
 int local20 = localVariablesSorter.newLocal(objectType);

 localVariablesSorter.visitLocalVariable("test1", null, null, null, null, local1);
 localVariablesSorter.visitLocalVariable("test2", null, null, null, null, local2);
 localVariablesSorter.visitLocalVariable("test3", null, null, null, null, local3);
 localVariablesSorter.visitLocalVariable("test4", null, null, null, null, local4);
 localVariablesSorter.visitLocalVariable("test5", null, null, null, null, local5);
 localVariablesSorter.visitLocalVariable("test6", null, null, null, null, local6);
 localVariablesSorter.visitLocalVariable("test7", null, null, null, null, local7);
 localVariablesSorter.visitLocalVariable("test8", null, null, null, null, local8);
 localVariablesSorter.visitLocalVariable("test9", null, null, null, null, local9);
 localVariablesSorter.visitLocalVariable("test10", null, null, null, null, local10);
 localVariablesSorter.visitLocalVariable("test11", null, null, null, null, local11);
 localVariablesSorter.visitLocalVariable("test12", null, null, null, null, local12);
 localVariablesSorter.visitLocalVariable("test13", null, null, null, null, local13);
 localVariablesSorter.visitLocalVariable("test14", null, null, null, null, local14);
 localVariablesSorter.visitLocalVariable("test15", null, null, null, null, local15);
 localVariablesSorter.visitLocalVariable("test16", null, null, null, null, local16);
 localVariablesSorter.visitLocalVariable("test17", null, null, null, null, local17);
 localVariablesSorter.visitLocalVariable("test18", null, null, null, null, local18);
 localVariablesSorter.visitLocalVariable("test19", null, null, null, null, local19);
 localVariablesSorter.visitLocalVariable("test20", null, null, null, null, local20);

 assertEquals(local1, localVariablesSorter.remap(1, intType));
 assertEquals(local2, localVariablesSorter.remap(2, intType));
 assertEquals(local3, localVariablesSorter.remap(3, intType));
 assertEquals(local4, localVariablesSorter.remap(4, intType));
 assertEquals(local5, localVariablesSorter.remap(5, longType));
 assertEquals(local6, localVariablesSorter.remap(6, longType));
 assertEquals(local7, localVariablesSorter.remap(7, floatType));
 assertEquals(local8, localVariablesSorter.remap(8, floatType));
 assertEquals(local9, localVariablesSorter.remap(9, doubleType));
 assertEquals(local10, localVariablesSorter.remap(10, doubleType));
 assertEquals(local11, localVariablesSorter.remap(11, objectType));
 assertEquals(local12, localVariablesSorter.remap(12, objectType));
 assertEquals(local13, localVariablesSorter.remap(13, objectType));
 assertEquals(local14, localVariablesSorter.remap(14, objectType));
 assertEquals(local15, localVariablesSorter.remap(15, objectType));
 assertEquals(local16, localVariablesSorter.remap(16, objectType));
 assertEquals(local17, localVariablesSorter.remap(17, objectType));
 assertEquals(local18, localVariablesSorter.remap(18, objectType));
 assertEquals(local19, localVariablesSorter.remap(19, objectType));
 assertEquals(local20, localVariablesSorter.remap(20, objectType));
 }
}