package com.github.goive.atm;

import com.github.goive.atm.dto.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TitleParserTest {

    private static TitleParser titleParser;

    @BeforeAll
    static void setup() {
        titleParser = new TitleParser(new Database());
    }

    @ParameterizedTest
    @CsvSource({
            "[project-gxs] UFO Princess Valkyrie Collection [10bit DVD 480p], UFO Princess Valkyrie Collection",
            "[bonkai77] Mirai Nikki + OVA (ENHANCED) [1080p] [DUAL-AUDIO] [x265] [HEVC] [AAC] [10bit], Mirai Nikki + OVA"
    })
    void shouldCleanupTags(String input, String expected) {
        String result = titleParser.cleanup(input);

        assertEquals(expected, result);
    }

    /**
     * This test uses the delimiter 'µ', because occasionally commas and quotes appear in titles. When I find one Anime
     * title that contains this character, I will change it to something else.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/anime.csv", delimiter = 'µ')
    void shouldCheckListOfAnime(String directoryName, String expectedTitle, String message) {
        Item parse = titleParser.parse(directoryName);

        assertEquals(expectedTitle, parse.getTitle(), message);
    }

}
