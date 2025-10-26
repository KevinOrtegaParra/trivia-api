package com.trivia.api.services.impl;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.TokenVerifier;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class GoogleTokenVerifier {
     // 👉 tu Client ID de Google Cloud Console (OAuth 2.0)
    private static final String CLIENT_ID = "TU_CLIENT_ID_DE_GOOGLE";

    public JsonObject verify(String idTokenString) {
        try {
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(CLIENT_ID)
                    .build();

            verifier.verify(idTokenString);

            // Decodificar el token para obtener datos del usuario
            String[] parts = idTokenString.split("\\.");
            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            JsonObject payload = JsonParser.parseString(payloadJson).getAsJsonObject();

            return payload;

        } catch (VerificationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
