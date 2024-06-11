package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 LocalVariablesSorter localVariablesSorter;
 MethodVisitor mockedMethodVisitor;

 @Before
 public void setUp() {
 mockedMethodVisitor = mock(MethodVisitor.class);
 localVariablesSorter = new LocalVariablesSorter(0, "()V", mockedMethodVisitor);
 }

 @Test
 public void testConstructor() {
 assertEquals(1, localVariablesSorter.nextLocal);
 assertNotNull(localVariablesSorter.mapping);
 assertNotNull(localVariablesSorter.newLocals);
 assertEquals(0, localVariablesSorter.firstLocal);
 assertFalse(localVariablesSorter.changed);
 }

 @Test
 public void testVisitVarInsnWithIntegerType() {
 Type intType = Type.INT_TYPE;
 int local = localVariablesSorter.remap(0, intType);
 localVariablesSorter.visitVarInsn(21, local);
 verify(mockedMethodVisitor, times(1)).visitVarInsn(21, local);
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 Type floatType = Type.FLOAT_TYPE;
 int local = localVariablesSorter.remap(0, floatType);
 localVariablesSorter.visitVarInsn(23, local);
 verify(mockedMethodVisitor, times(1)).visitVarInsn(23, local);
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 Type longType = Type.LONG_TYPE;
 int local = localVariablesSorter.remap(0, longType);
 localVariablesSorter.visitVarInsn(22, local);
 verify(mockedMethodVisitor, times(1)).visitVarInsn(22, local);
 }

 @Test
 public void testVisitVarInsnWithDoubleType() {
 Type doubleType = Type.DOUBLE_TYPE;
 int local = localVariablesSorter.remap(0, doubleType);
 localVariablesSorter.visitVarInsn(24, local);
 verify(mockedMethodVisitor, times(1)).visitVarInsn(24, local);
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 int local = localVariablesSorter.remap(0, objectType);
 localVariablesSorter.visitVarInsn(25, local); // opcode 25 for Object type is not defined
 // In reality, this would result in a MethodInjectionException, but for testing purposes, let's just verify it was not called
 verify(mockedMethodVisitor, never()).visitVarInsn(25, local);
 }

 @Test
 public void testVisitIincInsn() {
 int var = 0;
 int increment = 1;
 localVariablesSorter.visitIincInsn(var, increment);
 verify(mockedMethodVisitor, times(1)).visitIincInsn(0, increment);
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 1;
 int maxLocals = 2;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 verify(mockedMethodVisitor, times(1)).visitMaxs(maxStack, localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "";
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;
 Type type = Type.getType(desc);
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = localVariablesSorter.remap(index, type);
 verify(mockedMethodVisitor, times(1)).visitLocalVariable(name, desc, signature, start, end, newIndex);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithExpandFramesFlagOff() {
 localVariablesSorter.visitFrame(-1, 1, new Object[]{Type.LONG_TYPE}, 2, new Object[]{Type.LONG_TYPE, Type.LONG_TYPE});
 // An IllegalStateException should be thrown if expand_frames flag is not set
 fail("An IllegalStateException should be thrown.");
 }

 @Test
 public void testVisitFrameWithNoChangedFlag() {
 Object[] oldLocals = new Object[]{Type.LONG_TYPE};
 Object[] newLocals = new Object[]{Type.LONG_TYPE, Type.LONG_TYPE};
 localVariablesSorter.visitFrame(-1, 1, oldLocals, 2, newLocals);
 verify(mockedMethodVisitor, times(1)).visitFrame(-1, 1, newLocals, 2, newLocals);
 }

 @Test
 public void testVisitFrameWithChangedFlag() {
 Object[] oldLocals = new Object[]{Type.LONG_TYPE};
 Object[] newLocals = new Object[]{Type.LONG_TYPE, Type.LONG_TYPE};
 localVariablesSorter.changed = true;
 localVariablesSorter.visitFrame(-1, 1, oldLocals, 2, newLocals);
 // The old locals array should be restored after the visitFrame call
 assertArrayEquals(oldLocals, localVariablesSorter.newLocals);
 verify(mockedMethodVisitor, times(1)).visitFrame(-1, 2, newLocals, 2, newLocals);
 }

 @Test
 public void testNewLocalWithIntegerType() {
 Type intType = Type.INT_TYPE;
 int local = localVariablesSorter.newLocal(intType);
 assertEquals(local, localVariablesSorter.remap(local, intType));
 }

 @Test
 public void testNewLocalWithFloatType() {
 Type floatType = Type.FLOAT_TYPE;
 int local = localVariablesSorter.newLocal(floatType);
 assertEquals(local, localVariablesSorter.remap(local, floatType));
 }

 @Test
 public void testNewLocalWithLongType() {
 Type longType = Type.LONG_TYPE;
 int local = localVariablesSorter.newLocal(longType);
 assertEquals(local, localVariablesSorter.remap(local, longType));
 }

 @Test
 public void testNewLocalWithDoubleType() {
 Type doubleType = Type.DOUBLE_TYPE;
 int local = localVariablesSorter.newLocal(doubleType);
 assertEquals(local, localVariablesSorter.remap(local, doubleType));
 }

 @Test
 public void testNewLocalWithObjectType() {
 Type objectType = Type.getType("java.lang.String");
 int local = localVariablesSorter.newLocal(objectType);
 assertEquals(local, localVariablesSorter.remap(local, objectType));
 }

 @Test
 public void testSetLocalType() {
 Type intType = Type.INT_TYPE;
 int local = localVariablesSorter.newLocal(intType);
 localVariablesSorter.setLocalType(local, intType);
 assertEquals(intType, localVariablesSorter.getLocalType(local));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLocalTypeWithInvalidLocal() {
 Type intType = Type.INT_TYPE;
 int local = 1234;
 localVariablesSorter.setLocalType(local, intType);
 // An IllegalArgumentException should be thrown for an invalid local index
 fail("An IllegalArgumentException should be thrown.");
 }

 @Test
 public void testSetFrameLocal() {
 Type longType = Type.LONG_TYPE;
 int local = localVariablesSorter.newLocal(longType);
 localVariablesSorter.setFrameLocal(local, longType);
 assertEquals(longType, localVariablesSorter.getFrameLocal(local));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetFrameLocalWithInvalidLocalIndex() {
 Type longType = Type.LONG_TYPE;
 int local = localVariablesSorter.newLocal(longType);
 int invalidIndex = local + 1;
 localVariablesSorter.setFrameLocal(invalidIndex, longType);
 // An IndexOutOfBoundsException should be thrown for an invalid local index
 fail("An IndexOutOfBoundsException should be thrown.");
 }

 @Test
 public void testRemapWithInvalidVar() {
 int invalidVar = -1;
 Type intType = Type.INT_TYPE;
 int local = localVariablesSorter.newLocal(intType);
 int mappedLocal = localVariablesSorter.remap(invalidVar, intType);
 assertEquals(local, mappedLocal);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemapWithInvalidType() {
 int var = 0;
 Type nonIntegerType = Type.getType("Ljava/lang/String;");
 int local = localVariablesSorter.remap(var, nonIntegerType);
 // An IllegalArgumentException should be thrown for an invalid type
 fail("An IllegalArgumentException should be thrown.");
 }

}