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
    }

    boolean readFile() {
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = br.readLine()) != null) {
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

    void doOperations() {
        loadingScreen("Executing_Operations");
        for (Instructions instruc : listOfInstructions) {
            if (instruc.valid && !instruc.emptyLine) {
                String base = "LRC";
                String farm = "";
                boolean isGoingToFarm = true;
                boolean ok = true;
                for (int i = 1; i < instruc.listOfProcedures.length - 1; i++) {
                    char persona = identify(instruc.listOfProcedures[i]);
                    if (isGoingToFarm) {
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
        if (location.contains("L") && location.contains("R")) {
            return false;
        }
        if (location.contains("C") && location.contains("R")) {
            return false;
        }
        return true;

    }

    char identify(char persona) {
        if (persona == 'L' || persona == 'l') {
            return 'L';
        } else if (persona == 'R' || persona == 'r') {
            return 'R';
        } else if (persona == 'C' || persona == 'c') {
            return 'C';
        }

        return 'N';
    }

    void writeFile() {
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

    void loadingScreen(String s) {
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

        char[] listOfProcedures;
        boolean valid = true;
        boolean emptyLine = false;
    }
}
