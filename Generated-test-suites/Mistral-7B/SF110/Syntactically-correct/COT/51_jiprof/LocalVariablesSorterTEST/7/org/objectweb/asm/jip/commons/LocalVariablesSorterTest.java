package org.objectweb.asm.jip.commons;

import static org.junit.Assert.*;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.Arrays;

public class LocalVariablesSorterTest {

 LocalVariablesSorter sorter;

 @Before
 public void setup() {
 sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", new MethodVisitor(Opcodes.ASM5) {
 @Override
 public void visitCode() {}
 });
 }

 @Test
 public void testConstructor() {
 assertEquals(40, sorter.mapping.length);
 assertEquals(20, sorter.newLocals.length);
 }

 @Test
 public void testVisitVarInsn() {
 int var = 0;
 Type type = Type.INT_TYPE;
 sorter.visitVarInsn(Opcodes.ISTORE, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsn_long() {
 int var = 0;
 Type type = Type.LONG_TYPE;
 sorter.visitVarInsn(Opcodes.LSTORE, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsn_float() {
 int var = 0;
 Type type = Type.FLOAT_TYPE;
 sorter.visitVarInsn(Opcodes.FSTORE, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsn_double() {
 int var = 0;
 Type type = Type.DOUBLE_TYPE;
 sorter.visitVarInsn(Opcodes.DSTORE, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitVarInsn_object() {
 int var = 0;
 Type type = Type.getType("java/lang/String");
 sorter.visitVarInsn(Opcodes.ASTORE, var);
 assertEquals(var, sorter.remap(var, type));
 }

 @Test
 public void testVisitIincInsn() {
 int var = 0;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 assertEquals(var, sorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 Type returnType = Type.VOID_TYPE;
 int maxStack = 1, maxLocals = 2;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = null;
 Label start = new Label(), end = new Label();
 int index = 0;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, Type.getType(desc));
 Type type = Type.getType(desc);
 assertEquals(newIndex, sorter.remap(index, type));
 }

 @Test
 public void testVisitFrame() {
 Object[] local = {Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.INTEGER};
 Object[] stack = {};
 int nLocal = local.length, nStack = stack.length;
 sorter.visitFrame(Opcodes.FRAME_TYPE, nLocal, local, nStack, stack);
 assertEquals(nLocal, sorter.nextLocal);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.INT_TYPE;
 int local = sorter.newLocal(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test
 public void testNewLocal_object() {
 Type type = Type.getType("java/lang/String");
 int local = sorter.newLocal(type);
 assertEquals(local, sorter.remap(local, type));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrame_withType() {
 sorter.visitFrame(Opcodes.FRAME_TYPE, 0, new Object[]{}, 0, null);
 }

 @Test
 public void testSetFrameLocal() {
 Object[] newLocals = new Object[5];
 int local = 0;
 sorter.setFrameLocal(local, Opcodes.INTEGER);
 assertEquals(local, sorter.remap(local, Type.INT_TYPE));
 }

 @Test
 public void testSetFrameLocal_object() {
 Object[] newLocals = new Object[5];
 int local = 0;
 sorter.setFrameLocal(local, Type.getType("java/lang/String"));
 assertEquals(local, sorter.remap(local, Type.getType("java/lang/String")));
 }

 @Test
 public void testSetFrameLocal_null() {
 Object[] newLocals = new Object[5];
 int local = 0;
 sorter.setFrameLocal(local, null);
 assertEquals(local, sorter.remap(local, Type.getObjectType("java/lang/Object")));
 }

 @Test
 public void testRemap_validIndex() {
 int index = 0;
 Type type = Type.INT_TYPE;
 int mappedIndex = sorter.remap(index, type);
 assertEquals(index, mappedIndex);
 }

 @Test
 public void testRemap_invalidIndex_lessThanFirstLocal() {
 int index = -1;
 Type type = Type.INT_TYPE;
 int mappedIndex = sorter.remap(index, type);
 assertNotEquals(index, mappedIndex);
 }

 @Test
 public void testRemap_invalidIndex_greaterThanFirstLocal() {
 int index = 10;
 Type type = Type.INT_TYPE;
 int mappedIndex = sorter.remap(index, type);
 assertNotEquals(index, mappedIndex);
 }

 @Test
 public void testNewLocalMapping() {
 Type type = Type.INT_TYPE;
 int newLocal = sorter.newLocalMapping(type);
 assertEquals(sorter.nextLocal, newLocal);
 }

 @Test
 public void testNewLocalMapping_object() {
 Type type = Type.getType("java/lang/String");
 int newLocal = sorter.newLocalMapping(type);
 assertEquals(sorter.nextLocal + type.getSize(), newLocal);
 }
}