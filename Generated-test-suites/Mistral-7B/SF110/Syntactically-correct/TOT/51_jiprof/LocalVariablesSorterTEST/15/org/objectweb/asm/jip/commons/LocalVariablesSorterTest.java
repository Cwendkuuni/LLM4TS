package org.objectweb.asm.jip.commons.test;

import org.junit.Assert;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LocalVariablesSorterTest {

    public void testLocalVariablesSorter() throws IOException {
        String inputClass =
                "package test;\n" +
                        "public class InputClass {\n" +
                        "    private int a;\n" +
                        "    private int b;\n" +
                        "    private int c;\n" +
                        "    private int d;\n" +
                        "    private double e;\n" +
                        "    private float f;\n" +
                        "    private long g;\n" +
                        "    private Object h;\n" +
                        "    public static void main(String[] args) {\n" +
                        "        InputClass obj = new InputClass();\n" +
                        "        obj.a = 1;\n" +
                        "        obj.b = 2;\n" +
                        "        obj.c = 3;\n" +
                        "        obj.d = 4;\n" +
                        "        obj.e = 5.5;\n" +
                        "        obj.f = 6.6f;\n" +
                        "        obj.g = 7L;\n" +
                        "        obj.h = new Object() {\n" +
                        "            public int i = 8;\n" +
                        "        };\n" +
                        "        System.out.println(\"Result:\");\n" +
                        "        System.out.println(\"a = \" + obj.a);\n" +
                        "        System.out.println(\"b = \" + obj.b);\n" +
                        "        System.out.println(\"c = \" + obj.c);\n" +
                        "        System.out.println(\"d = \" + obj.d);\n" +
                        "        System.out.println(\"e = \" + obj.e);\n" +
                        "        System.out.println(\"f = \" + obj.f);\n" +
                        "        System.out.println(\"g = \" + obj.g);\n" +
                        "        System.out.println(\"h = \" + obj.h.i);\n" +
                        "    }\n" +
                        "}";

        byte[] inputClassBytes = inputClass.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ClassWriter classWriter = new ClassWriter(0, outputStream);
        ClassReader classReader = new ClassReader(inputClassBytes);

        MethodVisitor mv = classWriter.visitMethod(
                0x0024, "main", "([Ljava/lang/String;)V", null, null);
        new LocalVariablesSorter(
                0x0001, "()V", mv)
                .visitCode();

        // Test visitVarInsn method with different variable types.
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.FLOAD, 2);
        mv.visitVarInsn(Opcodes.DLOAD, 3);
        mv.visitVarInsn(Opcodes.LLOAD, 4);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "test/InputClass", "h", "Ljava/lang/Object;");
        mv.visitFieldInsn(Opcodes.GETFIELD, "java/lang/Object", "i", "I");

        mv.visitVarInsn(Opcodes.ASTORE, 5); // Local variable 'i' is created.
        mv.visitIntInsn(Opcodes.BIPUSH, 9);
        mv.visitVarInsn(Opcodes.ISTORE, 6); // Local variable 'j' is created.

        mv.visitLabel(0);
        mv.visitFrame(Opcodes.FRAME_LOCALS, 7, new Object[]{"I", "I", "F", "D", "Ljava/lang/Object;", "I", "I"}, 0, null);
        mv.visitVarInsn(Opcodes.ILOAD, 5);
        mv.visitVarInsn(Opcodes.ILOAD, 6);
        mv.visitInsn(Opcodes.IADD);
        mv.visitVarInsn(Opcodes.ISTORE, 5);

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "test/InputClass", "a", "I");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "test/InputClass", "b", "I");
        mv.visitIntInsn(Opcodes.BIPUSH, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "(Ljava/io/PrintStream;)V");
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("a = ");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "test/InputClass", "a", "I");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitLdcInsn(" ");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, "test/InputClass", "b", "I");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "(Ljava/io/PrintStream;Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.RETURN);

        mv.visitMaxs(7, 7);
        mv.visitEnd();

        classWriter.visitEnd();

        byte[] outputClassBytes = outputStream.toByteArray();

        InputClass original = new InputClass();
        InputClass sorted = new InputClass();
        Class.forName(inputClass.substring(0, inputClass.lastIndexOf('/') + 1));
        originalClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader localClassLoader = new ClassLoader(originalClassLoader) {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals("test.InputClass")) {
                    return defineClass(name, inputClassBytes, 0, inputClassBytes.length);
                }
                return super.findClass(name);
            }
        };

        Class<?> originalClass = localClassLoader.loadClass("test.InputClass");
        Class<?> originalInputClass = Class.forName("test.InputClass");

        // Test visitVarInsn method with different variable types.
        Assert.assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1}, original.getClass().getFields().length);
        Assert.assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1}, originalClass.getFields().length);
        Assert.assertEquals(original.getClass(), originalClass);

        Class<?> sortedClass = new ClassLoader(originalClassLoader) {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals("test.InputClass")) {
                    return defineClass(name, outputClassBytes, 0, outputClassBytes.length);
                }
                return super.findClass(name);
            }
        }.loadClass("test.InputClass");

        Assert.assertNotEquals(originalClass, sortedClass);
        Field[] sortedFields = sortedClass.getFields();
        int[] sortedFieldTypes = new int[sortedFields.length];
        for (Field field : sortedFields) {
            sortedFieldTypes[sortedFields.indexOf(field)] = field.getType().getSort();
        }
        Assert.assertArrayEquals(new int[]{9, 1, 1, 1, 1, 1, 1, 1, 1}, sortedFieldTypes);

        Assert.assertEquals(sorted.getClass(), sortedClass);
    }

    private ClassLoader originalClassLoader;
}