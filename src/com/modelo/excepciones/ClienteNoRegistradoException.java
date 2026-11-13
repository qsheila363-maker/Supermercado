package com.modelo.excepciones;

public class ClienteNoRegistradoException extends Exception {
    public ClienteNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
