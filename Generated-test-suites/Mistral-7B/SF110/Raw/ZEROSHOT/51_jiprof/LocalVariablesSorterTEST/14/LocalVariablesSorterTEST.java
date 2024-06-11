package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 mv = new MethodVisitor(Opcodes.ASM5, new ByteArrayOutputStream()) {
 public void visitVarInsn(int opcode, int var) {}
 public void visitIincInsn(int var, int increment) {}
 public void visitMaxs(int maxStack, int maxLocals) {}
 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {}
 public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {}
 };
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", mv);
 }

 @Test
 public void testVisitVarInsn() {
 sorter.visitVarInsn(IINC, 1);
 assertEquals(1, sorter.remap(1, Type.INT_TYPE));
 sorter.visitVarInsn(ALOAD, 1);
 assertEquals(1, sorter.remap(1, Type.getType("Ljava/lang/Object;")));
 sorter.visitVarInsn(ILOAD, 1);
 assertEquals(1, sorter.remap(1, Type.INT_TYPE));
 sorter.visitVarInsn(LLOAD, 1);
 assertEquals(1, sorter.remap(1, Type.LONG_TYPE));
 sorter.visitVarInsn(FLOAD, 1);
 assertEquals(1, sorter.remap(1, Type.FLOAT_TYPE));
 sorter.visitVarInsn(DLOAD, 1);
 assertEquals(1, sorter.remap(1, Type.DOUBLE_TYPE));
 sorter.visitVarInsn(ASTORE, 1);
 assertEquals(1, sorter.remap(1, Type.getType("Ljava/lang/Object;")));
 }

 @Test
 public void testVisitIincInsn() {
 sorter.visitIincInsn(1, 1);
 assertEquals(1, sorter.remap(1, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxLocals = 5;
 sorter.visitMaxs(5, maxLocals);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "a";
 String desc = "I";
 Label start = new Label();
 Label end = new Label();
 int index = 1;
 sorter.visitLocalVariable(name, desc, null, start, end, index);
 assertEquals(index, sorter.remap(index, Type.INT_TYPE));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithoutAccept() {
 sorter.visitFrame(Opcodes.FRAME_CODE, 0, null, 0, null);
 }

 @Test
 public void testVisitFrame() {
 Object[] locals = {Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.LONG, null};
 int nLocal = locals.length;
 int nStack = 0;
 Object[] stack = {};
 sorter.visitFrame(Opcodes.FRAME_CODE, nLocal, locals, nStack, stack);
 Object[] newLocals = sorter.newLocals;
 assertEquals(nLocal, newLocals.length);
 for (int i = 0; i < nLocal; ++i) {
 Object local = locals[i];
 assertEquals(i, sorter.remap(i, Type.getType(local.getClass().getName())));
 }
 }

 @Test
 public void testNewLocal() {
 Type intType = Type.INT_TYPE;
 Type floatType = Type.FLOAT_TYPE;
 Type longType = Type.LONG_TYPE;
 Type doubleType = Type.DOUBLE_TYPE;
 Type objType = Type.getType("Ljava/lang/Object;");
 int local = sorter.newLocal(intType);
 assertEquals(local, sorter.remap(local, intType));
 local = sorter.newLocal(floatType);
 assertEquals(local, sorter.remap(local, floatType));local = sorter.newLocal(longType);
 assertEquals(local, sorter.remap(local, longType));
 local = sorter.newLocal(doubleType);
 assertEquals(local, sorter.remap(local, doubleType));
 local = sorter.newLocal(objType);
 assertEquals(local, sorter.remap(local, objType));
 }
}