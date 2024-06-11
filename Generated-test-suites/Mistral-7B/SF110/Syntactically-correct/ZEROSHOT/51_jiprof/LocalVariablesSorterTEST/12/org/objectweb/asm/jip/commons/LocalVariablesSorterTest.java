package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @Before
 public void setup() {
 sorter = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 // Test int
 sorter.visitVarInsn(21, 0);
 assertEquals(0, sorter.remap(0, Type.getIntType()));
 // Test long
 sorter.visitVarInsn(22, 1);
 assertEquals(1, sorter.remap(1, Type.getLongType()));
 // Test double
 sorter.visitVarInsn(24, 2);
 assertEquals(2, sorter.remap(2, Type.getDoubleType()));
 // Test float
 sorter.visitVarInsn(23, 3);
 assertEquals(3, sorter.remap(3, Type.getFloatType()));
 // Test Object
 sorter.visitVarInsn(25, 4);
 assertEquals(4, sorter.remap(4, Type.getObjectType("java/lang/Object")));
 // Test null
 sorter.visitVarInsn(25, 5);
 assertEquals(5, sorter.remap(5, LocalVariablesSorter.OBJECT_TYPE));
 }

 @Test
 public void testVisitIincInsn() {
 // Test int
 sorter.visitIincInsn(0, 1);
 assertEquals(0, sorter.remap(0, Type.getIntType()));
 // Test long
 sorter.visitIincInsn(1, 2);
 assertEquals(1, sorter.remap(1, Type.getLongType()));
 }

 @Test
 public void testVisitMaxs() {
 int maxLocals = 10;
 int maxStack = 5;
 // Test no change
 sorter.visitMaxs(maxStack, sorter.nextLocal);
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(sorter.nextLocal, sorter.mv.maxLocals);
 // Test change
 int newNextLocal = sorter.nextLocal + 3;
 sorter.visitLocalVariable("test", "I", null, null, null, 1);
 sorter.visitLocalVariable("test2", "I", null, null, null, 2);
 sorter.visitLocalVariable("test3", "I", null, null, null, 3);
 sorter.visitMaxs(maxStack, newNextLocal);
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(newNextLocal, sorter.mv.maxLocals);
 }

 @Test
 public void testVisitLocalVariable() {
 // Test int
 sorter.visitLocalVariable("test", "I", null, null, null, 1);
 assertEquals(1, sorter.remap(1, Type.getIntType()));
 // Test long
 sorter.visitLocalVariable("test", "J", null, null, null, 1);
 assertEquals(1, sorter.remap(1, Type.getLongType()));
 // Test float
 sorter.visitLocalVariable("test", "F", null, null, null, 1);
 assertEquals(1, sorter.remap(1, Type.getFloatType()));
 // Test double
 sorter.visitLocalVariable("test", "D", null, null, null, 1);
 assertEquals(1, sorter.remap(1, Type.getDoubleType()));
 // Test Object
 sorter.visitLocalVariable("test", "Ljava/lang/Object;", null, null, null, 1);
 assertEquals(1, sorter.remap(1, Type.getObjectType("java/lang/Object")));
 // Test null
 sorter.visitLocalVariable("test", "Ljava/lang/Object;", null, null, null, 1);
 assertEquals(1, sorter.remap(1, LocalVariablesSorter.OBJECT_TYPE));
 }

 @Test
 public void testVisitFrame() {
 Object[] local = {Opcodes.INTEGER, Opcodes.LONG, Opcodes.INTEGER, Opcodes.FLOAT};
 sorter.visitFrame(0, 3, null, 5, null);
 // Test no change
 sorter.visitFrame(0, 3, local, 5, null);
 assertArrayEquals(local, sorter.mv.localVariables);
 // Test change
 sorter.visitLocalVariable("test", "I", null, null, null, 1);
 sorter.visitLocalVariable("test2", "J", null, null, null, 2);
 sorter.visitLocalVariable("test3", "F", null, null, null, 3);
 sorter.visitFrame(0, 3, null, 5, null);
 assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.LONG, Opcodes.INTEGER}, sorter.mv.localVariables);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.getIntType();
 int local = sorter.newLocal(type);
 assertEquals(sorter.nextLocal, local);
 sorter.nextLocal++;
 Type type2 = Type.getFloatType();
 local = sorter.newLocal(type2);
 assertEquals(sorter.nextLocal, local);
 sorter.nextLocal++;
 }
}