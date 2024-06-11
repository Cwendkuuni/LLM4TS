package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class LocalVariablesSorterTest {

 private MethodVisitor mvMock;
 private ClassReader crMock;

 @Before
 public void setup() {
 mvMock = mock(MethodVisitor.class);
 crMock = mock(ClassReader.class);
 }

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 assertEquals(1, sorter.firstLocal);
 assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsnWithIntegerType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitVarInsn(21, 0);
 verify(mvMock, times(1)).visitVarInsn(21, 0);
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitVarInsn(22, 0);
 verify(mvMock, times(1)).visitVarInsn(22, 0);
 }

 @Test
 public void testVisitVarInsnWithDoubleType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitVarInsn(24, 0);
 verify(mvMock, times(1)).visitVarInsn(24, 0);
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitVarInsn(23, 0);
 verify(mvMock, times(1)).visitVarInsn(23, 0);
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.getType("java.lang.String");
 sorter.visitVarInsn(25, 0);
 verify(mvMock, times(1)).visitVarInsn(25, 0);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int index = 0;
 int increment = 5;
 sorter.visitIincInsn(index, increment);
 verify(mvMock, times(1)).visitIincInsn(index, increment);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int maxStack = 5;
 int maxLocals = 10;
 sorter.visitMaxs(maxStack, maxLocals);
 verify(mvMock, times(1)).visitMaxs(maxStack, maxLocals);
 }

 @Test
 public void testVisitLocalVariableWithStringNameAndIntType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 String name = "testName";
 Type desc = Type.INT_TYPE;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;
 sorter.visitLocalVariable(name, desc.getInternalName(), null, start, end, index);
 verify(mvMock, times(1)).visitLocalVariable(name, desc.getInternalName(), null, start, end, index);
 }

 @Test
 public void testVisitLocalVariableWithStringNameAndObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 String name = "testName";
 Type desc = Type.getType("java.lang.String");
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;
 sorter.visitLocalVariable(name, desc.getInternalName(), null, start, end, index);
 verify(mvMock, times(1)).visitLocalVariable(name, desc.getInternalName(), null, start, end, index);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int nLocal = 5;
 Object[] local = new Object[nLocal];
 int nStack = 5;
 Object[] stack = new Object[nStack];
 sorter.visitFrame(0, nLocal, local, nStack, stack);
 verify(mvMock, times(1)).visitFrame(0, nLocal, local, nStack, stack);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithNegativeType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitFrame(-1, 0, new Object[0], 0, new Object[0]);
 }

 @Test
 public void testVisitFrameWithUnchangedFlag() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 sorter.visitFrame(0, 5, new Object[5], 5, new Object[5]);
 }

 @Test
 public void testVisitFrameWithChangedFlag() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int[] oldLocals = new int[5];
 int[] newLocals = {1, 2, Opcodes.LONG, Opcodes.DOUBLE, 5};
 Object[] newLocalsObject = new Object[newLocals.length];
 System.arraycopy(newLocals, 0, newLocalsObject, 0, newLocals.length);
 sorter.visitFrame(0, 5, newLocalsObject, 5, new Object[5]);
 verify(mvMock, times(1)).visitFrame(0, 5, oldLocals, 5, new Object[5]);
 }

 @Test
 public void testNewLocalWithIntegerType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.INT_TYPE;
 int local = sorter.newLocal(type);
 assertEquals(1, local);
 }

 @Test
 public void testNewLocalWithLongType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.LONG_TYPE;
 int local = sorter.newLocal(type);
 assertEquals(2, local);
 }

 @Test
 public void testNewLocalWithDoubleType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.DOUBLE_TYPE;
 int local = sorter.newLocal(type);
 assertEquals(3, local);
 }

 @Test
 public void testNewLocalWithFloatType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.FLOAT_TYPE;
 int local = sorter.newLocal(type);
 assertEquals(4, local);
 }

 @Test
 public void testNewLocalWithObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.getType("java.lang.String");
 int local = sorter.newLocal(type);
 assertEquals(5, local);
 }

 @Test
 public void testSetLocalTypeWithIntegerType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Type type = Type.INT_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(Type.INT_TYPE, sorter.mapping[local * 2]);
 }

 @Test
 public void testSetLocalTypeWithLongType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Type type = Type.LONG_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(Type.LONG_TYPE, sorter.mapping[local * 2]);
 }

 @Test
 public void testSetLocalTypeWithDoubleType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Type type = Type.DOUBLE_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(Type.DOUBLE_TYPE, sorter.mapping[local * 2]);
 }

 @Test
 public void testSetLocalTypeWithFloatType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Type type = Type.FLOAT_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(Type.FLOAT_TYPE, sorter.mapping[local * 2]);
 }

 @Test
 public void testSetLocalTypeWithObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Type type = Type.getType("java.lang.String");
 sorter.setLocalType(local, type);
 assertEquals(type.getInternalName(), sorter.mapping[local * 2]);
 }

 @Test
 public void testSetFrameLocalWithIntegerType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Object type = Opcodes.INTEGER;
 sorter.setFrameLocal(local, type);
 assertEquals(Opcodes.INTEGER, sorter.newLocals[local]);
 }

 @Test
 public void testSetFrameLocalWithLongType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Object type = Opcodes.LONG;
 sorter.setFrameLocal(local, type);
 assertEquals(Opcodes.LONG, sorter.newLocals[local]);
 }

 @Test
 public void testSetFrameLocalWithDoubleType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Object type = Opcodes.DOUBLE;
 sorter.setFrameLocal(local, type);
 assertEquals(Opcodes.DOUBLE, sorter.newLocals[local]);
 }

 @Test
 public void testSetFrameLocalWithObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int local = 1;
 Object type = "java.lang.String";
 sorter.setFrameLocal(local, type);
 assertEquals("java.lang.String", sorter.newLocals[local].toString());
 }

 @Test
 public void testRemapWithIntegerType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int var = 1;
 Type type = Type.INT_TYPE;
 int expected = var;
 int result = sorter.remap(var, type);
 assertEquals(expected, result);
 }

 @Test
 public void testRemapWithLongType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int var = 1;
 Type type = Type.LONG_TYPE;
 int expected = var;
 int result = sorter.remap(var, type);
 assertEquals(expected, result);
 }

 @Test
 public void testRemapWithDoubleType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int var = 1;
 Type type = Type.DOUBLE_TYPE;
 int expected = var;
 int result = sorter.remap(var, type);
 assertEquals(expected, result);
 }

 @Test
 public void testRemapWithObjectType() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int var = 1;
 Type type = Type.getType("java.lang.String");
 int expected = var;
 int result = sorter.remap(var, type);
 assertEquals(expected, result);
 }

 @Test
 public void testRemapWithOutOfBoundsIndex() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 int var = 10;
 Type type = Type.INT_TYPE;
 int expected = -1;
 int result = sorter.remap(var, type);
 assertEquals(expected, result);
 }

 @Test
 public void testNewLocalMapping() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", mvMock);
 Type type = Type.INT_TYPE;
 int expectedLocal = sorter.nextLocal;
 int resultLocal = sorter.newLocalMapping(type);
 assertEquals(expectedLocal, resultLocal);
 }
}