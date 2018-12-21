package com.github.goive.atm;

import com.github.goive.atm.dto.FileParseResult;
import com.github.goive.atm.dto.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleApplication {

    private static final String CMD_LINE_SYNTAX = "atm [OPTIONS] <arg>";
    private static final String FILE_LONG_FLAG = "file";
    private static final String HELP_LONG_FLAG = "help";

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        Options options = createOptions();

        try {
            System.out.println(parse(options, args));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println(getUsageText(options));
            System.exit(1);
        }
    }

    static String getUsageText(Options options) {
        HelpFormatter formatter = new HelpFormatter();

        StringWriter writer = new StringWriter();

        formatter.printHelp(new PrintWriter(writer),
                HelpFormatter.DEFAULT_WIDTH,
                CMD_LINE_SYNTAX,
                null,
                options,
                HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD,
                null);

        return writer.toString();
    }

    static Options createOptions() {
        Options options = new Options();

        options.addOption("f", FILE_LONG_FLAG, true, "File with titles separated by new line.");
        options.addOption("h", HELP_LONG_FLAG, false, "Print this help.");

        return options;
    }

    static String parse(Options options, String... args) throws Exception {
        DefaultParser defaultParser = new DefaultParser();
        CommandLine commandLine = defaultParser.parse(options, args);

        if (commandLine.getOptions().length == 0 && commandLine.getArgList().isEmpty()) {
            return getUsageText(options);
        }

        if (commandLine.getArgList().contains("-")) {
            return parseStdIn(System.in);
        }

        if (commandLine.hasOption(FILE_LONG_FLAG)) {
            return parseFile(commandLine.getOptionValue(FILE_LONG_FLAG).trim());
        }

        if (commandLine.hasOption(HELP_LONG_FLAG)) {
            return getUsageText(options);
        }

        if (commandLine.getArgList().size() >= 1) {
            return parseSingle(StringUtils.join(commandLine.getArgList(), " "));
        }

        throw new IllegalStateException("No operation possible with arguments " + commandLine.getArgList());
    }

    private static String parseSingle(String title) {
        TitleParser titleParser = new TitleParser(new Database());
        Item item = titleParser.parse(title);

        return gson.toJson(item);
    }

    private static String parseFile(String fileName) throws Exception {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        TitleParser titleParser = new TitleParser(new Database());
        List<FileParseResult> fileParseResults = br.lines()
                .filter(StringUtils::isNotBlank)
                .map(line -> {
                    Item item = titleParser.parse(line);

                    return new FileParseResult(line, item.getTitle());
                })
                .collect(Collectors.toList());

        return gson.toJson(fileParseResults);
    }

    static String parseStdIn(InputStream in) {
        TitleParser titleParser = new TitleParser(new Database());
        List<String> result = new ArrayList<>();

        try (Scanner input = new Scanner(in)) {
            String line;
            while (!(line = input.nextLine()).isEmpty()) {
                result.add(line);
            }
        } catch (NoSuchElementException e) {
            // this produces extra output that I don't want
        }

        List<FileParseResult> parsedResults = result.stream()
                .filter(StringUtils::isNotBlank)
                .map(titleLine -> {
                    Item item = titleParser.parse(titleLine);

                    return new FileParseResult(titleLine, item.getTitle());
                })
                .collect(Collectors.toList());

        return gson.toJson(parsedResults);
    }

}
