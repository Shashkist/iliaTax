

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import bittrex.BittrexParser2;
import org.json.JSONObject;

/**
 * Created by Iliap on 12/28/2018.
 */
public class Main {
    public static void main(String[] Args) throws IOException, ParseException {
        //perfromRequest();
        System.out.println("Hello World!");
        // HttpClient httpClient = new InternalHttp();
        String response ="";
        //Preprocessing to create 2017 prices of BTC.
        //BittrexParser2.dateIteratorCreateCSVBitcoinRateS();
    }

    private static void perfromRequest() throws IOException {
        URL url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=BTC&tsyms=USD&ts=1452680400");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
      //  Map<String, String> parameters = new HashMap<>();
        //parameters.put("param1", "val");

        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(false);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        JSONObject jsonObj = new JSONObject(content.toString());
        Double resultinUSD = null;
        try {
            if (jsonObj.has("BTC") && !jsonObj.isNull("BTC")) {
                // Do something with object.
                JSONObject btcPrice = (JSONObject) jsonObj.get("BTC");
                resultinUSD = btcPrice.getDouble("USD");
            }

        } catch (Exception e) {

        }
        if(resultinUSD != null) {
            System.out.print(resultinUSD);
        }

       // DataOutputStream out = new DataOutputStream(con.getOutputStream());
       // out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
       // out.flush();
        //out.close();
    }
}
