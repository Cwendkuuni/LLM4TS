package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 mv = new BytecodeVisitorAdapter() {
 @Override
 public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
 return new LocalVariablesSorter(access, desc, this);
 }
 };
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testVisitVarInsn_int() {
 localVariablesSorter.visitVarInsn(21, 0);
 assertEquals(0, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitVarInsn_long() {
 localVariablesSorter.visitVarInsn(22, 0);
 localVariablesSorter.visitVarInsn(22, 1);
 assertEquals(3, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitVarInsn_float() {
 localVariablesSorter.visitVarInsn(23, 0);
 localVariablesSorter.visitVarInsn(23, 1);
 localVariablesSorter.visitVarInsn(23, 2);
 assertEquals(4, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitVarInsn_double() {
 localVariablesSorter.visitVarInsn(24, 0);
 localVariablesSorter.visitVarInsn(24, 1);
 localVariablesSorter.visitVarInsn(24, 2);
 localVariablesSorter.visitVarInsn(24, 3);
 assertEquals(8, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitVarInsn_object() {
 localVariablesSorter.visitVarInsn(25, "java/lang/String");
 localVariablesSorter.visitVarInsn(25, "java/lang/Integer");
 assertEquals(3, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitIincInsn() {
 localVariablesSorter.visitIincInsn(0, 1);
 assertEquals(1, localVariablesSorter.getNextLocal());
 }

 @Test
 public void testVisitFrame() {
 Type[] args = new Type[2];
 args[0] = Type.INT_TYPE;
 args[1] = Type.LONG_TYPE;
 localVariablesSorter.visitFrame(0, 2, new Object[2], 1, new Object[1]);
 Object[] oldLocals = localVariablesSorter.newLocals;
 int numberOfLocals = localVariablesSorter.getNextLocal();
 assertEquals(numberOfLocals, oldLocals.length);
 localVariablesSorter.visitFrame(0, numberOfLocals, oldLocals, 1, new Object[1]);
 }

 @Test
 public void testNewLocal() {
 localVariablesSorter.newLocal(Type.INT_TYPE);
 localVariablesSorter.newLocal(Type.FLOAT_TYPE);
 localVariablesSorter.newLocal(Type.LONG_TYPE);
 localVariablesSorter.newLocal(Type.DOUBLE_TYPE);
 localVariablesSorter.newLocal(Type.getType("java/lang/String"));
 localVariablesSorter.newLocal(Type.getType("java/lang/Integer"));
 assertEquals(8, localVariablesSorter.getNextLocal());
 }
}