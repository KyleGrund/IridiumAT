package com.kylegrund.gsm;

import java.util.Arrays;

/**
 * Class implementing functionality for consuming the SMS-DELIVER messages as defined in the GSM standard 03.40.
 */
class SmsDeliverPdu {
    /**
     * The text sent from the SC to the MS in the message.
     */
    private final String messageText;

    /**
     * The address of the sender, typically a phone number.
     */
    private final String senderAddress;

    /**
     * Initializes a new instance of the SmsDeliverPdu class.
     * @param msg The String containing the text representation of the SMS-DELIVER PDU.
     */
    SmsDeliverPdu(String msg) {
        if (msg.length() % 2 != 0) {
            throw new IllegalArgumentException("PDU octet String must contain an even number of characters.");
        }

        String[] octets = new String[msg.length() / 2];
        for (int octet = 0; octet < octets.length; octet++) {
            octets[octet] = msg.substring(octet * 2, (octet * 2) + 2);
        }

        // read past the address of the service center
        int lengthOfSMSC = Integer.parseInt(octets[0], 16);
        int atOctet = lengthOfSMSC + 1;

        // move past the first octet which has unused information
        atOctet++;

        // the next octet defines the length of the sender address in half
        int addressSemiOctetCount = Integer.parseInt(octets[atOctet], 16);
        atOctet++;
        int lastSenderAddressOctet = atOctet + ((addressSemiOctetCount + 1) / 2);

        // gets the type of address
        int typeOfAddress = Integer.parseInt(octets[atOctet], 16);
        atOctet++;

        // get the actual address
        StringBuilder senderNum = new StringBuilder();
        while (atOctet <= lastSenderAddressOctet) {
            //
            senderNum.append(reverse(octets[atOctet]));
            atOctet++;
        }

        // handle last octet being unused
        if (addressSemiOctetCount % 2 == 1) {
            senderNum.deleteCharAt(senderNum.length() - 1);
        }

        this.senderAddress = senderNum.toString();

        // skip past protocol id
        atOctet++;

        // decode data coding scheme
        int dataCodingScheme = Integer.parseInt(octets[atOctet], 16);
        atOctet++;

        // skip past time stamp
        atOctet += 7;

        // gets length of message data
        int messageDataLength = Integer.parseInt(octets[atOctet], 16);
        atOctet++;

        // get message data
        this.messageText = SmsTextDecoder.decodeFromOctets(Arrays.copyOfRange(octets, atOctet, octets.length), messageDataLength);
    }

    /**
     * Gets the text sent from the SC to the MS in the message.
     * @return The text sent from the SC to the MS in the message.
     */
    String getMessageText() {
        return this.messageText;
    }

    /**
     * Gets the address of the sender, typically a phone number.
     * @return The address of the sender, typically a phone number.
     */
    String getSenderAddress() {
        return this.senderAddress;
    }

    /**
     * Reverses the String passed to the function.
     * @param toReverse The String to reverse.
     * @return The reversed String.
     */
    private String reverse(String toReverse) {
        return new StringBuilder().append(toReverse).reverse().toString();
    }
}
