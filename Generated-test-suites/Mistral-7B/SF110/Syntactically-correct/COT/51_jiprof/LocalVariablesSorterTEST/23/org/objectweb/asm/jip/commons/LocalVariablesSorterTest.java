package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import org.junit.Before;
import org.junit.Test;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mvMock;
 private ClassWriter cwMock;

 @Before
 public void setUp() {
 mvMock = mock(MethodVisitor.class);
 cwMock = mock(ClassWriter.class);
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", cwMock);
 sorter.mv = mvMock;
 }

 @Test
 public void testVisitVarInsn() {
 final int var = 1;
 final int opcode = Opcodes.ILOAD;
 final Type type = Type.INT_TYPE;
 sorter.visitVarInsn(opcode, var);
 assertEquals(opcode, sorter.mv.visitVarInsn(sorter.remap(var, type), Opcodes.ALOAD).getOpcode());
 }

 @Test
 public void testVisitVarInsnWithLong() {
 final int var = 1;
 final int opcode = Opcodes.LLOAD;
 final Type type = Type.LONG_TYPE;
 sorter.visitVarInsn(opcode, var);
 assertEquals(opcode, sorter.mv.visitVarInsn(sorter.remap(var, type), Opcodes.ALOAD_2).getOpcode());
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 final int var = 1;
 final int opcode = Opcodes.DLOAD;
 final Type type = Type.DOUBLE_TYPE;
 sorter.visitVarInsn(opcode, var);
 assertEquals(opcode, sorter.mv.visitVarInsn(sorter.remap(var, type), Opcodes.ALOAD_3).getOpcode());
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 final int var = 1;
 final int opcode = Opcodes.FLOAD;
 final Type type = Type.FLOAT_TYPE;
 sorter.visitVarInsn(opcode, var);
 assertEquals(opcode, sorter.mv.visitVarInsn(sorter.remap(var, type), Opcodes.ALOAD).getOpcode());
 }

 @Test
 public void testVisitVarInsnWithObject() {
 final int var = 1;
 final int opcode = Opcodes.ALOAD;
 final Type type = Type.getType("java/lang/String");
 sorter.visitVarInsn(opcode, var);
 assertEquals(opcode, sorter.mv.visitVarInsn(sorter.remap(var, type), Opcodes.ALOAD).getOpcode());
 }

 @Test
 public void testVisitIincInsn() {
 final int var = 1;
 final int increment = 1;
 sorter.visitIincInsn(var, increment);
 sorter.mv.visitIincInsn(sorter.remap(var, Type.INT_TYPE), increment);
 }

 @Test
 public void testVisitMaxs() {
 final int maxStack = 1;
 final int maxLocals = 2;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, sorter.mv.visitMaxs(sorter.nextLocal, maxLocals).maxStack);
 assertEquals(sorter.nextLocal, sorter.mv.visitMaxs(sorter.nextLocal, maxLocals).maxLocals);
 }

 @Test
 public void testVisitLocalVariable() {
 final String name = "testName";
 final String desc = "I";
 final String signature = "Ljava/lang/String;";
 final Label start = mock(Label.class);
 final Label end = mock(Label.class);
 final int index = 1;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 assertEquals(name, sorter.mv.visitLocalVariable(name, desc, signature, start, end, sorter.remap(index, Type.getType(desc))).name);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithNegativeType() {
 final int type = -1;
 sorter.visitFrame(type, 0, new Object[0], 0, new Object[0]);
 }

 @Test
 public void testVisitFrameWithChanged() {
 final int nLocal = 1;
 final Object[] local = new Object[nLocal + 1];
 final Object[] stack = new Object[1];
 final Object[] newLocals = new Object[nLocal + 2];
 when(mvMock.visitFrame(Opcodes.FRAME_TYPE_ANY, nLocal + 1, local, 0, stack)).thenReturn(cwMock);
 sorter.visitFrame(Opcodes.FRAME_TYPE_ANY, nLocal, local, 0, stack);
 sorter.changed = true;
 sorter.visitFrame(Opcodes.FRAME_TYPE_ANY, nLocal, newLocals, 0, stack);
 assertEquals(nLocal, newLocals.length);
 }

 @Test
 public void testNewLocal() {
 final Type type = Type.INT_TYPE;
 final int local = sorter.newLocal(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 final Type type = Type.getType("java/lang/String");
 sorter.newLocal(type);
 }

 @Test
 public void testSetLocalType() {
 final int local = 1;
 final Type type = Type.INT_TYPE;
 sorter.setLocalType(local, type);
 }

 @Test
 public void testSetFrameLocal() {
 final int local = 1;
 final Object type = Opcodes.INTEGER;
 sorter.setFrameLocal(local, type);
 }

 @Test
 public void testRemap() {
 final int var = 1;
 final int index = 1;
 final Type type = Type.INT_TYPE;
 sorter.mapping = new int[4];
 sorter.newLocals = new Object[2];
 assertEquals(var, sorter.remap(var, type));
 assertEquals(index, sorter.remap(index, type));
 }

 @Test
 public void testRemapWithMaxSize() {
 final int var = 1;
 final int index = 1;
 final Type type = Type.INT_TYPE;
 sorter.mapping = new int[32768];
 sorter.newLocals = new Object[32768];
 assertEquals(var, sorter.remap(var, type));
 assertEquals(index, sorter.remap(index, type));
 }

 @Test
 public void testRemapWithNewLocal() {
 final int var = 1;
 final Type type = Type.INT_TYPE;
 final int newLocal = sorter.newLocal(type);
 assertEquals(var, sorter.remap(newLocal, type));
 }
}