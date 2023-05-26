//----------------------------------------------------------------------------------------------------//
// Contributers:-
// - Muhammed Enes Gökdeniz (150121538)
// - Tolga Fehmioğlu (150120022)
// - Abdelrahman Zahran (150120998)
//----------------------------------------------------------------------------------------------------//

import java.io.*;
import java.util.*;

import static javax.lang.model.SourceVersion.isKeyword;

//----------------------------------------------------------------------------------------------------//
public class LexicalChecker {

    static int[] rowColumn = {1, 1};

    // A HashMap containing mappings of brackets to their respective names
    public static HashMap<Character, String> brackets = new HashMap(Map.of(
            '(', "LEFTPAR",
            ')', "RIGHTPAR",
            '[', "LEFTSQUAREB",
            ']', "RIGHTSQUAREB",
            '{', "LEFTCURLYB",
            '}', "RIGHTCURLYB"));

    // An ArrayList containing keywords used in the language
    public static ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(
            "define",
            "let",
            "cond",
            "if",
            "begin"));

    // An ArrayList containing boolean values used in the language
    private static ArrayList<String> booleans = new ArrayList<String>(Arrays.asList(
            "true",
            "false"));
    // An ArrayList containing various signs used in the language
    public static ArrayList<Character> signs = new ArrayList<>(Arrays.asList(
            '!',
            '*',
            '/',
            ':',
            '<',
            '=',
            '>',
            '?'));
    // An ArrayList containing arithmetic signs used in the language
    public static ArrayList<Character> arithmeticSign = new ArrayList<>(Arrays.asList(
            '+',
            '-',
            '.'));


    //----------------------------------------------------------------------------------------------------//
    // This is a Java program that reads input from a file specified by the user
    // and writes the output to a file named "Output.txt".
    public static void main(String[] args) throws IOException {
        // Initialize variables---------------------------------------------//
        // Initialize ArrayList to store lines
        ArrayList<String> lines = new ArrayList<>();
        // Initialize ArrayList to store tokens
        ArrayList<String> tokens = new ArrayList<>();
        // Initialize ArrayList to store errors
        ArrayList<String> errors = new ArrayList<>();
    
        java.util.Scanner input = new java.util.Scanner(System.in);
        try (input) {
            // Prompt the user to enter the file name
            System.out.print("Enter Input File Name: ");
            String inputName = input.next();

            // Create a File object for input and output
            File inputFile = new File(inputName);
            File outputFile = new File("OutputTokenList.txt");

            java.util.Scanner scanner = new java.util.Scanner(inputFile);
            //   FileWriter writer = new FileWriter(outputFile);

            try (scanner) {
                while (scanner.hasNextLine()) {
                    lines.add(scanner.nextLine());
                }
                FileWriter writer = new FileWriter(outputFile);
                //   addTokens(lines, tokens);
                int last = tokens.size() - 1;
                try (writer) {
                    // Print tokens list
                    for (String token : tokens) {
                        writer.write(token);
                        System.out.print(token);
                        if (tokens.indexOf(token) != last) {
                            writer.write("\n");
                            System.out.println();
                        }
                    }
                    writer.close();
                    // Print errors list
//                    for (String error : errors) {
//                    	System.out.println(error);
//                    }
                    // Catch any IO exceptions thrown while writing to the file
                } catch (IOException Ex) {
                    // Print the error message
                    System.err.println("Error writng to file: " + Ex.getMessage());
                    // Return to exit the program
                    return;
                }

            }
            // Catch the exception thrown when the file is not found
        } catch (FileNotFoundException Ex) {
            // Print the stack trace for the exception
            System.err.println("Invalid Input File!");
            Ex.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------//
    // This method identifies tokens from the lines of the input file and store them
    public void addTokens(ArrayList<String> lines, ArrayList<String> tokenMessages, ArrayList<Token> tokenList) {
        String str = "";
        int size;
        Token token;
        for (String currentLine : lines) {
            String value = "";
            String type = "";
            for (int j = 0; j < currentLine.length(); j++) {

                char ch = currentLine.charAt(j);
                // check if the character is a bracket
                if (isBracket(j, currentLine)) {
                    // get the corresponding closing bracket and add the position information
                    str = brackets.get(ch) + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    type = brackets.get(ch);
                    value = String.valueOf(ch);
                    token = getToken(type, value, rowColumn[0], rowColumn[1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    rowColumn[1]++;

                    // check if the character is the start of a keyword
                } else if (isHasKeyword(j, currentLine)) {
                    // get the keyword and add the position information
                    size = getSizeOf(j, currentLine);
                    value = currentLine.substring(j, j + size);
                    type = value.toUpperCase(Locale.ROOT);
                    str = value.toUpperCase(Locale.ROOT) + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[
                            1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    j += (size - 1);
                    rowColumn[1] += size;

                    // check if the character is a whitespace
                } else if (ch == ' ') {
                    rowColumn[1]++;

                    // check if the character is the start of a char literal
                } else if (isChar(j, currentLine)) {
                    // add the char literal and position information
                    type = "CHAR";
                    value = String.valueOf(currentLine.charAt(j + 1));
                    str = type + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[
                            1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    rowColumn[1] += 3;
                    j += 2;

                    // check if the character is the start of a boolean literal
                } else if (isBoolean(j, currentLine)) {
                    // get the boolean literal and add the position information
                    size = getSizeOf(j, currentLine);
                    value = currentLine.substring(j, j + size);
                    type = value.toUpperCase(Locale.ROOT);
                    str = type + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[
                            1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    j += (size - 1);
                    rowColumn[1] += size;

                    // check if the character is a tilde
                } else if (ch == '~') {
                    // add the tilde token and position information
                    str = "TILDE" + String.format("%d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    tokenMessages.add(str);
                    break;

                    // check if the character is the start of a string literal
                } else if (isStringLiteral(j, currentLine)) {
                    // get the string literal and add the position information
                    int lastIndex = getQuotesIndex(j, currentLine);
                    type = "STRING";
                    value = currentLine.substring(j, lastIndex + 1);
                    str = type + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[1]);
                    tokenList.add(token);

                    tokenMessages.add(str);
                    size = lastIndex - j + 1;
                    j += size - 1;
                    rowColumn[1] += size;

                    // check if the character is the start of an identifier
                } else if (isIdentifier(j, currentLine)) {
                    // add the identifier and position information
                    type = "IDENTIFIER";
                    size = getSizeOf(j, currentLine);
                    value = currentLine.substring(j, j + size);
                    str = type + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    j += size - 1;
                    rowColumn[1] += size;

                    // check if the character is the start of a number
                } else if (isNumber(j, currentLine)) {
                    type = "NUMBER";
                    size = getSizeOf(j, currentLine);
                    value = currentLine.substring(j, j + size);
                    // add the number and position information
                    str = type + String.format(" %d:%d", rowColumn[0], rowColumn[1]);
                    // add the current token to the list of tokens
                    token = getToken(type, value, rowColumn[0], rowColumn[1]);
                    tokenList.add(token);
                    tokenMessages.add(str);
                    j += size - 1;
                    rowColumn[1] += size;
                } else {
                    int error_size = getSizeOf(j, currentLine);
                    String errorPart = currentLine.substring(j, j + error_size);
                    String err_mes = "LEXICAL ERROR" + String.format("[%d:%d]: Invalid token '%s'", rowColumn[0], rowColumn[1], errorPart);
                    // add the Error Message to the list of tokens
                    tokenMessages.add(err_mes);
                    // add the error to the list of errors
                    return;
                }
            }
            // Increase the row value(move to next row)
            rowColumn[0]++;
            // Set Column value to 1
            rowColumn[1] = 1;

        }
    }

    private static Token getToken(String type, String value, int row, int column) {
        return new Token(type, value, row, column);

    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if the input string is a number.
    private static boolean isNumber(int index, String CurrentLine) {

        return isDecimalSignedInteger(index, CurrentLine) || isHexadecimalUnsignedInteger(index, CurrentLine) || isBinaryUnsignedInteger(index, CurrentLine) || isFloatingPointNumber(index, CurrentLine);
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if a given character is a valid hexadecimal digit.
    private static boolean isHexadecimalDigit(Character ch) {
        //  The method uses a switch statement to match the input character against a list of valid hexadecimal digits,
        //  which includes both upper-case and lower-case letters from 'A' to 'F', as well as digits from '0' to '9'.
        return switch (ch) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' ->
                    true;
            default -> false;
        };
    }

    //----------------------------------------------------------------------------------------------------//
    // This method get size of (distance) between 2 characters / lexemes
    private static int getSizeOf(int startIndex, String currentLine) {
        // Get the character at the specified index.
        char ch = currentLine.charAt(startIndex);
        // Get the index of the last character in the string.
        int lastIndex = currentLine.length() - 1;
        // Initialize a counter to keep track of the size.
        int count = 0;
        // Continue looping until a bracket or whitespace is encountered.
        while (!isBracket(startIndex, currentLine) && !Character.isSpaceChar(ch)) {
            // If we've reached the end of the line, increment the counter and exit the loop.
            if (lastIndex == startIndex) {
                count++;
                break;
            }
            // Increment the counter and move to the next character.
            count++;
            startIndex++;
            ch = currentLine.charAt(startIndex);
        }
        // Return the size of the string.
        return count;
    }

    public void printResults(ArrayList<String> tokenMessage, File outputFile) {
        FileWriter writer;
        try {
            writer = new FileWriter(outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //   addTokens(lines, tokens);
        int last = tokenMessage.size() - 1;
        try {
            // Print tokens list
            for (String message : tokenMessage) {
                writer.write(message);
                System.out.print(message);
                if (tokenMessage.indexOf(message) != last) {
                    writer.write("\n");
                    System.out.println();
                }
            }

            writer.close();
            // Catch any IO exceptions thrown while writing to the file
        } catch (IOException Ex) {
            // Print the error message
            System.err.println("Error writing to file: " + Ex.getMessage());
            // Return to exit the program

        }

    }

    public boolean isLexicalError(ArrayList<String> tokenMessages) {
        String lastMessage = tokenMessages.get(tokenMessages.size() - 1);
        return lastMessage.startsWith("LEXICAL ERROR");
    }

    //----------------------------------------------------------------------------------------------------//
    // Check if the number is a single digit first
    private static boolean isDecimalSignedInteger(int startIndex, String currentLine) {

        char ch = currentLine.charAt(startIndex);
        int lastIndex = currentLine.length() - 1;

        // If the number has a sign
        if (ch == '-' || ch == '+') {
            // If the sign is at the end of the line, it cannot be a number
            if (startIndex == lastIndex) return false;
            startIndex++;
            ch = currentLine.charAt(startIndex);
            // If a bracket or a space comes right after the sign, it cannot be a number
            if (isBracket(startIndex, currentLine) || Character.isSpaceChar(ch)) {
                return false;
            }
        }

        // Check the rest of the digits
        while (!isBracket(startIndex, currentLine) && !Character.isSpaceChar(ch)) {
            // If the character is not a digit, it cannot be a number
            if (!Character.isDigit(ch)) {
                return false;
            }
            // If the index is at the end of the line, break the loop
            if (lastIndex == startIndex) {
                break;
            }
            startIndex++;
            ch = currentLine.charAt(startIndex);
        }

        // If none of the above conditions apply, it must be a valid signed decimal integer
        return true;
    }

    //----------------------------------------------------------------------------------------------------//
    // This method searches for the index of the closing quotation mark of a string literal
    // starting from the given 'start' index in the 'currentLine' string
    private static int getQuotesIndex(int start, String currentLine) {
        char ch;
        int index = start;
        for (int i = start + 1; i < currentLine.length(); i++) {
            ch = currentLine.charAt(i);
            // If a closing quotation mark is found, update the 'index' variable to the current index
            if (ch == '"') {
                index = i;
            }
        }
        return index;
    }

    //----------------------------------------------------------------------------------------------------//
    // This method check if the token is a string literal or not
    private static boolean isStringLiteral(int start, String currentLine) {

        char ch = currentLine.charAt(start);

        if (ch != '"') {
            return false;
        }

        // String literals cannot be of length 1
        if (currentLine.length() - start <= 2) return false;

        // String literals cannot start with double quotes
        if (currentLine.charAt(start + 1) == '"') return false;

        // Check if there is a closing quote
        if (!remainContainsQuote(start, currentLine)) {
            return false;
        }

        // Get the index of the closing quote
        int quoteIndex = getQuotesIndex(start, currentLine);

        // Loop through the characters in the string literal
        for (int i = start + 1; i < quoteIndex; i++) {
            ch = currentLine.charAt(i);
            if (ch == '\\') {
                // Handle escape sequences
                int next = i + 1;
                char nextChar = currentLine.charAt(next);
                if (nextChar != '\\' && nextChar != '"') {
                    return false;
                }
                if (nextChar == '"') {
                    if (next == quoteIndex) {
                        return false;
                    }
                }
            } else if (ch == '"') {
                // Double quotes must be escaped
                int prev = i - 1;
                char prevChar = currentLine.charAt(prev);
                if (prevChar != '\\') {
                    return false;
                }
            }
        }

        return true;
    }

    //----------------------------------------------------------------------------------------------------//
    // Method to check if the rest of the line contains a double quote character
    private static boolean remainContainsQuote(int start, String currentLine) {
        char ch = currentLine.charAt(start);
        // Iterate over the rest of the string, starting from the given index
        for (int i = start + 1; i < currentLine.length(); i++) {
            // Check if a double quote is found
            if (currentLine.charAt(i) == '"') {
                return true;
            }
        }
        // Return false if no double quote is found
        return false;
    }

    //----------------------------------------------------------------------------------------------------//
    // This method check if the token is a character token or not
    private static boolean isChar(int start, String currentLine) {
        char ch = currentLine.charAt(start);
        if (ch != '\'') {
            return false;
        } //'a'
        if (currentLine.length() - start < 3) return false; // minimum length should be 3, i.e., 'a'
        int last = getSizeOf(start, currentLine) - 1; // find the last index before space or bracket
        if (currentLine.charAt(start + 2) == '\'') {
            if (currentLine.charAt(start + 1) == '\'')
                return false; // empty character is not allowed
            if (currentLine.charAt(last) != '\'') return false; // ignore last if it is not '
            if ((last - start == 2) && currentLine.charAt(last) == '\'') {// 'a' 'b' ....
                return currentLine.charAt(start + 1) != '\\'; // ignore '\'
            }
            // '\''
            return currentLine.charAt(start + 1) == '\\' && last - start == 3; // '\''
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------//
    // The method check if the number is an unsigned hexadecimal integer
    private static boolean isHexadecimalUnsignedInteger(int startIndex, String currentLine) {
        char ch = currentLine.charAt(startIndex);
        // Check the first character. If it's not 0, it's not a hexadecimal number
        if (ch != '0') {
            return false;
        }
        int remain_len = currentLine.length() - startIndex;
        // If the remaining length is less than 3 and the second character is not 'x', return false;
        if (remain_len < 3 && currentLine.charAt(startIndex + 1) != 'x') {
            return false;
        }
        int lastIndex = currentLine.length() - 1;
        int digit_index = startIndex + 2;
        ch = currentLine.charAt(digit_index);
        // If two characters are satisfied, then check the hexadecimals
        while (!(brackets.containsKey(ch) || Character.isSpaceChar(ch))) {
            if (!isHexadecimalDigit(ch)) {
                return false;
            }
            if (lastIndex == digit_index) {
                break;
            }
            digit_index++;
            ch = currentLine.charAt(digit_index);
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------------//
    // This is a method called isBinaryUnsignedInteger, which takes a starting index and a string as input and
    // returns a boolean indicating whether or not the string starting at the given index represents a binary unsigned integer.
    private static boolean isBinaryUnsignedInteger(int startIndex, String currentLine) {
        char ch = currentLine.charAt(startIndex);
        // check the first character. If it's not 0 it's not a binary integer
        if (ch != '0') {
            return false;
        }
        int remain_len = currentLine.length() - startIndex;
        // if the remaining length is less than 3 and second character is not b return false;
        if (remain_len < 3 && currentLine.charAt(startIndex + 1) != 'b') {
            return false;
        }
        int lastIndex = currentLine.length() - 1;
        int digit_index = startIndex + 2;
        ch = currentLine.charAt(digit_index);
        // if two characters are satisfied then check the binary digits;
        while (!(brackets.containsKey(ch) || Character.isSpaceChar(ch))) {
            if (!isBinary(ch)) {
                return false;
            }
            if (lastIndex == digit_index) {
                break;
            }
            digit_index++;
            ch = currentLine.charAt(digit_index);
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------------//
    // This function checks if a character is a binary digit (0 or 1)
    private static boolean isBinary(Character ch) {
        return ch == '0' || ch == '1';
    }

    //----------------------------------------------------------------------------------------------------//
    // This is a method that checks whether a given substring is a floating point number or not.
    // The method takes in two parameters: the starting index of the substring and the string itself.
    private static boolean isFloatingPointNumber(int startIndex, String currentLine) {
        // check the first character;
        char ch = currentLine.charAt(startIndex);
        // if first character is not a digit, plus sign, minus sign, or decimal point, return false
        if (!(ch == '+' || ch == '-' || Character.isDigit(ch) || ch == '.')) {
            return false;
        }
        int length = currentLine.length();
        int curr_index = startIndex;
        // if the current index is the last character in the string, return false
        if (curr_index == length - 1) return false;
        // if the first character is a plus or minus sign, move to the next character
        if (ch == '-' || ch == '+') {
            curr_index++;
        }
        char current_char = currentLine.charAt(curr_index);
        // while the current character is not a decimal point or an 'e' or 'E' character
        while (current_char != '.' && (!(current_char == 'e' || current_char == 'E'))) {
            // if the current index is the last character in the string, return false
            if (curr_index == length - 1) {
                return false;
            }
            // if the current character is not a digit, return false
            if (!Character.isDigit(current_char)) {
                return false;
            }
            curr_index++;
            current_char = currentLine.charAt(curr_index);
        }

        // if current char is .
        if (current_char == '.') {
            // find the index of the decimal point
            curr_index = currentLine.indexOf(current_char);
            curr_index++;
            // if the index of the decimal point is the last character in the string, return false
            if (curr_index == length) return false;
            current_char = currentLine.charAt(curr_index);
            // if the character following the decimal point is not a digit, return false
            if (!Character.isDigit(current_char)) return false;// .dan sonra e veya decimal gelmesi lazım

            // while the current character is not an 'e' or 'E' character
            while (!(current_char == 'e' || current_char == 'E')) {
                // if the index of the current character plus one is the last character in the string
                if (curr_index + 1 == length) {
                    // if the current character is a bracket or a space character or a digit, return true
                    if (brackets.containsKey(current_char) || Character.isSpaceChar(current_char)) {
                        return true;
                    }
                    if (Character.isDigit(current_char)) {
                        return true;
                    }
                }
                // if the current character is not a digit, return false
                if (!Character.isDigit(current_char)) {
                    return false;

                }
                curr_index++;
                current_char = currentLine.charAt(curr_index);
            }
            // if the index of the current character plus one is the last character in the string, return false
            if (curr_index + 1 == length) return false;
            // check if the exponential part of the floating point number is satisfied
            return IsExponentialPartSatisfied(currentLine, length, curr_index, current_char);
        }
        // if the index of the current character plus one is the last character in the string, return false
        if (curr_index + 1 == length) return false;
        // check if the exponential part of the floating point number is satisfied
        return IsExponentialPartSatisfied(currentLine, length, curr_index, current_char);
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if the exponential part of a number is valid
    private static boolean IsExponentialPartSatisfied(String currentLine, int remain_len, int curr_index, char current_char) {
        int len = currentLine.length();

        // Get the next character
        current_char = currentLine.charAt(++curr_index);

        // Check if there's a sign (+/-) in the exponential part
        if (current_char == '+' || current_char == '-') {
            // Check if there are enough characters left to form a valid exponential part
            if (curr_index + 1 == len) return false;

            // Get the next character after the sign
            current_char = currentLine.charAt(++curr_index);

            // Loop through the remaining characters in the exponential part
            while (!brackets.containsKey(current_char) && !Character.isSpaceChar(current_char)) {
                // Check if the character is a digit
                if (!Character.isDigit(current_char)) {
                    return false;
                }

                // Check if we have reached the end of the exponential part
                if (curr_index == len - 1) {
                    break;
                }

                // Get the next character
                curr_index++;
                current_char = currentLine.charAt(curr_index);
            }

        } else {
            // If there's no sign, the first character in the exponential part must be a digit
            if (!Character.isDigit(current_char)) {
                return false;
            }

        }

        // If we reach here, the exponential part is valid
        return true;
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if a given startIndex in the currentLine string is a valid identifier
    private static boolean isIdentifier(int startIndex, String currentLine) {

        char ch = currentLine.charAt(startIndex);

        // If the character is an arithmetic sign, check if it is followed by a space or a bracket
        if (arithmeticSign.contains(ch)) {
            if (startIndex == currentLine.length() - 1) {
                return true;
            }

            char nextChar = currentLine.charAt(startIndex + 1);
            if (Character.isSpaceChar(nextChar) || isBracket(startIndex, currentLine)) {
                return true;
            }
        }

        // If the character is a sign or alphabetic, check the rest of the characters in the identifier
        if (signs.contains(ch) || Character.isAlphabetic(ch)) {

            int lastIndex = currentLine.length() - 1;
            if (lastIndex == startIndex) {
                return true;
            }

            int iter = 1;
            char nextChar = currentLine.charAt(startIndex + iter);

            // Loop through the remaining characters in the identifier
            while ((!Character.isSpaceChar(nextChar) && !isBracket(startIndex + iter, currentLine))) {
                // Check if the character is valid (alphabetic, numeric, or arithmetic sign)
                if ((!Character.isAlphabetic(nextChar) && !Character.isDigit(nextChar)
                        && !arithmeticSign.contains(nextChar)) || signs.contains(nextChar)) {
                    return false;
                }
                // example : if there are a sign elements except in first element, return false. ex: =aa?++
                //if(signs.contains(nextChar)) return false;

                // Check if we have reached the end of the identifier
                if (lastIndex == startIndex + iter) {
                    break;
                }

                iter++;
                nextChar = currentLine.charAt(startIndex + iter);

            }

            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if a given startIndex in the currentLine string is a valid boolean value
    private static boolean isBoolean(int startIndex, String currentLine) {

        char ch = currentLine.charAt(startIndex);
        int lastIndex = currentLine.length() - 1;
        int iter = 0;
        StringBuilder str = new StringBuilder();

        // Loop through the characters starting at the startIndex until a bracket or space is encountered
        while (!isBracket(startIndex, currentLine) && !Character.isSpaceChar(ch)) {
            // Check if we have reached the end of the string
            if (lastIndex == startIndex + iter) {
                str.append(ch);
                break;
            }
            str.append(ch);
            iter++;
            ch = currentLine.charAt(startIndex + iter);
        }

        // Check if the characters between startIndex and the bracket/space form a valid boolean
        return booleans.contains(str.toString());
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if a given startIndex in the currentLine string contains a valid keyword
    private static boolean isHasKeyword(int startIndex, String currentLine) {
        char ch = currentLine.charAt(startIndex);
        int lastIndex = currentLine.length() - 1;
        int iter = 0;
        StringBuilder str = new StringBuilder();

        // Loop through the characters starting at the startIndex until a bracket or space is encountered
        while (!isBracket(startIndex, currentLine) && !Character.isSpaceChar(ch)) {
            if (lastIndex == startIndex + iter) {
                str.append(ch);
                break;
            }
            str.append(ch);
            iter++;
            ch = currentLine.charAt(startIndex + iter);
        }

        // Check if the characters between startIndex and the bracket/space form a valid keyword
        return keywords.contains(str.toString());
    }

    //----------------------------------------------------------------------------------------------------//
    // This method checks if the character at a given index in the currentLine string is a bracket
    private static boolean isBracket(int index, String currentLine) {
        char ch = currentLine.charAt(index);
        // Check if the character is a key in the brackets map, indicating that it is a bracket
        return brackets.containsKey(ch);
    }


}
//----------------------------------------------------------------------------------------------------//}
//----------------------------------------------------------------------------------------------------//
