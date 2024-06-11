package org.mockito.internal.matchers.test;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @org.junit.Test
    public void testAll() {
        SameTest1 test1 = new SameTest1();
        test1.runTests();

        SameTest2 test2 = new SameTest2();
        test2.runTests();

        SameTest3 test3 = new SameTest3();
        test3.runTests();
    }

    private void runTests() {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(org.junit.Test.class)) {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}