package org.objectweb.asm.jip.commons.test;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

 private MethodVisitor mv;
 private LocalVariablesSorter lvs;

 @Before
 public void setUp() {
 mv = new Bytecode().newMethodVisitor(0, null, null, null);
 lvs = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 // Test int variable
 lvs.visitVarInsn(21, 0);
 assertEquals(0, lvs.remap(0, Type.INT_TYPE));
 // Test long variable
 lvs.visitVarInsn(22, 1);
 assertEquals(1, lvs.remap(1, Type.LONG_TYPE));
 // Test float variable
 lvs.visitVarInsn(23, 2);
 assertEquals(2, lvs.remap(2, Type.FLOAT_TYPE));
 // Test double variable
 lvs.visitVarInsn(24, 3);
 assertEquals(3, lvs.remap(3, Type.DOUBLE_TYPE));
 // Test object variable
 lvs.visitVarInsn(25, "java/lang/String");
 assertEquals(4, lvs.remap(4, Type.getType("java/lang/String")));
 }

 @Test
 public void testVisitIincInsn() {
 // Test int variable
 lvs.visitIincInsn(0, 1);
 assertEquals(0, lvs.remap(0, Type.INT_TYPE));
 // Test long variable
 lvs.visitIincInsn(1, 1L);
 assertEquals(1, lvs.remap(1, Type.LONG_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxLocals = 5;
 int maxStack = 10;
 lvs.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, lvs.nextLocal);
 assertEquals(maxLocals, lvs.mapping.length);
 }

 @Test
 public void testVisitLocalVariable() {
 // Test int variable
 lvs.visitLocalVariable("i", "I", null, null, null, 0);
 assertEquals(0, lvs.remap(0, Type.INT_TYPE));
 // Test long variable
 lvs.visitLocalVariable("l", "J", null, null, null, 1);
 assertEquals(1, lvs.remap(1, Type.LONG_TYPE));
 // Test float variable
 lvs.visitLocalVariable("f", "F", null, null, null, 2);
 assertEquals(2, lvs.remap(2, Type.FLOAT_TYPE));
 // Test double variable
 lvs.visitLocalVariable("d", "D", null, null, null, 3);
 assertEquals(3, lvs.remap(3, Type.DOUBLE_TYPE));
 // Test object variable
 lvs.visitLocalVariable("s", "Ljava/lang/String;", null, null, null, 4);
 assertEquals(4, lvs.remap(4, Type.getType("java/lang/String")));
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[3];
 local[0] = Opcodes.INTEGER;
 local[1] = Opcodes.LONG;
 local[2] = Opcodes.FLOAT;
 lvs.visitFrame(0, 3, local, 1, new Object[1]);
 assertEquals(3, lvs.nextLocal);
 assertEquals(3, lvs.mapping.length);
 assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT}, lvs.newLocals);
 }

 @Test
 public void testNewLocal() {
 // Test int variable
 int intLocal = lvs.newLocal(Type.INT_TYPE);
 assertEquals(5, lvs.nextLocal);
 assertEquals(intLocal, lvs.remap(intLocal, Type.INT_TYPE));
 // Test float variable
 int floatLocal = lvs.newLocal(Type.FLOAT_TYPE);
 assertEquals(6, lvs.nextLocal);
 assertEquals(floatLocal, lvs.remap(floatLocal, Type.FLOAT_TYPE));
 // Test long variable
 int longLocal = lvs.newLocal(Type.LONG_TYPE);
 assertEquals(7, lvs.nextLocal);
 assertEquals(longLocal, lvs.remap(longLocal, Type.LONG_TYPE));
 // Test object variable
 int objectLocal = lvs.newLocal(Type.getType("java/lang/String"));
 assertEquals(8, lvs.nextLocal);
 assertEquals(objectLocal, lvs.remap(objectLocal, Type.getType("java/lang/String")));
 }
}