package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Assert;
import org.junit.Test;

public class LocalVariablesSorterTest {

 LocalVariablesSorter sorter;
 MethodVisitor mv;

 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(0x0001, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 Type[] intTypes = {Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE};
 int[] expectedLocalIndices = {1, 2, 3};
 for (int i = 0; i < intTypes.length; i++) {
 int actualLocalIndex = sorter.remap(i, intTypes[i]);
 Assert.assertEquals(expectedLocalIndices[i], actualLocalIndex);
 }

 Type longType = Type.LONG_TYPE;
 int expectedLocalIndex = 1 + (longType.getSize() * 2);
 int actualLocalIndex = sorter.remap(0, longType);
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }

 @Test
 public void testVisitIincInsn() {
 int intVar = 3;
 int increment = 5;
 int expectedLocalIndex = 1 + intVar;
 sorter.visitIincInsn(intVar, increment);
 int actualLocalIndex = sorter.remap(intVar, Type.INT_TYPE);
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }

 @Test
 public void testVisitMaxs() {
 int maxLocals = 5;
 int maxStack = 3;
 int expectedNextLocal = 1 + maxLocals;
 sorter.visitMaxs(maxStack, maxLocals);
 int actualNextLocal = sorter.nextLocal;
 Assert.assertEquals(expectedNextLocal, actualNextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "";
 Label start = new Label();
 Label end = new Label();
 int index = 0;
 int expectedLocalIndex = 1 + index;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int actualLocalIndex = sorter.remap(index, Type.getType(desc));
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "java/lang/String", Opcodes.LONG};
 int nLocal = local.length;
 int nStack = 2;
 Object[] stack = new Object[nStack];
 int expectedNewLocalsLength = 1 + nLocal;
 int[] expectedNewLocals = new int[expectedNewLocalsLength];
 int i = 0;
 for (Object localElement : local) {
 int localSize = (localElement == Opcodes.LONG || localElement == Opcodes.DOUBLE) ? 2 : 1;
 int expectedLocalIndex = 1 + i;
 expectedNewLocals[i] = expectedLocalIndex;
 i += localSize;
 }
 sorter.visitFrame(-1, nLocal, local, nStack, stack);
 Object[] actualNewLocals = sorter.newLocals;
 int actualNewLocalsLength = actualNewLocals.length;
 Assert.assertEquals(expectedNewLocalsLength, actualNewLocalsLength);
 for (i = 0; i < expectedNewLocals.length; i++) {
 Assert.assertEquals(expectedNewLocals[i], actualNewLocals[i]);
 }
 }

 @Test
 public void testNewLocal() {
 Type intType = Type.INT_TYPE;
 int expectedLocalIndex = 1 + (intType.getSize() * 2);
 int actualLocalIndex = sorter.newLocal(intType);
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }

 @Test
 public void testSetLocalType() {
 int localIndex = 0;
 Type intType = Type.INT_TYPE;
 sorter.setLocalType(localIndex, intType);
 Type actualType = sorter.localTypes[localIndex];
 Assert.assertEquals(intType, actualType);
 }

 @Test
 public void testSetFrameLocal() {
 int localIndex = 0;
 Object intValue = Opcodes.INTEGER;
 sorter.setFrameLocal(localIndex, intValue);
 Object actualValue = sorter.frameLocals[localIndex];
 Assert.assertEquals(intValue, actualValue);
 }

 @Test
 public void testRemap() {
 int localIndex = 0;
 Type intType = Type.INT_TYPE;
 int expectedLocalIndex = 1 + (intType.getSize() * 2);
 int actualLocalIndex = sorter.remap(localIndex, intType);
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }

 @Test
 public void testNewLocalMapping() {
 Type intType = Type.INT_TYPE;
 int expectedLocalIndex = 1 + (intType.getSize() * 2);
 int actualLocalIndex = sorter.newLocalMapping(intType);
 Assert.assertEquals(expectedLocalIndex, actualLocalIndex);
 }
}