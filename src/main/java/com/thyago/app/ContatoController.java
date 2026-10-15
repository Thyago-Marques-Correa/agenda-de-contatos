package com.thyago.app;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContatoController {

    public static class Contato {
        private final long id;
        private final String nome;
        private final String telefone;
        private final String email;

        public Contato(long id, String nome, String telefone, String email) {
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;
            this.email = email;
        }

        public long getId() { return id; }
        public String getNome() { return nome; }
        public String getTelefone() { return telefone; }
        public String getEmail() { return email; }
    }

    private final List<Contato> contatos = new CopyOnWriteArrayList<>();
    private final AtomicLong sequencia = new AtomicLong();

    @GetMapping("/")
    public String listar(@RequestParam(required = false) String q, Model model) {
        List<Contato> lista = contatos;
        if (q != null && !q.isBlank()) {
            String termo = q.toLowerCase();
            lista = contatos.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(termo))
                    .toList();
        }
        model.addAttribute("contatos", lista);
        model.addAttribute("q", q);
        return "index";
    }

    @PostMapping("/add")
    public String adicionar(@RequestParam String nome, @RequestParam String telefone,
                            @RequestParam(defaultValue = "") String email) {
        if (!nome.isBlank()) {
            contatos.add(new Contato(sequencia.incrementAndGet(), nome.trim(), telefone.trim(), email.trim()));
        }
        return "redirect:/";
    }

    @PostMapping("/remove/{id}")
    public String remover(@PathVariable long id) {
        contatos.removeIf(c -> c.getId() == id);
        return "redirect:/";
    }
}
