package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

 LocalVariablesSorter sorter;
 int[] mapping;
 Object[] newLocals;

 @Test
 public void testConstructor() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }
 });
 assertEquals(1, sorter.nextLocal);
 assertNull(sorter.mapping);
 assertNull(sorter.newLocals);
 }

 @Test
 public void testVisitVarInsn() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }

 @Override
 public void visitVarInsn(int opcode, int var) {
 mapping = sorter.mapping;
 newLocals = sorter.newLocals;
 Type type = null;
 switch (opcode) {
 case 22:
 case 55: {
 type = Type.LONG_TYPE;
 break;
 }
 case 24:
 case 57: {
 type = Type.DOUBLE_TYPE;
 break;
 }
 case 23:
 case 56: {
 type = Type.FLOAT_TYPE;
 break;
 }
 case 21:
 case 54: {
 type = Type.INT_TYPE;
 break;
 }
 default: {
 type = Type.getObjectType("java/lang/Object");
 break;
 }
 }
 int newVar = sorter.remap(var, type);
 assertEquals(var, newVar);
 }
 });
 assertNotNull(sorter.mapping);
 assertNotNull(sorter.newLocals);
 }

 @Test
 public void testVisitIincInsn() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }

 @Override
 public void visitIincInsn(int var, int increment) {
 int newVar = sorter.remap(var, Type.INT_TYPE);
 assertEquals(var, newVar);
 }
 });
 }

 @Test
 public void testVisitLocalVariable() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }

 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 int newIndex = sorter.remap(index, Type.getType(desc));
 assertEquals(index, newIndex);
 }
 });
 }

 @Test
 public void testVisitFrame() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }

 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 int i;
 for (i = 0; i < nLocal; ++i) {
 Object t = local[i];
 Type typ;
 if (t == Opcodes.LONG || t == Opcodes.DOUBLE) {
 typ = Type.LONG_TYPE;
 }
 else if (t == Opcodes.FLOAT) {
 typ = Type.FLOAT_TYPE;
 }
 else if (t == Opcodes.INTEGER) {
 typ = Type.INT_TYPE;
 }
 else {
 typ = Type.getObjectType((String)t);
 }
 int newIndex = sorter.remap(i, typ);
 assertEquals(i, newIndex);
 }
 }
 });
 }

 @Test
 public void testNewLocal() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }

 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type stringType = Type.getType("java/lang/String");
 int local1 = sorter.newLocal(intType);
 int local2 = sorter.newLocal(intType);
 int local3 = sorter.newLocal(floatType);
 int local4 = sorter.newLocal(longType);
 int local5 = sorter.newLocal(doubleType);
 int local6 = sorter.newLocal(stringType);
 int local7 = sorter.newLocal(intType);
 int local8 = sorter.newLocal(intType);
 int local9 = sorter.newLocal(intType);
 int local10 = sorter.newLocal(intType);
 int local11 = sorter.newLocal(intType);
 int local12 = sorter.newLocal(intType);
 int local13 = sorter.newLocal(intType);
 int local14 = sorter.newLocal(intType);
 int local15 = sorter.newLocal(intType);
 int local16 = sorter.newLocal(intType);
 int local17 = sorter.newLocal(intType);
 int local18 = sorter.newLocal(intType);
 int local19 = sorter.newLocal(intType);
 int local20 = sorter.newLocal(intType);
 assertEquals(21, sorter.nextLocal);
 }

 @Test
 public void testSetFrameLocal() {
 sorter = new LocalVariablesSorter(0, "", new MethodVisitor( MethodVisitor.ASM5) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 // Do nothing
 }
 });
 Object[] locals = new Object[10];
 locals[0] = Opcodes.INTEGER;
 locals[1] = Opcodes.INTEGER;
 locals[2] = Opcodes.FLOAT;
 locals[3] = Opcodes.LONG;
 locals[4] = Opcodes.DOUBLE;
 locals[5] = "java/lang/String";
 locals[6] = Opcodes.INTEGER;
 locals[7] = Opcodes.INTEGER;
 locals[8] = Opcodes.INTEGER;
 locals[9] = Opcodes.INTEGER;
 int local1 = sorter.nextLocal;
 sorter.setFrameLocal(local1, locals[0]);
 assertEquals(locals[0], sorter.newLocals[local1]);
 }
}