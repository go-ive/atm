package com.github.goive.atm;

import com.github.goive.atm.dto.Item;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.util.Optional;

public class TitleParser {

    private Database database;

    public TitleParser(Database database) {
        this.database = database;
    }

    public Item parse(String titleCandidate) {
        String cleanedTitleCandidate = cleanup(titleCandidate);

        Optional<Item> first = searchCaseInsensitiveTitle(cleanedTitleCandidate);
        if (first.isPresent()) {
            return first.get();
        }

        Optional<Item> second = searchCaseInsensitiveSynonym(cleanedTitleCandidate);
        if (second.isPresent()) {
            return second.get();
        }

        Optional<Item> third = searchPartialTitle(cleanedTitleCandidate.toLowerCase());
        if (third.isPresent()) {
            return third.get();
        }

        return searchByBestMatchingTitle(cleanedTitleCandidate.toLowerCase());
    }

    String cleanup(String titleCandidate) {
        return titleCandidate
                .replaceAll("\\[.*?\\]", "")
                .replaceAll("\\(.*?\\)", "")
                .trim();
    }

    private Item searchByBestMatchingTitle(String titleCandidate) {
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        database.getItems().parallelStream()
                .forEach(item -> {
                    item.setSynonymScore(0.0);
                    item.setTitleScore(0.0);
                    item.setTitleScore(jaroWinklerDistance.apply(item.getTitle().toLowerCase(), titleCandidate));
                    item.getSynonyms().forEach(syn -> {
                        Double distanceOfSynonym = jaroWinklerDistance.apply(syn.toLowerCase(), titleCandidate);
                        if (distanceOfSynonym > item.getSynonymScore()) {
                            item.setSynonymScore(distanceOfSynonym);
                        }
                    });
                });

        Item highestScoreItem = database.getItems().stream()
                .min((o2, o1) -> Double.compare(
                        o1.getTitleScore() > o1.getSynonymScore() ? o1.getTitleScore() : o1.getSynonymScore(),
                        o2.getTitleScore() > o2.getSynonymScore() ? o2.getTitleScore() : o2.getSynonymScore()))
                .orElseThrow(() -> new IllegalStateException("A database of " + database.getItems().size() +
                        " items returned no results after a sort operation. This should never happen."));

        if (highestScoreItem.getTitleScore() > highestScoreItem.getSynonymScore()) {
            return highestScoreItem;
        } else {
            return database.getItems().stream()
                    .filter(item -> item.getTitleScore() >= highestScoreItem.getSynonymScore())
                    .findFirst()
                    .orElse(highestScoreItem);
        }
    }

    private Optional<Item> searchPartialTitle(String titleCandidate) {
        return database.getItems().parallelStream()
                .filter(item -> item.getTitle().toLowerCase().contains(titleCandidate))
                .findFirst();
    }

    private Optional<Item> searchCaseInsensitiveSynonym(String titleCandidate) {
        return database.getItems().parallelStream()
                .filter(item -> item.getSynonyms().stream()
                        .anyMatch(syn -> syn.equalsIgnoreCase(titleCandidate)))
                .findFirst();
    }

    private Optional<Item> searchCaseInsensitiveTitle(String titleCandidate) {
        return database.getItems().parallelStream()
                .filter(item -> titleCandidate.equalsIgnoreCase(item.getTitle())).findFirst();
    }

}
