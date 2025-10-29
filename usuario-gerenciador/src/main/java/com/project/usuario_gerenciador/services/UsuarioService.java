package com.project.usuario_gerenciador.services;

import com.project.usuario_gerenciador.models.Usuario;
import com.project.usuario_gerenciador.repositories.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Usuario> listAll(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
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
}
