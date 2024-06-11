package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    private InspirentoMediator mediatorMock;

    @Before
    public void setUp() {
        mediatorMock = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediatorMock);
    }

    @Test
    public void testMainMenuCreation() {
        assertNotNull(mainMenu);
        assertEquals(InspirentoUtilities.tokenize(UIResources.getString("menu")), mainMenu.getComponents());
        verifyNoMoreInteractions(mediatorMock);
    }

    @Test
    public void testCreateMenuWithLabel() {
        String label = "File";
        String expectedMenuLabel = "File";
        JMenu menu = mainMenu.createMenu(label);
        assertEquals(expectedMenuLabel, menu.getText());
    }

    @Test
    public void testCreateMenuWithLabelAndMnemonic() {
        String label = "Edit**";
        String expectedMenuLabel = "Edit";
        JMenu menu = mainMenu.createMenu(label);
        assertEquals(expectedMenuLabel, menu.getText());
        assertEquals('E', menu.getMnemonic());
    }

    @Test
    public void testCreateMenuWithLabelAndSubMenu() {
        String label = "Help";
        String submenuLabel = "Online Help";
        JMenu parentMenu = mainMenu.createMenu(label);
        JMenu subMenu = mainMenu.createMenu(submenuLabel);
        parentMenu.add(subMenu);
        assertEquals(submenuLabel, subMenu.getText());
    }

    @Test
    public void testCreateMenuItemWithLabelAndShortcut() {
        String label = "Open";
        String shortcut = "Ctrl + O";
        JMenuItem menuItem = mainMenu.createMenuItem("Open", label, shortcut);
        assertEquals(label, menuItem.getText());
        assertEquals(KeyStroke.getKeyStroke(shortcut), menuItem.getAccelerator());
    }

    @Test
    public void testCreateMenuItemWithLabelAndDefaultIcon() {
        String label = "Save";
        JMenuItem menuItem = mainMenu.createMenuItem(label);
        assertNull(menuItem.getIcon());
    }

    @Test
    public void testCreateMenuItemWithLabelAndCustomIcon() {
        String label = "Save";
        String iconPath = "/icons/save.png";
        JMenuItem menuItem = mainMenu.createMenuItem(label, iconPath);
        assertNotNull(menuItem.getIcon());
        verify(mediatorMock).loadIcon(eq(iconPath));
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        String label = "InvalidLabel";
        mainMenu.createMenu(label);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuItemLabel() {
        String label = "InvalidLabel";
        mainMenu.createMenuItem(label);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMissingShortcut() {
        String label = "Save";
        mainMenu.createMenuItem(label, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMissingIconPath() {
        String label = "Save";
        mainMenu.createMenuItem(label, "");
    }

    @Test
    public void testChanged() {
        mainMenu.changed();
        verifyNoInteractions(mediatorMock);
    }

    
}