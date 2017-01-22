package io.phineas.pogostatusmanager;

import io.phineas.pogostatusmanager.highavailability.UptimeWatcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;

public class Manager {

    //..
    public static String CURRENT_DROPLET_ID = APIv2.getDropletInfo("id", "default");
    public static String CURRENT_DROPLET_ID_PROXY = APIv2.getDropletInfo("id", "proxy");

    private static Manager manager = null;
    public static Manager getManager(){return manager;}

    public static void main(String[] args) {
        Manager.getManager().start();
    }

    public void start() {
        manager = this;

        long start = System.currentTimeMillis();
        System.out.println("Starting PokemonGO Status Manager v" + Version.LATEST.toString() + "...");
        Manager.getManager().timerAction(true);
        System.out.println("PokemonGO Status Manager is now running (" + String.valueOf(System.currentTimeMillis() - start) + "ms)");
    }

    public void timerAction(boolean a) {
        Timer timer = new Timer();
        if(a) {
            timer.schedule(new UptimeWatcher(), 2000, 5000);
            System.out.println("PGOStatus Manager> Enabling module 'Uptime Watcher'");
        } else {
            System.out.println("PGOStatus Manager> Disabling module 'Uptime Watcher'");
            timer.cancel();
            timer.purge();
        }
    }

    public void executeOSLevelCommand(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public void mitigateFloatingIP(String destinationDropletID) {
        System.out.println("PGOStatus Droplets> Assigning destination Droplet of floating IP " + APIv2.getDropletInfo("ip", "proxy") + "' to Droplet ID " + destinationDropletID);
        executeOSLevelCommand("doctl compute floating-ip-action assign " + APIv2.getDropletInfo("ip", "proxy") + " " + destinationDropletID);
        timerAction(true);
    }
}
