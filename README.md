# Assignment 3, Group 37

[![Formatting](https://github.com/SOFTENG206-2021/assignment-3-and-project-team-37/actions/workflows/lint.yml/badge.svg)](https://github.com/SOFTENG206-2021/assignment-3-and-projefct-team-37/actions/workflows/lint.yml)
[![Unit Tests](https://github.com/SOFTENG206-2021/assignment-3-and-project-team-37/actions/workflows/test.yml/badge.svg)](https://github.com/SOFTENG206-2021/assignment-3-and-projefct-team-37/actions/workflows/test.yml)

# Instructions to run the project

To start the `final.jar` file, run

```sh
chmod u+x start.sh && . ./start.sh
```

If you have any issues, see the [User_Manual.pdf](/User_Manual.pdf) file.

<br />
<br />
<br />
<br />
<br />
<br />

# Development Documentation

> ✨ The rest of this document is aimed at people who want to edit the code

## Setting up Eclipse

- Go to `Window -> Preferences -> Java -> Build Path -> User Libraries` and ensure the user library is called `JavaFX-11`
- Go to `Run -> Run Configurations -> Java Application -> Main -> Arguments` and set the VM Arguments to:
  ```
  -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml
  ```
- You may need to run `sudo apt-get install -y gtk2-engines-pixbuf libcanberra-gtk-module` to fix unclickable buttons (see piazza#63)

<details>
<summary>How to generate the <code>.jar</code> file (click to expand)</summary>

in eclipse: file -> export -> java -> JAR file -> next -> [check box on left next to repo folder] -> [click browse for save path of JAR file] -> next -> next -> [click browse next to main class] -> finish

</details>

## Format of the word lists

The files under `src/words/*` are csv files. The first row is the name of the topic. All subsequent rows are the words. Example:

```csv
TopicNameInTeReo,TopicNameInEnglish
word1TeReo,word1English
word2TeReo,word2English
...
```

If you want to show an icon or image on the Topic Selection page, create a file in `src/images` with the same name as the word list file, expcet `.jpg` instead of `.csv`

## Project architecture

This project uses JavaFX, built with SceneBuilder.

**Key info about java:**

- The entry point is `Main.java`.
- Every controller has a layout file with a similar file name (e.g. `Game.fxml` & `Game.java`)
- Every controller extends a abstract class called `UIController`, which provides methods like `navigateTo(pageName)`
- Every controller has access to a global context, via `this.context`. This gives you access to settings like TTS Speed

**Key info about the project:**

- We use Test-driven development (TDD) for some code. Unit tests are stored alongside the code files, e.g. `Answer.java` & `AnswerTest.java`
- Every commit and pull-request you make is tested by GitHub CI. This does two things:
  1. Checks the formatting of the code, using [prettier](https://prettier.io).
  2. Runs all unit tests, using JUnit.
- If files are not formatted correctly, or any unit tests fail, the commit is marked with ❌ in the GitHub UI. Commits that pass all checks have a ✅
