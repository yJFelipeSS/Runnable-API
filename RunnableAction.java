package br.com.felipess.fswlobby.managers.runnable;

public interface RunnableAction {

    public void eachSecond();

    public void eachDelay();

    public void runnableEndAction();

    public void runnableEndCommand();
}
