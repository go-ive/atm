package com.github.goive.atm;

import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleApplicationTest {

    private static final String TEST_FILE_NAME = "src/test/resources/testfileForFileFlag.txt";
    private static final String TESTFILE_FOR_FILE_FLAG_EXPECTED_RESULT = "[\n" +
            "  {\n" +
            "    \"originalLine\": \"Venus Project\",\n" +
            "    \"matchedTitle\": \"Venus Project - Climax -\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"Violet Evergarden\",\n" +
            "    \"matchedTitle\": \"Violet Evergarden\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"タービレ OVA\",\n" +
            "    \"matchedTitle\": \"Nodame Cantabile OVA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"chou tokkyuu\",\n" +
            "    \"matchedTitle\": \"Chou Tokkyuu Hikarian\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"TSUKAI SALLY\",\n" +
            "    \"matchedTitle\": \"Mahou Tsukai Sally\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"angel-tachi no private LESSON\",\n" +
            "    \"matchedTitle\": \"Stringendo: Angel-tachi no Private Lesson\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"Kamisama Kazoku\",\n" +
            "    \"matchedTitle\": \"Kamisama Kazoku\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"Kamisama Kiss\",\n" +
            "    \"matchedTitle\": \"Kamisama Hajimemashita\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"Kamisama_no_Memochou\",\n" +
            "    \"matchedTitle\": \"Kamisama no Memochou\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"originalLine\": \"Kämpfer\",\n" +
            "    \"matchedTitle\": \"Kämpfer\"\n" +
            "  }\n" +
            "]";

    @ParameterizedTest
    @CsvSource(value = {
            "Grisaia, Single unmapped argument",
            "Grisaia|no|Kajitsu, Multiple unmapped arguments",
    })
    void shouldParseWithUnmappedArguments(String arguments, String message) throws Exception {
        assertEquals("{\n" +
                "  \"type\": \"TV\",\n" +
                "  \"title\": \"Grisaia no Kajitsu\",\n" +
                "  \"picture\": \"https://cdn.myanimelist.net/images/anime/10/73591.jpg\",\n" +
                "  \"thumbnail\": \"https://cdn.myanimelist.net/images/anime/10/73591t.jpg\",\n" +
                "  \"episodes\": 13,\n" +
                "  \"sources\": [\n" +
                "    \"https://anidb.net/a9749\",\n" +
                "    \"https://anilist.co/anime/17729\",\n" +
                "    \"https://animenewsnetwork.com/encyclopedia/anime.php?id\\u003d16140\",\n" +
                "    \"https://kitsu.io/anime/7982\",\n" +
                "    \"https://myanimelist.net/anime/17729\"\n" +
                "  ],\n" +
                "  \"relations\": [\n" +
                "    \"https://anidb.net/a11008\",\n" +
                "    \"https://anilist.co/anime/21005\",\n" +
                "    \"https://anilist.co/anime/21010\",\n" +
                "    \"https://animenewsnetwork.com/encyclopedia/anime.php?id\\u003d16672\",\n" +
                "    \"https://kitsu.io/anime/10150\",\n" +
                "    \"https://kitsu.io/anime/10154\",\n" +
                "    \"https://myanimelist.net/anime/29093\",\n" +
                "    \"https://myanimelist.net/anime/29101\"\n" +
                "  ],\n" +
                "  \"synonyms\": [\n" +
                "    \"GriKaji\",\n" +
                "    \"grisaia\",\n" +
                "    \"Le Fruit De La Grisaia\",\n" +
                "    \"Le Fruit de la Grisaia\",\n" +
                "    \"The Fruit of Grisaia\",\n" +
                "    \"グリザイアの果実\",\n" +
                "    \"グリザイアの果実 -LE FRUIT DE LA GRISAIA-\"\n" +
                "  ]\n" +
                "}", ConsoleApplication.parse(ConsoleApplication.createOptions(), arguments.split("\\|")), message);
    }

    @ParameterizedTest
    @CsvSource({
            "-f=" + TEST_FILE_NAME + ", Short flag with =",
            "-f|" + TEST_FILE_NAME + ", Short flag without =",
            "--file=" + TEST_FILE_NAME + ", Long flag"
    })
    void shouldParseFile(String arguments, String message) throws Exception {
        assertEquals(TESTFILE_FOR_FILE_FLAG_EXPECTED_RESULT,
                ConsoleApplication.parse(ConsoleApplication.createOptions(), arguments.split("\\|")), message);
    }

    @ParameterizedTest
    @CsvSource({
            "-h, short flag",
            "--help, long flag"
    })
    void shouldPrintHelp(String args, String message) throws Exception {
        Options options = ConsoleApplication.createOptions();

        assertEquals(ConsoleApplication.getUsageText(options),
                ConsoleApplication.parse(options, args),
                message);
    }

    @Test
    void shouldReadFromInputStreamLikeFile() throws Exception {
        byte[] stdinData = StringUtils.join(Files
                .lines(Paths.get(TEST_FILE_NAME)).collect(Collectors.toList()), "\n")
                .getBytes();

        assertEquals(TESTFILE_FOR_FILE_FLAG_EXPECTED_RESULT,
                ConsoleApplication.parseStdIn(new ByteArrayInputStream(stdinData)));
    }

    @Test
    void shouldHandleNoInput() throws Exception {
        Options options = ConsoleApplication.createOptions();

        assertEquals(ConsoleApplication.getUsageText(options),
                ConsoleApplication.parse(options, null),
                "Print help text if no arguments provided.");
    }

}
