package Scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	// ALL OTHER CLASSES EXCEPT MAIN.JAVA, SCANNER.JAVA, TOKEN.JAVA CAN BE CANCELED UNLESS IF YOU GUYS WANT TO SEPERATE TOKEN HANDLING IN DIFFERENT CLASSES INSTEAD OF PUTTING ALL OF IT IN SCANNER CLASS
	//===========================================================================================================//
// WE CAN BUILD A NEW ONE USING FILE PROCESSING THIS LIKLY DIFFICULT TO OPERATE + PROCESSING
	//===========================================================================================================//
	public Main() {
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage: java Main <Input>");
			return;
		}
		
		String InputFileName = args[0];
		FileReader FileReader = new FileReader(InputFileName);
		
		try (FileReader) {
			BufferedReader BufferReader = new BufferedReader(FileReader);
			try (BufferReader) {
				Scanner scanner = new Scanner(BufferReader);
				Token Token;		
				do {
					Token = scanner.getNextToken();
					if (Token != null) {
						System.out.println(Token);
					}
				} while (Token != null);
			} catch (IOException Ex) {
				System.err.println("Error reading input file: " + Ex.getMessage());
			} catch (LexicalException Ex) { //
				System.err.println("Lexical error: " + Ex.getMessage() + " at position " + Ex.getPosition());
			}
		} catch (FileNotFoundException Ex) {
			Ex.printStackTrace();
		}
	}
}



/*
*
*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <input-file>");
            return;
        }

        String inputFilename = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            Scanner scanner = new Scanner(reader);
            Token token;
            do {
                token = scanner.getNextToken();
                if (token != null) {
                    System.out.println(token);
                }
            } while (token != null);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        } catch (LexicalException e) {
            System.err.println("Lexical error: " + e.getMessage() + " at position " + e.getPosition());
        }
    }
}

*
*
*/
