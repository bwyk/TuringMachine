import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TuringMachine {
   public static void main(String[] args) throws FileNotFoundException {
      
      //args[0] is the CSV text file machine to acquire
      String targetFileName = args[0];
      
      //create scanner, set delimiter
      Scanner scanForLines = new Scanner(new File(targetFileName));
      scanForLines.useDelimiter(",");
      
      //skip first line
      scanForLines.nextLine();
      
      //prep the tMachine array by scanning through the file and counting lines
      int fileLineCount = 0;
      while (scanForLines.hasNext()) {
         fileLineCount++;
         scanForLines.nextLine();
      }
      /*
      //test printing
      System.out.println("Number of lines in this text file: " + fileLineCount);
      scanForLines.close();
      */
      
      //we hardcode the number of columns,
      //but the number of rows can differ from machine to machine
      
      String[][] tMachine = new String[fileLineCount][5];
      
      Scanner scanner = new Scanner(new File(targetFileName));
      scanner.useDelimiter(", ");
      
      //skip first line again
      scanner.nextLine();
      
      while (scanner.hasNext()) {
         for (int lineNum = 0; lineNum < fileLineCount; lineNum++) {
            for (int column = 0; column < 5; column++) {
               if (!scanner.hasNext()) {
                  break;
               }
               
               String fileInput = new String();
               if (column == 4) {
                  fileInput = scanner.nextLine();
                  fileInput = fileInput.substring(2);
               }
               else {
                  fileInput = scanner.next();
                 
               }
               if (fileInput != null) {
                  tMachine[lineNum][column] = fileInput;
               }
               
            }
           //scanner.nextLine();
         } 
      }
      scanner.close();
      
      //test print
      for (int lineNum = 0; lineNum < fileLineCount ; lineNum++) {
         for (int column = 0; column < 5; column++) {
            if (tMachine[lineNum][column] != null) {
               System.out.print(tMachine[lineNum][column] + " | ");
            }
         }
         System.out.println("");
      }
      System.out.println("");
      
      //args[1] is the word to test for acceptance
      String testWord = args[1];
      char[] tapeWord = new char[testWord.length()];
      for (int i = 0; i < testWord.length(); i++) {
         tapeWord[i] = testWord.charAt(i);
      }
      int tapePos = 0;
      char tapeHead = ' ';
      String currentState = tMachine[0][0];
      boolean stateSearch = true;
      boolean running = true;
      boolean accepted = false;
      int numBlanksOnTape = 0;
      int numAttempts = 0;
      String rejectReason = "invalid word";
      boolean validTransitionFound = false;
      
      while(running) {
         if (numBlanksOnTape > 0) {
            tapeHead = '_';
         }
         else {
            tapeHead = tapeWord[tapePos];
         }
         stateSearch = true;
         while(stateSearch == true) {
            validTransitionFound = false;
            for (int transitionNum = 0; transitionNum < fileLineCount; transitionNum++) {
               
               if (currentState.equals(tMachine[transitionNum][0]) == true) {
                  if (tapeHead == tMachine[transitionNum][1].charAt(0) == true)  {
                     validTransitionFound = true;
                     tapeWord[tapePos] = tMachine[transitionNum][2].charAt(0);
                     stateSearch = false;
                     if ((tMachine[transitionNum][3].charAt(0) == 'r') || (tMachine[transitionNum][3].charAt(0) == 'R') ) {
                        tapePos++;
                        if (tapePos > testWord.length() - 1) {
                           numBlanksOnTape++;
                           tapePos--; 
                        }
                     }
                     if ((tMachine[transitionNum][3].charAt(0) == 'l') || (tMachine[transitionNum][3].charAt(0) == 'L') ) {
                        if (numBlanksOnTape > 0) {
                           numBlanksOnTape--;
                        }
                        else
                        {
                           tapePos--;
                        }
                        if (tapePos < 0) {
                           //WORD: REJECTED
                           running = false;
                           accepted = false;
                           rejectReason = "tapehead moved to position -1";
                        }
                        
                     }
                     
                     currentState = tMachine[transitionNum][4];
                     break;
                  }
               }
               numAttempts++;
               
               if (numAttempts > 500) {
                  //WORD: REJECTED
                  running = false;
                  accepted = false;
                  stateSearch = false;
                  rejectReason = "invalid word through egregious transition count: " + numAttempts;
                  break;
               }
            }
            
            
         }
         
         if ((currentState.contains("Halt")) || ((currentState.contains("HALT")))) {
               running = false;
               accepted = true;
         }
        if (validTransitionFound = false) {
            running = false;
            accepted = false;
            rejectReason = "invalid word; no transition found for current state and tape character.";
        }
      }
      
      
      //end of running loop 
      if (accepted == false) {
         System.out.println("Rejected: " + testWord);
         System.out.println("Reason: " + rejectReason);
      }
      else {
         System.out.println("Accepted: " + testWord);
      }
   }
}