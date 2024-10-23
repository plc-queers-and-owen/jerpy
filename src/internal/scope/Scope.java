package internal.scope;

import java.util.HashMap;

public class Scope {
    @SuppressWarnings("rawtypes")
    private final HashMap<String, SymbolItem> contents;

    @SuppressWarnings("rawtypes")
    public HashMap<String, SymbolItem> getContents() {
        return contents;
    }

    public Scope() {
        this.contents = new HashMap<>();
    }
}
