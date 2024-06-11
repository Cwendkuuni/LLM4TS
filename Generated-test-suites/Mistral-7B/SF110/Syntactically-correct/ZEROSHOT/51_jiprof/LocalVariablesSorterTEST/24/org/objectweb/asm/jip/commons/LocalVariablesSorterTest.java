package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testLocalVariablesSorter() {
 LocalVariablesSorter lvs = new LocalVariablesSorter(0x0001, "(Ljava/lang/String;)V", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {
 super.visitEnd();
 }
 });
 ClassReader cr = new ClassReader("TestClass");
 cr.accept(lvs, 0);

 // Test visitVarInsn
 lvs.visitVarInsn(Opcodes.ILOAD, 0);
 lvs.visitVarInsn(Opcodes.ILOAD, 1);
 int expected1 = 0;
 int expected2 = 1;
 int actual1 = lvs.mapping[expected1];
 int actual2 = lvs.mapping[expected2];
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);

 // Test visitIincInsn
 lvs.visitIincInsn(1, 1);
 int expected3 = 2;
 int actual3 = lvs.mapping[expected3];
 assertEquals(expected3, actual3);

 // Test visitMaxs
 int maxStack = 5;
 int maxLocals = 10;
 lvs.visitMaxs(maxStack, maxLocals);
 int expectedMaxLocals = 12; // 10 original locals + 2 for long and double
 int actualMaxLocals = lvs.nextLocal;
 assertEquals(expectedMaxLocals, actualMaxLocals);

 // Test visitLocalVariable
 String name1 = "name1";
 String desc1 = "Ljava/lang/String;";
 Label start1 = new Label();
 Label end1 = new Label();
 int index1 = 1;
 lvs.visitLocalVariable(name1, desc1, null, start1, end1, index1);
 int expectedName1Index = 1;
 int actualName1Index = lvs.mapping[index1];
 assertEquals(expectedName1Index, actualName1Index);

 // Test visitFrame
 Object[] oldLocals = new Object[]{Opcodes.INTEGER, Opcodes.LONG, Opcodes.FLOAT};
 int nLocal = oldLocals.length;
 int nStack = 3;
 Object[] stack = new Object[3];
 lvs.visitFrame(Opcodes.ABORT, nLocal, oldLocals, nStack, stack);
 lvs.visitFrame(Opcodes.SAME, nLocal, oldLocals, nStack, stack);
 assertArrayEquals(oldLocals, lvs.newLocals);

 // Test newLocal
 Type type = Type.getObjectType("java/lang/String");
 int local = lvs.newLocal(type);
 int expectedLocal = 3; // assuming the first two locals are int and float
 int actualLocal = lvs.mapping[local];
 assertEquals(expectedLocal, actualLocal);

 // Test setFrameLocal and setLocalType
 lvs.setFrameLocal(local, type.getInternalName());
 String newTypeName = "java/lang/Integer";
 lvs.setLocalType(local, Type.getType(newTypeName));
 Type newType = Type.getType(newTypeName);
 assertEquals(newType.getInternalName(), lvs.newLocals[local].toString());

 // Test remap
 int originalVar = 2;
 Type originalType = Type.INT_TYPE;
 int newVar = lvs.remap(originalVar, originalType);
 int expectedNewVar = 3;
 int actualNewVar = lvs.mapping[newVar];
 assertEquals(expectedNewVar, actualNewVar);

 // Test newLocalMapping
 Type newType2 = Type.DOUBLE_TYPE;
 int newLocal = lvs.newLocalMapping(newType2);
 int expectedNewLocal = 5;
 int actualNewLocal = lvs.mapping[newLocal];
 assertEquals(expectedNewLocal, actualNewLocal);
 }
}