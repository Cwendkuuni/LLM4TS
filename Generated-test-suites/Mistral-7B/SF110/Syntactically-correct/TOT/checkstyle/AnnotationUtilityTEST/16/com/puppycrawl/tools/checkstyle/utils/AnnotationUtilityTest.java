
package com.example.junit4.tests.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class AnnotationUtilityTest {

    @org.junit.Before
    public void setup() {
        // Initialize mock DetailAST objects
        DetailAST astMock = mock(DetailAST.class);
        DetailAST annotationHolderMock = mock(DetailAST.class);
        DetailAST annotationMock = mock(DetailAST.class);

        // Initialize mock DetailAST factory
        DetailASTFactory detailASTFactory = mock(DetailASTFactory.class);
        when(detailASTFactory.create(anyInt(), anyInt(), anyList()))
                .thenAnswer(new Answer<DetailAST>() {
                    @Override
                    public DetailAST answer(InvocationOnMock invocation) {
                        int type = (Integer) invocation.getArguments()[0];
                        int line = (Integer) invocation.getArguments()[1];
                        List<DetailAST> children = (List<DetailAST>) invocation.getArguments()[2];

                        DetailAST detailAST = AnnotationUtility.createDetailAST(type, line, children);
                        if (detailAST instanceof DetailAST) {
                            when(detailAST.getType()).thenReturn(type);
                            when(detailAST.getLineNo()).thenReturn(line);
                            when(detailAST.getChildren()).thenReturn(children);
                        }

                        return detailAST;
                    }
                });

        // Set up dependencies
        when(AnnotationUtility.createDetailASTFactory()).thenReturn(detailASTFactory);
    }

    @org.junit.Test
    public void testContainsAnnotation() {
        // Test case for containsAnnotation method with valid inputs
        when(astMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationHolderMock);
        when(annotationHolderMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationMock);
        when(annotationMock.getText()).thenReturn("MyAnnotation");

        DetailAST ast = AnnotationUtility.createDetailAST(155, 1, ImmutableList.of(astMock));
        assertTrue(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));

        // Test case for containsAnnotation method with null ast input
        when(astMock.getText()).thenReturn(null);
        assertFalse(AnnotationUtility.containsAnnotation(ast, "MyAnnotation"));

        // Test case for containsAnnotation method with null annotation input
        when(annotationMock.getText()).thenReturn(null);
        assertFalse(AnnotationUtility.containsAnnotation(ast, null));
    }

    @org.junit.Test
    public void testGetAnnotationHolder() {
        // Test case for getAnnotationHolder method with valid input
        when(astMock.getType()).thenReturn(155);
        when(astMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationHolderMock);

        DetailAST result = AnnotationUtility.getAnnotationHolder(astMock);
        assertEquals(annotationHolderMock, result);

        // Test case for getAnnotationHolder method with null ast input
        when(astMock.getType()).thenReturn(0);
        assertNull(AnnotationUtility.getAnnotationHolder(astMock));
    }

    @org.junit.Test
    public void testGetAnnotation() {
        // Test case for getAnnotation method with valid inputs
        when(astMock.getType()).thenReturn(155);
        when(astMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationHolderMock);
        when(annotationHolderMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationMock);
        when(annotationMock.getText()).thenReturn("MyAnnotation");

        DetailAST result = AnnotationUtility.getAnnotation(astMock, "MyAnnotation");
        assertEquals(annotationMock, result);

        // Test case for getAnnotation method with null ast input
        when(astMock.getType()).thenReturn(0);
        assertNull(AnnotationUtility.getAnnotation(astMock, "MyAnnotation"));

        // Test case for getAnnotation method with null annotation name input
        when(astMock.getType()).thenReturn(155);
        when(astMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationHolderMock);
        when(annotationHolderMock.getFirstChild(eq(TokenTypes.ANNOTATION))).thenReturn(annotationMock);
        when(annotationMock.getText()).thenReturn("MyAnnotation");

        String annotationName = null;
        DetailAST result2 = AnnotationUtility.getAnnotation(astMock, annotationName);
        assertNull(result2);
    }
}
