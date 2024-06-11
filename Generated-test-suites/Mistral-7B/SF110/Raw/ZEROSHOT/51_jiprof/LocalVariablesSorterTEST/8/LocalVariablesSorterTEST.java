package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

 private MethodVisitor mvMock;

 @Before
 public void setUp() {
 mvMock = mock(MethodVisitor.class);
 }

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 Assert.assertArrayEquals(new int[40], sorter.mapping);
 Assert.assertArrayEquals(new Object[20], sorter.newLocals);
 Assert.assertEquals(1, sorter.nextLocal);
 Assert.assertEquals(1, sorter.firstLocal);
 Assert.assertEquals(0, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int var = 1;
 Type type = Type.getIntType();
 int expectedRemappedVar = 1;
 sorter.visitVarInsn(21, var);
 verify(mvMock).visitVarInsn(21, expectedRemappedVar);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int var = 1;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 verify(mvMock).visitIincInsn(1, increment);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int maxStack = 5;
 int maxLocals = 10;
 sorter.visitMaxs(maxStack, maxLocals);
 verify(mvMock).visitMaxs(maxStack, 10);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 String name = "name";
 String desc = "I";
 String signature = "";
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 1;
 Type type = Type.getIntType();
 int expectedNewIndex = 1;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 verify(mvMock).visitLocalVariable(name, desc, signature, start, end, expectedNewIndex);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int type = -1;
 int nLocal = 5;
 Object[] local = new Object[5];
 int nStack = 3;
 Object[] stack = new Object[3];
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 Assert.assertEquals(-1, type);
 Assert.assertEquals(5, nLocal);
 assertArrayEquals(local, sorter.newLocals);
 }

 @Test
 public void testVisitFrameWithChanged() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int type = -1;
 int nLocal = 5;
 Object[] local = new Object[5];
 Object[] oldLocals = new Object[5];
 int nStack = 3;
 Object[] stack = new Object[3];
 for (int i = 0; i < nLocal; i++) {
 local[i] = Opcodes.INTEGER;
 oldLocals[i] = i;
 }
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 assertArrayEquals(oldLocals, sorter.newLocals);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 Type type = Type.getIntType();
 int expectedLocal = 1;
 int newLocal = sorter.newLocal(type);
 assertEquals(expectedLocal, newLocal);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithUninitialized() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mvMock);
 int type = -1;
 int nLocal = 5;
 Object[] local = new Object[5];
 int nStack = 3;
 Object[] stack = new Object[3];
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 }
}