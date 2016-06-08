/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package androidcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author mahesh
 */
public class AndroidCode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("/Users/mahesh/Documents/words.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String s;
        boolean b;
        ArrayList ar = new ArrayList();
        System.out.println("Enter length of string required");
        Scanner scan = new Scanner(System.in);
        int len = scan.nextInt();
        while((s=br.readLine()) != null)
        {
            if(s.length() == len)
            {
                HashSet hs = new HashSet();
                b = false;
                for(int i=0;i<len;i++)
                {
                    if(!(hs.add(s.charAt(i))))
                    {
                        b = true;
                        break;
                    }
                }
                if(!b)
                {
                    System.out.println(s+" added");//for debug
                    ar.add(s);
                }
                else
                    System.out.println(s+" not added");//for debug
            }
            
        }
    }
    
}
