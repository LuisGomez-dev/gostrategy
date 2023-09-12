package main.java.com.prueba.cliente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.prueba.cliente.data.ClienteDAO;
import main.java.com.prueba.cliente.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    private int ultimoId = 0;
    
    @PostMapping
    public ResponseEntity<ClienteDAO> crearCliente(@RequestBody ClienteDAO cliente) {
        clienteService.agregarCliente(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ClienteDAO>> obtenerClientes() {
        List<ClienteDAO> listaClientes = clienteService.todosLosClientes();
        return new ResponseEntity<>(listaClientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDAO> obtenerClientePorId(@PathVariable int id) {
        ClienteDAO cliente = clienteService.buscarClientePorId(id);
        if(cliente != null){
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDAO> actualizarCliente(@PathVariable int id, @RequestBody ClienteDAO nuevoCliente) {
        ClienteDAO cliente = clienteService.editarCliente(id, nuevoCliente);
        if(cliente != null){
            return new ResponseEntity<>(nuevoCliente, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable int id) {
        ClienteDAO  cliente = clienteService.eliminarClientePorId(id);
        if(cliente != null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
