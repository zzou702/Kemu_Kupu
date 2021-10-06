# Assignment 3, Group 37

[![CI](https://github.com/SOFTENG206-2021/assignment-3-and-project-team-37/actions/workflows/ci.yml/badge.svg)](https://github.com/SOFTENG206-2021/assignment-3-and-projefct-team-37/actions/workflows/ci.yml)

## Instructions to Run the project

To start the `final.jar` file, run

```sh
. start.sh
# or
java -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar final.jar

```

## Set up instructions

> This is only if you want to edit to code

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
