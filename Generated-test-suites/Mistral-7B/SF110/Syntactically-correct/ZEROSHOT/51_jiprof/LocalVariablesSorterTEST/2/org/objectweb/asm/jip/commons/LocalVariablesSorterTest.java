package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testLocalVariablesSorterConstruction() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "()V", new MethodVisitor(0, null));
 assertEquals(1, sorter.nextLocal);
 }

 @Test
 public void testLocalVariablesSorterWithArguments() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 assertEquals(3, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 sorter.visitVarInsn(21, 0); // int
 sorter.visitVarInsn(22, 1); // long
 sorter.visitVarInsn(23, 2); // float
 sorter.visitVarInsn(24, 3); // double
 sorter.visitVarInsn(54, 4); // int
 sorter.visitVarInsn(55, 5); // long
 sorter.visitVarInsn(56, 6); // float
 sorter.visitVarInsn(57, 7); // double

 assertEquals(8, sorter.nextLocal);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 sorter.visitIincInsn(0, 1);
 sorter.visitIincInsn(1, 2);

 assertEquals(3, sorter.nextLocal);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 sorter.visitMaxs(10, 5);

 assertEquals(5, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 sorter.visitLocalVariable("i1", "I", null, null, null, 0);
 sorter.visitLocalVariable("l1", "J", null, null, null, 1);
 sorter.visitLocalVariable("f1", "F", null, null, null, 2);
 sorter.visitLocalVariable("d1", "D", null, null, null, 3);
 sorter.visitLocalVariable("i2", "I", null, null, null, 4);
 sorter.visitLocalVariable("l2", "J", null, null, null, 5);
 sorter.visitLocalVariable("f2", "F", null, null, null, 6);
 sorter.visitLocalVariable("d2", "D", null, null, null, 7);

 assertEquals(8, sorter.nextLocal);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 Object[] locals = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.LONG_TYPE};
 sorter.visitFrame(0, 5, locals, 1, null);
 sorter.visitFrame(0, 5, locals, 1, null);
 sorter.visitFrame(0, 5, locals, 1, null);

 assertTrue(sorter.changed);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(0x0029, "(II)I", new MethodVisitor(0, null));
 sorter.newLocal(Type.INT_TYPE);
 sorter.newLocal(Type.FLOAT_TYPE);
 sorter.newLocal(Type.LONG_TYPE);
 sorter.newLocal(Type.DOUBLE_TYPE);
 sorter.newLocal(Type.getType("java/lang/String"));

 assertEquals(11, sorter.nextLocal);
 }
}