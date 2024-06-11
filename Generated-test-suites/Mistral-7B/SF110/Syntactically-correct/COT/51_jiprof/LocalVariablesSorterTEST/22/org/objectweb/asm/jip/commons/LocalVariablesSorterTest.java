package org.objectweb.asm.jip.commons.test;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {}
 });
 }

 @Test
 public void testVisitVarInsn() {
 int var = 0;
 Type type = Type.INT_TYPE;
 sorter.visitVarInsn(Opcodes.ISTORE, var);
 Assert.assertEquals(var, sorter.remap(var, type));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithInvalidOpcode() {
 Type type = Type.INT_TYPE;
 sorter.visitVarInsn(Opcodes.IADD, variableIndex);
 }

 @Test
 public void testVisitIincInsn() {
 int var = 0;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 Assert.assertEquals(var, sorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 1;
 int maxLocals = 2;
 sorter.visitMaxs(maxStack, maxLocals);
 Assert.assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "";
 Label start = new Label();
 Label end = new Label();
 int index = 0;
 Type type = Type.getType(desc);
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, type);
 Assert.assertEquals(newIndex, sorter.mapping[index * 2 + type.getSize()]);
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[] {Type.LONG_TYPE, Type.FLOAT_TYPE, Type.INTEGER};
 int nLocal = local.length;
 int nStack = 1;
 Object[] stack = new Object[1];
 sorter.visitFrame(Opcodes.FRAME_TYPE, nLocal, local, nStack, stack);
 Assert.assertEquals(nLocal, sorter.nextLocal);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.LONG_TYPE;
 int newLocal = sorter.newLocal(type);
 Assert.assertEquals(Math.max(1, sorter.nextLocal - type.getSize()), newLocal);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 Type type = new Type("java/lang/String");
 sorter.newLocal(type);
 }

 @Test
 public void testSetFrameLocal() {
 Object type = Type.LONG_TYPE;
 int local = sorter.newLocal(type);
 sorter.setFrameLocal(local, type);
 Object[] newLocals = sorter.newLocals;
 Assert.assertEquals(type, newLocals[local]);
 }

 @Test
 public void testRemap() {
 int var = 0;
 Type type = Type.INT_TYPE;
 int mappedVar = sorter.remap(var, type);
 Assert.assertEquals(var, mappedVar);
 }

 @Test
 public void testRemapWithInvalidVarIndex() {
 int var = LocalVariablesSorter.MAX_LOCALS + 1;
 Type type = Type.INT_TYPE;
 int mappedVar = sorter.remap(var, type);
 Assert.assertEquals(-1, mappedVar);
 }

 @Test
 public void testRemapWithInvalidType() {
 int var = 0;
 Type type = new Type("java/lang/String");
 int mappedVar = sorter.remap(var, type);
 Assert.assertEquals(-1, mappedVar);
 }

 @Test
 public void testChangedFlag() {
 sorter.visitFrame(Opcodes.FRAME_TYPE_EXTENDED, 1, new Object[] {Type.LONG_TYPE}, 2, new Object[] {Type.LONG_TYPE, Type.INT_TYPE});
 Assert.assertTrue(sorter.changed);
 }

 @Test
 public void testNoChangedFlag() {
 sorter.visitVarInsn(Opcodes.ISTORE, 0);
 Assert.assertFalse(sorter.changed);
 }

}