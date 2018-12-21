package com.github.goive.atm;

import com.github.goive.atm.dto.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    private Database database;

    @BeforeEach
    public void init() {
        database = new Database();
    }

    @Test
    public void shouldFillDatabase() {
        assertFalse(database.getItems().isEmpty());

        Optional<Item> grisaia = database.getItems().stream().filter(item -> "Grisaia no Kajitsu".equals(item.getTitle())).findFirst();

        assertTrue(grisaia.isPresent(), "Grisaia is missing from list.");

        Item item = grisaia.get();

        assertAll("data",
                () -> assertEquals("Grisaia no Kajitsu", item.getTitle(), "Title"),
                () -> assertEquals("TV", item.getType(), "Type"),
                () -> assertEquals("https://cdn.myanimelist.net/images/anime/10/73591.jpg",
                        item.getPicture(), "Picture"),
                () -> assertEquals("https://cdn.myanimelist.net/images/anime/10/73591t.jpg",
                        item.getThumbnail(), "Thumbnail"),
                () -> assertEquals(13, item.getEpisodes(), "Episodes"),
                () -> assertEquals(item.getSynonyms(), Arrays.asList(
                        "GriKaji",
                        "grisaia",
                        "Le Fruit De La Grisaia",
                        "Le Fruit de la Grisaia",
                        "The Fruit of Grisaia",
                        "グリザイアの果実",
                        "グリザイアの果実 -LE FRUIT DE LA GRISAIA-"), "Synonyms"),
                () -> assertEquals(item.getRelations(), Arrays.asList(
                        "https://anidb.net/a11008",
                        "https://anilist.co/anime/21005",
                        "https://anilist.co/anime/21010",
                        "https://animenewsnetwork.com/encyclopedia/anime.php?id=16672",
                        "https://kitsu.io/anime/10150",
                        "https://kitsu.io/anime/10154",
                        "https://myanimelist.net/anime/29093",
                        "https://myanimelist.net/anime/29101"), "Relations"),
                () -> assertEquals(item.getSources(), Arrays.asList(
                        "https://anidb.net/a9749",
                        "https://anilist.co/anime/17729",
                        "https://animenewsnetwork.com/encyclopedia/anime.php?id=16140",
                        "https://kitsu.io/anime/7982",
                        "https://myanimelist.net/anime/17729"), "Sources")
        );
    }

}
