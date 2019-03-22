package com.saucelabs.common;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;


public class JavaScriptInvokerFactoryTest extends BaseTest{
    @Test
    public void shouldReturnObjectForTheManager()
    {
        JavaScriptInvokerManager jsManager = JavaScriptInvokerFactory.create(mockWebDriver);
        assertThat(jsManager, instanceOf(JavaScriptInvokerManager.class));
    }
}