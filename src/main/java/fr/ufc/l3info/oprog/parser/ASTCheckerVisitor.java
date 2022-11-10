package fr.ufc.l3info.oprog.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Visiteur réalisant des vérifications sur l'AST du fichier de stations.
 */
public class ASTCheckerVisitor implements ASTNodeVisitor {

    private final Map<String, ERROR_KIND> errors;
    private final Set<String> stations;

    public ASTCheckerVisitor() {
        this.errors = new HashMap<>();
        this.stations = new HashSet<>();
    }

    public Map<String, ERROR_KIND> getErrors() {
        return errors;
    }

    @Override
    public Object visit(ASTNode n) {
        return null;
    }

    @Override
    public Object visit(ASTListeStations n) {
        if (n.children.size() == 0) {
            errors.put(n.getLCPrefix() + " La liste de station est vide", ERROR_KIND.EMPTY_LIST);
        }
        for (ASTNode child : n) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ASTStation n) {
        return null;
    }

    @Override
    public Object visit(ASTDeclaration n) {
        return null;
    }

    @Override
    public Object visit(ASTChaine n) {
        return null;
    }

    @Override
    public Object visit(ASTIdentificateur n) {
        return null;
    }

    @Override
    public Object visit(ASTNombre n){
        return null;
    }
}

enum ERROR_KIND {
    EMPTY_LIST,
    EMPTY_STATION_NAME,
    DUPLICATE_STATION_NAME,
    MISSING_DECLARATION,
    DUPLICATE_DECLARATION,
    WRONG_NUMBER_VALUE
}