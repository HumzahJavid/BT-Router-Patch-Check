package btrouterpatch;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author humzah
 */
public class BTRouterPatchTest {

    public BTRouterPatchTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class BTRouterPatch.
     */
    @Test
    public void testMain() throws Exception {
        //main function cannot be tested as it is dependant on user input
        System.out.println("main");
    }

    /**
     * Test of getFileScanner method, of class BTRouterPatch.
     */
    @Test
    public void testGetFileScanner() throws Exception {
        //get file scanner cannot be accurately tested as it is dependant on user input
        System.out.println("getFileScanner");
        Scanner expResult = null;
        Scanner result = BTRouterPatch.getFileScanner();
        System.out.println("result = " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of createRouterArrayList method, of class BTRouterPatch.
     */
    @Test
    public void testCreateRouterArrayList() throws Exception {
        System.out.println("createRouterArrayList");
        //Scanner input = new Scanner(System.in);
        java.io.File file = new java.io.File("sample.csv");
        Scanner input = new Scanner(file);
        ArrayList<String> expResult = new ArrayList<>(Arrays.asList(
                "A.example.COM,1.1.1.1,NO,11,Faulty fans",
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "C.EXAMPLE.COM,1.1.1.3,no,12.1,",
                "d.example.com,1.1.1.4,yes,14,",
                "c.example.com,1.1.1.5,no,12,Case a bit loose",
                "e.example.com,1.1.1.6,no,12.3,",
                "f.example.com,1.1.1.7,No,12.200,",
                "g.example.com,1.1.1.6,no,15.0,Guarded by  sharks with lasers on their heads"
        ));
        ArrayList<String> result = BTRouterPatch.createRouterArrayList(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeDuplicates method, of class BTRouterPatch.
     */
    @Test
    public void testRemoveDuplicates() {
        System.out.println("removeDuplicates");
        ArrayList<String> array = new ArrayList<>(Arrays.asList(
                "A.example.COM,1.1.1.1,NO,11,Faulty fans",
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "C.EXAMPLE.COM,1.1.1.3,no,12.1,",
                "d.example.com,1.1.1.4,yes,14,",
                "c.example.com,1.1.1.5,no,12,Case a bit loose",
                "e.example.com,1.1.1.6,no,12.3,",
                "f.example.com,1.1.1.7,No,12.200,",
                "g.example.com,1.1.1.6,no,15.0,Guarded by  sharks with lasers on their heads"
        ));
        ArrayList<String> expResult = new ArrayList<>(Arrays.asList(
                "A.example.COM,1.1.1.1,NO,11,Faulty fans",
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "d.example.com,1.1.1.4,yes,14,",
                "f.example.com,1.1.1.7,No,12.200,"
        ));
        ArrayList<String> result = BTRouterPatch.removeDuplicates(array);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeRemainingInvalidRouters method, of class BTRouterPatch.
     */
    @Test
    public void testRemoveRemainingInvalidRouters() {
        System.out.println("removeRemainingInvalidRouters");
        ArrayList<String> routers = new ArrayList<>(Arrays.asList(
                "A.example.COM,1.1.1.1,NO,11,Faulty fans",
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "C.EXAMPLE.COM,1.1.1.3,no,12.1,",
                "d.example.com,1.1.1.4,yes,14,",
                "c.example.com,1.1.1.5,no,12,Case a bit loose",
                "e.example.com,1.1.1.6,no,12.3,",
                "f.example.com,1.1.1.7,No,12.200,",
                "g.example.com,1.1.1.6,no,15.0,Guarded by  sharks with lasers on their heads"
        ));
        ArrayList<String> expResult = new ArrayList<>(Arrays.asList(
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "C.EXAMPLE.COM,1.1.1.3,no,12.1,",
                "c.example.com,1.1.1.5,no,12,Case a bit loose",
                "e.example.com,1.1.1.6,no,12.3,",
                "f.example.com,1.1.1.7,No,12.200,",
                "g.example.com,1.1.1.6,no,15.0,Guarded by  sharks with lasers on their heads"
        ));
        ArrayList<String> result = BTRouterPatch.removeRemainingInvalidRouters(routers);
        assertEquals(expResult, result);
    }

    /**
     * Test of printFinalRouters method, of class BTRouterPatch.
     */
    @Test
    public void testPrintFinalRouters() {
        System.out.println("printFinalRouters");
        ArrayList<String> arrList = new ArrayList<>(Arrays.asList(
                "b.example.com,1.1.1.2,no,13,Behind the other routers so no one sees it",
                "f.example.com,1.1.1.7,No,12.200,"
        ));
        BTRouterPatch.printFinalRouters(arrList);
    }

}
