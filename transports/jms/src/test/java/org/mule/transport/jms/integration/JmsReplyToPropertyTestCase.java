package org.mule.transport.jms.integration;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

public class JmsReplyToPropertyTestCase extends AbstractJmsFunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "integration/jms-replyto-property.xml";
    }

    public void testReplyTo() throws Exception
    {
        Properties props = new Properties();
        props.put("JMSReplyTo", "middle2");
        dispatchMessage(DEFAULT_INPUT_MESSAGE, props);

        // Check that the property-based routing worked
        MuleMessage output = receiveMessage();
        
        // Check that the property is still on the outbound message
        assertEquals("queue://middle2", output.getProperty("JMSReplyTo").toString());
        
        // Check that the reply message was generated
        MuleClient client = new MuleClient();
        output = client.request("middle2", 2000);
        assertNotNull(output);
        assertEquals(DEFAULT_OUTPUT_MESSAGE, output.getPayload());
    }
}
