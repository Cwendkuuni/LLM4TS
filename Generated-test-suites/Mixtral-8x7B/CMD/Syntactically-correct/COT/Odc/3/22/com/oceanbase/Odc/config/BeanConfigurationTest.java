import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        List<OBInstance> instances = new ArrayList<>();
        when(cloudMetadataClient.listInstances()).thenReturn(instances);
        assertEquals(instances, cloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        List<OBTenant> tenants = new ArrayList<>();
        when(cloudMetadataClient.listTenants(instanceId)).thenReturn(tenants);
        assertEquals(tenants, cloudMetadataClient.listTenants(instanceId));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        String tenantId = "tenant-01";
        OBTenant tenant = mock(OBTenant.class);
        when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenReturn(tenant);
        assertEquals(tenant, cloudMetadataClient.getTenant(instanceId, tenantId));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        String tenantId = "tenant-01";
        List<OBDatabaseUser> users = new ArrayList<>();
        when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenReturn(users);
        assertEquals(users, cloudMetadataClient.listDatabaseUsers(instanceId, tenantId));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        String tenantId = "tenant-01";
        OBTenantEndpoint endpoint = mock(OBTenantEndpoint.class);
        when(cloudMetadataClient.getTenantEndpoint(instanceId, tenantId)).thenReturn(endpoint);
        assertEquals(endpoint, cloudMetadataClient.getTenantEndpoint(instanceId, tenantId));
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        OBDatabaseUser user = mock(OBDatabaseUser.class);
        when(cloudMetadataClient.getSysTenantUser(instanceId)).thenReturn(user);
        assertEquals(user, cloudMetadataClient.getSysTenantUser(instanceId));
    }

    @Test
    public void testNullCloudMetadataClientUnsupportedException() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        String instanceId = "instance-01";
        String tenantId = "tenant-01";
        Exception exception = assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.getTenant(instanceId, tenantId));
        assertNotNull(exception);
        assertEquals("CloudMetadata not supported", exception.getMessage());
    }
}