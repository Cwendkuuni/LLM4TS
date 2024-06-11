import org.junit.Test;
import org.junit.Assert.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {

 private static final String CLASS_NAME = "org.objectweb.asm.jip.commons.LocalVariablesSorter";

 @Test
 public void testConstructor() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 Assert.assertNotNull(localVariablesSorter);
 }

 @Test
 public void testVisitVarInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 int var = 1;
 Type type = Type.INT_TYPE;
 localVariablesSorter.visitVarInsn(21, var);
 int expected = var;
 int actual = localVariablesSorter.remap(var, type);
 assertEquals(expected, actual);
 }

 @Test
 public void testVisitIincInsn() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 int var = 1;
 int increment = 1;
 localVariablesSorter.visitIincInsn(var, increment);
 int expected = var + increment;
 int actual = localVariablesSorter.remap(var, Type.INT_TYPE);
 assertEquals(expected, actual);
 }

 @Test
 public void testVisitMaxs() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 int maxStack = 1;
 int maxLocals = 1;
 localVariablesSorter.visitMaxs(maxStack, maxLocals);
 int expectedMaxStack = maxStack;
 int expectedMaxLocals = maxLocals;
 int actualMaxStack = localVariablesSorter.nextLocal;
 int actualMaxLocals = localVariablesSorter.nextLocal - maxLocals;
 assertEquals(expectedMaxStack, actualMaxStack);
 assertEquals(expectedMaxLocals, actualMaxLocals);
 }

 @Test
 public void testVisitLocalVariable() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 String name = "name";
 String desc = "I";
 String signature = "";
 Label startLabel = new Label();
 Label endLabel = new Label();
 int index = 1;
 localVariablesSorter.visitLocalVariable(name, desc, signature, startLabel, endLabel, index);
 int expectedIndex = index;
 int actualIndex = localVariablesSorter.remap(index, Type.getType(desc));
 assertEquals(expectedIndex, actualIndex);
 }

 @Test
 public void testVisitFrame() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 Object[] local = new Object[1];
 local[0] = Opcodes.INTEGER;
 int nLocal = local.length;
 Object[] stack = new Object[0];
 int nStack = stack.length;
 localVariablesSorter.visitFrame(Type.INTEGER_TYPE.getMethodDescriptor(Type.VOID_TYPE), nLocal, local, nStack, stack);
 int expectedNumberOfLocals = nLocal;
 Object[] expectedNewLocals = localVariablesSorter.newLocals;
 Object[] actualNewLocals = new Object[expectedNumberOfLocals];
 System.arraycopy(expectedNewLocals, 0, actualNewLocals, 0, expectedNumberOfLocals);
 localVariablesSorter.visitFrame(Type.INTEGER_TYPE.getMethodDescriptor(Type.VOID_TYPE), nLocal, local, nStack, stack);
 Object[] actualNewLocalsAfterVisitFrame = localVariablesSorter.newLocals;
 assertArrayEquals(actualNewLocals, actualNewLocalsAfterVisitFrame);
 }

 @Test
 public void testNewLocal() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 Type type = Type.LONG_TYPE;
 int newLocal = localVariablesSorter.newLocal(type);
 int expectedNewLocal = localVariablesSorter.nextLocal - type.getSize();
 int actualNewLocal = newLocal;
 assertEquals(expectedNewLocal, actualNewLocal);
 }

 @Test(expected = IllegalStateException.class)
 public void testVisitFrameWithChangedFlag() {
 LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", new MethodVisitor(0, null) {
 @Override
 public void visitEnd() {}
 });
 localVariablesSorter.changed = true;
 Object[] local = new Object[1];
 local[0] = Opcodes.INTEGER;
 int nLocal = local.length;
 Object[] stack = new Object[0];
 int nStack = stack.length;
 localVariablesSorter.visitFrame(Type.INTEGER_TYPE.getMethodDescriptor(Type.VOID_TYPE), nLocal, local, nStack, stack);
 }
}