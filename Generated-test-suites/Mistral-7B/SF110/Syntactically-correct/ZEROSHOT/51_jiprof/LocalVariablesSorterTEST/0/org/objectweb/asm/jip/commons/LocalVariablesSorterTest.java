package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.MethodAdapter;
import org.objectweb.asm.Type;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        String methodDesc = "(Ljava/lang/String;)V";
        MethodVisitor mv = Mockito.mock(MethodVisitor.class);
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, methodDesc, mv);
        assertEquals(1, localVariablesSorter.nextLocal);
        assertEquals(41, localVariablesSorter.mapping.length);
        assertEquals(20, localVariablesSorter.newLocals.length);
        assertEquals(1, localVariablesSorter.firstLocal);
        assertEquals(0, localVariablesSorter.nextLocal);
        assertEquals(false, localVariablesSorter.changed);
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int var = 5;
        Type type = Type.INT_TYPE;
        PowerMockito.doNothing().when(localVariablesSorter).mv.visitVarInsn(Mockito.anyInt(), Mockito.anyInt());
        localVariablesSorter.visitVarInsn(21, var);
        PowerMockito.verify(localVariablesSorter.mv, Mockito.times(1)).visitVarInsn(21, Mockito.eq(localVariablesSorter.remap(var, type)));
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int var = 5;
        int increment = 10;
        PowerMockito.doNothing().when(localVariablesSorter).mv.visitIincInsn(Mockito.anyInt(), Mockito.anyInt());
        localVariablesSorter.visitIincInsn(var, increment);
        PowerMockito.verify(localVariablesSorter.mv, Mockito.times(1)).visitIincInsn(Mockito.eq(localVariablesSorter.remap(var, Type.INT_TYPE)), Mockito.eq(increment));
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int maxStack = 10;
        int maxLocals = 20;
        PowerMockito.doNothing().when(localVariablesSorter).mv.visitMaxs(Mockito.anyInt(), Mockito.anyInt());
        localVariablesSorter.visitMaxs(maxStack, maxLocals);
        PowerMockito.verify(localVariablesSorter.mv, Mockito.times(1)).visitMaxs(Mockito.eq(maxStack), Mockito.eq(localVariablesSorter.nextLocal));
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        String name = "name";
        String desc = "Ljava/lang/String;";
        String signature = "";
        Label start = Mockito.mock(Label.class);
        Label end = Mockito.mock(Label.class);
        int index = 5;
        PowerMockito.doNothing().when(localVariablesSorter).mv.visitLocalVariable(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(Label.class), Mockito.any(Label.class), Mockito.anyInt());
        localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
        PowerMockito.verify(localVariablesSorter.mv, Mockito.times(1)).visitLocalVariable(Mockito.eq(name), Mockito.eq(desc), Mockito.eq(signature), Mockito.eq(start), Mockito.eq(end), Mockito.eq(localVariablesSorter.remap(index, Type.getType(desc))));
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int type = -1;
        int nLocal = 10;
        Object[] local = new Object[10];
        Object[] stack = new Object[10];
        PowerMockito.doNothing().when(localVariablesSorter).mv.visitFrame(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyArray(), Mockito.anyInt(), Mockito.anyArray());
        localVariablesSorter.visitFrame(type, nLocal, local, 0, stack);
        PowerMockito.verify(localVariablesSorter.mv, Mockito.times(1)).visitFrame(Mockito.eq(type), Mockito.eq(nLocal), Mockito.eq(local), Mockito.eq(0), Mockito.eq(stack));
    }

    @Test
    public void testNewLocal() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        Type type = Type.INT_TYPE;
        int local = localVariablesSorter.newLocal(type);
        assertEquals(5, local);
    }

    @Test
    public void testSetLocalType() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int local = 5;
        Type type = Type.INT_TYPE;
        localVariablesSorter.setLocalType(local, type);
        // Not testing this method as it is empty
    }

    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int local = 5;
        Object type = Type.INT_TYPE;
        localVariablesSorter.setFrameLocal(local, type);
        Object[] newLocals = localVariablesSorter.newLocals;
        assertNotNull(newLocals);
        assertEquals(20, newLocals.length);
        assertEquals(Type.INT_TYPE, newLocals[local]);
    }

    @Test
    public void testRemap() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "(Ljava/lang/String;)V", Mockito.mock(MethodVisitor.class));
        int var = 5;
        Type type = Type.INT_TYPE;
        int remappedVar = localVariablesSorter.remap(var, type);
        assertEquals(5, remappedVar);
    }
}