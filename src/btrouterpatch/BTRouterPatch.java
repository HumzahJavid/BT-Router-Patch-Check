package btrouterpatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BTRouterPatch {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the filename: ");
        String fileName = input.next();
        java.io.File file = new java.io.File(fileName);
        input = new Scanner(file);
        //prepare file for scanning

        String headingLine;
        //stores the first line which contains heading for the routers
        String currentLine = "";
        int maxRouters = 10;
        //end index for allRouters array... can be increased if array is full
        String[] allRouters = new String[maxRouters];
        //contains every router read from the CSV file
        int totalRouterCount = 0;
        //initialise variables

        headingLine = input.nextLine();
        //discard the first line which contains the name for the fields of the router 
        while (input.hasNext()) {
            currentLine = input.nextLine();
            if ((currentLine == null) || (currentLine.trim().length() == 0)) {
                //if the current line is empty or null discard it 
            } else {
                allRouters[totalRouterCount] = currentLine;
                totalRouterCount++;
                //store the line (router) in an array (of routers)
            }

            if (totalRouterCount == maxRouters) {
                String[] tempRouters = new String[(maxRouters * 2)];
                System.arraycopy(allRouters, 0, tempRouters, 0, totalRouterCount);
                allRouters = tempRouters;
                maxRouters = maxRouters * 2;
            }
            //if array gets full, copy the array to a new array with its size doubled. 
        }

        String[] noDuplicateRouters = removeDuplicates(allRouters, totalRouterCount);
        //the chain of functions returns an array with routers that do not share a hostname or ip address with any other routers
        ArrayList<String> routerArrayList = new ArrayList<String>(Arrays.asList(noDuplicateRouters));
        //converted to an array list 
        routerArrayList = removeRemainingInvalidRouters(routerArrayList);
        //removes routers which have are already patched, or have an OS > 12
        printFinalRouters(routerArrayList);
        //prints a list of routers that can be patched 
    }

    public static void printFinalRouters(ArrayList<String> arrList) {
        String resultString = "";
        for (String router : arrList) {
            resultString = "";
            ArrayList<String> fields = new ArrayList<String>(Arrays.asList(router.split(",")));

            for (int i = 0; i < fields.size(); i++) {
                switch (i) {
                    case 0:
                        resultString += fields.get(i);
                        break;
                    case 1:
                        resultString += (" (" + fields.get(i) + "), ");
                        break;
                    case 2: //do not modify the patched field of the router
                        break;
                    case 3:
                        resultString += ("OS version " + fields.get(i));
                        break;
                    case 4:
                        resultString += " [" + fields.get(i) + "]";
                } //modifies the necassary router field to match the expected output
            }
            System.out.println(resultString);
            //prints each valid router
        }
    }

    public static ArrayList<String> removeRemainingInvalidRouters(ArrayList<String> routers) {
        //this function removes routers which have an OS < 12 and those which are already patched
        ArrayList<String> invalidRouters = new ArrayList<String>();
        //will store the routers that should not be patched
        for (String router : routers) {
            String[] fields = router.split(",");
            //split each router string into an array of 5 fields
            if ((Double.parseDouble(fields[3]) < 12) || (fields[2].equalsIgnoreCase("yes"))) {
                //if the router has already been patched (field[2] = yes) or the OS (field[3]) < 12
                invalidRouters.add(router);
                //store the router in a separate arraylist
            }
        }
        routers.removeAll(invalidRouters);
        //remove all invalid routers from the main router array list
        return routers;
    }

    public static String[] removeDuplicates(String[] array, int endIndex) {
        //more appropriate name for the chain of functions which will effectively remove all routers which share a hostname or IP address
        //with any other routers.
        return buildBooleanArray(array, endIndex);
    }

    public static String[] buildBooleanArray(String[] array, int endIndex) {
        //This function will check the array and inspect the fields in index 0 (hostname) and 1(ip address)
        //if any routers share their hosntame or ip address
        //then the positions representing those routers in the boolean area will be set to true (marking it for eventual removal)
        boolean[] duplicates = new boolean[endIndex];
        for (int i = 0; i < endIndex; i++) {
            String[] fields = array[i].split(",");
            for (int j = i + 1; j < endIndex; j++) {
                String[] fields2 = array[j].split(",");
                if ((fields[0].equalsIgnoreCase(fields2[0])) || (fields[1].equalsIgnoreCase(fields2[1]))) {
                    //if hostname (field[0]) or ip (field[1]) of any Two routers are identical
                    duplicates[i] = true;
                    duplicates[j] = true;
                    //the corresponding index in the boolean array is marked as true
                }
            }
        }
        return removePadding(duplicates, array, endIndex);
    }

    public static String[] removePadding(boolean[] booleanArray, String[] routers, int endIndex) {
        //this function removes the preceeding true values(padding of trues) before the first false in the boolean array 
        //these changes are also represented in the routers array
        int numberOfPaddingElements = 0;
        for (int i = 0; i < endIndex; i++) {
            if (!booleanArray[i]) {
                //locates the first false in the boolean array 
                for (int j = i; j < endIndex; j++) {
                    booleanArray[j - i] = booleanArray[j];
                    //removes all the 'true' elements(the padding) preceeding the first 'false' 
                    routers[j - i] = routers[j];
                    //does the same for the array of routers
                }
                numberOfPaddingElements = i;
                //stores the index of the first false (in its original position). 
                i = endIndex;
                //to exit the loop
            }
        }
        endIndex -= numberOfPaddingElements;
        //end index with number of padding elements removed.
        return actualRemoval(routers, booleanArray, endIndex);
    }

    public static String[] actualRemoval(String[] duplicatesArray, boolean[] booleanArray, int endIndex) {
        boolean duplicatesFound = true;
        //required to enter the while loop 
        while (duplicatesFound) {
            duplicatesFound = false;
            //set to false, will be set to true, if a duplicate is found, thus requiring another parse of the while loop
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
        }
        //end while
        //while loop has been exited, if the array contains false values 
        //its accompanying array contains routers which do not share a hostname or IP address with any other router
        return trimRouterArray(duplicatesArray, endIndex);
    }

    public static String[] trimRouterArray(String[] routerArray, int endIndex) {
        //this function transfers the array to the necessary size (COMMENT MORE CLEARER)
        String[] newRouterArray = new String[endIndex];
        System.arraycopy(routerArray, 0, newRouterArray, 0, endIndex);
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
