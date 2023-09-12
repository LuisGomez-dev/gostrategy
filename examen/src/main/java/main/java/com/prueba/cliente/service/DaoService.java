package main.java.com.prueba.cliente.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import main.java.com.prueba.cliente.data.ClienteDAO;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DaoService {

    private static final String NOMBRE_ARCHIVO = "cliente.json";
    private List<ClienteDAO> dbCliente = new ArrayList<ClienteDAO>();


    
    public DaoService() {
        this.loadDataBase();
    }

    private void loadDataBase() {
        Gson gson = new Gson();

        StringBuilder contenido = new StringBuilder();

        File archivo = new File(NOMBRE_ARCHIVO);


        if (!archivo.exists()) {
            try {

                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return; // Salir si no se pudo crear el archivo
            }
        }


        try (Scanner scanner = new Scanner(new File(NOMBRE_ARCHIVO))) {
            while (scanner.hasNextLine()) {
                contenido.append(scanner.nextLine()).append("\n");
            }
           dbCliente = gson.fromJson(contenido.toString(), new TypeToken<List<ClienteDAO>>() {}.getType()) ;
            if(dbCliente == null){
                dbCliente = new ArrayList<ClienteDAO>();
            }
           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void saveDataBase() {
        Gson gson = new Gson();
        String json = gson.toJson(dbCliente);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO, false))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadDataBase();
    }

    private int obtenerNuevoId() {
        Optional<Integer> maxIdOptional = dbCliente.stream()
                .map(ClienteDAO::getId)
                .max(Integer::compare);

        int maxId = maxIdOptional.orElse(0);

        // Si no se encontró ningún id, inicializar en 1
        if (maxId == 0) {
            maxId = 1;
        } else {
            // Sumar uno al máximo id para crear un nuevo id único
            maxId++;
        }

        return maxId;
    }


    public List<ClienteDAO> todosLosClientes(){
        return dbCliente;
    }

    public ClienteDAO buscarClientePorId(int idBuscado) {
        return dbCliente.stream()
                .filter(cliente -> cliente.getId() == idBuscado)
                .findFirst()
                .orElse(null); // Devuelve null si no se encuentra ningún cliente con el ID buscado
    }

    public ClienteDAO eliminarClientePorId(int idBuscado) {
        ClienteDAO clienteEncontrado = buscarClientePorId(idBuscado);
        if(clienteEncontrado != null){
            dbCliente.remove(clienteEncontrado);
            saveDataBase();
        }
        return clienteEncontrado;
    }

    public void agregarCliente(ClienteDAO nuevoCliente) {
        nuevoCliente.setId(obtenerNuevoId()); 
        
        dbCliente.add(nuevoCliente);
            saveDataBase();
    }

    public ClienteDAO editarCliente(int idBuscado, ClienteDAO editaCliente) {
        ClienteDAO clienteEncontrado = buscarClientePorId(idBuscado);

        if(clienteEncontrado != null){
            dbCliente.remove(clienteEncontrado);
            editaCliente.setId(idBuscado);
            dbCliente.add(editaCliente);
            saveDataBase();
        }

        return editaCliente;
    }

}