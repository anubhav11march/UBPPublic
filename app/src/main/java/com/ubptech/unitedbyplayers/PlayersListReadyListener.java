package com.ubptech.unitedbyplayers;

import java.util.ArrayList;

/**
 * Created by Kylodroid on 26-06-2020.
 */
public  interface PlayersListReadyListener {
    void updatePlayersList(ArrayList<PlayerCardDetails> playerCardDetails);
}
