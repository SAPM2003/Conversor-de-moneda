package lad.com.alura.conversormoneda;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;
import java.text.DecimalFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Conversor {

    private static final String API_KEY = "55d0afde8915272d96dbd97c";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");

        while (true) {
            System.out.println("\n=== CONVERSOR DE MONEDAS ===");
            System.out.println("1. USD → EUR");
            System.out.println("2. EUR → USD");
            System.out.println("3. USD → GBP");
            System.out.println("4. GBP → USD");
            System.out.println("5. USD → JPY");
            System.out.println("6. JPY → USD");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            if (opcion == 7) {
                System.out.println("Saliendo del programa");
                break;
            }

            String from = "";
            String to = "";

            switch (opcion) {
                case 1 -> { from = "USD"; to = "EUR"; }
                case 2 -> { from = "EUR"; to = "USD"; }
                case 3 -> { from = "USD"; to = "GBP"; }
                case 4 -> { from = "GBP"; to = "USD"; }
                case 5 -> { from = "USD"; to = "JPY"; }
                case 6 -> { from = "JPY"; to = "USD"; }
                default -> {
                    System.out.println("Opción inválida");
                    continue;
                }
            }


            scanner.nextLine();
            System.out.print("Ingrese la cantidad a convertir: ");
            String input = scanner.nextLine().replace(",", ".");
            double cantidad = Double.parseDouble(input);


            double tasa = obtenerTasa(from, to);

            double resultado = cantidad * tasa;

            System.out.println("Resultado: " + df.format(resultado) + " " + to);
        }

        scanner.close();
    }

    public static double obtenerTasa(String from, String to) throws Exception {
        String url = BASE_URL + API_KEY + "/latest/" + from;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send
                (request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(response.body(), JsonObject.class);

        return json.getAsJsonObject("conversion_rates").get(to).getAsDouble();
    }
}
