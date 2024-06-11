package com.allenstudio.ir.ui.test;

import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.Assert.*;
import org.junit.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    @Mock
    private InspirentoMediator mockMediator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMainMenuConstruction() {
        // Test code for MainMenu construction goes here
    }

    @Test
    public void testCreateMenuWithLabel() {
        // Test code for creating a JMenu with a label goes here
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        // Test code for creating a JCheckBoxMenuItem goes here
    }

    @Test
    public void testCreateMenuItem() {
        // Test code for creating a JMenuItem goes here
    }

    @Test
    public void testAddShortcutAndIcon() {
        // Test code for adding a shortcut and icon to a JMenuItem goes here
    }

    @Test(expected = RuntimeException.class)
    public void testMissingResource() {
        // Test code for handling missing resources goes here
    }

    @After
    public void tearDown() {
        Mockito.reset(mockMediator);
    }
}