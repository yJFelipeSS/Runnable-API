Optional<RunnableManager.PlayerDelay> delayOptional = RunnableManager.delays.stream().filter(delay -> delay.getPlayer() == player).findAny();
            if (delayOptional.isPresent()) {
                plugin.sendToLobby(player);

                player.sendMessage(plugin.colorTranslate("&aConnecting..."));
                delayOptional.get().end();
                return true;
            }
            RunnableManager.PlayerDelay delay = RunnableManager.newPlayerDelay()
                    .manager(runnableManager)
                    .player(player)
                    .startInSeconds(20)
                    .delayInTicks(20)
                    .endInSeconds(0);
            Player delayedPlayer = delay.getPlayer();
            delay.putAction(new RunnableAction() {
                @Override
                public void eachSecond() {
                    delayedPlayer.sendMessage("§a§lD E L A Y §aDelay de: §f"
                            + delayedPlayer.getName() + "§a. restam: §f"
                            + delay.getDelayTime()
                            + " §asegundos!");
                }

                @Override
                public void eachDelay() {
                    /* NOT YET */
                }

                @Override
                public void runnableEndAction() {
                    fSWLobby.inLobbyPlayers.remove(delayedPlayer);
                }

                @Override
                public void runnableEndCommand() {
                    /* NOT YET */
                }
            });
            delay.init();
            Stream.of("&cAre you sure you want to teleport to lobby?", "&cIf so, type /lobby again")
                    .map(plugin::colorTranslate).forEach(player::sendMessage);
            return true;
        }
