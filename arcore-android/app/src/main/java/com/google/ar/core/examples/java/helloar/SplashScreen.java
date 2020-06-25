/*=============================================
Copyright (c) 2018 echoAR. All Rights Reserved.
===============================================*/
package com.google.ar.core.examples.java.helloar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import xyz.echoar.android.CMS;
import xyz.echoar.data.Database;
import xyz.echoar.data.Entry;
import xyz.echoar.data.holograms.EchoHologram;
import xyz.echoar.data.holograms.Hologram;
import xyz.echoar.data.holograms.ModelHologram;
import xyz.echoar.data.holograms.VideoHologram;
import xyz.echoar.data.targets.BrickTarget;
import xyz.echoar.data.targets.GeolocationTarget;
import xyz.echoar.data.targets.ImageTarget;
import xyz.echoar.data.targets.Target;

public class SplashScreen extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Call super onCreate
        super.onCreate(savedInstanceState);

        // Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Make fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
            R.layout.splash_screen, null, false);
        addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT));

        // Check for offline mode
        if (!Config.OFFLINE_MODE) {

            // Inflate & customize key text
            final EditText keyText = (EditText) findViewById(R.id.keyText);
            keyText.setHint("Enter API key...");

            // Inflate & customize key button
            Button keyButton = (Button) findViewById(R.id.keyButton);
            keyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Connect to CMS/CDN
                        CMS.initialize(keyText.getText().toString(),
                                Entry.SDKs.ARCORE,
                                SplashScreen.this,
                                new Callable() {
                                    @Override
                                    public Object call() throws Exception {
                                        // Close splash screen
                                        ((SplashScreen) CMS.getContext()).closeSplash();
                                        return null;
                                    }
                                });
                    } catch (Exception e){
                        Toast.makeText(SplashScreen.this, "Key not found.", Toast.LENGTH_SHORT);
                    }
                }
            });
        } else {
            // Create and invoke post-splash handler
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    // Close splash screen
                    closeSplash();
                }

            }, Config.SPLASH_MILLIS);
        }
    }

    public void closeSplash(){

        // Initialize AR Camera Activity
        Intent intent = new Intent(SplashScreen.this, HelloArActivity.class);
        startActivity(intent);

        // Close this Splash Activity
        finish();

    }
}
