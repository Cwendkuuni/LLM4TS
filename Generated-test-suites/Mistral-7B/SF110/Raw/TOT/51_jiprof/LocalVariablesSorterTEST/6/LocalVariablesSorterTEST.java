package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 mv = new MethodVisitor(0, null, null, null) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 localVariablesSorter.visitVarInsn(opcode, var);
 }

 @Override
 public void visitIincInsn(int var, int increment) {
 localVariablesSorter.visitIincInsn(var, increment);
 }

 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 }

 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 }

 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
 }

 @Override
 public void visitEnd() {
 // Do nothing
 }
 };
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 Type intType = Type.INT_TYPE;
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 1);
 Type longType = Type.LONG_TYPE;
 localVariablesSorter.visitVarInsn(Opcodes.LLOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.LLOAD, 1);
 Type floatType = Type.FLOAT_TYPE;
 localVariablesSorter.visitVarInsn(Opcodes.FLOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.FLOAD, 1);
 Type doubleType = Type.DOUBLE_TYPE;
 localVariablesSorter.visitVarInsn(Opcodes.DLOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.DLOAD, 1);
 Type objectType = Type.getObjectType("java/lang/String");
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 2);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 2);
 localVariablesSorter.visitVarInsn(Opcodes.PUTFIELD, "java/lang/String", "value", "Ljava/lang/String;");
 localVariablesSorter.mv.visitMaxs(2, 3);

 int[] expectedMapping = new int[]{0, 1, 2};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testVisitVarInsn", expectedMapping, expectedNewLocals);
 }

 @Test
 public void testVisitIincInsn() {
 localVariablesSorter.visitIincInsn(0, 1);
 localVariablesSorter.visitIincInsn(1, 2);
 localVariablesSorter.mv.visitMaxs(2, 2);

 int[] expectedMapping = new int[]{0, 1, 2, 3};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testVisitIincInsn", expectedMapping, expectedNewLocals);
 }

 @Test
 public void testVisitMaxs() {
 localVariablesSorter.visitMaxs(3, 3);
 localVariablesSorter.mv.visitFrame(0, 3, null, 3, null);
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 localVariablesSorter.visitMaxs(2, 2);
 localVariablesSorter.mv.visitFrame(0, 2, null, 2, null);

 int[] expectedMapping = new int[]{0, 1, 2, 3, 4, 5};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testVisitMaxs", expectedMapping, expectedNewLocals);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "test";
 Type intType = Type.INT_TYPE;
 localVariablesSorter.visitLocalVariable(name, intType.getDescriptor(), null, new Label(), new Label(), 0);
 localVariablesSorter.visitLocalVariable(name, intType.getDescriptor(), null, new Label(), new Label(), 1);
 localVariablesSorter.visitLocalVariable("anotherTest", intType.getDescriptor(), null, new Label(), new Label(), 2);
 localVariablesSorter.mv.visitFrame(0, 3, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER}, 3, null);
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 localVariablesSorter.visitLocalVariable(name, intType.getDescriptor(), null, new Label(), new Label(), 0);
 localVariablesSorter.visitLocalVariable("anotherTest", intType.getDescriptor(), null, new Label(), new Label(), 1);
 localVariablesSorter.mv.visitFrame(0, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 2, null);

 int[] expectedMapping = new int[]{0, 1, 2, 3, 4, 5};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testVisitLocalVariable", expectedMapping, expectedNewLocals);
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException() {
 localVariablesSorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(0, null, null, null) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 // Do nothing
 }
 });
 localVariablesSorter.mv.visitFrame(0, 2, null, 2, null);
 }

 @Test
 public void testVisitFrame() {
 Object[] locals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT};
 localVariablesSorter.visitFrame(0, locals.length, locals, 4, null);
 localVariablesSorter.mv.visitFrame(0, locals.length + 1, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT, Opcodes.INTEGER}, 4, null);
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 localVariablesSorter.visitFrame(0, locals.length, locals, 4, null);
 localVariablesSorter.mv.visitFrame(0, locals.length, locals, 4, null);

 int[] expectedMapping = new int[]{0, 1, 2, 3, 4, 5, 6};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testVisitFrame", expectedMapping, expectedNewLocals);
 }

 @Test
 public void testNewLocal() {
 Type intType = Type.INT_TYPE;
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(intType);
 localVariablesSorter.newLocal(floatType);
 localVariablesSorter.newLocal(longType);
 localVariablesSorter.newLocal(doubleType);
 localVariablesSorter.newLocal(objectType);

 int expectedNextLocal = 7;
 int[] expectedMapping = new int[]{0, 1, 2, 3, 4, 5, 6};
 Object[] expectedNewLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, "java/lang/Object", Opcodes.TOP};
 verifyMappingAndNewLocalVariables("testNewLocal", expectedMapping, expectedNewLocals);
 }

 private void verifyMappingAndNewLocalVariables(String testName, int[] expectedMapping, Object[] expectedNewLocals) {
 assertEquals("Mapping is incorrect", expectedMapping, localVariablesSorter.mapping);
 assertEquals("New locals are incorrect", expectedNewLocals, localVariablesSorter.newLocals);
 localVariablesSorter.changed = false;
 ClassReader cr = new ClassReader(LocalVariablesSorter.class.getBytes());
 cr.accept(new ClassReader.ExClassVisitor(new LocalVariablesSorter(0, "()V", mv)) {
 @Override
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
 assertEquals("Frame locals are incorrect for test " + testName, expectedNewLocals, local);
 super.visitFrame(type, nLocal, local, nStack, stack);
 }
 });
 }

}