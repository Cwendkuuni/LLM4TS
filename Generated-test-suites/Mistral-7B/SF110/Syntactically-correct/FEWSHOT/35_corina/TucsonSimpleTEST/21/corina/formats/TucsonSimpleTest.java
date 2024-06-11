package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Year year1 = new Year(1);
        Year year1001 = new Year(1001);
        Year year8001 = new Year(8001);
        Year year9000 = new Year(9000);

        Sample sample = new Sample();
        sample.setRange(new Range(year1, year1));

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range expectedRange1 = sample.getRange();
        Range expectedRange2 = new Range(year1001, year1001);
        Range expectedRange3 = new Range(year8001, year8001);
        Range expectedRange4 = new Range(year1, year1000);

        assertEquals(expectedRange1, tucsonSimple.computeRange(sample));
        sample.setRange(new Range(year1000, year1001));
        assertEquals(expectedRange2, tucsonSimple.computeRange(sample));
        sample.setRange(new Range(year8000, year8001));
        assertEquals(expectedRange3, tucsonSimple.computeRange(sample));
        sample.setRange(new Range(new Year(1), year9999));
        assertEquals(expectedRange4, tucsonSimple.computeRange(sample));
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.setMeta(new HashMap<String, String>() {{
            put("id", "123456");
        }});

        TucsonSimple tucsonSimple = new TucsonSimple();

        String expectedCode = "123456";
        assertEquals(expectedCode, tucsonSimple.make6digitCode(sample));

        sample.setMeta(null);

        String expectedCodeNull = "000000";
        assertEquals(expectedCodeNull, tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveData() throws IOException {
        I18n i18nMock = mock(I18n.class);
        StringUtils stringUtilsMock = mock(StringUtils.class);

        Sample sample = new Sample();
        sample.setData(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        sample.setCount(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        sample.setMeta(new HashMap<String, String>() {{
            put("id", "123456");
        }});
        sample.setRange(new Range(new Year(1), new Year(10)));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(outputStream);

        TucsonSimple tucsonSimple = new TucsonSimple();

        tucsonSimple.saveData(sample, writer);

        String expectedOutput = "123456 1 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0\n" +
                "123456 2 2.0 2.0 2.0 2.0 2.0 2.0 2.0 2.0 2.0\n" +
                "123456 3 3.0 3.0 3.0 3.0 3.0 3.0 3.0 3.0 3.0\n" +
                "123456 4 4.0 4.0 4.0 4.0 4.0 4.0 4.0 4.0 4.0\n" +
                "123456 5 5.0 5.0 5.0 5.0 5.0 5.0 5.0 5.0 5.0\n" +
                "123456 6 6.0 6.0 6.0 6.0 6.0 6.0 6.0 6.0 6.0\n" +
                "123456 7 7.0 7.0 7.0 7.0 7.0 7.0 7.0 7.0 7.0\n" +
                "123456 8 8.0 8.0 8.0 8.0 8.0 8.0 8.0 8.0 8.0\n" +
                "123456 9 9.0 9.0 9.0 9.0 9.0 9.0 9.0 9.0 9.0\n" +
                "123456 10 10.0 10.0 10.0 10.0 10.0 10.0 10.0 10.0 10.0\n";

        String actualOutput = outputStream.toString();

        assertEquals(expectedOutput, actualOutput);

        verify(i18nMock).getText(eq("format.tucsonsimple"));
        verifyNoMoreInteractions(i18nMock);
        verify(stringUtilsMock).leftPad(anyString(), eq(4));
        verifyNoMoreInteractions(stringUtilsMock);
    }

    @Test
    public void testSaveDataWithProcessedData() throws IOException {
        I18n i18nMock = mock(I18n.class);
        StringUtils stringUtilsMock = mock(StringUtils.class);

        Sample sample = new Sample();
        sample.setData(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 999.0, 7.0, 8.0, 9.0, 10.0 });
        sample.setCount(new Double[] { 1.0, 2.0, 3.0, 4.0, 5.0, null, 7.0, 8.0, 9.0, 10.0 });
        sample.setMeta(new HashMap<String, String>() {{
            put("id", "123456");
        }});
        sample.setRange(new Range(new Year(1), new Year(10)));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(outputStream);

        TucsonSimple tucsonSimple = new TucsonSimple();

        tucsonSimple.saveData(sample, writer);

        String expectedOutput = "123456 1 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0 1.0\n" +
                "123456 2 2.0 2.0 2.0 2.0 2.0 2.0 2.0 2.0 2.0\n" +
                "123456 3 3.0 3.0 3.0 3.0 3.0 3.0 3.0 3.0 3.0\n" +
                "123456 4 4.0 4.0 4.0 4.0 4.0 4.0 4.0 4.0 4.0\n" +
                "123456 5 5.0 5.0 5.0 5.0 5.0 5.0 5.0 5.0 5.0\n" +
                "123456 6 999.0 null null null null null null null null null null\n" +
                "123456 7 7.0 7.0 7.0 7.0 7.0 7.0 7.0 7.0 7.0\n" +
                "123456 8 8.0 8.0 8.0 8.0 8.0 8.0 8.0 8.0 8.0\n" +
                "123456 9 9.0 9.0 9.0 9.0 9.0 9.0 9.0 9.0 9.0\n" +
                "123456 10 10.0 10.0 10.0 10.0 10.0 10.0 10.0 10.0 10.0\n";

        String actualOutput = outputStream.toString();

        assertEquals(expectedOutput, actualOutput);

        verify(i18nMock).getText(eq("format.tucsonsimple"));
        verifyNoMoreInteractions(i18nMock);
        verify(stringUtilsMock).leftPad(anyString(), eq(4));
        verify(stringUtilsMock).leftPad(anyString(), eq(3));
        verify(stringUtilsMock).leftPad("", eq(6));
        verifyNoMoreInteractions(stringUtilsMock);

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
        ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(writer).write(codeCaptor.capture(arg -> arg.startsWith("123456 ")));
        verify(writer).write(anyString());
        verify(writer).write(eq("9990   "));

        for (int i = 0; i < 9; i++) {
            verify(writer).write(stringUtilsMock.captureCall(call -> call.leftPad(argThat(s -> s.startsWith("123456 ")), eq(4))));
        }

        verify(writer).write(stringUtilsMock.captureCall(call -> call.leftPad("", eq(3))));
        verify(writer).write(stringUtilsMock.captureCall(call -> call.leftPad("", eq(6))));

        verify(writer).write(stringUtilsMock.captureCall(call -> call.leftPad("", eq(4))));
        verify(writer).newLine();
        verify(writer).write(stringUtilsMock.captureCall(call -> call.leftPad("", eq(4))));
        verify(writer).newLine();

        verify(writer).write(anyString());
        verify(writer).write(anyString());
        verify(writer).newLine();
    }
}