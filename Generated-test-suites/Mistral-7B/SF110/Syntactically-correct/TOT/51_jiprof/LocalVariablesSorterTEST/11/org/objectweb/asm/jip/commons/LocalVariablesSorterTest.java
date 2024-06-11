package org.objectweb.asm.jip.commons;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private MethodAdapter methodAdapter;
 private LocalVariablesSorter localVariablesSorter;

 @Before
 public void setUp() {
 methodAdapter = new MethodVisitor(Opcodes.ASM5, null, null, null) {
 // Empty visit methods
 };
 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", methodAdapter);
 }

 @Test
 public void testVisitVarInsn() {
 // Test int variable
 localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 1);
 assertEquals(1, localVariablesSorter.remap(1, Type.INT_TYPE));

 // Test long variable
 localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 2);
 assertEquals(3, localVariablesSorter.remap(2, Type.LONG_TYPE));

 // Test float variable
 localVariablesSorter.visitVarInsn(Opcodes.FSTORE, 3);
 assertEquals(5, localVariablesSorter.remap(3, Type.FLOAT_TYPE));

 // Test double variable
 localVariablesSorter.visitVarInsn(Opcodes.DSTORE, 4);
 assertEquals(7, localVariablesSorter.remap(4, Type.DOUBLE_TYPE));

 // Test object variable
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 5);
 assertEquals(9, localVariablesSorter.remap(5, Type.getType("java/lang/String")));
 }

 @Test
 public void testVisitIincInsn() {
 // Test int increment
 localVariablesSorter.visitIincInsn(1, 1);
 assertEquals(1, localVariablesSorter.remap(1, Type.INT_TYPE));

 // Test long increment
 localVariablesSorter.visitIincInsn(2, 2);
 assertEquals(3, localVariablesSorter.remap(2, Type.LONG_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 1;
 int maxLocals = 10;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, localVariablesSorter.mv.maxStack);
 assertEquals(maxLocals, localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 // Test int local variable
 localVariablesSorter.visitLocalVariable("i", "I", null, null, null, 1);
 assertEquals("i", localVariablesSorter.mv.localVariables[0].name);
 assertEquals("I", localVariablesSorter.mv.localVariables[0].desc);
 assertEquals(1, localVariablesSorter.remap(1, Type.INT_TYPE));

 // Test long local variable
 localVariablesSorter.visitLocalVariable("l", "J", null, null, null, 2);
 assertEquals("l", localVariablesSorter.mv.localVariables[1].name);
 assertEquals("J", localVariablesSorter.mv.localVariables[1].desc);
 assertEquals(3, localVariablesSorter.remap(2, Type.LONG_TYPE));

 // Test float local variable
 localVariablesSorter.visitLocalVariable("f", "F", null, null, null, 3);
 assertEquals("f", localVariablesSorter.mv.localVariables[2].name);
 assertEquals("F", localVariablesSorter.mv.localVariables[2].desc);
 assertEquals(5, localVariablesSorter.remap(3, Type.FLOAT_TYPE));

 // Test double local variable
 localVariablesSorter.visitLocalVariable("d", "D", null, null, null, 4);
 assertEquals("d", localVariablesSorter.mv.localVariables[3].name);
 assertEquals("D", localVariablesSorter.mv.localVariables[3].desc);
 assertEquals(7, localVariablesSorter.remap(4, Type.DOUBLE_TYPE));

 // Test object local variable
 localVariablesSorter.visitLocalVariable("str", "Ljava/lang/String;", null, null, null, 5);
 assertEquals("str", localVariablesSorter.mv.localVariables[4].name);
 assertEquals("Ljava/lang/String;", localVariablesSorter.mv.localVariables[4].desc);
 assertEquals(9, localVariablesSorter.remap(5, Type.getType("java/lang/String")));
 }

 @Test
 public void testVisitFrame() {
 // Test empty frame
 localVariablesSorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);
 assertEquals(Opcodes.FRAME_CODE, localVariablesSorter.mv.frame.opcode);
 assertTrue(localVariablesSorter.mv.frame.abs_offset == 0);
 assertTrue(localVariablesSorter.mv.frame.abs_offset_next == 0);

 // Test frame with locals
 Object[] localObjects = {Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT, Opcodes.DOUBLE, new Type("java/lang/String")};
 localVariablesSorter.visitFrame(Opcodes.FRAME_LOCALS, 5, localObjects, 0, null);
 assertEquals(5, localVariablesSorter.newLocals.length);
 assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[0]);
 assertEquals(Opcodes.LONG, localVariablesSorter.newLocals[1]);
 assertEquals(Opcodes.FLOAT, localVariablesSorter.newLocals[2]);
 assertEquals(Opcodes.DOUBLE, localVariablesSorter.newLocals[3]);
 assertEquals("java/lang/String", localVariablesSorter.newLocals[4].toString());
 }
}