import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        initMocks(this);
        ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", new NullCloudMetadataClient());
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(passwordEncoder);
        Assert.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        Assert.assertNotNull(objectMapper);
        Assert.assertEquals(objectMapper.getSerializationConfig().getOutputModules().size(), 2);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(defaultHttpFirewall);
        Assert.assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(cloudMetadataClient);
        Assert.assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(cookieSerializer);
        Assert.assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertThrows(
                UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId")
        );
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        Assert.assertNotNull(endpoint);
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        Assert.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testListInstances() {
        List<OBInstance> instances = new ArrayList<>();
        when(beanConfiguration.cloudMetadataClient.listInstances()).thenReturn(instances);

        List<OBInstance> result = beanConfiguration.listInstances();

        Assert.assertEquals(instances, result);
    }

    @Test
    public void testListTenants() {
        List<OBTenant> tenants = new ArrayList<>();
        when(beanConfiguration.cloudMetadataClient.listTenants("instanceId")).thenReturn(tenants);

        List<OBTenant> result = beanConfiguration.listTenants("instanceId");

        Assert.assertEquals(tenants, result);
    }

    @Test
    public void testGetTenant() {
        OBTenant tenant = new OBTenant();
        when(beanConfiguration.cloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(tenant);

        OBTenant result = beanConfiguration.getTenant("instanceId", "tenantId");

        Assert.assertEquals(tenant, result);
    }

    @Test
    public void testListDatabaseUsers() {
        List<OBDatabaseUser> users = new ArrayList<>();
        when(beanConfiguration.cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))
                .thenReturn(users);

        List<OBDatabaseUser> result = beanConfiguration.listDatabaseUsers("instanceId", "tenantId");

        Assert.assertEquals(users, result);
    }

    @Test
    public void testGetTenantEndpoint() {
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        when(beanConfiguration.cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"))
                .thenReturn(endpoint);

        OBTenantEndpoint result = beanConfiguration.getTenantEndpoint("instanceId", "tenantId");

        Assert.assertEquals(endpoint, result);
    }

    @Test
    public void testGetSysTenantUser() {
        OBDatabaseUser user = new OBDatabaseUser();
        when(beanConfiguration.cloudMetadataClient.getSysTenantUser("instanceId")).thenReturn(user);

        OBDatabaseUser result = beanConfiguration.getSysTenantUser("instanceId");

        Assert.assertEquals(user, result);
    }
}