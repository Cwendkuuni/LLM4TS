package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {

 private static final String CLASS_NAME = "TestLocalVariablesSorter";
 private static final String METHOD_NAME = "testMethod";
 private static final String DESCRIPTION = "(Ljava/lang/String;)V";

 @Test
 public void testLocalVariablesSorter() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 System.out.println("Method " + MethodName() + " ended.");
 }
 });

 assertEquals(Opcodes.ACC_PUBLIC, localVariablesSorter.access);
 assertEquals(CLASS_NAME, localVariablesSorter.className);
 assertEquals(METHOD_NAME, localVariablesSorter.name);
 assertEquals(DESCRIPTION, localVariablesSorter.desc);
 assertEquals(Opcodes.ACC_PUBLIC, localVariablesSorter.access);
 assertEquals(3, localVariablesSorter.nextLocal);

 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 System.out.println("Method " + MethodName() + " ended.");
 }
 });

 assertEquals(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, localVariablesSorter.access);
 assertEquals(CLASS_NAME, localVariablesSorter.className);
 assertEquals(METHOD_NAME, localVariablesSorter.name);
 assertEquals(DESCRIPTION, localVariablesSorter.desc);
 assertEquals(1, localVariablesSorter.nextLocal);

 ClassReader classReader = new ClassReader(CLASS_NAME);
 classReader.accept(new LocalVariablesSorter(Opcodes.ACC_PUBLIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 System.out.println("Method " + MethodName() + " ended.");
 }

 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 System.out.println("Visited local variable " + name + " with index " + index + " and type " + Type.getDescriptor(Type.getType(desc)) + ".");
 }
 }));

 assertEquals(1, classReader.getMethods()[0].getLocalVariables().length);
 assertEquals("test", classReader.getMethods()[0].getLocalVariables()[0].name);
 assertEquals("Ljava/lang/String;", Type.getDescriptor(Type.getType(classReader.getMethods()[0].getLocalVariables()[0].desc)));

 classReader = new ClassReader(CLASS_NAME);
 classReader.accept(new LocalVariablesSorter(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 System.out.println("Visited local variable " + name + " with index " + index + " and type " + Type.getDescriptor(Type.getType(desc)) + ".");
 }
 }));

 assertEquals(0, classReader.getMethods()[0].getLocalVariables().length);

 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitVarInsn(int opcode, int var) {
 super.visitVarInsn(opcode, localVariablesSorter.remap(var, Type.INT_TYPE));
 }

 @Override
 public void visitIincInsn(int var, int increment) {
 super.visitIincInsn(localVariablesSorter.remap(var, Type.INT_TYPE), increment);
 }

 @Override
 public void visitMaxs(int maxStack, int maxLocals) {
 super.visitMaxs(maxStack, localVariablesSorter.nextLocal);
 }

 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 final int newIndex = localVariablesSorter.remap(index, Type.getType(desc));
 super.visitLocalVariable(name, desc, signature, start, end, newIndex);
 }
 });

 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 1);
 localVariablesSorter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "length", "()I");
 localVariablesSorter.visitIntInsn(Opcodes.IADD, 5);
 localVariablesSorter.visitIntInsn(Opcodes.ISTORE, 2);
 localVariablesSorter.visitIntInsn(Opcodes.ILOAD, 2);
 localVariablesSorter.visitIntInsn(Opcodes.ICONST_1);
 localVariablesSorter.visitIntInsn(Opcodes.ISUB);
 localVariablesSorter.visitIntInsn(Opcodes.ISTORE, 2);
 localVariablesSorter.visitInsn(Opcodes.RETURN);

 localVariablesSorter.visitFrame(0, 0, null, 0, null);

 byte[] bytecode = localVariablesSorter.mv.toByteArray();
 Class<?> clazz = new ClassLoader().defineClass(CLASS_NAME, bytecode, 0, bytecode.length);

 Method method = clazz.getMethod(METHOD_NAME, String.class);
 Object result = method.invoke(clazz.newInstance(), "Test");

 assertEquals(5, ((Integer)result).intValue());

 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });

 localVariablesSorter.visitFrame(Opcodes.F_CODE, 1, null, 1, new Object[] {"java/lang/String"});
 localVariablesSorter.visitLocalVariable("str", "Ljava/lang/String;", null, null, null, 0);
 localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 0);
 localVariablesSorter.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 localVariablesSorter.visitLdcInsn("Hello World!");
 localVariablesSorter.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
 localVariablesSorter.visitInsn(Opcodes.RETURN);

 localVariablesSorter.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

 bytecode = localVariablesSorter.mv.toByteArray();
 clazz = new ClassLoader().defineClass(CLASS_NAME, bytecode, 0, bytecode.length);

 method = clazz.getMethod(METHOD_NAME, null);
 method.invoke(clazz.newInstance());

 assertNull(result);
 }

 private int remapLocalVariable(LocalVariablesSorter localVariablesSorter, int index, Type type) {
 int actualIndex = localVariablesSorter.mapping[2 * index + type.getSize()];
 if (actualIndex == 0) {
 actualIndex = localVariablesSorter.newLocalMapping(type);
 localVariablesSorter.setLocalType(actualIndex, type);
 localVariablesSorter.mapping[2 * index + type.getSize()] = actualIndex + 1;
 }
 return actualIndex - 1;
 }

 private void testVisitLocalVariable(LocalVariablesSorter localVariablesSorter) {
 localVariablesSorter.visitLocalVariable("test", Type.INT_TYPE.getDescriptor(), null, null, null, 0);
 assertEquals(-1, localVariablesSorter.remap(0, Type.INT_TYPE));
 assertEquals(0, localVariablesSorter.remap(0, Type.INT_TYPE));
 assertEquals(1, localVariablesSorter.remap(1, Type.INT_TYPE));
 assertEquals(2, localVariablesSorter.remap(2, Type.INT_TYPE));
 assertEquals(3, localVariablesSorter.remap(3, Type.INT_TYPE));
 assertEquals(4, localVariablesSorter.remap(4, Type.INT_TYPE));
 assertEquals(5, localVariablesSorter.remap(5, Type.INT_TYPE));
 assertEquals(0, localVariablesSorter.remap(6, Type.INT_TYPE));
 assertEquals(0, localVariablesSorter.remap(7, Type.INT_TYPE));
 assertEquals(-1, localVariablesSorter.remap(-1, Type.INT_TYPE));
 assertEquals(-1, localVariablesSorter.remap(0, Type.DOUBLE_TYPE));
 assertEquals(-1, localVariablesSorter.remap(0, Type.FLOAT_TYPE));
 assertEquals(-1, localVariablesSorter.remap(0, Type.LONG_TYPE));
 assertEquals(-1, localVariablesSorter.remap(0, Type.OBJECT_TYPE));
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 testVisitLocalVariable(localVariablesSorter);
 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 testVisitLocalVariable(localVariablesSorter);
 }

 @Test
 public void testRemapping() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, CLASS_NAME + MethodName(), new MethodVisitor(Opcodes.ASM5, null) {
 @Override
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
 super.visitLocalVariable(name, desc, signature, start, end, index);
 }
 });
 int index = 0;
 for (int i = 0; i < 6; ++i) {
 localVariablesSorter.visitLocalVariable("test" + i, Type.INT_TYPE.getDescriptor(), null, null, null, index++);
 }
 testVisitLocalVariable(localVariablesSorter);
 int initialIndex = 0;
 for (int i = 0; i < 6; ++i) {
 int actualIndex = localVariablesSorter.remap(i, Type.INT_TYPE);
 assertEquals(initialIndex++, actualIndex);
 }
 initialIndex = 6;
 localVariablesSorter.visitFrame(Opcodes.F_APPEND, 1, new Object[] {Type.LONG_TYPE});
 testVisitLocalVariable(localVariablesSorter);
 for (int i = 0; i < 6; ++i) {
 int actualIndex = localVariablesSorter.remap(i, Type.INT_TYPE);
 assertEquals(initialIndex++, actualIndex + 1);
 }
 }
}