//----------------------------------------------------------------------------------------------------//
// Contributers:-
// - Muhammed Enes Gökdeniz (150121538)
// - Tolga Fehmioğlu (150120022)
// - Abdelrahman Zahran (150120998)
//----------------------------------------------------------------------------------------------------//
// Imported Libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
//----------------------------------------------------------------------------------------------------//
/** Parser Class **/
//----------------------------------------------------------------------------------------------------//
public class Parser {
	
    ArrayList<Token> tokenList; // Declaration of ArrayList variable tokenList
    Token currentToken; // Declaration of Token variable currentToken
    StringBuilder output; // Declaration of StringBuilder variable output
    int currentIndex; // Declaration of int variable currentIndex

    Parser(ArrayList<Token> tokenList) { // Constructor with ArrayList parameter
        this.tokenList = tokenList; // Assigning the parameter value to tokenList
        output = new StringBuilder(); // Initializing output as a new StringBuilder object
        currentIndex = 0; // Initializing currentIndex to 0
    }
  //----------------------------------------------------------------------------------------------------//
    public void parse() { // Method for parsing
        if (tokenList.size() == 0) { // Checking if tokenList is empty
            return; // Return if empty
        }
        currentToken = tokenList.get(currentIndex); // Assigning the current token from tokenList
        int level = 0; // Initializing level to 0

        program(level); // Calling the program method with level parameter
    }
  //----------------------------------------------------------------------------------------------------//
    private void program(int level) { // Method for program
        printRule(level, GrammerRules.program); // Calling the printRule method with level and program as parameters
        if (currentIndex <= tokenList.size() - 1 && currentToken.getType().equals(TokenType.LEFTPAR)) { // Checking conditions
            TopLevelForm(level + 1); // Calling the TopLevelForm method with level + 1 parameter
            program(level+1); // Recursive call to program method with level + 1 parameter
        } else {
            printEpsilon(level + 1); // Calling the printEpsilon method with level + 1 parameter
        }
    }
  //----------------------------------------------------------------------------------------------------//
    private void printRule(int level, String rule) { // Method for printing a rule
        for (int i = 0; i < level; i++) { // Loop for indentation
            System.out.print(" "); // Printing indentation
            output.append(" "); // Appending indentation to output
        }
        System.out.print("<"); // Printing opening angle bracket
        output.append("<"); // Appending opening angle bracket to output
        System.out.print(rule); // Printing rule
        System.out.println(">"); // Printing closing angle bracket and newline
        output.append(rule); // Appending rule to output
        output.append(">"); // Appending closing angle bracket to output
        output.append("\n"); // Appending newline to output
    }
  //----------------------------------------------------------------------------------------------------//
    private void TopLevelForm(int level) { // Method for TopLevelForm

        printRule(level, GrammerRules.TopLevelForm); // Calling the printRule method with level and TopLevelForm as parameters
        consume(TokenType.LEFTPAR, level + 1); // Calling the consume method with LEFTPAR and level + 1 as parameters
        SecondLevelForm(level + 1); // Calling the SecondLevelForm method with level + 1 parameter
        consume(TokenType.RIGHTPAR, level + 1); // Calling the consume method with RIGHTPAR and level + 1 as parameters
    }
  //----------------------------------------------------------------------------------------------------//
    private void printTerminal(String terminal, int level) { // Method for printing a terminal
        for (int i = 0; i < level; i++) { // Loop for indentation
            System.out.print(" "); // Printing indentation
            output.append(" "); // Appending indentation to output
        }
        String message = terminal + " (" + currentToken.getValue() + ")"; // Creating the terminal message
        output.append(message); // Appending message to output
        System.out.println(message); // Printing the message
        output.append("\n"); // Appending newline to output
    }
  //----------------------------------------------------------------------------------------------------//
 // Moves to the next token in the token list
    private void nextMove() {
        currentIndex++;
        if (currentIndex < tokenList.size())
            currentToken = tokenList.get(currentIndex);
    }
  //----------------------------------------------------------------------------------------------------//
    // Handles the second level form in the grammar
    private void SecondLevelForm(int level) {
        printRule(level, GrammerRules.SecondLevelForm);
        
        // Checks if the current token is of type "DEFINE"
        if (currentToken.getType().equals(TokenType.DEFINE)) {
            Definition(level + 1); // Calls the Definition method
        } else {
            consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
            FunCall(level + 1); // Calls the FunCall method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        }
    }

    // Consumes the expected token type
    private void consume(String expectedType, int level) {
        if (currentToken.getType().equals(expectedType)) {
            printTerminal(expectedType, level); // Prints the terminal token
            nextMove(); // Moves to the next token
        } else {
            expectedType = getExpectedType(expectedType);
            String message = "SYNTAX ERROR[" + currentToken.getRow() + ":" + currentToken.getColumn() + "]: "
                    + "'" + expectedType + "'" + " is expected";
            output.append(message);
            throw new SyntaxErrorException(message); // Throws a syntax error exception
        }
    }
    //----------------------------------------------------------------------------------------------------//

 // Returns the expected type as a string for error messages
    private String getExpectedType(String expectedType) {
        switch (expectedType) {
            case TokenType.IDENTIFIER -> {
                return "identifier";
            }
            case TokenType.LEFTPAR -> {
                return "(";
            }
            case TokenType.RIGHTPAR -> {
                return ")";
            }
            case TokenType.BOOLEAN -> {
                return "boolean";
            }
            case TokenType.CHAR -> {
                return "char";
            }
            case TokenType.DEFINE -> {
                return "define";
            }
            case TokenType.NUMBER -> {
                return "number";
            }
            case TokenType.STRING -> {
                return "string";
            }
            default -> {
                return expectedType;
            }
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "Definition" rule in the grammar
    private void Definition(int level) {
        printRule(level, GrammerRules.Definition);
        consume(TokenType.DEFINE, level + 1); // Consumes a "DEFINE" token
        DefinitionRight(level + 1); // Calls the DefinitionRight method
    }

    // Handles the "DefinitionRight" rule in the grammar
    private void DefinitionRight(int level) {
        printRule(level, GrammerRules.DefinitionRight);
        
        // Checks if the current token is of type "IDENTIFIER"
        if (currentToken.getType().equals(TokenType.IDENTIFIER)) {
            nextMove(); // Moves to the next token
            expression(level + 1); // Calls the expression method
        } else {
            consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
            consume(TokenType.IDENTIFIER, level + 1); // Consumes an "IDENTIFIER" token
            ArgList(level + 1); // Calls the ArgList method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
            Statements(level + 1); // Calls the Statements method
        }
    }
    //----------------------------------------------------------------------------------------------------//

 // Handles the "Statements" rule in the grammar
    private void Statements(int level) {
        printRule(level, GrammerRules.Statements);
        
        // Checks if the current token is one of the valid types for an expression
        if (currentToken.getType().equals(TokenType.IDENTIFIER) ||
            currentToken.getType().equals(TokenType.NUMBER) ||
            currentToken.getType().equals(TokenType.CHAR) ||
            currentToken.getType().equals(TokenType.BOOLEAN) ||
            currentToken.getType().equals(TokenType.STRING) ||
            currentToken.getType().equals(TokenType.LEFTPAR)) {
            
            expression(level + 1); // Calls the expression method
        } else {
            Definition(level + 1); // Calls the Definition method
            Statements(level + 1); // Calls the Statements method recursively
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "Expressions" rule in the grammar
    private void expressions(int level) {
        printRule(level, GrammerRules.Expressions);
        
        // Checks if the current token is one of the valid types for an expression
        if (currentToken.getType().equals(TokenType.IDENTIFIER) ||
            currentToken.getType().equals(TokenType.NUMBER) ||
            currentToken.getType().equals(TokenType.CHAR) ||
            currentToken.getType().equals(TokenType.BOOLEAN) ||
            currentToken.getType().equals(TokenType.STRING) ||
            currentToken.getType().equals(TokenType.LEFTPAR)) {
            
            expression(level + 1); // Calls the expression method
            expressions(level + 1); // Calls the expressions method recursively
        } else {
            printEpsilon(level + 1); // Prints epsilon (empty) production
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "Expression" rule in the grammar
    private void expression(int level) {
        printRule(level, GrammerRules.Expression);
        
        // Checks if the current token is one of the valid types for a terminal
        if (currentToken.getType().equals(TokenType.IDENTIFIER) ||
            currentToken.getType().equals(TokenType.NUMBER) ||
            currentToken.getType().equals(TokenType.CHAR) ||
            currentToken.getType().equals(TokenType.BOOLEAN) ||
            currentToken.getType().equals(TokenType.STRING)) {
            
            printTerminal(currentToken.getType(), level + 1); // Prints the terminal token
            nextMove(); // Moves to the next token
        } else {
            consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
            Expr(level + 1); // Calls the Expr method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        }
    }
    //----------------------------------------------------------------------------------------------------//

 // Handles the "Expr" rule in the grammar
    private void Expr(int level) {
        printRule(level, GrammerRules.Expr);
        
        // Switch statement based on the type of the current token
        switch (currentToken.getType()) {
            case KEYWORDS.LET -> LetExpression(level + 1); // Calls the LetExpression method
            case KEYWORDS.COND -> CondExpression(level + 1); // Calls the CondExpression method
            case KEYWORDS.IF -> IfExpression(level + 1); // Calls the IfExpression method
            case KEYWORDS.BEGIN -> BeginExpression(level + 1); // Calls the BeginExpression method
            default -> FunCall(level + 1); // Calls the FunCall method
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "IfExpression" rule in the grammar
    private void IfExpression(int level) {
        printRule(level, GrammerRules.IfExpression);
        consume(KEYWORDS.IF, level + 1); // Consumes the "IF" keyword
        expression(level + 1); // Calls the expression method
        expression(level + 1); // Calls the expression method
        EndExpression(level + 1); // Calls the EndExpression method
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "EndExpression" rule in the grammar
    private void EndExpression(int level) {
        printRule(level, GrammerRules.EndExpression);
        
        // Checks if the current token is one of the valid types for an expression
        if (currentToken.getType().equals(TokenType.IDENTIFIER) ||
            currentToken.getType().equals(TokenType.NUMBER) ||
            currentToken.getType().equals(TokenType.CHAR) ||
            currentToken.getType().equals(TokenType.BOOLEAN) ||
            currentToken.getType().equals(TokenType.STRING) ||
            currentToken.getType().equals(TokenType.LEFTPAR)) {
            
            expression(level + 1); // Calls the expression method
        } else {
            printEpsilon(level + 1); // Prints epsilon (empty) production
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "BeginExpression" rule in the grammar
    private void BeginExpression(int level) {
        printRule(level, GrammerRules.BeginExpression);
        consume(KEYWORDS.BEGIN, level + 1); // Consumes the "BEGIN" keyword
        printTerminal(KEYWORDS.BEGIN, level + 1); // Prints the "BEGIN" keyword
        Statements(level + 1); // Calls the Statements method
    }
    //----------------------------------------------------------------------------------------------------//

 // Handles the "CondExpression" rule in the grammar
    private void CondExpression(int level) {
        printRule(level, GrammerRules.CondExpression);
        consume(KEYWORDS.COND, level + 1); // Consumes the "COND" keyword
        CondBranches(level + 1); // Calls the CondBranches method
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "CondBranches" rule in the grammar
    private void CondBranches(int level) {
        printRule(level, GrammerRules.CondBranches);
        consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
        expression(level + 1); // Calls the expression method
        Statements(level + 1); // Calls the Statements method
        consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        CondBranch(level + 1); // Calls the CondBranch method
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "CondBranch" rule in the grammar
    private void CondBranch(int level) {
        printRule(level, GrammerRules.CondBranch);
        
        // Checks if the current token is a left parenthesis token
        if (currentToken.getType().equals(TokenType.LEFTPAR)) {
            printTerminal(TokenType.LEFTPAR, level + 1); // Prints the left parenthesis token
            nextMove(); // Moves to the next token
            expression(level + 1); // Calls the expression method
            Statements(level + 1); // Calls the Statements method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "LetExpression" rule in the grammar
    private void LetExpression(int level) {
        printRule(level, GrammerRules.LetExpression);
        consume(KEYWORDS.LET, level + 1); // Consumes the "LET" keyword
        LetExpr(level + 1); // Calls the LetExpr method
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "LetExpr" rule in the grammar
    private void LetExpr(int level) {
        printRule(level, GrammerRules.LetExpr);
        
        // Checks if the current token is a left parenthesis token
        if (currentToken.getType().equals(TokenType.LEFTPAR)) {
            nextMove(); // Moves to the next token
            VarDefs(level + 1); // Calls the VarDefs method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        } else {
            consume(TokenType.IDENTIFIER, level + 1); // Consumes an "IDENTIFIER" token
            consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
            VarDefs(level + 1); // Calls the VarDefs method
            consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
            Statements(level + 1); // Calls the Statements method
        }
    }
    //----------------------------------------------------------------------------------------------------//

 // Handles the "VarDefs" rule in the grammar
    private void VarDefs(int level) {
        printRule(level, GrammerRules.VarDefs);
        consume(TokenType.LEFTPAR, level + 1); // Consumes a left parenthesis token
        consume(TokenType.IDENTIFIER, level + 1); // Consumes an "IDENTIFIER" token
        expression(level + 1); // Calls the expression method
        consume(TokenType.RIGHTPAR, level + 1); // Consumes a right parenthesis token
        VarDef(level + 1); // Calls the VarDef method
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "VarDef" rule in the grammar
    private void VarDef(int level) {
        printRule(level, GrammerRules.VarDef);
        
        // Checks if the current token is a left parenthesis token
        if (currentToken.getType().equals(TokenType.LEFTPAR)) {
            VarDefs(level + 1); // Calls the VarDefs method
        } else {
            printEpsilon(level + 1); // Prints epsilon (empty) production
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "ArgList" rule in the grammar
    private void ArgList(int level) {
        printRule(level, GrammerRules.ArgList);
        
        // Checks if the current token is an "IDENTIFIER" token
        if (currentToken.getType().equals(TokenType.IDENTIFIER)) {
            printTerminal(TokenType.IDENTIFIER, level + 1); // Prints the "IDENTIFIER" token
            nextMove(); // Moves to the next token
            ArgList(level + 1); // Calls the ArgList method recursively
        } else {
            printEpsilon(level + 1); // Prints epsilon (empty) production
        }
    }
    //----------------------------------------------------------------------------------------------------//

    // Prints epsilon (empty) production
    private void printEpsilon(int level) {
        for (int j = 0; j < level; j++) {
            System.out.print(" ");
            output.append(" ");
        }
        System.out.println("___");
        output.append("___");
        output.append("\n");
    }
    //----------------------------------------------------------------------------------------------------//

    // Handles the "FunCall" rule in the grammar______________
    private void FunCall(int level) {
        printRule(level, GrammerRules.FunCall);
        consume(TokenType.IDENTIFIER, level + 1); // Consumes an "IDENTIFIER" token
        expressions(level + 1); // Calls the expressions method
    }
    //----------------------------------------------------------------------------------------------------//

 // The main method for the program
    public static void main(String[] args) {
        ArrayList<String> inputList = getInputList(); // Retrieves the input list
        File outputFileParser = new File("Output.txt"); // Output file for parser results
        File outputFileTokenList = new File("OutputTokenList.txt"); // Output file for token list
        
        ArrayList<String> tokenMessages = new ArrayList<>(); // Stores token messages
        ArrayList<Token> tokenList = new ArrayList<>(); // Stores tokens
        
        LexicalChecker lexicalChecker = new LexicalChecker(); // Creates a lexical checker object
        lexicalChecker.addTokens(inputList, tokenMessages, tokenList); // Performs lexical analysis
        
        if (lexicalChecker.isLexicalError(tokenMessages)) {
            lexicalChecker.printResults(tokenMessages, outputFileParser); // Prints lexical error messages
            return;
        }
        
        Parser parser;
        parser = new Parser(tokenList); // Creates a parser object with the token list
        
        try {
            parser.parse(); // Parses the token list
        } catch (SyntaxErrorException e){
            System.out.println(e.getMessage()); // Prints syntax error message
        }
        
        try {
            FileWriter writer = new FileWriter(outputFileParser); // Creates a file writer for the parser output
            writer.write(String.valueOf(parser.output)); // Writes the parser output to the file
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        try {
            FileWriter writer_tokenList = new FileWriter(outputFileTokenList); // Creates a file writer for the token list output
            
            // Print tokens list
            for (Token token : tokenList) {
                writer_tokenList.write(token + "\n"); // Writes each token to the file
            }
            
            writer_tokenList.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------------//

/*
    public static void main(String[] args) {
        ArrayList<String> inputList = getInputList();
        File outputFileParser = new File("Output.txt");
        File outputFileTokenList = new File("OutputTokenList.txt");
        System.out.println(inputList);
        // creating scanner object for lexical analysis
        ArrayList<String> tokenMessages = new ArrayList<>();
        ArrayList<Token> tokenList = new ArrayList<>();
        LexicalChecker lexicalChecker = new LexicalChecker();
        lexicalChecker.addTokens(inputList, tokenMessages, tokenList);

        if (lexicalChecker.isLexicalError(tokenMessages)) {
            lexicalChecker.printResults(tokenMessages, outputFileParser);
            return;
        }
        
        Parser parser;
        parser = new Parser(tokenList);

        try {
            parser.parse();
        } catch (SyntaxErrorException e){
            System.out.println(e.getMessage());
        }
        try {
            FileWriter writer = new FileWriter(outputFileParser);
            writer.write(String.valueOf(parser.output));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        FileWriter writer_tokenList;
		try {
			writer_tokenList = new FileWriter(outputFileTokenList);
            // Print tokens list
			int last = tokenList.size() + 1;
            for (Token token : tokenList) {
            	writer_tokenList.write(token + "\n");
                System.out.print(token);
                if (tokenList.indexOf(token) != last) {
                	writer_tokenList.write("\n");
                    System.out.println();
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
*/
    //----------------------------------------------------------------------------------------------------//

 // Retrieves the input list from the user
    private static ArrayList<String> getInputList() {
        Scanner input = new Scanner(System.in); // Create a Scanner object for user input
        ArrayList<String> inputString;

        try {
            // Prompt the user to enter the file name
            System.out.print("Enter Input File Name (input.txt): ");
            String inputName = input.next();

            // Create a File object for input and output
            File inputFile = new File(inputName);

            // Read the contents of the input file
            Scanner reader = new Scanner(inputFile);
            inputString = new ArrayList<>();

            // Read each line of the input file and add it to the inputString list
            while (reader.hasNextLine()) {
                inputString.add(reader.nextLine());
            }

            reader.close(); // Close the reader after reading the file
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        input.close(); // Close the scanner object

        return inputString; // Return the inputString list
    }
}
//----------------------------------------------------------------------------------------------------//
 // Class defining token types used in the program
    class TokenType {
        public static final String LEFTPAR = "LEFTPAR";       // Represents the left parenthesis token
        public static final String RIGHTPAR = "RIGHTPAR";     // Represents the right parenthesis token
        public static final String DEFINE = "DEFINE";         // Represents the define token
        public static final String IDENTIFIER = "IDENTIFIER"; // Represents an identifier token
        public static final String NUMBER = "NUMBER";         // Represents a number token
        public static final String CHAR = "CHAR";             // Represents a character token
        public static final String BOOLEAN = "BOOLEAN";       // Represents a boolean token
        public static final String STRING = "STRING";         // Represents a string token
    }
    //----------------------------------------------------------------------------------------------------//

    // Class defining keywords used in the program
    class KEYWORDS {
        public static final String IF = "IF";         // Represents the if keyword
        public static final String LET = "LET";       // Represents the let keyword
        public static final String BEGIN = "BEGIN";   // Represents the begin keyword
        public static final String COND = "COND";     // Represents the cond keyword
    }
    //----------------------------------------------------------------------------------------------------//

    // Class defining grammar rules used in the program
    class GrammerRules {
        public static String program = "Program";                 // Represents the program rule
        public static String TopLevelForm = "TopLevelForm";       // Represents the top-level form rule
        public static String SecondLevelForm = "SecondLevelForm"; // Represents the second-level form rule
        public static String Definition = "Definition";           // Represents the definition rule
        public static String DefinitionRight = "DefinitionRight"; // Represents the right-hand side of a definition rule
        public static String ArgList = "ArgList";                 // Represents the argument list rule
        public static String Statements = "Statements";           // Represents the statements rule
        public static String Expressions = "Expressions";         // Represents the expressions rule
        public static String Expression = "Expression";           // Represents the expression rule
        public static String Expr = "Expr";                       // Represents the expr rule
        public static String FunCall = "FunCall";                 // Represents the function call rule
        public static String LetExpression = "LetExpression";     // Represents the let expression rule
        public static String LetExpr = "LetExpr";                 // Represents the let expression inside a let rule
        public static String VarDefs = "VarDefs";                 // Represents the variable definitions rule
        public static String VarDef = "VarDef";                   // Represents a single variable definition rule
        public static String CondExpression = "CondExpression";   // Represents the cond expression rule
        public static String CondBranches = "CondBranches";       // Represents the branches of a cond expression rule
        public static String CondBranch = "CondBranch";           // Represents a single branch in a cond expression rule
        public static String IfExpression = "IfExpression";       // Represents the if expression rule
        public static String EndExpression = "EndExpression";     // Represents the end expression rule
        public static String BeginExpression = "BeginExpression"; // Represents the begin expression rule
    }
    //----------------------------------------------------------------------------------------------------//