package esta.bf.sir.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // Liste en mémoire
    private List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));

    // GET - récupérer tous les noms
    @GetMapping
    public List<String> getAll() {
        return names;
    }

    // POST - ajouter un nouveau nom
    @PostMapping
    public String addName(@RequestBody String name) {
        names.add(name);
        return "Nom ajouté avec succès : " + name;
    }

    // PUT - modifier un nom existant par son index
    @PutMapping("/{index}")
    public String updateName(@PathVariable int index, @RequestBody String newName) {
        if(index >= 0 && index < names.size()) {
            String oldName = names.set(index, newName);
            return "Nom '" + oldName + "' remplacé par '" + newName + "'";
        } else {
            return "Index invalide";
        }
    }

    // DELETE - supprimer un nom par son index
    @DeleteMapping("/{index}")
    public String deleteName(@PathVariable int index) {
        if(index >= 0 && index < names.size()) {
            String removed = names.remove(index);
            return "Nom supprimé : " + removed;
        } else {
            return "Index invalide";
        }
    }
}