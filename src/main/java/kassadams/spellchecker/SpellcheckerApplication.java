package kassadams.spellchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpellcheckerApplication {

  public static void main(String[] args) {
    // SpringApplication.run(SpellcheckerApplication.class, args);
    Instant start = Instant.now();

    if (args == null || args.length != 2) {
      System.out.println("Usage: ( <pathToDictFile> | langtool ) <pathToTestFile>");
      return;
    }

    if (args[0].equals("langtool")) {
      useLanguageTool(args[1]);
    } else {
      useSpellChecker(args);
    }

    Instant end = Instant.now();
    System.out.printf("time: %d milliseconds\n", Duration.between(start, end).toMillis());
  }

  private static void useSpellChecker(String[] args) {
    File dictFile = new File(args[0]);
    File checkFile = new File(args[1]);
    String currentLine;
    HashSet<String> dictHashSet;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dictFile))) {
      // XXX: better initial capacity logic?
      dictHashSet = new HashSet<>(1900000, 1);
      // dont check for the only one word per line as per readme
      currentLine = bufferedReader.readLine();
      while (currentLine != null) {
        // could toLowerCase with add if dictionary file wasn't all lowercase
        dictHashSet.add(currentLine);
        currentLine = bufferedReader.readLine();
      }
    } catch (Exception e) {
      System.err.println(e.getLocalizedMessage());
      return;
    }

    try (LineNumberReader lnReader = new LineNumberReader(new FileReader(checkFile))) {
      String currentToken;
      while ((currentLine = lnReader.readLine()) != null) {
        // delimiter does not include '
        StringTokenizer tokenizer = new StringTokenizer(currentLine, "`~!@#$%^&*()-_=+[]{}\\|;:\",<.>/?\t ");
        while (tokenizer.hasMoreTokens()) {
          currentToken = tokenizer.nextToken();
          if (!dictHashSet.contains(currentToken.toLowerCase())) {
            // is a number
            if (currentToken.matches("[0-9]+")) {
              continue;
            }
            // contains non letter character like ' or a number?
            if (currentToken.matches("[^a-zA-Z]")) {
              continue;
            }
            // first letter capitalized, assume is pronoun & ignore
            // (will be issue for unknown words at beginning of sentence)
            if (currentToken.matches("^[A-Z].*")) {
              continue;
            }
            System.out.printf("Ln %d, Col %d: { %s }\n", lnReader.getLineNumber(), 0,
                currentToken);
            System.out.println("Contents of line: " + currentLine);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private static void useLanguageTool(String testFilePath) {
    File checkFile = new File(testFilePath);
    String currentLine;
    JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-US"));
    // langTool.getAllActiveRules().forEach(rul -> {
    // if (rul.getCategory().getId().equals(CategoryIds.CASING)) {
    // System.out.println(rul.getDescription());
    // }
    // });
    // Tools.selectRules(langTool, null, null, null, null, false, false);
    try (LineNumberReader lnReader = new LineNumberReader(new FileReader(checkFile))) {
      while ((currentLine = lnReader.readLine()) != null) {
        List<RuleMatch> matches = langTool.check(currentLine);
        for (RuleMatch match : matches) {
          System.out.printf("\nLine %d, Characters %d-%d\n",
              lnReader.getLineNumber(), match.getFromPosSentence(), match.getToPosSentence());
          System.out.println("Message: " + match.getMessage());
          System.out.println("Suggested correction(s): " +
              match.getSuggestedReplacements());
          System.out.printf("Sentence: %s\n", match.getSentence().getText());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }
}
