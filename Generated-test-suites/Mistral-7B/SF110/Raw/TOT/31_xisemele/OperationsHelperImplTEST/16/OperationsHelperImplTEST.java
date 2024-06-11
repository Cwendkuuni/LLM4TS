package net.sf.xisemele.impl.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.w3c.dom.*;
import java.util.*;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    @org.junit.Test
    public void testChildren() {
        ChildrenTest childrenTest = new ChildrenTest();
        childrenTest.runTests();
    }

    @org.junit.Test
    public void testFind() {
        FindTest findTest = new FindTest();
        findTest.runTests();
    }

    @org.junit.Test
    public void testNodeWithNameAndSublistWithName() {
        NodeWithNameAndSublistWithNameTest nodeWithNameAndSublistWithNameTest = new NodeWithNameAndSublistWithNameTest();
        nodeWithNameAndSublistWithNameTest.runTests();
    }
}