package org.objectweb.asm.jip.commons.test;

import org.junit.Test;
import org.objectweb.asm.jip.*;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 }
 });
 }

 @Test
 public void testConstructor() {
 assertEquals(40, sorter.mapping.length);
 assertEquals(20, sorter.newLocals.length);
 }

 @Test
 public void testVisitVarInsn() {
 Type type;
 int var = 1;
 sorter.visitVarInsn(Opcodes.ILOAD, var);
 type = Type.INT_TYPE;
 assertEquals(var, sorter.remap(var, type));

 var = 2;
 sorter.visitVarInsn(Opcodes.ILOAD, var);
 assertEquals(var, sorter.remap(var, Type.INT_TYPE));

 var = 3;
 sorter.visitVarInsn(Opcodes.ALOAD, var);
 String desc = "Ljava/lang/String;";
 Type typeDesc = Type.getType(desc);
 assertEquals(var, sorter.remap(var, typeDesc));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithInvalidOpcode() {
 sorter.visitVarInsn(99, 1);
 }

 @Test
 public void testVisitIincInsn() {
 int var = 1;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 assertEquals(var, sorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 10;
 int maxLocals = 20;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, sorter.mv.getStackSize());
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "testName";
 String desc = "Ljava/lang/String;";
 String signature = "()V";
 Label start = new Label();
 Label end = new Label();
 int index = 1;
 Type type = Type.getType(desc);
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, type);
 assertNotSame(index, newIndex);
 }

 @Test
 public void testVisitFrame() {
 int nLocal = 5;
 Object[] local = new Object[nLocal];
 Object[] stack = new Object[1];
 Label start = new Label();
 Label end = new Label();
 int nStack = 1;
 Object[] framesLocal = new Object[nLocal];
 Object[] framesStack = new Object[1];
 int type = -1;
 sorter.visitFrame(type, nLocal, local, nStack, stack);
 assertArrayEquals(local, framesLocal);
 assertArrayEquals(stack, framesStack);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.getType("Ljava/lang/String;");
 int local = sorter.newLocal(type);
 assertEquals(type.getSort(), Type.getInternalName(sorter.mapping[local]).charAt(0) - '0');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewLocalWithInvalidType() {
 Type type = new Type("invalidType");
 sorter.newLocal(type);
 }

 @Test
 public void testSetLocalType() {
 final int local = 1;
 Type type = Type.getType("Ljava/lang/String;");
 sorter.setLocalType(local, type);
 assertEquals(type, sorter.newLocals[local]);
 }

 @Test
 public void testSetFrameLocal() {
 final int local = 1;
 Type type = Type.getType("Ljava/lang/String;");
 sorter.setFrameLocal(local, type);
 assertEquals(type, sorter.newLocals[local]);
 }

 @Test
 public void testRemap() {
 int var = 1;
 Type type = Type.INT_TYPE;
 int key = 2 * var + type.getSize() - 1;
 int value = 2;
 int size = sorter.mapping.length;
 sorter.mapping[key] = value;
 int expected = (var == 1) ? value : value - 1;
 assertEquals(expected, sorter.remap(var, type));
 }

 @Test
 public void testNewLocalMapping() {
 Type type = Type.getType("Ljava/lang/String;");
 int local = sorter.newLocalMapping(type);
 assertEquals(type.getSort(), Type.getInternalName(sorter.mapping[local]).charAt(0) - '0');
 }
}