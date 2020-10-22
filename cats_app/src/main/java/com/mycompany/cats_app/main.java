package com.mycompany.cats_app;

import static com.mycompany.cats_app.CatServices.showCats;
import static com.mycompany.cats_app.CatServices.showFavorite;
import java.io.IOException;
import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) throws IOException {

        int opcion_menu = -1;
        String[] botones = {"1. ver gatos", "2. ver favoritos","3. salir"};

        do {
            //Menu principal
            String opcion = (String) JOptionPane.showInputDialog(null, "Java Cats", "Main Menu", JOptionPane.INFORMATION_MESSAGE,
                     null, botones, botones[0]);

            //Validamos que opcion selecciona el ususario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcion_menu = i;

                }
            }
            switch (opcion_menu) {
                case 0:
                    showCats();
                    break;
                case 1:
                    Cat cat = new Cat();
                    showFavorite(cat.getApi_key());
                default:
                    break;
            }
        } while (opcion_menu != 1);

    }
}