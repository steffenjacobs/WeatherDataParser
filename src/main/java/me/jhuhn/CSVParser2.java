package me.jhuhn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVParser2 {
	
	
	

	public static void main(String[] args) throws IOException {
		boolean debug = false;
		boolean debug2 = false;
		String seperator = "|";
		PrintWriter writer = new PrintWriter("data_set_modified_by_jo.csv", "UTF-8");
        BufferedReader br = new BufferedReader(new FileReader(new File("data_set_original.csv")));        
        String oneLine = "";
        int k = 0;
        
        
        
        while ( (oneLine = br.readLine()) != null) {
        	if(debug) {        		System.out.println(oneLine); }
           
           String[] parts = oneLine.split(",");
           
           if(parts[15].trim().equals("")) {      	   parts[15] = "NONE";           }
           

           
           if(debug2) {
        	   System.out.println(parts[15] + "," + parts[16]);
           }
           

           if(parts[15].contains("\"")) {
        	//   System.err.println(parts[15]);
        	   
        	//-------------------   
        	  // String[] rawViolationValues =  parts[14].split("\\" + seperator);
        	   
        	//-------------------   
        	   
        	   String[] test = oneLine.split(",\"");        	   
        	   String last = test[test.length -1];
        	   String violationCSV = last.split("\",")[0];
        	   //System.err.println(violationCSV);
        	   String[] violations = violationCSV.split(",");
        	  // System.err.println(violations[0] + " - " + violations[1]);
        	   

        	   for (int i = 0; i < violations.length; i++) {
        		   parts[15] = violations[i];
//        		   try {
//        			   parts[14] = rawViolationValues[i];
//        		   }catch(Exception e) {
//        			   System.err.println("does not match...");
//        			   System.out.println(oneLine);
//        			   parts[14] = rawViolationValues[0];
//        		   }
        		   
        		   String newLine = "";
        		   for (int j = 0; j < parts.length; ) {
        			   
        			   if(j == 15) {
        				   j = j + violations.length;
        				   newLine += (parts[15] + ",");
        			   }
        			   
        			   
        			   newLine += (parts[j] + ",");
        			   j++;
        		   }
        		   
        		  // System.err.println(newLine);
        		   
        		   writer.write(newLine + "\n");
        	   }
        	  
        	   
        	   

           }else {
        	   writer.write(oneLine + "\n");
           }
           
           if(debug)
           System.out.println("-------------------------------------------------------------------------------------------------");
           
           k++;
           
//           if(k > 100000) {
//        	   writer.close();
//        	   return;
//           }
        }
	}
}
