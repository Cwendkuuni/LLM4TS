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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    private HttpFirewall httpFirewall;

    private CloudMetadataClient cloudMetadataClient;

    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        ReflectionTestUtils.setField(beanConfiguration, "passwordEncoder", passwordEncoder);

        objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(beanConfiguration, "objectMapper", objectMapper);

        httpFirewall = mock(DefaultHttpFirewall.class);
        ReflectionTestUtils.setField(beanConfiguration, "httpFirewall", httpFirewall);

        cloudMetadataClient = mock(CloudMetadataClient.class);
        ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);

        cookieSerializer = mock(DefaultCookieSerializer.class);
        ReflectionTestUtils.setField(beanConfiguration, "cookieSerializer", cookieSerializer);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);

        // Check if I18nOutputSerializer is added as a serializer
        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils
                .getField(mapper, "customOutputSerializer");
        assertTrue(customOutputSerializer.hasSerializer(Internationalizable.class));
    }

    @Test
    public void testHttpFirewallBean() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(firewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall) firewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertTrue(client instanceof NullCloudMetadataClient);

        NullCloudMetadataClient nullClient = (NullCloudMetadataClient) client;
        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());

        List<OBInstance> emptyList = new ArrayList<>();
        when(cloudMetadataClient.listInstances()).thenReturn(emptyList);
        assertEquals(emptyList, nullClient.listInstances());

        List<OBTenant> emptyTenantList = new ArrayList<>();
        when(cloudMetadataClient.listTenants("instanceId")).thenReturn(emptyTenantList);
        assertEquals(emptyTenantList, nullClient.listTenants("instanceId"));

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(endpoint);
        assertEquals(endpoint, nullClient.getTenantEndpoint("instanceId", "tenantId"));

        assertThrows(UnsupportedException.class, () -> nullClient.getSysTenantUser("instanceId"));
        assertThrows(UnsupportedException.class, () -> nullClient.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> nullClient.getTenant("instanceId", "tenantId"));
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertTrue(serializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", serializer.getCookieName());

        CookieSerializer newSerializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");
        assertNotSame(newSerializer, serializer);
    }
}