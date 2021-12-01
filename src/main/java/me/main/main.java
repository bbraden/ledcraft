/*
original code by:
@bbraden on github
braden#9999 on discord
11/29/2021
*/
package me.main;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import jssc.*;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static me.main.methods.*;
import static me.main.variables.*;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("[!] If port dosent work the first time, put a space before the port [!]");
        System.out.println("[+] input desired port");

        String userName = myObj.nextLine();  // Read user input
        System.out.println("[-] port: " + userName);  // Output user input
        myObj.close();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        com = new SerialPort(userName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            try {
                com.openPort();
            } catch (SerialPortException e) {
                com = new SerialPort(getEnvVariable("port"));
                System.out.println("[-] Port Invalid, Changed To Port Listed In src/main/resources/.env At Line 2");
                Thread.sleep(500);
                com.openPort();
            }
            Thread.sleep(1000);
            turnOn(com);
            Thread.sleep(1000);
            turnRed(com);
            Thread.sleep(500);
            turnGreen(com);
            Thread.sleep(500);
            turnBlue(com);
            Thread.sleep(500);
            turnGreen(com);
            Thread.sleep(1000);
            System.out.println("[-] port active");
        } catch (InterruptedException | SerialPortException e) {
            e.printStackTrace();
            System.out.println("=============\n[!] port invalid or was unable to open, please try again or check your code.");
            return;
        }

        state = true;
        power = true;
        walk = true;
        sight = true;
        click = true;
        brek = true;
        damage = true;
        death = true;
        respawn = true;

        Bukkit.getPluginManager().registerEvents(new listeners(this), this);
        Objects.requireNonNull(this.getCommand("led")).setExecutor(new commands());
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                ConnectionString connectionString = new ConnectionString("mongodb+srv://braden:1234@cluster0.w4snx.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();
                MongoClient mongoClient = MongoClients.create(settings);
                MongoDatabase database = mongoClient.getDatabase("chat");
                MongoCollection<Document> collection = database.getCollection("chat");
                ArrayList<Document> inserts = new ArrayList<>();
                FindIterable<Document> iterDoc = collection.find();
                while (true) {
                    for (Document document : iterDoc) {
                        if (document == null) {
                            return;
                        } else {
                            if (!inserts.contains(document)) {
                                inserts.add(document);
                                System.out.println(document.get("message").toString());
                                entry = document.get("message").toString();
                            }
                        }
                    }
                }
            }
        }, 0L, 10L);
    }
}
