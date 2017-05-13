package btrouterpatch;

import java.util.Scanner;

public class BTRouterPatch {

    public static void main(String[] args) throws Exception {
        java.io.File file = new java.io.File("sample.csv");
        Scanner input = new Scanner(file);
        //use string split instead 
        boolean isDuplicateHost = false;

        boolean isDuplicateIP = false;
        String currentLine = "";
        String[] fields;
        String headingLine;
        String[] host = new String[10];
        String[] ip = new String[10];
        String[] routersToBePatched = new String[10];
        String[] routersToNotBePatched = new String[10];
        int invalidRouterCount = 0;
        int validRouterCount = 0;
        int totalRouterCount = validRouterCount + invalidRouterCount;

        headingLine = input.nextLine();
        boolean patched = false;
        boolean invalidOS = false;
        System.out.println("headingLine = " + headingLine + "\n\n");

        while (input.hasNext()) {
            currentLine = input.nextLine();
            isDuplicateHost = false;
            isDuplicateIP = false;
            patched = false;
            invalidOS = false;

            fields = currentLine.split(",");
            //System.out.println("currentLine = " + currentLine);
            for (int i = 0; i < fields.length; i++) {
                //   System.out.println(fields[i]);
            }
            System.out.println("\n");
            if (host[0] == null) {
                //this is not a duplicate... 
                isDuplicateHost = false;
                host[0] = fields[0];
                //store the first host in the host array
            } else {
                for (int i = 0; i < totalRouterCount; i++) {
                    if (fields[0].equals(host[i])) {

                        isDuplicateHost = true;
                        //dont patch 
                    }
                }

                if (ip[0] == null) {
                    //this is not a duplicate... 
                    isDuplicateIP = false;
                    ip[0] = fields[1];
                } else {
                    isDuplicateIP = true;
                    //dont patch
                }

                if (fields[2].equals("true")) {
                    patched = true;
                    //dont patch
                } else {
                    patched = false;
                }

                //check patched
                if (Double.parseDouble(fields[3]) < 12) {
                    invalidOS = true;
                    //dont patch
                } else {
                    invalidOS = false;

                }
                //check OS

                if (isDuplicateHost || isDuplicateIP || patched || invalidOS) {
                    //if any of the above result in true, then it should not be patched
                    // System.out.println("The current INVALID router being checked is \n" + currentLine);
                    routersToNotBePatched[invalidRouterCount] = currentLine;
                    invalidRouterCount++;

                } else {
                    //it should be patched
                    // System.out.println("The current VALID router: \n" + currentLine);
                    routersToBePatched[validRouterCount] = currentLine;
                    validRouterCount++;
                }

                System.out.println("Current Router = " + currentLine);
                System.out.println("dup host  " + isDuplicateHost);
                System.out.println("dup IP    " + isDuplicateIP);
                System.out.println("patched?  " + patched);
                System.out.println("OS < 12?  " + invalidOS);
                System.out.println("\n\n");
            }

            System.out.println("\n\n\n THE RESULTS: \n\n valid router: " + validRouterCount);

            for (int i = 0; i < validRouterCount; i++) {
                System.out.println("Router " + i + " = " + routersToBePatched[i]);
            }

            System.out.println(" \n\n invalid router: " + invalidRouterCount);

            for (int i = 0; i < invalidRouterCount; i++) {
                System.out.println("Router " + i + " = " + routersToNotBePatched[i]);
            }
        }

    }
}
