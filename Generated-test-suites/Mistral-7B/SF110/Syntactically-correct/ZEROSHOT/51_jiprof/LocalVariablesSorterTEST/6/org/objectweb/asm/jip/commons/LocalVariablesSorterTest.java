package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 private LocalVariablesSorter sorter;
 private MethodVisitor mv;

 @Before
 public void setup() {
 sorter = new LocalVariablesSorter(0, "", mv);
 }

 @Test
 public void testVisitVarInsn() {
 int[] expectedMapping = {-1, 1, 1, 2, 3, -1, 5, 5, 4, 4, -1, 7, 7, 6, 6, -1, 9, 9};
 Type[] types = {Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.OBJECT_TYPE, Type.OBJECT_TYPE};
 for (int i = 0; i < types.length; i++) {
 int var = i + 1;
 int opcode = (i < 10) ? 21 : ((i % 2 == 0) ? 56 : ((i % 2 == 1) ? 55 : ((i % 2 == 0) ? 57 : 54)));
 sorter.visitVarInsn(opcode, var);
 assertEquals(expectedMapping[i], sorter.mapping[var * 2 + types[i].getSize()]);
 }
 }

 @Test
 public void testVisitIincInsn() {
 int[] expectedMapping = {1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9};
 for (int i = 0; i < 16; i++) {
 int var = i + 1;
 int increment = i + 1;
 sorter.visitIincInsn(var, increment);
 assertEquals(expectedMapping[i], sorter.mapping[var]);
 }
 }

 @Test
 public void testVisitMaxs() {
 int expectedMaxLocals = 17;
 int expectedMaxStack = 1;
 sorter.visitMaxs(expectedMaxStack, 10);
 assertEquals(expectedMaxLocals, sorter.nextLocal);
 }

 @Test
 public void testVisitLocalVariable() {
 String[] names = {"i1", "i2", "l1", "f1", "i3", "f2", "d1", "d2", "i4", "i5", "i6", "l2", "l3", "f3", "f4", "f5", "c1", "c2"};
 Type[] descs = {Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.INT_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.getType("Ljava/lang/String;"), Type.getType("Ljava/lang/String;")};
 Label[] starts = {new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label()};
 Label[] ends = {new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label()};
 int[] indices = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
 for (int i = 0; i < indices.length; i++) {
 String name = names[i];
 Type desc = descs[i];
 Label start = starts[i];
 Label end = ends[i];
 int index = indices[i];
 sorter.visitLocalVariable(name, desc.getDescriptor(), null, start, end, index);
 assertEquals(index, sorter.mapping[name.hashCode() * 2 + desc.getSize()]);
 }
 }

 @Test
 public void testVisitFrame() {
 Object[] local = {Integer.TYPE, Float.TYPE, Long.TYPE, LocalVariablesSorter.OBJECT_TYPE, LocalVariablesSorter.OBJECT_TYPE, LocalVariablesSorter.OBJECT_TYPE, Long.TYPE, Long.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Long.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, String.class, String.class};
 int[] expectedNewLocals = {-1, 1, 1, 2, 3, -1, 5, 5, 4, 4, -1, 7, 7, 6, 6, -1, 9, 9};
 int numberOfLocals = local.length;
 int[] oldLocals = new int[numberOfLocals];
 System.arraycopy(expectedNewLocals, 0, oldLocals, 0, numberOfLocals);
 sorter.visitFrame(0, numberOfLocals, local, 0, null);
 assertArrayEqual(oldLocals, sorter.newLocals);
 }

 @Test
 public void testNewLocal() {
 Type type = Type.getIntType();
 int expectedLocal = 17;
 int actualLocal = sorter.newLocal(type);
 assertEquals(expectedLocal, actualLocal);
 }

 @Test
 public void testSetLocalType() {
 int local = 1;
 Type type = Type.getIntType();
 sorter.setLocalType(local, type);
 assertEquals(type, sorter.localTypes[local]);
 }

 @Test
 public void testSetFrameLocal() {
 int local = 1;
 Object obj = Integer.TYPE;
 sorter.setFrameLocal(local, obj);
 assertEquals(obj, sorter.newLocals[local]);
 }

 @Test
 public void testRemap() {
 int var = 6;
 Type type = Type.getFloatType();
 int expectedMapping = 8;
 int actualMapping = sorter.remap(var, type);
 assertEquals(expectedMapping, actualMapping);
 }

 @Test
 public void testNewLocalMapping() {
 Type type = Type.getFloatType();
 int expectedLocal = 18;
 int actualLocal = sorter.newLocalMapping(type);
 assertEquals(expectedLocal, actualLocal);
 }
}