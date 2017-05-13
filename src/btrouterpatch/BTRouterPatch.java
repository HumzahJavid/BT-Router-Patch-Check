//ASSUMPTION #1 Host names in different cases are duplicates i.e. C.EXAMPLE.COM shares its hostname with c.example.com
//ASSUMPTION #2 The order of the fields in the CVS will always be 
//              hostname,ipAddress,patched,OS version and if available notes

/* Code snippet inspiration for removeRemainingInvalidRouters
 List<MyBean> deleteCandidates = new ArrayList<>();
 List<MyBean> myBeans = getThemFromSomewhere();

 // Pass 1 - collect delete candidates
 for (MyBean myBean : myBeans) {
    if (shallBeDeleted(myBean)) {
       deleteCandidates.add(myBean);
    }
 }

 // Pass 2 - delete
 for (MyBean deleteCandidate : deleteCandidates) {
    myBeans.remove(deleteCandidate);
 }

http://stackoverflow.com/questions/14231688/how-to-remove-element-from-arraylist-by-checking-its-value
*/

//tested with java version "1.8.0_121"
package btrouterpatch;

import java.util.ArrayList;
import java.util.Scanner;

public class BTRouterPatch {

    public static void main(String[] args) throws Exception {
        Scanner input;
        //contains all the routers read from CSV file
        ArrayList<String> allRouters;
        //will eventually contain the final list of routers which can be patched
        ArrayList<String> routerArrayList;

        //returns a scanner which will be used on the CSV file 
        input = getFileScanner();
        //returns an arrayList with all the routers from the CSV file 
        allRouters = createRouterArrayList(input);
        //returns an arrayList with routers that do not share a (hostname or ip address) with any other routers
        routerArrayList = removeDuplicates(allRouters);
        //returns an arrayList after removing routers which have (already been patched, or have an OS > 12)
        routerArrayList = removeRemainingInvalidRouters(routerArrayList);

        //prints a list of routers that can be patched 
        printFinalRouters(routerArrayList);
    }

    public static Scanner getFileScanner() throws Exception {
        //accepts a user input for a file name, and creates a scanner for that file
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the filename: ");
        String fileName = input.next();
        java.io.File file = new java.io.File(fileName);
        return new Scanner(file);
    }

    public static ArrayList<String> createRouterArrayList(Scanner input) {
        //creates an arraylist of strings representing routers by reading the CSV file

        //discard the first line which contains heading for the routers (by not using or returning it)
        String headingLine = input.nextLine();

        //reads the remaining lines of the csv file and stores them in an array list
        ArrayList<String> allRouters = new ArrayList<String>();

        //contains the line(router) read from the CSV file
        String currentLine = "";

        while (input.hasNext()) {
            currentLine = input.nextLine();
            //if the current line is null or empty discard it 
            if ((currentLine == null) || (currentLine.trim().length() == 0)) {
            } else {
                allRouters.add(currentLine);
            }
        }
        //end while
        return allRouters;
    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> array) {
        //This function will check the array and inspect the fields in index 0 (hostname) and 1(ip address)
        //if any routers share their hostname or ip address
        //then they will stored in a separate array for removal
        
        //contains the routers which share a hostname or ipAddress with any other router
        ArrayList<String> invalidRouters = new ArrayList<String>(array.size());
        boolean removeRouterI = false;

        for (int i = 0; i < array.size(); i++) {
            String[] fields = array.get(i).split(",");
            for (int j = i + 1; j < array.size(); j++) {
                String[] fields2 = array.get(j).split(",");
                //if router (index i) shares it hostname or ip address with router (index j)
                if ((fields[0].equalsIgnoreCase(fields2[0])) || (fields[1].equalsIgnoreCase(fields2[1]))) {
                    //mark router (index i) for removal
                    removeRouterI = true;
                    //place router(index j) in invalid routers array
                    invalidRouters.add(array.get(j));
                }
            }//end for loop 'j'

            //if the router in index i was marked for removal store it in the invalid routers array
            if (removeRouterI) {
                invalidRouters.add(array.get(i));
                removeRouterI = false;
            }
            //remove all routers collected for one iteration of i  
            array.removeAll(invalidRouters);
        }//end for loop 'i'
        return array;
    }

    public static ArrayList<String> removeRemainingInvalidRouters(ArrayList<String> routers) {
        //this function removes routers which have an OS < 12 and those which are already patched

        //will store the routers that should not be patched
        ArrayList<String> invalidRouters = new ArrayList<String>();
        for (String router : routers) {
            //split each router string into an array of 5 fields
            String[] fields = router.split(",");
            //if the router has already been patched (field[2] = yes) or the OS (field[3]) < 12
            if ((Double.parseDouble(fields[3]) < 12) || (fields[2].equalsIgnoreCase("yes"))) {
                //store the router in a separate arraylist
                invalidRouters.add(router);
            }
        }

        //remove all invalid routers from the main router array list
        routers.removeAll(invalidRouters);
        return routers;
    }

    public static void printFinalRouters(ArrayList<String> arrList) {
        //this function will print a list of routers that can patched in the required format
        String resultString = "";
        for (String router : arrList) {
            //clear the resultString, for each new Router
            resultString = "";
            String[] fields = router.split(",");

            //modifies the necassary router field to match the expected output
            for (int i = 0; i < fields.length; i++) {
                switch (i) {
                    case 0:
                        resultString += fields[i];
                        break;
                    case 1:
                        resultString += (" (" + fields[i] + "), ");
                        break;
                    case 2: //print the 'patched' field of the router in an unmodified state
                        break;
                    case 3:
                        resultString += ("OS version " + fields[i]);
                        break;
                    case 4:
                        resultString += " [" + fields[i] + "]";
                }
            }
            //prints each valid router
            System.out.println(resultString);
        }
    }
}
