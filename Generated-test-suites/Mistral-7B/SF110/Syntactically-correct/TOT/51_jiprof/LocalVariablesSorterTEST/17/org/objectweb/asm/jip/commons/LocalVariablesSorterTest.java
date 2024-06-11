package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.assertArrayEquals;

import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

 @Test
 public void testLocalVariablesSorter() {
 final String testClass = "TestLocalVariablesSorter";
 final String testMethod = "testLocalVariablesSorting";
 final String desc = "(I)V"; // void method with one integer argument

 // Create test class with LocalVariablesSorter applied
 final ClassReader cr = new ClassReader(TestLocalVariablesSorter.class.getBytes());
 final ClassWriter cw = new ClassWriter(0);
 final LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(AccessFlag.PUBLIC, testClass + desc, cw);
 localVariablesSorter.visit(Opcodes.ASM5);
 localVariablesSorter.visitMethod(Opcodes.ACC_PUBLIC, testMethod, desc, null, null);
 localVariablesSorter.visitCode();
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitIntInsn(Opcodes.BIPUSH, 5);
 localVariablesSorter.visitIntInsn(Opcodes.IADD);
 localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 1);
 localVariablesSorter.visitIntInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
 localVariablesSorter.visitIntInsn(Opcodes.IRETURN);
 localVariablesSorter.visitEnd();
 final byte[] testClassBytes = cw.toByteArray();

 // Create original class reader for comparison
 final ClassReader originalClassReader = new ClassReader(TestLocalVariablesSorter.class.getBytes());

 // Test sorting of local variables
 final int[] originalMapping = new int[40];
 final Object[] originalLocals = new Object[20];
 final int[] sortedMapping = new int[40];
 final Object[] sortedLocals = new Object[20];

 // Get original local variables mapping and locals
 originalClassReader.accept(new MethodAdapter() {
 @Override
 public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
 originalMapping[index] = index;
 originalLocals[index - 1] = (Object)desc;
 }
 });

 // Sort local variables using LocalVariablesSorter
 localVariablesSorter.accept(new MethodAdapter() {
 @Override
 public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
 sortedMapping[index] = index;
 sortedLocals[index - 1] = (Object)desc;
 }
 });
 assertArrayEquals("Local variables mapping should be the same before and after sorting", originalMapping, sortedMapping);
 assertArrayEquals("Local variables types should be the same before and after sorting", originalLocals, sortedLocals);

 // Test visitFrame method with no changes
 final int maxStack = 3;
 final int maxLocals = 4;
 localVariablesSorter.visitFrame(Opcodes.FRAME_NONE, maxLocals, null, maxStack, null);

 // Test visitFrame method with changes
 localVariablesSorter.changed = true;
 final Object[] newLocals = new Object[20];
 localVariablesSorter.visitFrame(Opcodes.FRAME_OBJECT, maxLocals, newLocals, maxStack, null);
 assertArrayEquals("New locals array should be the same as the one set in visitFrame method", newLocals, sortedLocals);
 localVariablesSorter.changed = false;
 }
}