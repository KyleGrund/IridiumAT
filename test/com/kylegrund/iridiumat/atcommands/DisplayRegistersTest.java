package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.ResponseParseError;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DisplayRegistersTest {
    @org.junit.Test
    public void getCommandString() {
        assertEquals("AT%R\n", new DisplayRegisters().getCommandString());
    }

    @org.junit.Test
    public void parseResponse() {
        StringBuilder response = new StringBuilder();

        response.append("REG  DEC HEX  REG  DEC HEX\n");
        response.append("\n");
        response.append("S000 000 00H  S001 000 00H\n");
        response.append("\n");
        response.append("S002 043 2bH  S003 013 0dH\n");
        response.append("\n");
        response.append("S004 010 0aH  S005 008 08H\n");
        response.append("\n");
        response.append("S006 004 04H  S007 050 32H\n");
        response.append("\n");
        response.append("S008 004 04H  S009 006 06H\n");
        response.append("\n");
        response.append("S010 014 0eH  S011 000 00H\n");
        response.append("\n");
        response.append("S012 050 32H  S013 049 31H\n");
        response.append("\n");
        response.append("S014 170 aaH  S015 000 00H\n");
        response.append("\n");
        response.append("S016 000 00H  S017 000 00H\n");
        response.append("\n");
        response.append("S018 000 00H  S019 000 00H\n");
        response.append("\n");
        response.append("S020 000 00H  S021 048 30H\n");
        response.append("\n");
        response.append("S022 246 f6H  S023 061 3dH\n");
        response.append("\n");
        response.append("S024 000 00H  S025 005 05H\n");
        response.append("\n");
        response.append("S026 000 00H  S027 009 09H\n");
        response.append("\n");
        response.append("S028 000 00H  S029 000 00H\n");
        response.append("\n");
        response.append("S030 000 00H  S031 000 00H\n");
        response.append("\n");
        response.append("S032 017 11H  S033 019 13H\n");
        response.append("\n");
        response.append("S034 000 00H  S035 007 07H\n");
        response.append("\n");
        response.append("S036 000 00H  S037 000 00H\n");
        response.append("\n");
        response.append("S038 000 00H  S039 003 03H\n");
        response.append("\n");
        response.append("S040 104 68H  S041 000 00H\n");
        response.append("\n");
        response.append("S042 016 10H  S043 036 24H\n");
        response.append("\n");
        response.append("S044 037 25H  S045 070 46H\n");
        response.append("\n");
        response.append("S046 000 00H  S047 000 00H\n");
        response.append("\n");
        response.append("S048 000 00H  S049 001 01H\n");
        response.append("\n");
        response.append("S050 000 00H  S051 008 08H\n");
        response.append("\n");
        response.append("S052 000 00H  S053 000 00H\n");
        response.append("\n");
        response.append("S054 020 14H  S055 000 00H\n");
        response.append("\n");
        response.append("S056 000 00H  S057 000 00H\n");
        response.append("\n");
        response.append("S058 003 03H  S059 000 00H\n");
        response.append("\n");
        response.append("S060 000 00H  S061 000 00H\n");
        response.append("\n");
        response.append("S062 000 00H  S063 000 00H\n");
        response.append("\n");
        response.append("S064 000 00H  S065 000 00H\n");
        response.append("\n");
        response.append("S066 000 00H  S067 000 00H\n");
        response.append("\n");
        response.append("S068 000 00H  S069 000 00H\n");
        response.append("\n");
        response.append("S070 000 00H  S071 000 00H\n");
        response.append("\n");
        response.append("S072 000 00H  S073 000 00H\n");
        response.append("\n");
        response.append("S074 000 00H  S075 000 00H\n");
        response.append("\n");
        response.append("S076 000 00H  S077 000 00H\n");
        response.append("\n");
        response.append("S078 000 00H  S079 000 00H\n");
        response.append("\n");
        response.append("S080 000 00H  S081 000 00H\n");
        response.append("\n");
        response.append("S082 000 00H  S083 000 00H\n");
        response.append("\n");
        response.append("S084 000 00H  S085 000 00H\n");
        response.append("\n");
        response.append("S086 000 00H  S087 000 00H\n");
        response.append("\n");
        response.append("S088 000 00H  S089 000 00H\n");
        response.append("\n");
        response.append("S090 000 00H  S091 000 00H\n");
        response.append("\n");
        response.append("S092 000 00H  S093 000 00H\n");
        response.append("\n");
        response.append("S094 000 00H  S095 000 00H\n");
        response.append("\n");
        response.append("S096 000 00H  S097 000 00H\n");
        response.append("\n");
        response.append("S098 105 69H  S099 105 69H\n");
        response.append("\n");
        response.append("S100 015 0fH  S101 000 00H\n");
        response.append("\n");
        response.append("S102 030 1eH  S103 010 0aH\n");
        response.append("\n");
        response.append("S104 025 19H  S105 000 00H\n");
        response.append("\n");
        response.append("S106 010 0aH  S107 010 0aH\n");
        response.append("\n");
        response.append("S108 000 00H  S109 000 00H\n");
        response.append("\n");
        response.append("S110 000 00H  S111 000 00H\n");
        response.append("\n");
        response.append("S112 000 00H  S113 000 00H\n");
        response.append("\n");
        response.append("S114 000 00H  S115 000 00H\n");
        response.append("\n");
        response.append("S116 000 00H  S117 000 00H\n");
        response.append("\n");
        response.append("S118 000 00H  S119 000 00H\n");
        response.append("\n");
        response.append("S120 000 00H  S121 003 03H\n");
        response.append("\n");
        response.append("S122 004 04H  S123 008 08H\n");
        response.append("\n");
        response.append("S124 015 0fH  S125 010 0aH\n");
        response.append("\n");
        response.append("S126 002 02H  S127 000 00H\n");
        response.append("\n");
        response.append("S128 000 00H\n");
        response.append("\n");
        response.append("OK\n");

        Map<String, String> result = null;

        try {
            result = new DisplayRegisters().parseResponse(response.toString());
        } catch (ResponseParseError ex) {
            fail(ex.getLocalizedMessage());
        }

        assertEquals(129, result.size());
    }
}
