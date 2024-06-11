/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        MDC.clear();
    }

    @After
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

        assertEquals(userId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(organizationId.toString(), MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        context.put(OnlineSchemaChangeContextHolder.TASK_ID, "2");
        context.put(OdcConstants.ORGANIZATION_ID, "3");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("3", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testGet() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "3");

        assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));

        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    }

    @Test
    public void testEdgeCaseTraceLongWithNull() {
        OnlineSchemaChangeContextHolder.trace(null, null, null);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testEdgeCaseTraceStringWithEmptyStrings() {
        OnlineSchemaChangeContextHolder.trace("", "", "");

        assertEquals("", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void testExceptionCaseTraceStringWithNonNumericUserId() {
        OnlineSchemaChangeContextHolder.trace("a", "2", "3");
    }

    @Test(expected = NumberFormatException.class)
    public void testExceptionCaseTraceStringWithNonNumericTaskId() {
        OnlineSchemaChangeContextHolder.trace("1", "a", "3");
    }

    @Test(expected = NumberFormatException.class)
    public void testExceptionCaseTraceStringWithNonNumericOrganizationId() {
        OnlineSchemaChangeContextHolder.trace("1", "2", "a");
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionCaseRetraceWithNullContext() {
        OnlineSchemaChangeContextHolder.retrace(null);
    }

    @Test
    public void testExceptionCaseRetraceWithEmptyContext() {
        Map<String, String> context = new HashMap<>();

        OnlineSchemaChangeContextHolder.retrace(context);

        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
    }
}