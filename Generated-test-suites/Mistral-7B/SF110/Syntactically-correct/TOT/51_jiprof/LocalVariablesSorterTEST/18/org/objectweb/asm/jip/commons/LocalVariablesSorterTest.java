package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private MethodVisitor mv;
 private LocalVariablesSorter sorter;

 @Before
 public void setUp() {
 mv = new BytecodeBuilder(null).visit(Opcodes.ASM5, Opcodes.ACC_PUBLIC, null, null, null, null);
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 // Test integer variable
 sorter.visitVarInsn(Opcodes.ISTORE, 0);
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 // Test float variable
 sorter.visitVarInsn(Opcodes.FSTORE, 1);
 assertEquals(1, sorter.remap(1, Type.FLOAT_TYPE));
 // Test long variable
 sorter.visitVarInsn(Opcodes.LSTORE, 2);
 assertEquals(2, sorter.remap(2, Type.LONG_TYPE));
 // Test double variable
 sorter.visitVarInsn(Opcodes.DSTORE, 3);
 assertEquals(3, sorter.remap(3, Type.DOUBLE_TYPE));
 // Test object variable
 String className = "java.lang.String";
 sorter.visitVarInsn(Opcodes.ASTORE, 4);
 assertEquals(4, sorter.remap(4, Type.getType(className)));
 }

 @Test
 public void testVisitIincInsn() {
 // Test integer variable increment
 sorter.visitIincInsn(0, 1);
 sorter.visitVarInsn(Opcodes.ILOAD, 0);
 sorter.visitInsn(Opcodes.IADD, 1);
 sorter.visitVarInsn(Opcodes.ISTORE, 0);
 assertEquals(0, sorter.remap(0, Type.INT_TYPE));
 // Test long variable increment
 sorter.visitIincInsn(5, 2);
 sorter.visitVarInsn(Opcodes.LLOAD, 5);
 sorter.visitInsn(Opcodes.LADD_LONG, 2);
 sorter.visitVarInsn(Opcodes.LSTORE, 5);
 assertEquals(5, sorter.remap(5, Type.LONG_TYPE));
 }

 @Test
 public void testVisitLocalVariable() {
 // Test integer local variable
 sorter.visitLocalVariable("intVar", "I", null, null, null, 0);
 assertEquals("intVar", sorter.getLocalVariableName(0));
 // Test float local variable
 sorter.visitLocalVariable("floatVar", "F", null, null, null, 1);
 assertEquals("floatVar", sorter.getLocalVariableName(1));
 // Test long local variable
 sorter.visitLocalVariable("longVar", "J", null, null, null, 2);
 assertEquals("longVar", sorter.getLocalVariableName(2));
 // Test object local variable
 String className = "java.lang.String";
 sorter.visitLocalVariable("objVar", className, null, null, null, 3);
 assertEquals(className, sorter.getLocalVariableType(3).getDescriptor());
 }

 @Test
 public void testVisitFrame() {
 // Test empty frame
 sorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);
 // Test frame with one local variable
 Type intType = Type.INT_TYPE;
 sorter.visitFrame(Opcodes.FRAME_CODE, 1, new Object[]{intType}, 0, null);
 // Test frame with multiple local variables
 Type intType1 = Type.INT_TYPE;
 Type intType2 = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 sorter.visitFrame(Opcodes.FRAME_CODE, 3, new Object[]{intType1, intType2, floatType}, 0, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithExpandFrames() {
 // Test exception when EXPAND_FRAMES flag is not set
 sorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);
 sorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);
 }

 @Test
 public void testNewLocal() {
 // Test creating and mapping new local variables
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type stringType = Type.getType(className);

 int localVar1 = sorter.newLocal(intType);
 int localVar2 = sorter.newLocal(intType);
 int localVar3 = sorter.newLocal(floatType);
 int localVar4 = sorter.newLocal(longType);
 int localVar5 = sorter.newLocal(doubleType);
 int localVar6 = sorter.newLocal(stringType);

 assertEquals(localVar1, sorter.remap(0, intType));
 assertEquals(localVar2, sorter.remap(1, intType));
 assertEquals(localVar3, sorter.remap(2, floatType));
 assertEquals(localVar4, sorter.remap(3, longType));
 assertEquals(localVar5, sorter.remap(4, doubleType));
 assertEquals(localVar6, sorter.remap(5, stringType));
 }
}