import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        assertSame(customOutputSerializer, ((JacksonFactory.unsafeJsonMapper())
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer))).getSerializationConfig()
                .getActiveView());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(defaultHttpFirewall.getAllowedHttpMethods().contains("PUT"));
        assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
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
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        List<OBInstance> instances = new ArrayList<>();
        when(nullCloudMetadataClient.listInstances()).thenReturn(instances);

        assertSame(instances, nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        List<OBTenant> tenants = new ArrayList<>();
        when(nullCloudMetadataClient.listTenants(null)).thenReturn(tenants);

        assertSame(tenants, nullCloudMetadataClient.listTenants(null));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        OBTenant tenant = mock(OBTenant.class);
        when(nullCloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(tenant);

        assertSame(tenant, nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        List<OBDatabaseUser> users = new ArrayList<>();
        when(nullCloudMetadataClient.listDatabaseUsers(null, null)).thenReturn(users);

        assertSame(users, nullCloudMetadataClient.listDatabaseUsers(null, null));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        OBTenantEndpoint endpoint = mock(OBTenantEndpoint.class);
        when(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(endpoint);

        assertSame(endpoint, nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        OBDatabaseUser user = mock(OBDatabaseUser.class);
        when(nullCloudMetadataClient.getSysTenantUser(null)).thenReturn(user);

        assertSame(user, nullCloudMetadataClient.getSysTenantUser(null));
    }

    @Test
    public void testNullCloudMetadataClientUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientGetTenantUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsersUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(null, null));
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUserUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(null));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpointUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration
                .new NullCloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
    }
}