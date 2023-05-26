//----------------------------------------------------------------------------------------------------//
// Contributers:-
// - Muhammed Enes Gökdeniz (150121538)
// - Tolga Fehmioğlu (150120022)
// - Abdelrahman Zahran (150120998)
//----------------------------------------------------------------------------------------------------//
/** Token Class **/
//----------------------------------------------------------------------------------------------------//
class Token {
    private final String type; // Type of the token
    private final String value; // Value of the token
    private final int row; // Row position of the token
    private final int column; // Column position of the token

    public Token(String type, String value, int row, int column) { // Constructor for creating a Token object
        this.type = type; // Assigning the type of the token
        this.value = value; // Assigning the value of the token
        this.column = column; // Assigning the column position of the token
        this.row = row; // Assigning the row position of the token
    }

    public String getType() { // Getter for retrieving the type of the token
        return type;
    }

    public String getValue() { // Getter for retrieving the value of the token
        return value;
    }

    public int getRow() { // Getter for retrieving the row position of the token
        return row;
    }

    public int getColumn() { // Getter for retrieving the column position of the token
        return column;
    }

    @Override
    public String toString() { // Method for converting the Token object to a string representation
        return "TYPE: " + type + "\n" + "VALUE: " + value + "\n" + "ROW: " + row + " COLUMN: " + column + "\n" + "=========" + "\n";
    }
}
//----------------------------------------------------------------------------------------------------//