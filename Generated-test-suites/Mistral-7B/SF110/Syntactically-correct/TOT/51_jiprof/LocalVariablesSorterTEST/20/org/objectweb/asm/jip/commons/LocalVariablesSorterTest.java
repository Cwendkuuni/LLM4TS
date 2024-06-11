package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.assertArrayEquals;

public class LocalVariablesSorterTest {

 @Test
 public void testLocalVariablesSorter() throws Exception {
 final String testClassName = "TestLocalVariablesSorter";
 final String testMethodName = "testLocalVariablesSorting";
 final String testClassDesc = "()V";

 // Given
 final String inputClassCode =
 "public class TestInput {" +
 "    int a; " +
 "    int b; " +
 "    float c; " +
 "    int d; " +
 "    long e; " +
 "    long f; " +
 "    void test() {" +
 "        a = 1; " +
 "        b = 2; " +
 "        c = 3.0f; " +
 "        d = 4; " +
 "        e = 5L; " +
 "        f = 6L; " +
 "    }" +
 "}";

 final byte[] inputClassBytes = ClassReader.parseFromSource(inputClassCode, null);
 final ClassWriter cw = new ClassWriter(0);
 final MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, testMethodName, testClassDesc, null, null);
 final LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, testClassDesc, mv);

 // When
 final int[] inputMapping = { 1, 3, 5, 2, 0, 6, 4 };
 final Object[] inputNewLocals = { Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.LONG, Opcodes.LONG, Opcodes.TOP };
 sorter.visitLocalVariable("a", "I", null, null, null, 0);
 sorter.visitLocalVariable("b", "I", null, null, null, 1);
 sorter.visitLocalVariable("c", "F", null, null, null, 2);
 sorter.visitLocalVariable("d", "I", null, null, null, 3);
 sorter.visitLocalVariable("e", "J", null, null, null, 4);
 sorter.visitLocalVariable("f", "J", null, null, null, 5);
 final int[] outputMapping = sorter.mapping;
 final Object[] outputNewLocals = sorter.newLocals;

 sorter.visitFrame(Opcodes.F_CHOP, 0, null, 0, null);
 sorter.visitMaxs(2, inputMapping.length);
 sorter.visitEnd();

 final byte[] outputClassBytes = cw.toByteArray();
 final byte[] expectedOutputClassBytes = getExpectedOutputClassBytes();

 // Then
 assertArrayEquals("Test Local Variables Sorting failed.", expectedOutputClassBytes, outputClassBytes);
 }

 private byte[] getExpectedOutputClassBytes() {
 // ... implementation to get the expected output class bytes for comparison
 }

}