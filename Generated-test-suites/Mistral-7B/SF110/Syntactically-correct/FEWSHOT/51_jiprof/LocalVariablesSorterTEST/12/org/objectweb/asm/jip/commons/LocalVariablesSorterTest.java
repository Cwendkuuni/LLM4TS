package org.objectweb.asm.jip.commons;

import org.objectweb.asm.jip.MethodVisitor;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 LocalVariablesSorter sorter;
 MethodVisitor mv;

 @Before
 public void setup() {
 sorter = new LocalVariablesSorter(0, "()V", mv);
 mv = mock(MethodVisitor.class);
 }

 @Test
 public void testVisitVarInsn() {
 int[] testCases = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10};
 Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
 for (int i = 0; i < testCases.length; i++) {
 int var = testCases[i];
 Type type = types[i];
 sorter.visitVarInsn(1, var);
 assertEquals(var, sorter.remap(var, type));
 }
 }

 @Test
 public void testVisitIincInsn() {
 int[] testCases = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10};
 for (int i = 0; i < testCases.length; i++) {
 int var = testCases[i];
 sorter.visitIincInsn(var, 1);
 assertEquals(var, sorter.remap(var, Type.INT_TYPE));
 }
 }

 @Test
 public void testVisitLocalVariable() {
 String[] names = {"name1", "name2", "name3", "name4", "name5", "name6", "name7", "name8", "name9", "name10", "name11"};
 String[] descs = {"I", "F", "J", "D", "Ljava/lang/String;", "Ljava/lang/Object;", "Ljava/util/List;", "Ljava/util/Map;"};
 String[] signatures = {"", "", "", "", "", "", "", "", "", "", ""};
 Label[] starts = {new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label()};
 Label[] ends = {new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label(), new Label()};
 int[] indices = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 for (int i = 0; i < indices.length; i++) {
 int index = indices[i];
 String name = names[i];
 String desc = descs[i];
 String signature = signatures[i];
 Label start = starts[i];
 Label end = ends[i];
 sorter.visitLocalVariable(name, desc, signature, start, end, index);
 int newIndex = sorter.remap(index, Type.getType(desc));
 assertEquals(newIndex, sorter.nextLocal - Type.getType(desc).getSize());
 }
 }

 @Test
 public void testVisFrame() {
 Object[] locals = {Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE, String.class, Object.class, List.class, Map.class};
 Object[] stack = {null, null, null, null, null, null, null, null};
 int[] nLocals = {1, 2, 2, 2, 1, 1, 1, 1};
 int nStack = 1;
 boolean changed = false;
 sorter.visitFrame(0, 8, locals, nStack, stack);
 assertFalse(changed);
 changed = true;
 sorter.visitFrame(1, 8, locals, nStack, stack);
 assertTrue(changed);
 }

 @Test
 public void testNewLocal() {
 Type[] types = {Type.INT_TYPE, Type.FLOAT_TYPE, Type.LONG_TYPE, Type.DOUBLE_TYPE, Type.OBJECT_TYPE};
 int[] locals = {1, 2, 3, 4, 5};
 for (int i = 0; i < locals.length; i++) {
 int local = sorter.newLocal(types[i]);
 assertEquals(local, sorter.remap(i, types[i]));
 }
 }
}