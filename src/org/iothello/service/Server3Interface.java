package org.iothello.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public interface Server3Interface extends Remote {
    public User login(String name, String password, String IP, int port) throws RemoteException;

    public boolean createUser(String name, String password) throws RemoteException;

    public List<User> getUserlist() throws RemoteException;

    public String sendPublicMessage(String message) throws RemoteException;

    public String sendPrivateMessage(String fromUser, String toUser, String message) throws RemoteException;

    public void clearPrivateMessages(String user) throws RemoteException;

    public List<String> getPublicChatMessagesSince(int messId) throws RemoteException;

    public List<String> getPrivateChatMessagesSince(String user, int messId) throws RemoteException;

    public void upDateScores(String player1, String player2, int p1points, int p2points) throws RemoteException;

    public boolean challengeUser(User challenger, User challengee) throws RemoteException;

    public void disconnect(String user) throws RemoteException;

    public User challenged(String user) throws RemoteException;

    public void setChallenger(User me, User user) throws RemoteException;

    public void setPlayerStatus(User me, int i) throws RemoteException;

    public int getPlayerStatus(User user) throws RemoteException;

    public void answerChallenge(User challenger, User me, boolean b) throws RemoteException;

    public ArrayList getHighscore() throws RemoteException;

    public ArrayList getRanking() throws RemoteException;
    
    public void reportIn(User user) throws RemoteException;
}
