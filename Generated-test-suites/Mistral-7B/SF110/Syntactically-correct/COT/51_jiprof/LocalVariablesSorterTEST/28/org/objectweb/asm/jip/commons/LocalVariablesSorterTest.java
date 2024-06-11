package org.objectweb.asm.jip.commons;

import static org.junit.Assert.*;

import org.objectweb.asm.jip.*;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @Before
 public void setUp() {
 Class<?> clazz = LocalVariablesSorter.class;
 try {
 Constructor<?> constructor = clazz.getDeclaredConstructor(AccessFlag.class, String.class, MethodVisitor.class);
 constructor.setAccessible(true);
 sorter = (LocalVariablesSorter) constructor.newInstance(Opcodes.ACC_PUBLIC, "test", mv -> {});
 mv = MockMethodVisitor.createMock();
 } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testConstructor() {
 LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "test", mv);
 assertNotNull(sorter.mapping);
 assertNotNull(sorter.newLocals);
 assertEquals(0, sorter.nextLocal);
 assertEquals(1, sorter.firstLocal);
 }

 @Test
 public void testVisitVarInsn() {
 int var = 0;
 Type type = Type.INT_TYPE;
 sorter.visitVarInsn(Opcodes.ILOAD, var);
 int remappedVar = sorter.remap(var, type);
 assertTrue(remappedVar >= 0);
 assertEquals(var, remappedVar);
 }

 @Test
 public void testVisitIincInsn() {
 int var = 0;
 int increment = 1;
 sorter.visitIincInsn(var, increment);
 int remappedVar = sorter.remap(var, Type.INT_TYPE);
 assertEquals(var, remappedVar);
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 1;
 int maxLocals = 1;
 sorter.visitMaxs(maxStack, maxLocals);
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "();V";
 Label start = new Label();
 Label end = new Label();
 int index = 1;
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, Type.getType(desc));
 assertEquals(index, newIndex);
 }

 @Test
 public void testVisitFrame() {
 Object[] local = new Object[1];
 local[0] = Opcodes.INTEGER;
 sorter.visitFrame(Opcodes.FRAME_NONE, 1, local, 1, new Object[1]);
 int number = sorter.newLocals.length;
 Object[] newLocals = sorter.newLocals;
 assertArrayEquals(new Object[]{Opcodes.TOP}, newLocals);
 assertEquals(number, sorter.mv.visitFrame(Opcodes.FRAME_NONE, number, sorter.newLocals, 1, new Object[1]).localVariables.length);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.INT_TYPE;
 int newLocal = sorter.newLocal(type);
 assertEquals(newLocal, sorter.nextLocal - type.getSize());
 assertEquals(Type.INT_TYPE, sorter.mapping[newLocal]);
 }

 @Test
 public void testSetLocalType() {
 int local = 0;
 Type type = Type.INT_TYPE;
 sorter.setLocalType(local, type);
 assertEquals(type, sorter.mapping[local]);
 }

 @Test
 public void testSetFrameLocal() {
 int local = 0;
 Object type = Opcodes.INTEGER;
 sorter.setFrameLocal(local, type);
 assertEquals(type, sorter.newLocals[local]);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithExpandedFrames() {
 sorter.visitFrame(Opcodes.FRAME_same, 1, new Object[1], 1, new Object[1]);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithChanged() {
 sorter.visitFrame(Opcodes.FRAME_same, 1, new Object[1], 1, new Object[1]);
 sorter.setFrameLocal(0, Opcodes.LONG);
 }
}