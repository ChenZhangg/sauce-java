package com.saucelabs.remotedriver;

import org.hamcrest.core.IsNot;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SauceSessionTest {

    private SauceSession sauceSession;

    @Before
    public void setUp()
    {
        sauceSession = new SauceSession();
    }
    @Test
    public void seleniumServer_notSet_returnsNull()
    {
        //TODO is this okay to be like a property,
        //or should it be a getSeleniumServer()
        assertNull(sauceSession.seleniumServer);
    }

    @Test
    public void defaultConstructor_instantiated_setsConcreteDriverManager()
    {
        assertThat(sauceSession.getDriverManager(), instanceOf(ConcreteRemoteDriverManager.class));
    }

    @Test
    public void getInstance_serverNotSet_setsSauceSeleniumServer() throws MalformedURLException {
        RemoteDriverInterface fakeRemoteDriver = mock(RemoteDriverInterface.class);
        sauceSession = new SauceSession(fakeRemoteDriver);
        sauceSession.getInstanceOld();
        String expectedServer = "https://ondemand.saucelabs.com/wd/hub";
        assertEquals(expectedServer, sauceSession.seleniumServer);
    }
    @Test
    public void browserNameCapability_isSetToCorrectKey() throws MalformedURLException {
        RemoteDriverInterface fakeRemoteDriver = mock(RemoteDriverInterface.class);
        sauceSession = new SauceSession(fakeRemoteDriver);
        sauceSession.getInstanceOld();
        String expectedBrowserCapabilityKey = "browserName";
        String actualBrowser = sauceSession.capabilities.getCapability(expectedBrowserCapabilityKey).toString();
        assertThat(actualBrowser, IsNot.not(""));
    }
    @Test
    @Ignore("The problem with this approach is that you need to know which method" +
        "to call to get the desired behavior. However, if we move the logic out from" +
        "the getCapabilities() method into another method, this test will no longer work." +
        "So this test is implementation specific. The test above is not.")
    public void getCapabilities_browserNameCapSet_validKeyExists2() {
        sauceSession.getCapabilities();
        String expectedBrowserCapabilityKey = "browserName";
        String actualBrowser = sauceSession.capabilities.getCapability(expectedBrowserCapabilityKey).toString();
        assertThat(actualBrowser, IsNot.not(""));
    }
    @Test
    public void noSauceOptionsSet_whenCreated_defaultIsChrome()
    {
        String actualBrowser = sauceSession.getCapabilities().getBrowserName();
        assertThat(actualBrowser, IsEqualIgnoringCase.equalToIgnoringCase("Chrome"));
    }
    @Test
    public void noSauceOptionsSet_whenCreated_defaultIsWindows10() {
        String actualOs = sauceSession.getCapabilities().getPlatform().name();
        assertThat(actualOs, IsEqualIgnoringCase.equalToIgnoringCase("win10"));
    }
    @Test
    public void noSauceOptionsSet_whenCreated_latestBrowserVersion()
    {
        MutableCapabilities caps = new SauceSession().getCapabilities();
        String actualOperatingSystem = caps.getCapability("browserVersion").toString();
        assertThat(actualOperatingSystem, IsEqualIgnoringCase.equalToIgnoringCase("latest"));
    }
    @Test
    public void sauceOptions_defaultConfiguration_setsSauceOptions()
    {
        sauceSession.getCapabilities();
        boolean hasAccessKey = sauceSession.getSauceOptionsCapability().asMap().containsKey("accessKey");
        assertTrue(hasAccessKey);
    }

    @Test
    public void defaultSafari_notSet_returnsLatestVersion()
    {
        RemoteDriverInterface fakeRemoteDriver = mock(RemoteDriverInterface.class);
        sauceSession = new SauceSession(fakeRemoteDriver);
        sauceSession.withSafari();

        String safariVersion = sauceSession.getCapabilities().getVersion();

        assertThat(safariVersion, IsEqualIgnoringCase.equalToIgnoringCase("latest"));
    }
    @Test
    public void withSafari_browserName_setToSafari()
    {
        sauceSession.withSafari();
        String actualBrowserName = sauceSession.getCapabilities().getBrowserName();
        assertThat(actualBrowserName, IsEqualIgnoringCase.equalToIgnoringCase("safari"));
    }
    @Test
    public void withSafari_versionChangedFromDefault_returnsCorrectVersion()
    {
        sauceSession.withSafari().withBrowserVersion(SafariVersion.elevenDotOne);
        String safariVersion = sauceSession.getCapabilities().getVersion();
        assertThat(safariVersion, IsEqualIgnoringCase.equalToIgnoringCase("11.1"));
    }
}