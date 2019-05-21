package testclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import lioncraftserver.tools.Tools;

public class Mocktest {

	public Mocktest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		Mocktest test=new Mocktest();
		
		test.WorldFromCSV("D:\\erwin\\JMonkey\\unix_systems.csv");
	}

	public void WorldFromCSV(String csvfilename) {
		
		int maxrow=20;
		int step=5;
		int x=10;
		int z=10;
		
		File csvfile=new File(csvfilename);
		try {
			Scanner csvscan = new Scanner(csvfile);
			while(csvscan.hasNextLine())
			{
				String line=csvscan.nextLine();
				System.out.println(line);
				String[] fields=line.split(",");
				System.out.println(fields[2]);
				
			}
			csvscan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	

}
