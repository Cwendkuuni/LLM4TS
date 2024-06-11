package org.junit.jupiter.api;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.objectweb.asm.*;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        Type intType = Type.INT_TYPE;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitVarInsn(21, 0);
        localVariablesSorter.visitVarInsn(21, 1);

        Type longType = Type.LONG_TYPE;
        localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitVarInsn(22, 0);
        localVariablesSorter.visitVarInsn(22, 1);

        Type floatType = Type.FLOAT_TYPE;
        localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitVarInsn(23, 0);
        localVariablesSorter.visitVarInsn(23, 1);

        Type doubleType = Type.DOUBLE_TYPE;
        localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitVarInsn(24, 0);
        localVariablesSorter.visitVarInsn(24, 1);

        Type objectType = Type.getType("java.lang.String");
        localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitVarInsn(25, 0);
        localVariablesSorter.visitVarInsn(25, 1);

        verify(mock(MethodVisitor.class)).visitVarInsn(opcode, arg);
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitIincInsn(0, 1);
        localVariablesSorter.visitIincInsn(1, 2);

        verify(mock(MethodVisitor.class)).visitIincInsn(index, increment);
    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mv);

        localVariablesSorter.visitFrame(0, 0, null, 0, null);

        Object[] locals = new Object[]{1, 2.0f, 3L, 4.0};
        localVariablesSorter.visitFrame(0, locals.length, locals, 0, null);

        verify(mv).visitFrame(type, nLocal, local, nStack, stack);
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        Type intType = Type.INT_TYPE;
        int local = localVariablesSorter.newLocal(intType);
        assertEquals(local, 0);

        Type floatType = Type.FLOAT_TYPE;
        local = localVariablesSorter.newLocal(floatType);
        assertEquals(local, 1);

        Type longType = Type.LONG_TYPE;
        local = localVariablesSorter.newLocal(longType);
        assertEquals(local, 2);

        Type doubleType = Type.DOUBLE_TYPE;
        local = localVariablesSorter.newLocal(doubleType);
        assertEquals(local, 3);

        Type stringType = Type.getType("java.lang.String");
        local = localVariablesSorter.newLocal(stringType);
        assertEquals(local, 4);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        String name = "testName";
        String desc = "I";
        Label start = new Label();
        Label end = new Label();
        int index = 1;
        localVariablesSorter.visitLocalVariable(name, desc, null, start, end, index);

        String signature = "I";
        localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);

        verify(mock(MethodVisitor.class)).visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, null, mock(MethodVisitor.class));
        localVariablesSorter.visitMaxs(1, 1);
        localVariablesSorter.visitMaxs(2, 2);

        verify(mock(MethodVisitor.class)).visitMaxs(maxStack, maxLocals);
    }
}