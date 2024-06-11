package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.KeyEventDispatcher.KeyEventDispatcherEvent;
import java.awt.event.ItemEvent;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MainMenuTest {

    private static final String MENU_NAME = "TestMenu";
    private static final String SUB_MENU_NAME = "TestSubMenu";
    private static final String ITEM_LABEL = "TestItem";
    private static final String ICON_PATH = "/testIcon.png";
    private static final String SHORTCUT = "Ctrl F1";
    private static final String MISSING_RESOURCE = "MissingResource";

    private MainMenu menu;
    private InspirentoMediator mediator;

    @Before
    public void setup() {
        mediator = mock(InspirentoMediator.class);
        menu = new MainMenu(mediator);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMenuCreation() {
        // Test menu creation with valid data
        String[] menuItems = {"TestMenu"};
        String[] subMenuItems = {"TestSubMenu"};
        JMenu testMenu = menu.createMenu(MENU_NAME);

        // Test menu creation with missing resource
        String missingMenu = "MissingMenu";
        try {
            menu.createMenu(missingMenu);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }

        // Test adding items to the menu
        JMenu subMenu = menu.createMenu(SUB_MENU_NAME);
        for (String item : subMenuItems) {
            if (item.equals(ITEM_LABEL)) {
                JMenuItem testItem = menu.createMenuItem(ITEM_LABEL);
                subMenu.add(testItem);
                // Test shortcut and icon setting
                menu.addShortcutAndIcon(testItem, ITEM_LABEL);
                assertEquals(KeyStroke.getKeyStroke(SHORTCUT), testItem.getAccelerator());
                assertEquals(new ImageIcon(new URL(getClass().getResource(ICON_PATH))), testItem.getIcon());
            }
        }

        // Test adding a separator to the menu
        subMenu.addSeparator();
        JMenuItem separator = subMenu.getItemCount() - 1;
        assertNotNull(separator);

        // Test menu building with valid data
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(testMenu);
        menuBar.validate();
        menuBar.doLayout();
        assertEquals(1, testMenu.getItemCount());
        assertEquals(subMenu.getItemCount(), testMenu.getComponentCount() - 1);
    }

    @Test
    public void testMenuEventHandling() {
        // Test item click event
        JMenuItem testItem = mock(JMenuItem.class);
        doNothing().when(testItem).actionPerformed(any(ActionEvent.class));
        menu.addShortcutAndIcon(testItem, ITEM_LABEL);
        menu.createMenuItem(ITEM_LABEL).addActionListener(testItem);
        testItem.addActionListener(e -> assertTrue(true));

        menu.getItem(0).doClick();
        verify(testItem).actionPerformed(any(ActionEvent.class));

        // Test checkbox item state change event
        JCheckBoxMenuItem testCheckBoxItem = mock(JCheckBoxMenuItem.class);
        doNothing().when(testCheckBoxItem).itemStateChanged(any(ItemEvent.class));
        menu.createMenuItem(ITEM_LABEL + "Checkbox").addActionListener(testCheckBoxItem);
        testCheckBoxItem.addItemListener(e -> assertTrue(true));

        testCheckBoxItem.setSelected(true);
        verify(testCheckBoxItem).itemStateChanged(any(ItemEvent.class));
    }
}