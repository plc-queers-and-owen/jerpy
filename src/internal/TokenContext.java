package internal;

// State of an in-progress token
public enum TokenContext {
    COLON_FC_HEADER, // Either a single colon or a function call header (::)
    REL_OP_ASSIGN, // Either a rel-op or assignment
    REL_OP, // A rel-op, requires < or >
    NUMBER, // Number, requires extended parsing
    STRING, // String, requires extended parsing
    NOTEQUAL, // Not equal, requires !=
    ID_KEYWORD // Keyword or ID, requires extended parsing
}
