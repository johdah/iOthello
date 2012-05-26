package org.iothello.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class User implements Serializable {
	private static final long serialVersionUID = 2430471622661962697L;
	private String name;
    private String IP;
    private int port;
    private List<String> privatechat = new ArrayList<String>();
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

    public List<String> getMessSince(int since) {
        List<String> returnlist = new ArrayList<String>();
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
