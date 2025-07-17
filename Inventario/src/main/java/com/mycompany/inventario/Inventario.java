package com.mycompany.inventario;

import java.sql.Connection;
import util.Conexion;
import vista.*;
import modelo.*;
import javax.swing.*;

public class Inventario {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> SplashScreen.mostrar());
    }
}
