package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.MethodAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

 @Test
 public void testLocalVariablesSorter() {
 int access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
 String desc = "()V";
 MethodVisitor mv = new ClassWriter(0).visitLdcInsn(this.getClass().getName())
 .visit(Opcodes.V1_5)
 .visit(Opcodes.V1_5, Opcodes.ATHROW)
 .visitEnd();
 LocalVariablesSorter sorter = new LocalVariablesSorter(access, desc, mv);

 // Test normal usage
 Type[] argTypes = new Type[] {Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE};
 sorter.visitVarInsn(Opcodes.ALOAD, 0); // load this
 for (int i = 0; i < argTypes.length; i++) {
 sorter.visitVarInsn(Opcodes.ALOAD, i + 1); // load args
 sorter.visitVarInsn(Opcodes.ASTORE, sorter.newLocal(argTypes[i]));
 }
 sorter.visitMaxs(2, sorter.nextLocal);
 sorter.visitEnd();
 ClassReader cr = new ClassReader(sorter.mv.toByteArray());
 ClassNode node = (ClassNode) cr.parse();
 assertNotNull(node.localVariables);
 assertEquals(argTypes.length + 1, node.localVariables.size());

 // Test edge cases: long and double variables
 sorter = new LocalVariablesSorter(access, desc, mv);
 sorter.visitVarInsn(Opcodes.ALOAD, 0); // load this
 sorter.visitVarInsn(Opcodes.LCONST_0); // load long 0
 sorter.visitVarInsn(Opcodes.LSTORE, sorter.newLocal(Type.LONG_TYPE));
 sorter.visitVarInsn(Opcodes.ALOAD, 0); // load this
 sorter.visitVarInsn(Opcodes.DCONST_0); // load double 0
 sorter.visitVarInsn(Opcodes.DSTORE, sorter.newLocal(Type.DOUBLE_TYPE));
 sorter.visitMaxs(3, sorter.nextLocal);
 sorter.visitEnd();
 ClassReader cr2 = new ClassReader(sorter.mv.toByteArray());
 ClassNode node2 = (ClassNode) cr2.parse();
 assertNotNull(node2.localVariables);
 assertEquals(argTypes.length + 1 + 2, node2.localVariables.size());

 // Test error scenarios: unsupported types
 sorter = new LocalVariablesSorter(access, desc, mv);
 sorter.visitVarInsn(Opcodes.ALOAD, 0); // load this
 sorter.visitVarInsn(Opcodes.ICONST_0); // load int 0
 sorter.visitVarInsn(Opcodes.ANEWARRAY, Type.CHAR_TYPE); // new char[]
 sorter.visitVarInsn(Opcodes.ASTORE, sorter.newLocal(Type.getType("java.lang.String[]")));
 sorter.visitMaxs(2, sorter.nextLocal);
 sorter.visitEnd();
 ClassReader cr3 = new ClassReader(sorter.mv.toByteArray());
 try {
 cr3.parse();
 fail("Expected ClassFormatError");
 } catch (ClassFormatError e) {
 // expected
 }
 }
}