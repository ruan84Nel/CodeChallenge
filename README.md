# Scrabble Top-3 (Pure Java, No Dependencies)

Small console app that reads a word list from a local file, scores each word using classic Scrabble letter values, and prints the **Top 3 highest-scoring words**. No Gradle/Maven, no external libs, no imports beyond the JDK.

## How it works

- Reads words from `input2.txt` (one word per line).
- For each word:
  - Sums letter points using a simple score table:
    - **1**: A E I L N O R S T U
    - **2**: D G
    - **3**: B C M P
    - **4**: F H V W Y
    - **5**: K
    - **8**: J X
    - **10**: Q Z
- Maintains an in-memory **Top-3** (word, score) while streaming the file.
- Writes full results to `output.txt` as `word,score`.
- Prints the **Top 3** to the console at the end.

## Files

- `Main.java` — program entry point when reading from a local file (`input2.txt`).
- `Main2.java` — alternative entry point that reads the word list directly from the JSON URL.
- `input2.txt` — word list (one word per line) used by `Main.java`.
- `output.txt` — generated, contains all `word,score` pairs.


## Getting `input2.txt` from the provided JSON

I received a gitHub Repo with a list of words.
They had special characters, and due to using Pure Java,
and that there was a time limit, had to not waste too much time on
special characters, so did the following.

1. Paste into my Notepad++ text editor.
2. Removed `[` and `]`.
3. Replaced `","` with a newline.
4. Removed remaining `"` double quote characters.

This resulted in one word per line.

> Tip: Many editors can do this with Find/Replace. Keep it simple.

## Running

### Option 1: Read from local file (`Main.java`)

```bash
# compile
javac Main.java

# run
java Main

#OR
Using Intellij, just click the play button on the main.java class (org/example/Main.java)
```

### Option 2: Read directly from JSON URL ('Main2.java')

```bash
# compile
javac Main2.java

# run
java Main2

#OR
Using Intellij, just click the play button on the main.java class (org/example/Main2.java)
```

### Expected console output (example)

```
Hello and welcome!

Top 3 highest-scoring words:
CHARLESSS,50
CHARLES,46
BOB,45
```

…and `output.txt` will contain all words and their scores, CSV-style (easy to open in Excel).

## Key methods (what to look for in the code)

### `scoreOf(char ch)`
Uppercases letters without imports and returns the letter’s score by scanning your score table:

```java
static final Object[][] BUILDSCORESHEET = {
    {1, "AEILNORSTU"}, {2, "DG"}, {3, "BCMP"},
    {4, "FHVWY"}, {5, "K"}, {8, "JX"}, {10, "QZ"}
};
```

Non-letters (digits/symbols) return `0`.

### `updateTop3(String word, int score)`
Keeps a fixed Top-3:

- Fills first 3 slots.
- Afterwards, finds the current lowest and replaces it if a new score is higher.

O(1) memory, trivial logic.

## Assumptions

- Input is one word per line in `input2.txt`.
- Case-insensitive scoring (`a` == `A`).
- Non-letters are ignored (score `0`).
- Ties are kept in the order they appear (no special tie-breaker).

## Changing tie-breakers (optional)

If you want to prefer shorter words (or alphabetical) when scores tie, modify `updateTop3` to compare `(score, length, word)` before replacing. Keep it simple if you don’t need it.

## Complexity

You read each word once.

- For each word, you add up its letter scores. The time this takes grows with the word’s length.

- Keep only the best 3 scores as you go, which takes constant, tiny work per word.

So overall: if there are N words and each is about L letters long, the work is roughly N × L steps. One pass through the file, almost no extra memory.

## Quick sanity check (manual)

- `hello` → 4(H)+1(E)+1(L)+1(L)+1(O) = **8**
- `quiz` → 10(Q)+1(U)+1(I)+10(Z) = **22**
