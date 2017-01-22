package io.phineas.pogostatusmanager.highavailability;

import io.phineas.pogostatusmanager.APIv2;
import io.phineas.pogostatusmanager.Manager;

import java.util.TimerTask;

/**
 * Created by Phineas (phineas.io) on 23/07/2016
 */
public class UptimeWatcher extends TimerTask {
    public void run() {
        if(!Manager.getManager().pingHost("pokemongostatus.net", 80, 5000) || APIv2.getHTTPResponse() >= 500) {
            System.out.println("ERROR! pokemongostatus.net FAILED TO PING! Starting floating IP mitigation process...");

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            Manager.getManager().mitigateFloatingIP(Manager.CURRENT_DROPLET_ID);
                        }
                    },
                    2000
            );
            Manager.getManager().timerAction(false);
        }
    }

}
