package org.objectweb.asm.jip.commons.test;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LocalVariablesSorterTest {

 private ClassReader classReader;
 private ClassWriter classWriter;
 private LocalVariablesSorter localVariablesSorter;

 @Before
 public void setup() {
 classReader = new ClassReader("LocalVariablesSorterTest$TestClass.class");
 classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "TestMethod", classWriter);
 }

 @Test
 public void testVisitVarInsn() {
 // Test int variable
 localVariablesSorter.visitVarInsn(Opcodes.ISTORE, 0);
 assertEquals(0, localVariablesSorter.remap(0, Type.INT_TYPE));

 // Test long variable
 localVariablesSorter.visitVarInsn(Opcodes.LSTORE, 1);
 assertEquals(1, localVariablesSorter.remap(1, Type.LONG_TYPE));

 // Test double variable
 localVariablesSorter.visitVarInsn(Opcodes.DSTORE, 2);
 assertEquals(2, localVariablesSorter.remap(2, Type.DOUBLE_TYPE));

 // Test float variable
 localVariablesSorter.visitVarInsn(Opcodes.FSTORE, 3);
 assertEquals(3, localVariablesSorter.remap(3, Type.FLOAT_TYPE));

 // Test object variable
 localVariablesSorter.visitVarInsn(Opcodes.ASTORE, 4);
 assertEquals(4, localVariablesSorter.remap(4, Type.getType("java.lang.String")));
 }

 @Test
 public void testVisitIincInsn() {
 // Test int variable increment
 localVariablesSorter.visitIincInsn(0, 1);
 localVariablesSorter.visitIincInsn(0, 2);
 assertArrayEquals(new int[]{3, 4}, localVariablesSorter.getNewLocals());

 // Test long variable increment
 localVariablesSorter.visitIincInsn(1, 1);
 localVariablesSorter.visitIincInsn(1, 2);
 assertArrayEquals(new int[]{3, 5}, localVariablesSorter.getNewLocals());
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithExpandedFrames() {
 localVariablesSorter.visitFrame(Opcodes.ABORT, 0, null, 0, null);
 }

 @Test
 public void testVisitFrame() {
 // Test no changes
 localVariablesSorter.visitFrame(Opcodes.ABORT, 5, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER}, 0, null);
 assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER}, localVariablesSorter.getNewLocals());

 // Test changes
 localVariablesSorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "TestMethod2", classWriter);
 localVariablesSorter.visitFrame(Opcodes.ABORT, 5, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER}, 0, null);
 localVariablesSorter.setFrameLocal(1, Opcodes.TOP);
 assertArrayEquals(new Object[]{Opcodes.INTEGER, Opcodes.TOP, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER}, localVariablesSorter.getNewLocals());
 }

 @Test
 public void testNewLocal() {
 // Test int variable
 int local = localVariablesSorter.newLocal(Type.INT_TYPE);
 assertEquals(5, localVariablesSorter.remap(local, Type.INT_TYPE));

 // Test long variable
 local = localVariablesSorter.newLocal(Type.LONG_TYPE);
 assertEquals(7, localVariablesSorter.remap(local, Type.LONG_TYPE));

 // Test float variable
 local = localVariablesSorter.newLocal(Type.FLOAT_TYPE);
 assertEquals(8, localVariablesSorter.remap(local, Type.FLOAT_TYPE));

 // Test double variable
 local = localVariablesSorter.newLocal(Type.DOUBLE_TYPE);
 assertEquals(9, localVariablesSorter.remap(local, Type.DOUBLE_TYPE));

 // Test object variable
 local = localVariablesSorter.newLocal(Type.getType("java.lang.String"));
 assertEquals(10, localVariablesSorter.remap(local, Type.getType("java.lang.String")));
 }

 private int[] getMapping() {
 return localVariablesSorter.mapping;
 }

 private Object[] getNewLocals() {
 return (Object[]) localVariablesSorter.newLocals.clone();
 }
}

class TestClass {
 public void TestMethod(String str) {
 int i = 1;
 long l = 1;
 float f = 1.0f;
 double d = 1.0;
 String s = "test";
 i = (int)newLocal(Type.INT_TYPE);
 l = (long)newLocal(Type.LONG_TYPE);
 f = (float)newLocal(Type.FLOAT_TYPE);
 d = (double)newLocal(Type.DOUBLE_TYPE);
 visitVarInsn(Opcodes.ISTORE, i);
 visitVarInsn(Opcodes.LSTORE, l);
 visitVarInsn(Opcodes.FSTORE, f);
 visitVarInsn(Opcodes.DSTORE, d);
 visitVarInsn(Opcodes.ASTORE, s);
 visitIincInsn(i, 1);
 visitIincInsn(l, -1);
 visitIincInsn(i, -2);
 visitFrame(Opcodes.ABORT, 5, new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, Opcodes.INTEGER}, 0, null);
 visitLocalVariable("testInt", "I", null, new Label(0), new Label(5), 0);
 visitLocalVariable("testFloat", "F", null, new Label(10), new Label(15), 1);
 visitLocalVariable("testLong", "J", null, new Label(20), new Label(25), 2);
 visitLocalVariable("testDouble", "D", null, new Label(30), new Label(35), 3);
 visitLocalVariable("testString", "Ljava/lang/String;", null, new Label(40), new Label(45), 4);
 }
}