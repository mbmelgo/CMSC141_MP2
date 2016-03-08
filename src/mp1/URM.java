/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp1;

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
public class URM {

    private String path = "";
    private int[] urm = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean firstLine = true;
    private LinkedList<Syntax> instructions = new LinkedList<>();
    private LinkedList<int[]> output = new LinkedList<>();

    void start() {
        System.out.println("\t\tBC's URM Tracer");
        System.out.println("\tPROGRAM INSTRUCTIONS");
        System.out.println("1. URM TRACER IS NOT CASE SENSETIVE");
        System.out.println("2. URM INSTRCUTIONS SHOULD BE SPACE/TAB OR BOTH SEPARATED");
        System.out.println("3. IF THE PROGRAM ENCOUNTERS A BLANK LINE IT WILL BE SKIPPED");
        System.out.println("   AND WILL JUST PROCEED TO A LINE THAT HAS INTRUCTIONS");
        Scanner user_input = new Scanner(System.in);
        System.out.print("Enter File Path:");
        path = user_input.next();
        if (readFile()) {
            if (doOperations()) {
                writeFile();
            } else {
                System.out.println("Contains Invalid Instruction");
            }
        } 
    }

    boolean readFile() {
        try {
            
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                }
                if (firstLine) {
                    line = line.trim();
                    String[] URMContent = line.split("\\s+");
                    for (int index = 0; index < URMContent.length; index++) {
                        urm[index] = Integer.parseInt(URMContent[index]);

                    }
                    firstLine = false;
                    output.add(Arrays.copyOf(urm, urm.length));
                } else if (line.length() > 0) {
                    Syntax tmp = new Syntax();
                    line = line.trim();
                    String[] syntax = line.split("\\s+");
                    tmp.operation = syntax[0];
                    if (tmp.operation.equals("S") || tmp.operation.equals("s")
                            || tmp.operation.equals("Z") || tmp.operation.equals("z")) {
                        if (syntax.length == 2) {
                            tmp.a = Integer.parseInt(syntax[1]);
                        } else {
                            return false;
                        }
                    } else if (tmp.operation.equals("J") || tmp.operation.equals("j")) {
                        if (syntax.length == 4) {
                            tmp.a = Integer.parseInt(syntax[1]);
                            tmp.b = Integer.parseInt(syntax[2]);
                            tmp.c = Integer.parseInt(syntax[3]);
                        } else {
                            return false;
                        } 
                    } else if (tmp.operation.equals("C") || tmp.operation.equals("c")) {
                        if (syntax.length == 3) {
                            tmp.a = Integer.parseInt(syntax[1]);
                            tmp.b = Integer.parseInt(syntax[2]);
                        } else {
                            return false;
                        }
                    }else {
                        return false;
                    }
                    instructions.add(tmp);
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("File Path Incorrect!");
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
        loadingScreen("Reading_Input_From_File");
        System.out.println("File Reading Complete!");
        return true;
    }

    void writeFile() {
        loadingScreen("Writing_Output_To_File");
        FileWriter writer = null;
        try {
            File file = new File("mp1.out");
            file.createNewFile();
            writer = new FileWriter(file);

            for (int[] s : output) {
                for (int i = 0; i < s.length; i++) {
                    writer.write(s[i] + " ");
                }
                writer.write(System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(URM.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(URM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Output has been Written to File!");
    }

    boolean doOperations() {
        loadingScreen("Executing_Operations");
        for (int i = 0; i < instructions.size();) {
            if ((instructions.get(i).operation.equals("S")
                    || instructions.get(i).operation.equals("s"))
                    && instructions.get(i).a >= 0
                    && instructions.get(i).b < 0
                    && instructions.get(i).c < 0) {
                urm[(instructions.get(i).a)] = urm[(instructions.get(i).a)] + 1;
                i++;
                output.add(Arrays.copyOf(urm, urm.length));
            } else if ((instructions.get(i).operation.equals("Z")
                    || instructions.get(i).operation.equals("z"))
                    && instructions.get(i).a >= 0
                    && instructions.get(i).b < 0
                    && instructions.get(i).c < 0) {
                urm[(instructions.get(i).a)] = 0;
                i++;
                output.add(Arrays.copyOf(urm, urm.length));
            } else if ((instructions.get(i).operation.equals("J")
                    || instructions.get(i).operation.equals("j"))
                    && instructions.get(i).a >= 0
                    && instructions.get(i).b >= 0
                    && instructions.get(i).c >= 0) {
                if (urm[instructions.get(i).a] == urm[instructions.get(i).b]) {
                    i = instructions.get(i).c - 1;
                } else {
                    i++;
                    output.add(Arrays.copyOf(urm, urm.length));
                }
            } else if ((instructions.get(i).operation.equals("C")
                    || instructions.get(i).operation.equals("c"))
                    && instructions.get(i).a >= 0
                    && instructions.get(i).b >= 0
                    && instructions.get(i).c < 0) {
                    urm[(instructions.get(i).b)] = urm[(instructions.get(i).a)];
                    i++;
                    output.add(Arrays.copyOf(urm, urm.length));                
            }else if (i > instructions.size()) {
                break;
            } else {
                return false;
            }

        }
        System.out.println("Instructions Executed!");
        return true;
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
                Logger.getLogger(URM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("");
    }

    private class Syntax {

        String operation = " ";
        int a = -1;
        int b = -1;
        int c = -1;

    }
    
    
}
