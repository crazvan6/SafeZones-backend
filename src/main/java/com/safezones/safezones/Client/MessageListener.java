package com.safezones.safezones.Client;


import com.safezones.safezones.Model.Message;

import java.util.ArrayList;

public interface MessageListener {
    void onMessageRecieve(Message message);
    void onActiveUsersUpdated(ArrayList<String> users);
}