package org.iothello.logic.players.TD.Interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: Johan Dahlberg
 * Date: 2013-03-13
 * Time: 20:30
 */
public interface IEnvironment {
    public void init(Object[] arr);
    public State startTrial();
    public ActionResult step(Action action);
}
