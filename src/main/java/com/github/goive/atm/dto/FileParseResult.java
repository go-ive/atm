package com.github.goive.atm.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FileParseResult {

    private String originalLine;
    private String matchedTitle;

    public FileParseResult(String originalLine, String matchedTitle) {
        this.originalLine = originalLine;
        this.matchedTitle = matchedTitle;
    }

    public String getOriginalLine() {
        return originalLine;
    }

    public void setOriginalLine(String originalLine) {
        this.originalLine = originalLine;
    }

    public String getMatchedTitle() {
        return matchedTitle;
    }

    public void setMatchedTitle(String matchedTitle) {
        this.matchedTitle = matchedTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("originalLine", originalLine)
                .append("matchedTitle", matchedTitle)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FileParseResult that = (FileParseResult) o;

        return new EqualsBuilder()
                .append(matchedTitle, that.matchedTitle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(matchedTitle)
                .toHashCode();
    }
}
