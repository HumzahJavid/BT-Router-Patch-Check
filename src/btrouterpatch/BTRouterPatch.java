package btrouterpatch;

import java.util.Scanner;

public class BTRouterPatch {

    public static void main(String[] args) throws Exception {
        java.io.File file = new java.io.File("sample.csv");
        Scanner input = new Scanner(file);
        boolean isDuplicateHost = false;
        boolean isDuplicateIP = false;
        boolean patched = false;
        boolean invalidOS = false;
        String currentLine = "";
        String[] fields;
        String headingLine;
        String[] routersToBePatched = new String[10];
        String[] routersToNotBePatched = new String[10];
        int invalidRouterCount = 0;
        int validRouterCount = 0;
        int totalRouterCount = 0;

        headingLine = input.nextLine();
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
            if (fields[2].equals("yes")) {
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
                System.out.println("The router below is invalid");
                //if any of the above result in true, then it should not be patched
                // System.out.println("The current INVALID router being checked is \n" + currentLine);
                routersToNotBePatched[invalidRouterCount] = currentLine;
                invalidRouterCount++;

            } else {

                System.out.println("The router below is valid");
                //it should be patched
                // System.out.println("The current VALID router: \n" + currentLine);
                routersToBePatched[validRouterCount] = currentLine;
                validRouterCount++;
            }
            totalRouterCount++;
            System.out.println(totalRouterCount);

            System.out.println("Current Router = " + currentLine);
            System.out.println("dup host  " + isDuplicateHost);
            System.out.println("dup IP    " + isDuplicateIP);
            System.out.println("patched?  " + patched);
            System.out.println("OS < 12?  " + invalidOS);
            System.out.println("\n\n");
        }

        System.out.println(
                "\n\n\n THE RESULTS: \n\n valid router: " + validRouterCount);

        for (int i = 0;
                i < validRouterCount;
                i++) {
            System.out.println("Router " + i + " = " + routersToBePatched[i]);
        }

        System.out.println(
                " \n\n invalid router: " + invalidRouterCount);

        for (int i = 0;
                i < invalidRouterCount;
                i++) {
            System.out.println("Router " + i + " = " + routersToNotBePatched[i]);
        }
        System.out.println("\n\n\n");
        checkDuplicateHostName(routersToBePatched, validRouterCount);
    }

    public static String[] actualRemoval(String[] duplicatesArray, boolean[] booleanArray, int endIndex) {
        boolean duplicatesFound = false;
        System.out.println("after removing the padding /before removing the duplicates");
        printArrays(duplicatesArray, booleanArray, endIndex);
        for (int i = 0; i < endIndex; i++) {
            if (booleanArray[i]) {
                duplicatesFound = true;
                for (int j = i + 1; j < endIndex; j++) {
                    booleanArray[j - 1] = booleanArray[j];
                    duplicatesArray[j - 1] = duplicatesArray[j];
                }
                endIndex = endIndex - 1;
            }
        }
        if (duplicatesFound) {
            System.out.println("temp pass may need to remove more dupes");
            printArrays(duplicatesArray, booleanArray, endIndex);
            actualRemoval(duplicatesArray, booleanArray, endIndex);
        } else {
            System.out.println("After removing the duplicates");
            //printArrays(duplicatesArray, booleanArray, endIndex);
        }
        System.out.println("Actual Removal endinded = " + endIndex);
        return trimRouterArray(duplicatesArray, endIndex);
    }

    public static void checkDuplicateHostName(String[] array, int endIndex) {

        //This function will recursively check for duplicates, until all are exterminated,
        //Will do a final pass, if no duplicates are found (using the flag). Then the final list of routers has been determined
        int checkingIndex = 0;
        boolean duplicatesFound = false;
        for (int i = 0; i < endIndex; i++) {
            System.out.println(array[i]);
        }

        System.out.println("\n\n");
        for (int i = 0; i < endIndex; i++) {
            checkingIndex = i + 1;

            String[] fields = array[i].split(",");
            for (int j = checkingIndex; j < endIndex; j++) {
                String[] fields2 = array[j].split(",");

                if (fields[0].equalsIgnoreCase(fields2[0])) {
                    System.out.println(fields[0] + " and " + fields2[0] + " are the same");
                    System.out.println("I = " + i + " j = " + j);

                    for (int k = j + 1; k < endIndex; k++) {
                        array[k - 1] = array[k];
                    }

                    for (int k = i + 1; k < endIndex; k++) {
                        array[k - 1] = array[k];
                    }

                    endIndex = endIndex - 2;
                    duplicatesFound = true;
                }

                System.out.println("\n\n"); //checking intermediary array
                if (fields[1].equalsIgnoreCase(fields2[1])) {

                    System.out.println(fields[1] + " and " + fields2[1] + " are the same");

                    System.out.println("I = " + i + " j = " + j);

                    for (int k = j + 1; k < endIndex; k++) {
                        array[k - 1] = array[k];
                    }

                    for (int k = i + 1; k < endIndex; k++) {
                        array[k - 1] = array[k];
                    }
                    endIndex = endIndex - 2;
                    duplicatesFound = true;
                }
            }
        }
        System.out.println("final array");
        for (int i = 0; i < endIndex; i++) {
            System.out.println(array[i]);
        }

        if (duplicatesFound) {
            checkDuplicateHostName(array, endIndex);
        } else {
            //do nothing
        }
    }

    public static String[] trimRouterArray(String[] routerArray, int endIndex) {
        System.out.println("Trim router was called");
        System.out.println("FINAL END INDEX = " + endIndex);
        String[] newRouterArray = new String[endIndex];
        System.arraycopy(routerArray, 0, newRouterArray, 0, endIndex);
        /* for (int i = 0; i < newRouterArray.length; i++) {
            System.out.println(newRouterArray[i]);
        }*/
        return newRouterArray;
    }

    public static void printArrays(String[] sArray, boolean[] bArray, int endIndex) {
        for (int i = 0; i < endIndex; i++) {
            System.out.println(sArray[i]);
        }
        for (int i = 0; i < endIndex; i++) {
            System.out.println(bArray[i]);
        }
        System.out.println("\n\n");
    }

}
