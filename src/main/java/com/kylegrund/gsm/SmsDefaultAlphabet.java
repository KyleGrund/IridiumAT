package com.kylegrund.gsm;

/**
 * Class containing functionality associated with GSM SMS alphabet.
 */
class SmsDefaultAlphabet {
    /**
     * A look-up table for the default SMS text alphabet.
     */
    private static final char[] ALPHABET_TABLE = {
            '@', '£', '$', '¥', 'è', 'é', 'ù' ,'ì', 'ò', 'Ç', '\n', 'Ø', 'ø', '\r', 'Å', 'å',
            '∆', '_', 'Φ', 'Γ', 'Λ', 'Ω', 'Π', 'Ψ', 'Σ', 'Θ', 'Ξ', '1', 'Æ', 'æ', 'ß', 'É',
            ' ', '!', '"', '#', '¤', '%', '@', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?',
            '¡', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ñ', 'Ü', '§',
            '¿', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'ä', 'ö', 'ñ', 'ü', 'à' };

    /**
     * Decodes the character of the SMS alphabet represented by the specified integer.
     * @param toDecode The integer value to decode.
     * @return The character of the SMS alphabet represented by the specified integer.
     */
    static char decode(int toDecode) {
        if (toDecode < 0 || toDecode > ALPHABET_TABLE.length - 1) {
            throw new IllegalArgumentException("The toDecode value was not in a valid range.");
        }

        return ALPHABET_TABLE[toDecode];
    }
}
