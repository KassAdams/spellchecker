# Startup Instructions

Use Java 17 or higher. 

From the root of the project use the `spellcheck.sh` script which follows the directed pattern for running on the command line. 
There is a slight modification to the 1st argument where you can use `langtool`, instead of the path to a dictionary file, and it will use [LanguageTool](https://dev.languagetool.org/java-api.html).

The first time you run the `spellcheck.sh` script it will create a jar then execute it.

The provided `dictionary.txt` file and the `testfile.txt` I used are available to use at the root of the project.

From the root of the project on a command line:
- Usage
  - `./spellcheck.sh (<pathToDictFile>|langtool) <pathToTestFile>`
- Examples
  - `./spellcheck.sh dictionary.txt testfile.txt`
  - `./spellcheck.sh langtool testfile.txt`

---

# Make a spell checker!

Write a program that checks spelling. The input to the program is a dictionary file containing a list of valid words and a file containing the text to be checked.

The program should run on the command line like so:

```text
<path to your program> dictionary.txt file-to-check.txt
# output here
```

Your program should support the following features (time permitting):

- The program outputs a list of incorrectly spelled words.
- For each misspelled word, the program outputs a list of suggested words.
- The program includes the line and column number of the misspelled word.
- The program prints the misspelled word along with some surrounding context.
- The program handles proper nouns (person or place names, for example) correctly.


## Additional information

- The formatting of the output is up to you, but make it easy to understand.
- The dictionary file (`dictionary.txt` in the example above) is always a plain text file with one word per line.
- You can use the `dictionary.txt` file included in this directory as your dictionary.
- The input file (`file-to-check.txt` in the example above) is a plain text file that may contain full sentences and paragraphs.
- You should come up with your own content to run through the spell checker.
- Use any programming language, but extra credit for using Java or Kotlin.
- Feel free to fork the repo and put your code in there or create a new blank repo and put your code in there instead.
- Send us a link to your code and include instructions for how to build and run it.
- Someone from Voze will review the code with you, so be prepared to discuss your code.
