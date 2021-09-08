
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Stream.of("&a[ fSWLobby ] Plugin starting..."
                , "&a[ fSWLobby ] Author: " + getDescription().getAuthors().get(0)
                , "&a[ fSWLobby ] Version: " + getDescription().getVersion())
                .map(this::colorTranslate).forEach(getLogger()::info);

        final PlayerData playerData = new PlayerData(this);

        new PlayerInLobbyListener(this);
        new PlayerJoinListener(this, inLobbyPlayers, playerData);
      
        final RunnableManager runnableManager = new RunnableManager().manager(this, playerData);
        runnableManager.runTaskTimer(this, 1, 1);

        new GoldCommand(this, playerData, topGoldPlayersInventory);
        new GroupCommand(this, playerData);
        new LobbyCommand(this, runnableManager);
        new MinionCommand(this, minionsConfig);
        new PingCommand(this);
        this.onDisable = () -> {
            saveConfig();

            for (RunnableManager.PlayerDelay delay : RunnableManager.delays) {
                if (Objects.nonNull(delay))
                    delay.updateTicks();
            }
        };
    }
