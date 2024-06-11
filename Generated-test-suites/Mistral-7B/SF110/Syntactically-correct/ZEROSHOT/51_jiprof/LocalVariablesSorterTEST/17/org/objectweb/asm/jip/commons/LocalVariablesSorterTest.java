package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn_int() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitVarInsn(21, 0); // int i
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitVarInsn_long() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitVarInsn(22, 0); // long l
 assertEquals(0, sorter.remap(0, Type.LONG_TYPE));
 }

 @Test
 public void testVisitVarInsn_double() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitVarInsn(24, 0); // double d
 assertEquals(0, sorter.remap(0, Type.DOUBLE_TYPE));
 }

 @Test
 public void testVisitVarInsn_float() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitVarInsn(23, 0); // float f
 assertEquals(0, sorter.remap(0, Type.FLOAT_TYPE));
 }

 @Test
 public void testVisitVarInsn_object() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0003, "(Ljava/lang/String;)V", new MethodVisitor(0, null));
 sorter.visitVarInsn(25, 0); // Ljava/lang/String; str
 assertEquals(0, sorter.remap(0, Type.getType("java.lang.String")));
 }

 @Test
 public void testVisitVarInsn_multipleTypes() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0010, "(II)V", new MethodVisitor(0, null));
 sorter.visitVarInsn(21, 0); // int i
 sorter.visitVarInsn(21, 1); // int j
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 assertEquals(1, sorter.remap(1, Type.INT_TYPE));
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitIincInsn(0, 1);
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 sorter.visitLocalVariable("i", "I", null, new Label(), new Label(), 0);
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 Object[] local = {Opcodes.INTEGER, Opcodes.LONG};
 sorter.visitFrame(-1, 2, local, 0, null);
 assertEquals(2, sorter.newLocals.length);
 assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.TOP}, sorter.newLocals);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(0, null));
 Type intType = Type.INT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type stringType = Type.getType("java.lang.String");
 int localInt = sorter.newLocal(intType);
 int localLong = sorter.newLocal(longType);
 int localFloat = sorter.newLocal(floatType);
 int localDouble = sorter.newLocal(doubleType);
 int localObject = sorter.newLocal(stringType);
 assertEquals(1, sorter.remap(localInt, Type.INT_TYPE));
 assertEquals(1, sorter.remap(localLong, Type.LONG_TYPE));
 assertEquals(1, sorter.remap(localFloat, Type.FLOAT_TYPE));
 assertEquals(1, sorter.remap(localDouble, Type.DOUBLE_TYPE));
 assertEquals(1, sorter.remap(localObject, stringType));
 }
}