package com.rsostream.storlyze.properties;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("crud")
public class PropertiesCRUD {
    @ConfigValue(watch = true)
    private String serviceName;

    @ConfigValue(watch = true)
    private String version;

    @ConfigValue(watch = true)
    private String environment;

    @ConfigValue(watch = true)
    private String readingBaseUri;

    @ConfigValue(watch = true)
    private String deviceBaseUri;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getReadingBaseUri() {
        return readingBaseUri;
    }

    public void setReadingBaseUri(String readingBaseUri) {
        this.readingBaseUri = readingBaseUri;
    }

    public String getDeviceBaseUri() {
        return deviceBaseUri;
    }

    public void setDeviceBaseUri(String deviceBaseUri) {
        this.deviceBaseUri = deviceBaseUri;
    }
}
