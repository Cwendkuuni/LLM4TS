import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", new NullCloudMetadataClient());
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils.getField(objectMapper, "customOutputSerializer");
        assertNotNull(customOutputSerializer);
        assertEquals(1, customOutputSerializer.getSerializers().size());

        I18nOutputSerializer i18nOutputSerializer = (I18nOutputSerializer) customOutputSerializer.getSerializers().get(0);
        assertNotNull(i18nOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) defaultHttpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> obInstances = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        when(sensitivePropertyHandler.encrypt(anyString())).thenReturn("encrypted");
        when(nullCloudMetadataClient.listInstances()).thenReturn(obInstances);

        List<OBInstance> instances = nullCloudMetadataClient.listInstances();
        assertEquals(0, instances.size());

        List<OBTenant> obTenants = new ArrayList<>();
        when(nullCloudMetadataClient.listTenants(anyString())).thenReturn(obTenants);

        List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
        assertEquals(0, tenants.size());

        OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
        when(nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(obTenantEndpoint);

        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);

        List<OBDatabaseUser> obDatabaseUsers = new ArrayList<>();
        when(nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(obDatabaseUsers);

        List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertEquals(0, databaseUsers.size());

        when(nullCloudMetadataClient.getSysTenantUser(anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}