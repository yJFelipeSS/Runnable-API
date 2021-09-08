package br.com.felipess.fswlobby.managers.runnable;

import br.com.felipess.fswlobby.fSWLobby;
import br.com.felipess.fswlobby.sql.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RunnableManager extends BukkitRunnable {

    public static List<PlayerDelay> delays;
    private fSWLobby plugin;
    private PlayerData playerData;

    public static PlayerDelay newPlayerDelay() {
        return new PlayerDelay();
    }

    public RunnableManager manager(fSWLobby plugin, PlayerData playerData) {
        this.plugin = plugin;
        this.playerData = playerData;

        delays = new ArrayList<>();
        return this;
    }

    @Override
    public void run() {
        for (PlayerDelay delay : delays) {
            if (Objects.nonNull(delay))
                delay.updateTicks();
        }
    }

    public static final class PlayerDelay {

        private final List<RunnableAction> actions = new ArrayList<>();
        private Player player;
        private int start;
        private int current;
        private int delay;
        private int currentdelay;
        private int end;
        private RunnableManager manager;

        public PlayerDelay manager(RunnableManager manager) {
            this.manager = manager;
            return this;
        }

        public PlayerDelay player(Player player) {
            this.player = player;
            return this;
        }

        public PlayerDelay startInSeconds(int start) {
            this.start = start;
            this.current = start;
            return this;
        }

        public PlayerDelay delayInTicks(int delay) {
            this.delay = delay;
            this.currentdelay = delay;
            return this;
        }

        public PlayerDelay endInSeconds(int end) {
            this.end = end;
            return this;
        }

        public PlayerDelay putAction(RunnableAction action) {
            actions.add(action);
            return this;
        }

        public Player getPlayer() {
            return player;
        }

        public int getDelayTime() {
            return current;
        }

        public int getStartValue() {
            return start;
        }

        public int getDelayTicks() {
            return currentdelay;
        }

        public int getCurrentDelayTicks() {
            return currentdelay;
        }

        public void init() {
            manager.delays.add(this);
        }

        public void updateTicks() {
            currentdelay--;
            runActions();
            if (currentdelay == 0) {
                current--;
                currentdelay = delay;
            }
            if (current == end)
                end();
        }

        public void runActions() {
            actions.forEach(action -> {
                if (currentdelay == 0)
                    action.eachSecond();
                action.eachDelay();
                if (current == 0)
                    end();
            });
        }

        public void end() {
            actions.forEach(action -> {
                action.runnableEndAction();
                action.runnableEndCommand();
            });
            manager.delays.remove(this);
        }
    }
}
