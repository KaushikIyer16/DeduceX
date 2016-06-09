package androidcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 *
 * @author mahesh
 */
public class AndroidCode {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        File f = new File("/Users/mahesh/Documents/words.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String s;
        int i=0;
        ArrayList ar = new ArrayList();
        
        System.out.println("Enter length of string required");
        Scanner scan = new Scanner(System.in);
        int len = scan.nextInt();
        
        while((s=br.readLine()) != null)
        {
            if(s.length() == len)
            {
                TreeSet ts = new TreeSet();
                for(i=0;i<len && ts.add(s.charAt(i));i++);
                if(i == len)
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
