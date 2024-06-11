package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    @Test
    public void testGetID() { /* Expert 1 */ }
    
    @Test
    public void testIterator() { /* Expert 1 */ }
    
    @Test
    public void testIterator_whenCalledTwiceOnSameInstance() { /* Expert 1 */ }
    
    @Test
    public void testIterator_whenRemoveIsCalled() { /* Expert 1 */ }
    
    @Test
    public void testMinus_whenTwoInstancesHaveDifferentNumberOfAttributes() { /* Expert 2 */ }
    
    @Test
    public void testMinus_whenTwoInstancesHaveSameAttributesButDifferentValues() { /* Expert 2 */ }
    
    @Test
    public void testMinus_whenCalledWithNonInstanceObject() { /* Expert 2 */ }
    
    @Test
    public void testNoAttributes() { /* Expert 3 */ }
    
    @Test
    public void testNoAttributes_whenAbstractInstanceNextIDIsInvalid() {
        AbstractInstance.nextID = -1;
        AbstractInstance instance = new AbstractInstance(new Object());
        int actualNoAttributes = instance.noAttributes();
        fail("noAttributes() should throw an exception when AbstractInstance.nextID is invalid");
    }
}