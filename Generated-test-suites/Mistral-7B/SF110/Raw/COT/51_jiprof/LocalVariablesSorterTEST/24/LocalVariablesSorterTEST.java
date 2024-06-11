package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.jip.ClassLoaderForTests;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mvMock;

 @Before
 public void setup() {
 mvMock = mock(MethodVisitor.class);
 sorter = new LocalVariablesSorter(0, "", mvMock);
 }

 @Test
 public void testConstructor() {
 // Check that the constructor sets the nextLocal variable correctly
 int expectedNextLocal = 1;
 assertEquals(expectedNextLocal, sorter.nextLocal);
 }

 @Test
 public void testVisitVarInsnWithIntType() {
 // Given
 int var = 0;
 Type intType = Type.getIntType();

 // When
 sorter.visitVarInsn(Opcodes.ILOAD, var);
 int expectedRemappedVar = var;

 // Then
 assertEquals(expectedRemappedVar, sorter.remap(var, intType));
 }

 @Test
 public void testVisitVarInsnWithLongType() {
 // Given
 int var = 0;
 Type longType = Type.getLongType();

 // When
 sorter.visitVarInsn(Opcodes.LLOAD, var);
 int expectedRemappedVar = var * 2 + longType.getSize();

 // Then
 assertEquals(expectedRemappedVar, sorter.remap(var, longType));
 }

 @Test
 public void testVisitVarInsnWithFloatType() {
 // Given
 int var = 0;
 Type floatType = Type.getFloatType();

 // When
 sorter.visitVarInsn(Opcodes.FLOAD, var);
 int expectedRemappedVar = var * 2 + floatType.getSize();

 // Then
 assertEquals(expectedRemappedVar, sorter.remap(var, floatType));
 }

 @Test
 public void testVisitVarInsnWithDoubleType() {
 // Given
 int var = 0;
 Type doubleType = Type.getDoubleType();

 // When
 sorter.visitVarInsn(Opcodes.DLOAD, var);
 int expectedRemappedVar = var * 2 + doubleType.getSize();

 // Then
 assertEquals(expectedRemappedVar, sorter.remap(var, doubleType));
 }

 @Test
 public void testVisitVarInsnWithObjectType() {
 // Given
 int var = 0;
 Type objectType = Type.getObjectType("java/lang/String");

 // When
 sorter.visitVarInsn(Opcodes.ALOAD, var);
 int expectedRemappedVar = var;

 // Then
 assertEquals(expectedRemappedVar, sorter.remap(var, objectType));
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitVarInsnWithInvalidOpcode() {
 // Given
 int var = 0;
 int opcode = Opcodes.IRETURN;
 Type type = Type.getIntType();

 // When
 sorter.visitVarInsn(opcode, var);

 // Then - an exception should be thrown
 }

 @Test
 public void testVisitIincInsn() {
 // Given
 int var = 0;
 int increment = 1;

 // When
 sorter.visitIincInsn(var, increment);

 // Then
 assertEquals(var + increment, sorter.remap(var, Type.getIntType()));
 }

 @Test
 public void testVisitMaxs() {
 // Given
 int maxStack = 1;
 int maxLocals = 2;

 // When
 sorter.visitMaxs(maxStack, maxLocals);

 // Then
 assertEquals(maxStack, sorter.mv.maxStack);
 assertEquals(maxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariableWithNullName() {
 // Given
 String name = null;
 String desc = "I";
 String signature = null;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;

 // When
 sorter.visitLocalVariable(name, desc, signature, start, end, index);

 // Then - no exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVisitLocalVariableWithEmptyName() {
 // Given
 String name = "";
 String desc = "I";
 String signature = null;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;

 // When
 sorter.visitLocalVariable(name, desc, signature, start, end, index);

 // Then - an exception should be thrown
 }

 @Test
 public void testVisitLocalVariableWithNullDesc() {
 // Given
 String name = "name";
 String desc = null;
 String signature = null;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;

 // When
 sorter.visitLocalVariable(name, desc, signature, start, end, index);

 // Then - no exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVisitLocalVariableWithInvalidDesc() {
 // Given
 String name = "name";
 String desc = "I;";
 String signature = null;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;

 // When
 sorter.visitLocalVariable(name, desc, signature, start, end, index);

 // Then - an exception should be thrown
 }

 @Test
 public void testVisitLocalVariableWithValidParameters() {
 // Given
 String name = "name";
 String desc = "I";
 String signature = null;
 Label start = mock(Label.class);
 Label end = mock(Label.class);
 int index = 0;

 // When
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int expectedRemappedIndex = index;

 // Then
 assertEquals(expectedRemappedIndex, sorter.remap(index, Type.getType(desc)));
 }

 @Test
 public void testVisitFrameWithNoChanges() {
 // Given
 int type = Opcodes.FRAME_NOP;
 int nLocal = 1;
 Object[] local = new Object[nLocal];
 int nStack = 1;
 Object[] stack = new Object[nStack];

 // When
 sorter.visitFrame(type, nLocal, local, nStack, stack);

 // Then
 assertEquals(type, sorter.mv.visitFrame(type, nLocal, local, nStack, stack).getFrameType());
 }

 @Test
 public void testVisitFrameWithChanges() {
 // Given
 int type = Opcodes.FRAME_same;
 int nLocal = 2;
 Object[] local = new Object[nLocal];
 Object[] oldLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER};
 System.arraycopy(oldLocals, 0, local, 0, nLocal);
 int nStack = 1;
 Object[] stack = new Object[nStack];
 Object[] newLocals = new Object[nLocal + 2];
 System.arraycopy(local, 0, newLocals, 0, nLocal);
 newLocals[nLocal] = Opcodes.LONG;
 newLocals[nLocal + 1] = Opcodes.TOP;

 // When
 sorter.changed = true;
 sorter.visitFrame(type, nLocal, local, nStack, stack);

 // Then
 assertEquals(type, sorter.mv.visitFrame(type, newLocals.length, newLocals, nStack, stack).getFrameType());
 }

 @Test
 public void testNewLocalWithIntType() {
 // Given
 Type intType = Type.getIntType();

 // When
 int newLocal = sorter.newLocal(intType);

 // Then
 assertEquals(newLocal, sorter.remap(newLocal, intType));
 }

 @Test
 public void testNewLocalWithLongType() {
 // Given
 Type longType = Type.getLongType();

 // When
 int newLocal = sorter.newLocal(longType);

 // Then
 assertEquals(newLocal * 2 + longType.getSize(), sorter.remap(newLocal, longType));
 }

 @Test
 public void testNewLocalWithFloatType() {
 // Given
 Type floatType = Type.getFloatType();

 // When
 int newLocal = sorter.newLocal(floatType);

 // Then
 assertEquals(newLocal * 2 + floatType.getSize(), sorter.remap(newLocal, floatType));
 }

 @Test
 public void testNewLocalWithDoubleType() {
 // Given
 Type doubleType = Type.getDoubleType();

 // When
 int newLocal = sorter.newLocal(doubleType);

 // Then
 assertEquals(newLocal * 2 + doubleType.getSize(), sorter.remap(newLocal, doubleType));
 }

 @Test
 public void testNewLocalWithObjectType() {
 // Given
 Type objectType = Type.getObjectType("java/lang/String");

 // When
 int newLocal = sorter.newLocal(objectType);

 // Then
 assertEquals(newLocal, sorter.remap(newLocal, objectType));
 }

 @Test
 public void testSetFrameLocalWithNullType() {
 // Given
 int local = 0;
 Object type = null;

 // When
 sorter.setFrameLocal(local, type);

 // Then - no exception should be thrown
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSetFrameLocalWithInvalidLocal() {
 // Given
 int local = sorter.newLocalMapping(Type.getIntType()) + 1;
 Object type = Type.getIntType();

 // When
 sorter.setFrameLocal(local, type);

 // Then - an exception should be thrown
 }

 @Test
 public void testSetLocalTypeWithNullType() {
 // Given
 int local = 0;
 Type type = null;

 // When
 sorter.setLocalType(local, type);

 // Then - no exception should be thrown
 }

 @Test
 public void testRemapWithNegativeVar() {
 // Given
 int var = -1;
 Type type = Type.getIntType();

 // When
 int remappedVar = sorter.remap(var, type);

 // Then
 assertNull(remappedVar);
 }

 @Test
 public void testRemapWithFirstLocal() {
 // Given
 int var = sorter.firstLocal;
 Type type = Type.getIntType();

 // When
 int remappedVar = sorter.remap(var, type);

 // Then
 assertEquals(var, remappedVar);
 }

 @Test
 public void testRemapWithMappedVar() {
 // Given
 int var = sorter.nextLocal + 1;
 Type type = Type.getIntType();
 int mappedVar = sorter.newLocalMapping(type);

 // When
 int remappedVar = sorter.remap(var, type);

 // Then
 assertEquals(mappedVar, remappedVar);
 }
}