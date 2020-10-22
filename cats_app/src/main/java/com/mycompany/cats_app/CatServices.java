package com.mycompany.cats_app;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CatServices {

    public static void showCats() throws IOException {
        //Vamos a traer los datos de la Appi
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();
        //Hay que serializar el json en un obejto con Gson
        String elJson = response.body().string();

        //Cortar los corchetes del JSon
        System.out.println(elJson);
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length() - 1);
        //Crear un objeto de la clase Gson
        Gson gson = new Gson();
        Cat cats = gson.fromJson(elJson, Cat.class);

        //Redimencion de imagen en caso de necesitar
        Image image = null;
        try {
            URL url = new URL(cats.getUrl());

            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);

            if (fondoGato.getIconWidth() > 800) {
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String menu = "Opciones:\n"
                    + "1.Ver otra imagen\n"
                    + "2.Favoritos\n"
                    + "3.Volver\n";

            String[] botones = {"ver otra imagen", "favorito", "volver"};
            String id_gato = (cats.getUrl());
            String opcion = (String) JOptionPane.showInputDialog(null, menu, "Gallery", JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

            int seleccion = -1;
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }

            switch (seleccion) {
                case 0:
                    showCats();
                    break;
                case 1:
                    favoritoGato(cats);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public static void favoritoGato(Cat gato) {
        try {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\":\"" + gato.getId() + "\"\n}");
            Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApi_key()).build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static void showFavorite(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("x-api-key", apiKey)
                .build();

        Response response = client.newCall(request).execute();
        //Guardamos el string ocn la respouesta
        String elJson = response.body().string();

        //Creamos el objeto Gson
        Gson gson = new Gson();

        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);

        if (gatosArray.length > 0) {
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min;

            int indice = aleatorio - 1;

            GatosFav gatofav = gatosArray[indice];
            //Redimencion de imagen en caso de necesitar
            Image image = null;
            try {
                URL url = new URL(gatofav.getImage().getUrl());

                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if (fondoGato.getIconWidth() > 800) {
                    //redimensionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }

                String menu = "Opciones:\n"
                        + "1.Ver otra imagen\n"
                        + "2.Eliminar Favoritos\n"
                        + "3.Volver\n";

                String[] botones = {"ver otra imagen", "eliminar favorito", "volver"};
                String id_gato = (gatofav.getId());
                String opcion = (String) JOptionPane.showInputDialog(null, menu, "Gallery", JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

                int seleccion = -1;
                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])) {
                        seleccion = i;
                    }
                }

                switch (seleccion) {
                    case 0:
                        showFavorite(apiKey);
                        break;
                    case 1:
                        deleteFavorite(gatofav);
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                System.out.println(e);

            }
        }
    }

    public static void deleteFavorite(GatosFav gatofav) {

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites"+gatofav.getId()+"")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatofav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException ex) {
            Logger.getLogger(CatServices.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
