import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
    }

    @Test
    public void testPasswordEncoder_defaultConstructor() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("testPassword");
        assertTrue(passwordEncoder.matches("testPassword", encodedPassword));
    }

    @Test
    public void testObjectMapper_withSensitiveInputHandlingAndCustomOutputHandling() throws JsonMappingException {
        Internationalizable internationalizable = new Internationalizable() {
            @Override
            public String getLocale() {
                return "en_US";
            }

            @Override
            public String getValue() {
                return "Test Value";
            }
        };

        String json = objectMapper.writeValueAsString(internationalizable);
        assertEquals("{\"value\":\"Test Value\",\"locale\":\"en_US\"}", json);

        ObjectMapper.DefaultTyping typing = ObjectMapper.DefaultTyping.EVERYTHING;
        Object value = objectMapper.readValue("{\"value\":\"Test Value\",\"locale\":\"en_US\"}", typing);
        assertEquals(internationalizable.getClass(), value.getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient_whenConditionalOnMissingBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);

        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> instances = nullCloudMetadataClient.listInstances();
        assertNull(instances);

        List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
        assertNull(tenants);

        OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNull(tenant);

        List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertNull(databaseUsers);

        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());

        OBDatabaseUser sysTenantUser = nullCloudMetadataClient.getSysTenantUser("instanceId");
        assertNull(sysTenantUser);

        UnsupportedException exception = assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertEquals("getTenant not supported in non-could", exception.getMessage());

        exception = assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertEquals("CloudMetadata not supported", exception.getMessage());

        exception = assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
        assertEquals("CloudMetadata not supported", exception.getMessage());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}