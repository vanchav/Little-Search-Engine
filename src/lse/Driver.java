package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter name of main file: ");
		String fileName =  scanner.next();
		System.out.print("Enter name of noise file: ");
		String noiseFile = scanner.next();
		System.out.println(fileName + "  "+noiseFile);
		
		LittleSearchEngine lse = new LittleSearchEngine();
		try {
			lse.makeIndex(fileName,noiseFile);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		System.out.print("keyword 1: ");
		String keyword1 =  scanner.next();
		System.out.print("keyword 2: ");
		String keyword2 = scanner.next();
		scanner.close();
		ArrayList<String> results = lse.top5search(keyword1, keyword2);
		System.out.println(results);
		
	}

}

