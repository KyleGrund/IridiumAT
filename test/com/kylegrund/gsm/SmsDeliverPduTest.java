package com.kylegrund.gsm;

import org.junit.Assert;

import static org.junit.Assert.*;

/**
 * Class containing unit tests for the SmsDeliverPdu class.
 */
public class SmsDeliverPduTest {
    /**
     * Tests building a PDU from a string containing PDU data octets represented in hexadecimal character paris.
     */
    @org.junit.Test
    public void testBuildPdu() {
        // test the decoding of a known PDU from an Iridium Extreme handset.
        SmsDeliverPdu toTest = new SmsDeliverPdu("0791886126090050040B986105555555F500009160711011420016CD30B90C4AD341E877BB0C9A87CD65761EA42302");
        assertEquals("Sender Phone Number", "16505555555", toTest.getSenderAddress());
        assertEquals("Sender Message", "Made it home safely :D", toTest.getMessageText());
    }
}