# CARD GAME
## _CARD GAME WITH 2 GAME TYPES: SINGLE AND DOUBLE_
demo: https://www.youtube.com/watch?v=xmC47ePi8eg&ab_channel=NurisArt

## Known limitations of my solution:
- Maximum number of players: 2
- Players must choose the same type of game.
- You need to restart the application if you want to play the game again.
- The data is reset after restarting the application.
## Key design decisions made:
- 3-layer architecture.
- CardGame interface is used for the pattern Factory Method.
- CardGame2Player interface is used for the pattern Template Method
- BillingService interface is used for the pattern Strategy
- CardGameSession class is used for initiating the game, following the game, storing state of players’ hands and encapsulated from external influences.
## Launch the solution:
- ./gradlew bootRun
 
- Add a client UI (command line or graphical)
- Handle player disconnects (consider allowing reconnecting to a running game and games timing out)

