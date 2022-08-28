# ContainerFaker
Adds random loot to all containers in a world in a performant way

Example commands:
```
pools:
  food:
    BLAST_FURNACE:
      commands:
        as-player:
          - cmi warp spawn
        as-console:
          - give {player} minecraft:stone 5
```
