package com.kylegrund.gsm;

/**
 * Class containing functionality to decode text from an SMS representation.
 */
class SmsTextDecoder {
    /**
     * Decodes a String of text from an array of hexadecimal pairs representing octets.
     * @param octets An array of Strings of hexadecimal pairs representing data octets.
     * @param messageDataLength The length of the message to be decoded.
     * @return The decoded text message.
     */
    static String decodeFromOctets(String[] octets, int messageDataLength) {
        return decodeChars(unpackSeptetsFromOctets(convertToIntegers(octets, messageDataLength), messageDataLength));
    }

    /**
     * Unpacks septets packed into octets.
     * @param source An array of integers representing the packed data.
     * @param count The number of septets to unpack.
     * @return The unpacked septets.
     */
    private static int[] unpackSeptetsFromOctets(int[] source, int count) {
        if (source.length < (int) Math.ceil((count * 7.0f) / 8.0f)) {
            throw new IllegalArgumentException("Insufficient data in source to decode all septets.");
        }

        // holds unpacked data
        int[] toRet = new int[count];

        // the source character currently being unpacked
        int sourceChar = 0;

        // the source bit currently being unpacked
        int sourceBit = 0;

        // the destination character being unpacked into
        int destChar = 0;

        // the destination bit being unpacked into
        int destBit = 0;

        // unpacking loop executes for each bit to be unpaccked
        while (destChar < count) {
            // perform the actual unpacking mapping the source bit to the destination bit
            toRet[destChar] |= (((source[sourceChar] >> sourceBit) & 0x1) << destBit);

            // move to next source bit
            if (++sourceBit > 7) {
                sourceBit = 0;
                sourceChar++;
            }

            // move to next destination bit
            if (++destBit > 6) {
                destBit = 0;
                destChar++;
            }
        }

        return toRet;
    }

    /**
     * Decodes text from an array of septets.
     * @param toDecode The septets to decode.
     * @return Decoded text.
     */
    private static String decodeChars(int[] toDecode) {
        StringBuilder toRet = new StringBuilder();
        for (int data : toDecode) {
            toRet.append(SmsDefaultAlphabet.decode(data));
        }
        return toRet.toString();
    }

    /**
     * Converts an array of Strings in hexadecimal format to an array of integers.
     * @param toConvert The hexadecimal data Strings to convert.
     * @param convertCount The number of strings to convert.
     * @return The converted array of integers.
     */
    private static int[] convertToIntegers(String[] toConvert, int convertCount) {
        int atOctet = 0;
        int byteCount = (int) Math.ceil((convertCount * 7.0f) / 8.0f);
        int[] data = new int[byteCount];
        for (int i = 0; i < byteCount; i++) {
            data[i] = Integer.parseInt(toConvert[atOctet], 16);
            atOctet++;
        }
        return data;
    }
}
