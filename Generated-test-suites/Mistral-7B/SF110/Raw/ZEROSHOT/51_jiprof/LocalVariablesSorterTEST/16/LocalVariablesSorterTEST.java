package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 LocalVariablesSorter sorter;
 MethodVisitor mvMock;
 ClassReader crMock;

 @Before
 public void setup() {
 mvMock = mock(MethodVisitor.class);
 crMock = mock(ClassReader.class);
 sorter = new LocalVariablesSorter(0, "", mvMock);
 }

 @Test
 public void testVisitVarInsnWithInt() {
 int var = 1;
 Type type = Type.getIntType();
 sorter.visitVarInsn(21, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 int var = 1;
 Type type = Type.getFloatType();
 sorter.visitVarInsn(23, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithLong() {
 int var = 1;
 Type type = Type.getLongType();
 sorter.visitVarInsn(22, var);
 assertEquals(var * 2, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 int var = 1;
 Type type = Type.getDoubleType();
 sorter.visitVarInsn(24, var);
 assertEquals(var * 2, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsnWithObject() {
 int var = 1;
 String objectName = "TestObject";
 Type type = Type.getType(objectName);
 sorter.visitVarInsn(23, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitIincInsn() {
 int var = 1;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 assertEquals(var, sorter.remap(var, Type.getIntType()));
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 1;
 int maxLocals = 1;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "Ljava/lang/String;";
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 1;
 Type type = Type.getType(desc);
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 assertEquals(index, sorter.remap(index, type));
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[1];
 local[0] = Opcodes.INTEGER;
 int nLocal = local.length;
 int nStack = 1;
 Object[] stack = new Object[1];
 int type = -1;
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 assertSame(local, sorter.newLocals);
 }

 @Test
 public void testSetFrameLocal() {
 int local = 1;
 Object type = Opcodes.INTEGER;
 sorter.setFrameLocal(local, type);
 assertEquals(local, sorter.mapping[local * 2 + type.getSize() - 1]);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.getIntType();
 int newLocal = sorter.newLocal(type);
 assertEquals(sorter.nextLocal - type.getSize(), newLocal);
 }

 @Test
 public void testSetLocalType() {
 int local = 1;
 Type type = Type.getIntType();
 sorter.setLocalType(local, type);
 assertEquals(type, sorter.newLocals[local]);
 }

 @Test
 public void testVisitFrameWithChangedFlag() {
 Object[] local = new Object[1];
 local[0] = Opcodes.INTEGER;
 int nLocal = local.length;
 int nStack = 1;
 Object[] stack = new Object[1];
 int type = -1;
 sorter.changed = true;
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 assertNotSame(local, sorter.newLocals);
 }

 @Test
 public void testRemap() {
 int var = 1;
 Type type = Type.getIntType();
 int expected = var;
 int actual = sorter.remap(var, type);
 assertEquals(expected, actual);
 }

 @Test
 public void testNewLocalMapping() {
 Type type = Type.getIntType();
 int newLocal = sorter.newLocalMapping(type);
 assertEquals(sorter.nextLocal - type.getSize(), newLocal);
 }
}