package com.lts.swing.combobox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ItemEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    // ... test cases from Experts 1 and 2

    @Test
    public void testItemEvent() {
        when(simpleModel.getSelectedItem()).thenReturn("Test");
        simpleComboBox.addItemListener(e -> {});
        simpleComboBox.setSelectedItem("Test");
        verify(simpleModel, times(1)).setSelectedValue("Test");
    }
}