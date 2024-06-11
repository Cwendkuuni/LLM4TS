package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testVisitVarInsnWithInteger() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 Type intType = Type.getIntType();
 sorter.visitVarInsn(Opcodes.ILOAD, 0);
 assertEquals(0, sorter.mapping[0]);
 }

 @Test
 public void testVisitVarInsnWithLong() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 Type longType = Type.getLongType();
 sorter.visitVarInsn(Opcodes.LLOAD, 0);
 assertEquals(-1, sorter.mapping[0]);
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 Type doubleType = Type.getDoubleType();
 sorter.visitVarInsn(Opcodes.DLOAD, 0);
 assertEquals(-2, sorter.mapping[0]);
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 Type floatType = Type.getFloatType();
 sorter.visitVarInsn(Opcodes.FLOAD, 0);
 assertEquals(-3, sorter.mapping[0]);
 }

 @Test
 public void testVisitVarInsnWithObject() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 String objectTypeName = "java.lang.String";
 Type objectType = Type.getObjectType(objectTypeName);
 sorter.visitVarInsn(Opcodes.ALOAD, 0);
 assertEquals(-4 - objectTypeName.length(), sorter.mapping[0]);
 }

 @Test
 public void testVisitVarInsnWithNegativeIndex() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, var);
 }
 });
 sorter.visitVarInsn(Opcodes.ILOAD, -1);
 assertNull(sorter.mapping);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitIincInsn(int var, int increment) {
 super.visitIincInsn(var, increment);
 }
 });
 sorter.visitIincInsn(0, 1);
 assertEquals(0, sorter.mapping[0]);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 super.visitMaxs(maxStack, maxLocals);
 }
 });
 int maxLocals = 5;
 sorter.visitMaxs(5, maxLocals);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 String name = "test";
 Type type = Type.getIntType();
 Label start = new Label();
 Label end = new Label();
 sorter.visitLocalVariable(name, type.getDescriptor(), null, start, end, 0);
 assertEquals(0, sorter.mapping[LocalVariablesSorter.remap(0, type)]);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Object[] local = new Object[5];
 local[0] = Opcodes.INTEGER;
 local[1] = Opcodes.INTEGER;
 local[2] = Opcodes.INTEGER;
 local[3] = Opcodes.FLOAT;
 local[4] = Opcodes.LONG;
 sorter.visitFrame(0, 5, local, 5, null);
 assertEquals(5, sorter.nextLocal);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Type intType = Type.getIntType();
 int local = sorter.newLocal(intType);
 assertEquals(1, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalWithLong() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Type longType = Type.getLongType();
 int local = sorter.newLocal(longType);
 assertEquals(2, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalWithDouble() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Type doubleType = Type.getDoubleType();
 int local = sorter.newLocal(doubleType);
 assertEquals(3, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalWithObject() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 String objectTypeName = "java.lang.String";
 Type objectType = Type.getObjectType(objectTypeName);
 int local = sorter.newLocal(objectType);
 assertEquals(-objectTypeName.length() - 1, sorter.mapping[local]);
 }

 @Test
 public void testSetLocalType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void setLocalType(int local, Type type) {
 super.setLocalType(local, type);
 }
 });
 Type intType = Type.getIntType();
 sorter.setLocalType(0, intType);
 assertNull(sorter.newLocals);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithoutExpandFlags() {
 ClassReader cr = new ClassReader("TestClass.class");
 cr.accept(new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 }));
 }

 @Test
 public void testVisitFrameWithChanged() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(12345) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 Object[] oldLocals = new Object[3];
 oldLocals[0] = Opcodes.INTEGER;
 oldLocals[1] = Opcodes.INTEGER;
 oldLocals[2] = Opcodes.INTEGER;
 int[] newMapping = new int[10];
 int index = 0;
 for (Object t : sorter.newLocals) {
 if (t != null && t != Opcodes.TOP) {
 Type typ;
 if (t == Opcodes.INTEGER) {
 typ = Type.INT_TYPE;
 }
 else if (t == Opcodes.FLOAT) {
 typ = Type.FLOAT_TYPE;
 }
 else if (t == Opcodes.LONG) {
 typ = Type.LONG_TYPE;
 }
 else if (t == Opcodes.DOUBLE) {
 typ = Type.DOUBLE_TYPE;
 }
 else {
 typ = Type.getObjectType((String)t);
 }
 sorter.setFrameLocal(sorter.remap(index++, typ), t);
 }
 }
 Object[] local = new Object[3];
 local[0] = Opcodes.INTEGER;
 local[1] = Opcodes.INTEGER;
 local[2] = Opcodes.INTEGER;
 sorter.visitFrame(0, 3, local, 3, null);
 assertEquals(3, sorter.nextLocal);
 }
}