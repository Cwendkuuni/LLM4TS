package com.lts.event;

import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;
    private JFrame frame;

    @BeforeClass
    public static void setUp() {
        // Set up the environment if needed
    }

    @AfterClass
    public static void tearDown() {
        // Clean up the environment if needed
    }

    @Before
    public void setUp() {
        simpleKeyListenerHelper = null;
        frame = new JFrame();
    }

    @After
    public void tearDown() {
        if (simpleKeyListenerHelper != null) {
            simpleKeyListenerHelper.detach();
            simpleKeyListenerHelper = null;
        }
        frame.dispose();
    }

    @Test
    public void testConstructor() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        assertNotNull(simpleKeyListenerHelper);
    }

    @Test
    public void testConstructorWithNullComponent() {
        assertNull(new SimpleKeyListenerHelper(null));
    }

    @Test
    public void testConstructorWithAlreadyAddedKeyListener() {
        frame.addKeyListener(new SimpleKeyListenerHelper(frame));
        assertNotNull(new SimpleKeyListenerHelper(frame));
    }

    @Test(expected = IllegalStateException.class)
    public void testDetachBeforeKeyPressed() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        frame.removeKeyListener(simpleKeyListenerHelper);
        simpleKeyListenerHelper.detach();
        simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyListenerWithInvalidType() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        simpleKeyListenerHelper.notifyListener(new Object(), 123, new Object());
    }

    @Test
    public void testKeyPressedWithComponentDisposed() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        frame.dispose();
        KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "").getSource();
        simpleKeyListenerHelper.keyPressed(event);
    }

    @Test
    public void testDetach() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        simpleKeyListenerHelper.detach();
        assertNull(simpleKeyListenerHelper.component);
    }

    @Test
    public void testNotifyListenerWithValidArguments() {
        SimpleKeyListener listener = mock(SimpleKeyListener.class);
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        simpleKeyListenerHelper.notifyListener(listener, 10, null);
        verify(listener).enter(argThat(isA(Component.class)));
    }

    @Test
    public void testKeyPressedWithValidKeycode() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "").getSource();
        simpleKeyListenerHelper.keyPressed(event);
        // Verify that the appropriate listener method is called
    }

    @Test
    public void testKeyPressedWithInvalidKeycode() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 123, "").getSource();
        simpleKeyListenerHelper.keyPressed(event);
        // Verify that no listener method is called
    }

    @Test
    public void testKeyPressedWithComponentNotFocused() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "").getSource();
        frame.requestFocus();
        simpleKeyListenerHelper.keyPressed(event);
        // Verify that the appropriate listener method is called
    }

    @Test
    public void testKeyPressedWithComponentFocused() {
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(frame);
        KeyEvent event = new KeyEvent(frame, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "").getSource();
        frame.requestFocus();
        simpleKeyListenerHelper.keyPressed(event);
        // Verify that the appropriate listener method is called
    }
}