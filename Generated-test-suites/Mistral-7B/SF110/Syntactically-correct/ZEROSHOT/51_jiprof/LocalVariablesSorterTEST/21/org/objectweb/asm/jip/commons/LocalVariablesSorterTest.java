package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

 private static final int ACCESS_PUBLIC = 0x0001;
 private static final int DESC_INT_INT = "(II)V";
 private static final int DESC_INT_LONG = "(IL)V";
 private static final int DESC_INT_OBJECT = "(Ljava/lang/Object;)V";
 private static final int DESC_VOID_OBJECT = "()Ljava/lang/Object;";

 @Test
 public void testLocalVariablesSorter() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_INT_INT, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });

 // Test visitVarInsn
 localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
 localVariablesSorter.visitVarInsn(Opcodes.IADD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
 localVariablesSorter.visitInsn(Opcodes.IRETURN);
 localVariablesSorter.visitMaxs(1, 2);

 int[] expectedMappingIntInt = new int[]{0, 1, 2};
 int[] actualMappingIntInt = localVariablesSorter.mapping.clone();
 assertEquals(expectedMappingIntInt, actualMappingIntInt);

 // Test visitLocalVariable
 localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_INT_LONG, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });
 localVariablesSorter.visitCode();
 localVariablesSorter.visitVarInsn(Opcodes.LLOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.LADD, 1);
 localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
 localVariablesSorter.visitInsn(Opcodes.LRETURN);
 localVariablesSorter.visitMaxs(1, 2);
 localVariablesSorter.visitEnd();

 int[] expectedMappingIntLong = new int[]{0, 2, 3};
 int[] actualMappingIntLong = localVariablesSorter.mapping.clone();
 assertEquals(expectedMappingIntLong, actualMappingIntLong);

 // Test visitLocalVariable with Object type
 localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_INT_OBJECT, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });
 localVariablesSorter.visitCode();
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 1);
 localVariablesSorter.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/lang/Object", "toString", "()Ljava/lang/String;");
 localVariablesSorter.visitInsn(Opcodes.IRETURN);
 localVariablesSorter.visitMaxs(1, 2);
 localVariablesSorter.visitEnd();

 Type objectType = Type.getType("java/lang/String");
 int[] expectedMappingIntObject = new int[]{0, 1, 2};
 int[] actualMappingIntObject = localVariablesSorter.mapping.clone();
 assertEquals(expectedMappingIntObject, actualMappingIntObject);
 assertEquals(objectType, localVariablesSorter.newLocals[1]);

 // Test visitFrame
 localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_INT_OBJECT, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });
 localVariablesSorter.visitCode();
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 1);
 localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 1);
 localVariablesSorter.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/lang/Object", "toString", "()Ljava/lang/String;");
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 2);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 2);
 localVariablesSorter.visitInsn(Opcodes.ARETURN);
 localVariablesSorter.visitMaxs(1, 3);
 localVariablesSorter.visitEnd();

 Object[] expectedLocalsIntObjectString = new Object[]{null, null, "java.lang.String"};
 Object[] actualLocalsIntObjectString = localVariablesSorter.newLocals;
 assertEquals(expectedLocalsIntObjectString, actualLocalsIntObjectString);

 localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_VOID_OBJECT, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });
 localVariablesSorter.visitCode();
 localVariablesSorter.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
 localVariablesSorter.visitInsn(Opcodes.RETURN);
 localVariablesSorter.visitMaxs(1, 1);
 localVariablesSorter.visitEnd();

 // Test no change when no local variables are added
 localVariablesSorter = new LocalVariablesSorter(ACCESS_PUBLIC, DESC_VOID_VOID, new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 // empty
 }
 });
 localVariablesSorter.visitCode();
 localVariablesSorter.visitInsn(Opcodes.RETURN);
 localVariablesSorter.visitMaxs(1, 0);
 localVariablesSorter.visitEnd();

 Object[] expectedNoLocals = new Object[0];
 Object[] actualNoLocals = localVariablesSorter.newLocals;
 assertEquals(expectedNoLocals, actualNoLocals);
 }
}