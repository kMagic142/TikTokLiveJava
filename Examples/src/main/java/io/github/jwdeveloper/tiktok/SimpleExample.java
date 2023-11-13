/*
 * Copyright (c) 2023-2023 jwdeveloper jacekwoln@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jwdeveloper.tiktok;

import io.github.jwdeveloper.tiktok.exceptions.TikTokLiveOfflineHostException;
import io.github.jwdeveloper.tiktok.utils.ConsoleColors;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;

public class SimpleExample
{
    public static String TIKTOK_HOSTNAME = "bangbetmenygy";
    public static void main(String[] args) throws IOException {

        showLogo();
        // set tiktok username

        /*
        Optional checking if host name is correct
        if(TikTokLive.isHostNameValid(TIKTOK_HOSTNAME))
        {
            System.out.println("Live is online!");
        }

        Optional checking if live is online
        if(TikTokLive.isLiveOnline(TIKTOK_HOSTNAME))
        {
            System.out.println("Live is online!");
        }
        */

        TikTokLive.newClient(SimpleExample.TIKTOK_HOSTNAME)
                .configure(clientSettings ->
                {
                    clientSettings.setHostName(SimpleExample.TIKTOK_HOSTNAME); // This method is useful in case you want change hostname later
                    clientSettings.setClientLanguage("en"); // Language
                    clientSettings.setTimeout(Duration.ofSeconds(2)); // Connection timeout
                    clientSettings.setLogLevel(Level.ALL); // Log level
                    clientSettings.setPrintToConsole(true); // Printing all logs to console even if log level is Level.OFF
                    clientSettings.setRetryOnConnectionFailure(true); // Reconnecting if TikTok user is offline
                    clientSettings.setRetryConnectionTimeout(Duration.ofSeconds(1)); // Timeout before next reconnection

                    //Optional: Sometimes not every message from chat are send to TikTokLiveJava to fix this issue you can set sessionId
                    // documentation how to obtain sessionId https://github.com/isaackogan/TikTok-Live-Connector#send-chat-messages

                    // clientSettings.setSessionId("86c3c8bf4b17ebb2d74bb7fa66fd0000");

                    //Optional:
                    //RoomId can be used as an override if you're having issues with HostId.
                    //You can find it in the HTML for the livestream-page

                    //clientSettings.setRoomId("XXXXXXXXXXXXXXXXX");
                })
                .onGift((liveClient, event) ->
                {
                    switch (event.getGift()) {
                        case ROSE -> print(ConsoleColors.RED, "Rose!");
                        case GG -> print(ConsoleColors.YELLOW, " GOOD GAME!");
                        case TIKTOK -> print(ConsoleColors.CYAN,"Thanks for TikTok");
                        default -> print(ConsoleColors.GREEN, "[Thanks for gift] ", ConsoleColors.YELLOW, event.getGift().getName(), "x", event.getCombo());
                    }
                })
                .onGiftCombo((liveClient, event) ->
                {
                    print(ConsoleColors.RED,"GIFT COMBO",event.getGift().getName(),event.getCombo());
                })
                .onConnected((client, event) ->
                {
                    print(ConsoleColors.GREEN, "[Connected]");
                })
                .onDisconnected((liveClient, event) ->
                {
                    print(ConsoleColors.RED,"[Disconnected]");
                })
                .onRoomInfo((liveClient, event) ->
                {
                    var info = event.getRoomInfo();
                })
                .onFollow((liveClient, event) ->
                {
                    print(ConsoleColors.BLUE, "Follow:", ConsoleColors.WHITE_BRIGHT, event.getUser().getName());
                })
                .onJoin((client, event) ->
                {
                    print(ConsoleColors.WHITE, "Join:", ConsoleColors.WHITE_BRIGHT, event.getUser().getName());
                })
                .onComment((client, event) ->
                {
                    print(ConsoleColors.GREEN, event.getUser().getName(), ":", ConsoleColors.WHITE_BRIGHT, event.getText());
                })
                .onEvent((client, event) ->
                {
                    //System.out.println("Event: " +event.getClass().getSimpleName());
                })
                .onError((client, event) ->
                {
                    event.getException().printStackTrace();
                })
                .buildAndConnectAsync();
        System.in.read();
    }

    private static void print(Object... messages) {
        var sb = new StringBuilder();
        for (var message : messages) {
            sb.append(message).append(" ");
        }
        System.out.println(sb);
    }

    private static void showLogo()
    {
        System.out.println(ConsoleColors.GREEN+"""
                                
                 _____ _ _    _____     _    _     _          \s
                |_   _(_) | _|_   _|__ | | _| |   (_)_   _____\s
                  | | | | |/ / | |/ _ \\| |/ / |   | \\ \\ / / _ \\
                  | | | |   <  | | (_) |   <| |___| |\\ V /  __/ 
                  |_| |_|_|\\_\\ |_|\\___/|_|\\_\\_____|_| \\_/ \\___| 
                """);

    }
}
