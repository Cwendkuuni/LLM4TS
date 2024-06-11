package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;

import java.util.Arrays;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter localVariablesSorter;
 private MethodVisitor methodVisitor;

 @Before
 public void setUp() {
 methodVisitor = new Bytecode();
 localVariablesSorter = new LocalVariablesSorter(ACC_PUBLIC, "()V", methodVisitor);
 }

 @Test
 public void testVisitVarInsnWithInt() {
 int var = 0;
 Type intType = INT_TYPE;
 localVariablesSorter.visitVarInsn(ILOAD, var);
 localVariablesSorter.visitVarInsn(this.localVariablesSorter.remap(var, intType));
 }

 @Test
 public void testVisitVarInsnWithLong() {
 int var = 0;
 Type longType = LONG_TYPE;
 localVariablesSorter.visitVarInsn(LLOAD, var);
 localVariablesSorter.visitVarInsn(this.localVariablesSorter.remap(var, longType));
 }

 @Test
 public void testVisitVarInsnWithFloat() {
 int var = 0;
 Type floatType = FLOAT_TYPE;
 localVariablesSorter.visitVarInsn(FLOAD, var);
 localVariablesSorter.visitVarInsn(this.localVariablesSorter.remap(var, floatType));
 }

 @Test
 public void testVisitVarInsnWithDouble() {
 int var = 0;
 Type doubleType = DOUBLE_TYPE;
 localVariablesSorter.visitVarInsn(DLOAD, var);
 localVariablesSorter.visitVarInsn(this.localVariablesSorter.remap(var, doubleType));
 }

 @Test
 public void testVisitVarInsnWithObject() {
 String objectName = "java.lang.String";
 Type objectType = getObjectType(objectName);
 int var = 0;
 localVariablesSorter.visitVarInsn(ALOAD, var);
 localVariablesSorter.visitVarInsn(this.localVariablesSorter.remap(var, objectType));
 }

 @Test
 public void testVisitIincInsn() {
 int var = 0;
 int increment = 1;
 localVariablesSorter.visitIincInsn(var, increment);
 localVariablesSorter.visitIincInsn(this.localVariablesSorter.remap(var, INT_TYPE), increment);
 }

 @Test
 public void testVisitMaxs() {
 int maxStack = 0;
 int maxLocals = 1;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 localVariablesSorter.visitMaxs(maxStack, this.localVariablesSorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariableWithStringDesc() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = null;
 Label start = new Label();
 Label end = new Label();
 int index = 0;
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 localVariablesSorter.visitLocalVariable(name, this.localVariablesSorter.remapDesc(desc), signature, start, end, this.localVariablesSorter.remap(index, getObjectType(desc)));
 }

 @Test
 public void testVisitLocalVariableWithNullDesc() {
 String name = "name";
 Label start = new Label();
 Label end = new Label();
 int index = 0;
 localVariablesSorter.visitLocalVariable(name, null, null, start, end, index);
 localVariablesSorter.visitLocalVariable(name, null, null, start, end, this.localVariablesSorter.remap(index, INT_TYPE));
 }

 @Test
 public void testVisitLocalVariableWithSignature() {
 String name = "name";
 String desc = "Ljava/lang/String;";
 String signature = "()V";
 Label start = new Label();
 Label end = new Label();
 int index = 0;
 localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
 localVariablesSorter.visitLocalVariable(name, this.localVariablesSorter.remapDesc(desc), signature, start, end, this.localVariablesSorter.remap(index, getObjectType(desc)));
 }

 @Test
 public void testVisitFrame() {
 int type = Opcodes.FRAME_TYPE_FULL;
 int nLocal = 1;
 Object[] local = {INT_TYPE};
 int nStack = 0;
 Object[] stack = null;
 localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
 localVariablesSorter.visitFrame(type, nLocal, this.localVariablesSorter.newLocalMapping(local[0]), nStack, stack);
 }

 @Test
 public void testNewLocalWithIntType() {
 Type intType = INT_TYPE;
 int local = localVariablesSorter.newLocal(intType);
 localVariablesSorter.visitLocalVariable("", intType.getDescriptor(), null, new Label(), new Label(), local);
 localVariablesSorter.visitVarInsn(ILOAD, local);
 }

 @Test
 public void testNewLocalWithFloatType() {
 Type floatType = FLOAT_TYPE;
 int local = localVariablesSorter.newLocal(floatType);
 localVariablesSorter.visitLocalVariable("", floatType.getDescriptor(), null, new Label(), new Label(), local);
 localVariablesSorter.visitVarInsn(FLOAD, local);
 }

 @Test
 public void testNewLocalWithLongType() {
 Type longType = LONG_TYPE;
 int local = localVariablesSorter.newLocal(longType);
 localVariablesSorter.visitLocalVariable("", longType.getDescriptor(), null, new Label(), new Label(), local);
 localVariablesSorter.visitVarInsn(LLOAD, local);
 }

 @Test
 public void testNewLocalWithObjectType() {
 Type objectType = getObjectType("java.lang.String");
 int local = localVariablesSorter.newLocal(objectType);
 localVariablesSorter.visitLocalVariable("", objectType.getDescriptor(), null, new Label(), new Label(), local);
 localVariablesSorter.visitVarInsn(ALOAD, local);
 }

 @Test
 public void testSetFrameLocal() {
 Object t = Opcodes.INTEGER;
 int local = localVariablesSorter.nextLocal;
 localVariablesSorter.nextLocal += 1;
 localVariablesSorter.setFrameLocal(local, t);
 localVariablesSorter.newLocals = new Object[]{t};
 localVariablesSorter.visitFrame(Opcodes.FRAME_TYPE_FULL, 1, new Object[]{INT_TYPE}, 0, null);
 }

 @Test
 public void testRemap() {
 int var = 0;
 int index = 0;
 Type intType = INT_TYPE;
 Type longType = LONG_TYPE;
 Type floatType = FLOAT_TYPE;
 Type doubleType = DOUBLE_TYPE;
 Type objectType = getObjectType("java.lang.String");
 localVariablesSorter.mapping = new int[10];
 localVariablesSorter.newLocals = new Object[]{Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER};
 localVariablesSorter.changed = false;

 int remappedVar = localVariablesSorter.remap(var, intType);
 assertEquals(0, remappedVar);
 remappedVar = localVariablesSorter.remap(var, longType);
 assertEquals(1, remappedVar);
 remappedVar = localVariablesSorter.remap(var, floatType);
 assertEquals(2, remappedVar);
 remappedVar = localVariablesSorter.remap(var, doubleType);
 assertEquals(3, remappedVar);
 remappedVar = localVariablesSorter.remap(var, objectType);
 assertEquals(4, remappedVar);

 localVariablesSorter.remap(index, intType);
 assertEquals(5, localVariablesSorter.mapping[index]);
 localVariablesSorter.remap(index, longType);
 assertEquals(6, localVariablesSorter.mapping[index]);
 localVariablesSorter.remap(index, floatType);
 assertEquals(7, localVariablesSorter.mapping[index]);
 localVariablesSorter.remap(index, doubleType);
 assertEquals(8, localVariablesSorter.mapping[index]);
 localVariablesSorter.remap(index, objectType);
 assertEquals(9, localVariablesSorter.mapping[index]);
 }
}