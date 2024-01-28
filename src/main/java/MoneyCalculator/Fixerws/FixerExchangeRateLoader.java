package MoneyCalculator.Fixerws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import MoneyCalculator.ExchangeRateLoader;
import MoneyCalculator.ExchangeRate;
import MoneyCalculator.Currency;

public class FixerExchangeRateLoader implements ExchangeRateLoader {

    LocalDate fecha = LocalDate.now();
    double tasa = 0.0;
    HttpURLConnection connection = null;
    @Override
    public ExchangeRate load(Currency from, Currency to) {
        String url = "";
        try {
            url = loadJson(from);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection(url, to);
        return new ExchangeRate(from, to, fecha, tasa);
    }

    public void Connection(String url, Currency to) {
        try {
            URL obj = new URL(url);
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                String dateObject = jsonObject.getAsJsonObject().get("date").getAsString();
                JsonObject ratesObject = jsonObject.getAsJsonObject("rates");

                tasa = ratesObject.get(to.code()).getAsDouble();
                fecha = LocalDate.parse(dateObject, DateTimeFormatter.ISO_DATE);

                in.close();
            } else {
                System.out.println("Error en la llamada a la API. Código de respuesta: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadJson(Currency from) throws IOException {
        String Mockurl = "https://api.apilayer.com/fixer/latest?"
                + "base=" + from.code()
                + "&apikey=" + FixerAPI.key;
        return Mockurl;
    }
}
