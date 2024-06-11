package org.objectweb.asm.jip.commons;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @Test
 public void testLocalVariablesSorterConstructor() {
 sorter = new LocalVariablesSorter(0x0001, "()V", mv);
 Assert.assertEquals(1, sorter.nextLocal);

 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);
 Assert.assertEquals(1, sorter.nextLocal);
 Assert.assertEquals(Type.INT_TYPE, sorter.getType(0));

 sorter = new LocalVariablesSorter(0x0002, "(Ljava/lang/String;)V", mv);
 Assert.assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 // Int
 sorter.visitVarInsn(21, 1);
 Assert.assertEquals(1, sorter.remap(1, Type.INT_TYPE));

 // Long
 sorter.visitVarInsn(22, 1);
 Assert.assertEquals(1, sorter.remap(1, Type.LONG_TYPE));

 // Double
 sorter.visitVarInsn(24, 1);
 Assert.assertEquals(1, sorter.remap(1, Type.DOUBLE_TYPE));

 // Float
 sorter.visitVarInsn(23, 1);
 Assert.assertEquals(1, sorter.remap(1, Type.FLOAT_TYPE));

 // Object
 sorter.visitVarInsn(16, 1);
 Assert.assertEquals(1, sorter.remap(1, LocalVariablesSorter.OBJECT_TYPE));
 }

 @Test
 public void testVisitIincInsn() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 sorter.visitIincInsn(1, 1);
 Assert.assertEquals(1, sorter.remap(1, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 sorter.visitMaxs(2, 3);
 Assert.assertEquals(3, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 sorter.visitLocalVariable("i", "I", null, null, null, 0);
 Assert.assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitFrame() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 Object[] locals = {Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
 sorter.visitFrame(0, 3, locals, 1, null);
 Assert.assertEquals(3, sorter.nextLocal);

 int[] remappedLocals = {1, 2, 3};
 for (int i = 0; i < locals.length; i++) {
 Assert.assertEquals(remappedLocals[i], sorter.remap(i, Type.getIntType()));
 }
 }

 @Test
 public void testNewLocal() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);

 int newLocalInt = sorter.newLocal(Type.INT_TYPE);
 Assert.assertEquals(1, sorter.remap(newLocalInt, Type.INT_TYPE));

 int newLocalLong = sorter.newLocal(Type.LONG_TYPE);
 Assert.assertEquals(2, sorter.remap(newLocalLong, Type.LONG_TYPE));

 int newLocalObject = sorter.newLocal(Type.getObjectType("java/lang/String"));
 Assert.assertEquals(3, sorter.remap(newLocalObject, Type.getObjectType("java/lang/String")));
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException() {
 sorter = new LocalVariablesSorter(0x0001, "(I)I", mv);
 sorter.visitFrame(-1, 3, null, 1, null);
 }
}