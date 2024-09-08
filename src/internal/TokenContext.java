package internal;

// State of an in-progress token
public enum TokenContext {
    COLON_FC_HEADER,
    REL_OP_ASSIGN,
    REL_OP,
    NUMBER,
    STRING,
    NOTEQUAL,
    ID_KEYWORD
}
