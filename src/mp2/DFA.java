/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Melgo
 */
public class DFA {

    private String path = "";
    private LinkedList<Instructions> listOfInstructions = new LinkedList<>();
    private LinkedList<String> outPut = new LinkedList<>();
    //private LinkedList<Instructions> outPuts = new LinkedList<>();
    //private int ctr = 0;
    
    void start() {
        System.out.println("\t\tBinibining Comsci Problems (Deterministic Finite Automata)");
        System.out.println("\tPROGRAM INSTRUCTIONS");
        System.out.println("1. DFA TRACER IS NOT CASE SENSETIVE");
        System.out.println("2. DFA INSTRCUTIONS CAN BE SPACE/TAB OR BOTH SEPARATED");
        System.out.println("3. IF THE PROGRAM ENCOUNTERS A BLANK LINE IT WILL BE SKIPPED");
        System.out.println("   AND WILL JUST PROCEED TO A LINE THAT HAS INTRUCTIONS");
        Scanner user_input = new Scanner(System.in);
        System.out.print("Enter File Path:");
        path = user_input.next();
        if (readFile()) {
            doOperations();
            writeFile();
        }
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(DFA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean readFile() {//READ INPUT FILE
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = br.readLine()) != null) {
                /*
                    IF LINE IS EMPTY, JUST ADD IT IN THE LIST OF INSTRUCTIONS
                    TO MAINTAIN LINE EQUALITY FOR OUTPUT PURPOSES
                */
                if (line.trim().equals("")) {
                    Instructions instruc = new Instructions();
                    instruc.emptyLine = true;
                    instruc.valid = false;
                    listOfInstructions.add(instruc);
                    continue;
                }
                line = line.trim();
                line = Arrays.toString(line.split("\\s+"));
                Instructions instruc = new Instructions();
                instruc.listOfProcedures = new char[line.toCharArray().length];
                /*
                    DISTINGUISH THE VALIDITY OF THE LINE
                */
                for (int i = 1; i < line.toCharArray().length - 1; i++) {
                    if (line.toCharArray()[i] == 'L' || line.toCharArray()[i] == 'l'
                            || line.toCharArray()[i] == 'R' || line.toCharArray()[i] == 'r'
                            || line.toCharArray()[i] == 'C' || line.toCharArray()[i] == 'c'
                            || line.toCharArray()[i] == 'N' || line.toCharArray()[i] == 'n') {
                        instruc.listOfProcedures[i] = line.toCharArray()[i];
                        instruc.valid = true;
                    } else {
                        instruc.valid = false;
                    }
                }
                listOfInstructions.add(instruc);
            }

            br.close();

        } catch (IOException e) {
            System.out.println("File Path Incorrect!");
            return false;
        }
        loadingScreen("Reading_Input_From_File");
        System.out.println("File Reading Complete!");
        return true;
    }

    void doOperations() {//EXECUTE OPERATIONS
        loadingScreen("Executing_Operations");
        for (Instructions instruc : listOfInstructions) {
            /*
                CHECKS IF LINE IS VALID AND NOT AN EMPTY LINE
            */
            if (instruc.valid && !instruc.emptyLine) {
                String base = "LRC";
                String farm = "";
                boolean isGoingToFarm = true;
                boolean ok = true;
                for (int i = 1; i < instruc.listOfProcedures.length - 1; i++) {
                    char persona = identify(instruc.listOfProcedures[i]);
                    /*
                        CHECKS IF IS GOING TO FARM OR FROM FARM, THIS ACTS AS 
                        WHERE THE MAN IS SINCE MY PROGRAM DOES NOT HAVE A VARIABLE MAN
                    */
                    if (isGoingToFarm) {
                        /*
                            CHECKS THE PERSONA THAT WILL BE RIDING THE BOAT
                            AND IF IT IS NOTHING THEN IT JUST SKIPS
                            IF PERSONA IS EITHER LION OR RABBIT OR CARROT
                            IT CHECKS IF THE PERSONA IS IN THE PLACE AND CAN BE 
                            TAKEN TO RIDE THE BOAT
                            IF THE PERSONA IS PRESENT THEN THE PROGRAM WILL REMOVE
                            IT FROM IT'S CURRENT PLACE AND ADD IT TO THE PLACE 
                            IT WILL BE TAKEN 
                        */
                        if (persona != 'N') {
                            String tmp = ""+persona;
                            if(!base.contains(tmp)){
                                 ok = false;
                                break;
                            }
                            base = base.replace(persona, ' ');
                            base = base.trim();
                            farm = farm + persona;
                        }
                        if (!checkStatus(base)) {
                            ok = false;
                            break;
                        }
                        isGoingToFarm = false;
                    } else {
                        if (persona != 'N') {
                            String tmp = ""+persona;
                            if(!farm.contains(tmp)){
                                 ok = false;
                                break;
                            }
                            farm = farm.replace(persona, ' ');
                            farm = farm.trim();
                            base = base + persona;
                        }
                        if (!checkStatus(farm)) {
                            ok = false;
                            break;
                        }
                        isGoingToFarm = true;
                    }
                }
                if (ok) {
                    /*
                        IF IT WAS SUCCESSFUL IN EXECUTING THE OPERATIONS
                        CHECK IF THE BASE IS EMPTY AND THE FARM HAS ALL 3 
                        PERSONA, LION, RABBIT AND CARROT,ALSO CHECKS IF 
                        THE MAN IS IN THE FARM
                    */
                    if(base.isEmpty()&&farm.contains("L")
                            &&farm.contains("R")&&farm.contains("C")
                            && !isGoingToFarm){
                        outPut.add("OK");
                        //outPuts.add(instruc);
                        //ctr++;
                    }
                    else
                        outPut.add("NG");
                } else {
                    outPut.add("NG");
                }
            } else if (!instruc.valid && instruc.emptyLine){
                outPut.add("This Line is an Empty Line");
                }
            else {
                outPut.add("This Line Contains Invalid Instructions");
            }
        }
        System.out.println("Instructions Executed!");
    }

    boolean checkStatus(String location) {
        /*
            FUNCTION THAT CHECKS THE NON ALLOWED MOVES
        */
        if (location.contains("L") && location.contains("R")) {
            return false;
        }
        if (location.contains("C") && location.contains("R")) {
            return false;
        }
        return true;

    }

    char identify(char persona) {
        /*
            FUNCTION THAT CHECKS THE PERSONA
        */
        if (persona == 'L' || persona == 'l') {
            return 'L';
        } else if (persona == 'R' || persona == 'r') {
            return 'R';
        } else if (persona == 'C' || persona == 'c') {
            return 'C';
        }

        return 'N';
    }

    void writeFile() {// WRITES OUTPUT TO FILE
        loadingScreen("Writing_Output_To_File");
        FileWriter writer = null;
        try {
            File file = new File("mp2.out");
            file.createNewFile();
            writer = new FileWriter(file);
            for (String s : outPut) {
                writer.write(s);
                writer.write(System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DFA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(DFA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Output has been Written to File!");
//        System.out.println(ctr);
//        for (Instructions i:outPuts) {
//            System.out.println(Arrays.toString(i.listOfProcedures));
//        }
    }

    void loadingScreen(String s) {//SIMULATE A LOADING SCREEN FOR FUN PURPOSES 
        String load = s + "[                    ]";
        int j = 13;
        for (int i = 0; i < 200; i++) {
            try {
                if (i % 10 == 0) {
                    load = load.replaceFirst(" ", "=");
                    j++;
                    System.out.print("\r" + load);
                    System.out.flush();
                }
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(DFA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("");
    }

    private class Instructions {
        /*
            A CLASS THAT HOLDS THE INSTRUCTIONS/PROCEDURES ON THE INPUTS
        */
        char[] listOfProcedures;
        boolean valid = true;
        boolean emptyLine = false;
    }
}
