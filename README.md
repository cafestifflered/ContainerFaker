# ContainerFaker
Adds random loot to all containers in a world in a performant way

## Commands

- `/containerfaker clearcache`
Permission: containerfaker.command.clearcache

- `/containeroverride`
Permission: containerfaker.command.containeroverride

## Example Configuration:
```yml
inactive-regions:
  # Inactive regions take priority over active regions and region overrides
  - player_town1
  - player_town2
active-regions:
  - spawn
  - mines
  - `__global__`
region-overrides:
  houses:
    # This will override all BLAST_FURNACES in the HOUSES region with the WEAPONS pool.
    BLAST_FURANCE: "weapons"
    
# Set to -1 to disable this
expire-time: 10
expire-time-unit: MINUTES

randomize-durability: true
random-min-durability: 1
stackable-items:
  # Use vanilla item material
  - ARROW
  - FLINT
  - FEATHER
  - BONE
  - STICK
stack-minimum: 0
stack-maximum: 99

pools:
  food:
    BLAST_FURNACE:
      commands:
        as-player:
          - cmi warp spawn
        as-console:
          - give {player} minecraft:stone 5
    SMOKER: { }
    DROPPER: { }
    RED_SHULKER_BOX: { }
    FURNACE: { }
  armor:
    WHITE_SHULKER_BOX: { }
    LIGHT_BLUE_SHULKER_BOX: { }
    LOOM: { }
    MAGENTA_SHULKER_BOX: { }
    PURPLE_SHULKER_BOX: { }
  random:
    BARREL: { }
    CHEST: { }
    LIGHT_GRAY_SHULKER_BOX: { }
    GRAY_SHULKER_BOX: { }
    PINK_SHULKER_BOX: { }
    CHISELED_STONE_BRICKS: { }
    INFESTED_CHISELED_STONE_BRICKS: { }
  drinks_health:
    SHULKER_BOX: { }
    BLACK_SHULKER_BOX: { }
    BROWN_SHULKER_BOX: { }
    CYAN_SHULKER_BOX: { }
    GREEN_SHULKER_BOX: { }
  weapons:
    BLUE_SHULKER_BOX: { }
    YELLOW_SHULKER_BOX: { }
    OBSERVER: { }
    ORANGE_SHULKER_BOX: { }
    LIME_SHULKER_BOX: { }
  junk:
    FLETCHING_TABLE: { }
    DEAD_TUBE_CORAL: { }
    DEAD_HORN_CORAL: { }
    DEAD_BRAIN_CORAL: { }
    SMITHING_TABLE: { }
```
