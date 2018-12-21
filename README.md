# Anime Title Matcher (ATM)

The purpose of this tool is to find the best matching Anime for a given string.

The result is always exactly one Anime with some metadata in JSON-format. Anime Indexer recognizes
multiple languages as far as Anime titles go.

The data source is [Manami-Project's anime-offline-database](https://github.com/manami-project/anime-offline-database),
which gets downloaded into the execution directory if it does not exist.

Successful output is **always in JSON**. The only exceptions are error messages and the help text.

Matching is done in steps:

1. Search for exact title (case-insensitive)
2. Search for exact alternative title (=synonym, also case-insensitive)
3. Search for partial title (case-insensitive)
4. Match by highest score of titles and synonyms combined using the [Jaro-Winkler Distance](https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance).

## Example

```
$ java -jar atm.jar 未来日記
{
  "type": "OVA",
  "title": "Mirai Nikki",
  "picture": "https://img7.anidb.net/pics/anime/60575.jpg",
  "thumbnail": "https://img7.anidb.net/pics/anime/thumbs/50x65/60575.jpg-thumb.jpg",
  "episodes": 1,
  "sources": [
    "https://anidb.net/a7432",
    "https://anilist.co/anime/8460",
    "https://animenewsnetwork.com/encyclopedia/anime.php?id\u003d11373",
    "https://kitsu.io/anime/5344"
  ],
  "relations": [
    "https://anidb.net/a8395",
    "https://anilist.co/anime/10620",
    "https://animenewsnetwork.com/encyclopedia/anime.php?id\u003d12896",
    "https://kitsu.io/anime/6266"
  ],
  "synonyms": [
    "Future Diary",
    "Gelecek Günlüğü",
    "Mirai Nikki OVA",
    "The Future Diary OVA",
    "未来日記"
  ]
}
```

## Requirements

ATM requires a local Java Runtime installation with version 1.8 or higher. An active internet connection is required
for the first execution to download the Anime database file.

## Building and Installation

Required tools:

* Maven 3.3.9
* JDK 1.8+

Clone the git repository and run maven in the root directory.

`$ mvn clean install`

This creates the **atm.jar** file in the _target_ directory. This is the main executable as
shown in the examples.

For convenience you can create an alias in your .bashrc or profile:

`$ alias atm="java -jar /path/of/jar/atm.jar"`

## Usage and Options

`$ java -jar atm.jar [OPTIONS] <arg>`

### Example Usages

```
# regular usage
$ java -jar atm.jar Grisaia
$ java -jar atm.jar -f myfile.txt

# read from stdin
$ cat myfile.txt | java -jar atm.jar -
```

| Flag | Description |
|---|---|
| - | Empty flag. Read from stdin. |
| -h, --help | Prints the help text. |
| -f, --file FILE | Parses a file containing Anime titles separated by newline. |

## Usage as Library

The console application is very slow as of now due to the re-creation of the database on each execution,
but Anime Indexer can also be used as a Java library. For a code example see `ConsoleApplication.java`.

## License

Copyright 2018 go-ive

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.