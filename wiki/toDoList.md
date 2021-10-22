# To do list

### Meeting 1: 16/09/2021

- [x] Set up image on PC - everyone
- [x] Change Festival to be non-blocking - Kyle
- [x] Delete Stats/ClearStats functionality - Ethan
- [x] Create new screen to select the topic (List view of categories), then start - Kyle
  - [x] Add new word lists files - Kyle
- [x] Create new "reward screen" - Zimo
- [x] Edit quiz view - Ethan
  - [x] Add "speak word again" button to quiz view - Ethan
  - [x] Add second letter of word as hint - Ethan
  - [x] Adding don't know button - Ethan
  - [x] Adding submit button - Ethan
- [x] Edit home page:
  - [x] ~~Add TTS speed settings (on home page?). With play button that reads word at speed~~ - Scrapped
- [x] ~~Change Festival to support the custom TTS speed~~ - Scrapped, will do it for the project
- [x] Change Festival to support the te reo voice - Kyle
- [x] Documentation
  - [x] Update README - Kyle
  - [x] Add /wiki folder with todo list, etc.
- [x] Final checks
  - [x] Code formatting - Ethan
  - [x] Comments - Ethan

### Meeting 2: 18/09/2021 (added on 20/09/2021)

- [x] Design
  - [x] Kēmu Kupu name - Zimo
  - [x] css? - Zimo
- [x] Error handling? - Ethan
- [x] Bugs
  - [x] Fix error in console about “-e”- Kyle
  - [x] Fix bug with csv files with commas in the word (e.g. engineering.csv) - Kyle
  - [x] Fix bug when there are less than 5 words in the file - Kyle
- [x] Refactoring
  - [x] Rename fxml file names to be consistent with java file names - Kyle
- [x] Final checks
  - [x] Unused imports - Ethan
  - [x] Short variable names - Ethan
  - [x] Add final .jar file to repo

### Meeting 3: 21/09/2021 (added on 23/09/2021)

- [x] award 0.5 points if you get the answer right the second time - Ethan
- [x] consider making the whole app multilingual (see `translations.md`)
- [x] accept double vowels instead of macrons - Kyle
- [x] give hints if your answer is almost right - Kyle
- [x] resizing and implementation of Te Reo on UI - Zimo

### Discussion on Discord: 24/09/2021

- [x] buttons to insert vowels with macrons into text field - Ethan
- [x] implemented queuing of festival words - Kyle

### Meeting 4: 25/09/2021

- [x] remove the one `System.out.println` statement - Kyle
- [x] update wiki files and upload team agreement
- [x] add "New Game" button to reward screen - Kyle
- [x] _we also planned the presentation in this meeting_

### Meeting 5: 28/09/2021

- [x] _continued planning the presentation_

### Meeting 6: 29/09/2021

- [x] _continued planning the presentation_

### Meeting 7: 30/09/2021

- [x] _continued planning the presentation_

### Meeting 8: 5/10/2021

- [x] bash file to start jar file - Kyle
- [ ] Update topic selection page - Zimo
  - [x] Show image/icon for each topic - Ethan
- [x] Design - Zimo
  - [x] buttons - Zimo
- [x] Smaller things: - Ethan
  - [x] Help button - using a popup? - Ethan
  - [x] Show more letters than just the second letter - Ethan
  - [x] Show correct spelling if user gets the word wrong - Ethan
  - [x] Show how many letters are in each word - Ethan
- [x] TTS speed ~~OR edit csv files~~ - Kyle
- [x] Convert current game into ‘practice module’ - Kyle
  - [x] Create games module - Kyle
- [x] Keep track of the answers the user provided - Kyle
- [x] persistent high score - Kyle
- [x] Create a time-based scoring system and Progress bar - Ethan
  - [x] Set a time limit for questions in game mode
- [x] Update reward screen - Zimo
  - [x] More images/stuff - Zimo
  - [x] Show which words were correct/incorrect/faulted/skipped - Kyle
- [x] Make everything multilingual
- [x] enlarge the whole app - Ethan
- [x] add new final jar for Assignment 4a

### Meeting 9: 13/10/2021

- [x] User manual
  - [x] Text - Kyle
  - [x] Proofread text - Ethan
- [x] Add back help buttons and localize the help messages - Kyle
- [x] Code attribution - Kyle
- [x] Bug: timer doesn't stop when you navigate away - Ethan
  - [x] Clear festival queue when navigate away - Ethan
- [x] UX
  - [x] Primary buttons in a different colour. - Zimo
  - [x] Add blocking for buttons like skip/repeat? Blocking for repeat/back - Ethan
  - [x] Hover and click effect for buttons - Zimo
  - [x] not obvious that you have to click the clouds - Zimo
  - [x] cursor: pointer - Zimo
  - [x] Leaf button design - Zimo
- [x] Update hint - use the “\_ _ a _ \_ \_ \_ \_ \_ b” style - Kyle
  - [x] Then remove 4 lines of the hint text - Kyle
  - [x] Fade (or flash?) colours of the hint when they answer wrong the first time - Kyle
- [x] Bigger font size? - Kyle
- [x] High-score ~~or achievements?~~ - Ethan
  - [x] ~~Reset preferences button?~~
- [x] Page transitions/animations? - Ethan
- [x] fireworks on reward page - Kyle
- [x] Images of cartoon bees in the reward screen to add more variety - Zimo
- [ ] ~~Program icon?~~
- [x] Stars above image on topic selection - Zimo
- [x] Bug: Hide time limit in practice mode - Ethan
- [x] Delay on 5th question - Ethan
- [x] Spacing between macron buttons - Kyle
- [x] reward page Medals - Zimo
- [x] reward page Fireworks only for a good score - Kyle
- [x] Css comments - Kyle
- [x] remove `while(festival...)` - Kyle
- [x] Remove “the correct spelling was….” - Kyle
- [x] address feedback from peer reviews
  - [x] “the play again button labeled as “new game” could cause confusion after finishing a practice quiz when the home menu distinguish between “new game” and “new practice quiz”. - Ethan
  - [x] macron insertion disregards the current position of the cursor x2 - Ethan
  - [x] TTS speed on the games page too? - Ethan
- [x] option to switch between languages x2 - maybe “English / Te Reo / Both”? - Kyle
- [x] words randomly being spoken x2
- [x] “Index out of bounds 5 of 5” error - Kyle
- [x] Add the rest of the words to the i18n csv file - Kyle

### Meeting 10: 18/10/2021

- [x] _we planned the competition presentation_
- [x] Bug: Medals show up in practice mode - Ethan
- [x] Typo in Huntley - Ethan
- [x] Remove System.out - Kyle
- [x] Checking commets - Kyle
- [x] Regenerate jar
- [x] User manual screenshots
- [x] save final user manual as pdf in repo
- [x] Update wiki folder

### Meeting 11: 19/10/2021

- [x] _we continued to plan the competition presentation_

---

For proof of edits, see [the original google doc](https://docs.google.com/document/d/1aegnmFmoExPen6VH4ZZ35DU_kPfrHIygAGg9Vwp8C8w/edit?usp=sharing).
