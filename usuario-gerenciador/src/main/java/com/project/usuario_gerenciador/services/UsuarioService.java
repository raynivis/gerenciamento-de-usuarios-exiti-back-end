package com.project.usuario_gerenciador.services;

import com.project.usuario_gerenciador.models.Usuario;
import com.project.usuario_gerenciador.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Usuario> listAll(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        return usuarioRepository.findAllWithFilters(status, search, pageable);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario create(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario oldUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String novoEmail = usuario.getEmail();
        if (novoEmail != null && !novoEmail.equals(oldUsuario.getEmail())) {
            if (usuarioRepository.existsByEmail(novoEmail)) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            oldUsuario.setEmail(novoEmail);
        }

        if (usuario.getNome() != null) {
            oldUsuario.setNome(usuario.getNome());
        }


        return usuarioRepository.save(oldUsuario);
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    public Usuario toggleStatus(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if ("Ativo".equals(usuario.getStatus())) {
            usuario.setStatus("Inativo");
        } else {
            usuario.setStatus("Ativo");
        }
        
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public List<Usuario> importUsuarios(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> emailsSet = new HashSet<>();
        List<String> emailsDuplicates = new ArrayList<>();
        
        for (Usuario u : usuarios) {
            String email = u.getEmail();
            if (email != null && !email.trim().isEmpty()) {
                if (!emailsSet.add(email)) {
                    emailsDuplicates.add(email);
                }
            }
        }
        
        if (!emailsDuplicates.isEmpty()) {
            throw new IllegalArgumentException("Emails repetidos na lista de importação: " + emailsDuplicates);
        }

        List<String> emailsList = new ArrayList<>(emailsSet);
        
        if (!emailsList.isEmpty()) {
            List<Usuario> existingUsuarios = usuarioRepository.findByEmailIn(emailsList);
            if (!existingUsuarios.isEmpty()) {
                List<String> emailsFound = new ArrayList<>();
                for (Usuario u : existingUsuarios) {
                    emailsFound.add(u.getEmail());
                }
                throw new IllegalArgumentException("Emails já cadastrados no sistema: " + emailsFound);
            }
        }

        return usuarioRepository.saveAll(usuarios);
    }
}
