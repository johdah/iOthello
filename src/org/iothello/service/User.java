package org.iothello.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class User implements Serializable {
    private String name;
    private String IP;
    private int port;
    private List<String> privatechat = new ArrayList();
    private User challenger = null;
    private int playerStatus = 0;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    public User getChallenger() {
        return challenger;
    }

    public void setStatus(int playerStatus) {
        this.playerStatus = playerStatus;
    }

    public int getStatus() {
        return playerStatus;
    }
    
    public User(String IP, String name, int port) {
        this.name = name;
        this.IP = IP;
        this.port = port;

    }

    public String getIP() {
        return IP;
    }

    public String getName() {
        return name;
    }

    public void addPrivateMess(String from, String mess) {
        privatechat.add(from + ": " + mess);
    }

    public List getMessSince(int since) {
        List returnlist = new ArrayList();
        for (int i = since; i < privatechat.size(); i++) {
            returnlist.add(privatechat.get(i));
        }
        return returnlist;
    }

    void clearMessages() {
        privatechat.clear();


    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return (this.getName().equals(user.getName()));
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
