package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;
import static org.objectweb.asm.Opcodes.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 }
 });
 }

 @Test
 public void testVisitVarInsn() {
 sorter.visitVarInsn(21, 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 sorter.visitVarInsn(22, 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitVarInsnWithDoubleType() {
 sorter.visitVarInsn(24, 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 sorter.visitVarInsn(23, 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 sorter.visitVarInsn(21, objectType.getIndex());
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitIincInsn() {
 sorter.visitIincInsn(0, 1);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitFrame() {
 Object[] locals = new Object[]{INTEGER, INTEGER};
 sorter.visitFrame(255, 2, locals, 1, null);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithInvalidType() {
 sorter.visitFrame(1, 2, new Object[]{INTEGER, INTEGER}, 1, null);
 fail("Expected an IllegalStateException");
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithoutChangedFlag() {
 sorter = new LocalVariablesSorter(ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 }

 @Override
 public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 sorter.visitFrame(255, 2, new Object[]{INTEGER, INTEGER}, 1, null);
 fail("Expected an IllegalStateException");
 }

 @Test
 public void testVisitLocalVariableWithIntegerType() {
 sorter.visitLocalVariable("i", "I", null, new Label(0), new Label(1), 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitLocalVariableWithFloatType() {
 sorter.visitLocalVariable("f", "F", null, new Label(0), new Label(1), 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitLocalVariableWithLongType() {
 sorter.visitLocalVariable("l", "J", null, new Label(0), new Label(1), 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitLocalVariableWithDoubleType() {
 sorter.visitLocalVariable("d", "D", null, new Label(0), new Label(1), 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testVisitLocalVariableWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 sorter.visitLocalVariable("s", objectType.getDescriptor(), null, new Label(0), new Label(1), 0);
 assertTrue(sorter.mv.instructions.size() > 0);
 }

 @Test
 public void testNewLocalWithIntegerType() {
 int local = sorter.newLocal(Type.INT_TYPE);
 assertEquals(local, sorter.mv.localVariables.size());
 }

 @Test
 public void testNewLocalWithFloatType() {
 int local = sorter.newLocal(Type.FLOAT_TYPE);
 assertEquals(local + 1, sorter.mv.localVariables.size());
 }

 @Test
 public void testNewLocalWithLongType() {
 int local = sorter.newLocal(Type.LONG_TYPE);
 assertEquals(local + 2, sorter.mv.localVariables.size());
 }

 @Test
 public void testNewLocalWithDoubleType() {
 int local = sorter.newLocal(Type.DOUBLE_TYPE);
 assertEquals(local + 2, sorter.mv.localVariables.size());
 }

 @Test
 public void testNewLocalWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 int local = sorter.newLocal(objectType);
 assertEquals(local + 1, sorter.mv.localVariables.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 sorter.newLocal(Type.BOOLEAN_TYPE);
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testSetLocalType() {
 sorter.setLocalType(0, Type.INT_TYPE);
 assertEquals(Type.INT_TYPE, sorter.getLocalType(0));
 }

 @Test
 public void testSetFrameLocal() {
 Object[] locals = new Object[]{INTEGER, INTEGER};
 sorter.visitFrame(255, 2, locals, 1, null);
 sorter.setFrameLocal(0, INTEGER);
 assertEquals(INTEGER, sorter.mv.localVariables.get(0).desc);
 }

 @Test
 public void testNewLocalMappingWithIntegerType() {
 int local = sorter.newLocalMapping(Type.INT_TYPE);
 assertEquals(local, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalMappingWithFloatType() {
 int local = sorter.newLocalMapping(Type.FLOAT_TYPE);
 assertEquals(local + 1, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalMappingWithLongType() {
 int local = sorter.newLocalMapping(Type.LONG_TYPE);
 assertEquals(local + 2, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalMappingWithDoubleType() {
 int local = sorter.newLocalMapping(Type.DOUBLE_TYPE);
 assertEquals(local + 2, sorter.mapping[local]);
 }

 @Test
 public void testNewLocalMappingWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 int local = sorter.newLocalMapping(objectType);
 assertEquals(local + 1, sorter.mapping[local]);
 }

 @Test
 public void testVisitFrameWithChangedFlag() {
 Object[] locals = new Object[]{INTEGER, INTEGER};
 sorter.visitFrame(255, 2, locals, 1, null);
 assertTrue(sorter.changed);
 }

 @Test
 public void testVisitFrameWithNullLocalArray() {
 sorter.visitFrame(255, 2, null, 1, null);
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testVisitFrameWithInvalidStackArray() {
 sorter.visitFrame(255, 2, new Object[]{INTEGER, INTEGER}, 0, null);
 fail("Expected an IllegalArgumentException");
 }
}