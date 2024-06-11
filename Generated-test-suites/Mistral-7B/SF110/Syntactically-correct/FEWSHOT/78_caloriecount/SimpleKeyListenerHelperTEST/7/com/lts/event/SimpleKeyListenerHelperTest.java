package com.lts.event;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListener helper;
    private Component component;

    @Before
    public void setUp() {
        component = mock(Component.class);
        helper = new SimpleKeyKeyListenerHelper(component);
    }

    @Test
    public void testConstructor() {
        assertNotNull(helper);
    }

    @Test
    public void testDetach() {
        helper.detach();
        verify(component).removeKeyListener(helper);
    }

    @Test
    public void testNotifyListenerEnter() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCode = 10;
        helper.fire(keyCode);
        verify(listener).enter(component);
    }

    @Test
    public void testNotifyListenerInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCode = 155;
        helper.fire(keyCode);
        verify(listener).insert(component);
    }

    @Test
    public void testNotifyListenerDelete() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCode = 127;
        helper.fire(keyCode);
        verify(listener).delete(component);
    }

    @Test
    public void testNotifyListenerTab() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCode = 9;
        helper.fire(keyCode);
        verify(listener).tab(component);
    }

    @Test
    public void testKeyPressedEnterOrTabOrDeleteOrInsert() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCodeEnter = 10;
        int keyCodeTab = 9;
        int keyCodeDelete = 127;
        int keyCodeInsert = 155;

        helper.keyPressed(new KeyEvent(component, keyCodeEnter, false, false, keyCodeEnter, ""));
        helper.keyPressed(new KeyEvent(component, keyCodeTab, false, false, keyCodeTab, ""));
        helper.keyPressed(new KeyEvent(component, keyCodeDelete, false, false, keyCodeDelete, ""));
        helper.keyPressed(new KeyEvent(component, keyCodeInsert, false, false, keyCodeInsert, ""));

        verify(listener).enter(component);
        verify(listener).enter(component);
        verify(listener).delete(component);
        verify(listener).insert(component);
    }

    @Test
    public void testKeyPressedOtherKey() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        int keyCodeOther = 1;

        helper.keyPressed(new KeyEvent(component, keyCodeOther, false, false, keyCodeOther, ""));

        verify(listener, times(0)).enter(component);
        verify(listener, times(0)).insert(component);
        verify(listener, times(0)).delete(component);
        verify(listener, times(0)).tab(component);
    }
}