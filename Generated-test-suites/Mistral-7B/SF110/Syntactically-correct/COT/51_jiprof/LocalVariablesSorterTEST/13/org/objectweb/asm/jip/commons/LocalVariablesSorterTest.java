package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.jip.MethodAdapter;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

 private static final String CLASS_NAME = "TestLocalVariablesSorter";
 private static final String METHOD_NAME = "testLocalVariablesSorter";

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitEnd() {}
 });
 assertEquals(21, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 sorter.visitVarInsn(21, 2); // int
 sorter.visitVarInsn(22, 3); // long
 sorter.visitVarInsn(56, 4); // float
 sorter.visitVarInsn(55, 5); // long
 sorter.visitVarInsn(56, 6); // float

 assertEquals(7, sorter.nextLocal);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitIincInsn(int var, int increment) {
 sorter.visitIincInsn(var, increment);
 }
 });
 sorter.visitIincInsn(1, 5);

 assertEquals(6, sorter.nextLocal);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 sorter.visitMaxs(maxStack, maxLocals);
 }
 });

 ClassReader reader = new ClassReader(getClassFile(CLASS_NAME));
 assertEquals(1, reader.maxStack);
 assertEquals(1, reader.maxLocals);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });

 ClassReader reader = new ClassReader(getClassFile(CLASS_NAME));
 assertEquals("Test", reader.getLocalVariable("name", "(Ljava/lang/String;)V", 0).name);
 assertEquals("Ljava/lang/String;", reader.getLocalVariable("name", "(Ljava/lang/String;)V", 0).desc);
 assertEquals("name", reader.getLocalVariable("name", "(Ljava/lang/String;)V", 0).nameIndex);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 sorter.visitFrame(1, 2, new Object[]{Type.INT_TYPE, Type.FLOAT_TYPE}, 2, new Object[]{Type.INT_TYPE, Type.FLOAT_TYPE});

 ClassReader reader = new ClassReader(getClassFile(CLASS_NAME));
 assertEquals(1, reader.frame.localVariables.length);
 assertEquals(2, reader.frame.localVariables[0].index);
 assertEquals(Type.INT_TYPE, reader.frame.localVariables[0].type);
 assertEquals(Type.INT_TYPE, reader.frame.localVariables[0].typeNameIndex);
 assertEquals(Type.FLOAT_TYPE, reader.frame.localVariables[1].type);
 assertEquals(Type.FLOAT_TYPE, reader.frame.localVariables[1].typeNameIndex);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithExpandFrames() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 sorter.visitFrame(-1, 2, null, 2, null);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 int local1 = sorter.newLocal(Type.INT_TYPE);
 int local2 = sorter.newLocal(Type.FLOAT_TYPE);
 assertEquals(5, sorter.nextLocal);
 }

 @Test
 public void testSetLocalType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 sorter.setLocalType(1, Type.LONG_TYPE);
 sorter.setLocalType(2, Type.DOUBLE_TYPE);
 assertNull(sorter.getLocalType(1));
 assertEquals(Type.DOUBLE_TYPE, sorter.getLocalType(2));
 }

 @Test
 public void testSetFrameLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 int local1 = sorter.newLocal(Type.INT_TYPE);
 sorter.setFrameLocal(local1, "TestInt");
 sorter.setFrameLocal(local1, Type.LONG_TYPE);
 assertEquals("TestInt", sorter.getFrameLocal(local1).toString());
 assertEquals(Type.LONG_TYPE, sorter.getFrameLocal(local1 + 1).getType());
 }

 @Test
 public void testRemap() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 int local1 = sorter.newLocal(Type.INT_TYPE);
 int local2 = sorter.newLocal(Type.FLOAT_TYPE);
 int local3 = sorter.newLocal(Type.LONG_TYPE);
 int local4 = sorter.newLocal(Type.DOUBLE_TYPE);
 int local5 = sorter.newLocal(Type.OBJECT_TYPE);

 int index1 = sorter.remap(local1, Type.INT_TYPE);
 int index2 = sorter.remap(local2, Type.FLOAT_TYPE);
 int index3 = sorter.remap(local3, Type.LONG_TYPE);
 int index4 = sorter.remap(local4, Type.DOUBLE_TYPE);
 int index5 = sorter.remap(local5, Type.OBJECT_TYPE);

 assertEquals(local1, index1);
 assertEquals(local2, index2);
 assertEquals(local3, index3);
 assertEquals(local4, index4);
 assertEquals(local5, index5);
 }

 @Test
 public void testNewLocalMapping() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0020, "()V", new MethodVisitor(new Bytecode()) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 sorter.visitVarInsn(opcode, var);
 }
 });
 int local1 = sorter.newLocal(Type.INT_TYPE);
 int local2 = sorter.newLocal(Type.FLOAT_TYPE);
 int local3 = sorter.newLocalMapping(Type.DOUBLE_TYPE);
 int local4 = sorter.newLocalMapping(Type.LONG_TYPE);
 int local5 = sorter.newLocalMapping(Type.OBJECT_TYPE);

 assertEquals(local1, sorter.mapping[2]);
 assertEquals(local2, sorter.mapping[3]);
 assertEquals(local3, sorter.mapping[7]);
 assertEquals(local4, sorter.mapping[8]);
 assertEquals(local5, sorter.mapping[9]);
 }
}