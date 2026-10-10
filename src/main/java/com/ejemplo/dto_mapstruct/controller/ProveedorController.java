package com.ejemplo.dto_mapstruct.controller;

import com.ejemplo.dto_mapstruct.dto.ProveedorDTO;
import com.ejemplo.dto_mapstruct.entity.Proveedor;
import com.ejemplo.dto_mapstruct.mapper.ProveedorMapper;
import com.ejemplo.dto_mapstruct.repository.ProveedorRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorRepository repository;
    private final ProveedorMapper mapper;

    public ProveedorController(ProveedorRepository repository, ProveedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ProveedorDTO> listar() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProveedorDTO crear(@RequestBody ProveedorDTO dto) {
        Proveedor proveedor = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(proveedor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long id, @RequestBody ProveedorDTO dto) {
        return repository.findById(id)
                .map(proveedor -> {
                    proveedor.setNombre(dto.getNombre());
                    proveedor.setTelefono(dto.getTelefono());
                    proveedor.setCorreo(dto.getCorreo());
                    return ResponseEntity.ok(mapper.toDTO(repository.save(proveedor)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}