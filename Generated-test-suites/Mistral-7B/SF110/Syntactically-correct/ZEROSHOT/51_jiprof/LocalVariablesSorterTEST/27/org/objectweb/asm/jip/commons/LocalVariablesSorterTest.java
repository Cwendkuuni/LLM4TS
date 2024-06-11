package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @BeforeEach
 public void setUp() {
 mv = new NoOpMethodVisitor(0, null, null);
 sorter = new LocalVariablesSorter(0, "()V", mv);
 }

 @Test
 public void testConstructor() {
 assertEquals(1, sorter.nextLocal);
 assertEquals(0, sorter.firstLocal);
 assertEquals(20, sorter.newLocals.length);
 assertNull(sorter.newLocals[0]);
 assertArrayEquals(new int[40], sorter.mapping);
 }

 @Test
 public void testVisitVarInsnWithIntType() {
 int var = 3;
 sorter.visitVarInsn(21, var);
 int newVar = sorter.remap(var, Type.INT_TYPE);
 assertEquals(var, newVar);
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 int var = 3;
 sorter.visitVarInsn(22, var);
 int newVar = sorter.remap(var, Type.LONG_TYPE);
 assertEquals(var + 2, newVar);
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 int var = 3;
 sorter.visitVarInsn(23, var);
 int newVar = sorter.remap(var, Type.FLOAT_TYPE);
 assertEquals(var + 1, newVar);
 }

 @Test
 public void testVisitVarInsnWithDoubleType() {
 int var = 3;
 sorter.visitVarInsn(24, var);
 int newVar = sorter.remap(var, Type.DOUBLE_TYPE);
 assertEquals(var + 3, newVar);
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 String objectType = "java.lang.String";
 int var = 3;
 sorter.visitVarInsn(25, var);
 int newVar = sorter.remap(var, Type.getType(objectType));
 assertNotEquals(var, newVar);
 }

 @Test
 public void testVisitVarInsnWithNullObjectType() {
 int var = 3;
 sorter.visitVarInsn(25, var);
 int newVar = sorter.remap(var, Type.getObjectType("java.lang.Object"));
 assertEquals(var, newVar);
 }

 @Test
 public void testVisitIincInsn() {
 int var = 3;
 int increment = 5;
 sorter.visitIincInsn(var, increment);
 assertEquals(var + 1, sorter.remap(var, Type.INT_TYPE));
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 10;
 int maxLocals = 20;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "";
 Label start = new Label();
 Label end = new Label();
 int index = 3;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, Type.getType(desc));
 assertEquals(index, newIndex);
 }

 @Test
 public void testVisitFrame() {
 int numberOfLocals = 5;
 Object[] locals = new Object[numberOfLocals];
 for (int i = 0; i < numberOfLocals; i++) {
 locals[i] = i + 1;
 }
 sorter.visitFrame(0, numberOfLocals, locals, 10, null);
 Object[] newLocals = sorter.newLocals;
 assertEquals(numberOfLocals, newLocals.length);
 for (int i = 0; i < numberOfLocals; i++) {
 assertEquals(i + 1, newLocals[i]);
 }
 }

 @Test
 public void testNewLocal() {
 Type type = Type.INT_TYPE;
 int newLocal = sorter.newLocal(type);
 assertEquals(sorter.nextLocal - type.getSize(), newLocal);
 }

 @Test
 public void testSetFrameLocal() {
 int local = 3;
 String objectType = "java.lang.String";
 sorter.setFrameLocal(local, Type.getObjectType(objectType));
 assertEquals(objectType, sorter.newLocals[local]);
 }

 @Test
 public void testRemap() {
 int var = 3;
 int size = 1;
 Type type = Type.INT_TYPE;
 int remappedVar = sorter.remap(var, type);
 assertEquals(var + size, remappedVar);
 }

 @Test
 public void testNewLocalMapping() {
 Type type = Type.INT_TYPE;
 int newLocal = sorter.newLocalMapping(type);
 assertEquals(sorter.nextLocal - type.getSize(), newLocal);
 }
}