package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.Opcodes;
import org.junit.Test;
import static org.junit.Assert.*;

import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM4) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 }
 });
 }

 @Test
 public void testVisitVarInsn() {
 int var1 = sorter.newLocal(Type.INT_TYPE);
 int var2 = sorter.newLocal(Type.FLOAT_TYPE);
 int var3 = sorter.newLocal(Type.LONG_TYPE);
 int var4 = sorter.newLocal(Type.DOUBLE_TYPE);
 int var5 = sorter.newLocal(Type.OBJECT_TYPE);

 sorter.visitVarInsn(Opcodes.ALOAD, var5);
 sorter.visitVarInsn(Opcodes.ISTORE, var1);
 sorter.visitVarInsn(Opcodes.FLOAD, var2);
 sorter.visitVarInsn(Opcodes.LDC, Opcodes.INTEGER_0);
 sorter.visitInsn(Opcodes.IADD);
 sorter.visitVarInsn(Opcodes.LSTORE, var3);
 sorter.visitVarInsn(Opcodes.DLOAD, var4);
 sorter.visitLdcInsn(1.0d);
 sorter.visitInsn(Opcodes.DMUL);
 sorter.visitVarInsn(Opcodes.ALOAD, var5);
 sorter.visitInsn(Opcodes.ARETURN);

 int expectedI = sorter.mapping[var1];
 int expectedF = sorter.mapping[var2];
 int expectedL = sorter.mapping[var3];
 int expectedD = sorter.mapping[var4];
 int expectedO = sorter.mapping[var5];

 assertEquals("Unexpected variable mapping for integer: ", var1, expectedI);
 assertEquals("Unexpected variable mapping for float: ", var2, expectedF);
 assertEquals("Unexpected variable mapping for long: ", var3, expectedL);
 assertEquals("Unexpected variable mapping for double: ", var4, expectedD);
 assertEquals("Unexpected variable mapping for Object: ", var5, expectedO);
 }

 @Test
 public void testVisitIincInsn() {
 int var1 = sorter.newLocal(Type.INT_TYPE);
 int var2 = sorter.newLocal(Type.INT_TYPE);

 sorter.visitLocalVariable("testVar1", "I", null, null, null, var1);
 sorter.visitLocalVariable("testVar2", "I", null, null, null, var2);
 sorter.visitIincInsn(var1, 1);
 sorter.visitIincInsn(var2, 2);

 int expectedVar1 = sorter.mapping[var1];
 int expectedVar2 = sorter.mapping[var2];

 assertEquals("Unexpected variable mapping for integer1: ", expectedVar1, var1 + 1);
 assertEquals("Unexpected variable mapping for integer2: ", expectedVar2, var2 + 2);
 }

 @Test
 public void testVisitMaxs() {
 int stackSize = 10;
 int localsSize = 15;

 sorter.visitMaxs(stackSize, localsSize);

 int expectedMaxLocals = sorter.nextLocal;
 int expectedMaxStack = stackSize;

 assertEquals("Unexpected maximum number of locals: ", expectedMaxLocals, localsSize);
 assertEquals("Unexpected maximum number of stack frames: ", expectedMaxStack, stackSize);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "testVar";
 String desc = "I";
 int var = sorter.newLocal(Type.INT_TYPE);

 sorter.visitLocalVariable(name, desc, null, null, null, var);

 int expectedVar = sorter.mapping[var];

 assertEquals("Unexpected variable mapping for local variable: ", expectedVar, var);
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[3];
 local[0] = Opcodes.INTEGER;
 local[1] = Opcodes.FLOAT;
 local[2] = Opcodes.LONG;

 int stackSize = 5;
 int localsSize = 10;

 sorter.visitFrame(-1, localsSize, local, stackSize, null);

 int expectedLocalsSize = sorter.nextLocal;

 assertEquals("Unexpected number of locals after visitFrame: ", expectedLocalsSize, localsSize);
 }
}